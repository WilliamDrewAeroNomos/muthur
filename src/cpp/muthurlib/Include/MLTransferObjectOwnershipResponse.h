//------------------------------------------------------------------------------
/*! \file	MLTransferObjectOwnershipResponse.h
//
//  Contains declaration of the CMLTransferObjectOwnershipResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-28-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_TRANSFER_OBJECT_OWNERSHIP_RESPONSE_H__)
#define __ML_TRANSFER_OBJECT_OWNERSHIP_RESPONSE_H__

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
class MUTHURLIB_API CMLTransferObjectOwnershipResponse : public CMLEvent
{	
	protected:
		
		char								m_szDataObjectUUID[ML_MAXLEN_UUID];								//!< Unique object identifier
		
	public:
			
											CMLTransferObjectOwnershipResponse();
											CMLTransferObjectOwnershipResponse(const CMLTransferObjectOwnershipResponse& rSource);
						
		virtual							   ~CMLTransferObjectOwnershipResponse();

		virtual void						Copy(const CMLTransferObjectOwnershipResponse& rSource);
		virtual const CMLTransferObjectOwnershipResponse& operator = (const CMLTransferObjectOwnershipResponse& rSource);

		char*								GetDataObjectUUID();		
	
		void								SetDataObjectUUID(char* pszUUID);
		
		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_TRANSFER_OBJECT_OWNERSHIP; }
};

#endif // !defined(__ML_TRANSFER_OBJECT_OWNERSHIP_RESPONSE_H__)
