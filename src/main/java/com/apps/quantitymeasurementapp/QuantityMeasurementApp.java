package com.apps.quantitymeasurementapp;

public class QuantityMeasurementApp {
	
	public static void demonstrateFeetEquality() {
		
		Feet f1 = new Feet(2.5);
		
		Feet f2 = new Feet(2.5);
		
		System.out.println("Equals: " + f1.equals(f2));
		
	}
	
	public static void demonstrateInchesEquality() {
		
		Inches i1 = new Inches(2.5);
		
		Inches i2 = new Inches(2.5);

		System.out.println("Equals: " + i1.equals(i2));
		
	}

	public static void main(String[] args) {
		
		QuantityMeasurementApp.demonstrateFeetEquality();
		QuantityMeasurementApp.demonstrateInchesEquality();

	}

}
