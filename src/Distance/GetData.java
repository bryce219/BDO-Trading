package Distance;

import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import TradeSolver.Item;


public class GetData{

	public static String file = "C:/Users/Bryce219/eclipse-workspace/BDO Trading/src/Distance/RawData.txt";
	
	public static void main(String[] args) {
		String text = "";
		try {
			  BufferedReader br = new BufferedReader(new FileReader(new File(file))); 
			  String st; 
			  while ((st = br.readLine()) != null) 
			    text += st;
			  br.close();
		} catch (Exception e){}
		
		JSONObject json = null;
		try {
			json = (JSONObject) new JSONParser().parse(text);
		} catch (ParseException e) {}
		
		ArrayList<double[]> centers = new ArrayList<>();
		ArrayList<String> names = new ArrayList<>();
		ArrayList<double[]> traderLocs = new ArrayList<>();
		ArrayList<double[]> nodeLocs = new ArrayList<>();
		ArrayList<ArrayList<Item>> itemsList = new ArrayList<>();
		
		
		double[] center;
		String name;
		double[] traderLoc;
		double[] nodeLoc;
		ArrayList<Item> itemList;
		
		JSONObject nodes = (JSONObject) json.get("nodes");
		JSONObject node;
		JSONObject traders;
		
		for(Object key : nodes.keySet()) {
			node = (JSONObject) nodes.get(key);
			traders = (JSONObject) node.get("traders");
			
			if(isTradeManager(traders)) {
				try {
					center = getCenter(node);
					name = getName(node);
					traderLoc = getTraderLoc(traders);
					nodeLoc = getNodeLoc(node);
					itemList = getItems(traders);
				} catch(Exception e) {
					continue;
				}
				centers.add(center);
				names.add(name);
				traderLocs.add(traderLoc);
				nodeLocs.add(nodeLoc);
				itemsList.add(itemList);
			}
		}
		
		for(int i = 0; i < centers.size(); i++) {
			System.out.println(names.get(i));
			for(Item j : itemsList.get(i))
				System.out.println("	"+j.toString());
		}
		
		test(centers,names);
		
	}
	
	public static boolean isTradeManager(JSONObject traders) {
		if(traders == null)
			return false;
		for(Object key : traders.keySet()) {
			String type = ((JSONObject) traders.get(key)).get("type").toString();
			if(type.equals("Trade Manager") || type.equals("Node Manager"))
				return true;
		}
		return false;
	}
	
	public static double[] getCenter(JSONObject node) {
		double[] center = new double[3];
		JSONObject centerObj = (JSONObject) node.get("center");
		center[0] = Double.parseDouble(centerObj.get("x").toString());
		center[1] = Double.parseDouble(centerObj.get("y").toString());
		center[2] = Double.parseDouble(centerObj.get("z").toString());
		return center;
	}
	
	public static String getName(JSONObject node) {
		String name = node.get("name").toString();
		return fixName(name);
	}
	
	public static String fixName(String name) {
		name = name.replace('Ã', 'a');
		name = name.replace('â', '\'');
		name = name.replaceAll("[¡€™]", "");
		return name;
	}
	
	public static double[] getTraderLoc(JSONObject traders) {
		double[] traderLoc = new double[2];
		for(Object key : traders.keySet()) {
			String type = ((JSONObject) traders.get(key)).get("type").toString();
			if(type.equals("Trade Manager") || type.equals("Node Manager")) {
				JSONObject locObj = (JSONObject) ((JSONObject) traders.get(key)).get("location");
				traderLoc[0] = Double.parseDouble(locObj.get("x").toString());
				traderLoc[1] = Double.parseDouble(locObj.get("y").toString());
				return traderLoc;
			}
		}
		return null;
	}
	
	public static double[] getNodeLoc(JSONObject node) {
		double[] nodeLoc = new double[2];
		JSONObject locObj = (JSONObject) node.get("location");
		nodeLoc[0] = Double.parseDouble(locObj.get("x").toString());
		nodeLoc[1] = Double.parseDouble(locObj.get("y").toString());
		return nodeLoc;
	}
	
	public static ArrayList<Item> getItems(JSONObject traders){
		ArrayList<Item> items = new ArrayList<Item>();
		for(Object key : traders.keySet()) {
			String type = ((JSONObject) traders.get(key)).get("type").toString();
			if(type.equals("Trade Manager") || type.equals("Node Manager")) {
				JSONArray packObj = (JSONArray) ((JSONObject) traders.get(key)).get("packs");
				for(Object itemObj : packObj) {
					items.add(new Item(
							fixName(((JSONObject) itemObj).get("name").toString()),
							Integer.parseInt( ((JSONObject) itemObj).get("price").toString() ),
							Double.parseDouble( ((JSONObject) itemObj).get("weight").toString() ),
							Integer.parseInt( ((JSONObject) itemObj).get("quantity").toString() ),
							Integer.parseInt( ((JSONObject) itemObj).get("level_requirement").toString() )
							));
				}
				return items;
			}
		}
		return null;
	}
	
	public static void OutputToTradesFile() {
		
	}
	
	public static void OutputToPositionsFile() {
		
	}
	
	public static void test(ArrayList<double[]> centers, ArrayList<String> names) {
		double lowestAlt = Double.POSITIVE_INFINITY;
		double highestAlt = Double.NEGATIVE_INFINITY;
		int lowestI = -1;
		int lowestJ = -1;
		int highestI = -1;
		int highestJ = -1;
		for(int i = 0; i < centers.size(); i++) {
			for(int j = i+1; j < centers.size(); j++) {
				System.out.println(names.get(i) + " (" + centers.get(i)[1] + ")" + ", " + names.get(j) + " (" + centers.get(j)[1] + ")");
				if(Math.abs(centers.get(i)[1] - centers.get(j)[1]) < lowestAlt) {
					lowestI = i;
					lowestJ = j;
					lowestAlt = Math.abs(centers.get(i)[1] - centers.get(j)[1]);
				}
				if(Math.abs(centers.get(i)[1] - centers.get(j)[1]) > highestAlt) {
					highestI = i;
					highestJ = j;
					highestAlt = Math.abs(centers.get(i)[1] - centers.get(j)[1]);
				}
			}
		}
		System.out.println("---");
		System.out.println(names.get(lowestI) + " (" + centers.get(lowestI)[1] + ")" + ", " + names.get(lowestJ) + " (" + centers.get(lowestJ)[1] + ")");
		System.out.println(names.get(highestI) + " (" + centers.get(highestI)[1] + ")" + ", " + names.get(highestJ) + " (" + centers.get(highestJ)[1] + ")");
	}
}