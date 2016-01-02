package assembly;

public class BloodGlucoseSensor {
	private static BloodGlucoseSensor BGSensorInstance = null;
	private int bloodglucose = AssemblyConstants.NINETY;;

	public BloodGlucoseSensor() {

	}

	public int checkBloodGlucose() {

		return measureBloodGlucose();
	}

	public void bloodGlucoseChangeOnActivity(boolean isinsulin, double carbs) {
		if (!isinsulin)
			bloodglucose = (int) (bloodglucose + 200 * Math.exp(-2.77 * 10 / carbs));
		else
			bloodglucose = (int) (bloodglucose - 200 * Math.exp((-1.45) / carbs));
	}

	public void bloodGlucoseChangeOnBasalActivity(double insulin) {

		bloodglucose = (int) (bloodglucose - 40 * (insulin));

	}


	public void bloodchangeBolus() {
		bloodglucose = AssemblyConstants.HUNDRED_INTEGER;

	}

	private int measureBloodGlucose() {

		return bloodglucose;

	}

	public static synchronized BloodGlucoseSensor getInstance() {

		if (BGSensorInstance == null) {
			BGSensorInstance = new BloodGlucoseSensor();
		}
		return BGSensorInstance;

	}

}
