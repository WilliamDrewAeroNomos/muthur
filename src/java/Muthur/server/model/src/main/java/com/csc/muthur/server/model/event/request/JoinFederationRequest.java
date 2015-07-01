/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;
import com.csc.muthur.server.model.event.EventHandlerName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class JoinFederationRequest extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private FederationExecutionModel federationExecutionModel;
	private String federateRegistrationHandle;

	/**
	 * 
	 */
	public JoinFederationRequest() {
		super();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element fedExecModelElement =
					new Element(EventAttributeName.fedExecModel.toString());

			dataBlockElement.addContent(fedExecModelElement);

			Element femModelEntry = null;

			if (federationExecutionModel != null) {

				if (federationExecutionModel.getName() != null) {
					femModelEntry =
							new Element(EventAttributeName.femName.toString())
									.setText(federationExecutionModel.getName());
					fedExecModelElement.addContent(femModelEntry);
				}

				if (federationExecutionModel.getFedExecModelUUID() != null) {
					femModelEntry =
							new Element(EventAttributeName.fedExecModelUUID.toString())
									.setText(federationExecutionModel.getFedExecModelUUID());
					fedExecModelElement.addContent(femModelEntry);
				}

				if (federationExecutionModel.getDescription() != null) {
					femModelEntry =
							new Element(EventAttributeName.description.toString())
									.setText(federationExecutionModel.getDescription());
					fedExecModelElement.addContent(femModelEntry);
				}

				// total federation execution time

				femModelEntry =
						new Element(EventAttributeName.durationFederationExecutionMSecs
								.toString()).setText(Long.toString(federationExecutionModel
								.getDurationFederationExecutionMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// default duration between steps in startup protocol

				femModelEntry =
						new Element(
								EventAttributeName.defaultDurationWithinStartupProtocolMSecs
										.toString()).setText(Long.toString(federationExecutionModel
								.getDefaultDurationWithinStartupProtocolMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// duration join

				femModelEntry =
						new Element(EventAttributeName.durationJoinFederationMSecs
								.toString()).setText(Long.toString(federationExecutionModel
								.getDurationJoinFederationMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// duration register publications

				femModelEntry =
						new Element(EventAttributeName.durationRegisterPublicationMSecs
								.toString()).setText(Long.toString(federationExecutionModel
								.getDurationRegisterPublicationMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// duration register publications

				femModelEntry =
						new Element(EventAttributeName.durationRegisterSubscriptionMSecs
								.toString()).setText(Long.toString(federationExecutionModel
								.getDurationRegisterSubscriptionMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// duration register ready to run

				femModelEntry =
						new Element(EventAttributeName.durationRegisterToRunMSecs
								.toString()).setText(Long.toString(federationExecutionModel
								.getDurationRegisterToRunMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// duration to wait after termination notice

				femModelEntry =
						new Element(EventAttributeName.durationTimeToWaitAfterTermination
								.toString()).setText(Long.toString(federationExecutionModel
								.getDurationTimeToWaitAfterTerminationMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// auto start value
				//
				femModelEntry =
						new Element(EventAttributeName.autoStart.toString())
								.setText(Boolean.toString(federationExecutionModel
										.isAutoStart()));
				fedExecModelElement.addContent(femModelEntry);

				// logical start time

				femModelEntry =
						new Element(EventAttributeName.fedExecModelLogicalStartTimeMSecs
								.toString()).setText(Long.toString(federationExecutionModel
								.getLogicalStartTimeMSecs()));
				fedExecModelElement.addContent(femModelEntry);

				// list of required federates that must complete the startup
				// protocol
				//
				Element requiredFederatesElement =
						new Element(EventAttributeName.requiredFederates.toString());
				fedExecModelElement.addContent(requiredFederatesElement);

				for (String requiredFederate : federationExecutionModel
						.getNamesOfRequiredFederates()) {
					if (requiredFederate != null) {
						requiredFederatesElement.addContent(new Element(
								EventAttributeName.requiredFederate.toString())
								.setText(requiredFederate));
					}
				}

			}
		}

		return dataBlockElement;

	}

	/**
	 * @return the federateName
	 */
	public final FederationExecutionModel getFederationExecutionModel() {
		return federationExecutionModel;
	}

	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.JoinFederationRequest.getDescription();
	}

	@Override
	public void startOfElement(String currentElementName) throws MuthurException {
		// signals that a new element has begun
		if (EventAttributeName.fedExecModel.toString().equalsIgnoreCase(
				currentElementName)) {
			federationExecutionModel =
					new FederationExecutionModel(currentElementName);
		}
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if (federationExecutionModel != null) {
			if ((elementValue != null) && (elementValue.length() > 0)) {

				if (elementName.equalsIgnoreCase(EventAttributeName.femName.toString())) {
					federationExecutionModel.setName(elementValue);
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.fedExecModelUUID.toString())) {
					federationExecutionModel.setFedExecModelUUID(elementValue);
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.requiredFederate.toString())) {
					federationExecutionModel.addRequiredFededrate(elementValue);
				} else if (elementName.equalsIgnoreCase(EventAttributeName.description
						.toString())) {
					federationExecutionModel.setDescription(elementValue);
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.fedExecModelLogicalStartTimeMSecs
								.toString())) {
					federationExecutionModel.setLogicalStartTimeMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationFederationExecutionMSecs
								.toString())) {
					federationExecutionModel.setDurationFederationExecutionMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.defaultDurationWithinStartupProtocolMSecs
								.toString())) {
					federationExecutionModel
							.setDefaultDurationWithinStartupProtocolMSecs(Long
									.valueOf(elementValue));
				} else if (elementName.equalsIgnoreCase(EventAttributeName.autoStart
						.toString())) {
					federationExecutionModel.setAutoStart(Boolean.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationJoinFederationMSecs
								.toString())) {
					federationExecutionModel.setDurationJoinFederationMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationRegisterPublicationMSecs
								.toString())) {
					federationExecutionModel.setDurationRegisterPublicationMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationRegisterSubscriptionMSecs
								.toString())) {
					federationExecutionModel.setDurationRegisterSubscriptionMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationRegisterToRunMSecs
								.toString())) {
					federationExecutionModel.setDurationRegisterToRunMSecs(Long
							.valueOf(elementValue));
				} else if (elementName
						.equalsIgnoreCase(EventAttributeName.durationTimeToWaitAfterTermination
								.toString())) {
					federationExecutionModel
							.setDurationTimeToWaitAfterTerminationMSecs(Long
									.valueOf(elementValue));
				}

			}
		}

	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.JoinFederationRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#initialization()
	 */
	@Override
	public void initialization() {
		if ((this.federateRegistrationHandle != null)
				&& (!this.federateRegistrationHandle.equals(""))) {
			setFederateRegistrationHandle(this.federateRegistrationHandle);
		}
	}

	/**
	 * @param federationExecutionModel
	 *          the federationExecutModel to set
	 */
	public void setFederationExecutionModel(
			FederationExecutionModel federationExecutionModel) {
		this.federationExecutionModel = federationExecutionModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.joinFederationRequestHandler.toString();
	}

}
