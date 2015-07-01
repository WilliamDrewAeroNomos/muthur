//------------------------------------------------------------------------------
/*! \file	MQConsumer.h
//
//  Contains declaration of the CMQConsumer class
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
#if !defined(__MQ_CONSUMER_H__)
#define __MQ_CONSUMER_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MQApi.h>


//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
class CMQWrapper; // the owner wrapper

//------------------------------------------------------------------------------
//! \brief Class used to implement an ActiveMQ consumer
//------------------------------------------------------------------------------
class CMQConsumer : public ExceptionListener, public MessageListener
{
	private:
	
		CMQWrapper*					m_pMQWrapper;
		int							m_iMQWrapperId;

		cms::Session*				m_pMQSession;
		cms::Destination*			m_pMQDestination;
		cms::MessageConsumer*		m_pMQConsumer;
		
		bool						m_bUseTopic;
		std::string					m_sDestinationName;

	public:

									CMQConsumer(CMQWrapper* pMQWrapper = NULL, int iMQWrapperId = -1);
		virtual					   ~CMQConsumer();
		
		cms::Destination*			GetMQDestination(){ return m_pMQDestination; }
		std::string					GetMQDestinationName(){ return m_sDestinationName; }
		void						SetMQWrapper(CMQWrapper* pMQWrapper, int iMQWrapperId);

		void						Close();
		bool 						Open(const char* pszDestinationName, bool bUseTopic = false);

		void 						onMessage( const Message* message );
		void 						onException( const CMSException& ex AMQCPP_UNUSED);

	private:

		void						ReleaseAll();
};


#endif // !defined(__MQ_CONSUMER_H__)
