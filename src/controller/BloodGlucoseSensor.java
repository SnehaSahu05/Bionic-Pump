package controller;

import declarations.AssemblyConstants;

public class BloodGlucoseSensor {
	private static BloodGlucoseSensor BGSensorInstance = null;

	private int bloodsugar = AssemblyConstants.HUNDRED_INTEGER;

	public BloodGlucoseSensor() {
	}

	public int checkBloodGlucose() {
		return measureBloodGlucose();
	}

	public void bslChangeOnActivity(double carbs) {
		/* k1=0.0453; k2=0.0224; t=30.8min~2times[5sec~15min] */
		// double k1 = 0.0453;
		// double k2 = 0.0224;
		if (carbs > 0)
			// bloodsugar = (int) (bloodsugar + ( 2 * carbs * (k1/(k2-k1)) *
			// (Math.exp(-k1 * 30.8)-Math.exp(-k2 * 30.8)) ));
			bloodsugar = (int) (bloodsugar + 200 * Math.exp(-2.77 * 10 / carbs));
		else
			bloodsugar -= 6;
	}

	public void bslChangeOnIDose(double insulin) {
		if (insulin > 0)
			bloodsugar = (int) (bloodsugar - 200 * Math.exp(-1.45 / insulin));
	}
	// bloodsugar = (int) (bloodsugar - 40 * (insulin));

	private int measureBloodGlucose() {
		return bloodsugar;
	}

	public static synchronized BloodGlucoseSensor getInstance() {

		if (BGSensorInstance == null) {
			BGSensorInstance = new BloodGlucoseSensor();
		}
		return BGSensorInstance;

	}

	public void bslChangeOnGDose(double glucagon) {
		bloodsugar += glucagon * 2;
	}

	public void bslChangeOnIdle() {
		bloodsugar -= 2;

	}

}
