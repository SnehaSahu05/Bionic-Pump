package controller;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

public class Clock {

	public interface LoadingSetTimeListener {
		void setTime(String currentTime);
	}
	private static long delay = 1;
	private static long period = 1000;
	private DateTime creationDate;
	GregorianCalendar cal = new GregorianCalendar();
	private String formattedDate;
	private final LoadingSetTimeListener callClasslistener;

	public Clock(LoadingSetTimeListener listener) {
		this.callClasslistener = listener;
	}

	public void startClock() {

		Timer clock = new Timer();

		clock.schedule(new TimerTask() {

			@Override
			public synchronized void run() {
				cal = new GregorianCalendar();
				creationDate = new DateTime(cal.getTimeInMillis());
				formattedDate = creationDate.toString("HH:mm:ss");
				//IgpsGuiController.setClock(formattedDate);
				callClasslistener.setTime(formattedDate);
				creationDate = null;

			}

		}, delay,period);

	}

	public String getCurrentFormattedTime() {

		return formattedDate;
	}

}
