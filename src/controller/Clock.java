package controller;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

public class Clock {

	private static long delay = 1000;
	private static long period = 1000;
	private DateTime creationDate;
	GregorianCalendar cal = new GregorianCalendar();
	private String formattedDate;

	public Clock() {

	}

	public void startClock() {

		Timer clock = new Timer();

		clock.schedule(new TimerTask() {

			@Override
			public synchronized void run() {
				creationDate = new DateTime(cal.getTimeInMillis());
				formattedDate = creationDate.toString("HH:mm:ss");
				IgpsGuiController.setClock(formattedDate);
				creationDate = null;

			}

		}, delay);

	}

	public String getCurrentFormattedTime() {

		return formattedDate;
	}

}
