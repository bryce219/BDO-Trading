package Format;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class FormatTrades {

	static final File CONVERSION_FILE = new File("src/Format/Conversion File.txt");
	
	static String[] seaNodes = {};
	
	static String nodeName;
	static String Region = "Sea";
	static String pathName;
	
	public static void readFile() {
		BufferedReader objReader = null;
		try {
			new File(pathName).createNewFile();
			String strCurrentLine;
			objReader = new BufferedReader(new FileReader(CONVERSION_FILE));
			
			int i = 0;
			ArrayList<Integer> numItem = new ArrayList<>(), level = new ArrayList<>(), cost = new ArrayList<>();
			ArrayList<Double> weight = new ArrayList<>();
			ArrayList<String> name = new ArrayList<>();
			
			while((strCurrentLine = objReader.readLine()) != null) {
				if(i == 0) {
					numItem.add(Integer.parseInt(strCurrentLine.split("×")[0]));
					name.add(strCurrentLine.split("×")[1].substring(1, strCurrentLine.split("×")[1].length()));
				} else if(i == 1)
					level.add(convertLevel(strCurrentLine));
				else if(i == 2)
					weight.add(Double.parseDouble(strCurrentLine.split("L")[0]));
				else if(i == 3) {
					cost.add(Integer.parseInt(strCurrentLine.replace(",","")));
					writeToFile(numItem, weight, cost, name, level);
					i = -2;
				}
				i++;
			}
			objReader.close();
		} catch (Exception e) {
			System.out.println("readFile: ooooh u stupid");
		}
	}
	
	public static void writeToFile(ArrayList<Integer> numItem, ArrayList<Double> weight, ArrayList<Integer> cost, ArrayList<String> name, ArrayList<Integer> level) {
    	try {
			File f = new File(pathName);
	    	BufferedWriter buffer = new BufferedWriter(new FileWriter(f));  
	    	for(int i = 0; i < numItem.size(); i++)
	    		buffer.write(numItem.get(i)+","+weight.get(i)+","+name.get(i)+","+cost.get(i)+","+level.get(i)+"\n");
	    	buffer.close();
    	} catch(Exception e) {
    		System.out.println("writeToFile: ooooh u stupid");
    	}
	}
	
	public static int convertLevel(String stringLevel) {
		int remainder = Integer.parseInt(stringLevel.split(" ")[1]);
		int mainGroup = -1;
		switch(stringLevel.split(" ")[0]) {
			case "Beginner":
				mainGroup = 0;
				break;
			case "Apprentice":
				mainGroup = 10;
				break;
			case "Skilled":
				mainGroup = 20;
				break;
			case "Professional":
				mainGroup = 30;
				break;
			case "Artisan":
				mainGroup = 40;
				break;
			case "Master":
				mainGroup = 50;
				break;
			case "Guru":
				mainGroup = 80;
				break;
		}
		return mainGroup + remainder;
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		File[] Files = new File("src/"+Region+" Trades").listFiles();
		for(File file: Files) {
			nodeName = file.getName().substring(0,file.getName().length()-4);
			pathName = "src/"+Region+" Trades/"+nodeName+".txt";
			System.out.println(nodeName);
			while(true)
				if(scan.next().equals("a")) {
					readFile();
					break;
				}
		}
		scan.close();
	}
	
}
