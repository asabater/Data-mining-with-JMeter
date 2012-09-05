/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.jmeter.visualizers;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import org.apache.jmeter.gui.util.JMeterColor;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

/**
 * This class implements a statistical analyser that calculates both the average
 * and the standard deviation of the sampling process and outputs them as
 * autoscaling plots.
 * 
 * Created February 8, 2001
 * 
 */
public class GraphVisualizer extends AbstractVisualizer implements
		ImageVisualizer, ItemListener, Clearable {

	/** The go. */
	private JButton go;

	/** The chooser. */
	private JFileChooser chooser;

	/** The label folder. */
	private JLabel labelFolder;

	/** The label full screen. */
	private JLabel labelFullScreen;

	/** The choosertitle. */
	private String choosertitle;

	/** The destination folder. */
	public static String destinationFolder;

	/** The test id. */
	public static int testId;

	/** The req url. */
	private String reqUrl;

	/** The clean url. */
	private String cleanUrl;

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 240L;

	/** The Constant ZERO. */
	private static final String ZERO = "0"; //$NON-NLS-1$

	/** The log file name. */
	public static String logFileName;

	/** The result file name. */
	public static String resultFileName;

	/** The nf. */
	private NumberFormat nf = NumberFormat.getInstance(); // OK, because used in
															// synchronised
															// method
	/** The log file. */
	private static File logFile;

	/** The writer log. */
	private static FileWriter writerLog;

	/** The result file. */
	private static File resultFile;

	/** The writer result. */
	private static FileWriter writerResult;

	/** The model. */
	private CachingStatCalculator model;

	/** The max y field. */
	private JTextField maxYField = null;

	/** The min y field. */
	private JTextField minYField = null;

	/** The no samples field. */
	private JTextField noSamplesField = null;

	/** The html tag1 label. */
	private JLabel htmlTag1Label;

	/** The html label. */
	private JTextField htmlLabel = null;

	/** The minute. */
	private String minute = JMeterUtils.getResString("minute"); // $NON-NLS-1$

	/** The graph. */
	private Graph graph;

	/** The data. */
	private JCheckBox data;

	/** The average. */
	private JCheckBox average;

	/** The deviation. */
	private JCheckBox deviation;

	/** The throughput. */
	private JCheckBox throughput;

	/** The median. */
	private JCheckBox median;

	/** The data field. */
	private JTextField dataField;

	/** The average field. */
	private JTextField averageField;

	/** The deviation field. */
	private JTextField deviationField;

	/** The throughput field. */
	private JTextField throughputField;

	/** The median field. */
	private JTextField medianField;

	/** The is windows. */
	private static Boolean isWindows = false;

	/**
	 * Constructor for the GraphVisualizer object.
	 */
	public GraphVisualizer() {
		model = new CachingStatCalculator("Graph");
		graph = new Graph(model);
		init();
	}

	/**
	 * Gets the Image attribute of the GraphVisualizer object.
	 * 
	 * @return the Image value
	 */
	public Image getImage() {
		Image result = graph.createImage(graph.getWidth(), graph.getHeight());

		graph.paintComponent(result.getGraphics());

		return result;
	}

	/**
	 * Update gui.
	 * 
	 * @param s
	 *            the s
	 */
	public synchronized void updateGui(Sample s) {
		System.out.print(s);
		// We have received one more sample
		graph.updateGui(s);

		noSamplesField.setText(Long.toString(s.getCount()));
		dataField.setText(Long.toString(s.getData()));
		averageField.setText(Long.toString(s.getAverage()));
		deviationField.setText(Long.toString(s.getDeviation()));
		throughputField.setText(nf.format(60 * s.getThroughput()) + "/"
				+ minute); // $NON-NLS-1$
		medianField.setText(Long.toString(s.getMedian()));
		updateYAxis();
	}

	/**
	 * Adds the.
	 * 
	 * @param res
	 *            the res
	 */
	public void add(SampleResult res) {
		String sSistemaOperativo = System.getProperty("os.name");
		System.out.println("os: " + sSistemaOperativo);

		if (sSistemaOperativo.contains("Windows")) {
			isWindows = true;
		}

		updateGui(model.addSample(res));
		String filename = "";
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HH.mm.ss");
		try {

			reqUrl = res.getUrlAsString();

			cleanUrl = reqUrl;
			cleanUrl = cleanUrl.replace("http", "");
			cleanUrl = cleanUrl.replace(":", "");
			cleanUrl = cleanUrl.replace("//", "");
			cleanUrl = cleanUrl.replace("?", "-");
			cleanUrl = cleanUrl.replace("&.", "-");
			// cleanUrl = cleanUrl.replace("www.", "");
			cleanUrl = cleanUrl.replace("/", "");

			filename = cleanUrl + ".html";

			String folderName = "$" + dateFormat.format(cal.getTime()) + "_";

			File subfolder;

			if (!isWindows) {
				subfolder = new File(getDestinationFolder() + System.getProperty("file.separator") + folderName);
			} else {
				subfolder = new File(getDestinationFolder() + "\\" + folderName);
			}
			subfolder.mkdir();

			File file;
			if (!isWindows) {
				file = new File(getDestinationFolder() + System.getProperty("file.separator") + folderName + System.getProperty("file.separator")
						+ filename);
			} else {
				file = new File(getDestinationFolder() + "\\" + folderName
						+ "\\" + filename);
			}
			FileWriter writer = new FileWriter(file);

			res.setEncodingAndType("text/xml; charset=utf-8");

			writer.write(res.getResponseDataAsString());

			writer.close();

			logFile = new File(getDestinationFolder() + System.getProperty("file.separator") + getLogFileName());

			FileWriter writerLog = new FileWriter(logFile, true);

			writerLog.append("#" + cleanUrl + "@");
			writerLog.append(getDestinationFolder() + System.getProperty("file.separator") + folderName + System.getProperty("file.separator")
					+ filename + "\n");
			writerLog.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Gets the label resource.
	 * 
	 * @return the label resource
	 */
	public String getLabelResource() {
		return "graph_results_title"; // $NON-NLS-1$
	}

	/**
	 * Gets the static label. Note: Changed to show the label we want
	 * 
	 * @return the static label
	 */
	public String getStaticLabel() {
		return "HTML Downloader";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ItemListener#itemStateChanged(java.awt.event.ItemEvent)
	 */
	public void itemStateChanged(ItemEvent e) {
		if (e.getItem() == data) {
			this.graph.enableData(e.getStateChange() == ItemEvent.SELECTED);
		} else if (e.getItem() == average) {
			this.graph.enableAverage(e.getStateChange() == ItemEvent.SELECTED);
		} else if (e.getItem() == deviation) {
			this.graph
					.enableDeviation(e.getStateChange() == ItemEvent.SELECTED);
		} else if (e.getItem() == throughput) {
			this.graph
					.enableThroughput(e.getStateChange() == ItemEvent.SELECTED);
		} else if (e.getItem() == median) {
			this.graph.enableMedian(e.getStateChange() == ItemEvent.SELECTED);
		}
		this.graph.repaint();
	}

	/**
	 * Clear data.
	 */
	public void clearData() {
		graph.clearData();
		model.clear();
		dataField.setText(ZERO);
		averageField.setText(ZERO);
		deviationField.setText(ZERO);
		throughputField.setText("0/" + minute); //$NON-NLS-1$
		medianField.setText(ZERO);
		noSamplesField.setText(ZERO);
		updateYAxis();
		repaint();
	}

	// @Override
	/**
	 * To string.
	 * 
	 * @return the string
	 */
	public String toString() {
		return "Show the samples analysis as dot plots";
	}

	/**
	 * Update the max and min value of the Y axis.
	 */
	private void updateYAxis() {
		maxYField.setText(Long.toString(graph.getGraphMax()));
		minYField.setText(ZERO);
	}

	/**
	 * Initialize the GUI & other settings.
	 */
	private void init() {

		this.setLayout(new BorderLayout());

		Random generator2 = new Random(42444232);
		testId = generator2.nextInt();

		// MAIN PANEL
		Border margin = new EmptyBorder(10, 10, 5, 10);

		this.setBorder(margin);

		// Set up the graph with header, footer, Y axis and graph display
		JPanel graphPanel = new JPanel(new BorderLayout());
		graphPanel.add(createYAxis(), BorderLayout.WEST);
		graphPanel.add(createChoosePanel(), BorderLayout.NORTH);
		// graphPanel.add(createDagPanelPanel(), BorderLayout.CENTER);
		graphPanel.add(createGraphInfoPanel(), BorderLayout.SOUTH);

		// Add the main panel and the graph
		this.add(makeTitlePanel(), BorderLayout.NORTH);
		this.add(graphPanel, BorderLayout.CENTER);
	}

	// Methods used in creating the GUI

	/**
	 * Creates the panel containing the graph's Y axis labels.
	 * 
	 * @return the Y axis panel
	 */
	private JPanel createYAxis() {
		JPanel graphYAxisPanel = new JPanel();

		graphYAxisPanel.setLayout(new BorderLayout());

		maxYField = createYAxisField(5);
		minYField = createYAxisField(3);

		// Not needed in HTML Downloader
		// graphYAxisPanel.add(createYAxisPanel("graph_results_ms", maxYField),
		// BorderLayout.NORTH); // $NON-NLS-1$
		// graphYAxisPanel.add(createYAxisPanel("graph_results_ms", minYField),
		// BorderLayout.SOUTH); // $NON-NLS-1$

		return graphYAxisPanel;
	}

	/**
	 * Creates a text field to be used for the value of a Y axis label. These
	 * fields hold the minimum and maximum values for the graph. The units are
	 * kept in a separate label outside of this field.
	 * 
	 * @param length
	 *            the number of characters which the field will use to calculate
	 *            its preferred width. This should be set to the maximum number
	 *            of digits that are expected to be necessary to hold the label
	 *            value.
	 * @return a text field configured to be used in the Y axis
	 * @see #createYAxisPanel(String, JTextField)
	 */
	private JTextField createYAxisField(int length) {
		JTextField field = new JTextField(length);
		field.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		field.setEditable(false);
		field.setForeground(Color.black);
		field.setBackground(getBackground());
		field.setHorizontalAlignment(JTextField.RIGHT);
		return field;
	}

	/**
	 * Creates a panel for an entire Y axis label. This includes the dynamic
	 * value as well as the unit label.
	 * 
	 * @param labelResourceName
	 *            the name of the label resource. This is used to look up the
	 *            label text using {@link JMeterUtils#getResString(String)}.
	 * @param field
	 *            the field
	 * @return a panel containing both the dynamic and static parts of a Y axis
	 *         label
	 */
	private JPanel createYAxisPanel(String labelResourceName, JTextField field) {
		JPanel panel = new JPanel(new FlowLayout());
		JLabel label = new JLabel(JMeterUtils.getResString(labelResourceName));

		panel.add(field);
		panel.add(label);
		return panel;
	}

	/**
	 * Creates a panel which allows the user to choose the folder where the
	 * sourcecode have to be saved and to display the directed acyclic graphs
	 * from the realations between the webpages.
	 * 
	 * @return a panel allowing the user to choose which graphs to display
	 */
	public JPanel createChoosePanel() {
		JPanel chooseGraphsPanel = new JPanel();

		chooseGraphsPanel.setLayout(new BoxLayout(chooseGraphsPanel,
				BoxLayout.Y_AXIS));

		TitledBorder title;
		title = BorderFactory.createTitledBorder("HTML Downloader prefecences");
		chooseGraphsPanel.setBorder(title);

		JButton buttonFolder = new JButton("Select");
		labelFolder = new JLabel("Destination Folder: ");
		MySelectFolder msf = new MySelectFolder();
		buttonFolder.addActionListener(msf);

		chooseGraphsPanel.add(labelFolder);
		chooseGraphsPanel.add(buttonFolder);

		/*
		 * UNCOMMENT FOR KEYNOTE //Until we have all the data, DAGS cant be
		 * displayed dagViewerButton.setEnabled(false);
		 */

		JFileChooser folderChooser = new JFileChooser();
		return chooseGraphsPanel;
	}

	/**
	 * Gets the destination folder.
	 * 
	 * @return the destination folder
	 */
	public static String getDestinationFolder() {
		return destinationFolder;
	}

	/**
	 * Sets the destination folder.
	 * 
	 * @param value
	 *            the new destination folder
	 */
	public static void setDestinationFolder(String value) {
		destinationFolder = value;
	}

	/**
	 * Gets the log file name.
	 * 
	 * @return the log file name
	 */
	public static String getLogFileName() {
		System.out.println("getLogFileName:" + logFileName);
		return logFileName;
	}

	/**
	 * Gets the result file name.
	 * 
	 * @return the result file name
	 */
	public static String getResultFileName() {
		System.out.println("getResultFileName:" + resultFileName);
		return resultFileName;
	}

	/**
	 * Sets the log file name.
	 * 
	 * @param s
	 *            the new log file name
	 */
	public static void setLogFileName(String s) {
		logFileName = s;
	}

	/**
	 * Sets the result file name.
	 * 
	 * @param s
	 *            the new result file name
	 */
	public static void setResultFileName(String s) {
		resultFileName = s;
	}

	/**
	 * Creates a check box configured to be used to in the choose panel allowing
	 * the user to select whether or not a particular kind of graph data will be
	 * 
	 * displayed.
	 * 
	 * @param labelResourceName
	 *            the name of the label resource. This is used to look up the
	 *            label text using {@link JMeterUtils#getResString(String)}.
	 * @param color
	 *            the color used for the checkbox text. By convention this is
	 *            the same color that is used to draw the graph and for the
	 *            corresponding info field.
	 * 
	 * @return a checkbox allowing the user to select whether or not a kind of
	 *         graph data will be displayed
	 */
	private JCheckBox createChooseCheckBox(String labelResourceName, Color color) {
		JCheckBox checkBox = new JCheckBox(
				JMeterUtils.getResString(labelResourceName));
		checkBox.setSelected(true);
		checkBox.addItemListener(this);
		checkBox.setForeground(color);
		return checkBox;
	}

	/**
	 * NOTE: DO NOT DELETE OR MODIFY Creates a scroll pane containing the actual
	 * graph of the results.
	 * 
	 * @return a scroll pane containing the graph
	 */
	private Component createGraphPanel() {
		JScrollPane graphScrollPanel = makeScrollPane(graph,
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		graphScrollPanel.setViewportBorder(BorderFactory.createEmptyBorder(2,
				2, 2, 2));
		graphScrollPanel.setPreferredSize(graphScrollPanel.getMinimumSize());

		return graphScrollPanel;
	}

	/**
	 * NOTE: DO NOT DELETE OR MODIFY Creates a panel which numerically displays
	 * the current graph values.
	 * 
	 * @return a panel showing the current graph values
	 */
	private Box createGraphInfoPanel() {
		Box graphInfoPanel = Box.createHorizontalBox();

		noSamplesField = createInfoField(Color.black, 6);
		dataField = createInfoField(Color.black, 5);
		averageField = createInfoField(Color.blue, 5);
		deviationField = createInfoField(Color.red, 5);
		throughputField = createInfoField(JMeterColor.dark_green, 15);
		medianField = createInfoField(JMeterColor.purple, 5);

		graphInfoPanel.add(createInfoColumn(
				createInfoLabel("graph_results_no_samples", noSamplesField), // $NON-NLS-1$
				noSamplesField,
				createInfoLabel("graph_results_deviation", deviationField),
				deviationField)); // $NON-NLS-1$
		graphInfoPanel.add(Box.createHorizontalGlue());

		graphInfoPanel.add(createInfoColumn(
				createInfoLabel("graph_results_latest_sample", dataField),
				dataField, // $NON-NLS-1$
				createInfoLabel("graph_results_throughput", throughputField),
				throughputField)); // $NON-NLS-1$
		graphInfoPanel.add(Box.createHorizontalGlue());

		graphInfoPanel.add(createInfoColumn(
				createInfoLabel("graph_results_average", averageField),
				averageField, // $NON-NLS-1$
				createInfoLabel("graph_results_median", medianField),
				medianField)); // $NON-NLS-1$
		graphInfoPanel.add(Box.createHorizontalGlue());
		return graphInfoPanel;
	}

	/**
	 * Creates one of the fields used to display the graph's current values.
	 * 
	 * @param color
	 *            the color used to draw the value. By convention this is the
	 *            same color that is used to draw the graph for this value and
	 *            in the choose panel.
	 * @param length
	 *            the number of digits which the field should be able to display
	 * 
	 * @return a text field configured to display one of the current graph
	 *         values
	 */
	private JTextField createInfoField(Color color, int length) {
		JTextField field = new JTextField(length);
		field.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		field.setEditable(false);
		field.setForeground(color);

		field.setBackground(getBackground());

		// The text field should expand horizontally, but have
		// a fixed height
		field.setMaximumSize(new Dimension(field.getMaximumSize().width, field
				.getPreferredSize().height));
		return field;
	}

	/**
	 * Creates a label for one of the fields used to display the graph's current
	 * values. Neither the label created by this method or the
	 * <code>field</code> passed as a parameter is added to the GUI here.
	 * 
	 * @param labelResourceName
	 *            the name of the label resource. This is used to look up the
	 *            label text using {@link JMeterUtils#getResString(String)}.
	 * @param field
	 *            the field this label is being created for.
	 * @return the j label
	 */
	private JLabel createInfoLabel(String labelResourceName, JTextField field) {
		JLabel label = new JLabel(JMeterUtils.getResString(labelResourceName));
		label.setForeground(field.getForeground());
		label.setLabelFor(field);
		return label;
	}

	/**
	 * Creates a panel containing two pairs of labels and fields for displaying
	 * the current graph values. This method exists to help with laying out the
	 * fields in columns. If one or more components are null then these
	 * components will be represented by blank space.
	 * 
	 * @param label1
	 *            the label for the first field. This label will be placed in
	 *            the upper left section of the panel. If this parameter is
	 *            null, this section of the panel will be left blank.
	 * @param field1
	 *            the field corresponding to the first label. This field will be
	 *            placed in the upper right section of the panel. If this
	 *            parameter is null, this section of the panel will be left
	 *            blank.
	 * @param label2
	 *            the label for the second field. This label will be placed in
	 *            the lower left section of the panel. If this parameter is
	 *            null, this section of the panel will be left blank.
	 * @param field2
	 *            the field corresponding to the second label. This field will
	 *            be placed in the lower right section of the panel. If this
	 *            parameter is null, this section of the panel will be left
	 *            blank.
	 * @return the box
	 */
	private Box createInfoColumn(JLabel label1, JTextField field1,
			JLabel label2, JTextField field2) {
		// This column actually consists of a row with two sub-columns
		// The first column contains the labels, and the second
		// column contains the fields.
		Box row = Box.createHorizontalBox();
		Box col = Box.createVerticalBox();
		col.add(label1 != null ? label1 : Box.createVerticalGlue());
		col.add(label2 != null ? label2 : Box.createVerticalGlue());
		row.add(col);

		row.add(Box.createHorizontalStrut(5));

		col = Box.createVerticalBox();
		col.add(field1 != null ? field1 : Box.createVerticalGlue());
		col.add(field2 != null ? field2 : Box.createVerticalGlue());
		row.add(col);

		row.add(Box.createHorizontalStrut(5));

		return row;
	}

	/**
	 * Creates a window with a folder selector.
	 * 
	 * @return a window with a JFileChooser
	 */
	public class MySelectFolder implements ActionListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent e) {
			JFileChooser folderChooser = new JFileChooser();
			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			folderChooser.setAcceptAllFileFilterUsed(false);

			if (folderChooser.showOpenDialog(folderChooser) == JFileChooser.APPROVE_OPTION) {
				setDestinationFolder(folderChooser.getSelectedFile()
						.getAbsolutePath());
				try {
					// LogFile creation
					setLogFileName("log.txt");
					// setResultFileName("DAG" + testId + ".xml");

					logFile = new File(getDestinationFolder() + System.getProperty("file.separator")
							+ getLogFileName());
					// resultFile = new File(getDestinationFolder() + System.getProperty("file.separator")
					// + getResultFileName());
					if (!logFile.exists()) {
						FileWriter writerLog2 = new FileWriter(logFile);
						// FileWriter writerResult = new FileWriter(resultFile);

						writerLog2
								.append("#Internal use - DO NOT DELETE OR MODIFY#\n");
						writerLog2.close();

						// writerResult.close();
					}

				} catch (IOException ex) {
					ex.printStackTrace();
				}

				System.out
						.println("destinationFolder (MySelectFolder actionPerformed):"
								+ destinationFolder);
				labelFolder.setText("Destination folder: " + destinationFolder);
			} else {
				System.out.println("No Selection ");
			}
		}
	}
}
