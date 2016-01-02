package selfDefinedClasses;

import assembly.AssemblyConstants;

public class InsulinGlucagon {
	private static double calculatedinsulindose;

	public void calculateInsulinBolus(int newBSL) {

	}

	public static double calculateInsulinBasal(int current_glucose_level) {

		if (current_glucose_level > AssemblyConstants.ONE_HUNDRED_TWENTY) {
			calculatedinsulindose = (current_glucose_level - AssemblyConstants.HUNDRED) / 50;

		} else {
			calculatedinsulindose = AssemblyConstants.ZERO;
		}

		return calculatedinsulindose;

	}

	public void calculateGlucagon(int newBSL) {

	}

}
