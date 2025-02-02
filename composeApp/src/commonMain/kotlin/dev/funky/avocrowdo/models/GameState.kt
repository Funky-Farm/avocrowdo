package models

typealias VertexId = Int
typealias Edge = Pair<VertexId, VertexId>

data class Graph(val vertices: Map<VertexId, Point>, val edges: List<Edge>)

data class GameState(
    val objects: List<Polygon>,
    val agents: List<Agent>,
    val width: Double,
    val height: Double,
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

        val verticesCopy = vertices.toList()
        while (verticesCopy.isNotEmpty()) {

        }

        TODO()
    }
}
