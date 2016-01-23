package controller;

import java.util.HashMap;
import java.util.Map;

import declarations.BatteryManager;
import declarations.GlucagonBank;
import declarations.InsulinGlucagon;
import declarations.InsulinBank;

public class PrimeController {

	// static variables have uniform value throughout
	private static PrimeController privatecontrollerinstance = null;;
	private static Map<String, Number> accesorystatus = new HashMap<String, Number>();
	private static Double current_battery_level = 0.0;
	private static Double calculatedinsulindose = 0.0;
	private static Double calculatedglucagondose = 0.0;
	private static String currentbolus;

	public PrimeController() {

		if (accesorystatus == null) {
			accesorystatus = new HashMap<String, Number>();
		}
		accesorystatus.clear();
		// new Dose();
	}

	public Map<String, Number> computeDosage() {
		// step 1. Check blood glucose level
		int bgLevel = BloodGlucoseSensor.getInstance().checkBloodGlucose();
		// Put all the parameters in a map which is to be displayed on UI
		accesorystatus.put("glucoselevel", bgLevel);
		accesorystatus.put("insulinlevel", InsulinBank.getInstance().checkInsulinLevel(calculatedinsulindose));
		accesorystatus.put("glucagonlevel", GlucagonBank.getInstance().checkGlucagonLevel(calculatedglucagondose));

		// Step 2. Compute dose based on blood glucose level
		calculatedinsulindose = InsulinGlucagon.computeIDose(bgLevel);
		// System.out.println("bgLevel :" +bgLevel + "calculatedinsulindose: " +
		// calculatedinsulindose);
		calculatedglucagondose = InsulinGlucagon.computeGDose(bgLevel);
		// System.out.println("bgLevel :" +bgLevel + "calculatedglucagondose: " +
		// calculateglucagondose);

		// Step 3. Inject Insulin/glucagon to change BSL
		if (calculatedinsulindose > 0.0){
			BloodGlucoseSensor.getInstance().bslChangeOnIDose(calculatedinsulindose);}
		if (calculatedglucagondose > 0.0){
			
			BloodGlucoseSensor.getInstance().bslChangeOnGDose(calculatedglucagondose);
		}
		current_battery_level = BatteryManager.getInstance().getBatteryLevel();
		accesorystatus.put("batterylevel", current_battery_level);

		return accesorystatus;
	}

	public static synchronized PrimeController getInstance() {

		if (privatecontrollerinstance == null) {
			privatecontrollerinstance = new PrimeController();
		}
		return privatecontrollerinstance;

	}

	public static String getCurrentbolus() {
		return currentbolus;
	}

	public static void setCurrentbolus(String currentbolus) {
		PrimeController.currentbolus = currentbolus;
	}

	public static void injectInsulin(double insulin) {
		BloodGlucoseSensor.getInstance().bslChangeOnIDose(insulin);
	}

	public static void injectGlucagon(double glucagon) {
		BloodGlucoseSensor.getInstance().bslChangeOnGDose(glucagon);

	}

	public static void changeBGLOnIdle() {
		BloodGlucoseSensor.getInstance().bslChangeOnIdle();

	}
}
