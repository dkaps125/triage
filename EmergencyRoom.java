package triage;

import java.util.ArrayList;

/**
 * A class that defines the emergency room of a triage simulation
 * @author Daniel Kapit, Tim Hersman, Giorgi S
 *
 */
public class EmergencyRoom {
	int beds;
	ArrayList<Patient> patients = new ArrayList<Patient>();
	
	/**
	 * A constructor for an emergency room
	 * @param beds the number of beds in the ER
	 * @param docs the number of doctors in the ER
	 */
	public EmergencyRoom(int beds) {
		this.beds = beds;
	}
	
	/**
	 * Determines whether there are any open beds in the ER
	 * @return a boolean: true if there are beds available, false otherwise
	 */
	public boolean hasBeds() {
		if (this.patients.size() == this.beds)
			return false;
		return true;
	}
	
	/**
	 * Steps through all of the patients in the algorithm, changing their qualities
	 * @return an arraylist of all patients in the ER
	 */
	public ArrayList<Patient> step() {
		ArrayList<Patient> release = new ArrayList<Patient>();
		for (Patient p : this.patients) {
			p.life_span += 1;
			if (p.life_span < 55)
				p.severity = Patient.IMMEDIATE;		
			else if (p.life_span <= 70) {
				p.severity = Patient.DELAYED;
				p.breathing = true;
			}
			else if (p.life_span > 70) {
				p.severity = Patient.MINOR;
				p.walking = true;
			}
			if (p.life_span > 86)
				release.add(p);
		}
		return release;
	}
}
