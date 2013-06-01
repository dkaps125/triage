package triage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class that creates a triage simulation
 * 
 * @author Daniel Kapit, Tim Hersman, Giorgi Sharmazanashvili
 * 
 */
public class Triage {

//	public static void main(String[] args) throws IOException,
//			InterruptedException {
//		Triage one;
//		BufferedWriter br;
//		br = new BufferedWriter(new FileWriter(new File("H:\\StandardAlgorithm.txt"), true));
//		int i = 1;
//		int dead = 0;;
//		int released = 0;
//		while (i <= 100) {
//			one = new Triage(50, 25, 15, 15);
//			one.refill = 50;
//			one.triageSim();
//			while (!one.wr.people.isEmpty() || !one.er.patients.isEmpty())
//				one.triageSim();
//			//br.write("Test " + i + ": Released: " + one.rel.size() + ", ");
//			dead = dead + one.dead.size();
//			released = released + one.rel.size();
//			br.write((double) one.rel.size()/one.dead.size() +"");
//			br.newLine();
//			i++;
//		}	
//		br.write("Dead: " + (double) dead/100 + " -- " + "Released: " + (double) released/100);
//		br.flush();
//		br.close();
//	}

	EmergencyRoom er;
	WaitingRoom wr;
	static boolean running = true;
	int refill = 0;
	int init_patients;
	int tot_patients;
	ArrayList<Patient> rel = new ArrayList<Patient>();
	ArrayList<Patient> dead = new ArrayList<Patient>();

	/**
	 * The constructor for the simulation
	 * 
	 * @param init_p
	 *            the initial number of patients
	 * @param beds
	 *            the number of beds in the ER
	 * @param doctors
	 *            the number of doctors
	 * @param wait_cap
	 *            the capacity of the waiting room
	 * @throws IOException
	 *             if the patients' names cannot be read
	 */
	public Triage(int init_p, int beds, int wait_cap)
			throws IOException {
		this.er = new EmergencyRoom(beds);
		this.wr = new WaitingRoom(wait_cap);
		this.init_patients = init_p;
		this.tot_patients = init_p;
		for (int i = 0; i < init_p; i++) {
			this.wr.add(Patient.genPatient());
		}
	}

	/**
	 * The logic of the simulation itself
	 * 
	 * @throws IOException
	 *             if the names cannot be read
	 */
	public void triageSim2() throws IOException {

		// sorting the waiting room
		ArrayList<Patient> min = new ArrayList<Patient>();
		ArrayList<Patient> imm = new ArrayList<Patient>();
		ArrayList<Patient> del = new ArrayList<Patient>();

		for (Patient p : this.wr.people) {
			// int t = analyze(p);
			int t = custAlg(p);
			if (t == 1)
				min.add(p);
			else if (t == 2)
				del.add(p);
			else if (t == 3)
				imm.add(p);
		}
		min = HeapSort.heapSort(min, min.size());
		del = HeapSort.heapSort(del, del.size());
		imm = HeapSort.heapSort(imm, imm.size());
		this.wr.people.clear();
		for (Patient pat : min)
			this.wr.people.add(pat);
		for (Patient pat : del)
			this.wr.people.add(pat);
		for (Patient pat : imm)
			this.wr.people.add(pat);

		// adding to the emergency room

		while (this.er.hasBeds() && this.wr.people.size() > 0)
			if (this.wr.people.size() > 0)
				this.er.patients.add(this.wr.people.remove(0));

		this.dead.addAll(this.wr.step());
		this.rel.addAll(this.er.step());
		this.er.patients.removeAll(this.rel);
		this.wr.people.removeAll(this.dead);

		this.tot_patients = this.er.patients.size() + this.wr.people.size();
		if (this.refill != 0) {
			for (int i = 0; i < this.refill; i++) {
				this.wr.add(Patient.genPatient());
				this.tot_patients++;
				this.refill--;
			}
		}

	}

	/**
	 * The logic of the simulation itself
	 * 
	 * @throws IOException
	 *             if the names cannot be read
	 */
	public void triageSim() throws IOException {

		// sorting the waiting room
		ArrayList<Patient> min = new ArrayList<Patient>();
		ArrayList<Patient> imm = new ArrayList<Patient>();
		ArrayList<Patient> del = new ArrayList<Patient>();

		for (Patient p : this.wr.people) {
			int t = analyze(p);
			if (t == 1 || t == 0)
				min.add(p);
			else if (t == 2)
				del.add(p);
			else if (t == 3)
				imm.add(p);
		}
		min = HeapSort.heapSort(min, min.size());
		del = HeapSort.heapSort(del, del.size());
		imm = HeapSort.heapSort(imm, imm.size());
		this.wr.people.clear();
		for (Patient pat : min)
			this.wr.people.add(pat);
		for (Patient pat : del)
			this.wr.people.add(pat);
		for (Patient pat : imm)
			this.wr.people.add(pat);

		// adding to the emergency room

		while (this.er.hasBeds() && this.wr.people.size() > 0)
			if (this.wr.people.size() > 0)
				this.er.patients.add(this.wr.people.remove(0));

		this.dead.addAll(this.wr.step());
		this.rel.addAll(this.er.step());
		this.er.patients.removeAll(this.rel);
		this.wr.people.removeAll(this.dead);

		this.tot_patients = this.er.patients.size() + this.wr.people.size();

		if (this.refill != 0) {
			for (int i = 0; i < this.refill; i++) {
				this.wr.add(Patient.genPatient());
				this.tot_patients++;
				this.refill--;
			}
		}

	}

	/**
	 * A class that tests our own algorithm. This algorithm takes those who can be treated faster
	 * and treats them first, leaving others for later.
	 * @param p the patient in the waiting room
	 * @return the severity of the patient
	 */
	public static int custAlg(Patient p) {
		if (p.life_span != -1)
			return p.severity;

		else if (p.walking == true) {
			p.severity = Patient.MINOR;
			p.life_span = new Random().nextInt(10) + 75;
			return p.severity;
		} else {
			if (p.breathing == false) {
				p.severity = Patient.IMMEDIATE;
				p.life_span = new Random().nextInt(10) + 10;
				return p.severity;
			} else {
				if (p.resp_rate > 30 || p.resp_rate < 15) {
					p.severity = Patient.IMMEDIATE;
					p.life_span = new Random().nextInt(20) + 20;
					return p.severity;
				} else {
					if (p.mental == false) {
						p.severity = Patient.IMMEDIATE;
						p.life_span = new Random().nextInt(25) + 30;
						return p.severity;
					} else {
						p.severity = Patient.DELAYED;
						p.life_span = new Random().nextInt(10) + 60;
						return p.severity;
					}
				}
			}
		}

	}

	/**
	 * Analyzes the patients to determine severity and life span
	 * 
	 * @param p
	 *            the patient to be analyzed
	 * @return the severity of the patient
	 */
	public static int analyze(Patient p) {
		if (p.life_span != -1)
			return p.severity;

		else if (p.walking == true) {
			p.severity = Patient.MINOR;
			p.life_span = new Random().nextInt(10) + 75;
			return p.severity;
		} else {
			if (p.breathing == false) {
				p.severity = Patient.IMMEDIATE;
				p.life_span = new Random().nextInt(10) + 10;
				return p.severity;
			} else {
				if (p.resp_rate > 30 || p.resp_rate < 15) {
					p.severity = Patient.IMMEDIATE;
					p.life_span = new Random().nextInt(20) + 20;
					return p.severity;
				} else {
					if (p.mental == false) {
						p.severity = Patient.IMMEDIATE;
						p.life_span = new Random().nextInt(25) + 30;
						return p.severity;
					}

					else {
						p.severity = Patient.DELAYED;
						p.life_span = new Random().nextInt(10) + 60;
						return p.severity;
					}
				}
			}
		}
	}
}
