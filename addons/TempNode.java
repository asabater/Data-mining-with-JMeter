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

import java.util.StringTokenizer;
import java.util.ArrayList;
//import java.security.MessageDigest;

import org.w3c.dom.CDATASection;

// TODO: Auto-generated Javadoc
/**
 * The Class TempNode.
 *
 * @description TempNode is used to declare temporal nodes used at execution
 * @author agustinsabaterpineiro
 * 
 * Created December 28, 2011
 */
public class TempNode {

	/** The id. */
	private Integer id; // ID formed by the hash of the source
	
	/** The father id. */
	private Integer fatherId; // Obvious
	
	/** The opener tag. */
	private String openerTag; // Something ike "<div class='post-body')"
	
	/** The content. */
	private String content; // All the content (without enclosing tags)
	
	/** The fathers. */
	private ArrayList<Integer> fathers; // HTMLs file where it was found
	
	/** The sons. */
	private ArrayList<Integer> sons; // HTMLs nodes found into the node
	// private String node; // All the node (with enclosing tags)
	/** The size. */
	private Integer size = 0; // Number of chars
	
	/** The start column number. */
	private Integer startColumnNumber = 0;
	
	/** The start line number. */
	private Integer startLineNumber = 0;
	
	/** The end column number. */
	private Integer endColumnNumber = 0;
	
	/** The end line number. */
	private Integer endLineNumber = 0;

	/** The web. */
	private String web; // URL from html file
	
	/** The date. */
	private String date = "";

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/** The node counter. */
	private static Integer nodeCounter = 0;

	/**
	 * Constructor for the TempNode object.
	 *
	 * @param src the src
	 */
	public TempNode(String src) {
		System.out.println("TempNode - TempNode()");
		init(src);
	}

	/**
	 * Inits the.
	 *
	 * @param src the src
	 */
	public void init(String src) {

		try {
			this.id = src.hashCode();
			this.setContent(src);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * Gets the father id.
	 *
	 * @return the father id
	 */
	public Integer getFatherId() {
		return this.fatherId;
	}

	/**
	 * Sets the father id.
	 *
	 * @param x the new father id
	 */
	public void setFatherId(String x) {
		try {
			this.fatherId = x.hashCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the father id.
	 *
	 * @param x the new father id
	 */
	public void setFatherId(Integer x) {
		try {
			this.fatherId = x;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the father id.
	 */
	public void setFatherId() {
		this.fatherId = 0;
	}

	/**
	 * Gets the start column number.
	 *
	 * @return the startColumnNumber
	 */
	public Integer getStartColumnNumber() {
		return startColumnNumber;
	}

	/**
	 * Sets the start column number.
	 *
	 * @param startColumnNumber the startColumnNumber to set
	 */
	public void setStartColumnNumber(Integer startColumnNumber) {
		this.startColumnNumber = startColumnNumber;
	}

	/**
	 * Gets the start line number.
	 *
	 * @return the startLineNumber
	 */
	public Integer getStartLineNumber() {
		return startLineNumber;
	}

	/**
	 * Sets the start line number.
	 *
	 * @param startLineNumber the startLineNumber to set
	 */
	public void setStartLineNumber(Integer startLineNumber) {
		this.startLineNumber = startLineNumber;
	}

	/**
	 * Gets the end column number.
	 *
	 * @return the endColumnNumber
	 */
	public Integer getEndColumnNumber() {
		return endColumnNumber;
	}

	/**
	 * Sets the end column number.
	 *
	 * @param endColumnNumber the endColumnNumber to set
	 */
	public void setEndColumnNumber(Integer endColumnNumber) {
		this.endColumnNumber = endColumnNumber;
	}

	/**
	 * Gets the end line number.
	 *
	 * @return the endLineNumber
	 */
	public Integer getEndLineNumber() {
		return endLineNumber;
	}

	/**
	 * Sets the end line number.
	 *
	 * @param endLineNumber the endLineNumber to set
	 */
	public void setEndLineNumber(Integer endLineNumber) {
		this.endLineNumber = endLineNumber;
	}

	/**
	 * Sets the opener tag.
	 *
	 * @param ot the ot
	 * @return true, if successful
	 */
	public boolean setOpenerTag(String ot) {

		try {
			this.openerTag = ot;
			return true;
		} catch (Exception e) {
			System.out.println("Exception found at setOpenerTag ot:" + ot);
			return false;
		}
	}

	/**
	 * Sets the size.
	 *
	 * @param s the s
	 * @return true, if successful
	 */
	public boolean setSize(Integer s) {

		try {
			this.size = s;
			return true;
		} catch (Exception e) {
			System.out.println("Exception found at setSize st:" + s);
			return false;
		}
	}

	/**
	 * Sets the web.
	 *
	 * @param web the web
	 * @return true, if successful
	 */
	public boolean setWeb(String web) {

		try {
			this.web = web;
			return true;
		} catch (Exception e) {
			System.out.println("Exception found at setSize st:" + web);
			return false;
		}
	}

	/**
	 * Gets the web.
	 *
	 * @return the web
	 */
	public String getWeb() {
		return this.web;
	}

	/*
	 * set Content and also trim openerTag and sets size NOTE: If you are an
	 * objected developing religious go home!
	 */
	// public boolean setContent(String cont, String node, String pureContent) {
	/**
	 * Sets the content.
	 *
	 * @param cont the cont
	 * @return true, if successful
	 */
	public boolean setContent(String cont) {
		try {
			this.content = cont;
			// this.node = node;

			StringTokenizer openerTag = new StringTokenizer(cont, ">");
			String ot = openerTag.nextToken();
			this.setOpenerTag(ot + ">");
			this.setSize(cont.length());

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return this.content;
	}

	/**
	 * Gets the opener tag.
	 *
	 * @return the opener tag
	 */
	public String getOpenerTag() {
		return this.openerTag;
	}

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public Integer getSize() {
		return this.size;
	}

	/**
	 * Gets the node counter.
	 *
	 * @return the node counter
	 */
	public static Integer getNodeCounter() {
		return TempNode.nodeCounter;
	}

}
