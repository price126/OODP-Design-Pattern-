import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class PausedLanesFile implements Serializable {

    public static void addPausedLane(Lane lane) throws IOException, ClassNotFoundException {
        String filepath = "LANE.DAT";
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Vector storedLaneVector = (Vector) objectIn.readObject();
        objectIn.close();

//        Vector storedLaneVector = new Vector<>();
        Vector v = new Vector<>();
        v.add(true);

        v.add(lane.setter.rnd);
        v.add(lane.setter.pins);
        v.add(lane.setter.foul);
        v.add(lane.setter.throwNumber);

//        System.out.println("In store:" + lane.calculateScore.curScores[0]);
        v.add(lane.calculateScore);
        v.add(lane.gameIsHalted);
        v.add(lane.gameFinished);
        v.add(lane.ball);
        v.add(lane.bowlIndex);

        v.add(lane.frameNumber);
        v.add(lane.tenthFrameStrike);
        v.add(lane.canThrowAgain);
        v.add(lane.gameNumber);
        v.add(lane.currentThrower);

        storedLaneVector.add(v);
        FileOutputStream fileOut = new FileOutputStream(filepath);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(storedLaneVector);
        objectOut.close();
    }

    public static Vector readPausedLanes() throws IOException, ClassNotFoundException {
        String filepath = "LANE.DAT";
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Vector storedLaneVector = (Vector) objectIn.readObject();
        objectIn.close();
        return storedLaneVector;
    }

    public static void resumePausedLane(int index) throws IOException, ClassNotFoundException {
        String filepath = "LANE.DAT";
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Vector storedLaneVector = (Vector) objectIn.readObject();
//        System.out.println("In resume");
//        System.out.println(((Vector)storedLaneVector.get(index)).get(0));
        ((Vector) storedLaneVector.get(index)).set(0, false);
//        System.out.println(((Vector)storedLaneVector.get(index)).get(0));
        objectIn.close();
        FileOutputStream fileOut = new FileOutputStream(filepath);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(storedLaneVector);
        objectOut.close();
    }

}

