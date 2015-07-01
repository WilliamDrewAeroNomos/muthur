//------------------------------------------------------------------------------
/*! \file	MLPauseFederationResponse.h
//
//  Contains declaration of the CMLPauseFederationResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>12-10-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_PAUSE_FEDERATION_RESPONSE_H__)
#define __ML_PAUSE_FEDERATION_RESPONSE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class wrapper for Ambassador's response to a Pause Federation request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLPauseFederationResponse : public CMLEvent
{	
protected:
	bool							m_bPauseFedResponseAck; //!< returns true to accept the request or false telling the sender that you, the federate are not able to pause 
	public:
			
											CMLPauseFederationResponse();
											CMLPauseFederationResponse(const CMLPauseFederationResponse& rSource);
						
		virtual							   ~CMLPauseFederationResponse();

		virtual bool					GetPauseFedResponseAck();
		virtual void					SetPauseFedResponseAck(bool);
		
		virtual void						Copy(const CMLPauseFederationResponse& rSource);
		virtual const CMLPauseFederationResponse&		operator = (const CMLPauseFederationResponse& rSource);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_PAUSE_FED; }

		CMLPropMember*						GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_RESPONSE_H__)
