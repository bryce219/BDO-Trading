package Distance;

import java.util.ArrayList;
import TradeSolver.Item;

public class Node {
	String node;
	int nx, ny, nz; // Node location
	int tx, ty; // Trader location
	ArrayList<Item> items;
	
	public Node() {}
	
	public void addItem(String name, int value, double weight, int amount, int level) {
		items.add(new Item(name, value, weight, amount, level));
	}
	
}
