package RouteSolver;

import java.util.ArrayList;

public class Node {

	double bound;
	ArrayList<Integer> path;
	double distance;
	double profit;
	
	public Node() {}
	
	@SuppressWarnings("unchecked")
	public Node(Node node) {
		bound = node.bound;
		path = (ArrayList<Integer>) node.path.clone();
		distance = node.distance;
		profit = node.profit;
	}
	
	public int getNode() { return path.get(path.size()-1); }
}