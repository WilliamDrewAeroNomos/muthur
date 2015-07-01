//------------------------------------------------------------------------------
/*! \file	MQProducer.cpp
//
//  Contains the implementation of the CMQProducer class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-09-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
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
#include <MQApi.h>			// ActiveMQ headers
#include <MQProducer.h>
#include <MQConsumer.h>
#include <MQWrapper.h>

#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CReporter _theReporter; //!< The global diagnostics / error reporter


//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMQProducer::CMQProducer()
{
	m_pMQWrapper = NULL;
	m_pMQSession = NULL;
	m_pMQDestination = NULL;
	m_pMQProducer = NULL;
	m_pMQResponseConsumer = NULL;	
	
	m_bUseTopic = false;
	m_sDestinationName = "";
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMQProducer::~CMQProducer()
{
	ReleaseAll();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to close the connection
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQProducer::Close()
{
	m_bUseTopic = false;
	m_sDestinationName = "";
	
	ReleaseAll();
}

//------------------------------------------------------------------------------
//!	Summary:	Called when an exception occurs
//!
//!	Parameters:	ex - the exception that was thrown
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQProducer::onException(const CMSException& ex AMQCPP_UNUSED)
{
	_theReporter.Critical("", "onException", "producer <%s> : Ex: %s", m_sDestinationName.c_str(), ex.getMessage().c_str());
	//ex.printStackTrace();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to open the connection to the ActiveMQ broker
//!
//!	Parameters:	\li pszDestinationName - The name of the destination queue/topic
//!				\li bUseTopic - true to use topic destination instead of queue
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMQProducer::Open(const char* pszDestinationName, bool bUseTopic) 
{
	bool bSuccessful = false;
	
	try 
	{
		m_sDestinationName = pszDestinationName;
		m_bUseTopic = bUseTopic;
				
		//	Make sure we have an active session
		if(m_pMQWrapper != NULL)
		{
			m_pMQSession = m_pMQWrapper->GetCMSSession();
		}
		
		if(m_pMQSession != NULL)
		{
			// Create the destination
			if(m_bUseTopic == true) 
			{
				if(m_sDestinationName.length() == 0)
				{
					m_pMQDestination = m_pMQSession->createTemporaryTopic();
					m_sDestinationName = ((TemporaryTopic*)(m_pMQDestination))->getTopicName();
				}
				else
				{
					m_pMQDestination = m_pMQSession->createTopic(m_sDestinationName);
				}
			} 
			else 
			{
				if(m_sDestinationName.length() == 0)
				{
					m_pMQDestination = m_pMQSession->createTemporaryQueue();
					m_sDestinationName = ((TemporaryQueue*)(m_pMQDestination))->getQueueName();
				}
				else
				{
					m_pMQDestination = m_pMQSession->createQueue(m_sDestinationName);
				}
			}

		_theReporter.Debug("", "Open", "producer <%s> : creating producer", pszDestinationName);

			// Create a MessageProducer from the Session to the Topic or Queue
			m_pMQProducer = m_pMQSession->createProducer(m_pMQDestination);
			m_pMQProducer->setDeliveryMode(DeliveryMode::NON_PERSISTENT);
			
			bSuccessful = true;
		
		}

	}
	catch(CMSException& e) 
	{
		_theReporter.Fatal("", "Open", "producer <%s> : Ex: %s", m_sDestinationName.c_str(), e.getMessage().c_str());
		//e.printStackTrace();
	}
	
	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to release all MQ resources
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQProducer::ReleaseAll()
{	
	if(m_pMQProducer != NULL)
	{
		try
		{
			m_pMQProducer->close();
			delete m_pMQProducer;
		}
		catch(...){}
	}
	
	if(m_pMQDestination != NULL)
	{
		try { delete m_pMQDestination; }
		catch(...){}
	}

	m_pMQDestination = NULL;
	m_pMQProducer = NULL;
	m_pMQSession = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to send the requested message
//!
//!	Parameters:	\li pszMessage - the message to be sent
//!				\li pszCorrelationID - the id used to manage the response
//!				\li pszStrPropName - optional string property name added to request
//!				\li pszStrpropValue - optional string property value added to request
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMQProducer::Send(const char* pszMessage, const char* pszCorrelationID, 
					   const char* pszStrPropName, const char* pszStrPropValue) 
{
	TextMessage*	ptxtMessage = NULL;
	std::string		sMessage;
	bool			bSuccessful = false;
	
	try 
	{
		if((m_pMQSession != NULL) && (m_pMQProducer != NULL) && (pszMessage != NULL))
		{
			if((ptxtMessage = m_pMQSession->createTextMessage(std::string(pszMessage))) != NULL)
			{
				if((m_pMQResponseConsumer != NULL) && (m_pMQResponseConsumer->GetMQDestination() != NULL))
				{
					ptxtMessage->setCMSReplyTo(m_pMQResponseConsumer->GetMQDestination());
				}
				
				if((pszCorrelationID != NULL) && (lstrlen(pszCorrelationID) > 0))
				{
					ptxtMessage->setCMSCorrelationID(std::string(pszCorrelationID));
				}
				
				if((pszStrPropName != NULL) && (pszStrPropValue != NULL) && (lstrlen(pszStrPropName) > 0))
				{
					ptxtMessage->setStringProperty(std::string(pszStrPropName), std::string(pszStrPropValue));
				}
			
                m_pMQProducer->send(ptxtMessage);
                delete ptxtMessage;
		
				bSuccessful = true;		
			}
		}

	}
	catch(CMSException& e) 
	{
		_theReporter.Debug("", "Send", "producer <%s> : Ex: %s", m_sDestinationName.c_str(), e.getMessage().c_str());
		
		if(ptxtMessage != NULL)
			delete ptxtMessage;
	}
	
	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the link to the owner MQ wrapper object
//!
//!	Parameters:	\li pMQWrapper - the wrapper object that owns this consumer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQProducer::SetMQWrapper(CMQWrapper* pMQWrapper)
{
	m_pMQWrapper = pMQWrapper;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the consumer to which responses should be sent
//!
//!	Parameters:	\li pMQConsumer - the consumer destination
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQProducer::SetResponseConsumer(CMQConsumer* pMQConsumer)
{
	m_pMQResponseConsumer = pMQConsumer;
}

