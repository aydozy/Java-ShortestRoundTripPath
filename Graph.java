import java.util.ArrayList;
import java.util.List;

//-----------------------------------------------------
//Title: Graph Class
//Author: Ayda Nil Özyürek
//Description: This class does the following: 
//             provides a method to find all paths between a given source 
//             and destination vertex using Depth-first-search (DFS) algorithm.		
//------------------------------------------------
public class Graph {
	// Number of vertices (islands) in graph
	private final int V;

	private List<Integer> path;

	private List<List<Integer>> pathList;

	private List<List<Integer>> adjacencyList;

	public List<List<Integer>> getPathList() {
		return pathList;
	}

	// Constructor. Creates a graph with v vertices having no edges yet
	public Graph(int v) {
		// initialize vertex count
		this.V = v;

		// initialize adjacency list
		initAdjacencyList();
	}

	@SuppressWarnings("unchecked")
	private void initAdjacencyList() {
		adjacencyList = new ArrayList<List<Integer>>();

		for (int i = 0; i < V; i++) {
			List<Integer> node = new ArrayList<Integer>();
			adjacencyList.add(node);
		}
	}

	// Add an edge between u and v
	public void addEdge(int u, int v) {
		// Add v to u's list.
		adjacencyList.get(u).add(v);
	}

	public void FindAllPaths(int src, int dst) {
		path = new ArrayList<Integer>();
		pathList = new ArrayList<List<Integer>>();

		path.add(src);

		DFS(adjacencyList, src, dst, path);
	}

	// Depth-first-search algorithm to find a route frpm src to dst
	public void DFS(List<List<Integer>> graph, int src, int dst, List<Integer> path) {
		if (src == dst) {   // this means we reached to the destination. Now we can the path to the path
							// list.
			List<Integer> pathToSave = new ArrayList<Integer>();
			pathToSave.addAll(path);

			for (int i = 0; i < pathToSave.size(); i++) {
				pathToSave.set(i, pathToSave.get(i) + 1);
			}

			pathList.add(pathToSave);
		} else {    // here we haven't reached to the destination yet. We must recursively continue
					// with unvisited nodes.
			for (Integer adjnode : graph.get(src)) {
				if (!path.contains(adjnode)) {
					path.add(adjnode);
					DFS(graph, adjnode, dst, path);
					path.remove(path.size() - 1);
				}
			}
		}
	}
}
