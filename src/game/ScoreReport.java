package game; /**
 * 
 * SMTP implementation based on code by R?al Gagnon mailto:real@rgagnon.com
 * 
 */


import user.Bowler;
import viewer.PrintableText;

import java.io.*;
import java.util.Vector;
import java.util.Iterator;
import java.net.*;
import java.awt.print.*;

public class ScoreReport {

	private StringBuilder content;
	
	public ScoreReport(Bowler bowler, int[] scores, int games ) {
		String nick = bowler.getNick();
		String full = bowler.getFullName();
		Vector v = null;
		try{
			v = ScoreHistoryFile.getScores(nick);
		} catch (Exception e){System.err.println("Error: " + e);}

		assert v != null;
		Iterator scoreIt = v.iterator();
		
		content = new StringBuilder("--Lucky Strike Bowling lane.Alley game.Score Report--\n\n");
		content.append("Report for ").append(full).append(", aka \"").append(nick).append("\":\n\n");
		content.append("Final scores for this session: ");
		content.append(scores[0]);

		for (int i = 1; i < games; i++){
			content.append(", ").append(scores[i]);
		}

		content.append(".\n\n\n");
		content.append("Previous scores by date: \n");

		while (scoreIt.hasNext()){
			Score score = (Score) scoreIt.next();
			content.append("  ").append(score.getDate()).append(" - ").append(score.getScore());
			content.append("\n");
		}

		content.append("\n\n");
		content.append("Thank you for your continuing patronage.");
		System.out.println(content);
	}

	public void sendEmail(String recipient) {
		try {
			Socket s = new Socket("osfmail.rit.edu", 25);
			BufferedReader in =
				new BufferedReader(
					new InputStreamReader(s.getInputStream(), "8859_1"));
			BufferedWriter out =
				new BufferedWriter(
					new OutputStreamWriter(s.getOutputStream(), "8859_1"));

			String boundary = "DataSeparatorString";

			// here you are supposed to send your username
			sendln(in, out, "HELLO world");
			sendln(in, out, "MAIL FROM: <mda2376@rit.edu>");
			sendln(in, out, "RCPT TO: <" + recipient + ">");
			sendln(in, out, "DATA");
			sendln(out, "Subject: Bowling game.Score Report ");
			sendln(out, "From: <Lucky Strikes Bowling Club>");

			sendln(out, "Content-Type: text/plain; charset=\"us-ascii\"\r\n");
			sendln(out, content + "\n\n");
			sendln(out, "\r\n");

			sendln(in, out, ".");
			sendln(in, out, "QUIT");
			s.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendPrintout() {
		PrinterJob job = PrinterJob.getPrinterJob();

		PrintableText printobj = new PrintableText(content.toString());

		job.setPrintable(printobj);

		if (job.printDialog()) {
			try {
				job.print();
			} catch (PrinterException e) {
				System.out.println(e);
			}
		}

	}

	public void sendln(BufferedReader in, BufferedWriter out, String s) {
		try {
			out.write(s + "\r\n");
			out.flush();
			in.readLine();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendln(BufferedWriter out, String s) {
		try {
			out.write(s + "\r\n");
			out.flush();
			System.out.println(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
