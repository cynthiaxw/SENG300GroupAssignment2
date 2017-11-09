package ca.ucalgary.seng300.a1;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
public class EventLog {
	PrintWriter writer;
	public EventLog() {
		try {
			writer = new PrintWriter("WorkLog.txt", "UTF-8");
			writer.println("DATE/TIME \t\t EVENT");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.out.println("trouble creating WorkLog file");
			e.printStackTrace();
		}		
	}
	public void buttonPushed(String pname, int btnNum) {
		//print the selection made
		timeStamp();
		writer.println("button: " + btnNum + " pushed. Selection is: " + pname + "\n");
	}
	public void coinInserted(int val, int crdt) {
		//print value of coin, and total credit
		timeStamp();
		writer.println("Coin of value: " + val + " inserted. Credit: " + crdt + "\n");
	}
	public void changeReturned(int chng) {
		//print how much change returned
		timeStamp();
		writer.println("Change has been returned in the amount of: " + chng + "\n");
	}
	public void popDispensed(String pName, int crd) {
		//print which type of pop is dispensed
		timeStamp();
		writer.println(pName + " has been dispensed \t Credit is now: " + crd + "\n");
	}
	public void timeStamp() {
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		writer.println(df.format(date) + "\t\t");
	}
	
}
