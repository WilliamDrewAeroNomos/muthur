/**
 * 
 */
package com.csc.muthur.server.model;

import java.util.List;
import java.util.Vector;

import com.csc.muthur.server.model.event.SubscriptionDataAttribute;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DataSubscription {

	private String className;

	private List<SubscriptionDataAttribute> attributes = new Vector<SubscriptionDataAttribute>();

	/**
	 * 
	 * @param className
	 */
	public DataSubscription(final String className) {
		this.className = className;
	}

	/**
	 * 
	 * @param attr
	 */
	public void addAttribute(final SubscriptionDataAttribute attr) {
		if ((attributes != null) && (attr != null)) {
			attributes.add(attr);
		}
	}

	/**
	 * @return the className
	 */
	public final String getClassName() {
		return className;
	}

	/**
	 * @param className
	 *          the className to set
	 */
	public final void setClassName(String className) {
		this.className = className;
	}

	/**
	 * @return the attributes
	 */
	public final List<SubscriptionDataAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *          the attributes to set
	 */
	public final void setAttributes(List<SubscriptionDataAttribute> attributes) {
		this.attributes = attributes;
	}

}
