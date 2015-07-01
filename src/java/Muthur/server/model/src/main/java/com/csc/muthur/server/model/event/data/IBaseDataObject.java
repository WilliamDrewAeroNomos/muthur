/**
 * 
 */
package com.csc.muthur.server.model.event.data;

import org.jdom.Element;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface IBaseDataObject {

	/**
	 * 
	 * @return
	 */
	String getNaturalKey();

	/**
	 * 
	 * @return
	 */
	String getEventName();

	/**
	 * 
	 * @return
	 */
	String serialize();

	/**
	 * 
	 * @param currentElementName
	 */
	void startOfElement(String currentElementName);

	/**
	 * 
	 * @param currentElementName
	 */
	void endOfElement(String currentElementName);

	/**
	 * 
	 * @param elementName
	 * @param elementValue
	 */
	void newElement(String elementName, String elementValue);

	/**
	 * 
	 * @return
	 */
	DataTypeEnum getDataType();

	/**
	 * 
	 * @param objectAsXML
	 * @throws MuthurException
	 */
	void initialization(final String objectAsXML) throws MuthurException;

	/**
	 * 
	 * @return
	 */
	boolean validate();
	
	/**
	 * 
	 * @param dataBlockElement
	 */
	void addDataBlockElements(Element dataBlockElement);

	/**
	 * 
	 * @param dataObjectAsXMLElement
	 */
	public abstract void buildDataObjectAsXMLElement(
			Element dataObjectAsXMLElement);

	/**
	 * @return the objectUUID
	 */
	public String getDataObjectUUID();

	/**
	 * @return the createTimestampMSecs
	 */
	public long getObjectCreateTimeMSecs();

	/**
	 * The default for data objects is an aircraft element as the ID element. The
	 * ID element is a child to the dataObjectAsXML element
	 */
	public void getIDElementContents(Element dataObjectAsXML);
}
