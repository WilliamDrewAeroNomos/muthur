/**
 * 
 */
package com.csc.muthur.server.commons.exception;

import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class MuthurException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2227812881327825304L;

	/**
	 * 
	 * @author wdrew
	 * 
	 */
	public enum Cause {
		INVALID_PARMETER(
				"Invalid parameter");

		private String text;

		Cause(final String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

	}

	private IEvent event;

	/**
	 * 
	 */
	public MuthurException() {
	}

	/**
	 * @param message
	 */
	public MuthurException(String message) {
		super(message);
	}

	/**
	 * @param message
	 */
	public MuthurException(String message, IEvent event) {
		super(message);
		this.setEvent(event);
	}

	/**
	 * @param cause
	 */
	public MuthurException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public MuthurException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @return the event
	 */
	public IEvent getEvent() {
		return event;
	}

	/**
	 * @param event
	 *          the event to set
	 */
	public void setEvent(IEvent event) {
		this.event = event;
	}

}
