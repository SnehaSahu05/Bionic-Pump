package declarations;

public class GlucagonBank {
	private static GlucagonBank GBInstance = null;
	private static Double currentGlvl = 0.0;
	private double maxGlvl = AssemblyConstants.HUNDRED;

	public GlucagonBank() {
		currentGlvl = maxGlvl;
	}

	public Double checkGlucagonLevel(double calculatedGDose) {
		synchronized (currentGlvl) {
			currentGlvl -= calculatedGDose;
			if (calculatedGDose > 0) {
				// included in accesorystatus in ComputeDosage
				//IgpsGuiController.addMessage(String.format("Injected %s Dose of Glucagon", calculatedGDose), Color.GREEN);
			}
		}
		return currentGlvl / 100.0;
	}

	public static void setGlucagonLevel(Double value) {
		currentGlvl = value;
	}

	public static synchronized GlucagonBank getInstance() {
		if (GBInstance == null) {
			GBInstance = new GlucagonBank();
		}
		return GBInstance;

	}
}
