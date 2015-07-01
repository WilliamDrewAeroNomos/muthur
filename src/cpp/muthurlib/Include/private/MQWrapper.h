//------------------------------------------------------------------------------
/*! \file	MQWrapper.h
//
//  Contains declaration of the CMQWrapper class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__MQ_WRAPPER_H__)
#define __MQ_WRAPPER_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MQApi.h>
#include <MQProducer.h>
#include <MQConsumer.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

// Consumer identifiers
#define MQ_CONSUMER_ID_SYSTEM						 1
#define MQ_CONSUMER_ID_RESPONSE						 2
#define MQ_CONSUMER_ID_DATA							 3
#define MQ_CONSUMER_ID_FED_EVENTS					 4
#define MQ_CONSUMER_ID_MUTHUR_HEARTBEAT				 5
#define MQ_CONSUMER_ID_MUTHUR_TIMEMANAGEMENT		 6
#define MQ_CONSUMER_ID_MUTHUR_OWNERSHIPRELINQUISH	 7

// Reply to queue identifiers
#define MQ_REPLY_TO_DEFAULT				0
#define MQ_REPLY_TO_FED_EVENTS			1
#define MQ_REPLY_TO_DATA_EVENTS			2

// Predefined queue names
#define MQ_AMBASSADOR_QUEUE_NAME		"MUTHUR.EVENT.QUEUE"
#define MQ_SYSTEM_QUEUE_NAME			""
#define MQ_FED_EVENTS_STRING_PROP_NAME	"FEDERATION.EVENT.QUEUE.NAME"
#define MQ_DATA_EVENTS_STRING_PROP_NAME	"DATA.EVENT.QUEUE.NAME"

/*
	private final String MUTHER_EVENT_QUEUE_NAME = "MUTHUR.EVENT.QUEUE";
	private final String MUTHUR_SYSTEM_EVENT_TOPIC_NAME =
			"MUTHUR.SYSTEM.EVENT.TOPIC";
	private final String MUTHUR_DISPATCH_QUEUE_NAME = "MUTHUR.DISPATCH.QUEUE";

	// names of message properties
	//
	private final String DATA_EVENT_QUEUE_PROP_NAME = "DATA.EVENT.QUEUE.NAME";
	private final String FEDERATION_EVENT_QUEUE_PROP_NAME =
			"FEDERATION.EVENT.QUEUE.NAME";


*/

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
class CMLAmbassador;
class CMLRequestParams;

//------------------------------------------------------------------------------
//! \brief Class used to serialize CMLDataObject derived classes
//------------------------------------------------------------------------------
class CMQWrapper : public ExceptionListener
{
	private:
		
		cms::ConnectionFactory*	m_pCMSConnectionFactory;				//!< The CMS connection factory
		cms::Connection*		m_pCMSConnection;						//!< The active CMS connection
		cms::Session*			m_pCMSSession;							//!< The active CMS session
		
		CMLAmbassador*			m_pMLAmbassador;						//!< Pointer to the active Ambassador
		bool					m_bInitialized;							//!< Flag to indicate if interface is initialized
		char					m_szMuthurUrl[ML_MAXLEN_MUTHUR_URL];	//!< Url of the Muthur server
		int						m_iMuthurPort;							//!< Port used by Muthur server
	
		CMQConsumer				m_systemTopicConsumer;					//!< Predefined Topic monitored for system alerts
		CMQProducer				m_requestProducer;						//!< Predefined queue where requests are sent to Muthur
		CMQConsumer				m_responseConsumer;						//!< Temporary queue where Muthur submits request responses
		CMQConsumer				m_fedEventsConsumer;					//!< Temporary queue where Muthur submits federation events
		CMQConsumer				m_dataEventsConsumer;					//!< Temporary queue where data subscriptions come in
		CMQConsumer				m_muthurHeartbeatConsumer;				//!< Temporary queue where muthur heartbeats come in
		CMQConsumer				m_muthurTimeManagementConsumer;			//!< Temporary queue where muthur time management events come in
		CMQConsumer				m_muthurOwnershipEventConsumer;			//!< Temporary queue where muthur ownership events come in

	public:
			
								CMQWrapper(CMLAmbassador* pMLAmbassador = NULL);
		virtual				   ~CMQWrapper();
		
		CMLAmbassador*			GetAmbassador();
		cms::Session*			GetCMSSession();
		char*					GetMuthurUrl();
		int						GetMuthurPort();
		bool					GetInitialized();
		char*					GetDataSubQueueName(char* pszName, int iSize);
		char*					GetMuthurHeartbeatQueueName(char* pszName, int iSize);
		char*					GetMuthurTimeManagementQueueName(char* pszName, int iSize);
		char*					GetMuthurOwnershipEventQueueName(char* pszName, int iSize);
		
		void					SetAmbassador(CMLAmbassador* pMLAmbassador);
		void					SetMuthurUrl(char* pszUrl);
		void					SetMuthurPort(int iPort);

		bool					Initialize(unsigned long ulWaitMsecs);
		void					Shutdown();
		
		bool					SendRequest(const char* pszRequest, const char* pszCorrelationID, int iReplyTo);
		
		void					OnConsumerMessage(int iConsumerId, const char* pszMessage);
		void 					onException( const CMSException& ex AMQCPP_UNUSED);

	protected:

		bool					CreateConnection();
};

#endif // !defined(__MQ_WRAPPER_H__)
