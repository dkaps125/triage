package triage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HeapSort {
	
	public static void main(String[] args) throws IOException {
		ArrayList<Patient> p = new ArrayList<Patient>();
		for (int i = 0; i < 20; i++) {
			Patient npat = Patient.genPatient();
			Triage.analyze(npat);
			p.add(npat);
		}
		ArrayList<Patient> np = heapSort(p, p.size());
		for (Patient a : np)
			System.out.println(a.toString());
	}
	
	public static ArrayList<Patient> heapSort(ArrayList<Patient> a, int length) {
		a = heapify(a, a.size());
		int end = length - 1;
		while (end > 0) {
			swap(a, end, 0);
			end -= 1;
			siftDown(a, 0, end);
		}
		return a;
	}
	
	private static ArrayList<Patient> heapify(ArrayList<Patient> a, int length) {
		int start = (length - 2) / 2;
		while (start >= 0) {
			siftDown(a, start, length-1);
			start -= 1;
		}
		return a;
	}
	
	private static ArrayList<Patient> siftDown(ArrayList<Patient> a, int start, int end) {
		int root = start;
		while ((root * 2) + 1 <= end) {
			int child = (root * 2) + 1;
			int swap = root;
			if (a.get(swap).life_span < a.get(child).life_span) {
				swap = child;
			}
			if (child + 1 <= end && a.get(swap).life_span < a.get(child+1).life_span) {
				swap = child + 1;
			}
			if (swap != root) {
				swap(a, root, swap);
				root = swap;
			}
			else 
				return a;
		}
		return a;
	}
	
	private static ArrayList<Patient> swap(ArrayList<Patient> a, int one, int two) {
		if (a.size() > one && a.size() > two) {
			Patient temp = a.get(one);
			a.set(one, a.get(two));
			a.set(two, temp);
		}
		return a;
	}
}
