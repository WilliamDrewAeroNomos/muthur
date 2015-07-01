/**
 * 
 */
package com.csc.muthur.server.model.internal;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.osgi.context.BundleContextAware;
import org.xml.sax.SAXException;

import com.atcloud.commons.exception.ATCloudException;
import com.atcloud.fem.FederationExecutionModelService;
import com.atcloud.model.FEM;
import com.atcloud.model.Federate;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.EventFactory;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ModelServiceImpl implements ModelService, ApplicationContextAware,
		BundleContextAware, ManagedService {

	private static final Logger LOG = LoggerFactory
			.getLogger(ModelServiceImpl.class.getName());

	/**
	 * List of current {@link FederationExecutionModel}s available to federates.
	 */
	public Map<String, FederationExecutionModel> uuidToFederationExecutionModel =
			new ConcurrentHashMap<String, FederationExecutionModel>();

	private BundleContext bundleContext;

	private static ApplicationContext appContext;

	private String serviceMessagingPID = "com.csc.muthur.server.model";

	private ServiceRegistration ppcService;

	private FederationExecutionModelConfiguratonFileParser federationExecutionModelConfiguratonFileParser;

	private Calendar c = Calendar.getInstance();

	private FederationExecutionModelService federationExecutionModelService;

	/**
	 * 
	 */
	public ModelServiceImpl() {
		super();

	}

	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return appContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.internal.ModelService#start()
	 */
	public void start() {

		LOG.info("Starting Model service...");

		if (bundleContext != null) {

			LOG.debug("Registering model service as a managed service...");

			// register managed service for messaging

			Dictionary<String, String> props = new Hashtable<String, String>();

			props.put("service.pid", serviceMessagingPID);

			ppcService =
					bundleContext.registerService(ManagedService.class.getName(), this,
							props);

			LOG.info("Model service registered as a managed service...");

		} else {
			LOG.warn("Bundle context was null.");
		}

		LOG.info("Model service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.internal.ModelService#stop()
	 */
	public void stop() {
		LOG.info("Stopping Model service...");

		if (ppcService != null) {
			ppcService.unregister();
			ppcService = null;
		}

		uuidToFederationExecutionModel.clear();

		LOG.info("Model service stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.internal.ModelService#addFEM(com.csc.muthur
	 * .server.model. FederationExecutionModel)
	 */
	public void addFederationExecutionModelToCache(
			final FederationExecutionModel fem) {
		if (fem != null) {
			uuidToFederationExecutionModel.put(fem.getFedExecModelUUID(), fem);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.internal.ModelService#getFederationExecutionModels
	 * ()
	 */
	public Collection<FederationExecutionModel> getFederationExecutionModels() {
		return uuidToFederationExecutionModel.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.internal.ModelService#removeFEM(com.csc.muthur
	 * .server.model .FederationExecutionModel)
	 */
	public void removeFEM(final FederationExecutionModel femToRemove) {
		if (femToRemove != null) {
			uuidToFederationExecutionModel.remove(femToRemove);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
		ModelServiceImpl.appContext = appContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.osgi.context.BundleContextAware#setBundleContext(org
	 * .osgi.framework.BundleContext)
	 */
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.ModelService#getModel(java.lang.String)
	 */
	@Override
	public FederationExecutionModel getModel(final String uuid) {

		FederationExecutionModel model = null;

		if (uuid != null) {

			model = getFederationExecutionModelFromCache(uuid);

			if (model == null) {

				/*
				 * Check if it's in the database
				 */

				try {

					model = getModelFromPersistentStore(uuid);

					if (model != null) {

						/*
						 * If we found it in the database then add it to the cache
						 */
						addFederationExecutionModelToCache(model);

						StringBuffer sb =
								new StringBuffer(
										"Added federation execution model to cache : \n");
						sb.append("Name : [" + model.getName() + "]\n");
						sb.append("Required federates : ["
								+ model.getNamesOfRequiredFederates() + "]\n");
						c.setTimeInMillis(model.getLogicalStartTimeMSecs());
						Format timeFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
						sb.append("Federation start time ["
								+ timeFormat.format(c.getTime()) + "]");
						LOG.debug(sb.toString());

					} else {
						LOG.debug("Unable to find model in persistent store using handle ["
								+ uuid + "]");
					}
				} catch (MuthurException e) {
					LOG.warn("Error attempting to query federation execution "
							+ "model from persistent store using handle [" + uuid + "]", e);

				}

			}

		}

		return model;
	}

	/**
	 * @param uuid
	 * @return
	 */
	private FederationExecutionModel getFederationExecutionModelFromCache(
			final String uuid) {
		FederationExecutionModel model;
		model = uuidToFederationExecutionModel.get(uuid);
		return model;
	}

	/**
	 * 
	 * @param handle
	 * @return
	 * @throws MuthurException
	 */
	protected FederationExecutionModel getModelFromPersistentStore(
			final String handle) throws MuthurException {

		FederationExecutionModel federationExecutionModel = null;

		try {

			FEM fem = federationExecutionModelService.getFEMByID(handle);

			if (fem != null) {

				federationExecutionModel = new FederationExecutionModel();

				/*
				 * Copy the FEM to the FederationExecutionModel
				 */

				federationExecutionModel.setName(fem.getName());
				federationExecutionModel.setDescription(fem.getDescription());
				federationExecutionModel.setFedExecModelUUID(fem.getFemID());
				federationExecutionModel.setLogicalStartTimeMSecs(fem
						.getLogicalStrtTimeMSecs());
				federationExecutionModel.setAutoStart(fem.isAutoStart());
				federationExecutionModel
						.setDefaultDurationWithinStartupProtocolMSecs(fem
								.getDefDurStrtupPrtclMSecs());
				federationExecutionModel.setDurationFederationExecutionMSecs(fem
						.getFederationExecutionMSecs());
				federationExecutionModel.setDurationJoinFederationMSecs(fem
						.getJoinFederationMSecs());
				federationExecutionModel.setDurationRegisterPublicationMSecs(fem
						.getRegisterPublicationMSecs());
				federationExecutionModel.setDurationRegisterSubscriptionMSecs(fem
						.getRegisterSubscriptionMSecs());
				federationExecutionModel.setDurationRegisterToRunMSecs(fem
						.getRegisterToRunMSecs());
				federationExecutionModel
						.setDurationWaitForStartFederationDirectiveMSecs(fem
								.getWaitForStartMSecs());
				federationExecutionModel.setDurationTimeToWaitAfterTerminationMSecs(fem
						.getWaitTimeAfterTermMSecs());
				List<Federate> federates = null;

				/*
				 * Get all federates associated with this FEM and add to the
				 * FederationExecutionModel
				 */
				try {
					federates =
							federationExecutionModelService.getFederatesInFEM(fem.getName());

					if ((federates != null) && (federates.size() > 0)) {
						for (Federate nextFederate : federates) {
							if (nextFederate != null) {
								federationExecutionModel.addRequiredFededrate(nextFederate
										.getName());
							}
						}
					}

				} catch (ATCloudException e) {
					throw new MuthurException(e);
				}
			}

		} catch (ATCloudException e) {
			LOG.error(
					"Error attempting to retrieve FEM from persistence store using handle ["
							+ handle + "]", e);
			throw new MuthurException(e);
		}

		return federationExecutionModel;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary props)
			throws ConfigurationException {

		String configFileName = null;

		if (props == null) {

			LOG.debug("No configuration from configuration admin or "
					+ "old configuration has been deleted");

			if (federationExecutionModelConfiguratonFileParser != null) {
				federationExecutionModelConfiguratonFileParser
						.getFederationExecutionModels().clear();
			}

			if (uuidToFederationExecutionModel != null) {
				uuidToFederationExecutionModel.clear();
			}

		} else {

			LOG.debug("Model properties updated - [" + props + "] from ["
					+ props.get("felix.fileinstall.filename") + "]");

			configFileName = (String) props.get("modelFileName");

			String cwd = System.getProperty("user.dir");

			String configFileFullPathName =
					cwd + File.separator + "deploy" + File.separator + configFileName;

			File f = new File(configFileFullPathName);

			if (f.exists()) {

				LOG.debug("Loading federation execution models from file ["
						+ configFileFullPathName + "]");

				try {

					federationExecutionModelConfiguratonFileParser =
							new FederationExecutionModelConfiguratonFileParser(
									configFileFullPathName);

					if (!federationExecutionModelConfiguratonFileParser.isValidParse()) {
						throw new ConfigurationException(
								"Error parsing federation execution model file",
								configFileFullPathName);
					}

					// update the FEM list of models

					uuidToFederationExecutionModel =
							federationExecutionModelConfiguratonFileParser
									.getFederationExecutionModels();

					LOG.info("Loaded [" + uuidToFederationExecutionModel.size()
							+ "] models.");

					for (FederationExecutionModel fem : uuidToFederationExecutionModel
							.values()) {

						if (fem != null) {
							LOG.info("Federation Execution Model : ");
							LOG.info("Name : [" + fem.getName() + "]");
							LOG.info("Required federates : ["
									+ fem.getNamesOfRequiredFederates() + "]");
							c.setTimeInMillis(fem.getLogicalStartTimeMSecs());
							Format timeFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");
							LOG.info("Federation start time ["
									+ timeFormat.format(c.getTime()) + "]");
						}
					}

				} catch (ParserConfigurationException e) {
					LOG.error(e.getLocalizedMessage());
				} catch (SAXException e) {
					LOG.error(e.getLocalizedMessage());
				} catch (IOException e) {
					LOG.error(e.getLocalizedMessage());
				}

			} else {

				LOG.error("File [" + configFileFullPathName + "] from ["
						+ props.get("felix.fileinstall.filename") + "] does not exist. "
						+ "Please provide a valid value for the "
						+ "modelFileName property within the file ["
						+ props.get("felix.fileinstall.filename")
						+ "] in the /etc directory in the Fuse deployment.");

				throw new ConfigurationException("modelFileName", "File ["
						+ configFileFullPathName + "] from ["
						+ props.get("felix.fileinstall.filename") + "] does not exist.");
			}

			LOG.debug("Properties [" + props + "]");

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.ModelService#createEvent(String)
	 */
	@Override
	public IEvent createEvent(final String eventAsXML) throws MuthurException {
		return EventFactory.getInstance().createEvent(eventAsXML);
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
	 * @return the federationExecutionModelService
	 */
	@Override
	public FederationExecutionModelService getFederationExecutionModelService() {
		return federationExecutionModelService;
	}

	/**
	 * @param federationExecutionModelService
	 *          the federationExecutionModelService to set
	 */
	@Override
	public void setFederationExecutionModelService(
			FederationExecutionModelService federationExecutionModelService) {
		this.federationExecutionModelService = federationExecutionModelService;
	}

}
