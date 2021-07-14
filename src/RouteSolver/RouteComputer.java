package RouteSolver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;

import Distance.Equations;
import Distance.TradeXP;
import TradeSolver.Item;

public class RouteComputer {
	
	static String Region;
	static int maxSlots;
	static double maxWeight;
	static String finalFileName;
	static int Level;
	static boolean useMax;
	static int effectiveLevel;
	
	static int maxRouteSize;
	
	static File outputFolderCost;
	static File outputFolderXP;
	
	static File solvedFolderCost;
	static File solvedFolderXP;
	
	static File[] filesCost;
	static File[] filesXP;
	
	static int maxDistance;
	
	static ArrayList<ArrayList<Double>> costHeuristic = new ArrayList<>();
	static ArrayList<ArrayList<Double>> numberHeuristic = new ArrayList<>();
	
	static ArrayList<ArrayList<Integer>> allNumbersCost = new ArrayList<>();
	static ArrayList<ArrayList<Integer>> allNumbersXP = new ArrayList<>();
	
	static ArrayList<Integer> validNodes = new ArrayList<>();
	
	static ArrayList<ArrayList<Integer>> allCostsCost = new ArrayList<>();
	static ArrayList<ArrayList<Integer>> allCostsXP = new ArrayList<>();
	
	static ArrayList<ArrayList<String>> allOrdersCost = new ArrayList<>();
	static ArrayList<ArrayList<String>> allOrdersXP = new ArrayList<>();
	
	static ArrayList<ArrayList<Double>> Distances = new ArrayList<>();
	
	static int mostProfit1 = 0;
	static int mostXP1 = 0;
	static int mostProfit2 = 0;
	static int mostXP2 = 0;
	
	static ArrayList<Integer> bestXPRoute = new ArrayList<>();
	static ArrayList<Integer> bestCostRoute = new ArrayList<>();
	
	static ArrayList<Integer> seedNumVisits = new ArrayList<>();
	
	
	private static DataInputStream in;
	
	
	static double[][][] totalCostInformation;
	static double[][][] totalXPInformation;
	
	public static void ReadFromBinNew(File file1, String file1Name, File file2, String file2Name, String priority) {
		int index = 0;
		try {
			in = new DataInputStream(new FileInputStream(file1));
			try {
				while(true)
					if(priority.equals("Cost")) {
						System.out.println(in.readInt());
						totalCostInformation[nameToInt(file1.getName())][nameToInt(file2.getName())][index] = in.readInt() * Equations.CostBonus(file1Name, file2Name);
						System.out.println(index);
					}
					else if(priority.equals("Number"))
						totalXPInformation[nameToInt(file1.getName())][nameToInt(file2.getName())][index] = in.readInt();
			} catch (Exception e) {}
			in.close();
    	} catch(Exception e) {
    		System.out.println("readFromBinNew: ooooh u stupid");
    	}
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	public static boolean isSameNode(int n1, int n2) {
		return n1 == n2 || 
				(n1 == 17 && n2 == 18) || (n1 == 17 && n2 == 19) || (n1 == 18 && n2 == 17) || (n1 == 18 && n2 == 19) || (n1 == 19 && n2 == 17) || (n1 == 19 && n2 == 18) ||
				(n1 == 88 && n2 == 89) || (n1 == 89 && n2 == 88);
	}
	
	public static void writeToFile(String priority) {
    	try {
	    	switch(priority) {
	    		case "Cost":
					File f1 = new File(outputFolderCost+"/"+finalFileName+" Solved.txt");
				    f1.createNewFile();
			    	BufferedWriter buffer1 = new BufferedWriter(new FileWriter(f1));  
			    	ArrayList<Integer> seedNumVisitsCopy = (ArrayList<Integer>) seedNumVisits.clone();
			    	buffer1.write("Priority: Cost, Slots: "+maxSlots+", Weight: "+maxWeight+", Level: "+Level+", Use All Items: "+useMax+", Max Route Size: "+maxRouteSize+"\n\nMax Distance: "+maxDistance+"\nTotal Silver: "+mostProfit1+"  |  Total XP: "+mostXP1+"\n\n");
			    	for(int i = 0; i < bestCostRoute.size()-1; i++) {
			    		buffer1.write(filesCost[bestCostRoute.get(i)].getName().split(" Solved")[0]+" -> "+filesCost[bestCostRoute.get(i+1)].getName().split(" Solved")[0]+"\n");
			    		try {
			    			buffer1.write("		| "+allOrdersCost.get(bestCostRoute.get(i)).get(seedNumVisitsCopy.get(bestCostRoute.get(i))).split(" - ")[1]+"\n");
			    		} catch(Exception e) {
			    			buffer1.write("		| Nothing\n");
			    		}
			    		seedNumVisitsCopy.set(bestCostRoute.get(i), seedNumVisitsCopy.get(bestCostRoute.get(i)) + 1);
			    	}
				    buffer1.write("\n"+bestCostRoute);
				    buffer1.close();
				    break;
	    		case "Number":
				    File f2 = new File(outputFolderXP+"/"+finalFileName+" Solved.txt");
				    f2.createNewFile();
				    BufferedWriter buffer2 = new BufferedWriter(new FileWriter(f2)); 
				    seedNumVisitsCopy = (ArrayList<Integer>) seedNumVisits.clone();
				    buffer2.write("Priority: Number, Slots: "+maxSlots+", Weight: "+maxWeight+", Level: "+Level+", Use All Items: "+useMax+", Max Route Size: "+maxRouteSize+"\n\nMax Distance: "+maxDistance+"\nTotal Silver: "+mostProfit2+"  |  Total XP: "+mostXP2+"\n\n");
			    	for(int i = 0; i < bestXPRoute.size()-1; i++) {
			    		buffer2.write(filesCost[bestXPRoute.get(i)].getName().split(" Solved")[0]+" -> "+filesCost[bestXPRoute.get(i+1)].getName().split(" Solved")[0]+"\n");
			    		try {
			    			buffer2.write("		| "+allOrdersXP.get(bestXPRoute.get(i)).get(seedNumVisitsCopy.get(bestXPRoute.get(i))).split(" - ")[1]+"\n");
			    		} catch(Exception e) {
			    			buffer2.write("		| Nothing\n");
			    		}
			    			seedNumVisitsCopy.set(bestXPRoute.get(i), seedNumVisitsCopy.get(bestXPRoute.get(i))+1);
			    	}
			    	buffer2.write("\n"+bestXPRoute);
			    	buffer2.close();
			    	break;
	    	}
    	} catch(Exception e) {
    		e.printStackTrace();
    		System.out.println("writeToFile: ooooh u stupid");
    	}
	}
	
	public static void setDistances() {
		for(int i = 0; i < filesCost.length; i++) {
			seedNumVisits.add(0);
			ArrayList<Double> temp = new ArrayList<>();
			for(int j = 0; j < filesCost.length; j++) {
				String a = filesCost[i].getName();
				String b = filesCost[j].getName();
				temp.add(Equations.HorizontalDistance(a.substring(0,a.length()-11), b.substring(0,b.length()-11)));
			}
			Distances.add(temp);
		}	
	}
	
	public static void setNumbers(String priority) {
		BufferedReader objReader;
		for(File file: new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+priority+","+effectiveLevel).listFiles()) {
			if(priority.equals("Cost")) {
				allNumbersCost.add(new ArrayList<>());
				allOrdersCost.add(new ArrayList<>());
			} else if(priority.equals("Number")) {
				allNumbersXP.add(new ArrayList<>());
				allOrdersXP.add(new ArrayList<>());
			}
			try {
				String strCurrentLine;
				objReader = new BufferedReader(new FileReader(file));
				objReader.readLine();
				objReader.readLine();
				while((strCurrentLine = objReader.readLine()) != null) {
					if(strCurrentLine.split(" ")[1].equals("Trial,"))
						break;
					if(priority.equals("Cost"))
						allOrdersCost.get(nameToInt(file.getName().split(" Solved")[0])).add(strCurrentLine);
					else if(priority.equals("Number"))
						allOrdersXP.get(nameToInt(file.getName().split(" Solved")[0])).add(strCurrentLine);
					int temp = 0;
					for(int i = 0; i < strCurrentLine.split("x").length-1; i++) {
						try {
							temp += Integer.parseInt(strCurrentLine.split("x")[i].split(" ")[strCurrentLine.split("x")[i].split(" ").length-1]);
						} catch(Exception e) {}
					}
					if(priority.equals("Cost"))
						allNumbersCost.get(nameToInt(file.getName().split(" Solved")[0])).add(temp);
					else if(priority.equals("Number"))
						allNumbersXP.get(nameToInt(file.getName().split(" Solved")[0])).add(temp);
				}
				objReader.close();
			} catch (Exception e) { }	
		}
	}
	
	public static void ReadFromBin(File file, String priority) {
		ArrayList<Integer> temp = new ArrayList<>();
		try {
			in = new DataInputStream(new FileInputStream(file));
			try {
				while(true) {
					temp.add(in.readInt());
				}
			} catch (Exception e) {}
			if(priority.equals("Cost"))
				allCostsCost.add(temp);
			else if(priority.equals("Number"))
				allCostsXP.add(temp);
			in.close();
    	} catch(Exception e) {
    		System.out.println("readFromBin2: ooooh u stupid");
    	}
	}
	
	public static int closestNode(int node) {
		int closestNode = 0;
		double bestDistance = Double.POSITIVE_INFINITY;
		for(int i = 0; i < filesCost.length; i++)
			if(i != node && Distances.get(node).get(closestNode) < bestDistance) {
				bestDistance = Distances.get(node).get(closestNode);
				closestNode = i;
			}
		return closestNode;
	}
	
	public static double equationCost(int n1, int n2) {
		if(isSameNode(n1, n2) || allCostsCost.get(n1).size() == 0 || allCostsCost.get(n2).size() == 0)
			return 0;
		
		int n2Closest = closestNode(n2);
		
		String a = filesCost[n1].getName().split(" Solved")[0];
		String b = filesCost[n2].getName().split(" Solved")[0];
		String c = filesCost[n2Closest].getName().split(" Solved")[0];
		
		return allCostsCost.get(n1).get(0) * Equations.CostBonus(a, b) / Distances.get(n1).get(n2)
				+ allCostsCost.get(n2).get(0) * Equations.CostBonus(b, c) / Distances.get(n2).get(n2Closest);
	}
	
	public static double equationNumber(int n1, int n2) {
		if(isSameNode(n1, n2) || allNumbersXP.get(n1).size() == 0 || allNumbersXP.get(n2).size() == 0)
			return 0;
		
		int n2Closest = closestNode(n2);
		
		String a = filesCost[n1].getName().split(" Solved")[0];
		String b = filesCost[n2].getName().split(" Solved")[0];
		String c = filesCost[n2Closest].getName().split(" Solved")[0];
		
		return allNumbersXP.get(n1).get(0) * TradeXP.getXP(a, b, Level) / Distances.get(n1).get(n2) + allNumbersXP.get(n2).get(0) * TradeXP.getXP(b, c, Level) / Distances.get(n2).get(n2Closest);
	}
	
	public static void nodeHeuristic(String priority) {
		switch(priority) {
			case "Cost":
				for(int i = 0; i < filesCost.length; i++)
					for(int j = 0; j < filesCost.length; j++)
						costHeuristic.get(i).set(j, equationCost(i, j));
				break;
			case "Number":
				for(int i = 0; i < filesCost.length; i++)
					for(int j = 0; j < filesCost.length; j++)
						numberHeuristic.get(i).set(j, equationNumber(i, j));
				break;
		}
	}
	
	public static void cost(int startingNode) {
		
		int bestMostProfit = 0;
		
		validNodes.add(startingNode);
		ArrayList<Integer> nodesToAdd = new ArrayList<>();
		
		for(int i = 0; i < filesCost.length; i++) {
			if(i == startingNode || Distances.get(startingNode).get(i) > maxDistance)
				continue;
			nodesToAdd.add(i);
		}
		
		for(int i = 0; i < filesCost.length; i++) { // initialize heuristic arrays
			costHeuristic.add(new ArrayList<>());
			numberHeuristic.add(new ArrayList<>());
			for(int j = 0; j < filesCost.length; j++) {
				costHeuristic.get(i).add(0d);
				numberHeuristic.get(i).add(0d);
			}
		}
		
		nodeHeuristic("Cost");
		
		System.out.println("Adding other stuff...");
		
		for(int i = 0; i < nodesToAdd.size(); i++)
			for(int j = 0; j < nodesToAdd.size(); j++)
				if(costHeuristic.get(startingNode).get(nodesToAdd.get(i)) > costHeuristic.get(startingNode).get(nodesToAdd.get(j))) {
					int temp = nodesToAdd.get(i);
					nodesToAdd.set(i, nodesToAdd.get(j));
					nodesToAdd.set(j, temp);
				}
		
		System.out.println("Done!");
		
		ArrayList<Integer> route = new ArrayList<>();
		route.add(startingNode);
		
		System.out.println(nodesToAdd);
		//System.exit(1);
		for(int i = 0; i < nodesToAdd.size(); i++) {
			/*
			validNodes.add(nodesToAdd.get(0));
			nodesToAdd.remove(0);
			
			validNodes.add(nodesToAdd.get(0));
			nodesToAdd.remove(0);
			
			validNodes.add(nodesToAdd.get(0));
			nodesToAdd.remove(0);
			*/
			while(nodesToAdd.size() > validNodes.size()) {
				validNodes.add(nodesToAdd.get(0));
				nodesToAdd.remove(0);
			}
			
			//System.out.println(bestCostRoute);
			
			Collections.sort(validNodes);
			RouteCost(0, 0, 0, (ArrayList<Integer>) seedNumVisits.clone(), (ArrayList<Integer>) route.clone(), false);
			break; //temp
			/*
			System.out.println(bestCostRoute);
			if(bestMostProfit < mostProfit1) {
				bestMostProfit = mostProfit1;
				
				mostProfit1 = 0;
				mostXP1 = 0;
			} else
				return;*/
		}
	}
	
	public static void RouteCost(int costProfit, int xpProfit, double distance, ArrayList<Integer> numVisits, ArrayList<Integer> route, boolean lastWasEmpty) {
		int n1 = costProfit;
		int n2 = xpProfit;
		double n3 = distance;
		int prevNode = route.get(route.size()-1);
		
		for(int i: validNodes) {
			
			if(isSameNode(i,prevNode))
				continue;
			
			costProfit = n1;
			xpProfit = n2;
			distance = n3 + Distances.get(prevNode).get(i);
			
			if(distance < maxDistance && (route.size() <= maxRouteSize || maxRouteSize == 0)) {
				
				ArrayList<Integer> newNumVisits;
				ArrayList<Integer> newRoute;
				
				//distance += Distances.get(i).get(prevNode);
				
				String a = filesCost[prevNode].getName().split(" Solved")[0];
				String b = filesCost[i].getName().split(" Solved")[0];
				
				newNumVisits = (ArrayList<Integer>) numVisits.clone();
				newRoute = (ArrayList<Integer>) route.clone();
				newRoute.add(i);
				
				boolean wasError = false;
				
				try {
					costProfit += allCostsCost.get(prevNode).get(numVisits.get(prevNode)) * Equations.CostBonus(a,b);
				} catch(Exception e) {
					if(lastWasEmpty)
						return;
					RouteCost(costProfit, xpProfit, distance, newNumVisits, newRoute, true);
					wasError = true;
				}
				
				if(costProfit > mostProfit1) {
					mostProfit1 = costProfit;
					mostXP1 = xpProfit;
					bestCostRoute = (ArrayList<Integer>) newRoute.clone();
					System.out.println(newRoute);
					System.out.println(mostProfit1);
				}
				
				if(newRoute.size() <= 5) {
					System.out.println(newRoute);
					System.out.println(mostProfit1);
				}
				
				if(!wasError) {
					newNumVisits.set(prevNode, newNumVisits.get(prevNode)+1);
					RouteCost(costProfit, xpProfit, distance, newNumVisits, newRoute, false);
				}
			}
		}
	}
	
	public static void RouteXP(int costProfit, int xpProfit, double distance, ArrayList<Integer> numVisits, ArrayList<Integer> route, boolean lastWasEmpty) {
		int n1 = costProfit;
		int n2 = xpProfit;
		double n3 = distance;
		int prevNode = route.get(route.size()-1);
		
		for(int i: validNodes) {
			
			if(isSameNode(i,prevNode))
				continue;
			
			costProfit = n1;
			xpProfit = n2;
			distance = n3 + Distances.get(prevNode).get(i);
			
			if(distance < maxDistance && (route.size() <= maxRouteSize || maxRouteSize == 0)) {
				
				ArrayList<Integer> newNumVisits;
				ArrayList<Integer> newRoute;
				
				//distance += Distances.get(i).get(prevNode);
				
				String a = filesCost[prevNode].getName().split(" Solved")[0];
				String b = filesCost[i].getName().split(" Solved")[0];
				
				newNumVisits = (ArrayList<Integer>) numVisits.clone();
				newRoute = (ArrayList<Integer>) route.clone();
				newRoute.add(i);
				
				boolean wasError = false;
				
				try {
					xpProfit += TradeXP.getXP(a, b, Level) * allNumbersXP.get(prevNode).get(numVisits.get(prevNode));
				} catch(Exception e) {
					if(lastWasEmpty)
						return;
					RouteXP(costProfit, xpProfit, distance, newNumVisits, newRoute, true);
					wasError = true;
				}
				
				if(xpProfit > mostXP2) {
					mostProfit2 = costProfit;
					mostXP2 = xpProfit;
					bestXPRoute = (ArrayList<Integer>) newRoute.clone();
					System.out.println(newRoute);
					System.out.println(mostXP2);
				}
				
				if(newRoute.size() <= 5) {
					System.out.println(newRoute);
					System.out.println(mostXP2);
				}
				
				if(!wasError) {
					newNumVisits.set(prevNode, newNumVisits.get(prevNode)+1);
					RouteXP(costProfit, xpProfit, distance, newNumVisits, newRoute, false);
				}
			}
		}
	}
	
	/*
	public static void Route(int costProfit1, int xpProfit1, int costProfit2, int xpProfit2, double distance, ArrayList<Integer> numVisits, ArrayList<Integer> route, boolean continueCost, boolean continueXP) {
		if(!continueCost && !continueXP)
			return;
		
		int n1 = costProfit1;
		int n2 = xpProfit1;
		double n3 = distance;
		int n4 = costProfit2;
		int n5 = xpProfit2;
		int prevNode = route.get(route.size()-1);
		
		for(int i: validNodes) {
			
			if(isSameNode(i,prevNode))
				continue;
			
			costProfit1 = n1;
			xpProfit1 = n2;
			distance = n3 + Distances.get(prevNode).get(i);
			costProfit2 = n4;
			xpProfit2 = n5;
			
			if(distance < maxDistance && (route.size() <= maxRouteSize || maxRouteSize == 0)) {
				
				ArrayList<Integer> newNumVisits;
				ArrayList<Integer> newRoute;
				
				//distance += Distances.get(i).get(prevNode);
				
				String a = filesCost[prevNode].getName().split(" Solved")[0];
				String b = filesCost[i].getName().split(" Solved")[0];
				
				boolean wasErrorCost = false;
				boolean wasErrorNumber = false;
				
				try {
					costProfit1 += allCostsCost.get(prevNode).get(numVisits.get(prevNode)) * Equations.CostBonus(a,b);
					xpProfit1 += TradeXP.getXP(a, b, Level) * allCostsXP.get(prevNode).get(numVisits.get(prevNode));
				} catch(Exception e) {
					newNumVisits = (ArrayList<Integer>) numVisits.clone();
					newRoute = (ArrayList<Integer>) route.clone();
					newNumVisits.set(prevNode, newNumVisits.get(prevNode)+1);
					newRoute.add(i);
					Route(costProfit1, xpProfit1, costProfit2, xpProfit2, distance, newNumVisits, newRoute, false, continueXP);
					wasErrorCost = true;
				}
			
				try {
					costProfit2 += allCostsXP.get(prevNode).get(numVisits.get(prevNode)) * Equations.CostBonus(a,b);
					xpProfit2 += TradeXP.getXP(a, b, Level) * allNumbersXP.get(prevNode).get(numVisits.get(prevNode));
				} catch(Exception e) {
					newNumVisits = (ArrayList<Integer>) numVisits.clone();
					newRoute = (ArrayList<Integer>) route.clone();
					newNumVisits.set(prevNode, newNumVisits.get(prevNode)+1);
					newRoute.add(i);
					Route(costProfit1, xpProfit1, costProfit2, xpProfit2, distance, newNumVisits, newRoute, continueCost, false);
					wasErrorNumber = true;
				}
				
				newRoute = (ArrayList<Integer>) route.clone();
				newNumVisits = (ArrayList<Integer>) numVisits.clone();
				
				newNumVisits.set(prevNode, newNumVisits.get(prevNode)+1);
				newRoute.add(i);
				
				if(costProfit1 > mostProfit1 && continueCost) {
					mostProfit1 = costProfit1;
					mostXP1 = xpProfit1;
					bestCostRoute = (ArrayList<Integer>) newRoute.clone();
					System.out.println(newRoute);
					System.out.println(mostProfit1);
				} 
				if(xpProfit2 > mostXP2 && continueXP) {
					mostProfit2 = costProfit2;
					mostXP2 = xpProfit2;
					bestXPRoute = (ArrayList<Integer>) newRoute.clone();
					System.out.println(newRoute);
				}
				
				if(newRoute.size() <= 6) {
					System.out.println(newRoute);
					System.out.println(mostProfit1);
				}
				
				if(!wasErrorCost && !wasErrorNumber)
					Route(costProfit1, xpProfit1, costProfit2, xpProfit2, distance, newNumVisits, newRoute, continueCost, continueXP);
			}
		}
	}
	*/
	
	public static int nameToInt(String name) {
		for(int i = 0; i < filesCost.length; i++)
			if(filesCost[i].getName().equals(name+" Solved.bin"))
				return i;
		return -1;
	}
	
	public static void main(String[] args) {
		
		Region = "Land";
		maxSlots = 20;
		maxWeight = 1121.1;
		Level = 0;
		useMax = false;
		maxDistance = 475000;
		maxRouteSize = 18;
		
		effectiveLevel = Level;
		if(useMax)
			effectiveLevel = 0;
		
		new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Routes").mkdir();
		
		filesCost = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Cost,"+effectiveLevel).listFiles();
		filesXP = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Number,"+effectiveLevel).listFiles();
		
		solvedFolderCost = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Cost,"+effectiveLevel);
		solvedFolderXP = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Number,"+effectiveLevel);
		
		outputFolderCost = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Routes/Solved "+Region+" Routes - "+maxSlots+","+maxWeight+",Cost,"+Level+","+useMax+","+maxDistance+","+maxRouteSize);
		outputFolderXP = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Routes/Solved "+Region+" Routes - "+maxSlots+","+maxWeight+",Number,"+Level+","+useMax+","+maxDistance+","+maxRouteSize);

		outputFolderCost.mkdir();
		outputFolderXP.mkdir();
		
		
		int maxTripsC = 1;
		int maxTripsN = 1;
		//File newFile = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Cost,"+effectiveLevel+"/"+"Altinova Solved.txt");
		for(File file: solvedFolderCost.listFiles()) {
			int i = 0;
			try {
				BufferedReader objReader = new BufferedReader(new FileReader(file));
				while(objReader.readLine() != null)
					i++;
				objReader.close();
			} catch (Exception e) {System.out.println("K");}
			if(i - 3 > maxTripsC)
				maxTripsC = i - 3;
		}
		
		for(File file: solvedFolderXP.listFiles()) {
			int i = 0;
			try {
				BufferedReader objReader = new BufferedReader(new FileReader(file));
				while(objReader.readLine() != null)
					i++;
				objReader.close();
			} catch (Exception e) {System.out.println("K");}
			if(i - 3 > maxTripsN)
				maxTripsN = i - 3;
		}
		
		totalCostInformation = new double[filesCost.length][filesCost.length][maxTripsC];
		totalXPInformation = new double[filesCost.length][filesCost.length][maxTripsN];
		
		//for(File file1: filesCost)
			//for(File file2: filesCost)
			//	ReadFromBinNew(file1, file2, "Cost");
		
		System.out.println(totalCostInformation[6][6][0]);
		System.exit(1);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		System.out.println("Setting Distances Array and Numbers Array...");
		
		setDistances();
		setNumbers("Cost");
		setNumbers("Number");
		
		System.out.println("Done!");
		
		for(File tempFile: filesCost)
			ReadFromBin(tempFile,"Cost");
		
		for(File tempFile: filesXP)
			ReadFromBin(tempFile,"XP");
		
		//[0, 83, 52, 70, 67, 11, 63, 87, 14, 55, 34, 29, 61, 16, 61, 16]
		//[90, 12, 35, 90, 49, 90, 13, 54, 85, 51, 5, 41, 60, 41, 23, 41]
		//for(File newFile: filesCost) {
		File newFile = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+",Cost,"+effectiveLevel+"/"+"Velia Solved.bin");
			finalFileName = newFile.getName().split(" Solved")[0];
			if(allCostsCost.get(nameToInt(finalFileName)).size() == 0)
			{}	//continue;
			
			if(new File(outputFolderCost+"/"+finalFileName+" Solved.txt").exists() && new File(outputFolderXP+"/"+finalFileName+" Solved.txt").exists())
			{}	//continue;
			
			ArrayList<Integer> route = new ArrayList<>();
			route.add(nameToInt(finalFileName));
			
			cost(nameToInt(finalFileName));
			
			//Route(0, 0, 0, (ArrayList<Integer>) seedNumVisits.clone(), route);
			
			if(new File(outputFolderCost+"/"+finalFileName+" Solved.txt").exists() && new File(outputFolderXP+"/"+finalFileName+" Solved.txt").exists())
			{}	//continue;
			
			writeToFile("Cost");
			
			System.out.println("\n\n\n"+bestCostRoute);
			System.out.println(mostProfit1);
			System.out.println(mostXP1+"\n\n");
			
			System.out.println(bestXPRoute);
			System.out.println(mostProfit2);
			System.out.println(mostXP2);
		//}
	}
}
