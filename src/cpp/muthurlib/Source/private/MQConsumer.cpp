//------------------------------------------------------------------------------
/*! \file	MQConsumer.cpp
//
//  Contains the implementation of the CMQConsumer class
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
#include <Windows.h>
#include <MQApi.h>			// ActiveMQ headers
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
//!	Parameters:	\li pMQWrapper - the wrapper object that owns this consumer
//!				\li iMQWrapperId - the wrapper assigned numeric identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMQConsumer::CMQConsumer(CMQWrapper* pMQWrapper, int iMQWrapperId)
{
	m_pMQWrapper = NULL;
	m_pMQSession = NULL;
	m_pMQDestination = NULL;
	m_pMQConsumer = NULL;
	
	m_bUseTopic = false;
	m_iMQWrapperId = 0;
	m_sDestinationName = "";
	
	SetMQWrapper(pMQWrapper, iMQWrapperId);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMQConsumer::~CMQConsumer()
{
	ReleaseAll();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to close the consumer's ActiveMQ connection
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQConsumer::Close()
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
void CMQConsumer::onException(const CMSException& ex AMQCPP_UNUSED)
{
	_theReporter.Critical("", "onException", "consumer <%s> : Ex: %s", m_sDestinationName.c_str(), ex.getMessage().c_str());
	//ex.printStackTrace();
}

//------------------------------------------------------------------------------
//!	Summary:	Called when a new message arrives
//!
//!	Parameters:	pMessage - the message to be processed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQConsumer::onMessage(const Message* pMessage)
{
	try
	{
		/*_theReporter.Debug("MQConsumer", "onMessage", "Debug1 pMessage is NULL:%d?", 
			pMessage == NULL);*/
		const TextMessage* ptxtMessage = dynamic_cast< const TextMessage* >(pMessage);

		if(ptxtMessage != NULL ) 
		{
			/*_theReporter.Debug("MQConsumer", "onMessage", "Debug2 pMessage is NULL:%d?", 
			pMessage == NULL);*/
			if(m_pMQWrapper != NULL)
			{
				/*_theReporter.Debug("MQConsumer", "onMessage", "Debug3 pMessage is NULL:%d?", 
					pMessage == NULL);*/
				m_pMQWrapper->OnConsumerMessage(m_iMQWrapperId, ptxtMessage->getText().c_str());
			}

		} 

	} 
	catch (CMSException& e) 
	{		
		//e.printStackTrace();
	}

	// Commit all messages.
	if(m_pMQSession->isTransacted())
	{
		m_pMQSession->commit();
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to open the connection to the ActiveMQ broker
//!
//!	Parameters:	\li pszDestinationName - The name of the destination queue/topic
//!				\li bUseTopic - true to use topic destination instead of queue
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMQConsumer::Open(const char* pszDestinationName, bool bUseTopic) 
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

			// Create a MessageConsumer from the Session to the Topic or Queue
			m_pMQConsumer = m_pMQSession->createConsumer(m_pMQDestination);
			m_pMQConsumer->setMessageListener( this );
			
			bSuccessful = true;
		
		}// if(m_pMQSession != NULL)

	}
	catch(CMSException& e) 
	{
		_theReporter.Fatal("", "Open", "consumer <%s> : Ex: %s", m_sDestinationName.c_str(), e.getMessage().c_str());
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
void CMQConsumer::ReleaseAll()
{	
	if(m_pMQConsumer != NULL)
	{
		try { if(m_pMQConsumer != NULL) delete m_pMQConsumer; }
		catch(...){}
		try { if(m_pMQDestination != NULL) delete m_pMQDestination; }
		catch(...){}
	}
	
	m_pMQDestination = NULL;
	m_pMQConsumer = NULL;
	m_pMQSession = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the link to the owner MQ wrapper object
//!
//!	Parameters:	\li pMQWrapper - the wrapper object that owns this consumer
//!				\li iMQWrapperId - the wrapper assigned numeric identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMQConsumer::SetMQWrapper(CMQWrapper* pMQWrapper, int iMQWrapperId)
{
	m_pMQWrapper = pMQWrapper;
	m_iMQWrapperId = iMQWrapperId;
}

