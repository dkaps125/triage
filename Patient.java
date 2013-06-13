package triage;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * A class that defines a patient for a triage simulation
 * @author Daniel Kapit, Tim Hersman, Giorgi Sharmazanashvili
 *
 */
public class Patient extends Person {
	int life_span;
	int wait_time;
	int severity;
	boolean walking;
	boolean breathing;
	int resp_rate;
	boolean mental;
	final static int FUCKED = 0;
	final static int MINOR = 1;
	final static int DELAYED = 2;
	final static int IMMEDIATE = 3; 
	static Random gen = new Random();
	boolean child;
	String loc = "WR";
	
	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Patient p = genPatient();
		System.out.println(p.toString() + "!");
	}
	
	/**
	 * An empty constructor for use with the names function
	 */
	public Patient() {}
	
	/**
	 * A constructor for a patient
	 * @param n the name of the patient
	 * @param a the patient's age
	 * @param life the patient's life span
	 * @param resp the patient's respiratory rate
	 * @param w whether the patient is walking
	 * @param b whether the patient is breathing
	 */
	public Patient(String n, int a, int life, int resp, boolean w, boolean b) {
		super(n, a);
		this.life_span = life;
		this.resp_rate = resp;
		this.walking = w;
		this.breathing = b;
		if (a < 13)
			this.child = true;
		this.child = false;
	}
	
	/**
	 * Generates a random patient with random properties
	 * @return a new patient
	 * @throws IOException if the name file cannot be read
	 */
	public static Patient genPatient() throws IOException {
		ArrayList<ArrayList<String>> a = getNames();
		String fname = a.get(0).get(gen.nextInt(a.get(0).size()));
		String lname = a.get(1).get(gen.nextInt(a.get(1).size()));
		int r;
		boolean w;
		boolean b = genBool();
		if (b == false) {
			r = 0;
			w = false;
		}
		else {
			r = gen.nextInt(40);
			w = genBool();
		}
		Patient p = new Patient(lname + ", " + fname, gen.nextInt(95), 
				-1, r, w, b);
		p.mental = genBool();
		return p;
	}
	
	public String names() {
		Scanner scan = new Scanner(getClass().getResourceAsStream("Names.txt"));
		String tot = "";
		int i = 1;
		while (scan.hasNext()) {
			if (i == 1) {
				tot += scan.next() + " ";
				i = 2;
			}
			
			else if (i == 2) {
				tot += scan.next() + "\n";
				i = 1;
			}
		}
		return tot;
	}
	
	/**
	 * Gets names from an input file
	 * @return an arraylist of arraylists of names, first and last
	 * @throws IOException if the file cannot be read
	 */
	public static ArrayList<ArrayList<String>> getNames() throws IOException {
		ArrayList<String> fname = new ArrayList<String>();
		ArrayList<String> lname = new ArrayList<String>();
		Scanner buf = new Scanner((new Patient()).names());
		while (buf.hasNextLine()) {
			String s = buf.nextLine();
			if (s == null)
				continue;
			Scanner scan = new Scanner(s);
			scan.useDelimiter(" ");
			if (scan.hasNext())
				fname.add(scan.next());
			if (scan.hasNext())
				lname.add(scan.next());
		}
		ArrayList<ArrayList<String>> a = new ArrayList<ArrayList<String>>();
		a.add(fname);
		a.add(lname);
		buf.close();
		return a;
	}
	
	/**
	 * Generates a random boolean based on a random number
	 * @return a random boolean
	 */
	public static boolean genBool() {
		Random bool = new Random();
		int i = bool.nextInt(2);
		if (i == 1)
			return true;
		return false;
			
	}
	
	/**
	 * Creates a string for a patient object
	 */
	@Override
	public String toString() {
		String fin = this.name;
		fin = fin + ": Age = " + this.age + "; Life span = " + this.life_span
			+ "; Walking: " + walking + "; Breathing: " + breathing
			+ "; Respiritory Rate = " + resp_rate + ". " + this.severity;
		return fin;
	}
}
