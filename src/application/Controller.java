package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.model.PomodoroModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Controller implements Initializable {
	
	@FXML
    private Button btnPause;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnSkip;

    @FXML
    private Pane containerTimer;

    @FXML
    private Label lblSession;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblTimer;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private ProgressBar pTimer;
    
    private String lastKnownStatuString = "";
    
    private Timeline timeline;
    
    PomodoroModel pomodoro = new PomodoroModel();
    
    public void playTimer(ActionEvent event) throws IOException {

    	lblTimer.setText(pomodoro.timeToString());
        lblStatus.setText(pomodoro.getStatus());
        
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();  // optional safety
        }
    	
    	// Instruction to start the timer every 1s
		timeline = new Timeline((new KeyFrame( Duration.seconds(1), ev -> {
			
			// starts the count down: start @ 24:59
			pomodoro.start();
			
			// UI render
			pTimer.setProgress(pomodoro.getProgress());
			
			lblTimer.setText(pomodoro.timeToString());
			
			String currentStatuString = pomodoro.getStatus();
			if (!currentStatuString.equals(lastKnownStatuString)) {				
				lastKnownStatuString = currentStatuString;
				lblStatus.setText(currentStatuString);
				lblSession.setText(pomodoro.sessionToString());
				if (lastKnownStatuString.equals("Work Session")) {
					pTimer.setStyle("-fx-accent: crimson;");
				} else {
					pTimer.setStyle("-fx-accent: limegreen;");
				}
			}
			
			//auto-pause logic
			if (pomodoro.isFinished()) {
				timeline.pause();
				btnPlay.setDisable(false);
				pomodoro.setFinished(false);
				return;
			}
			
		})));
		
		// Home for frame to display it in the UI
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
		btnPlay.setDisable(true);
		btnPause.setDisable(false);
		btnReset.setDisable(false);
		btnSkip.setDisable(false);
	}
    
    public void pauseTimer(ActionEvent event) {
        if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.pause();
            btnPause.setDisable(true);
            btnPlay.setDisable(false);
        }
    }
    
    public void resetTimer(ActionEvent event) {
    	if (timeline != null && timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
        }
    	pomodoro.resetPomodoro();
        lblTimer.setText(pomodoro.timeToString());
        pTimer.setProgress(pomodoro.getProgress());
        lblStatus.setText(pomodoro.getStatus());
        lblSession.setText(pomodoro.sessionToString());
        pTimer.setStyle("-fx-accent: red;");
        btnPlay.setDisable(false);
        btnPause.setDisable(true);
        btnReset.setDisable(true);
        btnSkip.setDisable(true);
	}
    
    public void skipTimer(ActionEvent event) {
    	pomodoro.skip();
    	
    	pTimer.setProgress(pomodoro.getProgress());
    	lblTimer.setText(pomodoro.timeToString());
    	
    	pomodoro.start();
    	
    	lblStatus.setText(pomodoro.getStatus());
    	lblSession.setText(pomodoro.sessionToString());
    	
    	if (pomodoro.isLongBreak()) {
    		btnSkip.setDisable(true);
    	}
    	
    }
   
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
//		lblTimer.setText("25:00");
		btnPause.setDisable(true);
		btnReset.setDisable(true);
		btnSkip.setDisable(true);
	}

}
