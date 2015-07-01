/**
 * 
 */
package com.csc.muthur.server.commons.internal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;

/**
 * @author williamdrew
 * 
 */
public class CommonsServiceImplTest {

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
	 * {@link com.csc.muthur.server.commons.internal.CommonsServiceImpl#findAvailablePort(int, int)}
	 * .
	 */
	@Test
	public void testFindAvailablePort() {

		CommonsService cs = new CommonsServiceImpl();

		int availablePort = cs.findAvailablePort(6000, 7000);

		assertTrue(availablePort != -1);
		assertTrue(availablePort == 6000);

		availablePort = cs.findAvailablePort(7000, 7000);

		assertTrue(availablePort != -1);
		assertTrue(availablePort == 7000);

		try {

			cs.findAvailablePort(7000, 6000);

			fail("Should have thrown exception for illegal port range.");

		} catch (IllegalArgumentException e) {
			// should be here
		}

	}

	@Test
	public void testNoPortsAvailable() {

		CommonsService cs = new CommonsServiceImpl();

		int port = 6000;

		ServerSocket ss = null;
		DatagramSocket ds = null;
		int availablePort = 0;

		try {
			ss = new ServerSocket(port);
			ss.setReuseAddress(true);
			ds = new DatagramSocket(port);
			ds.setReuseAddress(true);

			// should NOT find a port open
			availablePort = cs.findAvailablePort(port, port);

			assertTrue(availablePort == -1);

			// should find the next port available
			availablePort = cs.findAvailablePort(port, port + 1);

			assertTrue(availablePort == port + 1);

		} catch (IOException e) {
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

		// should find the previous used port now available
		availablePort = cs.findAvailablePort(port, port);

		assertTrue(availablePort == port);

	}

}
