package models

typealias VertexId = Int
typealias Edge = Pair<VertexId, VertexId>
typealias Path = List<Edge>

data class Graph(val vertices: Map<VertexId, Point>, val edges: List<Edge>, val weights: Map<Edge, Double>, val adj: Map<VertexId, MutableList<VertexId>>) {
    fun getWeight(edge: Edge): Double = weights.getOrElse(edge, {weights[Edge(edge.second, edge.first)]!!})

}

data class GameState(
    val objects: List<Polygon>,
    val agents: List<Agent>,
    val width: Double,
    val height: Double,
    val timestep: Double,
) {
    fun update() {
        agents.forEach { it.update(this) }
    }

    val graph: Graph = generateGraph()

    private fun generateGraph(): Graph {
        var vertexCount = 0
        val vertices = mutableMapOf<VertexId, Point>()
        for (polygon in objects) {
            for (point in polygon.points) {
                vertices.put(vertexCount, point)
                vertexCount++
            }
        }

        val verticesCopy = vertices.toList().toMutableList()
        val edges = mutableListOf<Edge>()
        val weights = mutableMapOf<Edge, Double>()
        val adj = mutableMapOf<VertexId, MutableList<VertexId>>()
        while (verticesCopy.isNotEmpty()) {
            val curVertex = verticesCopy.removeLast()
            for (otherVertex in verticesCopy) {
                if (!lineIntersectsObjects(curVertex.second, otherVertex.second, objects)) {
                    val newEdge = Pair(curVertex.first, otherVertex.first)
                    edges.addLast(newEdge)
                    if (!adj.contains(curVertex.first)) {
                        adj.put(curVertex.first, mutableListOf())
                    }
                    adj[curVertex.first]!!.addLast(otherVertex.first)
                    if (!adj.contains(otherVertex.first)) {
                        adj.put(otherVertex.first, mutableListOf())
                    }
                    adj[otherVertex.first]!!.addLast(curVertex.first)
                    weights[newEdge] = (curVertex.second - otherVertex.second).magnitude()
                }
            }
        }

        return Graph(vertices, edges, weights, adj)
    }
}
