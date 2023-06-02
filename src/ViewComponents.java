import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class ViewComponents {
    public static JFrame MakeWindow(String st){
        JFrame win = new JFrame("Control Desk");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);
        return win;
    }

    public static void SetWindowPosition(JFrame win){
        Dimension screenSize = (Toolkit.getDefaultToolkit()).getScreenSize();
        win.setLocation(
                ((screenSize.width) / 2) - ((win.getSize().width) / 2),
                ((screenSize.height) / 2) - ((win.getSize().height) / 2));
        win.setVisible(true);
    }

    public static void AddContentsToWindow(JFrame win, JPanel colPanel){
        win.getContentPane().add("Center", colPanel);
        win.pack();
    }

    public static JPanel MakeMainPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        return panel;
    }

    public static JPanel MakePanel(int row, int col, String st){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(row,col));
        panel.setBorder(new TitledBorder(st));
        return panel;
    }

    public static JPanel GridLayoutPanel(int row, int col){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(row,col));
        return panel;
    }

    public static JPanel FlowLayoutPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        return panel;
    }

    public static JTextField MakeField(String st, JPanel patronPanel){
        JPanel obj  = new JPanel();
        obj.setLayout((new FlowLayout()));
        JLabel label = new JLabel(st);
        JTextField text = new JTextField("",15);
        obj.add(label);
        obj.add(text);
        patronPanel.add(obj);
        return text;
    }

    public static JButton MakeButtons(String st, JPanel buttonPanel){
        JButton btn = new JButton(st);
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
//        btn.addActionListener((ActionListener) btn);
        btnPanel.add(btn);
        buttonPanel.add(btnPanel);
        return btn;
    }
}
