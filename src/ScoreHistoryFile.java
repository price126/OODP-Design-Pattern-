/**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.util.*;
import java.io.*;

public class ScoreHistoryFile {

	private static final String SCOREHISTORY_DAT = "SCOREHISTORY.DAT";

	public static void addScore(String nick, String date, String score)
			throws IOException {

		String data = nick + "\t" + date + "\t" + score + "\n";

		RandomAccessFile out = new RandomAccessFile(SCOREHISTORY_DAT, "rw");
		out.skipBytes((int) out.length());
		out.writeBytes(data);
		out.close();
	}

	public static Vector getScores(String nick)
			throws IOException {
		Vector scores = new Vector();

		BufferedReader in =
				new BufferedReader(new FileReader(SCOREHISTORY_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			// File format is nick\tfname\te-mail
			String[] scoredata = data.split("\t");
			//"Nick: scoredata[0] Date: scoredata[1] Score: scoredata[2]
			if (nick.equals(scoredata[0])) {
				scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
			}
		}
		return scores;
	}

	public static Vector getHighestAndLowest(String nick, boolean isGeneral)
			throws IOException {
		Vector scores = new Vector();

		BufferedReader in =
				new BufferedReader(new FileReader(SCOREHISTORY_DAT));
		String data;
		while ((data = in.readLine()) != null) {
			String[] scoredata = data.split("\t");
			scores.add(new Score(scoredata[0], scoredata[1], scoredata[2]));
		}

		Iterator scoreIt = scores.iterator();
		int max = -1;
		int min = 301;
		String bestplayer="";
		String worstplayer="";

		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
//			System.out.print(nick);
//			System.out.print(" ");
//			System.out.print(score.getNickName());
//			System.out.print(" ");
//			System.out.print(nick.equals(score.getNickName()));
//			System.out.print(" ");
//			System.out.print(nick.length());
//			System.out.println(score.getNickName().length());

			if (isGeneral || (!isGeneral && score.getNickName().equals(nick))) // isGeneral is a Boolean variable
			{
				int intScore = Integer.parseInt(score.getScore());

				if (intScore > max)
				{
					bestplayer = score.getNickName();
					max = intScore;
				}
				if (intScore < min)
				{
					worstplayer = score.getNickName();
					min = intScore;
				}
			}
		}

		Vector toreturn = new Vector<>();
		toreturn.add(max);
		toreturn.add(bestplayer);
		toreturn.add(min);
		toreturn.add(worstplayer);

		return toreturn;
	}

	public static double averageScore(String nick) {
		Vector scores = null;
		try{
			scores = getScores(nick);
		} catch (Exception e){System.err.println("Error: " + e);}

		assert scores != null;
		Iterator scoreIt = scores.iterator();

		double sum = 0, count = 0;
		while (scoreIt.hasNext()) {
			Score score = (Score) scoreIt.next();
			sum = sum + (double)Integer.parseInt(score.getScore());
			count = count + 1.0;
		}

		return sum/count;
	}
}

