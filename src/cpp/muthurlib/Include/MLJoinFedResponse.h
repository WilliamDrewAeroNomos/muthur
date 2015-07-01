//------------------------------------------------------------------------------
/*! \file	MLJoinFedResponse.h
//
//  Contains declaration of the CMLJoinFedResponse class
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
#if !defined(__ML_JOIN_FED_RESPONSE_H__)
#define __ML_JOIN_FED_RESPONSE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
#include <MLHandle.h>

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
//! \brief This class wraps the Ambassador's response to a JoinFed request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLJoinFedResponse : public CMLEvent
{
	protected:
	
		CMLHandle							m_fedExecHandle;	//!< Federate execution handle used to access data stores

	public:
			
											CMLJoinFedResponse();
											CMLJoinFedResponse(const CMLJoinFedResponse& rSource);
						
		virtual							   ~CMLJoinFedResponse();
		
		virtual CMLHandle					GetFedExecHandle();
		virtual void						SetFedExecHandle(CMLHandle& rHandle);

		virtual void						Copy(const CMLJoinFedResponse& rSource);
		virtual const CMLJoinFedResponse&	operator = (const CMLJoinFedResponse& rSource);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_JOIN_FED; }

		CMLPropMember*						GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_JOIN_FED_RESPONSE_H__)
