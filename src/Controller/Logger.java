package Controller;

import java.io.*;

public class Logger {
	
	public static final String path = "logs/logs.txt";
	
	public static void log (String error, String message) {
		String time = java.time.LocalDateTime.now ().toString ().replaceAll ("[^\\d\\w]", "_");
		System.out.println ("event: " + error + " has occurred.");
		System.out.println (message);
		System.out.println (time);
		try {
			StringBuilder sb = new StringBuilder ();
			sb.append (time.split ("T")[0].replaceAll ("_", "-"))
					.append ("\t")
					.append (time.split ("T")[1].replaceAll ("_", ":"))
					.append ("\t[")
					.append (error)
					.append ("]\t--- ")
					.append (message);

			PrintWriter pw = new PrintWriter (new FileWriter (path, true));
			pw.println (sb.toString ());
			pw.close ();
		} catch (FileNotFoundException ex) {
			System.out.println ("File not found error @ logger");
			System.out.println ("Exiting program now");
			System.exit (0);
		} catch (IOException e) {
			System.out.println ("I/O error @ logger");
			System.out.println ("Exiting program now");
			System.exit (0);
		}
	}

	public static void dblog (String event, String user, String description) {
		String datetime = java.time.LocalDateTime.now ().toString ().replaceAll ("[^\\d\\w]", "_");
		String date = datetime.split ("T")[0].replaceAll ("_", "-");
		String[] time = datetime.split ("T")[1].split ("_");
		System.out.println ("database log: " + user + "\t" + description);

		Main.getInstance ().sqlite.addLogs (event, user, description, date + " " + String.join (":", time[0], time[1], time[2]) + "." + time[3]);
	}
	
}