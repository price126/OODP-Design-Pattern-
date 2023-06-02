import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Lane extends LaneWithPinsetter implements Runnable {
    private ScorableParty scorer;
    private boolean paused;
    private boolean halted;

    Lane() {
        scorer = null;
        paused = false;
    }

    private void exitGame(final String partyName) {
        final EndGameReport egr = new EndGameReport(partyName, scorer.getMemberNicks());
        egr.printer(scorer);
        scorer = null;
        publish();
    }

    void saveState(final String fileName) {
        try {
            final FileWriter fw = new FileWriter(fileName);
            scorer.saveState(fw);
            fw.close();
        } catch (final IOException e) {
            System.err.println("Please check permissions, cannot write file");
        }
    }

    void loadState(final String fileName) {
        paused = true;
        final ScorableParty oldScorer = scorer;

        try {
            final FileReader fr = new FileReader(fileName);
            final BufferedReader bufferedReader = new BufferedReader(fr);
            scorer = new ScorableParty();
            scorer.loadState(bufferedReader);
            bufferedReader.close();
            fr.close();
        } catch (final IOException e) {
            final String err = "No saved file exists, or chosen file has invalid format. Please recheck.";
            JOptionPane.showMessageDialog(null, err);
            System.err.println(err);
            scorer = oldScorer;
        }

        paused = false;
    }

    private void onGameFinish() {
        final String partyName = scorer.getName();

        final EndGamePrompt egp = new EndGamePrompt(partyName);
        final int result = egp.getResult();

        if (result == 1) {
            scorer.onGameFinish();
        } else if (result == 2) {
            exitGame(partyName);
        }
    }

    public final void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            if (isPartyAssigned() && !paused) {
                if (scorer.isFinished()) onGameFinish();
                else {
                    while (halted) Util.busyWait(10);

                    while (scorer.canRollAgain()) rollBall();
                    resetPinsetter();

                    scorer.setFinalScoresOnGameEnd();
                    scorer.nextBowler();
                }
            }
            Util.busyWait(10);
        }
    }

    public final void receiveEvent(final Event pev) {
        final PinsetterEvent pe = (PinsetterEvent) pev;
        if (pe.isReset())
            return;

        final int pinsDownOnThisThrow = pe.pinsDownOnThisThrow();
        scorer.roll(pinsDownOnThisThrow);
        publish();
    }

    final void assignParty(final ScorableParty theParty) {
        scorer = theParty;
        scorer.resetScoresHard();
    }

    Event createEvent() {
        return new LaneEvent(scorer, getPinsDown(), halted);
    }

    final boolean isPartyAssigned() {
        return scorer != null;
    }

    final void pauseGame(final boolean state) {
        halted = state;
        publish();
    }

    void pauseManual(final boolean state) {
        paused = state;
    }

    boolean isPaused() {
        return paused;
    }
}
