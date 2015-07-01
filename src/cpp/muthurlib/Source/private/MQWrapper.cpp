//------------------------------------------------------------------------------
/*! \file	MQWrapper.cpp
//
//  Contains the implementation of the CMQWrapper class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-06-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <windows.h>
#include <winuser.h>

#include <MQApi.h>			// ActiveMQ headers
#include <MQWrapper.h>
#include <MQConsumer.h>
#include <MQProducer.h>

#include <MLAmbassador.h>
#include <MLApi.h>
#include <MLRequestParams.h>
#include <Reporter.h>

#include <MMSystem.h> // Multimedia timer services

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador _theAmbassador; // The singleton instance of CMLAmbassador
extern CReporter _theReporter;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pMLAmbassador - the owner ambassador object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMQWrapper::CMQWrapper(CMLAmbassador* pMLAmbassador)
{
	m_pCMSConnectionFactory = NULL;
	m_pCMSConnection = NULL;
	m_pCMSSession = NULL;
	m_pMLAmbassador = NULL;
	m_bInitialized = false;
	m_iMuthurPort = ML_DEFAULT_MUTHUR_PORT;
	lstrcpy(m_szMuthurUrl, ML_DEFAULT_MUTHUR_URL);
	
	if(pMLAmbassador != NULL)
		SetAmbassador(pMLAmbassador);
 }

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMQWrapper::~CMQWrapper()
{
	Shutdown();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to create the connection to the CMS services
//!
//!	Parameters:	None
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMQWrapper::CreateConnection()
{
	bool bSuccessful = false;
	
	try
	{
		// Create the connection factory if necessary
		//
		// Note: We only do this once. Otherwise, ActiveMQ causes a fault when 
		//		 the current factory is destroyed prior to allocating a new factory
		if(m_pCMSConnectionFactory == NULL)
		{
			char szUrl[512];
			
			if(isdigit(m_szMuthurUrl[0]))
				sprintf_s(szUrl, sizeof(szUrl), "failover:(tcp://%s:%d)?startupMaxReconnectAttempts=1", m_szMuthurUrl, m_iMuthurPort);
			else
				sprintf_s(szUrl, sizeof(szUrl), "tcp://%s:%d", m_szMuthurUrl, m_iMuthurPort);
			
//lstrcpy(szUrl, "failover:(tcp://192.168.1.75:61616)?startupMaxReconnectAttempts=10");			
//lstrcpy(szUrl, "failover:(tcp://localhost:61616)?startupMaxReconnectAttempts=1?initialReconnectDelay=1");			
			m_pCMSConnectionFactory = ConnectionFactory::createCMSConnectionFactory(std::string(szUrl));
		}

		m_pCMSConnection = m_pCMSConnectionFactory->createConnection();
		m_pCMSConnection->setExceptionListener(this);

		m_pCMSConnection->start(); // attempt to start
		bSuccessful = true;
	}
	catch(CMSException& e) 
	{
	//MessageBox(NULL, e.getMessage().c_str(), "", MB_OK);
	}
	catch(...)
	{
	//MessageBox(NULL, "FAILED", "", MB_OK);
	}

	if((bSuccessful == false) && (m_pCMSConnection != NULL))
	{
		// Clean up
		if(m_pCMSConnection != NULL)
		{			
			delete m_pCMSConnection;
			m_pCMSConnection = NULL;
		}
	}
	
	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the pointer to the owner ambassador object
//!
//!	Parameters:	None
//!
//!	Returns:	The pointer to the current ambassador
//------------------------------------------------------------------------------
CMLAmbassador* CMQWrapper::GetAmbassador()
{
	return m_pMLAmbassador;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the active CMS session
//!
//!	Parameters:	None
//!
//!	Returns:	The pointer to the current session object
//------------------------------------------------------------------------------
cms::Session* CMQWrapper::GetCMSSession()
{
	return m_pCMSSession;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the queue where our data subscriptions
//!				will be sent
//!
//!	Parameters:	\li pszName - buffer in which to store the name
//!				\li iSize - the size of the buffer in characters
//!
//!	Returns:	The buffer passed by the caller
//------------------------------------------------------------------------------
char* CMQWrapper::GetDataSubQueueName(char* pszName, int iSize)
{
	if(GetInitialized() == true)
	{
		lstrcpyn(pszName, m_dataEventsConsumer.GetMQDestinationName().c_str(), iSize);
	}
	else
	{
		memset(pszName, 0, iSize);
	}
	
	return pszName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the queue where our muthur heartbeats
//!				will be sent
//!
//!	Parameters:	\li pszName - buffer in which to store the name
//!				\li iSize - the size of the buffer in characters
//!
//!	Returns:	The buffer passed by the caller
//------------------------------------------------------------------------------
char* CMQWrapper::GetMuthurHeartbeatQueueName(char* pszName, int iSize)
{
	if(GetInitialized() == true)
	{
		lstrcpyn(pszName, m_muthurHeartbeatConsumer.GetMQDestinationName().c_str(), iSize);
	}
	else
	{
		memset(pszName, 0, iSize);
	}
	
	return pszName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the queue where our muthur time management events
//!				will be sent
//!
//!	Parameters:	\li pszName - buffer in which to store the name
//!				\li iSize - the size of the buffer in characters
//!
//!	Returns:	The buffer passed by the caller
//------------------------------------------------------------------------------
char* CMQWrapper::GetMuthurOwnershipEventQueueName(char* pszName, int iSize)
{
	if(GetInitialized() == true)
	{
		lstrcpyn(pszName, m_muthurOwnershipEventConsumer.GetMQDestinationName().c_str(), iSize);
	}
	else
	{
		memset(pszName, 0, iSize);
	}
	
	return pszName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the queue where our muthur time management events
//!				will be sent
//!
//!	Parameters:	\li pszName - buffer in which to store the name
//!				\li iSize - the size of the buffer in characters
//!
//!	Returns:	The buffer passed by the caller
//------------------------------------------------------------------------------
char* CMQWrapper::GetMuthurTimeManagementQueueName(char* pszName, int iSize)
{
	if(GetInitialized() == true)
	{
		lstrcpyn(pszName, m_muthurTimeManagementConsumer.GetMQDestinationName().c_str(), iSize);
	}
	else
	{
		memset(pszName, 0, iSize);
	}
	
	return pszName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the communications have been intialized
//!
//!	Parameters:	None
//!
//!	Returns:	true if initialized
//------------------------------------------------------------------------------
bool CMQWrapper::GetInitialized()
{
	return m_bInitialized;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the port identifier of the Muthur host
//!
//!	Parameters:	None
//!
//!	Returns:	The port identifier on the Muthur host machine
//------------------------------------------------------------------------------
int CMQWrapper::GetMuthurPort()
{
	return m_iMuthurPort;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the URL of the Muthur host
//!
//!	Parameters:	None
//!
//!	Returns:	The URL to the Muthur host machine
//------------------------------------------------------------------------------
char* CMQWrapper::GetMuthurUrl()
{
	return m_szMuthurUrl;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to initialize the wrapper
//!
//!	Parameters:	\li ulWaitMsecs - time (ms) allowed to wait for a connection
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMQWrapper::Initialize(unsigned long ulWaitMsecs)
{
	bool			bConnected = false;
	unsigned long	ulNow = _theAmbassador.GetSysTimeMsecs();

	m_bInitialized = false;

	while(m_bInitialized == false)
	{
		// Make sure we have address and port for Muthur (should be same as broker)
		if(lstrlen(m_szMuthurUrl) == 0)
			lstrcpy(m_szMuthurUrl, ML_DEFAULT_MUTHUR_URL);
		if(m_iMuthurPort <= 0)
			m_iMuthurPort = ML_DEFAULT_MUTHUR_PORT;

		//	Open the connection to the ActiveMQ broker
		while(bConnected == false)
		{
			if((bConnected = CreateConnection()) == false)
			{
				//	Have we timed out?
				if((ulNow + ulWaitMsecs) < _theAmbassador.GetSysTimeMsecs())
				{
					break;
				}
			}
		}
		if(bConnected == false) // unable to connect to ActiveMQ
			break;
			
		// Create a Session
		m_pCMSSession = m_pCMSConnection->createSession(Session::AUTO_ACKNOWLEDGE);
		
		m_systemTopicConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_SYSTEM);		
		m_systemTopicConsumer.Open(MQ_AMBASSADOR_QUEUE_NAME, true);

		// Create the temporary queue for federation events sent by Muthur
		m_fedEventsConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_FED_EVENTS);		
		m_fedEventsConsumer.Open("", false);

		// Create the temporary queue for request responses sent by Muthur
		m_responseConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_RESPONSE);		
		m_responseConsumer.Open("", false);

		// Create the temporary queue for receiving object subscriptions
		m_dataEventsConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_DATA);		
		m_dataEventsConsumer.Open("", false);

		// Create the temporary queue for receiving muthur heartbeats
		m_muthurHeartbeatConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_MUTHUR_HEARTBEAT);		
		m_muthurHeartbeatConsumer.Open("", false);
		
		// Create the temporary queue for receiving time management
		m_muthurTimeManagementConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_MUTHUR_TIMEMANAGEMENT);		
		m_muthurTimeManagementConsumer.Open("", false);

		// Create the temporary queue for receiving ownership events
		m_muthurOwnershipEventConsumer.SetMQWrapper(this, MQ_CONSUMER_ID_MUTHUR_OWNERSHIPRELINQUISH);		
		m_muthurOwnershipEventConsumer.Open("", false);

		// Create the predefined queue for sending requests to Muthur
		m_requestProducer.SetMQWrapper(this);		
		m_requestProducer.SetResponseConsumer(&m_responseConsumer); // sets up auto-reply to response queue
		m_requestProducer.Open(MQ_AMBASSADOR_QUEUE_NAME, false);
		
		m_bInitialized = true; // it's all good...
	}
	
	return m_bInitialized;
	
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a consumer when it receives a message
//!
//!	Parameters:	\li iConsumerId - the ID assigned to the consumer 
//!				\li pszMessage - the message text
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQWrapper::OnConsumerMessage(int iConsumerId, const char* pszMessage)
{
	if(m_pMLAmbassador != NULL)
	{
		_theReporter.Debug("MQWrapper", "OnConsumerMessage" ,"Debug1 iConsumerId:%d", iConsumerId);
		//	Invoke the appropriate callback
		switch(iConsumerId)
		{
			case MQ_CONSUMER_ID_SYSTEM:
			
				m_pMLAmbassador->OnMQSysMessage(pszMessage);
				break;
				
			case MQ_CONSUMER_ID_RESPONSE:
			
				m_pMLAmbassador->OnMQResponse(pszMessage);
				break;
				
			case MQ_CONSUMER_ID_DATA:
			
				m_pMLAmbassador->OnMQObjectAction(pszMessage);
				break;
				
			case MQ_CONSUMER_ID_FED_EVENTS:
			
				m_pMLAmbassador->OnMQFedEvent(pszMessage);
				break;

			case MQ_CONSUMER_ID_MUTHUR_HEARTBEAT:
			//	_theReporter.Debug("MQWrapper", "OnConsumerMessage", "Calling m_pMLAmbassador->OnMQMuthurHeartbeat");
				m_pMLAmbassador->OnMQMuthurHeartbeat(pszMessage);
				break;

			case MQ_CONSUMER_ID_MUTHUR_TIMEMANAGEMENT:
				//_theReporter.Debug("MQWrapper", "OnConsumerMessage", "Calling m_pMLAmbassador->OnMQMuthurTimeManagementUpdate");
				m_pMLAmbassador->OnMQMuthurTimeManagementUpdate(pszMessage);
				break;

			case MQ_CONSUMER_ID_MUTHUR_OWNERSHIPRELINQUISH:
				_theReporter.Debug("MQWrapper", "OnConsumerMessage", "Calling m_pMLAmbassador->OnMQMuthurObjectOwnershipReliniquish");
				m_pMLAmbassador->OnMQMuthurObjectOwnershipReliniquish(pszMessage);
				break;

				
				
			default:
			
				assert(false);
				break;
				
		}
	
	}// if(m_pMLAmbassador != NULL)

}
 
//------------------------------------------------------------------------------
//!	Summary:	Called when an exception occurs
//!
//!	Parameters:	ex - the exception that was thrown
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQWrapper::onException(const CMSException& ex AMQCPP_UNUSED)
{
	if(m_pMLAmbassador != NULL)
		m_pMLAmbassador->OnMQException(ex.getMessage().c_str());
}

//------------------------------------------------------------------------------
//!	Summary:	Called to send a request to Muthur
//!
//!	Parameters:	\li pszRequest - the request text to be sent
//!	Parameters:	\li pszCorrelationID - the ID used to synchronize the response
//!				\li iReplyTo - identifier for reply to destination
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMQWrapper::SendRequest(const char* pszRequest, const char* pszCorrelationID, int iReplyTo)
{
	bool bSuccessful = false;
	
	assert(pszRequest != NULL);
	
	if((pszRequest != NULL) && (GetInitialized() == true))
	{
		
		if(iReplyTo == MQ_REPLY_TO_FED_EVENTS)
		{
			bSuccessful = m_requestProducer.Send(pszRequest, pszCorrelationID, 
												 MQ_FED_EVENTS_STRING_PROP_NAME, 
												 m_fedEventsConsumer.GetMQDestinationName().c_str());
		}
		else if(iReplyTo == MQ_REPLY_TO_DATA_EVENTS)
		{
			bSuccessful = m_requestProducer.Send(pszRequest, pszCorrelationID, 
												 MQ_DATA_EVENTS_STRING_PROP_NAME, 
												 m_dataEventsConsumer.GetMQDestinationName().c_str());
		}
		else
		{
			bSuccessful = m_requestProducer.Send(pszRequest, pszCorrelationID, "", "");
		}
	}
	
	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the owner Ambassador object
//!
//!	Parameters:	\li pMLAmbassador - pointer to the owner object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQWrapper::SetAmbassador(CMLAmbassador* pMLAmbassador)
{
	m_pMLAmbassador = pMLAmbassador;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the port identifier of the Muthur host
//!
//!	Parameters:	\li iPort - the port identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQWrapper::SetMuthurPort(int iPort)
{
	if(iPort > 0)
		m_iMuthurPort = iPort;
	else
		m_iMuthurPort = ML_DEFAULT_MUTHUR_PORT;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the URL of the Muthur host
//!
//!	Parameters:	\li pszUrl - the URL of the Muthur host machine
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQWrapper::SetMuthurUrl(char* pszUrl)
{
	if((pszUrl != NULL) && (lstrlen(pszUrl) > 0))
		lstrcpyn(m_szMuthurUrl, pszUrl, sizeof(m_szMuthurUrl));
	else
		lstrcpy(m_szMuthurUrl, ML_DEFAULT_MUTHUR_URL);
}

//------------------------------------------------------------------------------
//!	Summary:	Callback to shut down communications with ActiveMQ & Muthur
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQWrapper::Shutdown()
{
	m_bInitialized = false;
		
	// Close the CMS resources
	try { if(m_pCMSSession != NULL) m_pCMSSession->close(); }
	catch(...){}
	try { if(m_pCMSConnection != NULL) m_pCMSConnection->close(); }
	catch(...){}

	m_dataEventsConsumer.Close();
	m_muthurHeartbeatConsumer.Close();
	m_muthurTimeManagementConsumer.Close();
	m_muthurOwnershipEventConsumer.Close();
	m_fedEventsConsumer.Close();
	m_responseConsumer.Close();
	m_systemTopicConsumer.Close();
	m_requestProducer.Close();

	// Destroy the CMS resources
	try { if(m_pCMSSession != NULL) delete m_pCMSSession; }
	catch(...){}
	try { if(m_pCMSConnection != NULL) delete m_pCMSConnection; }
	catch(...){}
	
	m_pCMSSession = NULL;
	m_pCMSConnection = NULL;
}
 
/* JAVA INITIALIZATION

	 * @throws JMSException
	 * @throws UnknownHostException
	private void initializeMessaging() throws JMSException, UnknownHostException {

		LOG.info("Initializing messaging...");

		// connect to JMS server

		gConnection = getJMSConnection();

		gConnection.setExceptionListener(this);

		gConnection.start();

		// create session

		session = gConnection.createSession(transacted, Session.AUTO_ACKNOWLEDGE);

		// Data publication queue - all satisfied subscriptions are received here

		LOG.debug("Create data publication queue...");

		dataPublicationQueue = session.createTemporaryQueue();

		LOG.debug("Create consumer for data publication queue...");

		MessageConsumer dataPublicationQueueConsumer =
				session.createConsumer(dataPublicationQueue);
		dataPublicationQueueConsumer
				.setMessageListener(new DataPublicationHandler());

		// All system events are published to the system event topic...

		LOG.debug("Creating system event topic...");

		systemEventTopic =
				session.createTopic(MessagingConfiguration.getInstance()
						.getMutherEventQueueName());

		LOG.debug("Creating consumer for system event topic...");

		MessageConsumer systemTopicConsumer =
				session.createConsumer(systemEventTopic);
		systemTopicConsumer.setMessageListener(new SystemMessageHandler());

		// queue for federation level events published for this federate

		LOG.debug("Creating federation event queue...");

		federationEventQueue = session.createTemporaryQueue();

		LOG.debug("Creating consumer to receive federation level messages...");

		MessageConsumer federationEventQueueConsumer =
				session.createConsumer(federationEventQueue);
		federationEventQueueConsumer
				.setMessageListener(new FederationEventHandler());

		LOG.debug("Creating queue for federation responses...");

		requestResponseQueue = session.createTemporaryQueue();

		LOG.debug("Creating consumer to receive responses...");

		MessageConsumer replyQueueConsumer =
				session.createConsumer(requestResponseQueue);
		replyQueueConsumer.setMessageListener(new ResponseHandler());

		LOG.debug("Connecting to the event queue on Muthur...");

		eventQueue =
				session.createQueue(MessagingConfiguration.getInstance()
						.getMutherEventQueueName());

		LOG.debug("Creating a producer to send events to Muthur...");

		eventQueueProducer = session.createProducer(eventQueue);
		eventQueueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		LOG.info("Successfully initialized messaging.");
	}



*/