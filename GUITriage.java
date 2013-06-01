package triage;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import javax.swing.table.*;

/**
 * A class that contains and defines a graphical user interface for a triage
 * simulation. It provides the simulation with information panels, tables to
 * contain the working pieces, and buttons to control the beginning and end of
 * the simulation.
 * 
 * @author Daniel Kapit, Tim Hersman, Giorgi S
 * 
 */
public class GUITriage extends JFrame {

	/**
	 * A datamodel for a JTable, this one for the table to show the released
	 * patients.
	 */
	static AbstractTableModel dataModel1 = new AbstractTableModel() {
		String[][] data = new String[1000][9];
		String columnNames[] = { "Name", "Age", "Life span", "Severity",
				"Walking", "Breathing", "Mental", "Respiritory", "Location" };

		@Override
		public int getColumnCount() {
			return 9;
		}

		@Override
		public int getRowCount() {
			return 1000;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			this.data[row][col] = value.toString();
			fireTableCellUpdated(row, col);
		}

		@Override
		public String getColumnName(int col) {
			return this.columnNames[col].toString();
		}
	};

	/**
	 * A datamodel for a JTable, this one for the table to show the dead
	 * patients.
	 */
	static AbstractTableModel dataModel2 = new AbstractTableModel() {
		String[][] data = new String[1000][9];
		String columnNames[] = { "Name", "Age", "Life span", "Severity",
				"Walking", "Breathing", "Mental", "Respiritory", "Location" };

		@Override
		public int getColumnCount() {
			return 9;
		}

		@Override
		public int getRowCount() {
			return 1000;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			this.data[row][col] = value.toString();
			fireTableCellUpdated(row, col);
		}

		@Override
		public String getColumnName(int col) {
			return this.columnNames[col].toString();
		}
	};

	/**
	 * A datamodel for a JTable, this one for the table to show the patients
	 * still in the simulation.
	 */
	static AbstractTableModel dataModel3 = new AbstractTableModel() {
		String[][] data = new String[1000][9];
		String columnNames[] = { "Name", "Age", "Life span", "Severity",
				"Walking", "Breathing", "Mental", "Respiritory", "Location" };

		@Override
		public int getColumnCount() {
			return 9;
		}

		@Override
		public int getRowCount() {
			return 1000;
		}

		@Override
		public Object getValueAt(int row, int col) {
			return this.data[row][col];
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			this.data[row][col] = value.toString();
			fireTableCellUpdated(row, col);
		}

		@Override
		public String getColumnName(int col) {
			return this.columnNames[col].toString();
		}
	};

	// The triage from which to pull the simulation
	static Triage tr;

	JLabel steps = new JLabel("Steps: ");
	int stepInt = 0;
	JLabel time = new JLabel("Time: ");
	static JTextField secs = new JTextField("1");

	// centerpane tables
	static JTable table1 = new JTable(dataModel1);
	static JTable table2 = new JTable(dataModel2);
	static JTable table3 = new JTable(dataModel3);

	JScrollPane centerpane = new JScrollPane(table1);
	JScrollPane centerpane1 = new JScrollPane(table2);
	JScrollPane centerpane2 = new JScrollPane(table3);
	JTabbedPane pane = new JTabbedPane();

	// main panels in window
	JPanel infoPanel = new JPanel();
	JPanel mainPanel = new JPanel();
	JPanel actionPanel = new JPanel();

	// three running buttons
	JButton step = new JButton("Step");
	JButton stop = new JButton("Stop");
	JButton start = new JButton("Start");

	// info on the side
	static JTextArea info;
	static int step_pass = 0;
	static int speed = Integer.parseInt(secs.getText());// * 1000;
	static JComboBox algo = new JComboBox();
	static JTextField setSteps = new JTextField("0");
	static JTextField tests = new JTextField("1");
	static int numSteps = Integer.parseInt(setSteps.getText());
	static int temp;
	Timer t = new Timer(speed, new stepButton());
	JLabel tCount = new JLabel("Tests: " + tests.getText());

	/**
	 * The constructor for the frame
	 * 
	 * @param triage
	 *            the triage from which to pull information
	 */
	public GUITriage(Triage triage) {

		GUITriage.tr = triage;
		temp = tr.refill;
		JMenuBar menu = new JMenuBar();
		menu.add(createOptionsItem());
		setJMenuBar(menu);

		JTableHeader header = table1.getTableHeader();
		header.setBackground(java.awt.Color.WHITE);
		mainPanel.add(header);

		fillTable();

		steps.setText("Steps: " + stepInt);
		secs.setText("" + speed);

		// three button with listeners
		this.step.addActionListener(new stepButton());
		this.stop.addActionListener(new stopButton());
		this.start.addActionListener(new startButton());

		this.pane.addTab("Released", this.centerpane);
		this.pane.addTab("Dead", this.centerpane1);
		this.pane.addTab("Triage", this.centerpane2);

		GUITriage.info = new JTextArea("Info: \n \n Released: " + tr.rel.size()
				+ "\n Dead: " + tr.dead.size() + "\n ER: "
				+ tr.er.patients.size() + "\n WR:" + tr.wr.people.size()
				+ "\n Beds: " + tr.er.beds);
		GUITriage.info.setEditable(false);

		infoPanel.add(info, BorderLayout.NORTH);
		algo.addItem("Standard Algorithm");
		algo.addItem("Custom Algorithm");
		mainPanel.add(pane);

		// action panel

		actionPanel.add(steps);
		actionPanel.add(start);
		actionPanel.add(stop);
		actionPanel.add(step);
		actionPanel.add(time);
		actionPanel.add(secs);
		actionPanel.add(tCount);

		add(infoPanel, BorderLayout.LINE_START);
		add(mainPanel, BorderLayout.CENTER);
		add(actionPanel, BorderLayout.SOUTH);
		setTitle("Triage");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		validate();
		pack();
	}

	/**
	 * This method refreshes the tables after each step of the simulation.
	 * 
	 * @throws IOException
	 *             if the names of the patients cannot be found
	 */
	public static void refresh() throws IOException {
		info.setText("Info: \n \n Released: " + tr.rel.size() + "\n Dead: "
				+ tr.dead.size() + "\n ER: " + tr.er.patients.size()
				+ "\n WR: " + tr.wr.people.size() + "\n Beds: " + tr.er.beds);
		// clear the last table
		clear(table3);
		// reset all the patients in the last table
		for (int i = 0; i < tr.tot_patients; i++) {
			if (i < tr.wr.people.size()) {
				Patient p = tr.wr.people.get(i);
				table3.setValueAt(p.name, i, 0);
				table3.setValueAt(p.age, i, 1);
				table3.setValueAt(p.life_span, i, 2);
				table3.setValueAt(p.severity, i, 3);
				table3.setValueAt(p.walking, i, 4);
				table3.setValueAt(p.breathing, i, 5);
				table3.setValueAt(p.mental, i, 6);
				table3.setValueAt(p.resp_rate, i, 7);
				table3.setValueAt("WR", i, 8);
			} else {
				int j = i - tr.wr.people.size();
				Patient p = tr.er.patients.get(j);
				table3.setValueAt(p.name, i, 0);
				table3.setValueAt(p.age, i, 1);
				table3.setValueAt(p.life_span, i, 2);
				table3.setValueAt(p.severity, i, 3);
				table3.setValueAt(p.walking, i, 4);
				table3.setValueAt(p.breathing, i, 5);
				table3.setValueAt(p.mental, i, 6);
				table3.setValueAt(p.resp_rate, i, 7);
				table3.setValueAt("ER", i, 8);
			}
		}

		for (Patient p : tr.rel) {
			int index = tr.rel.indexOf(p);
			table1.setValueAt(p.name, index, 0);
			table1.setValueAt(p.age, index, 1);
			table1.setValueAt(p.life_span, index, 2);
			table1.setValueAt(p.severity, index, 3);
			table1.setValueAt(p.walking, index, 4);
			table1.setValueAt(p.breathing, index, 5);
			table1.setValueAt(p.mental, index, 6);
			table1.setValueAt(p.resp_rate, index, 7);
			table1.setValueAt("Released", index, 8);
		}

		for (Patient p : tr.dead) {
			int index = tr.dead.indexOf(p);
			table2.setValueAt(p.name, index, 0);
			table2.setValueAt(p.age, index, 1);
			table2.setValueAt(p.life_span, index, 2);
			table2.setValueAt(p.severity, index, 3);
			table2.setValueAt(p.walking, index, 4);
			table2.setValueAt(p.breathing, index, 5);
			table2.setValueAt(p.mental, index, 6);
			table2.setValueAt(p.resp_rate, index, 7);
			table2.setValueAt("Morgue", index, 8);
		}
	}

	/**
	 * Clears the table of all of its contents
	 * 
	 * @param t
	 *            the table to clear
	 */
	public static void clear(JTable t) {
		for (int i = 0; i < t.getRowCount(); i++) {
			for (int j = 0; j < t.getColumnCount(); j++) {
				t.setValueAt("", i, j);
			}
		}
	}

	/**
	 * Performs a step through the algorithm, refreshing all relevant
	 * information
	 */
	public static void step() {
		try {
			if (algo.getSelectedItem().equals("Standard Algorithm"))
				tr.triageSim();
			else if (algo.getSelectedItem().equals("Custom Algorithm"))
				tr.triageSim2();
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * An actionlistener to step the program once per clock on a button or set
	 * time
	 * 
	 * @author Daniel Kapit
	 * 
	 */
	public class stepButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			stepInt++;
			steps.setText("Steps: " + stepInt);
			step();
			t.setDelay(Integer.parseInt(secs.getText()));
			if (tr.wr.people.isEmpty() && tr.er.patients.isEmpty()
					&& Integer.parseInt(tests.getText()) == 1) {
				t.stop();
				try {
					printTest(Integer.parseInt(tests.getText()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (tr.wr.people.isEmpty() && tr.er.patients.isEmpty()
					&& Integer.parseInt(tests.getText()) > 1) {
				GUITriage.clear(table1);
				GUITriage.clear(table2);
				GUITriage.clear(table3);
				try {
					printTest(Integer.parseInt(tests.getText()));
					GUITriage.tr = new Triage(GUITriage.tr.init_patients,
							GUITriage.tr.er.beds, GUITriage.tr.wr.capacity);
					GUITriage.tr.refill = temp;
					tests.setText((Integer.parseInt(tests.getText()) - 1) + "");
					tCount.setText("Tests: " + tests.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * An actionlistener to stop the program
	 * 
	 * @author Daniel Kapit
	 * 
	 */
	public class stopButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			t.stop();
		}
	}

	/**
	 * The actionlistener to start the program, beginning the timer.
	 * 
	 * @author Daniel Kapit
	 * 
	 */
	public class startButton implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			tCount.setText("Tests: " + tests.getText());
			t.start();
		}
	}

	/**
	 * A method to return a frame which can adjust the settings of the simulation
	 * @return a JMenuItem that opens a settings frame
	 */
	public JMenuItem createOptionsItem() {
		JMenuItem menu = new JMenuItem("Options...");
		class Option implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFrame settings = new JFrame();
				JPanel te = new JPanel();
				settings.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				settings.setSize(200, 200);
				settings.add(algo, BorderLayout.NORTH);
				te.add(new JLabel("Tests: "));
				te.add(tests);
				settings.add(te, BorderLayout.SOUTH);
				settings.setVisible(true);
			}
		}
		menu.addActionListener(new Option());
		return menu;
	}

	/**
	 * A method to fill the table at the initialization of the program
	 */
	public void fillTable() {
		for (int i = 0; i < tr.wr.people.size(); i++) {
			Patient p = tr.wr.people.get(i);
			table3.setValueAt(p.name, i, 0);
			table3.setValueAt(p.age, i, 1);
			table3.setValueAt(p.life_span, i, 2);
			table3.setValueAt(p.severity, i, 3);
			table3.setValueAt(p.walking, i, 4);
			table3.setValueAt(p.breathing, i, 5);
			table3.setValueAt(p.mental, i, 6);
			table3.setValueAt(p.resp_rate, i, 7);
			table3.setValueAt("WR", i, 8);
		}
	}

	/**
	 * A method that prints the results of a test to a file
	 * @param define the test number
	 * @throws IOException if the file to be written to returns an error
	 */
	public static void printTest(int define) throws IOException {
		BufferedWriter br;
		br = new BufferedWriter(new FileWriter(new File("H:\\"
				+ algo.getSelectedItem() + ".txt"), true));
		br.write("Test " + define + ": Released: " + GUITriage.tr.rel.size()
				+ ", ");
		br.write("Dead: " + GUITriage.tr.dead.size());
		br.newLine();
		br.flush();
		br.close();
	}

	public static void main(String[] args) throws IOException {
		Triage t = new Triage(50, 25, 15);
		t.refill = 15;
		JFrame triage = new GUITriage(t);
		triage.setVisible(true);
	}
}
