package triage;

import java.util.ArrayList;

/**
 * Waiting room object holds patients before admittance
 * @author Daniel Kapit, Tim Hersman, Giorgi Sharmazanashvili
 *
 */
public class WaitingRoom extends Room {
	ArrayList<Patient> people = new ArrayList<Patient>();
	int capacity;
	
	/**
	 * Constructor of waiting room
	 * @param capacity the amount of people who can fit in the waiting room
	 */
	public WaitingRoom(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * Adds a patient to the waiting room
	 * @param p the patient to be added
	 */
	//needs to deal with overflow
	public void add(Patient p) {
		this.people.add(p);
	}
	
	/**
	 * Removes a patient from the waiting room
	 * @param p the patient to be removed
	 */
	public void remove(Patient p) {
		this.people.remove(p);
	}
	
	/**
	 * The actions to be performed every step of the simulation
	 * @return a list of dead patients
	 */
	public ArrayList<Patient> step() {
		ArrayList<Patient> dead = new ArrayList<Patient>();
		for (Patient p : this.people) { 
			p.life_span -= 1;
			if (p.life_span <= 0)
				dead.add(p);
		}
		return dead;
	}
}
