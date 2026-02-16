package application.model;

//import java.math.BigDecimal;

public class PomodoroModel {
	
	private boolean isWorking = true;
	private boolean isFinished = false;
	
	private int workMinutes = 25 * 60; // 1500s
	private int breakMinute = 5 * 60; // 300s
	private int longBreak = 15 * 60; // 1800s
	private long remainingSeconds;
	private int pomodoroCompleted = 1;
	
	// Constructor method
	public PomodoroModel() {
		resetPomodoro();
	}
	
	public void resetPomodoro() {
		this.remainingSeconds = this.workMinutes;
		this.pomodoroCompleted = 1;
		this.isWorking = true;
	}
	
	// Play method
	public void start()  {
		
		remainingSeconds--;
		
		if (remainingSeconds <= 0) {
			if (isWorking) {
				isWorking = false;
				if (pomodoroCompleted % 4 == 0) {
					remainingSeconds = longBreak;
				} else {					
					remainingSeconds = breakMinute;
				}
			} else {
				if (pomodoroCompleted % 4 == 0) {
					this.isFinished = true;
					resetPomodoro();
				} else {					
					isWorking = true;
					pomodoroCompleted++;
					remainingSeconds = workMinutes;
				}
			}
		}
		
	}
	
	public void skip() {
		if (remainingSeconds <= 0) {
			return;
		}
		
		remainingSeconds = 0;
		
	}
	
	// Getters
	public long getRemainingTime() {
		return this.remainingSeconds;
	}
	
	public int getPomodoroCompleted() {
		return this.pomodoroCompleted;
	}
	
	public boolean isFinished() {
		return this.isFinished;
	}
	
	// Setters
	public void setFinished(boolean finished) {
	    this.isFinished = finished;
	}
	
	public String timeToString() {
		return formatTime((int) this.remainingSeconds);
	}
	
	public String sessionToString() {
		return formatSession(pomodoroCompleted);
	}
	
	// Helpers
	private String formatTime(int time) {
        int mins = time / 60;
        int secs = time % 60;
        return String.format("%02d:%02d", mins, secs);
    }
	
	private String formatSession(int session) {
		int maxSession = 4;
		return String.format("Session: %d/%d", session, maxSession);
	}
	
	public String getStatus() {
		String status;
		
		if (isWorking) {
			status = "Work Session";
		} else {
			if (pomodoroCompleted % 4 == 0) {
				status = "Long Break";
			} else {
				status = "Short Break";
			}
		}
        return status;
    }
	
	public double getProgress() {
		long totalSeconds = isWorking ? workMinutes : 
            (pomodoroCompleted % 4 == 0 ? longBreak : breakMinute);
		if (remainingSeconds == 1) return 1.0;
		double p = 1.0 - (remainingSeconds / (double) totalSeconds);
	    return Math.max(0.0, Math.min(1.0, p));
		
	    // Version 2
//		BigDecimal progress = new BigDecimal(String.format("%2f", 0.0));
//		
//		if (progress.doubleValue() <= 1) {
//			if (remainingSeconds == 1) {
//				return 1.0;
//			}
//			Double formulaDouble = 1.0 - (remainingSeconds / (double) totalSeconds);
//			progress = new BigDecimal(String.format("%2f", progress.doubleValue() + formulaDouble));
//			System.out.println(progress.doubleValue());
//		}
//		return progress.doubleValue();
	}
	
	public boolean isLongBreak() {
	    return !isWorking && (pomodoroCompleted % 4 == 0);
	}
}
