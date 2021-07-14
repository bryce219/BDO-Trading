package TradeSolver;

public class Node {

	int level;
	int profit; 
	int bound;
	boolean flag;
	double weight;
	
	public Node() {}
	
	public Node(Node node) {
		level = node.level;
		profit = node.profit;
		bound = node.bound;
		flag = node.flag;
		weight = node.weight;
	}
}