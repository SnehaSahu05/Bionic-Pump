package controller;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import controller.IgpsGuiController;

public class DisplayToControllerMediator extends TimerTask {

	private static DisplayToControllerMediator displaytocontrollermediatorinstance = null;
	private static int delay = 1000;
	private static int period = 5000;
	private int i = 0;

	public void run() {
		i = i + 1;
		System.out.println("current execution: " + i);
		HashMap<String, Double> accesorystatus = (HashMap<String, Double>) PrimeController.getInstance()
				.computeDosage();
		IgpsGuiController.setParameters(accesorystatus);

	}

	public void startSimulator(Timer timer) {
		timer.scheduleAtFixedRate(controller.DisplayToControllerMediator.getInstance(), delay,
				period);

	}

	public void cancelTask() {
		getInstance().cancel();
		displaytocontrollermediatorinstance = null;
	}

	public static DisplayToControllerMediator getInstance() {

		if (displaytocontrollermediatorinstance == null) {
			displaytocontrollermediatorinstance = new DisplayToControllerMediator();
		}
		return displaytocontrollermediatorinstance;

	}

}
