package declarations;

import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.TimerTask;

import org.joda.time.DateTime;

public class Clock {

	public interface LoadingSetTimeListener {
		void setTime(String currentTime);
	}
	private static long delay = 1;		//so that clock triggers immediately
	private static long period = 1000;	//time interval for update
	private DateTime creationDate;
	//GregorianCalendar cal = new GregorianCalendar();
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
				GregorianCalendar cal = new GregorianCalendar();	// new local object of gregorian calender each time of thread run.
				creationDate = new DateTime(cal.getTimeInMillis());	// from  joda.time.Datetime check 'Clock' lib.
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
