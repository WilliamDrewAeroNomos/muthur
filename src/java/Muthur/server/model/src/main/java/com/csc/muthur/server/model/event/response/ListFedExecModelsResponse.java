/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ListFedExecModelsResponse extends AbstractEvent {

	private static final Logger LOG = LoggerFactory
			.getLogger(ListFedExecModelsResponse.class.getName());
	/**
	 * dataBlock attributes
	 */
	private List<FederationExecutionModel> femList;
	private FederationExecutionModel currentFEM;

	/**
	 * 
	 */
	public ListFedExecModelsResponse() {
		super();
	}

	/**
	 * Overridden and must call super.initialization()
	 */
	public void initialization() {
		if (femList == null) {
			femList = new Vector<FederationExecutionModel>();
			LOG.debug("Created the Vector<FederationExecutionModel>");
		}
		super.initialization();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element femsElement = new Element(EventAttributeName.fems.toString());

			dataBlockElement.addContent(femsElement);

			if (femList != null) {

				for (FederationExecutionModel fem : femList) {

					if (fem != null) {

						Element fedExecModelElement = new Element(
								EventAttributeName.fedExecModel.toString());

						femsElement.addContent(fedExecModelElement);

						Element femModelEntry = null;

						// federate name
						//
						if (fem.getName() != null) {
							femModelEntry = new Element(EventAttributeName.femName.toString())
									.setText(fem.getName());
							fedExecModelElement.addContent(femModelEntry);
						}

						// description
						//
						if (fem.getDescription() != null) {
							femModelEntry = new Element(
									EventAttributeName.description.toString()).setText(fem
									.getDescription());
							fedExecModelElement.addContent(femModelEntry);
						}

						// UUID
						//
						if (fem.getFedExecModelUUID() != null) {
							femModelEntry = new Element(
									EventAttributeName.fedExecModelUUID.toString()).setText(fem
									.getFedExecModelUUID());
							fedExecModelElement.addContent(femModelEntry);
						}

						// total execution time of the federation MSecs
						//
						femModelEntry = new Element(
								EventAttributeName.durationFederationExecutionMSecs.toString())
								.setText(Long.toString(fem
										.getDurationFederationExecutionMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// default duration between steps in startup protocol
						//
						femModelEntry = new Element(
								EventAttributeName.defaultDurationWithinStartupProtocolMSecs
										.toString()).setText(Long.toString(fem
								.getDefaultDurationWithinStartupProtocolMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// duration for all federates to join federation MSecs
						//
						femModelEntry = new Element(
								EventAttributeName.durationJoinFederationMSecs.toString())
								.setText(Long.toString(fem.getDurationJoinFederationMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// duration for all to register publications MSecs
						//
						femModelEntry = new Element(
								EventAttributeName.durationRegisterPublicationMSecs.toString())
								.setText(Long.toString(fem
										.getDurationRegisterPublicationMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// duration for all federates to register subscriptions MSecs

						femModelEntry = new Element(
								EventAttributeName.durationRegisterSubscriptionMSecs.toString())
								.setText(Long.toString(fem
										.getDurationRegisterSubscriptionMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// duration for all federates to register to rub MSecs
						//
						femModelEntry = new Element(
								EventAttributeName.durationRegisterToRunMSecs.toString())
								.setText(Long.toString(fem.getDurationRegisterToRunMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// duration to wait after initial termination
						// notifications are sent to all federates MSecs
						//
						femModelEntry = new Element(
								EventAttributeName.durationTimeToWaitAfterTermination
										.toString()).setText(Long.toString(fem
								.getDurationTimeToWaitAfterTerminationMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// logical start time of the federation MSecs
						//
						femModelEntry = new Element(
								EventAttributeName.fedExecModelLogicalStartTimeMSecs.toString())
								.setText(Long.toString(fem.getLogicalStartTimeMSecs()));
						fedExecModelElement.addContent(femModelEntry);

						// auto start value
						//
						femModelEntry = new Element(EventAttributeName.autoStart.toString())
								.setText(Boolean.toString(fem.isAutoStart()));
						fedExecModelElement.addContent(femModelEntry);

						// list of required federates that must complete the startup
						// protocol
						//
						Element requiredFederatesElement = new Element(
								EventAttributeName.requiredFederates.toString());
						fedExecModelElement.addContent(requiredFederatesElement);

						for (String requiredFederate : fem.getNamesOfRequiredFederates()) {
							if (requiredFederate != null) {
								requiredFederatesElement.addContent(new Element(
										EventAttributeName.requiredFederate.toString())
										.setText(requiredFederate));
							}
						}
					}
				}

			}
		}

		return dataBlockElement;
	}

	@Override
	public void startOfElement(String currentElementName) {
		// signals that a new element has begun
		if ("fedExecModel".equalsIgnoreCase(currentElementName)) {
			try {
				currentFEM = new FederationExecutionModel(currentElementName);
			} catch (MuthurException e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if (currentFEM != null) {
			if ((elementValue != null) && (elementValue.length() > 0)) {

				if (elementName.equalsIgnoreCase(EventAttributeName.femName.toString())) {
					currentFEM.setName(elementValue);
				} else if (elementName.equalsIgnoreCase(EventAttributeName.description
						.toString())) {
					currentFEM.setDescription(elementValue);
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.fedExecModelUUID.toString())) {
					currentFEM.setFedExecModelUUID(elementValue);
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.requiredFederate.toString())) {
					currentFEM.addRequiredFededrate(elementValue);
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.defaultDurationWithinStartupProtocolMSecs
								.toString())) {
					currentFEM.setDefaultDurationWithinStartupProtocolMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationFederationExecutionMSecs
								.toString())) {
					currentFEM.setDurationFederationExecutionMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.fedExecModelLogicalStartTimeMSecs
								.toString())) {
					currentFEM.setLogicalStartTimeMSecs(Long.valueOf(elementValue));
				} else if (elementName.equalsIgnoreCase(EventAttributeName.autoStart
						.toString())) {
					currentFEM.setAutoStart(Boolean.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationJoinFederationMSecs
								.toString())) {
					currentFEM.setDurationJoinFederationMSecs(Long.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationRegisterPublicationMSecs
								.toString())) {
					currentFEM.setDurationRegisterPublicationMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationRegisterSubscriptionMSecs
								.toString())) {
					currentFEM.setDurationRegisterSubscriptionMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationRegisterToRunMSecs
								.toString())) {
					currentFEM.setDurationRegisterToRunMSecs(Long.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationTimeToWaitAfterTermination
								.toString())) {
					currentFEM.setDurationTimeToWaitAfterTerminationMSecs(Long
							.valueOf(elementValue));
				}
			}
		}

	}

	/**
	 * 
	 */
	public void endOfElement(String currentElementName) {

		if ((EventAttributeName.fedExecModel.toString()
				.equalsIgnoreCase(currentElementName)) && (currentFEM != null)) {
			if (femList == null) {
				femList = new Vector<FederationExecutionModel>();
				LOG.debug("Created the Vector<FederationExecutionModel>");
			}
			femList.add(currentFEM);
			LOG.debug("Added FEM to list.");
			currentFEM = null;
		}

	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Response.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.ListFedExecModelsResponse.toString();
	}

	/**
	 * @return Returns the uuidToFederationExecutionModel.
	 */
	public final List<FederationExecutionModel> getFemList() {
		return this.femList;
	}

	/**
	 * @param uuidToFederationExecutionModel
	 *          The uuidToFederationExecutionModel to set.
	 */
	public final void setFemList(List<FederationExecutionModel> femList) {
		this.femList = femList;
	}

	/**
	 * 
	 * @param fem
	 */
	public final void addFEM(final FederationExecutionModel fem) {
		if (femList == null) {
			femList = new Vector<FederationExecutionModel>();
			LOG.debug("Created the Vector<FederationExecutionModel>");
		}

		femList.add(fem);
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.FederateRegistrationResponse;
	}
}
