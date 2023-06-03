
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.stream.Collectors;

//-----------------------------------------------------
//Title: Main Class
//Author: Ayda Nil Özyürek
//Description: This class does the following:
//			   After reading the pair of inputs (X, Y), the program creates an undirected graph 
//             with N islands and M edges and finds all the routes from X to Y and all the routes from Y to X. 
//             These lines are then combined to create all possible round-trip routes from X to Y and back to X. 
//             The shortest round-trip path is then output as a list of nodes after the round-trip paths have been sorted by size (number of nodes).
//------------------------------------------------
public class Main {

	static int N; // the number of islands
	static int M; // number of connections
	static int X; // the island we start tour
	static int Y; // the island tour should include

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		N = sc.nextInt(); // the number of islands
		M = sc.nextInt(); // number of connections

		Graph islandsGraph = new Graph(N);

		// Read M pairs of integers to define edges
		for (int i = 0; i < M; i++) {
			int u = sc.nextInt();
			if (u > N || u < 1) {
				System.out.println(N);
				return;
			}
			u = u - 1; // Since array indexing starts from 0 we subtract 1 from each vertex number

			int v = sc.nextInt();
			if (v > N || v < 1) {
				System.out.println(N);
				return;
			}
			v = v - 1; // Since array indexing starts from 0 we subtract 1 from each vertex number

			// A pair of u,v indicates that there is a path from u to v.
			// Since our graph should be bidirectional,
			// we must also add the reverse direction from v to u
			islandsGraph.addEdge(u, v);
			islandsGraph.addEdge(v, u);
		}

		// Read the last pair of integers X,Y
		X = sc.nextInt(); // the island we start the tour
		if (X > N || X < 1) {
			System.out.println(N);
			return;
		}

		Y = sc.nextInt(); // the island the tour should include
		if (Y > N || Y < 1) {
			System.out.println(N);
			return;
		}

		// Calculate all paths from X to Y (forward path)
		islandsGraph.FindAllPaths(X - 1, Y - 1);
		List<List<Integer>> forwardPathList = islandsGraph.getPathList();

		// Calculate all paths from Y to X (return path to the beginning)
		islandsGraph.FindAllPaths(Y - 1, X - 1);
		List<List<Integer>> returnPathList = islandsGraph.getPathList();

		// Combine forward and return paths (we get all round-trip path combinations
		// with full paths from start to destination and then back to the starting point
		// again)
		List<List<Integer>> roundTripPathList = new ArrayList<List<Integer>>();

		for (int i = 0; i < forwardPathList.size(); i++) {
			for (int j = 0; j < returnPathList.size(); j++) {

				List<Integer> leftPath = forwardPathList.get(i);
				List<Integer> rightPath = returnPathList.get(j);

				// we don't prefer return paths which contain cities that we already visited
				boolean found = false;
				for (int k = 1; k < rightPath.size() - 1; k++) {
					if (leftPath.contains(rightPath.get(k))) {
						found = true;
						break;
					}
				}

				if (!found) {
					List<Integer> path = new ArrayList<Integer>();
					path.addAll(leftPath);
					path.addAll(rightPath.subList(1, rightPath.size()));
					roundTripPathList.add(path);
				}
			}
		}

		// If we only have single route directly from source to destination, we must
		// keep it,
		// otherwise we prefer different return routes.
		if (roundTripPathList.size() > 1) {
			for (int i = 0; i < roundTripPathList.size(); i++) {
				if (roundTripPathList.get(i).size() == 3) { // if the round-trip path size equals to 3, this indicates
															// "source-destination-source" route
					roundTripPathList.remove(i);
					i = i - 1;
				}
			}
		}

		// Now we convert all round-trip paths from list of ArrayLists to list of
		// TreeSets.
		// TreeSets keep natural orderings of elements and doesn't allow duplicate
		// values.
		List<TreeSet<String>> roundTripPathListUniqueNumbers = new ArrayList<TreeSet<String>>();

        for (int i = 0; i < roundTripPathList.size(); i++) {
            TreeSet<String> pathWithUniqueNodes = new TreeSet<String>();
            List<Integer> activePath =  roundTripPathList.get(i);
            for (int j = 0; j < activePath.size(); j++) {
                pathWithUniqueNodes.add(activePath.get(j).toString());
            }
            
            roundTripPathListUniqueNumbers.add(pathWithUniqueNodes);
        }

        //Now we sort the path list by their path sizes. After sorting, first element of the list will contain the shortest path.
        List<TreeSet<String>> roundTripPathListUniqueNumbersSorted = new ArrayList<TreeSet<String>>();

        roundTripPathListUniqueNumbersSorted = (List<TreeSet<String>>) sortListBySize(roundTripPathListUniqueNumbers);

		System.out.println(roundTripPathListUniqueNumbersSorted.get(0).toString().replace(",", "").replace("[", "")
				.replace("]", ""));
	}

	// A utility function to sort a path list by their path sizes. After sorting,
	// first element of the list will contain the shortest path.
	@SuppressWarnings("unchecked")
	public static <T> List<? extends NavigableSet<T>> sortListBySize(List<? extends NavigableSet<T>> list) {

		Collections.sort(list, new Comparator<NavigableSet<T>>() {

			@Override
			public int compare(NavigableSet<T> o1, NavigableSet<T> o2) {
				return Integer.valueOf(o1.size()).compareTo(o2.size());
			}
		});

		return list;
	}
}
