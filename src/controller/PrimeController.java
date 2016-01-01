package controller;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import assembly.AssemblyConstants;
import assembly.BloodGlucoseSensor;
import assembly.InsulinReservoir;

/**
 * 
 * @author Sneha Sahu
 */
public class PrimeController {

	private static PrimeController privatecontrollerinstance = null;;
	private static Map<String, Double> accesorystatus = new HashMap<String, Double>();;
	private static Double current_battery_level = 0.0;
	private static Double calculatedinsulindose = 0.0;
	private Dose dosage;

	public PrimeController() {

		if (accesorystatus == null) {
			accesorystatus = new HashMap<String, Double>();
		}
		accesorystatus.clear();
		dosage = new Dose();
	}

	public Map<String, Double> computeDosage() {
		dosage.computeDose(180);// BloodGlucoseSensor.getInstance().checkBloodGlucose());
		BloodGlucoseSensor.getInstance().bloodGlucoseChangeOnActivity(true, dosage.getInsulinDose());
		accesorystatus.put("insulinlevel", InsulinReservoir.getInstance().checkInsuliLevel(calculatedinsulindose));
		current_battery_level = BatteryManager.getInstance().getBatteryLevel();
		accesorystatus.put("glucoselevel", BloodGlucoseSensor.getInstance().checkBloodGlucose());
		accesorystatus.put("batterylevel", current_battery_level);
		return accesorystatus;
	}

	/*
	 * public Map startDevice() { current_glucose_level =
	 * GlucoseLevelManager.checkGlucoseLevel(); if
	 * (InsulinPumpGUIController.getDosageMode().equalsIgnoreCase("Auto")) {
	 * 
	 * // if(current_glucose_level > 120.0) if (current_glucose_level <
	 * AssemblyConstants.Ninety) { // error message } else if
	 * (current_glucose_level > AssemblyConstants.OneHundredTwenty) {
	 * 
	 * calculatedinsulindose = (current_glucose_level -
	 * AssemblyConstants.HUNDRED) / 50; if (calculatedinsulindose <
	 * minimumcalculateddose) { calculatedinsulindose = minimumcalculateddose; }
	 * GlucoseLevelManager.changeBloodGlucoseOnActivity(true,
	 * calculatedinsulindose);
	 * 
	 * if(current_glucose_level>previousglucoselevel &&
	 * Math.round(current_glucose_level-previousglucoselevel/50)==0 ){
	 * calculatedinsulindose = minimumcalculateddose;
	 * 
	 * } else if(current_glucose_level>previousglucoselevel &&
	 * Math.round(current_glucose_level-previousglucoselevel/50)>0 ){
	 * calculatedinsulindose = (double)
	 * Math.round(current_glucose_level-previousglucoselevel/50); } else
	 * if(current_glucose_level==previousglucoselevel ){ calculatedinsulindose =
	 * minimumcalculateddose; } else { calculatedinsulindose =
	 * minimumcalculateddose; }
	 * 
	 * 
	 * }
	 * 
	 * else if (current_glucose_level > AssemblyConstants.Ninety &&
	 * current_glucose_level < AssemblyConstants.OneHundredTwenty) { if
	 * ((current_glucose_level - AssemblyConstants.Ninety) > 17.0) {
	 * calculatedinsulindose = minimumsafecalculateddose; } else if
	 * ((current_glucose_level - AssemblyConstants.Ninety) < 17.0) {
	 * calculatedinsulindose = 0.3148; }
	 * 
	 * GlucoseLevelManager.changeBloodGlucoseOnActivity(true,
	 * calculatedinsulindose); }
	 * 
	 * System.out.println("Calculated insulin dose: " + calculatedinsulindose);
	 * // System.out.println("current gb lvel: "+current_glucose_level); //
	 * System.out.println("previous gb lvel: "+previousglucoselevel);
	 * current_insulin_level =
	 * InsulinInjectionManager.getInstance().checkInsulinLevel(
	 * calculatedinsulindose);
	 * 
	 * System.out.println("bt level: " + current_battery_level);
	 * 
	 * accesorystatus.put("insulinlevel", current_insulin_level);
	 * 
	 * }
	 * 
	 * else { System.out.println("MANUALLLLLLLLLLLLLLLLLLLLLLLLLLL"); // else
	 * part for basal profile List<BasalProfile> profilename =
	 * getBasalprofilelist();// HibernateChecker.getActivatedBasalProfile();
	 * Double currentisnulindose = new Double(0); if (profilename != null) {
	 * Iterator<BasalProfile> it = profilename.iterator(); while (it.hasNext())
	 * { BasalProfile bp = it.next(); if ((new
	 * Integer(bp.getBasalStartTime()).intValue() <=
	 * Clock.getInstance().getCurrentDate() .getHours()) && (new
	 * Integer(bp.getBasalEndTime()).intValue() >
	 * Clock.getInstance().getCurrentDate() .getHours())) {
	 * 
	 * currentisnulindose = new Double(bp.getBasalInsulinDose()).doubleValue();
	 * System.out.println(Clock.getInstance().getCurrentDate().getHours());
	 * System.out.println(currentisnulindose + "      ############");
	 * System.out.println(bp.getBasalProfileName()); break;
	 * 
	 * }
	 * 
	 * } current_insulin_level =
	 * InsulinInjectionManager.getInstance().checkInsulinLevel(
	 * calculatedinsulindose); accesorystatus.put("insulinlevel",
	 * current_insulin_level);
	 * GlucoseLevelManager.bloodGlucoseChangeOnBasalActivity(currentisnulindose
	 * / 60); // currentisnulindose= currentisnulindose/60;
	 * 
	 * InsulinDosageReadings insulindosage = new
	 * InsulinDosageReadings(currentisnulindose,
	 * Clock.getCurrentFormattedDate(), "BASAL"); //
	 * HibernateChecker.setInsulinDosage(insulindosage); } }
	 * 
	 * current_battery_level = BatteryManager.getInstance().getBatteryLevel();
	 * accesorystatus.put("glucoselevel", current_glucose_level);
	 * 
	 * accesorystatus.put("batterylevel", current_battery_level);
	 * 
	 * BloodGlucoseReadings bgread = new
	 * BloodGlucoseReadings(current_glucose_level.intValue() + "",
	 * Clock.getCurrentFormattedDate());
	 * HibernateChecker.setGlucoseData(bgread);
	 * 
	 * previousglucoselevel = current_glucose_level;
	 * 
	 * return accesorystatus;
	 * 
	 * }
	 */

	public void taskDistributor(int carbs) {
		// GlucoseLevelManager.
	}
	/*
	 * public static void main(String[] args) { List<BasalProfile> profilename =
	 * HibernateChecker.getActivatedBasalProfile();
	 * 
	 * Iterator<BasalProfile> it = profilename.iterator(); while(it.hasNext()){
	 * BasalProfile bp =it.next(); Clock.getCurrentDate(); if((new
	 * Integer(bp.getBasalStartTime()).intValue() <=
	 * Clock.getInstance().getCurrentDate().getHours()) && (new
	 * Integer(bp.getBasalEndTime()).intValue() >
	 * Clock.getInstance().getCurrentDate().getHours())){ System.out.println(
	 * "insulin dose: "+bp.getBasalInsulinDose()); }
	 * 
	 * 
	 * } }
	 */

	public static synchronized PrimeController getInstance() {

		if (privatecontrollerinstance == null) {
			privatecontrollerinstance = new PrimeController();
		}
		return privatecontrollerinstance;

	}

	private static String currentbolus;

	public static String getCurrentbolus() {
		return currentbolus;
	}

	public static void setCurrentbolus(String currentbolus) {
		PrimeController.currentbolus = currentbolus;
	}

	public static String getBolusInjected() {
		Double bloodglucose = BloodGlucoseSensor.getInstance().checkBloodGlucose();
		double insulindose = (double) (bloodglucose - AssemblyConstants.HUNDRED) / 50;
		Double bolusdose = new Double(insulindose);
		if (bolusdose > 0) {
			DecimalFormat df = new DecimalFormat("0.00");
			setCurrentbolus(df.format(bolusdose));
		} else
			setCurrentbolus("0.0");
		return getCurrentbolus();
	}

	public static void injectBolus() {

		BloodGlucoseSensor.getInstance().bloodchangeBolus();

	}
}
