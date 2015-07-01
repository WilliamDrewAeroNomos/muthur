//------------------------------------------------------------------------------
/*! \file	MQProducer.h
//
//  Contains declaration of the CMQProducer class
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
#if !defined(__MQ_PRODUCER_H__)
#define __MQ_PRODUCER_H__

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
class CMQWrapper;
class CMQConsumer;

//------------------------------------------------------------------------------
//! \brief Class used to implement an ActiveMQ producer
//------------------------------------------------------------------------------
class CMQProducer : public ExceptionListener
{
	private:

		CMQWrapper*					m_pMQWrapper;

		cms::Session*				m_pMQSession;
		cms::Destination*			m_pMQDestination;
		cms::MessageProducer*		m_pMQProducer;
		CMQConsumer*				m_pMQResponseConsumer;
		
		bool						m_bUseTopic;
		std::string					m_sDestinationName;
		
	public:

									CMQProducer();
		virtual					   ~CMQProducer();
		
		void						SetMQWrapper(CMQWrapper* pMQWrapper);
		void						SetResponseConsumer(CMQConsumer* pMQConsumer);
		
		void						Close();
		bool 						Open(const char* pszDestinationName, bool bUseTopic = false);
		bool						Send(const char* pszMessage, const char* pszCorrelationID, 
										 const char* pszStrPropName, const char* pszStrPropValue);

		void 						onException( const CMSException& ex AMQCPP_UNUSED);

	protected:

		void						ReleaseAll();
};


#endif // !defined(__MQ_PRODUCER_H__)
