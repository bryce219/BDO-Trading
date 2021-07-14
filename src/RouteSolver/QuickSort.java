package RouteSolver;

import java.util.LinkedList;

class QuickSort { 
	
	double[][][] totalInformation;
	double[][] Distances;
	
    int partition(LinkedList<Path> list, int low, int high) {
        Path pivot = list.get(high);
        int i = low - 1;
        Path temp;
        for (int j = low; j < high; j++)
            if(totalInformation[list.get(j).nodeStart][list.get(j).nodeEnd][list.get(j).visits] / Distances[list.get(j).nodeStart][list.get(j).nodeEnd] 
            		> totalInformation[pivot.nodeStart][pivot.nodeEnd][pivot.visits] / Distances[pivot.nodeStart][pivot.nodeEnd]) {
                i++;
				temp = list.get(i);
				list.set(i, list.get(j));
				list.set(j, temp);
            }
  
		temp = list.get(i+1);
		list.set(i+1, list.get(high));
		list.set(high, temp);
  
        return i+1;
    } 
  
    void sort(LinkedList<Path> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            sort(list, low, pi-1);
            sort(list, pi+1, high);
        }
    }
    
    LinkedList<Path> quickSort(LinkedList<Path> list, double[][][] totalInformation, double[][] Distances) {
    	this.totalInformation = totalInformation;
    	this.Distances = Distances;
    	sort(list, 0, list.size()-1);
    	return list;
    }
} 
