//------------------------------------------------------------------------------
/*! \file	MLRelinquishObjectOwnershipRequest.h
//
//  Contains declaration of the CMLRelinquishObjectOwnershipRequest class
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
#if !defined(__ML_RELINQUISH_OBJECT_OWNERSHIP_PARAMS_H__)
#define __ML_RELINQUISH_OBJECT_OWNERSHIP_PARAMS_H__

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
//! \brief This class wraps the parameters used to relinquish ownership of an object.
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLRelinquishObjectOwnershipRequest : public CMLRequestParams
{
	protected:
	
		char							m_szDataObjectUUID[ML_MAXLEN_UUID];			//!< Unique object identifier

	public:
			
										CMLRelinquishObjectOwnershipRequest();
										CMLRelinquishObjectOwnershipRequest(const CMLRelinquishObjectOwnershipRequest& rSource);
						
		virtual						   ~CMLRelinquishObjectOwnershipRequest();
		
		virtual char*					GetDataObjectUUID();
		virtual void					SetDataObjectUUID(char* pszUUID);

		virtual void					Copy(const CMLRelinquishObjectOwnershipRequest& rSource);
		virtual const CMLRelinquishObjectOwnershipRequest&	operator = (const CMLRelinquishObjectOwnershipRequest& rSource);

		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP; }

		CMLPropMember*					GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool							SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_RELINQUISH_OBJECT_OWNERSHIP_PARAMS_H__)
