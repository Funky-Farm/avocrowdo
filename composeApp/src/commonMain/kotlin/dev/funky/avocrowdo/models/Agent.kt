package models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import java.util.*
import kotlin.math.pow
import kotlin.random.Random

const val INITIAL_PRIORITY_QUEUE_SIZE = 16

/**
 * An entity that moves around in a space.
 */
interface Agent {
    /** The agent's size, which is used to determine its collision radius. */
    val size: Int

    /** The agent's current position. */
    var position: Point

    /** The agent's current velocity. */
    var velocity: Point

    /** Update the agent's position and velocity, given an observation of the game. */
    fun update(state: GameState)

    /** Execute the agent's movement, given a velocity. This function accounts for collisions. */
    fun execute(state: GameState, velocity: Point) {
        // Update the agent's position. If the agent "bumps into" anything, it should go as far as possible to just
        // about touch the object.
        val (xCollide, yCollide) = state.objects.map { obj ->
            val xCollide = obj.contains(Point(position.x + velocity.x, position.y), size)
            val yCollide = obj.contains(Point(position.x, position.y + velocity.y), size)
            Pair(xCollide, yCollide)
        }.reduce { acc, pair -> Pair(acc.first || pair.first, acc.second || pair.second) }.let {
            if (it.first || it.second) return@let Pair(it.first, it.second)

            return@let state.agents.map { agent ->
                val xCollide = agent.position != position
                    && agent.collides(Point(position.x + velocity.x, position.y), size)
                val yCollide = agent.position != position
                    && agent.collides(Point(position.x, position.y + velocity.y), size)
                Pair(xCollide, yCollide)
            }.reduce { acc, pair -> Pair(acc.first || pair.first, acc.second || pair.second) }
        }

        if (!xCollide) position.x += velocity.x
        if (!yCollide) position.y += velocity.y
    }

    /** Draw the agent in a particular scope. */
    fun draw(scope: DrawScope) {
        scope.drawCircle(
            color = Color.Black,
            style = Fill,
            radius = size.toFloat(),
            center = position.toOffset()
        )
    }

    /** Check if this agent collides with some other agent at [point] with radius [radius]. */
    fun collides(point: Point, radius: Int): Boolean {
        return (point - position).magnitude() < size + radius
    }
}

/**
 * An agent which behaves completely randomly.
 */
data class RandomAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double,
) : Agent {
    override fun update(state: GameState) {
        val xSpeed = Random.nextDouble(0.0, maxVelocity)
        val ySpeed = (maxVelocity.pow(2) - xSpeed.pow(2)).pow(0.5)
        velocity = Point(xSpeed, ySpeed)
        this.execute(state, velocity)
    }
}

data class SimpleAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double,
    val target: Point,
) : Agent {
    override fun update(state: GameState) {
        val distance: Point = target - position
        velocity = (distance / distance.magnitude()) * maxVelocity
        this.execute(state, velocity)
    }
}

data class PathFindingAgent(
    override val size: Int,
    override var position: Point,
    override var velocity: Point,
    val maxVelocity: Double,
    var target: Point,
) : Agent {
    override fun update(state: GameState) {
        val globalGraph: Graph = state.graph
        val vertices: MutableMap<VertexId, Point> = globalGraph.vertices.toMutableMap()
        val edges: MutableList<Edge> = globalGraph.edges.toMutableList()
        val adj: MutableMap<VertexId, MutableList<VertexId>> = globalGraph.adj.toMutableMap()
        val weights: MutableMap<Edge, Double> = globalGraph.weights.toMutableMap()

        vertices.put(-1, position)
        vertices.put(-2, target)

        // check all edges from start for intersection and add
        for (otherVertex in globalGraph.vertices) {
            if (!lineIntersectsObjects(position, otherVertex.value, state.objects)) {
                val newEdge = Pair(-1, otherVertex.key)
                edges.addLast(newEdge)
                if (!adj.contains(-1)) {
                    adj.put(-1, mutableListOf())
                }
                adj[-1]!!.addLast(otherVertex.key)
                if (!adj.contains(otherVertex.key)) {
                    adj.put(otherVertex.key, mutableListOf())
                }
                adj[otherVertex.key]!!.addLast(-1)
                weights[newEdge] = (position - otherVertex.value).magnitude()
            }
        }

        // check all edges from end for intersection and add
        for (otherVertex in globalGraph.vertices) {
            if (!lineIntersectsObjects(target, otherVertex.value, state.objects)) {
                val newEdge = Pair(-2, otherVertex.key)
                edges.addLast(newEdge)
                if (!adj.contains(-2)) {
                    adj.put(-2, mutableListOf())
                }
                adj[-2]!!.addLast(otherVertex.key)
                if (!adj.contains(otherVertex.key)) {
                    adj.put(otherVertex.key, mutableListOf())
                }
                adj[otherVertex.key]!!.addLast(-2)
                weights[newEdge] = (target - otherVertex.value).magnitude()
            }
        }

        // check edge between start and end
        if (!lineIntersectsObjects(position, target, state.objects)) {
            val newEdge = Pair(-1, -2)
            edges.addLast(newEdge)
            if (!adj.contains(-1)) {
                adj.put(-1, mutableListOf())
            }
            adj[-1]!!.addLast(-2)
            if (!adj.contains(-2)) {
                adj.put(-2, mutableListOf())
            }
            adj[-2]!!.addLast(-1)
            weights[newEdge] = (position - target).magnitude()
        }


        val agentGraph: Graph = Graph(vertices, edges, weights, adj)
        val agentPath: Path = aStar(-1, -2, agentGraph)

        val firstEdge = agentPath.first()
        val xDistance = vertices[firstEdge.second]!!.x - vertices[firstEdge.first]!!.x
        val yDistance = vertices[firstEdge.second]!!.y - vertices[firstEdge.first]!!.y
        velocity = Point(xDistance, yDistance)
        velocity = (velocity / velocity.magnitude()) * maxVelocity

        this.execute(state, velocity)
    }
}

fun aStar(startId: VertexId, targetId: VertexId, graph: Graph): Path {
    // Calculate heuristic as distance to target.
    val heuristic: MutableMap<VertexId, Double> = mutableMapOf()
    val parent: MutableMap<VertexId, VertexId> = mutableMapOf()
    val pathWeightFromStart: MutableMap<VertexId, Double> = mutableMapOf()
    val totalEstimateWeight: MutableMap<VertexId, Double> = mutableMapOf()
    val tree: MutableSet<VertexId> = mutableSetOf()
    val fringe: PriorityQueue<VertexId> =
        PriorityQueue(INITIAL_PRIORITY_QUEUE_SIZE, compareBy { totalEstimateWeight[it] })

    val targetPoint: Point = graph.vertices[targetId]!!

    // Initialise heuristic
    for ((vertexId, point) in graph.vertices) {
        heuristic[vertexId] = (point - targetPoint).magnitude()
        heuristic[targetId] = 0.0
        heuristic[startId] = Double.MAX_VALUE
    }

    // Initialise initial tree and fringe.
    tree.add(startId)
    for (vertexId in graph.adj[startId]!!) {
        pathWeightFromStart[vertexId] = graph.getWeight(Edge(startId, vertexId))
        totalEstimateWeight[vertexId] = pathWeightFromStart[vertexId]!! + heuristic[vertexId]!!
        fringe.add(vertexId)
        parent[vertexId] = startId
    }

    while (!tree.contains(targetId)) {
        val nextTreeNode = fringe.poll()
        tree.add(nextTreeNode)
        for (vertexId in graph.adj[nextTreeNode]!!.filter { !tree.contains(it) }) {
            val newWeightFromStart = pathWeightFromStart[nextTreeNode]!! + graph.getWeight(Edge(nextTreeNode, vertexId))
            if (!fringe.contains(vertexId)) {
                pathWeightFromStart[vertexId] = newWeightFromStart
                totalEstimateWeight[vertexId] = newWeightFromStart + heuristic[vertexId]!!
                fringe.add(vertexId)
                parent[vertexId] = nextTreeNode
            } else if (newWeightFromStart < pathWeightFromStart[vertexId]!!) {
                pathWeightFromStart[vertexId] = newWeightFromStart
                totalEstimateWeight[vertexId] = newWeightFromStart + heuristic[vertexId]!!
                parent[vertexId] = nextTreeNode
            }
        }
    }

    var pathVertex = targetId
    val path: MutableList<Edge> = mutableListOf()
    while (pathVertex != startId) {
        val p = parent[pathVertex]!!
        path.addFirst(Edge(p, pathVertex))
        pathVertex = p
    }

    return path
}

data class AgentConsumer(
    val interval: Double,
    val position: Point,
) {
    fun update() {
//        for (agent in gameState.agents) {
//            if (agent.position == this.position) {
//                agents.remove(agent)
//            }
//        }
    }
}

// (personId, pathId1) (pathId1, pathId2) ... personId.pos ≈ pathId1.pos
// (personId, pathId2) ... personId.pos ≈ pathId1.pos