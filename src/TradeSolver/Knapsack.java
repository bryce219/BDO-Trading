/*
"Greater love hath no man than this, that a man lay down his life for his friends"

package TradeSolver;
import java.util.ArrayList;

public class Knapsack {  
	
	public static String addTo(String A, int B, int C) {
		for(int i = 0; i < C; i++)
			A += B+",";
		return A;
	}
	
	public static int getLowestWeight(ArrayList<Item> items, int slots) {
		ArrayList<Item> newItems = new ArrayList<>();
		for(Item item: items)
			newItems.add(new Item(item));
        for (int i = 0; i < newItems.size()-1; i++) 
            for (int j = 0; j < newItems.size()-i-1; j++)
                if (newItems.get(j).getWeight() > newItems.get(j+1).getWeight()) {
                    Item temp = newItems.get(j);
                    newItems.set(j,newItems.get(j+1)); 
                    newItems.set(j+1,temp); 
                }
		int weight = 0;
		for(int i = 0; i < slots; i++)
			try {
				weight += newItems.get(0).getWeight();
				newItems.get(0).setAmount(newItems.get(0).getAmount()-1);
				if(newItems.get(0).getAmount() == 0)
					newItems.remove(0);
			} catch(Exception e) { break; }
		return weight;
	}
	
	public static String maxNumber(int S, int W, ArrayList<Item> items) {	
		ArrayList<Item> newItems = new ArrayList<>();
		for(Item item: items)
			newItems.add(new Item(item));
		int maxValue = -1, totalItems = 0;
    	for(Item item: items) {
    		totalItems += item.getAmount();
    		if(item.getValue() > maxValue)
    			maxValue = item.getValue();
    	}
    	for(Item item: newItems) {
    		item.setValue(item.getValue()+totalItems*(maxValue+1));
    		item.setWeight(item.getWeight()+W+1);
    	}
    	String toReturn = "";
    	for(int i = S; i > 0; i--)
    		if(getLowestWeight(items,i) <= W) {
    			toReturn = knapsack(W + i*(W+1), newItems, totalItems*(maxValue+1));
    			break;
    		}
    	return toReturn;
	}
	
	
    public static String maxCost(int S, int W, ArrayList<Item> items) {
    	ArrayList<Integer> 
		wt = new ArrayList<>(),
		val = new ArrayList<>(),
		names = new ArrayList<>(),
		slots = new ArrayList<>();
    	
    	int I = 0, itemCounter = 0, i, w, s;
    	for(int n = 0; n < items.size(); n++)
    		itemCounter += items.get(n).getAmount();
    	if(itemCounter == 0)
    		return "-1";
    	
		for(int A = 0; A < items.size(); A++) {
			int a = (int)(Math.log(items.get(A).getAmount())/Math.log(2));
			for(int B = 0; B < a; B++) {
				wt.add(items.get(A).getWeight() * (int)Math.pow(2,B));
				val.add(items.get(A).getValue() * (int)Math.pow(2,B));
				slots.add((int)Math.pow(2,B));
				names.add(A);
				I++;
			}
			if(a >= 0) {
				wt.add(items.get(A).getWeight() * ((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1));
				val.add(items.get(A).getValue() * ((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1));
				slots.add((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1);
				names.add(A);
				I++;
			}
		}
		
		int K[][][] = new int[I+1][W+1][S+1];
		ArrayList<Integer> slotsTotal = (ArrayList<Integer>) slots.clone();
		for(int a = 1; a < slotsTotal.size(); a++)
			slotsTotal.set(a, slotsTotal.get(a-1) + slotsTotal.get(a));
    	String[][][] nameArray = new String[I+1][W+1][S+1];
	    for(int a = 0; a <= I; a++) {
	    	for(int b = 0; b <= W; b++) {
	    		K[a][b][0] = 0;
	    		nameArray[a][b][0] = "";
	    	}
	    }
	
	    for(int a = 0; a <= I; a++) {
	    	for(int b = 0; b <= S; b++) {
	    		K[a][0][b] = 0;
	    		nameArray[a][0][b] = "";
	    	}
	    }

	    for(int a = 0; a <= W; a++) {
	    	for(int b = 0; b <= S; b++) {
	    		K[0][a][b] = 0;
	    		nameArray[0][a][b] = "";
	    	}
	    }
	    
	    for (s = 1; s <= S; s++)
		    for (i = 1; i <= I; i++) {
		    	//System.out.println(s+","+i);
		    	for (w = 1; w <= W; w++) {
		    		//System.out.println(s+","+i+","+w);
		    		int temp2 = K[i-1][w][s];
		    		int temp1 = val.get(i-1);
		            if (wt.get(i-1) <= w) {
		            	if(slotsTotal.get(i-1) <= s) {
			            	temp1 += K[i-1][w-wt.get(i-1)][s];
			            	if(temp1 > temp2) {
			            		K[i][w][s] = temp1;
			            		nameArray[i][w][s] = addTo("",names.get(i-1),slots.get(i-1));
			            		nameArray[i][w][s] += nameArray[i-1][w-wt.get(i-1)][s];
			            	} else {
			            		K[i][w][s] = temp2;
			            		nameArray[i][w][s] = nameArray[i-1][w][s];
			            	}
		            	} else if (slots.get(i-1) > s){
			            	K[i][w][s] = K[i-1][w][s];
			            	nameArray[i][w][s] = nameArray[i-1][w][s];
		            	} else {
		            		temp1 += K[i-1][w-wt.get(i-1)][s-slots.get(i-1)];
		            		//try {
		            		//if(i-1 < I && w-wt.get(i-1) < W && s-slots.get(i-1) < S)
		            			//temp1 += K[i-1][w-wt.get(i-1)][s-slots.get(i-1)];
		            		//} catch(Exception e) {}
			            	if(temp1 > temp2) {
			            		K[i][w][s] = temp1;
			            		nameArray[i][w][s] = addTo("",names.get(i-1),slots.get(i-1));
			            		nameArray[i][w][s] += nameArray[i-1][w-wt.get(i-1)][s-slots.get(i-1)];
			            		//try {
			            			//nameArray[i][w][s] += nameArray[i-1][w-wt.get(i-1)][s-slots.get(i-1)];
			            		//} catch(Exception e) {}
			            	} else {
			            		K[i][w][s] = temp2;
			            		nameArray[i][w][s] = nameArray[i-1][w][s];
			            	}
		            	}
		            } else {
		            	K[i][w][s] = K[i-1][w][s];
		            	nameArray[i][w][s] = nameArray[i-1][w][s];
		            }
	    		}
		    }
	    return nameArray[I][W][S]+K[I][W][S]; 
     } 
    
	public static String knapsack(int W, ArrayList<Item> items, int C) {
    	ArrayList<Integer> 
		wt = new ArrayList<Integer>(),
		val = new ArrayList<Integer>(),
		names = new ArrayList<Integer>(),
		slots = new ArrayList<Integer>();
    	
    	int I = 0, itemCounter = 0, i, w;
    	for(int n = 0; n < items.size(); n++)
    		itemCounter += items.get(n).getAmount();
    	if(itemCounter == 0)
    		return "-1";
    	
		for(int A = 0; A < items.size(); A++) {
			int a = (int)(Math.log(items.get(A).getAmount())/Math.log(2));
			for(int B = 0; B < a; B++) {
				wt.add(items.get(A).getWeight() * (int)Math.pow(2,B));
				val.add(items.get(A).getValue() * (int)Math.pow(2,B));
				slots.add((int)Math.pow(2,B));
				names.add(A);
				I++;
			}
			if(a >= 0) {
				wt.add(items.get(A).getWeight() * ((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1));
				val.add(items.get(A).getValue() * ((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1));
				slots.add((items.get(A).getAmount()) - (int)Math.pow(2,a) + 1);
				names.add(A);
				I++;
			}
		}
		int K[][] = new int[I+1][W+1];
		ArrayList<Integer> slotsTotal = (ArrayList<Integer>) slots.clone();
		for(int a = 1; a < slotsTotal.size(); a++)
			slotsTotal.set(a, slotsTotal.get(a-1) + slotsTotal.get(a));
    	String[][] nameArray = new String[I+1][W+1];
	
	    for(int a = 0; a <= I; a++) {
    		K[a][0] = 0;
    		nameArray[a][0] = "";
	    }

	    for(int a = 0; a <= W; a++) {
    		K[0][a] = 0;
    		nameArray[0][a] = "";
	    }
	    
	    for (i = 1; i <= I; i++)
	    	for (w = 1; w <= W; w++) {
            	int temp2 = K[i-1][w];
	            if (wt.get(i-1) <= w) {
	            	int temp1 = val.get(i-1) + K[i-1][w-wt.get(i-1)];
	            	if(temp1 > temp2) {
	            		K[i][w] = temp1;
	            		nameArray[i][w] = addTo("",names.get(i-1),slots.get(i-1)) + nameArray[i-1][w-wt.get(i-1)];
	            		//nameArray[i][w] += nameArray[i-1][w-wt.get(i-1)];
	            	} else {
	            		K[i][w] = temp2;
	            		nameArray[i][w] = nameArray[i-1][w];
	            	}
	            } else {
	            	K[i][w] = temp2;
	            	nameArray[i][w] = nameArray[i-1][w];
	            }
	    	}
	    return nameArray[I][W]+(K[I][W]-nameArray[I][W].split(",").length*C); 
     } 
} */