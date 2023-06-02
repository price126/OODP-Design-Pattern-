import javax.swing.*;
import java.util.Vector;

public class PinSetterRows {
    public static void SetFourthRow(JPanel pins, Vector pinVect){
        for (int i=7;i<=10;i++){
            JPanel value = new JPanel();
            JLabel label = new JLabel(Integer.toString(i));
            value.add(label);
            pinVect.add(label);
            pins.add(value);
            if(i != 10) {
                pins.add(new JPanel());
            }
        }
    }

    public static void SetThirdRow(JPanel pins, Vector pinVect){
        for (int i=4;i<=6;i++){
            JPanel value = new JPanel();
            JLabel label = new JLabel(Integer.toString(i));
            value.add(label);
            pinVect.add(label);
            pins.add(new JPanel());
            pins.add(value);
        }
    }

    public static void SetSecondRow(JPanel pins, Vector pinVect){
        pins.add(new JPanel());
        pins.add(new JPanel());
        for (int i=2;i<=3;i++){
            JPanel value = new JPanel();
            JLabel label = new JLabel(Integer.toString(i));
            value.add(label);
            pinVect.add(label);
            pins.add(new JPanel());
            pins.add(value);
        }
        pins.add(new JPanel());
        pins.add(new JPanel());
    }

    public static void SetFirstRow(JPanel pins, Vector pinVect){
        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(new JPanel());
        for (int i=1;i<=1;i++){
            JPanel value = new JPanel();
            JLabel label = new JLabel(Integer.toString(i));
            value.add(label);
            pinVect.add(label);
            pins.add(value);
        }
        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(new JPanel());
    }


}
