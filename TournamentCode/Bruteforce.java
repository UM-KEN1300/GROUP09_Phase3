public class Bruteforce {
	public static long startTime;
	public static int timeLimit;
	// n = number of vertices;
	// v = current vertex
	// m = number of colors to try
	// int [] q = array of colors used for the vertices
	// int input[][] = adjacency matrix

	//colors graph using minimum number of colors, returns chromatic number
	static int run(int n, int m, int[][] adj, int timeLimitParam){
		startTime = System.nanoTime();
		timeLimit = timeLimitParam;
		int[] colors= new int[n];
		int [][] adj_matrix= new int[n][n];
		adj_matrix = adj;
		for(int i = 1; i <= n; i++) { // colors from 1 to n
			int temp = Colorings(0,i,colors,n,adj_matrix);
			if(temp == 1){ // if graph can be colored using i colors starting at vertex 0
				return i;
			}
			else if(temp == -1) {
				return -1;
			}
		}
		return 1; // program will never get to this line
	}

	//colors graph using m colors starting at vertex v
	static int Colorings(int v, int m,int [] colors,int n,int [][]adj_matrix) {
		double elapsedTime = (System.nanoTime() - startTime) / 1000000000.0;
		if(v > n-1) { //if all vertices have been colored
			return 1;
		}
		else {
			if(colors[v] == 0) {
				for(int i = 1; i <= m; i++) {
					boolean match = false;
					colors[v] = i; //assign color i to vertex v
					for(int j = 0; j < n; j++) {
						if(adj_matrix[v][j] == 1) { // if the current vertex is adjacient with all other vertices
							if(i == colors[j]) {// if it is, check if they have the same color
								match = true;
								break;
							}
						}
					}
					if(match == false) { // if they are not adjacient
						if(Colorings(v+1,m,colors,n,adj_matrix) == 1) // use recursion for the next vertex
						return 1;
					}
				}
				colors[v] = 0; //if v is close to another vertex with the same color, assign color 0 to vertex v and fail
				return 0;
			}
			return Colorings(v+1, m, colors, n, adj_matrix);
		}
	}
}
