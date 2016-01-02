package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import com.sun.org.apache.bcel.internal.generic.Select;

import assembly.AssemblyConstants;
import assembly.BloodGlucoseSensor;
import controller.Clock.LoadingSetTimeListener;
import controller.DisplayToControllerMediator.DisplayControllable;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
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
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
//import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
//import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import selfDefinedClasses.InsulinGlucagon;

public class IgpsGuiController implements Initializable, LoadingSetTimeListener, DisplayControllable {

	@FXML
	private Text txtTimer;

	@FXML
	private ScrollBar tabScroll;

	@FXML
	private Button btnPNew;

	@FXML
	private TextField txtNewBSL;

	@FXML
	private CheckBox chkBoxCoke;

	@FXML
	private CheckBox chkBoxBread;

	@FXML
	private CheckBox chkBoxRice;

	@FXML
	private CheckBox chkBoxHoney;

	@FXML
	private CheckBox chkBoxChocolate;

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
	private Button btnCancel;

	@FXML
	private ListView<Text> listMsgBox;

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
	private LineChart<CategoryAxis, Number> linePlotBSL;

	@FXML
	private Text txtBatteryLevel;

	@FXML
	private AnchorPane apPatientInfo;

	@FXML
	private AnchorPane apActivity;

	@FXML
	private Group grpBank;

	@FXML
	private MenuItem menuItmWorkout;

	@FXML
	private ProgressBar progressInsulinBank;

	@FXML
	private ProgressBar progressGlucagonBank;

	@FXML
	private NumberAxis linePlotBSLyAxis;

	@FXML
	private Button btnPDel;

	private Map<String, Integer> carbsValue = new HashMap<String, Integer>();

	private Clock timerClock;
	private ObservableList<Text> msgBoxItems = FXCollections.observableArrayList();

	private static int i = 0;

	private static Series<CategoryAxis, Number> series = new XYChart.Series<CategoryAxis, Number>();
	private int previousBGL;
	final static AudioClip alert =

	new AudioClip(IgpsGuiController.class.getResource("alertvibrate.mp3").toString());

	public static boolean isMealConsumed = false;

	private int carbs = 0;

	private Timer rangeTimerOnMealConsumed = new Timer();

	@Override
	public void initialize(URL path, ResourceBundle resource) {

		timerClock = new Clock(this);
		timerClock.startClock();

		// set the series in graph to make it observable
		linePlotBSL.getData().add(series);

		// Default values
		txtNewBSL.setText("90");
		txtPrevBSL.setText("-");
		textRangeBSL.setText("NORMAL");
		txtBatteryLevel.setText("100");
		progressBattery.setProgress(1);
		progressInsulinBank.setProgress(1);
		progressGlucagonBank.setProgress(1);
		listMsgBox.setItems(msgBoxItems);
		addMessage("Simulator switched On", Color.GREEN);
		grpMeal.setDisable(true);
		btnConsume.setDisable(true);
		btnCancel.setDisable(true);
		linePlotBSLxAxis.setAutoRanging(true);

		// apPatientInfo.disableProperty();

		// Add Carbs to Map variable (carbsValue) list
		addCarbstoMap();

		// start simulator
		startIGPSimulator();
	}

	@FXML
	void fillupGlucagonBank(ActionEvent event) {
		try {
			Timeline task = new Timeline(
					new KeyFrame(Duration.ZERO,
							new KeyValue(progressGlucagonBank.progressProperty(),
									new Double(progressGlucagonBank.getProgress()))),
					new KeyFrame(Duration.seconds(5), new KeyValue(progressGlucagonBank.progressProperty(), 1)));
			task.playFromStart();
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
		} catch (Exception ex) {
		}
	}

	@FXML
	public void onWorkOut(ActionEvent event) {
		menuBtn.setText("Go for Workout");
		btnConsume.setText("Go");
		grpMeal.setDisable(true);
		btnConsume.setDisable(false);
		btnCancel.setDisable(false);
	}

	@FXML
	public void onMealSelected(ActionEvent event) {
		menuBtn.setText("Proceed for a meal");
		grpMeal.setDisable(false);
		btnConsume.setText("Consume");
		btnConsume.setDisable(false);
		btnCancel.setDisable(false);


		// check bgl
		int currentGlucoseLevel = BloodGlucoseSensor.getInstance().checkBloodGlucose();
		// inject insulin if bgl is greater than R1 Max
		if (currentGlucoseLevel > AssemblyConstants.RANGE_ONE_MAX) {
			addMessage("Blood glucose level is too high", Color.RED);
			double calculatedInsulin = InsulinGlucagon.calculateInsulinBolus(currentGlucoseLevel);
			PrimeController.injectBolus(calculatedInsulin);
			addMessage(String.format("%s insulin dose has been injected.", calculatedInsulin), Color.GREEN);
		}
		// inject glucagon if bgl is lesser than R1 Min
		else if (currentGlucoseLevel < AssemblyConstants.RANGE_ONE_MIN) {
			addMessage("Blood glucose level is low", Color.RED);
			double glucagon = InsulinGlucagon.calculateGlucagon(currentGlucoseLevel);
			PrimeController.injectGlucagon(glucagon);
			addMessage(String.format("%s glucagon dose has been injected.", glucagon), Color.GREEN);
		}
		carbs = 0;
	}

	@FXML
	void consumedMeal(ActionEvent event) {
		carbs = 0;
		if (btnConsume.getText() == "Go") {
			btnConsume.setText("Consume");
			carbs -= 10;
			addMessage("You lose carbohydrates by 10 units", Color.GREEN);
		} else {
			ObservableList<Node> children = grpMeal.getChildren();
			for (Iterator<Node> iter = children.iterator(); iter.hasNext();) {
				Object obj = iter.next();
				if (obj instanceof CheckBox) {
					if (((CheckBox) obj).selectedProperty().getValue()) {
						carbs += (int) carbsValue.get(((CheckBox) obj).getText());
					}
				}
			}
			addMessage(String.format("Consumed %d units of carbohydrates", carbs), Color.RED);
			// addMessage("Consumed Meal", Color.GREEN);
			if (isMealConsumed) {
				rangeTimerOnMealConsumed.cancel();
				//rangeTimerOnMealConsumed.purge();
				rangeTimerOnMealConsumed = new Timer();
			}

			rangeTimerOnMealConsumed.schedule(new TimerTask() {

				@Override
				public void run() {
					isMealConsumed = false;
					rangeTimerOnMealConsumed.cancel();
					rangeTimerOnMealConsumed = new Timer();
				//	rangeTimerOnMealConsumed.purge();

				}
			}, 60000, 60000);

			isMealConsumed = true;
			grpMeal.setDisable(true);
		}
		btnConsume.setDisable(true);
		btnCancel.setDisable(true);
		menuBtn.setText("Select Activity");

		BloodGlucoseSensor.getInstance().bglChangeOnActivity(carbs);
		clearCheckBox();

}

	@FXML
	void cancelActivity(ActionEvent event) {
		clearCheckBox();
		carbs = 0;
		btnConsume.setText("Consume");
		menuBtn.setText("Select Activity");
		btnConsume.setDisable(true);
		btnCancel.setDisable(true);
		grpMeal.setDisable(true);
	}

	@FXML
	void fillupBattery(ActionEvent event) {
		try {
			Timeline task = new Timeline(
					new KeyFrame(Duration.ZERO,
							new KeyValue(progressBattery.progressProperty(), new Double(progressBattery.getProgress())),
							new KeyValue(txtBatteryLevel.textProperty(),
									String.valueOf(new Double(progressBattery.getProgress() * 100).intValue()))),
					new KeyFrame(Duration.seconds(5), new KeyValue(progressBattery.progressProperty(), 1),
							new KeyValue(txtBatteryLevel.textProperty(), "100")));
			task.playFromStart();

			BatteryManager.setNewBattery(new Double(100));

		} catch (Exception ex) {

		}
	}

	@FXML
	void exitSimulator(ActionEvent event) {
		Stage scene = (Stage) mainAnchorPane.getScene().getWindow();
		scene.close();
		Platform.exit();
	}

	@Override
	public void setDisplayParameters(final HashMap<String, Number> parameters) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				int currentBGL = (Integer) parameters.get("glucoselevel");
				checkForBGLWarnings(false, currentBGL);
				// series.getData().add(new XYChart.Data<CategoryAxis,
				// Number>("", currentBGL));
				series.getData().add(new XYChart.Data(txtTimer.getText(), currentBGL));
				progressBattery.setProgress((double) parameters.get("batterylevel"));

				progressInsulinBank.setProgress((double) parameters.get("insulinlevel"));

				txtNewBSL.setText(String.valueOf(parameters.get("glucoselevel")));
				txtPrevBSL.setText(String.valueOf(previousBGL));
				previousBGL = (int) parameters.get("glucoselevel");
				// setting display
				Double batteryLevel = ((Double) parameters.get("batterylevel")) * AssemblyConstants.HUNDRED;
				txtBatteryLevel.setText(String.valueOf(batteryLevel.intValue()));
				if (i != 0 && i % 6 == 0) {
					PrimeController.changeBGLOnIdle();
				}
				i++;
				if (i > 13) {
					series.getData().remove(0);

					// linePlotBSLxAxis.setLowerBound(linePlotBSLxAxis.getLowerBound()
					// + 1);
					// linePlotBSLyAxis.setUpperBound(linePlotBSLxAxis.getUpperBound()
					// + 1);

				}
			}

		});

	}


	// for Popup menu.
	public static void infoBox(String infoMessage, String titleBar, String headerMessage) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(titleBar);
		alert.setHeaderText(headerMessage);
		alert.setContentText(infoMessage);
		alert.showAndWait();
	}

	private void clearCheckBox() {
		ObservableList<Node> children = grpMeal.getChildren();
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			Object obj = iter.next();
			if (obj instanceof CheckBox) {
				((CheckBox) obj).setSelected(false);
			}
		}
	}

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

	private void addMessage(String message, Color col) {
		if (msgBoxItems.size() > 0) {
			((Text) msgBoxItems.get(0)).setStroke(Color.GREY);
		}
		Text msg = new Text(message);
		msg.setStroke(col);
		msg.setFont(new Font(15));
		msgBoxItems.add(0, msg);
	}

	private void startIGPSimulator() {
		Timer timer = new Timer();
		DisplayToControllerMediator mediator = new DisplayToControllerMediator(this);
		synchronized (timer) {

			mediator.startSimulator(timer);
		}
	}

	private void checkForBGLWarnings(boolean isMealTimerFinished, int glucoseLevel) {
		Color color = null;
		String message = null;
		if (!isMealConsumed) {

			if (glucoseLevel > AssemblyConstants.RANGE_ONE_MAX) {
				color = Color.RED;
				message = "Blood glucose is high";
				setAlarm(true);
			} else if (glucoseLevel < AssemblyConstants.RANGE_ONE_MIN) {
				color = Color.RED;
				message = "Blood glucose is low";
				setAlarm(true);
			} else if (isBGLInWarningLevel(glucoseLevel, AssemblyConstants.RANGE_ONE_MIN, AssemblyConstants.EIGHTY)) {
				color = Color.YELLOW;
				message = "Blood glucose is lowering";

			} else if (isBGLInWarningLevel(glucoseLevel, AssemblyConstants.ONE_HUNDRED_TEN,
					AssemblyConstants.RANGE_ONE_MAX)) {
				color = Color.YELLOW;
				message = "Blood glucose is increasing";

			} else {
				color = Color.GREEN;
				message = "Blood glucose is normal.";
			}
			if (!msgBoxItems.get(0).getText().equals(message)) {
				addMessage(message, color);
			}

		} else {

			if (glucoseLevel > AssemblyConstants.RANGE_TWO_MAX) {
				color = Color.RED;
				message = "Blood glucose is high";
				setAlarm(true);
			} else if (glucoseLevel < AssemblyConstants.RANGE_ONE_MIN) {
				color = Color.RED;
				message = "Blood glucose is low";
				setAlarm(true);
			} else if (isBGLInWarningLevel(glucoseLevel, AssemblyConstants.RANGE_ONE_MIN, AssemblyConstants.EIGHTY)) {
				color = Color.YELLOW;
				message = "Blood glucose is lowering";

			} else if (isBGLInWarningLevel(glucoseLevel, AssemblyConstants.HUNDRED_NINGHTY,
					AssemblyConstants.RANGE_TWO_MAX)) {
				color = Color.YELLOW;
				message = "Blood glucose is increasing";

			} else {
				color = Color.GREEN;
				message = "Blood glucose is normal.";

			}
			if (!msgBoxItems.get(0).getText().equals(message)) {
				addMessage(message, color);
			}

		}
	}

	private boolean isBGLInWarningLevel(int glucoseLevel, int lowerBound, int higherBound) {
		if (glucoseLevel > lowerBound && glucoseLevel < higherBound)
			return true;
		return false;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see controller.Clock.LoadingSetTimeListener#setTime(java.lang.String)
	 */
	@Override
	public void setTime(String currentTime) {
		txtTimer.setText(currentTime);

	}


	private static void setAlarm(boolean isplay) {
		if (isplay) {
			if (!alert.isPlaying())
				alert.play();
		} else {
			if (alert.isPlaying())
				alert.stop();
		}

	}


}
