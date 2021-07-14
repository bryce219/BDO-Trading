package TradeSolver;

public class Item {
	String name;
	int value, amount, level;
	double weight;
	
	public Item(String name, int value, double weight, int amount, int level) {
		this.name = name;
		this.value = value;
		this.weight = weight;
		this.amount = amount;
		this.level = level;
	}
	
	public Item(String name, int value, double weight, int amount) {
		this.name = name;
		this.value = value;
		this.weight = weight;
		this.amount = amount;
		this.level = -1;
	}
	
	public Item(Item item) {
		this.name = item.getName();
		this.value = item.getValue();
		this.weight = item.getWeight();
		this.amount = item.getAmount();
		this.level = item.getLevel();
	}
	
	public int getValue() { return value; }
	public double getWeight() { return weight; }
	public int getAmount() { return amount; }
	public String getName() { return name; }
	public int getLevel() { return level; }
	
	public void setValue(int value) { this.value = value; }
	public void setWeight(double weight) { this.weight = weight; }
	public void setAmount(int amount) { this.amount = amount; }
	public void setName(String name) { this.name = name; }
	public void setLevel(int level) { this.level = level; }
	
	public String toString() {
		return this.amount+","+this.weight+","+this.name+","+this.value+","+this.level;
	}
}
