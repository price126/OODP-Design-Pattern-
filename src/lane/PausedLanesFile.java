package lane;

import java.io.*;
import java.util.Vector;

public class PausedLanesFile implements Serializable {

    public static void addPausedLane(Lane lane) throws IOException, ClassNotFoundException {
        String filepath = "./src/lane/LANE.DAT";
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Vector storedLaneVector = (Vector) objectIn.readObject();
        objectIn.close();

        Vector v = new Vector<>();
        v.add(true);

        v.add(lane.setter.rnd);
        v.add(lane.setter.pins);
        v.add(lane.setter.foul);
        v.add(lane.setter.throwNumber);

        v.add(lane.scoreCalculator);
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
        String filepath = "./src/lane/LANE.DAT";
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Vector storedLaneVector = (Vector) objectIn.readObject();
        objectIn.close();
        return storedLaneVector;
    }

    public static void resumePausedLane(int index) throws IOException, ClassNotFoundException {
        String filepath = "./src/lane/LANE.DAT";
        FileInputStream fileIn = new FileInputStream(filepath);
        ObjectInputStream objectIn = new ObjectInputStream(fileIn);
        Vector storedLaneVector = (Vector) objectIn.readObject();
        ((Vector) storedLaneVector.get(index)).set(0, false);
        objectIn.close();
        FileOutputStream fileOut = new FileOutputStream(filepath);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(storedLaneVector);
        objectOut.close();
    }
}

