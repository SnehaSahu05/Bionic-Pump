package controller;

/**
 *
 * @author Sneha Sahu
 */
public class Dose {

	private double r0; // the reading before r1
	private double r1; // the previous reading
	// static int r2; // the current reading
	private double compInsulinDose = 0; // insulin dose required according to
										// the insulin dosage computation
	private double cumulativeInsulinDose; // total insulin dose that has already
											// been delivered over the day
	static double maxInsulinDailyDose = 35;
	static double maxInsulinSingleDose = 5;
	static double minInsulinDose = 1;
	private double compGlucagonDose = 0; // glucagon dose required according to
											// the glucagon dosage computation
	private double cumulativeGlucagonDose; // total glucagon dose that has
											// already been delivered over the
											// day
	static double maxGlucagonDailyDose = 35;
	static double maxGlucagonSingleDose = 5;
	static double minGlucagonDose = 1;

	static int safeMin = 70;
	static int safeMax = 130;

	int sugarLevel;

	double basalDose = 1;

	public double getBasalDose() {
		return basalDose;
	}

	public double getInsulinDose() {
		return compInsulinDose;
	}

	public double getGlucagonDose() {
		return compGlucagonDose;
	}

	public void computeDose(double r2) {

		/*
		 * System.out.println("r2:"+r2); System.out.println("r1:"+r1);
		 * System.out.println("r0:"+r0);
		 */

		if (r0 == 0 || r1 == 0) {

			compInsulinDose = 0;
			compGlucagonDose = 0;

		} else if (r2 < safeMin) { // reading is below the safe minimum

			// do not inject insulin
			compInsulinDose = 0;
			// Alarm is on and a warning message must be displayed

			// sugar level decreasing.
			if (r2 < r1) {

				// compute glucagon dose
				compGlucagonDose = Math.round((r1 - r2) / 4);

				// a minimum glucagon dose must be delivered if dose is rounded
				// to zero
				if (compGlucagonDose == 0) {

					compGlucagonDose = minGlucagonDose;
				}

			} else if (r2 == r1) { // sugar level stable

				compGlucagonDose = minGlucagonDose;

			} else if ((r2 > r1)) { // sugar level is increasing

				if ((r2 - r1) >= (r1 - r0)) { // rate of increase is increasing

					compGlucagonDose = 0;

				} else { // rate of increase is decreasing

					compGlucagonDose = minGlucagonDose;
				}

			}

		} else if (r2 > safeMin && r2 < safeMax) { // reading is within the safe
													// zone

			if (r2 == r1) { // sugar level stable or falling

				compInsulinDose = 0;
				compGlucagonDose = 0;

			} else if ((r2 > r1)) { // sugar level increasing

				compGlucagonDose = 0; // do not inject glucagon

				if ((r2 - r1) < (r1 - r0)) { // rate of increasing falling

					compInsulinDose = 0;

				} else { // rate of increase increasing, compute insulin dose

					compInsulinDose = Math.round((r2 - r1) / 4);

					// a minimum dose must be delivered if dose is rounded to
					// zero
					if (compInsulinDose == 0) {

						compInsulinDose = minInsulinDose;

					}
				}

			} else if (r2 < r1) { // sugar level is falling

				compInsulinDose = 0;

				if ((r1 - r2) < (r0 - r1)) { // rate of decreasing falling

					compGlucagonDose = 0;

				} else { // rate of decrease increasing, compute glucagon dose

					compGlucagonDose = Math.round((r1 - r2) / 4);

					// a minimum dose must be delivered if dose is rounded to
					// zero
					if (compGlucagonDose == 0) {

						compGlucagonDose = minGlucagonDose;

					}
				}
			}

		} else if (r2 > safeMax) { // sugar level increasing

			// sugar level increasing.
			if (r2 > r1) {

				// compute insulin dose
				compInsulinDose = Math.round((r2 - r1) / 4);

				// a minimum insulin dose must be delivered if dose is rounded
				// to zero
				if (compInsulinDose == 0) {

					compInsulinDose = minInsulinDose;
				}

			} else if (r2 == r1) { // sugar level stable

				compInsulinDose = minInsulinDose;

			} else if ((r2 < r1)) { // sugar level is falling

				if ((r2 - r1) <= (r1 - r0)) { // rate of decrease is increasing

					compInsulinDose = 0;

				} else { // rate of decrease is decreasing

					compInsulinDose = minInsulinDose;
				}

			}

			// do not inject glucagon
			compGlucagonDose = 0;

		}

		r0 = r1;
		r1 = r2;

	}

	public void autorunSafetyCheck() {

		if (compInsulinDose > 0) { // insulin

			double sum = compInsulinDose + cumulativeInsulinDose;

			if (sum > maxInsulinDailyDose) { // maximum daily insulin dose is
												// exceeded by the sum of the
												// dose calculated and the
												// cumulative doze

				// the dose to be injected should be calculated by subtracting
				// the sum of
				// the calculated doze and cumulative dose of insulin from
				// maximum daily dose
				compInsulinDose = maxInsulinDailyDose - sum;
				// as suggested by professor we should not inject dose because
				// maximum daily dose is exceeded
				compInsulinDose = 0;
				// MainFrame.showWarning(Messages.WARNING_07); // Alert the user

			} else { // the sum of the dose calculated and cumulative doze is
						// less than or equal to maximum daily doze

				if (compInsulinDose <= maxInsulinSingleDose) { // the dose
																// calculated is
																// less than or
																// equal to
																// maximum
																// single dose

					// deliver the insulin calculated;

				} else { // the single dose computed is too high

					compInsulinDose = maxInsulinSingleDose; // deliver the
															// maximum single
															// dose
					// MainFrame.showWarning(Messages.WARNING_08); // Alert the
					// user
				}
			}

			cumulativeInsulinDose = cumulativeInsulinDose + compInsulinDose;

		} else if (compGlucagonDose > 0) { // glucagon

			double sum = compGlucagonDose + cumulativeGlucagonDose;

			if (sum > maxGlucagonDailyDose) { // maximum daily glucagon dose is
												// exceeded by the sum of the
												// dose calculated and the
												// cumulative doze

				// the dose to be injected should be calculated by subtracting
				// the sum of
				// the calculated doze and cumulative dose of glucagon from
				// maximum daily dose
				compGlucagonDose = maxGlucagonDailyDose - sum;
				// as suggested by professor we should not inject dose because
				// maximum daily dose is exceeded
				compGlucagonDose = 0;
				// MainFrame.showWarning(Messages.WARNING_09); // Alert the user

			} else { // the sum of the dose calculated and cumulative doze is
						// less than or equal to maximum daily doze

				if (compGlucagonDose <= maxGlucagonDailyDose) { // the dose
																// calculated is
																// less than or
																// equal to
																// maximum
																// single dose

					// deliver the glucagon calculated;

				} else { // the single dose computed is too high

					compGlucagonDose = maxGlucagonSingleDose; // deliver the
																// maximum
																// single dose
					// MainFrame.showWarning(Messages.WARNING_10); // Alert the
					// user
				}
			}

			cumulativeGlucagonDose = cumulativeGlucagonDose + compGlucagonDose;
		}

	}

	public void manualModeSafetyCheck() {

		if (compInsulinDose > 0) { // insulin

			if ((cumulativeInsulinDose + compInsulinDose) > maxInsulinDailyDose) { // maximum
																					// daily
																					// dose
																					// is
																					// exceeded
				// MainFrame.showWarning(Messages.WARNING_07); // Alert the user
			}

			if (compInsulinDose > maxInsulinSingleDose) { // maximum single dose
															// is exceeded
				// MainFrame.showWarning(Messages.WARNING_08); // Alert the user
			}

		} else if (compGlucagonDose > 0) { // glucagon

			if ((cumulativeGlucagonDose + compGlucagonDose) > maxGlucagonDailyDose) { // maximum
																						// daily
																						// dose
																						// is
																						// exceeded
				// MainFrame.showWarning(Messages.WARNING_09); // Alert the user
			}

			if (compGlucagonDose > maxGlucagonSingleDose) { // maximum single
															// dose is exceeded
				// MainFrame.showWarning(Messages.WARNING_10); // Alert the user
			}
		}
	}

	public void setCumulativeDose() {
		cumulativeInsulinDose = cumulativeInsulinDose + compInsulinDose;
		cumulativeGlucagonDose = cumulativeGlucagonDose + compGlucagonDose;
	}

}
