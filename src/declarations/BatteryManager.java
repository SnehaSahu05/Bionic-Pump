package declarations;

import declarations.AssemblyConstants;

public class BatteryManager {
	private static BatteryManager batterymanager;
	private static Double batterylevel;
	private Double MaxBatteryLevel = AssemblyConstants.HUNDRED;

	public BatteryManager() {
		batterylevel = MaxBatteryLevel;
	}

	public Double getBatteryLevel() {
		batterylevel -= 0.5;
		return (batterylevel) / 100;
	}

	public static BatteryManager getInstance() {

		if (batterymanager == null) {
			batterymanager = new BatteryManager();
		}
		return batterymanager;
	}

	public static void setNewBattery(Double value) {
		batterylevel = value;
	}

}
