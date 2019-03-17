package de.jcing.util;

public class Maths {
	
	public static int roundUp(double number) {
		int num = (int) number;
		return num == number ? num : num + 1;
	}
	
	public static int roundDown(double number) {
		return (int) number;
	}
	
}
