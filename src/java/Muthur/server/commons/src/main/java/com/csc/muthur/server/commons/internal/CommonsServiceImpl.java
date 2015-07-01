/**
 * 
 */
package com.csc.muthur.server.commons.internal;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederateRegistrationHandle;
import com.csc.muthur.server.commons.FederationExecutionHandle;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.FederationExecutionModelHandle;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */

public class CommonsServiceImpl implements CommonsService {

	private static final Logger LOG = LoggerFactory
			.getLogger(CommonsServiceImpl.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.internal.CommonsService#start()
	 */
	public void start() {

		LOG.debug("Starting commons service...");

		LOG.info("Started commons service.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.internal.CommonsService#stop()
	 */
	public void stop() {

		LOG.debug("Stopping commons service...");

		LOG.info("Stopped commons service.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.CommonsService#findAvailablePort(int,
	 * int)
	 */
	public int findAvailablePort(final int minPort, final int maxPort)
			throws IllegalArgumentException {

		if (maxPort < minPort) {
			throw new IllegalArgumentException(
					"Invalid port range provided. Minimum port was greater than the maximum port.");
		}

		int availablePort = -1;

		for (int x = minPort; x <= maxPort; x++) {
			if (portAvailable(x)) {
				availablePort = x;
				LOG.debug("Found port [" + availablePort + "] available.");
				break;
			}
		}

		return availablePort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.CommonsService#portAvailable(int)
	 */
	public boolean portAvailable(int port) {

		ServerSocket ss = null;
		DatagramSocket ds = null;
		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);
			return true;
		} catch (IOException e) {
			// catch and clean up below
		} finally {
			if (ds != null) {
				ds.close();
			}

			if (ss != null) {
				try {
					ss.close();
				} catch (IOException e) {
					/* should not be thrown */
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.commons.CommonsService#getAllFieldsInHierarchy(java
	 * .lang .Object)
	 */
	public List<Field> getAllFieldsInHierarchy(final Object object)
			throws ClassNotFoundException {

		List<Field> ll = new LinkedList<Field>();

		if (object != null) {

			// get the class
			Class<?> runtimeClazz = object.getClass();

			if (runtimeClazz != null) {

				String className = runtimeClazz.getName();

				if (className != null) {

					Class<?> clazz = Class.forName(className);

					if (clazz != null) {

						ll = getAllFields(ll, clazz);
					}
				}
			}
		}

		return ll;
	}

	/**
	 * Recursive method that traverses the object graph to retrieve all Fields.
	 * 
	 * @param fields
	 *          Will contain all the Fields in the class hierarchy upon return
	 * @param type
	 *          Outermost sub-class
	 * @return
	 */
	private static List<Field> getAllFields(List<Field> fields, Class<?> type) {

		for (Field field : type.getDeclaredFields()) {
			fields.add(field);
		}

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	/**
	 * Create a {@link ObjectOwnershipID} with federate registration handle
	 * represented as a {@link String} and a {@link FederationExecutionID}.
	 * 
	 * @param federateRegistrationHandle
	 * @param federationExecutionID
	 * @return
	 * @throws MuthurException
	 */
	public ObjectOwnershipID createObjectOwnershipID(
			final String federateRegistrationHandle,
			final FederationExecutionID federationExecutionID) throws MuthurException {

		// check parameters

		final String federationExecutionHandleNullMsg = "Federation execution handle is null";
		if (federateRegistrationHandle == null) {
			throw new MuthurException(federationExecutionHandleNullMsg);
		}
		if (federationExecutionID == null) {
			throw new MuthurException(federationExecutionHandleNullMsg);
		}

		return new ObjectOwnershipIDImpl(federationExecutionID,
				new FederateRegistrationHandle(federateRegistrationHandle));

	}

	/**
	 * Create a {@link ObjectOwnershipID} with federate registration handle,
	 * federation execution handle and federation execution model handle
	 * represented as {@link String}s.
	 * 
	 * @param federateRegistrationHandle
	 * @param federationExecutionHandle
	 * @param federationExecutionModelHandle
	 * @return
	 * @throws MuthurException
	 */
	public ObjectOwnershipID createObjectOwnershipID(
			final String federateRegistrationHandle,
			final String federationExecutionHandle,
			final String federationExecutionModelHandle) throws MuthurException {

		// check parameters

		if (federateRegistrationHandle == null) {
			throw new MuthurException("Federate registration handle is null");
		}
		if (federationExecutionHandle == null) {
			throw new MuthurException("Federation execution handle is null");
		}
		if (federationExecutionModelHandle == null) {
			throw new MuthurException("Federation execution model handle is null");
		}

		return new ObjectOwnershipIDImpl(createFederationExecutionID(
				federationExecutionHandle, federationExecutionModelHandle),
				new FederateRegistrationHandle(federateRegistrationHandle));

	}

	/**
	 * Create a {@link FederationExecutionID} with federation execution handle and
	 * federation execution model handle represented as {@link String}s.
	 * 
	 * @param federationExecutionHandle
	 * @param federationExecutionModelHandle
	 * @return
	 * @throws MuthurException
	 */
	public FederationExecutionID createFederationExecutionID(
			final String federationExecutionHandle,
			final String federationExecutionModelHandle) throws MuthurException {

		// check parameters

		if (federationExecutionHandle == null) {
			throw new MuthurException("Federation execution handle is null");
		}
		if (federationExecutionModelHandle == null) {
			throw new MuthurException("Federation execution model handle is null");
		}

		return new FederationExecutionIDImpl(new FederationExecutionHandle(
				federationExecutionHandle), new FederationExecutionModelHandle(
				federationExecutionModelHandle));
	}

}
