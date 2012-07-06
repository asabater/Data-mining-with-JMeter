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

package addons;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.List;

import java.io.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.apache.jmeter.gui.util.JMeterColor;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.Sample;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;

import org.apache.jmeter.extractor.gui.XPathExtractorGui;
import org.apache.jmeter.extractor.XPathExtractor;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.apache.jmeter.visualizers.GraphVisualizer;

import addons.*;
// TODO: Auto-generated Javadoc

/**
 * Creates a 500x500 pixel window for XPaths management.
 *
 * @return a window with a JFileChooser
 */
public class ManageXPaths implements ActionListener  {

	/** The x paths dialog. */
	public JDialog xPathsDialog;

	/** The write label. */
	private JLabel writeLabel;
	
	/** The new label code. */
	private JTextField newLabelCode = null;
	
	/** The X paths list label. */
	private JLabel XPathsListLabel;

	/** The add label button. */
	private JButton addLabelButton;
	
	/** The clear all button. */
	private JButton clearAllButton;
	
	/** The confirm button. */
	private JButton confirmButton;




	/** The xpaths. */
	private static List<String> xpaths = new ArrayList<String>();
	
	/** The num exec. */
	static int numExec = 0;

	/** The Constant webClient. */
	static final WebClient webClient = new WebClient();
	
	/** The current page. */
	private static HtmlPage currentPage;

	/** The list nodes. */
	private static ArrayList<TempNode> listNodes;

	/**
	 * Instantiates a new manage x paths.
	 */
	public ManageXPaths() {

	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {

		numExec++;
		System.out.println("ManageXPaths > actionPerformed -> numExec:"
				+ numExec);

		xPathsDialog = new JDialog();
		xPathsDialog.setModal(true);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenHeight = screenSize.height;
		int screenWidth = screenSize.width;

		xPathsDialog.setTitle("Manage XPaths");
		xPathsDialog.setSize(500, 500); // 500x500 (px)
		// At the center of the desktop
		xPathsDialog.setLocation((screenWidth / 2)
				- (xPathsDialog.getWidth() / 2), (screenHeight / 2)
				- (xPathsDialog.getHeight() / 2));
		xPathsDialog.add(createManageXPathsPanel());
		xPathsDialog.setVisible(true);

	}



	/**
	 * Creates a panel with some characteristics of XPaths.
	 *
	 * @return a panel allowing the user to choose which graphs to display
	 */
	public JPanel createManageXPathsPanel() {

		System.out.println("ManageXPaths > createManageXPathsPanel");
		JPanel manLabelPanel = new JPanel();
		manLabelPanel.setLayout(null);

		writeLabel = new JLabel("Write XPath");
		newLabelCode = new JTextField();

		addLabelButton = new JButton("ADD XPATH");
		clearAllButton = new JButton("CLEAR ALL");
		confirmButton = new JButton("CONFIRM LIST");	

		XPathsListLabel = new JLabel("Actual XPaths");

		if (numExec == 1) {
			XPath.listModel = new DefaultListModel();
		}

		XPath.XPathList = new JList(XPath.listModel);

		XPath.XPathList
				.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		XPath.XPathList.setLayoutOrientation(JList.VERTICAL);

		XPath.XPathList.setVisibleRowCount(10);

		JScrollPane listScroller = new JScrollPane(XPath.XPathList);
		listScroller.setPreferredSize(new Dimension(490, 300));

		addLabelButton.addActionListener(new ButtonListener());
		clearAllButton.addActionListener(new ButtonClearListener());
		confirmButton.addActionListener(new ButtonConfirmListener());

		manLabelPanel.add(writeLabel);
		manLabelPanel.add(newLabelCode);
		manLabelPanel.add(addLabelButton);
		manLabelPanel.add(clearAllButton);
		manLabelPanel.add(confirmButton);

		manLabelPanel.add(XPathsListLabel);
		manLabelPanel.add(XPath.XPathList);

		writeLabel.setBounds(10, 10, 485, 20);
		newLabelCode.setBounds(10, 30, 485, 25);
		addLabelButton.setBounds(10, 60, 220, 30);
		clearAllButton.setBounds(240, 60, 220, 30);
		XPathsListLabel.setBounds(10, 95, 485, 25);
		XPath.XPathList.setBounds(10, 125, 485, 300);
		confirmButton.setBounds(10, 430, 485, 25);

		manLabelPanel.setVisible(true);
		XPath.XPathList.repaint();

		return manLabelPanel;
	}

	/**
	 * The listener interface for receiving button events.
	 * The class that is interested in processing a button
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonListener<code> method. When
	 * the button event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonEvent
	 */
	protected class ButtonListener implements ActionListener {
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			System.out
					.println("JMeter downloader debug: Add XPath button pressed.");

			String s = newLabelCode.getText();
			if (s.isEmpty()) {
				// Empty XPaths forbidden
				JOptionPane
						.showMessageDialog(xPathsDialog,
								"Empty XPaths not allowed.\nPlease write a valid XPath.");
			} else {
				if (XPath.listModel.contains(s)) {
					JOptionPane
							.showMessageDialog(
									xPathsDialog,
									"XPath "
											+ s
											+ " already in the list.\nWrite a new XPath or SUBMIT.");
				} else {
					XPath.addQuery(s);
					newLabelCode.setText("");
				}
			}
		}
	}

	/**
	 * The listener interface for receiving buttonClear events.
	 * The class that is interested in processing a buttonClear
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonClearListener<code> method. When
	 * the buttonClear event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonClearEvent
	 */
	protected class ButtonClearListener implements ActionListener {
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {
			System.out
					.println("JMeter downloader debug: Clear XPaths button pressed.");
			XPath.listModel.clear();
			XPath.XPathList.repaint();
		}
	}

	/**
	 * The listener interface for receiving buttonConfirm events.
	 * The class that is interested in processing a buttonConfirm
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addButtonConfirmListener<code> method. When
	 * the buttonConfirm event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see ButtonConfirmEvent
	 */
	protected class ButtonConfirmListener extends XPathExtractorGui implements
			ActionListener {
		
		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		public void actionPerformed(ActionEvent e) {

			System.out
					.println("JMeter downloader debug: Confirm XPaths button pressed.");

			xPathsDialog.dispose();
		}
	}




}
