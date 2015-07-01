//------------------------------------------------------------------------------
/*! \file	MLRegisterResponse.h
//
//  Contains declaration of the CMLRegisterResponse class
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
#if !defined(__ML_REGISTER_RESPONSE_H__)
#define __ML_REGISTER_RESPONSE_H__

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
//! \brief Class wrapper for Ambassador's response to a Registration request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLRegisterResponse : public CMLEvent
{
	protected:
	
		CMLHandle							m_fedRegHandle;	//!< Federate registration handle used to identify the federate

	public:
			
											CMLRegisterResponse();
											CMLRegisterResponse(const CMLRegisterResponse& rSource);
						
		virtual							   ~CMLRegisterResponse();
		
		virtual CMLHandle					GetFedRegHandle();
		virtual void						SetFedRegHandle(CMLHandle& rHandle);

		virtual void						Copy(const CMLRegisterResponse& rSource);
		virtual const CMLRegisterResponse&	operator = (const CMLRegisterResponse& rSource);

		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool						SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_REGISTER; }
};

#endif // !defined(__ML_REGISTER_RESPONSE_H__)
