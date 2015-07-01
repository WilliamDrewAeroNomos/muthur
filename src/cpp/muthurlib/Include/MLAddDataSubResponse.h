//------------------------------------------------------------------------------
/*! \file	MLAddDataSubResponse.h
//
//  Contains declaration of the CMLAddDataSubResponse class
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
#if !defined(__ML_ADD_DATA_SUB_RESPONSE_H__)
#define __ML_ADD_DATA_SUB_RESPONSE_H__

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
//! \brief Class wrapper for Ambassador's response to a request to add data
//!		   subscriptions
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAddDataSubResponse : public CMLEvent
{	
	protected:
	
		int										m_iSubClassCount;	//!< The number of classes successfully subscribed

	public:
			
												CMLAddDataSubResponse();
												CMLAddDataSubResponse(const CMLAddDataSubResponse& rSource);
						
		virtual									~CMLAddDataSubResponse();
		
		virtual int								GetSubClassCount();
		virtual void							SetSubClassCount(int iCount);

		virtual void							Copy(const CMLAddDataSubResponse& rSource);
		virtual const CMLAddDataSubResponse&	operator = (const CMLAddDataSubResponse& rSource);

		EMLResponseClass						GetClass(){ return ML_RESPONSE_CLASS_ADD_DATA_SUB; }

		CMLPropMember*						GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_ADD_DATA_SUB_RESPONSE_H__)
