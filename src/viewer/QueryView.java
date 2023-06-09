package viewer;

import user.BowlerFile;
import game.ScoreHistoryFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class QueryView implements ActionListener, ListSelectionListener {
    private final JFrame win;
    private Vector bowlerdb;
    private final JList partyList;
    private final JList allBowlers;
    private final JButton average;
    private String selectedNick;


    public QueryView(){
        win = CustomView.createWindow("Query");
        JPanel colPanel = CustomView.createGridLayoutPanel(1,3);
        JPanel partyPanel = CustomView.createFlowLayoutPanel();
        partyPanel.setBorder(new TitledBorder("Query Result"));
        Vector empty = new Vector();
        empty.add("(Empty)");

        partyList = new JList(empty);
        partyList.setFixedCellWidth(200);
        partyList.setVisibleRowCount(6);
        partyList.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(partyList);
        partyPanel.add(partyPane);
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler List"));

        try {
            bowlerdb = new Vector(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector();
        }
        allBowlers = new JList(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);
        JPanel buttonPanel = CustomView.createGridLayoutPanel(5,1);
        Insets buttonMargin = new Insets(4, 4, 4, 4);

        average = CustomView.createButtonInPanel("Show Average Score", buttonPanel);
        average.addActionListener(this);

        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);
        CustomView.addContentsOnWindow(win,colPanel);
        CustomView.setWindowCentered(win);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(average)) {
            if (selectedNick != null) {
                double averagevalue;
                averagevalue = ScoreHistoryFile.averageScore(selectedNick);
                averagevalue = Math.round(averagevalue*100.0)/100.0;
                Vector<String> partyVector = new Vector<>();
                partyVector.add("Player: " + selectedNick);
                partyVector.add("Average score: " + averagevalue);
                partyList.setListData(partyVector);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick =
                    ((String) ((JList) e.getSource()).getSelectedValue());
        }
    }
}
