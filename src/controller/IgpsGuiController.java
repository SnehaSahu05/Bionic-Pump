package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;

import controller.Clock.LoadingSetTimeListener;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
//import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IgpsGuiController implements Initializable, LoadingSetTimeListener {

	@FXML
	private Text txtTimer;

	@FXML
	private ScrollBar tabScroll;

	@FXML
	private Button btnPNew;

	@FXML
	private TextField txtNewBSL;

	@FXML
	private CheckBox chkBox2;

	@FXML
	private CheckBox chkBox3;

	@FXML
	private Button btnBattery;

	@FXML
	private NumberAxis linePlotBSLxAxis;

	@FXML
	private MenuItem menuItmMeal;
	@FXML
	private Group grpMeal;

	@FXML
	private Button btnConsume;

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
	private LineChart<Number, Number> linePlotBSL;

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
	private CheckBox chkBoxCoke;

	@FXML
	private ProgressBar progressGlucagonBank;

	@FXML
	private NumberAxis linePlotBSLyAxis;

	@FXML
	private Button btnPDel;

	private Clock timerClock;
	private ObservableList<Text> msgBoxItems = FXCollections.observableArrayList();

	private static Series<Number, Number> series = new XYChart.Series<Number, Number>();

	@Override
	public void initialize(URL path, ResourceBundle resource) {

		/*
		 * Implementing the Initializable interface means that this method will
		 * be called when the controller instance is created
		 */

		// Initialise the line chart with the series of data
		// series.getData().add(new XYChart.Data<Number, Number>(
		// i/* Clock.getcurrentTime() */, (Double)
		// accesorystatuses.get("glucoselevel")));

		// set series name
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

		// apPatientInfo.disableProperty();

		// start simulator
		startIGPSimulator();
	}

	private void addMessage(String message, Color col) {
		if (msgBoxItems.size() > 0) {
			((Text) msgBoxItems.get(0)).setStroke(Color.GREY);
		}
		Text msg = new Text(message);
		msg.setStroke(col);
		msgBoxItems.add(0, msg);
	}

	private void startIGPSimulator() {
		Timer timer = new Timer();
		synchronized (timer) {

			DisplayToControllerMediator.getInstance().startSimulator(timer);
		}

		System.out.println("Scheduling started");
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
	void setCarbsValue(ActionEvent event) {

	}

	@FXML
	public void onWorkOut(ActionEvent event) {
		menuBtn.setText("Go for Workout");
	}

	@FXML
	public void onMealSelected(ActionEvent event) {
		menuBtn.setText("Proceed for a meal");
		grpMeal.setDisable(false);
	}

	@FXML
	void consumedMeal(ActionEvent event) {
		addMessage("Consumed Meal", Color.GREEN);
		grpMeal.setDisable(true);
		menuBtn.setText("Select Activity");
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
	}

	public static void setParameters(HashMap<String, Double> accesorystatus) {
		// TODO set all GUI related parameters

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

			}
		});

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

}
