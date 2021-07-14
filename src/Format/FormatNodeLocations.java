package Format;

import java.io.*;
import java.util.Scanner;

public class FormatNodeLocations {
	
	static String Region = "Land";
	static String location = "C:/Users/Bryce219/Desktop/BDO Trading/Node Positions/"+Region;
	
	public static boolean fileExists(String name) {
		return new File(location+"/"+name).exists();
	}
	
	public static void writeToFile(String name, Scanner scan) {
		String input = scan.nextLine();
		try {
			File f = new File(location+"/"+name);
		    if(f.createNewFile()) {
		    	BufferedWriter buffer = new BufferedWriter(new FileWriter(f));  
		    	buffer.write(input);
		    	buffer.close();
		    }
    	} catch(Exception e) {
    		System.out.println("writeToFile: ooooh u stupid");
    	}
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		File[] files = new File("C:/Users/Bryce219/eclipse-workspace/BDO Trading/src/"+Region+" Trades").listFiles();
		for(File file: files) {
			System.out.println("\n"+file.getName().substring(0,file.getName().length()-4)+":");
			String name = file.getName();
			if(!fileExists(name)) {
				writeToFile(name, scan);
			} else
				System.out.println("	>> Skipping...");
		}
		scan.close();
	}
	
}
