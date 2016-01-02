package selfDefinedClasses;

import assembly.AssemblyConstants;
import controller.IgpsGuiController;

public class InsulinGlucagon {
	private static double calculatedinsulindose;

	public static double calculateInsulinBolus(int current_glucose_level) {
		if (IgpsGuiController.isMealConsumed) {
			if (current_glucose_level > AssemblyConstants.RANGE_ONE_MAX) {
				calculatedinsulindose = (current_glucose_level - AssemblyConstants.HUNDRED) / 50;

			} else {
				calculatedinsulindose = AssemblyConstants.ZERO;
			}
		}else{
			if (current_glucose_level > AssemblyConstants.RANGE_TWO_MAX) {
				calculatedinsulindose = (current_glucose_level - AssemblyConstants.HUNDRED_NINGHTY) / 50;

			} else {
				calculatedinsulindose = AssemblyConstants.ZERO;
			}
			
		}

		return calculatedinsulindose;

	}

	public static double calculateGlucagon(int newBSL) {
		// TODO calculate formula
		return 0;
	}

}
