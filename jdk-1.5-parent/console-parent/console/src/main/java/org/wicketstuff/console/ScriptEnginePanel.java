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

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Application;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxIndicatorAware;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.wicketstuff.console.engine.Engines;
import org.wicketstuff.console.engine.IScriptEngine;
import org.wicketstuff.console.engine.IScriptExecutionResult;
import org.wicketstuff.console.engine.Lang;

/**
 * Abstract panel for executing Scripts.
 * <p>
 * Usage:
 * 
 * <pre>
 * add(new GroovyScriptEnginePanel(&quot;scriptPanel&quot;));
 * </pre>
 * 
 * <pre>
 * &lt;div wicket:id=&quot;scriptPanel&quot&gt;&lt;/div&gt;
 * </pre>
 * 
 * @see ClojureScriptEnginePanel
 * @see GroovyScriptEnginePanel
 * 
 * @author cretzel
 */
public class ScriptEnginePanel extends Panel {

	private final class ClearButton extends Button {
		private static final long serialVersionUID = 1L;

		private ClearButton(final String id) {
			super(id);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();
			add(new AttributeAppender("onclick", Model.of("clearTextarea('"
					+ inputTf.getMarkupId() + "')"), ";"));

		}

	}

	final class SubmitButton extends AjaxButton implements IAjaxIndicatorAware {
		private static final long serialVersionUID = 1L;

		private SubmitButton(final String id, final Form<?> form) {
			super(id, form);
		}

		@Override
		protected void onSubmit(final AjaxRequestTarget target,
				final Form<?> form) {
			process(target);
		}

		@Override
		protected void onError(final AjaxRequestTarget target,
				final Form<?> form) {

		}

		public String getAjaxIndicatorMarkupId() {
			return indicator.getMarkupId();
		}
	}

	private static final long serialVersionUID = 1L;

	private static final ResourceReference CSS = new CompressedResourceReference(
			ScriptEnginePanel.class, "ScriptEnginePanel.css");

	private static final ResourceReference JS = new CompressedResourceReference(
			ScriptEnginePanel.class, "ScriptEnginePanel.js");

	private String input;
	private String output;
	private String returnValue;

	private Label title;
	private Form<Void> form;
	private TextArea<String> inputTf;
	private TextArea<String> outputTf;
	private TextField<String> returnValueTf;

	private Image indicator;

	private final IModel<String> titleModel;

	private final Lang lang;

	public ScriptEnginePanel(final String id, final Lang lang) {
		this(id, lang, null);
	}

	public ScriptEnginePanel(final String id, final Lang lang,
			final IModel<String> title) {
		super(id);
		this.lang = lang;
		this.titleModel = title != null ? title : Model.of("Wicket Console");

		init();
	}

	private void init() {
		setDefaultModel(new CompoundPropertyModel<ScriptEnginePanel>(this));

		initCss();
		initJs();

		initComponents();
	}

	private void initCss() {
		final ResourceReference css = getCSS();
		if (css != null) {
			add(CSSPackageResource.getHeaderContribution(css));
		}
	}

	private void initJs() {
		add(JavascriptPackageResource.getHeaderContribution(JS));
	}

	protected void initComponents() {
		title = new Label("title", titleModel);
		add(title);

		form = new Form<Void>("form");
		add(form);

		inputTf = new TextArea<String>("input");
		inputTf.setOutputMarkupId(true);
		form.add(inputTf);

		add(new SubmitButton("submit", form));
		add(new ClearButton("clear"));

		indicator = new Image("indicator",
				AbstractDefaultAjaxBehavior.INDICATOR);
		indicator.setOutputMarkupId(true);
		add(indicator);

		returnValueTf = new TextField<String>("returnValue");
		returnValueTf.setOutputMarkupId(true);
		add(returnValueTf);

		outputTf = new TextArea<String>("output");
		outputTf.setOutputMarkupId(true);
		add(outputTf);
	}

	protected ResourceReference getCSS() {
		return CSS;
	}

	protected void process(final AjaxRequestTarget target) {

		final IScriptEngine engine = newEngine();
		final Map<String, Object> bindings = newBindings();

		final IScriptExecutionResult result = engine.execute(input, bindings);

		if (result.isSuccess()) {
			returnValue = String.valueOf(result.getReturnValue());
			output = result.getOutput();
		} else {
			returnValue = null;
			output = String.format("%s\n\n%s", result.getOutput(),
					result.getException());
		}

		target.addComponent(returnValueTf);
		target.addComponent(outputTf);
	}

	protected IScriptEngine newEngine() {
		return Engines.create(lang);
	}

	protected Map<String, Object> newBindings() {
		final Map<String, Object> bindings = new HashMap<String, Object>();
		bindings.put("application", Application.get());
		bindings.put("page", getPage());
		bindings.put("component", this);
		return bindings;
	}

	public String getInput() {
		return input;
	}

	public void setInput(final String input) {
		this.input = input;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(final String output) {
		this.output = output;
	}

	public String getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(final String returnValue) {
		this.returnValue = returnValue;
	}

	public TextArea<String> getInputTf() {
		return inputTf;
	}

	public TextArea<String> getOutputTf() {
		return outputTf;
	}

	public TextField<String> getReturnValueTf() {
		return returnValueTf;
	}

	@Override
	public void detachModels() {
		super.detachModels();
		if (titleModel != null) {
			titleModel.detach();
		}
	}

	public static ScriptEnginePanel create(final String wicketId,
			final Lang lang, final IModel<String> title) {
		switch (lang) {
		case GROOVY:
			return new GroovyScriptEnginePanel(wicketId, title);
		case CLOJURE:
			return new ClojureScriptEnginePanel(wicketId, title);
		default:
			throw new UnsupportedOperationException("Unsupported language: "
					+ lang);
		}
	}
}