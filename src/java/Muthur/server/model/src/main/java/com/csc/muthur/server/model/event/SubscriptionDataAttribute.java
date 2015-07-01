/**
 * 
 */
package com.csc.muthur.server.model.event;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class SubscriptionDataAttribute {
	
	private String name;
	private String value;
	private int type;
	/**
	 * @param name
	 * @param value
	 * @param type
	 */
	public SubscriptionDataAttribute(String name, String value, int type) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
	}
	/**
	 * @return the name
	 */
	public final String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public final void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public final String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public final void setValue(String value) {
		this.value = value;
	}
	/**
	 * @return the type
	 */
	public final int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public final void setType(int type) {
		this.type = type;
	}
	
	

}
