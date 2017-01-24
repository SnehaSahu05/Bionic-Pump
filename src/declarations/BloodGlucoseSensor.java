package declarations;

import declarations.AssemblyConstants;

public class BloodGlucoseSensor {
	private static BloodGlucoseSensor BGSensorInstance = null;
	public static final double k1 = 0.0453;
	public static final double k2 = 0.0224;

	private int bloodsugar = AssemblyConstants.NORMAL_BSL;

	public BloodGlucoseSensor() {
	}

	public int checkBloodGlucose() {
		return measureBloodGlucose();
	}

	public void bslChangeOnIdle() {
		// System.out.println("Idle @ BGS.java - OldBSL: " + bloodsugar);
		bloodsugar -= (5 * (k1 / (k2 - k1)) * (Math.exp(-k1 * 5) - Math.exp(-k2 * 5)));
		// System.out.println("Idle @ BGS.java - NewBSL: " + bloodsugar);
	}

	public void bslChangeOnActivity(double carbs, int t) {
		/* k1=0.0453; k2=0.0224; t=4x5sec[5sec~10min] */
		if (carbs != 0) {
			//System.out.println("Activity @ BGS.java - OldBSL: " + bloodsugar + "Acarbs: " + carbs + "T: " + t);
			bloodsugar += (2 * carbs * (k1 / (k2 - k1)) * (Math.exp(-k1 * 5 * t) - Math.exp(-k2 * 5 * t)));
			t++;
			AssemblyConstants.T = t;
			if (t == AssemblyConstants.Tmax) {
				carbs = 0;
				AssemblyConstants.CARBS = carbs;
			}
			//System.out.println("Activity @ BGS.java - NewBSL: " + bloodsugar + "Acarbs: " + carbs
			//		+ AssemblyConstants.CARBS + "T: " + t);
		}
	}

	public void bslChangeOnIDose(double insulin) {
		if (insulin > 0)
			// System.out.println("IDose @ BGS.java - OldBSL: " + bloodsugar);
			bloodsugar -= insulin * AssemblyConstants.ONE_IDOSE;
		// System.out.println("IDose @ BGS.java - NewBSL: " + bloodsugar);
	}

	public void bslChangeOnGDose(double glucagon) {
		// System.out.println("GDose @ BGS.java - OldBSL: " + bloodsugar);
		bloodsugar += glucagon * AssemblyConstants.ONE_GDOSE;
		// System.out.println("GDose @ BGS.java - NewBSL: " + bloodsugar);
	}

	private int measureBloodGlucose() {
		return bloodsugar;
	}

	public static synchronized BloodGlucoseSensor getInstance() {

		if (BGSensorInstance == null) {
			BGSensorInstance = new BloodGlucoseSensor();
		}
		return BGSensorInstance;

	}

}
