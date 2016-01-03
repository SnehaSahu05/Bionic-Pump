package controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import assembly.AssemblyConstants;
import assembly.BloodGlucoseSensor;
import assembly.InsulinReservoir;
import selfDefinedClasses.InsulinGlucagon;

/**
 *
 * @author Sneha Sahu
 */
public class PrimeController {

	private static PrimeController privatecontrollerinstance = null;;
	private static Map<String, Number> accesorystatus = new HashMap<String, Number>();;
	private static Double current_battery_level = 0.0;
	private static Double calculatedinsulindose = 0.0;
	private Dose dosage;
	private static String currentbolus;

	public PrimeController() {

		if (accesorystatus == null) {
			accesorystatus = new HashMap<String, Number>();
		}
		accesorystatus.clear();
		dosage = new Dose();
	}

	public Map<String, Number> computeDosage() {
		// step 1. Check blood glucose level
		int bgLevel = BloodGlucoseSensor.getInstance().checkBloodGlucose();
		accesorystatus.put("glucoselevel", bgLevel);

		// Step 2. Compute dose based on blood glucose level

		calculatedinsulindose = InsulinGlucagon.calculateInsulinBolus(bgLevel);

		// Step 3. Inject Insulin and change the blood glucose level on this
		// injection

		if (calculatedinsulindose > 0.0)
			BloodGlucoseSensor.getInstance().bglChangeOnInsulinDose(calculatedinsulindose);

		// Put all the parameters in a map which is to be displayed on UI
		current_battery_level = BatteryManager.getInstance().getBatteryLevel();

		accesorystatus.put("insulinlevel", InsulinReservoir.getInstance().checkInsuliLevel(calculatedinsulindose));
		// accesorystatus.put("glucoselevel",
		// BloodGlucoseSensor.getInstance().checkBloodGlucose());
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

	public static void injectBolus(double insulin) {

		BloodGlucoseSensor.getInstance().bloodGlucoseChangeOnBasalActivity(insulin);

	}

	public static void injectGlucagon(double glucagon) {
		BloodGlucoseSensor.getInstance().chnageBGLOnGlucagonInjection(glucagon);

	}

	public static void changeBGLOnIdle() {
		BloodGlucoseSensor.getInstance().changeBGLOnIdle();

	}
}
