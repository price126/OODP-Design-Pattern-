package viewer;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class CustomView {
    public static JFrame createWindow(String title){
        JFrame win = new JFrame(title);
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);
        return win;
    }

    public static void setWindowCentered(JFrame win){
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    public static void addContentsOnWindow(JFrame win, JPanel colPanel){
        win.getContentPane().add("Center", colPanel);
        win.pack();
    }

    public static JPanel createMainPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    public static JPanel createGridLayoutPanel(int row, int col){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(row,col));
        return panel;
    }

    public static JPanel createTitledBorderPanel(int row, int col, String title){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(row,col));
        panel.setBorder(new TitledBorder(title));
        return panel;
    }

    public static JPanel createFlowLayoutPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        return panel;
    }

    public static JButton createButtonInPanel(String title, JPanel buttonPanel){
        JButton btn = new JButton(title);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.add(btn);
        buttonPanel.add(btnPanel);
        return btn;
    }

    public static JTextField createTextFieldWithLabel(String title, JPanel patronPanel){
        JPanel obj  = new JPanel();
        obj.setLayout((new FlowLayout()));
        JLabel label = new JLabel(title);
        JTextField text = new JTextField("",15);
        obj.add(label);
        obj.add(text);
        patronPanel.add(obj);
        return text;
    }
}
