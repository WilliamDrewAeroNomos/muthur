//------------------------------------------------------------------------------
/*! \file	MLDeleteObjectResponse.h
//
//  Contains declaration of the CMLDeleteObjectResponse class
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
#if !defined(__ML_DELETE_OBJECT_RESPONSE_H__)
#define __ML_DELETE_OBJECT_RESPONSE_H__

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
class MUTHURLIB_API CMLDeleteObjectResponse : public CMLEvent
{	
	protected:
		
		char								m_szDataObjectUUID[ML_MAXLEN_UUID];	//!< Unique object identifier
		
	public:
			
											CMLDeleteObjectResponse();
											CMLDeleteObjectResponse(const CMLDeleteObjectResponse& rSource);
						
		virtual							   ~CMLDeleteObjectResponse();

		virtual void						Copy(const CMLDeleteObjectResponse& rSource);
		virtual const CMLDeleteObjectResponse& operator = (const CMLDeleteObjectResponse& rSource);

		char*								GetDataObjectUUID();				//!< Get UUID of object to be deleted	
		void								SetDataObjectUUID(char* pszUUID);	//!< Set UUID of object to be deleted
		
		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_DELETE_OBJECT; }
};

#endif // !defined(__ML_DELETE_OBJECT_RESPONSE_H__)
