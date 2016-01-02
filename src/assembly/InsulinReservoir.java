package assembly;

public class InsulinReservoir {

	private static InsulinReservoir insulin_reservoir_instance = null;
	private static Double current_insulin_level = 0.0;
	private double max_insulin_level = AssemblyConstants.HUNDRED;

	public InsulinReservoir() {
		current_insulin_level = max_insulin_level;
	}

	public Double checkInsuliLevel(double calculatedinsulindose) {

		synchronized (current_insulin_level) {
			current_insulin_level -= calculatedinsulindose;

		}

		return current_insulin_level / 100.0;

	}

	public void setInsulinLevel(Double insulindose) {
		current_insulin_level -= insulindose;
	}

	public static synchronized InsulinReservoir getInstance() {

		if (insulin_reservoir_instance == null) {
			insulin_reservoir_instance = new InsulinReservoir();
		}
		return insulin_reservoir_instance;

	}
}
