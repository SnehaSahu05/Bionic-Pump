package controller;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import controller.IgpsGuiController;

public class DisplayToControllerMediator extends TimerTask {

	public interface DisplayControllable {
		void setDisplayParameters(HashMap<String, Number> parameters);
	}

	private static int delay = 1000;
	private static int period = 5000;
	private int i = 0;
	private DisplayControllable displayControllerInterface;
		
	public DisplayToControllerMediator(DisplayControllable controllable){
		displayControllerInterface = controllable;
	}

	public void run() {
		i = i + 1;
		System.out.println("current execution: " + i);
		HashMap<String, Number> accesorystatus = (HashMap<String, Number>) PrimeController.getInstance()
				.computeDosage();
		displayControllerInterface.setDisplayParameters(accesorystatus);

	}

	public void startSimulator(Timer timer) {
		timer.scheduleAtFixedRate(this, delay, period);

	}

	public void cancelTask() {
		this.cancel();
	}


}
