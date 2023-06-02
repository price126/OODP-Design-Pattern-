import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

public class QueryView implements ActionListener, ListSelectionListener {
    private final JFrame win;
    private Vector bowlerdb;
    private final JList partyList;
    private final JList allBowlers;
    private final JButton highestplayer, lowestplayer, highestoverall, lowestoverall, average;
    private String selectedNick;


    public QueryView(){
        win = ViewComponents.MakeWindow("Query");
        JPanel colPanel = ViewComponents.GridLayoutPanel(1,3);
        JPanel partyPanel = ViewComponents.FlowLayoutPanel();
        partyPanel.setBorder(new TitledBorder("Query Result"));
        Vector empty = new Vector();
        empty.add("(Empty)");

        partyList = new JList(empty);
        partyList.setFixedCellWidth(200);
        partyList.setVisibleRowCount(6);
        partyList.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(partyList);
        //        partyPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
        JPanel buttonPanel = ViewComponents.GridLayoutPanel(5,1);
        Insets buttonMargin = new Insets(4, 4, 4, 4);

        highestplayer = ViewComponents.MakeButtons("Highest score for player", buttonPanel);
        highestplayer.addActionListener(this);
        lowestplayer = ViewComponents.MakeButtons("Lowest score for player", buttonPanel);
        lowestplayer.addActionListener(this);
        highestoverall = ViewComponents.MakeButtons("Highest score overall", buttonPanel);
        highestoverall.addActionListener(this);
        lowestoverall = ViewComponents.MakeButtons("Lowest score overall", buttonPanel);
        lowestoverall.addActionListener(this);
        average = ViewComponents.MakeButtons("Average score for player", buttonPanel);
        average.addActionListener(this);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);
        ViewComponents.AddContentsToWindow(win,colPanel);
        // Center Window on Screen
        ViewComponents.SetWindowPosition(win);
    }

    public Vector getHighestAndLowestCaller(String selectedNick, Boolean isGeneral)
    {
        Vector returnVector = new Vector<>();
        try {
            returnVector =  ScoreHistoryFile.getHighestAndLowest(selectedNick, isGeneral);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return returnVector;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(highestplayer)) {
            if (selectedNick != null) {
                Vector returnedVector = getHighestAndLowestCaller(selectedNick, false);

                Vector<String> partyVector = new Vector<>();
                partyVector.add("Player: " + selectedNick);
                partyVector.add(("Highest: " + returnedVector.get(0)));
                partyList.setListData(partyVector);
            }
        }
        else if (e.getSource().equals(lowestplayer)) {
            if (selectedNick != null) {
                Vector returnedVector = getHighestAndLowestCaller(selectedNick, false);

                Vector<String> partyVector = new Vector<>();
                partyVector.add("Player: " + selectedNick);
                partyVector.add(("Lowest: " + returnedVector.get(2)));
                partyList.setListData(partyVector);
            }
        }
        else if (e.getSource().equals(highestoverall)) {
            Vector returnedVector = getHighestAndLowestCaller(selectedNick, true);

            Vector<String> partyVector = new Vector<>();
            partyVector.add("Highest overall:");
            partyVector.add("Player: "+returnedVector.get(1));
            partyVector.add("Score: "+ returnedVector.get(0));
            partyList.setListData(partyVector);
        }
        else if (e.getSource().equals(lowestoverall)) {
            Vector returnedVector = getHighestAndLowestCaller(selectedNick, true);

            Vector<String> partyVector = new Vector<>();
            partyVector.add("Lowest overall:");
            partyVector.add("Player: "+returnedVector.get(3));
            partyVector.add("Score: "+ returnedVector.get(2));
            partyList.setListData(partyVector);
        }
        else if (e.getSource().equals(average)) {
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
