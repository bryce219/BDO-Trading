package RouteSolver;

import java.io.File;

import Distance.TradeXP;

public class RouteToNums {

	public static void main(String[] args) {
		int[] arr = new int[] {33, 57, 41, 85, 49, 90, 35, 90, 54, 90, 12, 90, 13, 90, 13};
		
		File[] files = new File("C:/Users/Bryce219/Desktop/BDO Trading/Solved Trades (bin)/Solved Land Trades - 20,1121.1,Cost,0").listFiles();
		
		int level = 130;
		
		double totalXP = 0;
		for(int i = 0; i < arr.length-1; i++)
			totalXP += 350 * TradeXP.getXP(files[arr[i]].getName().split(".txt")[0], files[arr[i+1]].getName().split(".txt")[0], level);
		System.out.println(totalXP);
	}
	
}
