/*
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.wicketstuff.datatable_autocomplete.behaviour;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.Request;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.datatable_autocomplete.panel.AbstractAutoCompleteDependencyProcessor;
import org.wicketstuff.datatable_autocomplete.panel.AutoCompletingPanel;
import org.wicketstuff.datatable_autocomplete.panel.AJAXAutoCompleteBehavior;

/**
 * @author mocleiri
 * 
 */
public class AutoCompletingBehavior extends AJAXAutoCompleteBehavior {

	/**
	 * 
	 */
	private static final long		serialVersionUID	= 7722548233439995560L;
	private static final Logger		log					= LoggerFactory
																.getLogger(AutoCompletingBehavior.class);
	private TextField<String>				textModel;
	private final HiddenField<?>	selectedContextField;

	
	public interface IAutoCompletingResponseValidator extends IClusterable {

		/**
		 * 
		 * @param value
		 * @param target
		 * @return true if the value is valid. false and the error will be handled otherwise.
		 */
		boolean validate(String value, AjaxRequestTarget target);
		
		
	};
	
	private class AutoCompletingDependencyProcessor extends
			AbstractAutoCompleteDependencyProcessor {

		/**
		 * 
		 */
		private static final long				serialVersionUID	= 5497206492287083574L;
		private final AutoCompletingPanel<?>	autoCompletingPanel;

		private final String					callbackName;
		private final IAutoCompletingResponseValidator validator;

		/**
		 * @param names
		 * @param components
		 * @param duration
		 */
		public AutoCompletingDependencyProcessor(String callbackName,
				TextField<?> textField,
				AutoCompletingPanel<?> autoCompletingPanel, Duration duration, IAutoCompletingResponseValidator validator) {

			super(new String[] { callbackName }, new Component[] { textField },
					duration);
			this.callbackName = callbackName;
			this.autoCompletingPanel = autoCompletingPanel;
			this.validator = validator;
		}

		
		public void onAjaxUpdate(Request request, AjaxRequestTarget target) {

			String value = request.getParameter(callbackName);
			
			textModel.setModelObject(value);

			// enable since this is an ajax update
			autoCompletingPanel.setInitialRenderDisabledMode(false);

			// will be revaluated during rendering
			autoCompletingPanel.setVisible(true);

			target.addComponent(autoCompletingPanel);

		}


		/* (non-Javadoc)
		 * @see org.wicketstuff.datatable_autocomplete.panel.AutoCompleteDependencyProcessor#validate(org.apache.wicket.Request, org.apache.wicket.ajax.AjaxRequestTarget)
		 */
		public boolean validate(Request request, AjaxRequestTarget target) {
			
			String value = request.getParameter(callbackName);
			
			return validator.validate (value, target);
		}
		
		

	};

	/**
	 * A special behaviour to generate a GET request for changes to the
	 * textComponent provided.
	 * 
	 * @param selectedContextField
	 *            the field where the id value of the selected row is placed
	 * @param textComponent
	 *            the component whose value is updated on the request (this
	 *            behavoir should be added to textComponent).
	 * @param referenceAutoCompletingPanel
	 *            the completing panel that needs to be updated when the
	 *            textfield value changes.
	 * @param milisecondDurationBetweenRequests
	 *            the number of mmiliseconds that must elapse before a
	 *            subsequent get request will be sent from the client.
	 */
	public AutoCompletingBehavior(HiddenField<?> selectedContextField,
			TextField<String> textComponent,
			AutoCompletingPanel<?> referenceAutoCompletingPanel,
			long milisecondDurationBetweenRequests, IAutoCompletingResponseValidator validator) {

		super("onkeyup");
		this.selectedContextField = selectedContextField;

		this.textModel = textComponent;

		super.setDependencyProcessor(new AutoCompletingDependencyProcessor(
				"value", textComponent, referenceAutoCompletingPanel, Duration
						.milliseconds(milisecondDurationBetweenRequests), validator));

	}

	
	public AutoCompletingBehavior(TextField<String> textComponent,
			AutoCompletingPanel<?> referenceAutoCompletingPanel,
			long milisecondDurationBetweenRequests, IAutoCompletingResponseValidator validator) {

		this(null, textComponent, referenceAutoCompletingPanel,
				milisecondDurationBetweenRequests, validator);

	}

	@Override
	protected void addAdditionalJavaScript(List<String> eventScripts) {

		if (selectedContextField != null) {
			// add in the clear script to be the first part of the element.
			eventScripts.add(0, "Wicket.$("
					+ selectedContextField.getMarkupId() + ").value='CLEAR'");
		}

	}

}
