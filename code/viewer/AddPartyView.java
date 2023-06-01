package viewer;

/* AddPartyView.java
 *
 *  Version:
 * 		 $Id$
 * 
 *  Revisions:
 * 		$Log$
 * 		Revision : 2023
 * 		Applied command Pattern
 * 		
 */

/**
 * Class for GUI components need to add a party
 *
 */

import user.Bowler;
import user.BowlerFile;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

// Command interface
interface Command {
    void execute();
}

// Concrete command for adding a patron
class AddPatronCommand implements Command {
    private final AddPartyView addPartyView;

    public AddPatronCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        addPartyView.addPatron();
    }
}

// Concrete command for removing a patron
class RemovePatronCommand implements Command {
    private final AddPartyView addPartyView;

    public RemovePatronCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        addPartyView.removePatron();
    }
}

// Concrete command for creating a new patron
class NewPatronCommand implements Command {
    private final AddPartyView addPartyView;

    public NewPatronCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        addPartyView.createNewPatron();
    }
}

// Concrete command for finishing the party creation
class FinishCommand implements Command {
    private final AddPartyView addPartyView;

    public FinishCommand(AddPartyView addPartyView) {
        this.addPartyView = addPartyView;
    }

    @Override
    public void execute() {
        addPartyView.finish();
    }
}

/**
 * Constructor for GUI used to Add Parties to the waiting party queue.
 */
public class AddPartyView implements ActionListener, ListSelectionListener {
    private final int maxSize;
    private final JFrame win;
    private final JButton addPatron;
    private final JButton newPatron;
    private final JButton remPatron;
    private final JButton finished;
    private final JList<String> partyList;
    private final JList<String> allBowlers;
    private final Vector<String> party;
    private Vector<String> bowlerdb;
    private String selectedNick;
    private String selectedMember;
    private final ControlDeskView controlDesk;

    public AddPartyView(ControlDeskView controlDesk, int max) {
        this.controlDesk = controlDesk;
        maxSize = max;

        win = new JFrame("Add Party");
        win.getContentPane().setLayout(new BorderLayout());
        ((JPanel) win.getContentPane()).setOpaque(false);

        JPanel colPanel = new JPanel();
        colPanel.setLayout(new GridLayout(1, 3));

        // Party Panel
        JPanel partyPanel = new JPanel();
        partyPanel.setLayout(new FlowLayout());
        partyPanel.setBorder(new TitledBorder("Your Party"));

        party = new Vector<>();
        Vector<String> empty = new Vector<>();
        empty.add("(Empty)");

        partyList = new JList<>(empty);
        partyList.setFixedCellWidth(120);
        partyList.setVisibleRowCount(5);
        partyList.addListSelectionListener(this);
        JScrollPane partyPane = new JScrollPane(partyList);
        partyPanel.add(partyPane);

        // Bowler Database
        JPanel bowlerPanel = new JPanel();
        bowlerPanel.setLayout(new FlowLayout());
        bowlerPanel.setBorder(new TitledBorder("Bowler Database"));

        try {
            bowlerdb = new Vector<String>(BowlerFile.getBowlers());
        } catch (Exception e) {
            System.err.println("File Error");
            bowlerdb = new Vector<>();
        }
        allBowlers = new JList<>(bowlerdb);
        allBowlers.setVisibleRowCount(8);
        allBowlers.setFixedCellWidth(120);
        JScrollPane bowlerPane = new JScrollPane(allBowlers);
        bowlerPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        allBowlers.addListSelectionListener(this);
        bowlerPanel.add(bowlerPane);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1));

        Insets buttonMargin = new Insets(4, 4, 4, 4);

        addPatron = new JButton("Add to Party");
        JPanel addPatronPanel = new JPanel();
        addPatronPanel.setLayout(new FlowLayout());
        addPatron.addActionListener(this);
        addPatronPanel.add(addPatron);

        remPatron = new JButton("Remove Member");
        JPanel remPatronPanel = new JPanel();
        remPatronPanel.setLayout(new FlowLayout());
        remPatron.addActionListener(this);
        remPatronPanel.add(remPatron);

        newPatron = new JButton("New Patron");
        JPanel newPatronPanel = new JPanel();
        newPatronPanel.setLayout(new FlowLayout());
        newPatron.addActionListener(this);
        newPatronPanel.add(newPatron);

        finished = new JButton("Finished");
        JPanel finishedPanel = new JPanel();
        finishedPanel.setLayout(new FlowLayout());
        finished.addActionListener(this);
        finishedPanel.add(finished);

        buttonPanel.add(addPatronPanel);
        buttonPanel.add(remPatronPanel);
        buttonPanel.add(newPatronPanel);
        buttonPanel.add(finishedPanel);

        // Clean up main panel
        colPanel.add(partyPanel);
        colPanel.add(bowlerPanel);
        colPanel.add(buttonPanel);

        win.getContentPane().add("Center", colPanel);

        win.pack();

        // Center Window on Screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(
                (screenSize.width / 2) - (win.getSize().width / 2),
                (screenSize.height / 2) - (win.getSize().height / 2));
        win.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        Command command;

        if (e.getSource().equals(addPatron)) {
            command = new AddPatronCommand(this);
        } else if (e.getSource().equals(remPatron)) {
            command = new RemovePatronCommand(this);
        } else if (e.getSource().equals(newPatron)) {
            command = new NewPatronCommand(this);
        } else if (e.getSource().equals(finished)) {
            command = new FinishCommand(this);
        } else {
            return;
        }

        command.execute();
    }

    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource().equals(allBowlers)) {
            selectedNick = allBowlers.getSelectedValue();
        }
        if (e.getSource().equals(partyList)) {
            selectedMember = partyList.getSelectedValue();
        }
    }

    public void addPatron() {
        if (selectedNick != null && party.size() < maxSize) {
            if (party.contains(selectedNick)) {
                System.err.println("Member already in Party");
            } else {
                party.add(selectedNick);
                partyList.setListData(party);
            }
        }
    }

    public void removePatron() {
        if (selectedMember != null) {
            party.removeElement(selectedMember);
            partyList.setListData(party);
        }
    }

    public void createNewPatron() {
        NewPatronView newPatron = new NewPatronView(this);
    }

    public void finish() {
        if (party != null && !party.isEmpty()) {
            controlDesk.updateAddParty(this);
        }
        win.setVisible(false);
    }

    public Vector<String> getNames() {
        return party;
    }

    public void updateNewPatron(NewPatronView newPatron) {
        try {
            Bowler checkBowler = BowlerFile.getBowlerInfo(newPatron.getNick());
            if (checkBowler == null) {
                BowlerFile.putBowlerInfo(
                        newPatron.getNick(),
                        newPatron.getFull(),
                        newPatron.getEmail());
                bowlerdb = new Vector<String>(BowlerFile.getBowlers());
                allBowlers.setListData(bowlerdb);
                party.add(newPatron.getNick());
                partyList.setListData(party);
            } else {
                System.err.println("A Bowler with that name already exists.");
            }
        } catch (Exception e) {
            System.err.println("File I/O Error");
        }
    }

    public Vector<String> getParty() {
        return party;
    }
}
