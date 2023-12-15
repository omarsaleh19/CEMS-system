package gui_headDep;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import client.CEMSClient;
import control_common.LoginController;

import entity.LoggedInUser;
import entity.User;

import entity.ActivatedExam;

import gui.GuiCommon;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HeadDepMenuBoundary implements Initializable {

    @FXML
    private Button timeReqBtn;
    @FXML
    private AnchorPane window;
    
    private Timer notificationHandler;

    @FXML
    private Button viewAsBtn;
    
    @FXML
    private Button getReportBtn;
    
    @FXML
    private Button logoutBtn;
    
    @FXML
    private Label msgArea;
    
	@FXML
	private Label userName;
    
    @FXML
    void getReport(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/ChooseReportType.fxml", "Choose Report", event, true);
    }

	@FXML
	void timeReq(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/TimeRequests.fxml", "Time Requests", event, true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(() -> {
	   		 Stage stage = (Stage) window.getScene().getWindow();
	   	    	stage.setOnCloseRequest(event -> {
	   	            event.consume(); // Consume the event to prevent automatic window closing
	   	         LoginController.setWithReq(true);
	   	         LoginController.disconnect();	   
	   	         stage.close();
	   	        });
	        });

		setupNotificationsHadler();
		
		try {
			userName.setText("Hi " + LoggedInUser.getHeadDep().getFirstName() + " "
					+ LoggedInUser.getHeadDep().getLastName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public void setupNotificationsHadler() {
		notificationHandler = new Timer();
		notificationHandler.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				// When extra time is received updates the timer and notifies the student
				checkServerNotification();
			}
		}, 0, 1000);
	}

	/**
	 * Checks for server notifications and handles them accordingly. This method is
	 * called every 10 seconds.
	 */
	private void checkServerNotification() {
		String notification = CEMSClient.notificationFromServer.getMsg();
		// System.out.println("!");
		if (notification != null) {
			if (notification.equals("popOutTimeReq")) {
				ActivatedExam ex = (ActivatedExam)CEMSClient.notificationFromServer.getData();
				Platform.runLater(() -> GuiCommon.getInstance()
						.popUp("You have one time request for exam "+ex.getExamID()+" addition\nCheck it in view Time Requests"));
				CEMSClient.notificationFromServer.setMsg(null);
			}
		}
	}
    
	
	
    @FXML
    void studentReport(ActionEvent event) {
		GuiCommon.getInstance().displayNextScreen("/gui_headDep/StudentReport.fxml", "Student Report", event, true);

    }
    
    @FXML
    void clickViewAs(ActionEvent event) {
    	GuiCommon.getInstance().displayNextScreen("/gui_headDep/ViewAsUser.fxml", "View As", event, true);

    }
    
    
    /**
     * Logs out the user from the application.
     * @param event The action event generated by the "Logout" button click.
     */
    @FXML
    void clickLogout(ActionEvent event) {
    	if( LoginController.logOutHeadDep()) {
			GuiCommon.getInstance().displayNextScreen("/gui/login.fxml", "LoginCEMS", event, true);
		}
    }  
    
    public void showMsg( String msg) {
    	msgArea.setText(msg);
    }
    
}