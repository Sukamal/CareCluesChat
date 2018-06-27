package careclues.rocketchat;

import java.util.Timer;
import java.util.TimerTask;

public class CcTaskHandler {

    private Timer timer;
    private TimerTask task;
    private Boolean isCancelled;

    public CcTaskHandler() {
        timer = new Timer();
        isCancelled = false;
    }

    public void postDelayed(TimerTask timerTask, long delay) {
        this.task = timerTask;
        if (isCancelled) {
            recreate();
        }
        timer.schedule(timerTask, delay);
    }

    public void scheduleAtFixedRate(TimerTask timerTask, long delay, long period) {
        if (isCancelled) {
            recreate();
        }
        timer.scheduleAtFixedRate(timerTask, delay, period);
    }

    public void removeLast() {
        if (task != null) {
            task.cancel();
        }
    }

    public void remove(TimerTask task) {
        task.cancel();
    }

    public void cancel() {
        if (!isCancelled) {
            removeLast();
            timer.cancel();
            timer.purge();
            isCancelled = true;
        }
    }

    private void recreate() {
        timer = new Timer();
        isCancelled = false;
    }
}