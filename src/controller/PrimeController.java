package controller;

import java.util.HashMap;
import java.util.Map;

import declarations.AssemblyConstants;
import declarations.BatteryManager;
import declarations.GlucagonBank;
import declarations.InsulinGlucagon;
import javafx.scene.paint.Color;
import declarations.InsulinBank;

public class PrimeController {

	// static variables have uniform value throughout
	private static PrimeController privatecontrollerinstance = null;;
	private static Map<String, Number> accesorystatus = new HashMap<String, Number>();
	private static Double current_battery_level = 0.0;
	private static Double calculatedinsulindose = 0.0;
	private static Double calculatedglucagondose = 0.0;

	public PrimeController() {

		if (accesorystatus == null) {
			accesorystatus = new HashMap<String, Number>();
		}
		accesorystatus.clear();
	}

	public Map<String, Number> computeDosage() {
		// step 1. Check blood glucose level
		int bgLevel = BloodGlucoseSensor.getInstance().checkBloodGlucose();
		// Put parameters in a map 'accesorystatus' which is to be displayed on
		// UI
		accesorystatus.put("glucoselevel", bgLevel);
		accesorystatus.put("insulinlevel", InsulinBank.getInstance().checkInsulinLevel(calculatedinsulindose));
		accesorystatus.put("glucagonlevel", GlucagonBank.getInstance().checkGlucagonLevel(calculatedglucagondose));

		if (AssemblyConstants.CARBS == 0 && AssemblyConstants.T == AssemblyConstants.Tmax) {// other
																							// than
																							// meal
																							// consumption
			// Step 2. Compute dose based on blood glucose level
			calculatedinsulindose = InsulinGlucagon.computeIDose(bgLevel);
			// System.out.println("bgLevel :" + bgLevel +
			// "calculatedinsulindose: " + calculatedinsulindose);
			calculatedglucagondose = InsulinGlucagon.computeGDose(bgLevel);
			// System.out.println("bgLevel :" + bgLevel +
			// "calculatedglucagondose: " + calculatedglucagondose);

			// Step 3. Inject + change BSL
			if (calculatedinsulindose > 0.0) {
				// System.out.println("@PC.java - oldBSL:" +bgLevel + "I:"
				// +calculatedinsulindose);
				injectInsulin(calculatedinsulindose);
			}
			if (calculatedglucagondose > 0.0) {
				// System.out.println("@PC.java - oldBSL:" +bgLevel + "G:"
				// +calculatedglucagondose);
				injectGlucagon(calculatedglucagondose);
			}
			// System.out.println("@PC.java - newBSL:" +bgLevel);
		} else if (AssemblyConstants.T < AssemblyConstants.Tmax) {// when meal
																	// consumed,
																	// only
																	// check
																	// blood
																	// sugar
			BloodGlucoseSensor.getInstance().bslChangeOnActivity(AssemblyConstants.CARBS, AssemblyConstants.T);
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
