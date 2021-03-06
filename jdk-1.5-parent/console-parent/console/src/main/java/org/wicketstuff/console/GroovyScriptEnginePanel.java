/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wicketstuff.console;

import org.apache.wicket.model.IModel;
import org.wicketstuff.console.engine.Lang;

/**
 * Main panel to execute Groovy scripts.
 * 
 * @author cretzel
 */
public class GroovyScriptEnginePanel extends ScriptEnginePanel {

	private static final long serialVersionUID = 1L;

	public GroovyScriptEnginePanel(final String wicketId) {
		this(wicketId, null);
	}

	public GroovyScriptEnginePanel(final String id, final IModel<String> title) {
		super(id, Lang.GROOVY, title);
		init();
	}

	protected void init() {
		setInput("println application\n" + "println page\n"
				+ "println component\n");
	}

}
