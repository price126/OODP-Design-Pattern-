import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Vector;

public class ResumeView implements ActionListener, ListSelectionListener {
    private final JFrame win;
    private Vector partydb;
    private final JList allParties;
    private final JButton resume;
    private Vector selectedParty;
    Vector partiesVector;
    Vector lanes;
    ControlDesk controlDesk;

    public ResumeView(ControlDesk controlDesk) throws IOException, ClassNotFoundException {
        this.controlDesk = controlDesk;
        win = ViewComponents.MakeWindow("Query");
        JPanel colPanel = ViewComponents.GridLayoutPanel(1,2);

        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Party List"));

        lanes = PausedLanesFile.readPausedLanes();
        partiesVector = new Vector<>();
        Vector validPartiesVector = new Vector<>();
//        System.out.println("START:");
        for (Object lane: lanes)
        {
            Vector v = new Vector<>();
//            System.out.println(((Vector) lane).get(0));

            for (Object bowler: ((CalculateScore)(((Vector) lane).get(5))).party.getMembers())
                v.add(((Bowler) bowler).getNick());
            partiesVector.add(v);

            if ((Boolean)(((Vector) lane).get(0)) == true)
                validPartiesVector.add(v);
        }


        allParties = new JList(validPartiesVector);
//        allParties.add
        allParties.setVisibleRowCount(8);
        allParties.setFixedCellWidth(120);
        JScrollPane partyPane = new JScrollPane(allParties);
        partyPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allParties.addListSelectionListener(this);
        partyPanel.add(partyPane);
        JPanel buttonPanel = ViewComponents.GridLayoutPanel(1,1);
        Insets buttonMargin = new Insets(4, 4, 4, 4);

        resume = ViewComponents.MakeButtons("Resume selected game", buttonPanel);
        resume.addActionListener(this);


        // Clean up main panel
//        colPanel.add(partyPanel);
        colPanel.add(partyPanel);
        colPanel.add(buttonPanel);
        ViewComponents.AddContentsToWindow(win,colPanel);
        // Center Window on Screen
        ViewComponents.SetWindowPosition(win);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(resume)) {
            int index = (partiesVector.indexOf(selectedParty));
            try {
                PausedLanesFile.resumePausedLane(index);
                PartyQueue.resumePartyQueue(controlDesk, (Vector) lanes.get(index));
                win.setVisible(false);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allParties)) {
            selectedParty =
                    ((Vector)((JList) e.getSource()).getSelectedValue());
        }
    }
}
