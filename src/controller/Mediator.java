package controller;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import controller.PrimeController;

public class Mediator extends TimerTask {

	public interface DisplayControllable {
		void setDisplayParameters(HashMap<String, Number> parameters);
	}

	private static int delay = 1000;
	private static int period = 5000;
	private int i = 0;
	private DisplayControllable displayControllerInterface;
		
	public Mediator(DisplayControllable controllable){
		displayControllerInterface = controllable;
	}

	public void run() {
		i = i + 1;
		//System.out.println("current execution: " + i);
		// for cyclic meal consumption... code here @ fixed iterations of i
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
