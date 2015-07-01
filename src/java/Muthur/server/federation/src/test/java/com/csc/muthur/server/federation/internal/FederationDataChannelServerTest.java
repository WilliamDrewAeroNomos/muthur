/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.csc.muthur.server.commons.ChannelAttributes;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationDataChannelServer;
import com.csc.muthur.server.federation.datachannel.internal.MockDataChannelListenerFactory;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelServerImpl;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.CreateObjectRequest;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public class FederationDataChannelServerTest {

	private static ApplicationContext applicationContext;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		applicationContext =
				new ClassPathXmlApplicationContext(
						"/META-INF/spring/federation-data-event-context.xml");
		assert (applicationContext != null);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	private Semaphore semClientCompleted;

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

	@Test
	public void test() {

		ExecutorService federationDataChanneServerlPool =
				Executors.newCachedThreadPool();

		int portNumber = 42424;

		FederationDataChannelServer fdcs = new FederationDataChannelServerImpl();
		fdcs.setDataChannelListenerFactory(new MockDataChannelListenerFactory());
		fdcs.setPortNumber(42424);

		try {
			federationDataChanneServerlPool.execute(fdcs);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		semClientCompleted = new Semaphore(0);

		startClient("localhost", portNumber);

		try {
			if (!semClientCompleted.tryAcquire(10, TimeUnit.SECONDS)) {
				fail("Client failed to complete.");
			} else {
				System.out.println("Client released semaphore");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		fdcs.stop();

	}

	private void startClient(final String hostName, final int portNumber) {

		long lPayloadLength = 0;

		String tailNumber = "N485UA";
		String acid = "DAL333";

		try {

			Socket sckt = new Socket(hostName, portNumber);

			OutputStream os = sckt.getOutputStream();

			Aircraft sacd = null;

			try {
				sacd = new Aircraft(tailNumber, acid);
			} catch (MuthurException e) {
				fail(e.getLocalizedMessage());
			}

			// create a CreateObjectRequest

			CreateObjectRequest cor = new CreateObjectRequest();

			cor.setFederationExecutionHandle(UUID.randomUUID().toString());
			cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
			cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
			cor.setSourceOfEvent("NexSim");
			cor.setDataObject(sacd);

			String serializedObject = cor.serialize();

			lPayloadLength = serializedObject.length();

			StringBuffer sb = new StringBuffer();

			String strVersion = "2.2.0";
			sb.append(StringUtils.rightPad(strVersion,
					ChannelAttributes.VERSION_SIZE, " "));

			String strEventName = "CreateObjectRequest";
			sb.append(StringUtils.rightPad(strEventName,
					ChannelAttributes.EVENT_NAME_SIZE, " "));

			String correlationID = UUID.randomUUID().toString();
			sb.append(StringUtils.rightPad(correlationID,
					ChannelAttributes.CORRELATION_ID_SIZE, " "));

			String replyToQueueName = UUID.randomUUID().toString();
			sb.append(StringUtils.rightPad(replyToQueueName,
					ChannelAttributes.REPLY_TO_QUEUE_NAME_SIZE, " "));

			String format =
					String.format("%% %dd", ChannelAttributes.PAYLOAD_LENGTH_SIZE);
			sb.append(String.format(format, lPayloadLength));

			String controlBlockBuffer =
					StringUtils.rightPad(sb.toString(),
							ChannelAttributes.controlBlockSize(), " ");

			for (int outLoopCnt = 0; outLoopCnt < 5; outLoopCnt++) {

				StringBuffer fullMessage = new StringBuffer();
				fullMessage.append(controlBlockBuffer);
				fullMessage.append(serializedObject);

				os.write(fullMessage.toString().getBytes());

				System.err.println("Wrote [" + fullMessage + "] to socket.");

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
			}

		} catch (UnknownHostException e) {
			fail("Don't know about host " + hostName);
		} catch (IOException e) {
			fail("Couldn't get I/O for the connection to " + hostName);
		}

		semClientCompleted.release();
	}
}
