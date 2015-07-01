package com.csc.muthur.server.federation.internal.datachannel;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.ChannelAttributes;
import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataChannelListener;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataChannelImpl extends Thread {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationDataChannelImpl.class.getName());

	private int controlBlockSize = 0;

	private Socket socket = null;

	private boolean channelAlive = true;

	private DataChannelListener dataChannelListener;

	private SocketAddress remoteSocketAddress;
	private int localPort;

	/**
	 * Pre-calculated ending index for each attribute in the channel buffer read
	 * 
	 */
	private final int endIdxEventName = ChannelAttributes.getEventNameOffset()
			+ ChannelAttributes.EVENT_NAME_SIZE;
	private final int endIdxCorrelationId = ChannelAttributes
			.getCorrelationIdOffset() + ChannelAttributes.CORRELATION_ID_SIZE;
	private final int endIdxReplyToQueueName = ChannelAttributes
			.getReplyToQueueNameOffset() + ChannelAttributes.REPLY_TO_QUEUE_NAME_SIZE;
	private final int endIdxPayLoadLength = ChannelAttributes
			.getPayloadLengthOffset() + ChannelAttributes.PAYLOAD_LENGTH_SIZE;

	/**
	 * 
	 * @param socket
	 * @throws MuthurException
	 */
	public FederationDataChannelImpl(final Socket socket,
			final DataChannelListener dataChannelListener) throws MuthurException {

		if (socket == null) {
			throw new MuthurException(
					"Socket passed to FederationDataChannelImpl was null");
		}

		if (dataChannelListener == null) {
			throw new MuthurException(
					"DataChannelListener passed to FederationDataChannelImpl was null");
		}

		this.socket = socket;

		/*
		 * save this information while it's open
		 */
		setRemoteSocketAddress(socket.getRemoteSocketAddress());
		setLocalPort(socket.getLocalPort());

		this.setDataChannelListener(dataChannelListener);

		controlBlockSize = ChannelAttributes.controlBlockSize();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {

		LOG.info("FederationDataChannel listening on port [" + getLocalPort()
				+ "] connected to host [" + getRemoteSocketAddress() + "]");

		try (InputStreamReader isr = new InputStreamReader(socket.getInputStream());) {

			while (isChannelAlive()) {

				DataChannelControlBlock dccBlock = new DataChannelControlBlock();

				String controlBlock = readChannel(isr, controlBlockSize);

				/*
				 * Get version
				 */

				dccBlock.setVersion(parseAttributeValueFromStream(controlBlock,
						ChannelAttributes.getVersionOffset(),
						ChannelAttributes.VERSION_SIZE));

				/*
				 * Get event name
				 */

				dccBlock.setEventName(parseAttributeValueFromStream(controlBlock,
						ChannelAttributes.getEventNameOffset(), endIdxEventName));

				/*
				 * Get correlation ID
				 */

				dccBlock.setCorrelationID(parseAttributeValueFromStream(controlBlock,
						ChannelAttributes.getCorrelationIdOffset(), endIdxCorrelationId));

				/*
				 * Get reply-to queue name
				 */

				dccBlock.setReplyToQueueName(parseAttributeValueFromStream(
						controlBlock, ChannelAttributes.getReplyToQueueNameOffset(),
						endIdxReplyToQueueName));

				/*
				 * Get payload length
				 */

				String payLoadLengthAsString =
						parseAttributeValueFromStream(controlBlock,
								ChannelAttributes.getPayloadLengthOffset(), endIdxPayLoadLength);

				try {
					dccBlock.setPayLoadLength(Integer.valueOf(payLoadLengthAsString));
				} catch (NumberFormatException nfe) {
					LOG.error("Number format exception attempting to format ["
							+ payLoadLengthAsString + "] to a long.", nfe);
					throw nfe;
				}

				LOG.debug("Control block  [" + controlBlock + "]");

				LOG.debug("Version [" + dccBlock.getVersion() + "].");
				LOG.debug("Event [" + dccBlock.getEventName() + "].");
				LOG.debug("Correlation ID  [" + dccBlock.getCorrelationID() + "].");
				LOG.debug("Reply-To queue [" + dccBlock.getReplyToQueueName() + "].");
				LOG.debug("Payload length [" + dccBlock.getPayLoadLength() + "].");

				/*
				 * Get payload
				 */

				String payLoad = readChannel(isr, dccBlock.getPayLoadLength());

				LOG.debug("Pay load [" + payLoad + "]");

				dataChannelListener.messageReceived(dccBlock, payLoad);
			}

		} catch (IOException e) {
			LOG.warn("IO exception on federation data channel ["
					+ e.getLocalizedMessage() + "]");
		}

		try {
			socket.close();
		} catch (IOException e) {
			/*
			 * Nothing to do here
			 */
		}

		LOG.info("FederationDataChannel listening on port [" + getLocalPort()
				+ "] connected to host [" + getRemoteSocketAddress() + "] is exiting");
	}

	/**
	 * 
	 * @param message
	 * @return
	 */
	private String parseAttributeValueFromStream(final String stream,
			final int beginIdx, final int endIdx) {

		String attributeValue = null;

		if (stream != null) {
			attributeValue = stream.substring(beginIdx, endIdx);
			if (attributeValue != null) {
				attributeValue = StringUtils.trimToEmpty(attributeValue);
			}
		}

		return attributeValue;
	}

	/**
	 * 
	 * @param reader
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public String readChannel(Reader reader, int length) throws IOException {

		char[] buffer = new char[length];
		int totalRead = 0;

		while (totalRead < length) {
			int read = reader.read(buffer, totalRead, length - totalRead);
			if (read == -1) {
				throw new IOException();
			}
			totalRead += read;
		}
		return new String(buffer);
	}

	/**
	 * @return the channelAlive
	 */
	public boolean isChannelAlive() {
		return channelAlive;
	}

	/**
	 * @param channelAlive
	 *          the channelAlive to set
	 */
	public void setChannelAlive(boolean channelAlive) {
		this.channelAlive = channelAlive;
	}

	/**
	 * @return the dataChannelListener
	 */
	public DataChannelListener getDataChannelListener() {
		return dataChannelListener;
	}

	/**
	 * @param dataChannelListener
	 *          the dataChannelListener to set
	 */
	public void setDataChannelListener(DataChannelListener dataChannelListener) {
		this.dataChannelListener = dataChannelListener;
	}

	/**
	 * @return the remoteSocketAddress
	 */
	public SocketAddress getRemoteSocketAddress() {
		return remoteSocketAddress;
	}

	/**
	 * @param remoteSocketAddress
	 *          the remoteSocketAddress to set
	 */
	public void setRemoteSocketAddress(SocketAddress remoteSocketAddress) {
		this.remoteSocketAddress = remoteSocketAddress;
	}

	/**
	 * @return the localPort
	 */
	public int getLocalPort() {
		return localPort;
	}

	/**
	 * @param localPort
	 *          the localPort to set
	 */
	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
}