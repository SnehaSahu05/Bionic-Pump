package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.javafx.scene.control.skin.ProgressIndicatorSkin;

import controller.Mediator;
import controller.Mediator.DisplayControllable;

import declarations.AssemblyConstants;
import declarations.BatteryManager;
import declarations.BloodGlucoseSensor;
import declarations.Clock;
import declarations.Clock.LoadingSetTimeListener;
import declarations.GlucagonBank;
import declarations.InsulinBank;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.IgpsGuiSimulator;

public class IgpsGuiController implements Initializable, LoadingSetTimeListener, DisplayControllable {

	/*********************************************
	 * FXML GUI items : declarations+methods
	 *********************************************/
	@FXML
	private TextField txtTimer;

	@FXML
	private Button btnPNew;

	@FXML
	private CheckBox chkBoxHoney;

	@FXML
	private TextField txtNewBSL;

	@FXML
	private Button btnBattery;

	@FXML
	private CategoryAxis linePlotBSLxAxis;

	@FXML
	private MenuItem menuItmMeal;

	@FXML
	private Group grpMeal;

	@FXML
	private Button btnConsume;

	@FXML
	private Label labelBattery;

	@FXML
	private MenuButton menuBtn;

	@FXML
	private Button btnPSave;

	@FXML
	private TableColumn<?, ?> tabColPWt;

	@FXML
	private AnchorPane apMsg;

	@FXML
	private TextField txtPrevBSL;

	@FXML
	private ProgressBar progressBattery;

	@FXML
	private TableColumn<?, ?> tabColPBSL;

	@FXML
	private TextField textRangeBSL;

	@FXML
	private Button btnExit;

	@FXML
	private AnchorPane mainAnchorPane;

	@FXML
	private TableView<?> tabPInfo;

	@FXML
	private HBox controlButtons;

	@FXML
	private Button btnGlucagon;

	@FXML
	private Button btnInsulin;

	@FXML
	private CheckBox chkBoxRice;

	@FXML
	private CheckBox chkBoxChocolate;

	@FXML
	private ScrollBar scroll;

	@FXML
	private ProgressIndicator piBatteryLevel;

	@FXML
	private TableColumn<?, ?> tabColPHt;

	@FXML
	private CheckBox chkBoxBread;

	@FXML
	private LineChart<CategoryAxis, NumberAxis> linePlotBSL;

	@FXML
	private Button btnCancel;

	@FXML
	private TableColumn<?, ?> tabColPName;

	@FXML
	private AnchorPane apPatientInfo;

	@FXML
	private AnchorPane apActivity;

	@FXML
	private Group grpBank;

	@FXML
	private MenuItem menuItmWorkout;

	@FXML
	private ListView<Text> listMsgBox;

	@FXML
	private ProgressBar progressInsulinBank;

	@FXML
	private ProgressIndicator piIlvl;

	@FXML
	private CheckBox chkBoxCoke;

	@FXML
	private ProgressBar progressGlucagonBank;

	@FXML
	private ProgressIndicator piGlvl;

	@FXML
	private NumberAxis linePlotBSLyAxis;

	@FXML
	private Button btnPDel;

	@FXML
	void fillupGlucagonBank(ActionEvent event) {
		try {
			Timeline task = new Timeline(
					new KeyFrame(Duration.ZERO,
							new KeyValue(progressGlucagonBank.progressProperty(),
									new Double(progressGlucagonBank.getProgress()))),
					new KeyFrame(Duration.seconds(5), new KeyValue(progressGlucagonBank.progressProperty(), 1)));
			task.playFromStart();
			GlucagonBank.setGlucagonLevel(new Double(100));
			setAlarm(false);
		} catch (Exception ex) {
		}
	}

	@FXML
	void fillupInsulinBank(ActionEvent event) {
		try {
			Timeline task = new Timeline(
					new KeyFrame(Duration.ZERO,
							new KeyValue(progressInsulinBank.progressProperty(),
									new Double(progressInsulinBank.getProgress()))),
					new KeyFrame(Duration.seconds(5), new KeyValue(progressInsulinBank.progressProperty(), 1)));
			task.playFromStart();
			InsulinBank.setInsulinLevel(new Double(100));
			setAlarm(false);
		} catch (Exception ex) {
		}
	}

	@FXML
	void onWorkOut(ActionEvent event) {
		menuBtn.setText("Go for Workout");
		btnConsume.setText("Go");
		grpMeal.setDisable(true);
		btnConsume.setDisable(false);
		btnCancel.setDisable(false);
	}

	@FXML
	void onMealSelected(ActionEvent event) {
		menuBtn.setText("Choose items to consume");
		grpMeal.setDisable(false);
		btnConsume.setText("Consume");
		btnConsume.setDisable(false);
		btnCancel.setDisable(false);
	}

	@FXML
	// on clicking btnConsume
	void ActionTakenMealOrWorkout(ActionEvent event) {
		carbs = 0;
		// check currentBSL
		int currentGlucoseLevel = BloodGlucoseSensor.getInstance().checkBloodGlucose();

		if (btnConsume.getText() == "Go") {/* on workout */
			btnConsume.setText("Consume");
			carbs -= 10;
			if (currentGlucoseLevel >= 86) {
				addMessage("losing carbohydrates by 10 units", Color.GREEN);
			} else if (currentGlucoseLevel >= 76) {
				addMessage("losing carbohydrates by 10 units", Color.ORANGE);
			} else {
				addMessage("losing carbohydrates by 10 units", Color.RED);
			}
		} else {/* when meal taken */
			ObservableList<Node> children = grpMeal.getChildren();
			for (Iterator<Node> iter = children.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof CheckBox) {
					if (((CheckBox) obj).selectedProperty().getValue()) {
						carbs += (int) carbsValue.get(((CheckBox) obj).getText());
					}
				}
			}
			if (currentGlucoseLevel > AssemblyConstants.SIXTY) {
				addMessage(String.format("gaining %d units of carbohydrates", carbs), Color.GREEN);
			} else if (currentGlucoseLevel < AssemblyConstants.ONE_HUNDRED_TEN) {
				addMessage(String.format("gaining %d units of carbohydrates", carbs), Color.ORANGE);
			} else {
				addMessage(String.format("gaining %d units of carbohydrates", carbs), Color.RED);
			}

			if (isMealConsumed) {
				rangeTimerOnMealConsumed.cancel();
				// rangeTimerOnMealConsumed.purge();
				rangeTimerOnMealConsumed = new Timer();
			}
			rangeTimerOnMealConsumed.schedule(new TimerTask() {
				@Override
				public void run() {
					isMealConsumed = false;
					rangeTimerOnMealConsumed.cancel();
					rangeTimerOnMealConsumed = new Timer();
					// rangeTimerOnMealConsumed.purge();
				}
			}, 60000, 60000);
			isMealConsumed = true;
			grpMeal.setDisable(true);
		}
		btnConsume.setDisable(true);
		btnCancel.setDisable(true);
		menuBtn.setText("Select Activity");

		AssemblyConstants.CARBS = carbs;
		AssemblyConstants.T = 1;
		BloodGlucoseSensor.getInstance().bslChangeOnActivity(AssemblyConstants.CARBS, AssemblyConstants.T);
		clearCheckBox();
		carbs = 0;
	}

	@FXML
	void cancelActivity(ActionEvent event) {
		// re-initialise
		clearCheckBox();
		carbs = 0;
		btnConsume.setText("Consume");
		menuBtn.setText("Select Activity");
		btnConsume.setDisable(true);
		btnCancel.setDisable(true);
		grpMeal.setDisable(true);
	}

	/*
	 * @FXML :: for TableView Text-"FUTURE WORK - Patient Database" void
	 * 00000067(ActionEvent event) {
	 * 
	 * }
	 */

	@FXML
	void fillupBattery(ActionEvent event) {
		try {
			Timeline task = new Timeline(
					new KeyFrame(Duration.ZERO,
							new KeyValue(progressBattery.progressProperty(),
									new Double(progressBattery.getProgress()))),
					new KeyFrame(Duration.seconds(5), new KeyValue(progressBattery.progressProperty(), 1)));
			task.playFromStart();
			BatteryManager.setNewBattery(new Double(100));
			setAlarm(false);
		} catch (Exception ex) {
		}
	}

	@FXML
	void exitSimulator(ActionEvent event) {
		Stage scene = (Stage) mainAnchorPane.getScene().getWindow();
		scene.close();
		scene.setOnCloseRequest(e -> Platform.exit());
	}

	/*********************************************
	 * private declarations & methods
	 *********************************************/
	// local variable for general use
	private static int i = 0;
	// alert audio
	final static AudioClip alert = new AudioClip(IgpsGuiSimulator.class.getResource("alertvibrate.mp3").toString());
	// for txtTimer
	private Clock timerClock;
	// BloodSugarLevels
	private int previousBSL;
	// for LinePlot Graph
	private static Series<CategoryAxis, NumberAxis> seriesL = new XYChart.Series<CategoryAxis, NumberAxis>();
	private static Series<CategoryAxis, NumberAxis> series = new XYChart.Series<CategoryAxis, NumberAxis>();
	private static Series<CategoryAxis, NumberAxis> seriesU = new XYChart.Series<CategoryAxis, NumberAxis>();
	// for meals
	private Map<String, Integer> carbsValue = new HashMap<String, Integer>();
	private int carbs = 0;
	public static boolean isMealConsumed = false;
	private Timer rangeTimerOnMealConsumed = new Timer();

	private void addCarbstoMap() {
		try {
			carbsValue.put(chkBoxCoke.getText(), 14);
			carbsValue.put(chkBoxBread.getText(), 8);
			carbsValue.put(chkBoxRice.getText(), 25);
			carbsValue.put(chkBoxHoney.getText(), 12);
			carbsValue.put(chkBoxChocolate.getText(), 15);
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	// for message Box - list items
	private static ObservableList<Text> msgBoxItems = FXCollections.observableArrayList();

	private static void addMessage(String message, Color col) {
		if (msgBoxItems.size() > 0) {
			((Text) msgBoxItems.get(0)).setStroke(Color.GREY);
		}
		Text msg = new Text(message);
		msg.setStroke(col);
		msg.setFont(new Font(15));
		msgBoxItems.add(0, msg);
		if (msgBoxItems.size() > 16) {
			msgBoxItems.remove(16);
		}

	}

	private void setTextofIndicator(ProgressIndicator pi) {
		// change 'Done' to 100%
		ProgressIndicatorSkin pis = new ProgressIndicatorSkin(pi);
		pi.skinProperty().set(pis);
		pi.progressProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				// if value changed to 1, change text to 100%
				if (newValue.doubleValue() >= 1) {
					// Text piText = (Text) pi.lookup(".text.percentage");
					// piText.setText("100%");
					((Text) pi.lookup(".text.percentage")).setText("100%");
				}
			};
		});
	}

	private void infoBox(String infoMessage, String titleBar, String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}

	// to maintain continuous sync between FXML interface and backEnd functions
	private void startIGPSimulator() {
		Timer timer = new Timer();
		Mediator mediator = new Mediator(
				this);/*
						 * requires to implement DisplayControllable for
						 * calling* setDisplayParameters
						 */
		synchronized (timer) {
			mediator.startSimulator(timer);
		}
	}

	private void checkForDose(double iDose, double gDose) {
		if (iDose > 0) {
			addMessage(String.format("Injected %s Dose of Insulin", iDose), Color.GREEN);
		}
		if (gDose > 0) {
			addMessage(String.format("Injected %s Dose of Glucagon", gDose), Color.GREEN);
		}
	}

	// Blood sugar monitoring
	private void checkForBSLWarnings(boolean isMealTimerFinished, int glucoseLevel) {
		Color color = null;
		String message = null;
		if (glucoseLevel > AssemblyConstants.RANGE_ONE_MAX) {
			color = Color.RED;
			message = "Blood glucose is high";
			textRangeBSL.setText("HIGH SUGAR");
			setAlarm(true);
			// computeIDose
		} else if (isBSLInWarningLevel(glucoseLevel, AssemblyConstants.ONE_HUNDRED_TEN,
				AssemblyConstants.RANGE_ONE_MAX)) {
			// checks if between 110 and R1max=120
			color = Color.ORANGE;
			message = "Blood glucose in upper limit";
			textRangeBSL.setText("NORMAL");
			setAlarm(false);
		} else if (isBSLInWarningLevel(glucoseLevel, AssemblyConstants.RANGE_ONE_MIN, AssemblyConstants.EIGHTY)) {
			// checks if between R1min=70 and 80
			color = Color.ORANGE;
			message = "Blood glucose in lower limit";
			textRangeBSL.setText("NORMAL");
			setAlarm(false);
		} else if (glucoseLevel < AssemblyConstants.RANGE_ONE_MIN) {
			color = Color.RED;
			message = "Blood glucose is low";
			textRangeBSL.setText("BELOW SAFE");
			setAlarm(true);
			// computeGDose
		} else {
			color = Color.GREEN;
			message = "Blood glucose is normal";
			textRangeBSL.setText("NORMAL");
			setAlarm(false);
		}
		if (!msgBoxItems.get(0).getText().equals(message)) {
			addMessage(message, color);
		}
	}

	private boolean isBSLInWarningLevel(int glucoseLevel, int lowerBound, int higherBound) {
		if (glucoseLevel >= lowerBound && glucoseLevel <= higherBound) {
			return true;
		} else {
			return false;
		}
	}

	private void setAlarm(boolean toPlay) {
		if (toPlay) {// when true
			// if (!alert.isPlaying())
				alert.play();
			// else no action if already playing
		} else {// when false
			if (alert.isPlaying())
				alert.stop();
			// else no action if already stopped
		}
		//System.out.println("Alarm status: " +alert.isPlaying());
	}

	private void clearCheckBox() {
		ObservableList<Node> children = grpMeal.getChildren();
		for (Iterator<Node> iter = children.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof CheckBox) {
				((CheckBox) obj).setSelected(false);
			}
		}
	}

	/*********************************************
	 * UnImplemented Methods
	 *********************************************/
	/* controller.IgpsGuiController.Initializable#initialize */
	@Override
	public void initialize(URL path, ResourceBundle resource) {
			// for txtTimer
			timerClock = new Clock(this); // requires to implement
											// LoadingSetTimeListener and calls
											// setTime
			timerClock.startClock();

			// set series properties
			seriesL.setName("Lower Limit for BSL ~ 70");
			series.setName("current BSL plot");
			seriesU.setName("Upper Limit for BSL ~ 120");
			// set the series in FXML LinePlot Graph to make it observable
			linePlotBSL.getData().add(seriesL);
			linePlotBSL.getData().add(series);
			linePlotBSL.getData().add(seriesU);
			linePlotBSLxAxis.setAutoRanging(true);

			// this method sets values for Carbs: Maps carbsValue with meal
			// Items
			addCarbstoMap();

			// other Default values @ start
			txtNewBSL.setText("100");
			txtPrevBSL.setText("-");
			textRangeBSL.setText("NORMAL");

			// change 'Done' to 100%
			setTextofIndicator(piBatteryLevel);
			// battery - bind indicator+progressBar
			try {
				piBatteryLevel.progressProperty().bind(progressBattery.progressProperty());
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			// set initial battery level
			progressBattery.setProgress(1);

			setTextofIndicator(piIlvl);
			try {
				piIlvl.progressProperty().bind(progressInsulinBank.progressProperty());
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			progressInsulinBank.setProgress(1);

			setTextofIndicator(piGlvl);
			try {
				piGlvl.progressProperty().bind(progressGlucagonBank.progressProperty());
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
			progressGlucagonBank.setProgress(1);

			listMsgBox.setItems(msgBoxItems);
			// this method adds current message to the existing list at 0 ptr
			addMessage("Simulator switched On", Color.GREEN);
			grpMeal.setDisable(true);
			btnConsume.setDisable(true);
			btnCancel.setDisable(true);
			
			// apPatientInfo.disableProperty();

			// start simulator : sync between controller & Gui
			startIGPSimulator();

	}

	/* declarations.clock.LoadingSetTimeListener#setTime(java.lang.String) */
	@Override
	public void setTime(String currentTime) {
		txtTimer.setText(currentTime);
	}

	/*
	 * declarations.Mediator.DisplayControllable# setDisplayParameters
	 */
	@Override
	public void setDisplayParameters(final HashMap<String, Number> accesory) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					int currentBSL = (Integer) accesory.get("glucoselevel");
					//System.out.println("bsl= " +currentBSL + "  i= " +accesory.get("iDose") + "  g= " +accesory.get("gDose"));
					checkForDose((double) accesory.get("iDose"), (double) accesory.get("gDose"));
					checkForBSLWarnings(false, currentBSL);
					
					seriesL.getData().add(new XYChart.Data(txtTimer.getText(), AssemblyConstants.RANGE_ONE_MIN));
					series.getData().add(new XYChart.Data(txtTimer.getText(), currentBSL));
					seriesU.getData().add(new XYChart.Data(txtTimer.getText(), AssemblyConstants.RANGE_ONE_MAX));
					
					progressBattery.setProgress((Double) accesory.get("batterylevel"));
					progressInsulinBank.setProgress((Double) accesory.get("insulinlevel"));
					progressGlucagonBank.setProgress((Double) accesory.get("glucagonlevel"));
					if (progressGlucagonBank.getProgress() < AssemblyConstants.ALERT_LIMIT) {
						setAlarm(true);
						infoBox("Refill Glucagon Bank by pressing the '+' sign.", "", "!! Glucagon Alert !!");
					}
					if (progressInsulinBank.getProgress() < AssemblyConstants.ALERT_LIMIT) {
						setAlarm(true);
						infoBox("Refill Insulin Bank by pressing the '+' sign.", "", "!! Insulin Alert !!");
					}
					if (progressBattery.getProgress() < AssemblyConstants.ALERT_LIMIT) {
						setAlarm(true);
						infoBox("Recharge battery by pressing the '+' sign.", "", "!! Low Battery Alert !!");
					}

					txtNewBSL.setText(String.valueOf(accesory.get("glucoselevel")));
					txtPrevBSL.setText(String.valueOf(previousBSL));
					previousBSL = (Integer) accesory.get("glucoselevel");
					if (i != 0 && i % 6 == 0) {
						PrimeController.changeBGLOnIdle();
					}
					i++;
					if (i > 22) {
						seriesL.getData().remove(0);
						series.getData().remove(0);
						seriesU.getData().remove(0);
					}
				}
			});
	}

}
