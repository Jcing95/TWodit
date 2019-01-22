package de.jcing.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Pointmask {
	
	public static <F> Set<F> getFromArray(F[][] array, int xOffset, int yOffset, double radius){
		HashSet<F> set = new HashSet<>();
		for(int i = xOffset; i< array.length; i++) {
			for(int j = yOffset; j < array[i].length; j++) {
				double xD = i-radius-xOffset;
				double yD = j-radius-yOffset;
				double dist = Math.sqrt(xD*xD+yD+yD);
				if(dist > radius)
					break;
				set.add(array[i][j]);
			}
		}
		return set;
	}
	
	public static <F> Set<F> getFromPointMap(Map<Point, F> array, int xOffset, int yOffset, double radius){
		HashSet<F> set = new HashSet<>();
		for(int i = xOffset-(int)radius; i< xOffset+2*radius; i++) {
			for(int j = yOffset-(int)radius; j < yOffset+2*radius; j++) {
				double xD = i-xOffset;
				double yD = j-yOffset;
				double dist = Math.sqrt(xD*xD+yD+yD);
				if(dist > radius)
					break;
				F canditate = array.get(new Point(i,j));
				if(canditate != null)
					set.add(canditate);
			}
		}
		return set;
	}
	
}
