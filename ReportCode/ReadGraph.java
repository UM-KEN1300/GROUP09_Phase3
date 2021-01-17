import java.io.*;
import java.util.*;

class ColEdge
{
	int u;
	int v;
}

public class ReadGraph
{

	public final static boolean DEBUG = false;

	public final static String COMMENT = "//";

	public static int bestUpperBound;

	public static int chromaticNumber;

	public static void main( String args[] )
	{
		if( args.length < 1 )
		{
			System.out.println("Error! No filename specified.");
			System.exit(0);
		}


		String inputfile = args[0];

		boolean seen[] = null;

		//! n is the number of vertices in the graph
		int n = -1;

		//! m is the number of edges in the graph
		int m = -1;

		//! e will contain the edges of the graph
		ColEdge e[] = null;

		try 	{
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);

			String record = new String();

			//! THe first few lines of the file are allowed to be comments, staring with a // symbol.
			//! These comments are only allowed at the top of the file.

			//! -----------------------------------------
			while ((record = br.readLine()) != null)
			{
				if( record.startsWith("//") ) continue;
				break; // Saw a line that did not start with a comment -- time to start reading the data in!
			}

			if( record.startsWith("VERTICES = ") )
			{
				n = Integer.parseInt( record.substring(11) );
				if(DEBUG) System.out.println(COMMENT + " Number of vertices = "+n);
			}

			seen = new boolean[n+1];

			record = br.readLine();

			if( record.startsWith("EDGES = ") )
			{
				m = Integer.parseInt( record.substring(8) );
				if(DEBUG) System.out.println(COMMENT + " Expected number of edges = "+m);
			}

			e = new ColEdge[m];

			for( int d=0; d<m; d++)
			{
				if(DEBUG) System.out.println(COMMENT + " Reading edge "+(d+1));
				record = br.readLine();
				String data[] = record.split(" ");
				if( data.length != 2 )
				{
					System.out.println("Error! Malformed edge line: "+record);
					System.exit(0);
				}
				e[d] = new ColEdge();

				e[d].u = Integer.parseInt(data[0]);
				e[d].v = Integer.parseInt(data[1]);

				seen[ e[d].u ] = true;
				seen[ e[d].v ] = true;

				if(DEBUG) System.out.println(COMMENT + " Edge: "+ e[d].u +" "+e[d].v);

			}

			String surplus = br.readLine();
			if( surplus != null )
			{
				if( surplus.length() >= 2 ) if(DEBUG) System.out.println(COMMENT + " Warning: there appeared to be data in your file after the last edge: '"+surplus+"'");
			}

		}
		catch (IOException ex)
		{
			// catch possible io errors from readLine()
			System.out.println("Error! Problem reading file "+inputfile);
			System.exit(0);
		}

		for( int x=1; x<=n; x++ )
		{
			if( seen[x] == false )
			{
				if(DEBUG) System.out.println(COMMENT + " Warning: vertex "+x+" didn't appear in any edge : it will be considered a disconnected vertex on its own.");
			}
		}

		//! At this point e[0] will be the first edge, with e[0].u referring to one endpoint and e[0].v to the other
		//! e[1] will be the second edge...
		//! (and so on)
		//! e[m-1] will be the last edge
		//!
		//! there will be n vertices in the graph, numbered 1 to n

		//Get the time when the program starts evaluating the graph
		long programStart = System.nanoTime();
		//And start getting some information about the graph!
		int[][] adj_matrix = HelperFunctions.getAdjacencyMatrix(e, m, n);
		int chromaticNumber = 0;
		//Check if it is complete...
		int completenessCheck = HelperFunctions.checkIfComplete(m, n);
		//If it is, we know the chromatic numberand can stop!
		if(completenessCheck != -1) {
			chromaticNumber = completenessCheck;
			System.out.println("This is a complete graph with " + completenessCheck + " nodes, therefore its chromatic number is " + completenessCheck);
			stop(programStart);
		}
		System.out.println("Not complete!");
		//Check if it is bipartite...
		System.out.println("Starting bipartite check...");
		long start = System.nanoTime();
		boolean isBipartite = BipartiteCheck.run(adj_matrix);
		double durationBipartite = (System.nanoTime() - start) / 1000000.0;
		//If it is, we know the chromatic number and can stop!
		if(isBipartite) {
			chromaticNumber = 2;
			System.out.println("This graph is bipartite, therefore its chromatic number must be 2!");
			System.out.println("Bipartition check took " + durationBipartite + "ms.");
			stop(programStart);
		}
		System.out.println("Graph is not bipartite!");
		//If it is neither bipartite nor complete, then we can start getting some more information and reducing it!
		Graph g = new Graph(e, m, n);
		ArrayList<Vertex> vertices = g.getVertices();
		//Get the maximal clique using Bron-Kerbosch with Tomita pivot selection
		System.out.println("Running Bron-Kerbosch with Tomita pivoting...");
		start = System.nanoTime();
		ArrayList<Vertex> largestClique = BronKerbosch.run(vertices);
		double duration = System.nanoTime() - start / 1000000.0;
		System.out.println("Bron-Kerbosch with Tomita pivoting took: " + duration);
		int lowerBound = largestClique.size();
		System.out.println("Result from Bron-Kerbosch: " + lowerBound);
		//Graph reduction (removing disconnected vertices and reducing the graph basedon procedure 1 and 2 mentioned in the report)
		adj_matrix = Reduce.run(largestClique, vertices, false);
		System.out.println("Reduced the graph by " + (n-adj_matrix.length) + " vertices. It now has " + adj_matrix.length + " vertices");
		int[] degrees = HelperFunctions.getDegrees(adj_matrix);
		m = HelperFunctions.getUpdatedEdgeCount(adj_matrix);
		//Get a partial coloring where the maximal clique is already colored
		int[] partialColoring = HelperFunctions.colorClique(largestClique, adj_matrix.length, m);
		//Special case: If the graph is complete after reduction...
		if(partialColoring.length == 0) {
			//...we know the chromatic number and can stop!
			System.out.println("The reduced graph appears to be complete, so the chromatic number is " + adj_matrix.length);
			stop(programStart);
		}
		//Run Backtracking...
		System.out.println("Trying coloring the graph using backtracking...");
		start = System.nanoTime();
		bestUpperBound = BacktrackingV2.run(lowerBound, adj_matrix, false);
		duration = (System.nanoTime() - start) / 1000000.0;
		System.out.println("Result from Backtracking: " + bestUpperBound);
		System.out.println("Backtracking took " + duration + "ms");
		//And evaluate what information we can get from that!
		eval(lowerBound, bestUpperBound, programStart);
		//Run DSatur...
		System.out.println("Running the DSatur algorithm to find an upper bound...");
		start = System.nanoTime();
		int[] DSaturColoring = DSatur.run(degrees, adj_matrix);
		int DSaturResult = HelperFunctions.getMax(DSaturColoring)+1;
		duration = (System.nanoTime() - start) / 1000000.0;
		System.out.println("Result from DSatur: " + DSaturResult);
		System.out.println("DSatur took " + duration + "ms");
		//And evaluate what information we can get from that!
		eval(lowerBound, DSaturResult, programStart);
		//Run RLF...
		System.out.println("Running RLF...");
		start = System.nanoTime();
		int RLFResult = RLF.run(vertices);
		duration = (System.nanoTime()-start) / 1000000.0;
		System.out.println("Result from RLF: " + RLFResult);
		System.out.println("RLF took " + duration + "ms");
		//And evaluate what information we can get from that!
		eval(lowerBound, RLFResult, programStart);
		System.out.println("Current best lower bound: " + lowerBound);
		System.out.println("Current best upper bound: " + bestUpperBound);
		//Run Bruteforce to try and get the chromatic number and/or get the bounds closer
		System.out.println("No matching lower and upper bounds were found, so starting bruteforcing from " + lowerBound + " to " + bestUpperBound);
		for(int i = lowerBound; i <= bestUpperBound; i++) {
			//Check what we know at the current time step... (with the current best lower bound)
			eval(lowerBound, bestUpperBound, programStart);
			System.out.println("Currently trying " + i + "-coloring...");
			//Try an i-coloring of the graph...
			int tmp = Bruteforce.Colorings(0, i, partialColoring, adj_matrix.length, adj_matrix);
			//If a solution with i colors is found, we know the chromatic number and can report it and stop the program!
			if(tmp == 1) {
				System.out.println("Found the chromatic number using bruteforcing! It is " + i);
				stop(programStart);
			}
			//Otherwise, we can update the lower bound because we ruled out an i-coloring as impossible
			else {
				lowerBound++;
				System.out.println("Current best lower bound: " + lowerBound);
			}
		}
	}
	/**
	This method is used to evaluate information about the graph gathered at specific points during execution of our program.
	@param lowerBound The best currently known lower bound
	@param upperBound An upper bound to check whether it is better than the best currently known upper bound.
	@param programStart The time when the program was started (of type <i>long</i>)
	*/
	private static void eval(int lowerBound, int upperBound, long programStart) {
		//If the lower bound and upper bound match, then we know we found the chromatic number!
		if(lowerBound == upperBound) {
			chromaticNumber = upperBound;
			System.out.println("Matching lower and upper bounds were found!");
			System.out.println("The chromatic number is " + chromaticNumber);
			stop(programStart);
		}
		//Otherwise, update the upper bound if it is better than the best currently known upper bound
		else if(upperBound < bestUpperBound) {
			bestUpperBound = upperBound;
		}
	}
	/**
	This method is used to stop the program and report the chromatic number and the time needed to find it
	@programStart The time when the program was started (of type <i>long</i>) to evaluate how long it took to find the chromatic number
	*/
	private static void stop(long programStart) {
		System.out.println("The time needed to find the chromatic number is " + ((System.nanoTime() - programStart) / 1000000.0) + "ms");
		System.exit(0);
	}
}
