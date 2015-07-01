/**
 * 
 */
package com.csc.muthur.server.registration;

import java.util.Map;

import com.atcloud.license.LicenseService;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.router.RouterService;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public interface RegistrationService {

	/**
	 * 
	 */
	void start() throws MuthurException;;

	/**
	 * 
	 */
	void stop() throws MuthurException;;

	/**
	 * 
	 * @param name
	 * @throws MuthurException
	 */
	void registerFederate(final FederateRegistrationRequest federateRegistration)
			throws MuthurException;

	/**
	 * 
	 * @param name
	 * @throws MuthurException
	 */
	void deregisterFederate(final String name) throws MuthurException;

	/**
	 * 
	 * @param federateName
	 * @return
	 */
	FederateRegistrationRequest getFederateRegistrationRequest(
			final String federateName);

	/**
	 * 
	 * @param registrationHandle
	 * @return
	 */
	String getFederateName(final String registrationHandle);

	/**
	 * 
	 * @return
	 */
	ConfigurationService getConfigurationService();

	/**
	 * @param configurationService
	 *          the configurationService to set
	 */
	void setConfigurationService(ConfigurationService configurationService);

	/**
	 * @return the routerService
	 */
	RouterService getRouterService();

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	void setRouterService(RouterService routerService);

	/**
	 * 
	 * @param federateRegistrationHandle
	 * @return
	 */
	boolean isFederateRegistered(final String federateRegistrationHandle);

	/**
	 * 
	 * @param licenseService
	 */
	void setLicenseService(LicenseService licenseService);

	/**
	 * 
	 * @return
	 */
	LicenseService getLicenseService();

	/**
	 * 
	 * @param nameToFederateRegistration
	 */
	void setNameToFederateRegistration(
			Map<String, FederateRegistrationRequest> nameToFederateRegistration);

	/**
	 * 
	 * @return
	 */
	Map<String, FederateRegistrationRequest> getNameToFederateRegistration();

}