package Distance;

import java.io.*;
import java.util.Scanner;

public class Equations {

static double costRatio = 68/100000000d, // %/dist
			unitRatio = 1/100d, // Game Units/dist
			covRatio = 10000/68d; // Game Units/% (147)
	static String location = "src/Node Positions/Both/";
	
	public static double TrueDistance(String start, String end) {
		File f1 = new File(location+start+".txt"), f2 = new File(location+end+".txt");
		if(!f1.exists() || !f2.exists())
			return 0;
		double[] Point1 = new double[3], Point2 = new double[3];
		try {
			BufferedReader objReader = null;
			String strCurrentLine;
			objReader = new BufferedReader(new FileReader(f1));
			strCurrentLine = objReader.readLine();
			Point1[0] = Double.parseDouble(strCurrentLine.split(",")[0]);
			Point1[1] = Double.parseDouble(strCurrentLine.split(",")[1]);
			Point1[2] = Double.parseDouble(strCurrentLine.split(",")[2]);
			objReader.close();
			objReader = new BufferedReader(new FileReader(f2));
			strCurrentLine = objReader.readLine();
			Point2[0] = Double.parseDouble(strCurrentLine.split(",")[0]);
			Point2[1] = Double.parseDouble(strCurrentLine.split(",")[1]);
			Point2[2] = Double.parseDouble(strCurrentLine.split(",")[2]);
			objReader.close();
		} catch (Exception e) {}
		return Math.sqrt((Point1[0]-Point2[0])*(Point1[0]-Point2[0]) + (Point1[1]-Point2[1])*(Point1[1]-Point2[1]) + (Point1[2]-Point2[2])*(Point1[2]-Point2[2]));
	}
	
	public static double HorizontalDistance(String start, String end) {
		File f1 = new File(location+start+".txt"), f2 = new File(location+end+".txt");
		if(!f1.exists() || !f2.exists())
			return 0;
		double[] Point1 = new double[2], Point2 = new double[2];
		try {
			BufferedReader objReader = null;
			String strCurrentLine;
			objReader = new BufferedReader(new FileReader(f1));
			strCurrentLine = objReader.readLine();
			Point1[0] = Double.parseDouble(strCurrentLine.split(",")[0]);
			Point1[1] = Double.parseDouble(strCurrentLine.split(",")[2]);
			objReader.close();
			objReader = new BufferedReader(new FileReader(f2));
			strCurrentLine = objReader.readLine();
			Point2[0] = Double.parseDouble(strCurrentLine.split(",")[0]);
			Point2[1] = Double.parseDouble(strCurrentLine.split(",")[2]);
			objReader.close();
		} catch (Exception e) {}
		return Math.sqrt((Point1[0]-Point2[0])*(Point1[0]-Point2[0]) + (Point1[1]-Point2[1])*(Point1[1]-Point2[1]));
	}
	
	public static double CostBonus(String start, String end) {
		if(start.equals(end))
			return 0;
		return Math.min(HorizontalDistance(start,end) * costRatio + 1, 2.50);
	}
	
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		
		//while(true) {
		
			//String start = scan.nextLine();
			//String end = scan.nextLine();
			
			String start = "Grana";
			String end = "Valencia City";
			
			System.out.println(HorizontalDistance(start,end));
			System.out.println(TrueDistance(start,end));
			System.out.println(CostBonus(start,end));
			//System.out.println(TradeXP.getXP(start, end, 55));
			//System.out.println(TrueDistance(start,end));

		//}
		scan.close();
	}
}
