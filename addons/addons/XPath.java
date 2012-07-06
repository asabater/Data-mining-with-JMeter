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

/**
 * Part of the Package addons
 */
package addons;

import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 * The Class XPath.
 * 
 * @author agustinsabaterpineiro
 */
public class XPath implements Query {
	
	/** The list model. */
	public static DefaultListModel listModel;
	
	/** The X path list. */
	public static JList XPathList;

	/**
	 * Instantiates a new x path.
	 */
	public XPath(){
		//Nothing here
	}
	
	/**
	 * Gets the query list.
	 * 
	 * @return the query list
	 */
	public String[] getQueryList() {

		String[] tempTL = new String[10];
		int size = listModel.getSize();
		for (int i = 1; i < (size - 1); i++) {
			tempTL[i] = listModel.getElementAt(i).toString();
		}

		return tempTL;
	}

	/**
	 * Adds XPaths elements to staticXPathList.
	 * 
	 * @param s
	 *            the s
	 */
	public static void addQuery(String s) {
		System.out.println("ManageXPaths - addQuery - String s:" + s
				+ " - length: " + s.length());
		listModel.addElement(s);
		System.out.println("listModel: " + listModel.toString());
	}

	/**
	 * Adds all the strings into ListXPaths.
	 * 
	 * @param s
	 *            the s
	 */
	public void addQuerys(String[] s) {
		System.out.println("ManageXPaths - addQuerys - String[] s:"
				+ s.toString() + " - length: " + s.length);
		for (int i = 0; i < s.length; i++) {
			addQuery(s[i]);
		}
	}

	/**
	 * Clear all XPaths from staticXPathList.
	 * 
	 * @return string into position i of the static staticXPathList
	 */
	public void clearQuerys() {
		System.out.println("ManageXPaths - clearQuerys");
		String[] t = getStaticsQuerys();
		XPathList.setListData(t);
	}

	/**
	 * Returns XPath in position i.
	 * 
	 * @param i
	 *            the i
	 * @return string
	 */
	public static String getStaticsQuery(int i) {
		System.out.println("ManageXPaths - getStaticsQuery");
		return listModel.get(i).toString();
	}

	/**
	 * Gets all the XPaths in staticXPathList.
	 * 
	 * @return Array
	 */
	public String[] getStaticsQuerys() {
		System.out.println("ManageXPaths - getStaticsQuerys");
		String[] s = new String[listModel.size()];
		for (int i = 0; i < listModel.size(); i++) {
			s[i] = listModel.get(i).toString();
		}

		System.out.println("getStaticsQuerys" + s);
		return s;
	}

	/**
	 * Gets all the XPaths in staticXPathList in arrayList.
	 * 
	 * @return Array
	 */
	public DefaultListModel getStaticsQuerysLM() {
		System.out.println("ManageXPaths - getStaticsQuerys 2");

		return listModel;
	}

	/**
	 * Gets all the XPaths in staticXPathList in arrayList.
	 * 
	 * @return Array
	 */
	public static Integer getCountQuerys() {
		System.out.println("ManageXPaths - getCountQuerys . listmodel: "
				+ listModel.toString());

		return listModel.getSize();
	}

	/**
	 * Saves an array of Strings into static staticXPathList.
	 * 
	 */
	public void saveHtmlQuerys() {
		System.out.println("ManageXPaths - saveHtmlQuerys");
		addQuerys(getStaticsQuerys());
	}
}
