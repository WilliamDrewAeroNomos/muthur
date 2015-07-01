//------------------------------------------------------------------------------
/*! \file	MLUpdateObjectResponse.h
//
//  Contains declaration of the CMLUpdateObjectResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-25-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_UPDATE_OBJECT_RESPONSE_H__)
#define __ML_UPDATE_OBJECT_RESPONSE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
#include <MLAircraftData.h>
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
//! \brief Class wrapper for Ambassador's response to a request to create an 
//!		   object
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLUpdateObjectResponse : public CMLEvent
{	
	protected:
		
		char								m_szDataObjectUUID[ML_MAXLEN_UUID];		//!< Unique object identifier
		
	public:
			
											CMLUpdateObjectResponse();
											CMLUpdateObjectResponse(const CMLUpdateObjectResponse& rSource);
						
		virtual							   ~CMLUpdateObjectResponse();

		virtual void						Copy(const CMLUpdateObjectResponse& rSource);
		virtual const CMLUpdateObjectResponse& operator = (const CMLUpdateObjectResponse& rSource);

		char*								GetDataObjectUUID();	
		void								SetDataObjectUUID(char* pszUUID);
		
		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_UPDATE_OBJECT; }
};

#endif // !defined(__ML_UPDATE_OBJECT_RESPONSE_H__)
