package viewer;

import game.PinsetterEvent;
import game.PinsetterObserver;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class PinSetterView implements PinsetterObserver {

    private final Vector<JLabel> pinVect = new Vector<>();
    private final JPanel firstRoll;
    private final JPanel secondRoll;

    private final JFrame frame;

    public PinSetterView(int laneNum) {

        frame = new JFrame("lane.Lane " + laneNum + ":");

        Container cpanel = frame.getContentPane();

        JPanel pins = new JPanel();

        pins.setLayout(new GridLayout(4, 7));

        JPanel top = new JPanel();

        firstRoll = new JPanel();
        firstRoll.setBackground(Color.yellow);

        secondRoll = new JPanel();
        secondRoll.setBackground(Color.black);

        top.add(firstRoll, BorderLayout.WEST);
        top.add(secondRoll, BorderLayout.EAST);

        JPanel one = new JPanel();
        JLabel oneL = new JLabel("1");
        one.add(oneL);
        JPanel two = new JPanel();
        JLabel twoL = new JLabel("2");
        two.add(twoL);
        JPanel three = new JPanel();
        JLabel threeL = new JLabel("3");
        three.add(threeL);
        JPanel four = new JPanel();
        JLabel fourL = new JLabel("4");
        four.add(fourL);
        JPanel five = new JPanel();
        JLabel fiveL = new JLabel("5");
        five.add(fiveL);
        JPanel six = new JPanel();
        JLabel sixL = new JLabel("6");
        six.add(sixL);
        JPanel seven = new JPanel();
        JLabel sevenL = new JLabel("7");
        seven.add(sevenL);
        JPanel eight = new JPanel();
        JLabel eightL = new JLabel("8");
        eight.add(eightL);
        JPanel nine = new JPanel();
        JLabel nineL = new JLabel("9");
        nine.add(nineL);
        JPanel ten = new JPanel();
        JLabel tenL = new JLabel("10");
        ten.add(tenL);

        pinVect.add(oneL);
        pinVect.add(twoL);
        pinVect.add(threeL);
        pinVect.add(fourL);
        pinVect.add(fiveL);
        pinVect.add(sixL);
        pinVect.add(sevenL);
        pinVect.add(eightL);
        pinVect.add(nineL);
        pinVect.add(tenL);

        pins.add(seven);
        pins.add(new JPanel());
        pins.add(eight);
        pins.add(new JPanel());
        pins.add(nine);
        pins.add(new JPanel());
        pins.add(ten);

        pins.add(new JPanel());
        pins.add(four);
        pins.add(new JPanel());
        pins.add(five);
        pins.add(new JPanel());
        pins.add(six);

        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(two);
        pins.add(new JPanel());
        pins.add(three);
        pins.add(new JPanel());
        pins.add(new JPanel());

        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(one);
        pins.add(new JPanel());
        pins.add(new JPanel());
        pins.add(new JPanel());

        top.setBackground(Color.black);

        cpanel.add(top, BorderLayout.NORTH);

        pins.setBackground(Color.black);
        pins.setForeground(Color.yellow);

        cpanel.add(pins, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }

    public void receivePinsetterEvent(PinsetterEvent pe) {
        if (!(pe.isFoulCommited())) {
            JLabel tempPin;
            for (int c = 0; c < 10; c++) {
                boolean pin = pe.pinKnockedDown(c);
                tempPin = (JLabel) pinVect.get(c);
                if (pin) {
                    tempPin.setForeground(Color.lightGray);
                }
            }
        }
        if (pe.getThrowNumber() == 1) {
            secondRoll.setBackground(Color.yellow);
        }
        if (pe.pinsDownOnThisThrow() == -1) {
            for (int i = 0; i < 10; i++) {
                ((JLabel) pinVect.get(i)).setForeground(Color.black);
            }
            secondRoll.setBackground(Color.black);
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public static void main(String[] args) {
        PinSetterView pg = new PinSetterView(1);
    }

}
