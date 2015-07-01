package com.csc.muthur.server.federation.internal.datachannel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataChannelListenerFactory;
import com.csc.muthur.server.federation.FederationDataChannelServer;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataChannelServerImpl implements
		FederationDataChannelServer {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationDataChannelServerImpl.class.getName());

	private int portNumber;
	private boolean listening = true;
	private DataChannelListenerFactory dataChannelListenerFactory;

	private ServerSocket serverSocket;
	private ExecutorService dataChannelPool = Executors.newCachedThreadPool();
	private List<Socket> socketList = new ArrayList<Socket>();

	/**
	 * 
	 * @param portNumber
	 * @param dataChannelListenerFactory
	 */
	public FederationDataChannelServerImpl() {
	}

	/**
	 * 
	 */
	@Override
	public void run() {

		try {

			setServerSocket(new ServerSocket(portNumber));

		} catch (IOException e) {
			LOG.error("Error starting FederationDataChannelServerImpl. ["
					+ e.getLocalizedMessage() + "] on port [" + portNumber + "]");
			return;
		}

		LOG.info("FederationDataChannelServerImpl is listening on port ["
				+ portNumber + "]");

		try {

			while (isListening()) {

				Socket s = serverSocket.accept();

				socketList.add(s);

				FederationDataChannelImpl fdc =
						new FederationDataChannelImpl(s,
								dataChannelListenerFactory.createListener());

				dataChannelPool.execute(fdc);

			}

		} catch (MuthurException e) {
			LOG.error("Error creating FederationDataChannel ["
					+ e.getLocalizedMessage() + "]");
		} catch (IOException e) {
			LOG.debug("IO exception on federation server socket ["
					+ e.getLocalizedMessage() + "]");
		}

		LOG.info("FederationDataChannelServerImpl listening on port [" + portNumber
				+ "] is exiting");

		if ((socketList != null) && (socketList.size() > 0)) {

			for (Socket thisSocket : socketList) {

				if (thisSocket != null) {
					try {
						LOG.debug("Closing data channel to host ["
								+ thisSocket.getInetAddress() + "] at port ["
								+ thisSocket.getPort() + "]");
						thisSocket.close();
						LOG.info("Closed data channel to host ["
								+ thisSocket.getInetAddress() + "] at port ["
								+ thisSocket.getPort() + "]");
					} catch (IOException e) {
						LOG.warn("IO Exception closing data channel to host ["
								+ thisSocket.getInetAddress() + "] at port ["
								+ thisSocket.getPort() + "]");
					}
				}

			}

		}

		/*
		 * Shut down the data channel listener thread pool
		 */
		if (dataChannelPool != null) {

			if (!dataChannelPool.isShutdown()) {
				shutdownAndAwaitTermination(dataChannelPool);
			}

		}
	}

	/**
	 * 
	 * @param pool
	 */
	private void shutdownAndAwaitTermination(final ExecutorService pool) {

		if (serverSocket != null) {

			try {
				serverSocket.close();
			} catch (IOException e) {
				LOG.debug("IO Exception closing server socket", e);
			}

		}

		pool.shutdown(); // Disable new tasks from being submitted

		try {
			// Cancel currently executing tasks
			pool.shutdownNow();

			// Wait a while for tasks to respond to being cancelled
			if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
				LOG.warn("Federation data channel pool did not terminate");
			}

		} catch (InterruptedException ie) {
			// (Re-)Cancel if current thread also interrupted
			pool.shutdownNow();
		}
	}

	/**
	 * 
	 */
	@Override
	public void stop() {

		setListening(false);

		shutdownAndAwaitTermination(dataChannelPool);
	}

	/**
	 * @return the listening
	 */
	@Override
	public boolean isListening() {
		return listening;
	}

	/**
	 * @param listening
	 *          the listening to set
	 */
	@Override
	public void setListening(boolean listening) {
		this.listening = listening;
	}

	/**
	 * @return the serverSocket
	 */
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * @param serverSocket
	 *          the serverSocket to set
	 */
	private void setServerSocket(ServerSocket serverSocket) {
		this.serverSocket = serverSocket;
	}

	/**
	 * @return the portNumber
	 */
	@Override
	public int getPortNumber() {
		return portNumber;
	}

	/**
	 * @param portNumber
	 *          the portNumber to set
	 */
	@Override
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	/**
	 * @return the dataChannelListenerFactory
	 */
	@Override
	public DataChannelListenerFactory getDataChannelListenerFactory() {
		return dataChannelListenerFactory;
	}

	/**
	 * @param dataChannelListenerFactory
	 *          the dataChannelListenerFactory to set
	 */
	@Override
	public void setDataChannelListenerFactory(
			DataChannelListenerFactory dataChannelListenerFactory) {
		this.dataChannelListenerFactory = dataChannelListenerFactory;
	}
}