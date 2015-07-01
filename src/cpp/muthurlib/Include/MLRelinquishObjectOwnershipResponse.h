//------------------------------------------------------------------------------
/*! \file	MLRelinquishObjectOwnershipResponse.h
//
//  Contains declaration of the CMLRelinquishObjectOwnershipResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-29-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_RELINQUISH_OBJECT_OWNERSHIP_RESPONSE_H__)
#define __ML_RELINQUISH_OBJECT_OWNERSHIP_RESPONSE_H__

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
//! \brief Class wrapper for Ambassador's response to a request to transfer  
//!		   ownership of an object.
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLRelinquishObjectOwnershipResponse : public CMLEvent
{	
	protected:
		
		char								m_szDataObjectUUID[ML_MAXLEN_UUID];								//!< Unique object identifier
		
	public:
			
											CMLRelinquishObjectOwnershipResponse();
											CMLRelinquishObjectOwnershipResponse(const CMLRelinquishObjectOwnershipResponse& rSource);
						
		virtual							   ~CMLRelinquishObjectOwnershipResponse();

		virtual void						Copy(const CMLRelinquishObjectOwnershipResponse& rSource);
		virtual const CMLRelinquishObjectOwnershipResponse& operator = (const CMLRelinquishObjectOwnershipResponse& rSource);

		char*								GetDataObjectUUID();		
	
		void								SetDataObjectUUID(char* pszUUID);
		
		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_RELINQUISH_OBJECT_OWNERSHIP; }
};

#endif // !defined(__ML_TRANSFER_OBJECT_OWNERSHIP_RESPONSE_H__)
