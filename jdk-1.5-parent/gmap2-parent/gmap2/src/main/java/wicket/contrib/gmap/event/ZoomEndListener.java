/*
 * $Id: org.eclipse.jdt.ui.prefs 5004 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) eelco12 $
 * $Revision: 5004 $
 * $Date: 2006-03-17 20:47:08 -0800 (Fri, 17 Mar 2006) $
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
package wicket.contrib.gmap.event;

import org.apache.wicket.Request;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * See "zoomend" in the event section of <a
 * href="http://www.google.com/apis/maps/documentation/reference.html#GMap2">GMap2</a>.
 */
public abstract class ZoomEndListener extends GEventListenerBehavior {
	@Override
	protected String getEvent() {
		return "zoomend";
	}

	@Override
	protected void onEvent(AjaxRequestTarget target) {
		Request request = RequestCycle.get().getRequest();
		int oldLevel = 0;
		int newLevel = 0;
		String oldZoomLevelParameter = request.getParameter("argument0");
		String newZoomLevelParameter = request.getParameter("argument1");
		if (oldZoomLevelParameter == null || newZoomLevelParameter == null) {
			return;
		}
		oldLevel = Integer.parseInt(oldZoomLevelParameter);
		newLevel = Integer.parseInt(newZoomLevelParameter);
		onZoomEnd(target, oldLevel, newLevel);
	}

	/**
	 * Override this method to provide handling of a zoomEnd.<br>
	 * You can get the new center coordinates of the map by calling
	 * {@link GMap2#getCenter()}.
	 * 
	 * @param target
	 *            the target that initiated the move
	 */
	protected abstract void onZoomEnd(AjaxRequestTarget target, int oldLevel,
			int newLevel);

}