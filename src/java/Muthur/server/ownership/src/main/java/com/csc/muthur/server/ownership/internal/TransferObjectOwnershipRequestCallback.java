package com.csc.muthur.server.ownership.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.response.TransferObjectOwnershipResponse;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.ownership.PendingFederateRequestEntry;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class TransferObjectOwnershipRequestCallback implements RequestCallback {

	private static final Logger LOG = LoggerFactory
			.getLogger(TransferObjectOwnershipRequestCallback.class.getName());
	private OwnershipService ownershipService;

	/**
	 * 
	 */
	public TransferObjectOwnershipRequestCallback(
			final OwnershipService ownershipService) {
		this.ownershipService = ownershipService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.internal.RequestCallback#onError(java.lang
	 * .Exception )
	 */
	@Override
	public void onError(PendingFederateRequestEntry entry, Exception e) {

		LOG.debug("Received error in callback [" + e.getLocalizedMessage() + "]");

		TransferObjectOwnershipResponse response =
				new TransferObjectOwnershipResponse();

		try {

			response.initialization(entry.getEvent().serialize());

			response.setSourceOfEvent(MessageDestination.MUTHUR);
			response.setStatus("complete");
			response.setSuccess(false);

			response.setDataObjectUUID(entry.getEvent().getEventUUID());

			DataChannelControlBlock dccBlock = entry.getDccBlock();

			if (dccBlock == null) {
				throw new MuthurException(
						"DataChannelControlBlock from original transfer ownership request was null.");
			}

			LOG.debug("Sending transfer object ownership error response to dispatcher...");

			ownershipService.getRouterService().queue(response.serialize(),
					dccBlock.getReplyToQueueName(), dccBlock.getCorrelationID());

			LOG.info("Transfer object ownership error response sent to ["
					+ entry.getEvent().getSourceOfEvent() + "]");

		} catch (MuthurException e1) {
			LOG.error("Error sending error message for pending [" + entry.getEvent()
					+ "] - [" + e1.getLocalizedMessage() + "]");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.internal.RequestCallback#onSuccess(com.
	 * csc.muthur .model.event.IEvent)
	 */
	@Override
	public void onSuccess(IEvent request) {
		// NOTE: This is not called since a successful response is handled in the
		// TransferObjectOwnershipResponse handler
	}

}