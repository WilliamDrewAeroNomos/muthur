//------------------------------------------------------------------------------
/*! \file	MLResumeFederationResponse.h
//
//  Contains declaration of the CMLResumeFederationResponse class
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
#if !defined(__ML_RESUME_FEDERATION_RESPONSE_H__)
#define __ML_RESUME_FEDERATION_RESPONSE_H__

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
//! \brief Class wrapper for Ambassador's response to a Resume Federation request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLResumeFederationResponse : public CMLEvent
{	
protected:
	bool							m_bResumeFedResponseAck; //!< returns true to accept the request or false telling the sender that you, the federate are not able to resume
	public:
			
											CMLResumeFederationResponse();
											CMLResumeFederationResponse(const CMLResumeFederationResponse& rSource);
						
		virtual							   ~CMLResumeFederationResponse();

		virtual bool					GetResumeFedResponseAck();
		virtual void					SetResumeFedResponseAck(bool);
		
		virtual void						Copy(const CMLResumeFederationResponse& rSource);
		virtual const CMLResumeFederationResponse&		operator = (const CMLResumeFederationResponse& rSource);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_RESUME_FED; }

		CMLPropMember*						GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_RESPONSE_H__)
