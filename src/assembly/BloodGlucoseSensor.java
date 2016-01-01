package assembly;

public class BloodGlucoseSensor {
	private static BloodGlucoseSensor BGSensorInstance = null;
	private static double bloodglucose = AssemblyConstants.Ninety;;

	public BloodGlucoseSensor() {

	}

	public double checkBloodGlucose() {

		return measureBloodGlucose();
	}

	public void bloodGlucoseChangeOnActivity(Boolean isinsulin, double carbs) {
		if (!isinsulin)
			bloodglucose = bloodglucose + 200 * Math.exp(-2.77 * 10 / carbs);
		else
			bloodglucose = bloodglucose - 200 * Math.exp((-1.45) / carbs);
	}

	public void bloodGlucoseChangeOnBasalActivity(double insulin) {

		bloodglucose = bloodglucose - 40 * (insulin);

	}

	public static void main(String[] args) {
		// getInstance().bloodchangeBolus(true, 50);
		// getInstance().bloodchangeBolus(false, 50);

	}

	public void bloodchangeBolus() {
		bloodglucose = AssemblyConstants.HUNDRED;

	}

	private static double measureBloodGlucose() {

		/*
		 * Double glucoseabovebelownormal = Math.random()*50; BloodGlucose =
		 * NormalBloodGlucoseMin+ glucoseabovebelownormal;
		 */
		// bloodglucose = 150 -200*Math.exp((-1.45)/0.3148);;
		return bloodglucose;

	}

	public static synchronized BloodGlucoseSensor getInstance() {

		if (BGSensorInstance == null) {
			BGSensorInstance = new BloodGlucoseSensor();
		}
		return BGSensorInstance;

	}

}
