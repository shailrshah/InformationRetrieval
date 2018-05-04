package com.shail.ir.pagerank;

/**
 * An implementation of IPageRanker is able to compute the PageRank for a given graph
 *
 * An example of a graph is:
 * A D E F
 * B A F
 * C A B D
 * D B C
 * E B C D F
 * F A B D
 *
 * Each node is delimited by a space. The first node in each line is the destination node,
 * and the subsequent nodes in the same line are the inlinks to the destination node.
 * So, "B A F" means that node B has inlinks from nodes A and F.
 *
 * A line with only one node is a source node (since it has only outgoing flow)
 *
 * @author Shail Shah`
 */
public interface IPageRanker {

	/**
	 * Computes the PageRank for a graph.
	 */
	void computePageRank();

	/**
	 * Print the top n nodes of PageRank to a file
	 * @param filePath the path where the result will be stored
	 * @param n the number of top nodes to print
	 */
	void printResult(String filePath, int n);

	/**
	 * Get the number of sink nodes in the graph
	 * @return the number of sink nodes in the graph
	 */
	int getSinkNodesCount();

	/**
	 * Get the number of source nodes in the graph
	 * @return the number of source nodes in the graph
	 */
	int getSourceNodesCount();

	/**
	 * Set the path of the graph
	 * @param graphPath the path of the graph
	 */
	void setGraphPath(String graphPath);
}
