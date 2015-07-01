/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.Collection;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.FederateStateEvent;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateStateQueueListener implements MessageListener {

	private final static Logger LOG = LoggerFactory
			.getLogger(FederateStateQueueListener.class.getName());

	private FederationService federationService;

	/**
	 * @param federationService
	 */
	public FederateStateQueueListener(final FederationService federationService) {
		this.federationService = federationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {

		LOG.debug("Event message received");

		IEvent event = null;

		if (message == null) {
			LOG.error("Message was null.");
		}

		if (!(message instanceof TextMessage)) {
			LOG.error("Invalid message type encountered. " + "Expecting TextMessage.");
		} else {

			TextMessage tm = (TextMessage) message;

			String xmlContent;

			try {

				xmlContent = tm.getText();

				LOG.debug("Event - [" + xmlContent + "]");

				event = federationService.getModelService().createEvent(xmlContent);

				if (event instanceof FederateStateEvent) {

					FederateStateEvent fse = (FederateStateEvent) event;

					String federateName = fse.getFederateName();

					if ((federateName != null) && (!"".equalsIgnoreCase(federateName))) {

						// TODO:

						if (0 == fse.getFederateState()) {

							LOG.debug("Terminate all federations in which [" + federateName
									+ "] is participating as a required federate.");

							Map<FederationExecutionModel, FederationExecutionController> modelToFedExecCtrlMap = federationService
									.getFedExecModelToFedExecCtrlrMap();

							if (modelToFedExecCtrlMap != null) {

								Collection<FederationExecutionController> fedExecCtrlrs = modelToFedExecCtrlMap
										.values();

								if (fedExecCtrlrs != null) {

									for (FederationExecutionController fedExecCtrl : fedExecCtrlrs) {

										if (fedExecCtrl != null) {

											if (fedExecCtrl.getFederationExecutionModel().isRequired(
													federateName)) {

												LOG.debug("Federation ["
														+ fedExecCtrl.getFederationExecutionModel()
																.getName() + "] being terminated due to "
														+ "required federate [" + federateName
														+ "] terminating.");

												fedExecCtrl.terminate();
											}
										}
									}
								}
							}
						}
					}
				}

			} catch (MuthurException e) {
				LOG.error(e.getLocalizedMessage());
			} catch (JMSException e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
	}
}
