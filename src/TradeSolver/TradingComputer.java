package TradeSolver;
import java.util.ArrayList;
import java.util.Locale;
import java.io.*;
import java.text.NumberFormat;
import java.util.Scanner;
import static java.util.Collections.*;

public class TradingComputer{
	
	static int maxSlots, Level, Trial = 1;
	static double maxWeight;
	static String Region, Priority;
	static ArrayList<Item> items;
	static ArrayList<String> taken;
	static ArrayList<Integer> takenCost;
	
	public static void readFile(File file) {
		BufferedReader objReader;
		try {
			String strCurrentLine;
			objReader = new BufferedReader(new FileReader(file));
			items = new ArrayList<Item>();
			taken = new ArrayList<String>();
			takenCost = new ArrayList<Integer>();
			while((strCurrentLine = objReader.readLine()) != null) {
				String[] temp = strCurrentLine.split(",");
				if(Level == 0 || Level >= Integer.parseInt(temp[4]))
					items.add(new Item(temp[2],
						Integer.parseInt(temp[3]),
						Double.parseDouble(temp[1]),
						Integer.parseInt(temp[0])));
			}
			objReader.close();
		} catch (Exception e) {}
	}
	
	public static boolean fileExists(String name) {
		name = name.substring(0,name.length()-4)+" Solved.txt";
		return new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+Priority+","+Level+"/"+name).exists();
	}
	
	public static int extractInt(String takenString) {
		int total = 0;
		String temp = "";
		boolean active = false;
		for(int i = 0; i < takenString.length(); i++) {
			if(takenString.charAt(i) == 'x')
				active = false;
			if(active)
				temp += takenString.charAt(i);
			else {
				try {
					total += Integer.parseInt(temp);
				} catch(Exception e) {}
				temp = "";
			}
			if(takenString.charAt(i) == ' ') {
				active = true;
				temp = "";
			}
		}
		return total;
	}
	
	public static void writeToFile(String name) {
    	int counter = -1;
    	try {
			File f = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+Priority+","+Level+"/"+name.substring(0,name.length()-4)+" Solved.txt");
		    if(f.createNewFile()) {
		    	BufferedWriter buffer = new BufferedWriter(new FileWriter(f));  
		    	buffer.write("Priority: "+Priority+", Slots: "+maxSlots+", Weight: "+maxWeight+", Level: "+Level+"\n\n");
		    	for(int i = 0; i < taken.size(); i++) {
		    		buffer.write("Trial "+(i+1)+" - "+taken.get(i)+"\n");
		    		counter = i+1;
		    	}
		    	if(counter == 1)
		    		buffer.write(counter + " Trial, END");
		    	else
			    	buffer.write(counter + " Trial, END");
		    	buffer.close();
		    }
    	} catch(Exception e) {
    		System.out.println("writeToFile: ooooh u stupid");
    	}
	}
	
	public static void writeToBin(String name) {
		try {
			FileOutputStream a = new FileOutputStream("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+Priority+","+Level+"/"+name.substring(0,name.length()-4)+" Solved.bin");
			DataOutputStream data = new DataOutputStream(a);
			if(Priority.equals("Cost"))
				for(int i = 0; i < takenCost.size(); i++)
					data.writeInt(takenCost.get(i));
			else if(Priority.equals("Number"))
				for(int i = 0; i < takenCost.size(); i++)
					data.writeInt(extractInt(taken.get(i)));
			data.flush();
			data.close();
    	} catch(Exception e) {
    		System.out.println("writeToBin: ooooh u stupid");
    	}
	}
	
	public static String getWeight(String a) {
		String[] A = a.split(",");
		double weight = 0;
		for(int i = 0; i < A.length-1; i++)
			weight += items.get(Integer.parseInt(A[i])).weight;
		return String.format("%.2f", weight);
	}
	
	public static String StringToNames(String a){
		String[] A = a.split(",");
		ArrayList<String> Aa = new ArrayList<String>();
		for(int i = 0; i < A.length; i++)
			Aa.add(A[i]);
		int cost = Integer.parseInt(Aa.get(Aa.size()-1));
		takenCost.add(cost);
		
		String b = "";
		Aa.remove(Aa.size()-1);
		sort(Aa);
		for(int i = 0; i < items.size(); i++) {
			int S = frequency(Aa, i+"");
			if(S != 0)
				b += S+"x "+items.get(i).getName()+", ";
			items.get(i).setAmount(items.get(i).getAmount()-S);
		}
		b = NumberFormat.getNumberInstance(Locale.US).format(cost)+" | "+getWeight(a)+": " + b.substring(0,b.length()-2);
		for(int i = 0; i < items.size()-1; i++)
			if(items.get(i).getAmount() == 0) {
				items.remove(i);
				i--;
			}
		taken.add(b);
		return b;
	}
	
	public static boolean atLeastOneItem() {
		for(Item item: items)
			if(item.amount != 0)
				return true;
		return false;
	}
	
	public static void calculator(int finalItemNum) {
		int counter = 0;
		while(atLeastOneItem()) {
			String a = "";
			if(Priority.equals("Cost"))
				a = newKnapsack.maxCost(maxSlots, maxWeight, items, finalItemNum);	
			else if (Priority.equals("Number"))
				a = newKnapsack.maxNumber(maxSlots, maxWeight, items, finalItemNum);
			else {
				System.out.println("WHOOPS!  Priority isn't rigghhhhtttt.... doo doo poo poo heeeaaaad");
				break;
			}
			if(a.equals("-1"))
				break;
			else
				System.out.println("("+(++counter)+")	>> "+StringToNames(a));
		}
		System.out.println("DONE");
	}
	
	public static void customItems() {
		items = new ArrayList<Item>();
		taken = new ArrayList<String>();
		takenCost = new ArrayList<Integer>();
		
		//FORMAT: Name, Value, Weight, Amount
		
		items.add(new Item("A",4,12,1));
		items.add(new Item("B",2,1,1));
		items.add(new Item("C",2,2,1));
		items.add(new Item("D",1,1,1));
		items.add(new Item("E",10,4,1));
	}
	
	public static void main(String[] args) {
		//~~
		/*
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Wagon Slots:\n>>> ");
		maxSlots = scan.nextInt();
		
		System.out.print("\nMax Weight:\n>>> ");
		maxWeight = scan.nextDouble();
		
		System.out.print("\nRegion ('Land' or 'Sea'):\n>>> ");
		Region = scan.next();
		
		System.out.print("\nPriority ('Number' or 'Cost'):\n>>> ");
		Priority = scan.next();
		
		System.out.print("\nLevel: ('0' for Max)\n>>> ");
		Level = scan.nextInt();
		
		scan.close();
		*/
		//~~
		
		maxSlots = 20; //20
		maxWeight = 1200; //1200
		Region = "Land"; //Land
		Priority = "Cost"; // Cost || Number
		Level = 55; //55 

		//~~~
		/*
		customItems();
		
		//String name = "Velia";
		
		//File file = new File("C:/Users/Bryce219/eclipse-workspace/BDO Trading/src/"+Region+" Trades/"+name+".txt");
		//readFile(file);
		
		calculator(0);
		System.exit(1);
		*/
		//~~~
		
		new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades").mkdir();
		new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)").mkdir();
		
		File textFolder = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+Priority+","+Level);
		textFolder.mkdir();
		
		File Binfolder = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved "+Region+" Trades - "+maxSlots+","+maxWeight+","+Priority+","+Level);
		Binfolder.mkdir();
		
		File[] files = new File("C:/Users/Bryce219/eclipse-workspace/BDO Trading/src/"+Region+" Trades").listFiles();
		
		for(File newFile: files) {
			
			System.out.println("\n"+newFile.getName().substring(0,newFile.getName().length()-4)+":");
			if(!fileExists(newFile.getName())) {
				readFile(newFile);
				calculator(0);
				writeToFile(newFile.getName());
				writeToBin(newFile.getName());
			} else
				System.out.println("	>> Skipping...");
		}
		
	}
}
