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
package org.apache.jmeter.extractor.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.apache.jmeter.extractor.XPathExtractor;
import org.apache.jmeter.assertions.gui.XMLConfPanel;
import org.apache.jmeter.extractor.XPathExtractor;
import org.apache.jmeter.processor.gui.AbstractPostProcessorGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.GraphVisualizer;
import org.apache.jorphan.gui.JLabeledTextField;

import addons.ManageXPaths;


/**
 * GUI for XPathExtractor class.
 */
/*
 * This file is inspired by RegexExtractor. See Bugzilla: 37183
 */
public class XPathExtractorGui extends AbstractPostProcessorGui {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 240L;

	/** The log file name. */
	public static String logFileName;
	
	/** The destination folder. */
	public static String destinationFolder;

	/** The list scroller. */
	private JScrollPane listScroller;
	
	/** The X path list. */
	private JList XPathList;
	
	/** The select folder. */
	private JButton selectFolder;
	
	/** The man xpaths button. */
	private JButton manXpathsButton;
	
	/** The build xml. */
	private JButton buildXml;
	
	/** The labelselect folder. */
	private JLabel labelselectFolder;
	
	/** The xpaths count. */
	public static JLabel xpathsCount;
	
	/** The list model. */
	private DefaultListModel listModel;

	/** The panel xpaths. */
	public JPanel panelXpaths;

	/** The default field. */
	private final JLabeledTextField defaultField = new JLabeledTextField(
			JMeterUtils.getResString("default_value_field"));//$NON-NLS-1$

	/** The xpath query field. */
	private final JLabeledTextField xpathQueryField = new JLabeledTextField(
			JMeterUtils.getResString("xpath_extractor_query"));//$NON-NLS-1$

	/** The ref name field. */
	private final JLabeledTextField refNameField = new JLabeledTextField(
			JMeterUtils.getResString("ref_name_field"));//$NON-NLS-1$

	// Should we return fragment as text, rather than text of fragment?
	/** The get fragment. */
	private final JCheckBox getFragment = new JCheckBox(
			JMeterUtils.getResString("xpath_extractor_fragment"));//$NON-NLS-1$

	/** The xml. */
	private final XMLConfPanel xml = new XMLConfPanel();

	/* getStaticLabel hacked to show XPath Model Analyzer */
	/**
	 * Gets the static label.
	 *
	 * @return the static label
	 */
	public String getStaticLabel() {
		return "Query Model Analyzer";
	}

	/**
	 * Gets the label resource.
	 *
	 * @return the label resource
	 */
	public String getLabelResource() {
		return "xpath_extractor_title"; //$NON-NLS-1$
	}

	/**
	 * Instantiates a new x path extractor gui.
	 */
	public XPathExtractorGui() {
		super();
		init();
	}

	/**
	 * Configure.
	 *
	 * @param el the el
	 */
	@Override
	public void configure(TestElement el) {
		super.configure(el);
		XPathExtractor xpe = (XPathExtractor) el;
		showScopeSettings(xpe, true);
		xpathQueryField.setText(xpe.getXPathQuery());
		defaultField.setText(xpe.getDefaultValue());
		refNameField.setText(xpe.getRefName());
		getFragment.setSelected(xpe.getFragment());
		xml.configure(xpe);
	}

	/**
	 * Creates the test element.
	 *
	 * @return the test element
	 */
	public TestElement createTestElement() {
		XPathExtractor extractor = new XPathExtractor();
		modifyTestElement(extractor);
		return extractor;
	}

	/**
	 * Modify test element.
	 *
	 * @param extractor the extractor
	 */
	public void modifyTestElement(TestElement extractor) {
		super.configureTestElement(extractor);
		if (extractor instanceof XPathExtractor) {
			XPathExtractor xpath = (XPathExtractor) extractor;
			saveScopeSettings(xpath);
			xpath.setDefaultValue(defaultField.getText());
			xpath.setRefName(refNameField.getText());
			xpath.setXPathQuery(xpathQueryField.getText());
			xpath.setFragment(getFragment.isSelected());
			xml.modifyTestElement(xpath);
		}
	}

	/**
	 * Implements JMeterGUIComponent.clearGui
	 */
	@Override
	public void clearGui() {
		super.clearGui();

		xpathQueryField.setText(""); // $NON-NLS-1$
		defaultField.setText(""); // $NON-NLS-1$
		refNameField.setText(""); // $NON-NLS-1$
		xml.setDefaultValues();
	}

	/**
	 * Inits the XPathExtractorGui
	 */
	private void init() {
		setLayout(new BorderLayout());
		setBorder(makeBorder());

		Box box = Box.createVerticalBox();
		box.add(makeTitlePanel()); // Untouchable
		box.add(createScopePanel(true));
		xml.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(),
				JMeterUtils.getResString("xpath_assertion_option"))); //$NON-NLS-1$

		/* Commented instructions to clean up Xpath Model Analyzer plugins */
		// box.add(xml);
		// box.add(getFragment);
		box.add(makeParameterPanel());

		box.add(makeOptionParameterPanel());
		// box.add(makeSearchTypePanel());
		box.add(makeXPathsPanel());
		box.add(makeXmlPanel());
		add(box, BorderLayout.NORTH);
	}

	/**
	 * Make parameter panel.
	 *
	 * @return the j panel
	 */
	private JPanel makeParameterPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		initConstraints(gbc);
		addField(panel, refNameField, gbc);
		resetContraints(gbc);
		addField(panel, xpathQueryField, gbc);
		resetContraints(gbc);
		gbc.weighty = 1;
		addField(panel, defaultField, gbc);
		return panel;
	}

	/**
	 * Make option parameter panel.
	 *
	 * @return the j panel
	 */
	private JPanel makeOptionParameterPanel() {
		// int align = FlowLayout.LEFT; // or LEFT, RIGHT
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel(new GridBagLayout());

		TitledBorder title;
		title = BorderFactory.createTitledBorder("Settings");
		panel.setBorder(title);

		selectFolder = new JButton("Select destination");
		labelselectFolder = new JLabel("HTML Downloader's Source:");

		MySelectFolder msf = new MySelectFolder();
		selectFolder.addActionListener(msf);

		panel.add(selectFolder, BorderLayout.WEST);
		panel.add(labelselectFolder, BorderLayout.CENTER);

		// Create the radio buttons.
		JRadioButton xp_rb = new JRadioButton("XPaths (default)");
		xp_rb.setMnemonic(KeyEvent.VK_X);
		xp_rb.setActionCommand("XPaths (default)");
		xp_rb.setSelected(true);
		xp_rb.addActionListener(xpaths());

		JRadioButton otherButton = new JRadioButton("Other (coming soon)");
		otherButton.setEnabled(false);
		otherButton.setMnemonic(KeyEvent.VK_C);
		otherButton.setActionCommand("Other (coming soon)");
		otherButton.addActionListener(other());

		// Group the radio buttons.
		ButtonGroup group = new ButtonGroup();
		group.add(xp_rb);
		group.add(otherButton);

		TitledBorder title2;
		title2 = BorderFactory.createTitledBorder("Search type: ");
		panel2.setBorder(title2);
		panel2.add(xp_rb);
		panel2.add(otherButton);

		// JLabel labelAlgorithm = new JLabel("Search type: ");
		// panel.add(labelAlgorithm);
		panel.add(panel2, BorderLayout.SOUTH);
		return panel;
	}

	/**
	 * Make xml panel.
	 *
	 * @return the j panel
	 */
	private JPanel makeXmlPanel() {
		int align = FlowLayout.LEFT; // or LEFT, RIGHT
		JPanel panel = new JPanel(new FlowLayout(align));

		buildXml = new JButton("Build XML file");
		buildXml.addActionListener(new BuildXml());
		panel.add(buildXml);

		return panel;
	}

	/**
	 * Adds the field.
	 *
	 * @param panel the panel
	 * @param field the field
	 * @param gbc the gbc
	 */
	private void addField(JPanel panel, JLabeledTextField field,
			GridBagConstraints gbc) {
		List<JComponent> item = field.getComponentList();
		panel.add(item.get(0), gbc.clone());
		gbc.gridx++;
		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(item.get(1), gbc.clone());
	}

	/**
	 * Reset contraints.
	 *
	 * @param gbc the gbc
	 */
	private void resetContraints(GridBagConstraints gbc) {
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.weightx = 0;
		gbc.fill = GridBagConstraints.NONE;
	}

	/**
	 * Inits the constraints.
	 *
	 * @param gbc the gbc
	 */
	private void initConstraints(GridBagConstraints gbc) {
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		gbc.weighty = 0;
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
	 * @param value the new destination folder
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
		return logFileName;
	}

	/**
	 * Sets the log file name.
	 *
	 * @param s the new log file name
	 */
	public static void setLogFileName(String s) {
		logFileName = s;
	}

	/**
	 * Gets the xpaths count.
	 *
	 * @return the xpaths count
	 */
	public static String getXpathsCount() {
		return xpathsCount.getText();
	}

	/**
	 * Sets the xpaths count.
	 *
	 * @param value the new xpaths count
	 */
	public static void setXpathsCount(String value) {
		xpathsCount.setText(value);
	}

	/**
	 * Make x paths panel.
	 *
	 * @return the j panel
	 */
	private JPanel makeXPathsPanel() {
		int align = FlowLayout.LEFT; // or LEFT, RIGHT
		panelXpaths = new JPanel(new FlowLayout(align));

		TitledBorder title;
		title = BorderFactory.createTitledBorder("XPaths settings");
		panelXpaths.setBorder(title);

		manXpathsButton = new JButton("Edit XPaths");
		manXpathsButton.addActionListener(new ManageXPaths());
		panelXpaths.add(manXpathsButton);

		// xpathsCount = new JLabel("No XPaths selected");
		// panel.add(xpathsCount);

		// MARK
		return panelXpaths;
	}

	/**
	 * Creates a window with a folder selector.
	 *
	 * @return a window with a JFileChooser
	 */

	public class MySelectFolder implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			Boolean found = false;
			JFileChooser folderChooser = new JFileChooser();
			folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			folderChooser.setAcceptAllFileFilterUsed(false);

			if (folderChooser.showOpenDialog(folderChooser) == JFileChooser.APPROVE_OPTION) {
				setDestinationFolder(folderChooser.getSelectedFile()
						.getAbsolutePath());
				File folder = new File(getDestinationFolder());

				if (folder.isDirectory()) {
					// obtenemos su contenido
					File[] files = folder.listFiles();

					String fileNameTemp;
					// y lo sacamos por pantalla
					for (File fTemp : files) {
						System.out.println(fTemp.getName());
						fileNameTemp = fTemp.getName();
						if (fileNameTemp.indexOf("log") != -1) {
							found = true;
						}
					}
					if (!found) {
						JOptionPane
								.showMessageDialog(null,
										"Folder selected has no HTML Downloader log file.");
					} else {
						labelselectFolder.setText("HTML Downloader's Folder: "
								+ getDestinationFolder());
					}
				}
				System.out
						.println("destinationFolder (MySelectFolder actionPerformed):"
								+ getDestinationFolder());

			} else {
				System.out.println("No Selection ");
			}
		}
	}

	/**
	 * Xpaths.
	 *
	 * @return the action listener
	 */
	private ActionListener xpaths() {

		return null;
	}

	/**
	 * Other.
	 *
	 * @return the action listener
	 */
	private ActionListener other() {

		return null;
	}

	/**
	 * Creates a window with a folder selector.
	 *
	 * @return a window with a JFileChooser
	 */

	public class BuildXml implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			Calendar cal = Calendar.getInstance();
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd.HH.mm.ss");

			String resultfilename = dateFormat.format(cal.getTime()) + ".xml";
			
			GraphVisualizer.setResultFileName(resultfilename);
			XPathExtractor xpe = new XPathExtractor();
			xpe.analyzeSamples();

		}
	}
}