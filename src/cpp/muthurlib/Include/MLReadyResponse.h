//------------------------------------------------------------------------------
/*! \file	MLReadyResponse.h
//
//  Contains declaration of the CMLReadyResponse class
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
#if !defined(__ML_READY_RESPONSE_H__)
#define __ML_READY_RESPONSE_H__

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
//! \brief Class wrapper for Ambassador's response to a Ready request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLReadyResponse : public CMLEvent
{	
protected:
	
	public:
			
											CMLReadyResponse();
											CMLReadyResponse(const CMLReadyResponse& rSource);
						
		virtual							   ~CMLReadyResponse();

		
		
		virtual void						Copy(const CMLReadyResponse& rSource);
		virtual const CMLReadyResponse&		operator = (const CMLReadyResponse& rSource);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_READY; }

		CMLPropMember*						GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_RESPONSE_H__)
