package com.csc.muthur.server.commons;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
import org.jdom.Element;

import com.csc.muthur.server.commons.exception.MuthurException;

public interface IEvent {

	public Element getDataBlockElement();

	public Element getErrorBlockElement();

	public String getEventTypeDescription();

	public void startOfElement(String currentElementName) throws MuthurException;

	public void endOfElement(String currentElementName);

	public String serialize();

	public void ingest(final String objectAsXML) throws MuthurException;

	public String getEventName();

	public EventTypeEnum getEventType();

	public void newElement(final String elementName, final String elementValue);

	public String getEventUUID();

	public long getExpirationTimeMilliSecs();

	public long getCreateTimeMilliSecs();

	public void initialization();

	public void initialization(final String objectAsXML) throws MuthurException;

	public void setHandler(final String handlerName);

	public String getHandler();

	public void setSourceOfEvent(String string);

	public String getSourceOfEvent();

	public boolean isSuccess();

	public String getErrorDescription();

	/**
	 * @return Returns the status.
	 */
	public String getStatus();

	/**
	 * @param status
	 *          The status to set.
	 */
	public void setStatus(String status);

	/**
	 * @param success
	 *          The success to set.
	 */
	public void setSuccess(boolean success);

	/**
	 * @param errorDescription
	 *          The errorDescription to set.
	 */
	public void setErrorDescription(String errorDescription);

	public abstract void setFederationExecutionHandle(String federationExecutionHandle);

	public abstract String getFederationExecutionHandle();

	public abstract void setFederationExecutionModelHandle(String federationExecutionModelHandle);

	public abstract String getFederationExecutionModelHandle();
}