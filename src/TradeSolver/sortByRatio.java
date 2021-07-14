package TradeSolver;

import java.util.Comparator;

class sortByRatio implements Comparator<Item> { 
    public int compare(Item a, Item b) { 
        boolean temp = (float)a.value  / a.weight > (float)b.value / b.weight; 
        return temp ? -1 : 1; 
    } 
}
