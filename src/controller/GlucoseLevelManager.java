package controller;

import assembly.BloodGlucoseSensor;

public class GlucoseLevelManager {
	//private int GlucoseLevel;
	private String message;

	public static int checkGlucoseLevel() {
	
		return BloodGlucoseSensor.getInstance().checkBloodGlucose();
	}

	public static void changeBloodGlucoseOnActivity(Boolean isinsulin,double carbohydratevalue) {
		
		BloodGlucoseSensor.getInstance().bloodGlucoseChangeOnActivity(isinsulin,carbohydratevalue);
	}
public static void changeBolusGlucose() {
		
		BloodGlucoseSensor.getInstance().bloodchangeBolus();
	}

	public static void bloodGlucoseChangeOnBasalActivity(double insulindosemanual){
		BloodGlucoseSensor.getInstance().bloodGlucoseChangeOnBasalActivity(insulindosemanual);
	}
	

}
