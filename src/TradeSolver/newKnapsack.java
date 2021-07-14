package TradeSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
/*
 * you're the one who wrote it, dont ask me how it works ;P
 */
class newKnapsack { 
	
    private static int size; 
    private static double maxWeight; 
    private static int numSlots;
    private static int numItems;
    private static int maxValue = -1;
    private static double epsilon = 0.000001;
    
    public static int maxValue(ArrayList<Item> arr) {
    	int maxValue = 0;
    	for(int i = 0; i < arr.size(); i++)
    		if(maxValue < arr.get(i).value && arr.get(i).amount > 0)
    			maxValue = arr.get(i).value;
    	return maxValue;
    }

    public static int maxValue(Item[] arr) {
    	int maxValue = 0;
    	for(int i = 0; i < arr.length; i++)
    		if(maxValue < arr[i].value)
    			maxValue = arr[i].value;
    	return maxValue;
    }
    
    public static int numItems(ArrayList<Item> items) {
    	int num = 0;
    	for(Item item: items)
    		num += item.amount;
    	return num;
    }
    
    public static int numItems(boolean[] list, Item[] arr) {
    	int num = 0;
    	for(int i = 0; i < list.length; i++)
    		num += list[i] ? 1*arr[i].amount : 0;
    	return num;
    }
    
	public static int Bound(Node u, Item arr[]) { 
	    if (u.weight > maxWeight) 
	        return 0; 
	  
	    int profit_bound = u.profit; 
	  
	    int j = u.level + 1; 
	    double totweight = u.weight; 
	  
	    while((j < size) && (totweight + arr[j].weight <= maxWeight)) { 
	        totweight += arr[j].weight; 
	        profit_bound += arr[j].value; 
	        j++; 
	    } 
	  
	    if(j < size) 
	        profit_bound += (maxWeight - totweight) * arr[j].value / arr[j].weight; 
	  
	    return profit_bound; 
	} 
	  
    public static int finalProfit(boolean[] path, Item[] arr) {
    	int profit = 0;
    	for(int i = 0; i < path.length; i++)
    		profit += path[i] ? arr[i].value : 0;
    	return profit;
    }
    
    public static double finalWeight(boolean[] path, Item[] arr) {
    	double weight = 0;
    	for(int i = 0; i < path.length; i++)
    		weight += path[i] ? arr[i].weight : 0;
    	return weight;
    }
    
    public static boolean[] fixPath(boolean[] path, Item[] arr) {
    	int bestIndex = -1;
    	int mostProfit = 0;
    	for(int i = 0; i < path.length; i++) {
    		if(path[i]) {
    			path[i] = false;
    			int tempProfit = finalProfit(path, arr);
    			if(tempProfit > mostProfit && finalWeight(path, arr) <= maxWeight) {
    				bestIndex = i;
    				mostProfit = tempProfit;
    			}
    			path[i] = true;
    		}	
    	}
    	path[bestIndex] = false;
    	return path;
    }
    
	@SuppressWarnings("unchecked")
	public static String knapsack(ArrayList<Item> items, boolean isAdvanced) { 
	   
		ArrayList<Integer>
		names = new ArrayList<>(),
		slots = new ArrayList<>(),
		val = new ArrayList<>();
	
		ArrayList<Double> 
		wt = new ArrayList<>();
		
		maxValue = maxValue(items);
		//numItems = numItems(items);
		
		for(int A = 0; A < items.size(); A++) { // lowers number of items into multiples of 2
			int a = (int)(Math.log(items.get(A).getAmount())/Math.log(2));
			for(int B = 0; B < a; B++) {
				wt.add(items.get(A).getWeight() * (int)Math.pow(2,B));
				val.add(items.get(A).getValue() * (int)Math.pow(2,B));
				slots.add((int)Math.pow(2,B));
				names.add(A);
			}
			if(a >= 0) {
				wt.add(items.get(A).getWeight() * ((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1));
				val.add(items.get(A).getValue() * ((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1));
				slots.add((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1);
				names.add(A);
			}
		}
		
		size = wt.size();
		Item[] arr = new Item[wt.size()];
		for(int i = 0; i < wt.size(); i++)
			arr[i] = new Item(""+names.get(i),val.get(i),wt.get(i),slots.get(i));
			
		if(isAdvanced) {
			arr = formatAtLeastSlots(arr);
			arr = formatAtMostSlots(arr);
		}
		
		Arrays.sort(arr, new sortByRatio()); 
		
	    LinkedList<Node> Q = new LinkedList<>();
	    Node u = new Node();
	    Node v = new Node();
	  
	    u.level = -1;
	    u.profit = 0;
	    u.weight = 0;
	    u.flag = false;
	    Q.add(u); 
	  
        ArrayList<Boolean> currPath = new ArrayList<>();
        ArrayList<Boolean> bestPath = new ArrayList<>();
	    
	    int maxProfit = 0;
	    while(!Q.isEmpty()) {
	        u = Q.pop();
	        
	        while(u.level != -1 && u.level < currPath.size())
	        	currPath.remove(currPath.size()-1);
            if (u.level >= 0) 
                currPath.add(u.flag);
            
	        if(u.weight <= maxWeight && u.profit > maxProfit) {
	            maxProfit = u.profit;
	            bestPath = (ArrayList<Boolean>) currPath.clone();
	        }
	        
	        if(u.level == size-1)
	            continue; 
	        
	        v.level = u.level + 1; 
	        
	        v.weight = u.weight + arr[v.level].weight; 
	        v.profit = u.profit + arr[v.level].value;
	        
	        v.bound = Bound(v, arr); 
	        if(v.bound > maxProfit) {
	        	Node temp = new Node(v);
	        	temp.flag = true;
	            Q.push(temp); 
	        }
	        
	        v.weight = u.weight; 
	        v.profit = u.profit; 
	        
	        v.bound = Bound(v, arr); 
	        if(v.bound > maxProfit) {
	        	Node temp = new Node(v);
	        	temp.flag = false;
	            Q.push(temp); 
	        }
	    }
	    boolean[] finalPath = new boolean[size];
	    
	    for(int i = 0; i < bestPath.size(); i++)
	    	finalPath[i] = bestPath.get(i);
	    
	    if(isAdvanced) {
	    	maxWeight = -(numSlots - maxWeight)/(numSlots + 1); //revert changes
        	arr = revertArray(arr);
	    }
	    
        //for(int i = 0; i < arr.length; i++)
        //	System.out.println(arr[i].name+","+arr[i].weight+","+arr[i].value+","+arr[i].amount);
        
        double finalWeight = finalWeight(finalPath, arr);
        
        //System.out.println(numItems(finalPath, arr)+","+numSlots);
        //System.out.println(finalWeight+","+maxWeight);
        
        if((!isAdvanced || numItems(finalPath, arr) == numSlots) && finalWeight <= maxWeight)
        	return getItemReturnString(finalPath, arr)+finalProfit(finalPath, arr);
        return "-1";
	} 
	
	public static String getItemReturnString(boolean[] path, Item[] arr) {
		String s = "";
		for(int i = 0; i < path.length; i++)
			if(path[i])
				for(int j = 0; j < arr[i].amount; j++)
					s += arr[i].name+",";
		return s;
	}
	
	public static Item[] revertArray(Item[] arr) {
		for(int i = 0; i < size; i++) {
			arr[i].value -= numItems * (maxValue + 1) * arr[i].amount;
    		arr[i].weight -= (maxWeight + 1) * arr[i].amount;
		}
		return arr;
	}
	
    public static Item[] formatAtMostSlots(Item[] arr) {
    	for(int i = 0; i < arr.length; i++) {
    		arr[i].weight += (maxWeight + 1) * arr[i].amount;
    	}
    	maxWeight += numSlots * (maxWeight + 1);
    	return arr;
    }
    
    public static Item[] formatAtLeastSlots(Item[] arr) {
    	for(int i = 0; i < arr.length; i++)
    		arr[i].value += numItems * (maxValue + 1) * arr[i].amount;
    	return arr;
    }
	
    public static double getLowestWeights(ArrayList<Item> items, int slots) {
    	double weight = 0;
    	if(slots > numItems)
    		return maxWeight + 1;
    	ArrayList<Item> newItems = new ArrayList<>();
		for(Item item: items)
			newItems.add(new Item(item));
        for (int i = 0; i < newItems.size(); i++)
            for (int j = i+1; j < newItems.size(); j++)
                if (newItems.get(j).getWeight() < newItems.get(i).getWeight()) {
                    Item temp = newItems.get(j);
                    newItems.set(j,newItems.get(i)); 
                    newItems.set(i,temp); 
                }
        for(int i = 0; i < slots; i++)
        	if(newItems.get(0).amount > 0) {
	        	weight += newItems.get(0).weight;
	        	newItems.get(0).amount--;
        	} else {
        		newItems.remove(0);
        		i--;
        	}
    	return weight;
    }
    
    public static String maxNumber(int S, double W, ArrayList<Item> items, int finalSlotNum) {
    	numItems = numItems(items);
		maxWeight = W + epsilon;
		if(finalSlotNum == 0) {
	    	for(int i = S; i > 0; i--)
	    		if(getLowestWeights(items, i) <= W) {
	    			numSlots = i;
	    			String a = knapsack(items,true);
	    			return a;
	    		}
	    	return "";
		} else {
			numSlots = finalSlotNum;
			return knapsack(items,true);
		}
    }
    
    public static String maxCost(int S, double W, ArrayList<Item> items, int finalSlotNum) {
    	numItems = numItems(items);
    	if(finalSlotNum == 0) {
	    	String toReturn = "";
			String max = "0";
			for(int i = 1; i <= S; i++) {
				maxWeight = W + epsilon;
		    	numSlots = i;
		    	String ks = knapsack(items,false);
		    	if(ks.split(",").length-1 > S)
		    		ks = knapsack(items,true);
				if(Integer.parseInt(ks.split(",")[ks.split(",").length-1]) > Integer.parseInt(max.split(",")[max.split(",").length-1]))
					max = ks;
				else
					break;
			}
			
		    toReturn += max;
	    	return toReturn;
    	} else {
    		maxWeight = W + epsilon;
    		numSlots = finalSlotNum;
    		String ks = knapsack(items,false);
    		if(ks.split(",").length-1 <= S)
    			return ks;
    		return knapsack(items,true);
    	}
    }
    
    public static void main(String args[]) {
    	maxCost(20,1121.1,new ArrayList<>(),0);
    } 
}


	