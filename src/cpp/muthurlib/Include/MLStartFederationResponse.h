//------------------------------------------------------------------------------
/*! \file	MLReadyResponse.h
//
//  Contains declaration of the CMLStartFederationResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>12-11-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_START_FEDERATION_RESPONSE_H__)
#define __ML_START_FEDERATION_RESPONSE_H__

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
//! \brief Class wrapper for Ambassador's response to a Start Federation request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLStartFederationResponse : public CMLEvent
{	
protected:
	bool							m_bStartFedResponseAck; //!< returns true to accept the request or false telling the sender that you, the federate are not able to start
	public:
			
											CMLStartFederationResponse();
											CMLStartFederationResponse(const CMLStartFederationResponse& rSource);
						
		virtual							   ~CMLStartFederationResponse();

		virtual bool					GetStartFedResponseAck();
		virtual void					SetStartFedResponseAck(bool);
		
		virtual void						Copy(const CMLStartFederationResponse& rSource);
		virtual const CMLStartFederationResponse&		operator = (const CMLStartFederationResponse& rSource);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_START_FED; }

		CMLPropMember*						GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_RESPONSE_H__)
