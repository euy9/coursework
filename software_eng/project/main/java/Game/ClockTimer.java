package Game;
import static Game.OaklandOligarchy.sec;
import static Game.OaklandOligarchy.min;
import static Game.OaklandOligarchy.hour;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/**
 * This class implements the clock functionality through multi-threading
 */
public class ClockTimer{
    Timer timer = new Timer();
    static ClockLabel clockLabel;
    
    /**
     * constructor
     */
    public ClockTimer() {
        clockLabel = new ClockLabel();
        timer.schedule(new UpdateTask(), 0, 1000);
    } 
    
    /**
     * Inner class which dynamically changes the ClockLabel to display the correct time
     */
    class UpdateTask extends TimerTask {
        @Override
        public void run() {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    clocking();
                    String s = sec.toString();
                    String m = min.toString();
                    String h = hour.toString();
                    
                    if (Integer.parseInt(s) < 10)
                        s = convert(s);
                    if (Integer.parseInt(m) < 10)
                        m = convert(m);
                    if (Integer.parseInt(h) < 10)
                        h = convert(h);
                    
                    clockLabel.setText(h + ":" + m + ":" + s);
                    
                }
            });
        }
    }
    
    /**
     * This method lets the min & hour to increment when sec & min is at max
     */
    public void clocking(){
        sec.addAndGet(1);
        if (sec.get() >= 60){
            sec.set(0);
            min.addAndGet(1);
            if (min.get() >= 60){
                min.set(0);
                hour.addAndGet(1);
            }
        }
    }
    
    public String convert(String s) {
        if (s.equals("0")){
            s = "00";
        } else if (s.equals("1")) {
            s = "01";
        } else if (s.equals("2")) {
            s = "03";
        } else if (s.equals("3")) {
            s = "03";
        } else if (s.equals("4")) {
            s = "04";
        } else if (s.equals("5")) {
            s = "05";
        } else if (s.equals("6")) {
            s = "06";
        } else if (s.equals("7")) {
            s = "07";
        } else if (s.equals("8")) {
            s = "08";
        } else if (s.equals("9")) {
            s = "09";
        }
        
        return s;
    }
    
    /**
     * This is an inner class for creating a special label for the clock
     */
    public class ClockLabel extends JLabel{
        public ClockLabel(){
            setForeground(Color.GREEN); 
            setBackground(Color.BLACK);
            setFont(new Font("Tahoma", Font.BOLD, 20));
            setHorizontalAlignment(SwingConstants.LEFT);
            setText("Time goes here");
        }

        @Override
        public void setText(String text) {
            super.setText(text);
        }
    }
}

