package declarations;

import declarations.AssemblyConstants;
import controller.IgpsGuiController;

public class InsulinGlucagon {
	private static double calculatedinsulindose;
	private static double calculatedglucagondose;

	public static Double computeIDose(int currentBSL) {
		if (IgpsGuiController.isMealConsumed) {// give Bolus Dose
			if (currentBSL >= AssemblyConstants.RANGE_ONE_MAX) {
				calculatedinsulindose = Math.min(AssemblyConstants.MAX_IDOSE,
						(currentBSL - AssemblyConstants.HUNDRED) / AssemblyConstants.ONE_IDOSE);
			} else {
				calculatedinsulindose = AssemblyConstants.ZERO;
			}
		} // give Basal Dose if no meal taken but BSL between R1max and 130
		else if (currentBSL >= AssemblyConstants.RANGE_ONE_MAX && currentBSL <= AssemblyConstants.ONE_HUNDRED_THIRTY) {
			calculatedinsulindose = AssemblyConstants.MIN_IDOSE;
		} else {
			calculatedinsulindose = AssemblyConstants.ZERO;
		}
		return calculatedinsulindose;
	}

	public static Double computeGDose(int currentBSL) {
		if (currentBSL < AssemblyConstants.RANGE_ONE_MIN) {
			calculatedglucagondose = Math.min(AssemblyConstants.MAX_GDOSE,
					(AssemblyConstants.RANGE_ONE_MIN - currentBSL) / AssemblyConstants.ONE_GDOSE);
		} else {
			calculatedglucagondose = AssemblyConstants.ZERO;
		}
		return calculatedglucagondose;
	}

}
