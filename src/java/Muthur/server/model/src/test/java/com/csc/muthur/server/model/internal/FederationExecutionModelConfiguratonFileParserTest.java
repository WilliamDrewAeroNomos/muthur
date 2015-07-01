/**
 * 
 */
package com.csc.muthur.server.model.internal;

import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * @author williamdrew
 * 
 */
public class FederationExecutionModelConfiguratonFileParserTest extends
		AbstractModelTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.internal.FederationExecutionModelConfiguratonFileParser#FederationExecutionModelConfiguratonFileParser(java.lang.String)}
	 * .
	 */
	@Test
	public void testFederationExecutionModelConfiguratonFileParser() {

		String cwd = System.getProperty("user.dir");

		String configFileFullPathName =
				cwd + System.getProperty("file.separator")
						+ "muthur.federation.execution.model.xml";

		FederationExecutionModelConfiguratonFileParser federationExecutionModelConfiguratonFileParser =
				null;
		try {

			federationExecutionModelConfiguratonFileParser =
					new FederationExecutionModelConfiguratonFileParser(
							configFileFullPathName);

			assertNotNull(federationExecutionModelConfiguratonFileParser);

		} catch (ParserConfigurationException e) {
			fail(e.getLocalizedMessage());
		} catch (SAXException e) {
			fail(e.getLocalizedMessage());
		} catch (IOException e) {
			fail(e.getLocalizedMessage());
		}

		Map<String, FederationExecutionModel> map =
				federationExecutionModelConfiguratonFileParser
						.getFederationExecutionModels();

		assertNotNull(map);

	}
}
