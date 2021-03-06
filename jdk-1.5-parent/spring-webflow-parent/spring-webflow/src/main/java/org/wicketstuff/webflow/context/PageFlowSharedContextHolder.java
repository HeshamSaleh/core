/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributed by United Services Automotive Association (USAA)
 */
package org.wicketstuff.webflow.context;

/**
 * Simple holder class that contains the page flow context information shared by wicket and webflow in the current thread.
 *
 * @author Clint Checketts, Florian Braun, Doug Hall, Subramanian Murali
 * @version $Id: $
 */
public final class PageFlowSharedContextHolder 
{
	private static ThreadLocal<PageFlowSharedContext> sharedContextHolder = new ThreadLocal<PageFlowSharedContext>();

	/**
	 * Associate the given shared context with the current thread.
	 *
	 * @param sharedContext - the shared context that needs to be set in the thread local.
	 */
	public static void setSharedContext(PageFlowSharedContext sharedContext) 
	{
		sharedContextHolder.set(sharedContext);
	}

	/**
	 * Return the shared context associated with the current thread.
	 *
	 * @return PageFlowSharedContext - the shared context associated with the current thread.
	 */
	public static PageFlowSharedContext getSharedContext() {
		return (PageFlowSharedContext) sharedContextHolder.get();
	}

	// not instantiable
	private PageFlowSharedContextHolder()
	{
	}
	
	/**
	 * Method that initializes an empty shared context in the current thread.
	 */
	public static void initializeSharedContext()
	{
		setSharedContext(new PageFlowSharedContext());
	}
	
	/**
	 * Method that clears the shared context in the current thread.
	 */
	public static void clearSharedContext()
	{
		setSharedContext(null);
	}	
}
