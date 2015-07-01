//------------------------------------------------------------------------------
/*! \file	MLTransferObjectOwnershipRequest.h
//
//  Contains declaration of the CMLTransferObjectOwnershipParams class
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
#if !defined(__ML_TRANSFER_OBJECT_OWNERSHIP_PARAMS_H__)
#define __ML_TRANSFER_OBJECT_OWNERSHIP_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>
#include <MLDataObject.h>


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
//! \brief This class wraps the parameters used to transfer ownership of an object.
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLTransferObjectOwnershipParams : public CMLRequestParams
{
	protected:
	
		char							m_szDataObjectUUID[ML_MAXLEN_UUID];			//!< Unique object identifier

	public:
			
										CMLTransferObjectOwnershipParams();
										CMLTransferObjectOwnershipParams(const CMLTransferObjectOwnershipParams& rSource);
						
		virtual						   ~CMLTransferObjectOwnershipParams();
		
		virtual char*					GetDataObjectUUID();
		virtual void					SetDataObjectUUID(char* pszUUID);

		virtual void					Copy(const CMLTransferObjectOwnershipParams& rSource);
		virtual const CMLTransferObjectOwnershipParams&	operator = (const CMLTransferObjectOwnershipParams& rSource);

		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_TRANSFER_OWNERSHIP; }

		CMLPropMember*					GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool							SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_TRANSFER_OBJECT_OWNERSHIP_PARAMS_H__)
