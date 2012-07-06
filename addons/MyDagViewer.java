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
import java.util.Calendar;
import java.awt.Dimension;
import java.awt.event.*;

import java.io.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import java.util.Arrays;
import java.util.ArrayList;

import org.apache.jmeter.gui.util.JMeterColor;
import org.apache.jmeter.samplers.Clearable;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jmeter.visualizers.gui.AbstractVisualizer;
import org.apache.jmeter.visualizers.*;

/**
 * Creates a window with a folder selector.
 *
 * @return a window with a JFileChooser
 */
public class MyDagViewer extends JFrame implements ActionListener {

	/** The dag viewer frame. */
	public JFrame dagViewerFrame; // Frame used to display the DAGs from the
	
	/** The dag viewer layout. */
	private BoxLayout dagViewerLayout;

	/** The man labels button. */
	private JButton manLabelsButton;
	
	/** The buid xml button. */
	private JButton buidXmlButton;

	/** The X paths list label. */
	private JLabel XPathsListLabel;

	/**
	 * My dag viewer.
	 */
	public void MyDagViewer() {
		//Nothing here...
		//... nothing there :P
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		dagViewerFrame = new JFrame();

		dagViewerFrame.setSize(dagViewerFrame.getMaximumSize());
		dagViewerFrame.setVisible(true);
		dagViewerFrame.setTitle("Test plan DAG viewer");

		dagViewerFrame.setLayout(new BorderLayout());

	}

}