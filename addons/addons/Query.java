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

/*
 * Part of package addons
 */
package addons;

import javax.swing.DefaultListModel;

/**
 * The Interface Query.
 */
interface Query {

	/**
	 * Gets the query list.
	 *
	 * @return the query list
	 */
	public String[] getQueryList();

	// public static void addQuery(String s);

	/**
	 * Adds the querys.
	 *
	 * @param s the s
	 */
	public void addQuerys(String[] s);

	/**
	 * Clear querys.
	 */
	public void clearQuerys();

	// public static String getStaticsQuery(int i);

	/**
	 * Gets the statics querys.
	 *
	 * @return the statics querys
	 */
	public String[] getStaticsQuerys();

	/**
	 * Gets the statics querys lm.
	 *
	 * @return the statics querys lm
	 */
	public DefaultListModel getStaticsQuerysLM();

	// public static Integer getCountQuerys();

	/**
	 * Save html querys.
	 */
	public void saveHtmlQuerys();
}
