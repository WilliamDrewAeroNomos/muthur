/**
 * 
 */
package com.csc.muthur.server.configuration.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.configuration.internal.ConfigurationServiceImpl;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */

public class ConfigurationServiceImplTest extends TestCase {

	private String propsFileName = "muthur.configuration.properties";
	private FileOutputStream fos;

	private String messagingHost = "messagingHost";
	private String messagingHostValue = "mbpro.local";
	private String messagingTransport = "messagingTransport";
	private String messagingTransportValue = "tcp";
	private String messagingPort = "messagingPort";
	private String messagingPortValue = "62629";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		fos = new FileOutputStream(propsFileName);

		// Read properties file.
		Properties properties = new Properties();

		properties.setProperty(messagingHost, messagingHostValue);
		properties.setProperty(messagingTransport, messagingTransportValue);
		properties.setProperty(messagingPort, messagingPortValue);

		// Write properties file.
		try {
			properties.store(fos, null);
		} catch (IOException e) {
		}

		new ClassPathXmlApplicationContext(
				"/META-INF/spring/configuration-bundle-context.xml");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (fos != null) {

			File f = new File(propsFileName);

			if (f.exists()) {
				f.delete();
			}

		}
	}

	/**
	 * 
	 */
	@Test
	public void testConfigurationServiceImplCreate() {

		ConfigurationService cs = new ConfigurationServiceImpl();

		assert (cs != null);

		cs.start();

		// TODO: Need to figure out a way to test in the container since
		// we have to have a valid bundle context in the start() method to update
		// the values in the service

		// assert(cs.getMessagingHost().equalsIgnoreCase(messagingHostValue));
		// assert (cs.getMessagingTransport()
		// .equalsIgnoreCase(messagingTransportValue));
		// assert (cs.getMessagingPort() == Integer.valueOf(messagingPortValue)
		// .intValue());

	}
}
