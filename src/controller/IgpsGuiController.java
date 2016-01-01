package controller;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Timer;

import javafx.application.Platform;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
//import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
//import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IgpsGuiController implements Initializable {

	@FXML
	private static Text txtTimer;

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
	private Button btnConsume;

	@FXML
	private TextArea txtMsgBox;

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

	private static Series<Number, Number> series = new XYChart.Series<Number, Number>();

	@Override
	public void initialize(URL path, ResourceBundle resource) {

		/*
		 * Implementing the Initializable interface means that this method will
		 * be called when the controller instance is created
		 */

		// Handle TextField 'TxtTimer' on launch
		/*
		 * Stage scene = (Stage) MainAnchorPane.getScene().getWindow(); while
		 * (scene.isShowing()) { String t = GeneralMethods.GetCurrentSysTime();
		 * TxtTimer.setText(t); }
		 */

		// TxtBatteryLevel.setTextContent("100");;

		// Initialise the line chart with the series of data
		// series.getData().add(new XYChart.Data<Number, Number>(
		// i/* Clock.getcurrentTime() */, (Double)
		// accesorystatuses.get("glucoselevel")));

		// set series name
		timerClock = new Clock();
		timerClock.startClock();

		series.setName("Blood Glucose Graph");

		// set the series in graph to make it observable
		linePlotBSL.getData().add(series);

	}

	private void startPumpScheduling() {
		Timer timer = new Timer();
		synchronized (timer) {

			DisplayToControllerMediator.getInstance().startScheduling(timer);
		}

		System.out.println("Scheduling started");
	}

	@FXML
	void fillupGlucagonBank(ActionEvent event) {

	}

	@FXML
	void fillupInsulinBank(ActionEvent event) {

	}

	@FXML
	void setCarbsValue(ActionEvent event) {

	}

	@FXML
	void consumedMeal(ActionEvent event) {

	}

	@FXML
	void fillupBattery(ActionEvent event) {

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

	public static void setClock(String currentTime) {

		txtTimer.setText(currentTime);

	}

}
