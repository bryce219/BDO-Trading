package RouteSolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

import Distance.Equations;
import Distance.TradeXP;

public class newRouteComputer {
	
	static String Region;
	static int maxSlots;
	static double maxWeight;
	static String finalFileName;
	static int Level;
	static int effectiveLevel;
	static int maxDistance;
	
	static File outputFolderCost;
	static File outputFolderXP;
	
	static File solvedFolderCost;
	static File solvedFolderXP;
	
	static File[] filesCost;
	static File[] filesXP;
	
	static LinkedList<Path> bestPaths;
	static Dictionary<String, Integer> nameToInt = new Hashtable<>();
	static ArrayList<Integer> validNodes;
	
	//static ArrayList<double[]>[] bestProfits;
	
	private static DataInputStream in;
	
	
	static double[][] Distances;
	
	static double[][][] totalCostInformation;
	static double[][][] totalXPInformation;
	static String[][] ordersInformation;
	
	public static void setDistances() {
		for(int i = 0; i < filesCost.length; i++)
			for(int j = 0; j < filesCost.length; j++) {
				String a = filesCost[i].getName();
				String b = filesCost[j].getName();
				Distances[i][j] = Equations.HorizontalDistance(a.substring(0,a.length()-11), b.substring(0,b.length()-11));
			}
	}
	
	public static void ReadFromBin(File file1, String file1Name, File file2, String file2Name, String priority) {
		int index = 0;
		try {
			in = new DataInputStream(new FileInputStream(file1));
			try {
				if(priority.equals("Cost")) {
					while(true) {
						totalCostInformation[nameToInt.get(file1Name)][nameToInt.get(file2Name)][index] = in.readInt() * Equations.CostBonus(file1Name, file2Name);
						index++;
					}
				}
				else if(priority.equals("Number")) {
					while(true) {
						totalXPInformation[nameToInt.get(file1Name)][nameToInt.get(file2Name)][index] = in.readInt() * TradeXP.getXP(file1Name, file2Name, Level);
						index++;
					}
				}
			} catch (Exception e) {}
			in.close();
    	} catch(Exception e) {
    		System.out.println("readFromBinNew: ooooh u stupid");
    	}
	}
	
	public static void setOrderInformation(String priority) {
		ordersInformation = new String[filesCost.length][maxTrips(priority)];
		BufferedReader objReader;
		for(File file: new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+priority+","+effectiveLevel).listFiles()) {
			try {
				String strCurrentLine;
				objReader = new BufferedReader(new FileReader(file));
				objReader.readLine();
				objReader.readLine();
				int visitsIndex = 0;
				while((strCurrentLine = objReader.readLine()) != null) {
					if(strCurrentLine.split(" ")[1].equals("Trial,"))
						break;
					ordersInformation[nameToInt.get(file.getName().split(" Solved")[0])][visitsIndex] = strCurrentLine;
					visitsIndex++;
				}
				objReader.close();
			} catch (Exception e) {}	
		}
	}
	
	public static int getRouteProfit(ArrayList<Integer> route) {
		int profit = 0;
		int[] visitsArray = new int[filesCost.length];
		for(int i = 1; i < route.size(); i++) {
			profit += totalCostInformation[route.get(i-1)][route.get(i)][visitsArray[route.get(i-1)]];
			visitsArray[route.get(i-1)]++;
		}
		return profit;
	}
	
	public static double getRouteXP(ArrayList<Integer> route) {
		double xp = 0;
		int[] visitsArray = new int[filesCost.length];
		for(int i = 1; i < route.size(); i++) {
			xp += totalXPInformation[route.get(i-1)][route.get(i)][visitsArray[route.get(i-1)]];
			visitsArray[route.get(i-1)]++;
		}
		return xp;
	}
	
	public static void writeToFile(ArrayList<Integer> route, String priority) {
    	try {
    		File f1 = null;
    		if(priority.equals("Cost"))
    			f1 = new File(outputFolderCost+"/"+finalFileName+" Solved.txt");
    		if(priority.equals("Number"))
    			f1 = new File(outputFolderXP+"/"+finalFileName+" Solved.txt");
		    f1.createNewFile();
	    	BufferedWriter buffer1 = new BufferedWriter(new FileWriter(f1));  
	    	int[] visitsArray = new int[filesCost.length];
	    	buffer1.write("Priority: Cost, Slots: "+maxSlots+", Weight: "+maxWeight+", Level: "+Level+", Effective Level: "+effectiveLevel+"\n\nMax Distance: "+maxDistance+"\nTotal Silver: "+getRouteProfit(route)+"  |  Total XP: "+(int)getRouteXP(route)+"\n\n");
	    	for(int i = 0; i < route.size()-1; i++) {
	    		buffer1.write(filesCost[route.get(i)].getName().split(" Solved")[0]+" -> "+filesCost[route.get(i+1)].getName().split(" Solved")[0]+"\n");
	    		try {
	    			buffer1.write("		| Visit "+ordersInformation[route.get(i)][visitsArray[route.get(i)]].split("Trial")[1]+"\n");
	    			visitsArray[route.get(i)]++;
	    		} catch(Exception e) {
	    			buffer1.write("		| Nothing\n");
	    		}
	    	}
		    buffer1.write("\n"+route);
		    buffer1.close();
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("writeToFile: ooooh u stupid");
    	}
	}
	
	public static void nameToInt() {
		for(int i = 0; i < filesCost.length; i++)
			nameToInt.put(filesCost[i].getName().substring(0,filesCost[i].getName().length()-11), i);
	}
	
	public static int maxTrips(String priority) {
		int maxTrips = 1;
		File[] files = null;
		if(priority.equals("Cost"))
			files = solvedFolderCost.listFiles();
		if(priority.equals("Number"))
			files = solvedFolderCost.listFiles();
		for(File file: files) {
			int i = 0;
			try {
				BufferedReader objReader = new BufferedReader(new FileReader(file));
				while(objReader.readLine() != null)
					i++;
				objReader.close();
			} catch (Exception e) {System.out.println("K");}
			if(i - 3 > maxTrips)
				maxTrips = i - 3;
		}
		return maxTrips;
	}
	
	public static void initialize() {
		
		nameToInt();
		
		totalCostInformation = new double[filesCost.length][filesCost.length][maxTrips("Cost")];
		totalXPInformation = new double[filesCost.length][filesCost.length][maxTrips("Number")];
		
		Distances = new double[filesCost.length][filesCost.length];
		
		System.out.println("Setting Distances Array...");
		
		setDistances();
		
		System.out.println("Generating Cost Information...");
		for(File file1: filesCost)
			for(File file2: filesCost)
				ReadFromBin(file1, file1.getName().substring(0,file1.getName().length()-11), file2, file2.getName().substring(0,file2.getName().length()-11), "Cost");
		
		System.out.println("Generating XP Information...");
		for(File file1: filesXP)
			for(File file2: filesXP)
				ReadFromBin(file1, file1.getName().substring(0,file1.getName().length()-11), file2, file2.getName().substring(0,file2.getName().length()-11), "Number");	
		
		System.out.println("Done!");
	}
	
	public static int closestNode(int node) {
		int closestNode = 0;
		double bestDistance = Double.POSITIVE_INFINITY;
		for(int i = 0; i < filesCost.length; i++)
			if(i != node && Distances[node][i] < bestDistance) {
				bestDistance = Distances[node][i];
				closestNode = i;
			}
		return closestNode;
	}
	
	public static double equationCost(int n1, int n2) {
		if(Distances[n1][n2] == 0)
			return 0;
		
		int n2Closest = closestNode(n2);
		
		return totalCostInformation[n1][n2][0] / Distances[n1][n2] + totalCostInformation[n2][n2Closest][0] / Distances[n2][n2Closest];
	}
	
	public static double equationNumber(int n1, int n2) {
		if(Distances[n1][n2] == 0)
			return 0;
		
		int n2Closest = closestNode(n2);
		
		return totalXPInformation[n1][n2][0] / Distances[n1][n2] + totalXPInformation[n2][n2Closest][0] / Distances[n2][n2Closest];
	}
	
	public static ArrayList<Integer> sort(int startingNode, ArrayList<Integer> list, String priority){
		if(priority.equals("Cost")) {
			for(int i = 0; i < list.size(); i++) {
				double numI = equationCost(startingNode, i);
				for(int j = i+1; j < list.size(); j++)
					if(equationCost(startingNode, j) > numI) {
						int temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
			}
		}
		if(priority.equals("Number")) {
			for(int i = 0; i < list.size(); i++) {
				double numI = equationNumber(startingNode, i);
				for(int j = i+1; j < list.size(); j++)
					if(equationNumber(startingNode, j) > numI) {
						int temp = list.get(i);
						list.set(i, list.get(j));
						list.set(j, temp);
					}
			}
		}
		return list;
	}
	
	public static ArrayList<Integer> getReachableNodes(int startingNode){
		ArrayList<Integer> nodes = new ArrayList<>();
		for(int i = 0; i < filesCost.length; i++)
			if(Distances[startingNode][i] <= maxDistance)
				nodes.add(i);
		return nodes;
	}
	
	public static double boundCost(int currNode, int nextNode, double distance, int[] visits) {
		int[] newVisits = visits.clone();
		double newDistance = distance + Distances[currNode][nextNode];
		if(newDistance > maxDistance)
			return 0;
		
		/*
		double lowestProfit = Double.POSITIVE_INFINITY;
		for(double[] profit: bestProfits[nextNode])
			if(newDistance < profit[0] && lowestProfit > profit[1])
				lowestProfit = profit[1];
		
		return lowestProfit;
		*/
		
		double profit = totalCostInformation[currNode][nextNode][newVisits[currNode]];
		newVisits[currNode]++;
		
		double remianingDistance = maxDistance - newDistance; 
		
		ArrayList<Path> newPaths = new ArrayList<>();
		
		for(Path path: bestPaths)
			if(newVisits[path.nodeStart] == path.visits && Distances[nextNode][path.nodeStart] <= remianingDistance && Distances[nextNode][path.nodeEnd] <= remianingDistance)
				newPaths.add(path);
		
		boolean exit = false;
		while(!exit) {
			exit = true;
			for(Path path: newPaths) {
				if(newVisits[path.nodeStart] == path.visits && newDistance + Distances[path.nodeStart][path.nodeEnd] <= maxDistance) {
					newDistance += Distances[path.nodeStart][path.nodeEnd];
					profit += totalCostInformation[path.nodeStart][path.nodeEnd][path.visits];
					newVisits[path.nodeStart]++;
					exit = false;
					break;
				}
			}
		}
		return profit; 
	}
	
	public static double boundNumber(int currNode, int nextNode, double distance, int[] visits) {
		int[] newVisits = visits.clone();
		double newDistance = distance + Distances[currNode][nextNode];
		if(newDistance > maxDistance)
			return 0;
		
		double profit = totalXPInformation[currNode][nextNode][newVisits[currNode]];
		newVisits[currNode]++;
		
		double remianingDistance = maxDistance - newDistance; 
		
		ArrayList<Path> newPaths = new ArrayList<>();
		
		for(Path path: bestPaths)
			if(Distances[nextNode][path.nodeStart] <= remianingDistance && Distances[nextNode][path.nodeEnd] <= remianingDistance)
				newPaths.add(path);
		
		boolean exit = false;
		while(!exit) {
			exit = true;
			for(Path path: newPaths) {
				if(newVisits[path.nodeStart] == path.visits && newDistance + Distances[path.nodeStart][path.nodeEnd] <= maxDistance) {
					newDistance += Distances[path.nodeStart][path.nodeEnd];
					profit += totalXPInformation[path.nodeStart][path.nodeEnd][path.visits];
					newVisits[path.nodeStart]++;
					exit = false;
					break;
				}
			}
		}
		return profit; 
	}
	
	public static int[] generateVisitsArray(ArrayList<Integer> path) {
		int[] visitsArray = new int[filesCost.length];
		for(int i = 0; i < path.size()-1; i++)
			visitsArray[path.get(i)]++;
		return visitsArray;
	}
	
	public static LinkedList<Path> generateBestPaths(String priority){
		LinkedList<Path> paths = new LinkedList<>();
		int maxVisits;
		if(priority.equals("Cost")) {
			maxVisits = totalCostInformation[0][0].length;
			
			for(int n1: validNodes) { //generate phase
				for(int n2: validNodes)
					if(Distances[n1][n2] != 0)
						for(int visits = 0; visits < maxVisits && totalCostInformation[n1][n2][visits] != 0; visits++)
							paths.add(new Path(n1, n2, visits));
			}
			
			System.out.println("Sorting Best Paths...");
			QuickSort qs = new QuickSort();
			qs.quickSort(paths, totalCostInformation, Distances);
			System.out.println("Done!");
		}
		if(priority.equals("Number")) {
			maxVisits = totalXPInformation[0][0].length;
			
			for(int n1: validNodes) { //generate phase
				for(int n2: validNodes)
					if(Distances[n1][n2] != 0)
						for(int visits = 0; visits < maxVisits && totalXPInformation[n1][n2][visits] != 0; visits++)
							paths.add(new Path(n1, n2, visits));
			}
			
			System.out.println("Sorting Best Paths...");
			QuickSort qs = new QuickSort();
			qs.quickSort(paths, totalXPInformation, Distances);
			System.out.println("Done!");
		}
		return paths;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Integer> calculate(int startingNode, String priority) {
		
		LinkedList<Node> Q = new LinkedList<>();
		
		Node u = new Node();
		Node v = new Node();
		
		u.path = new ArrayList<>();
		u.path.add(startingNode);
	    u.profit = 0;
	    u.distance = 0;
		Q.add(u);
		
		validNodes = sort(startingNode, getReachableNodes(startingNode), priority);
		bestPaths = generateBestPaths(priority);
		
		ArrayList<Integer> finalPath = null;
		int[] visitsArray = new int[filesCost.length];
		
		//System.out.println(Bound(6, 82, 334411, visitsArray, priority));
		//System.exit(1);
		//for(int i = 0; i < bestPaths.size(); i++)
		//	System.out.println(bestPaths.get(i).nodeStart+","+bestPaths.get(i).nodeEnd+","+bestPaths.get(i).visits+","+totalCostInformation[bestPaths.get(i).nodeStart][bestPaths.get(i).nodeEnd][bestPaths.get(i).visits] /* / Distances[bestPaths.get(i).nodeStart][bestPaths.get(i).nodeEnd]*/);
		
		
		double maxProfit = 0;
		System.out.println("Starting Route Calculation...");
		if(priority.equals("Cost")) {
		    while(!Q.isEmpty()) {
		        u = Q.pop();
		        
		        visitsArray = generateVisitsArray(u.path);
		        
		        if(u.path.size() > 1  && (u.path.size() >= 13 || u.path.size() <= 3)) {
			        System.out.println(Q.size() + " | " + u.path.get(1)+" , "+validNodes);
			        System.out.println(maxProfit+" || "+finalPath);
		        }
		        
		        int currNode = u.getNode();
		        
		        if(u.distance <= maxDistance && u.profit > maxProfit) {
		            maxProfit = u.profit;
		            finalPath = (ArrayList<Integer>) u.path.clone();
		        }
		        
		        for(int i = validNodes.size()-1; i >= 0; i--) {
		        	int nextNode = validNodes.get(i);
		        	v.distance = u.distance + Distances[currNode][nextNode];
		        	if(Distances[currNode][nextNode] == 0 || v.distance > maxDistance)
		        		continue;
		    		v.bound = boundCost(currNode, nextNode, u.distance, visitsArray) + u.profit;
		    		//System.out.println(nextNode+","+v.bound);
			        if(v.bound > maxProfit) {
			        	v.path = (ArrayList<Integer>) u.path.clone();
			        	v.path.add(nextNode);
			    		v.profit = u.profit + totalCostInformation[currNode][nextNode][visitsArray[currNode]];
			            Q.push(new Node(v));  
			        }
		        }
		        //bestProfits[u.path.get(u.path.size()-1)].add(new double[] {maxDistance - u.distance, u.profit});
		    }
		}
		if(priority.equals("Number")) {
		    while(!Q.isEmpty()) {
		        u = Q.pop();
		        
		        visitsArray = generateVisitsArray(u.path);
		        
		        if(u.path.size() > 1  && (u.path.size() >= 13 || u.path.size() <= 3)) {
			        System.out.println(Q.size() + " | " + u.path.get(1)+" , "+validNodes);
			        System.out.println(maxProfit+" || "+finalPath);
		        }
		        
		        int currNode = u.getNode();
		        
		        if(u.distance <= maxDistance && u.profit > maxProfit) {
		            maxProfit = u.profit;
		            finalPath = (ArrayList<Integer>) u.path.clone();
		        }
		        
		        for(int i = validNodes.size()-1; i >= 0; i--) {
		        	int nextNode = validNodes.get(i);
		        	v.distance = u.distance + Distances[currNode][nextNode];
		        	if(Distances[currNode][nextNode] == 0 || v.distance > maxDistance)
		        		continue;
		    		v.bound = boundNumber(currNode, nextNode, u.distance, visitsArray) + u.profit;
		    		//System.out.println(nextNode+","+v.bound);
			        if(v.bound > maxProfit) {
			        	v.path = (ArrayList<Integer>) u.path.clone();
			        	v.path.add(nextNode);
			    		v.profit = u.profit + totalXPInformation[currNode][nextNode][visitsArray[currNode]];
			            Q.push(new Node(v));  
			        }
		        }
		    }
		}
	    System.out.println(maxProfit);
	    return finalPath;
	}
	
	public static void main(String[] args) {
		
		Region = "Land";
		maxSlots = 16;
		maxWeight = 1200.0;
		Level = 55; //for xp calcs
		effectiveLevel = 0; //for file location
		maxDistance = 475000; //475000
		
		String priority = "Cost";
		
		new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Routes").mkdir();
		
		filesCost = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Cost,"+effectiveLevel).listFiles();
		filesXP = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Number,"+effectiveLevel).listFiles();
		
		solvedFolderCost = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Cost,"+effectiveLevel);
		solvedFolderXP = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Number,"+effectiveLevel);
		
		outputFolderCost = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Routes/Solved "+Region+" Routes - "+maxSlots+","+maxWeight+",Cost,"+Level+","+maxDistance);
		outputFolderXP = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Routes/Solved "+Region+" Routes - "+maxSlots+","+maxWeight+",Number,"+Level+","+maxDistance);

		outputFolderCost.mkdir();
		outputFolderXP.mkdir();
		
		initialize();
		
		//bestProfits = new ArrayList[filesCost.length];
				
		setOrderInformation(priority);
		String node = "Altinova";
		
		File file = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+priority+","+effectiveLevel+"/"+node+" Solved.txt");
		finalFileName = file.getName().split(" Solved")[0];
		
		ArrayList<Integer> route = calculate(nameToInt.get(file.getName().split(" Solved.txt")[0]), priority);
		writeToFile(route, priority);
		System.out.println(route);
	}
}
