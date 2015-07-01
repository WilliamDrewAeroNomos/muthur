//------------------------------------------------------------------------------
/*! \file	MLDeleteObjectParams.h
//
//  Contains declaration of the CMLDeleteObjectParams class
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
#if !defined(__ML_DELETE_OBJECT_PARAMS_H__)
#define __ML_DELETE_OBJECT_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>
#include <MLDataObject.h>
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
//! \brief This class wraps the parameters used to delete an object
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLDeleteObjectParams : public CMLRequestParams
{
	protected:
	
		char							m_szDataObjUUID[ML_MAXLEN_UUID];			//!< Unique object identifier

	public:
			
										CMLDeleteObjectParams();
										CMLDeleteObjectParams(const CMLDeleteObjectParams& rSource);
						
		virtual						   ~CMLDeleteObjectParams();
		
		virtual char*					GetDataObjUUID();
		virtual void					SetDataObjUUID(char* pszUUID);


		virtual void					Copy(const CMLDeleteObjectParams& rSource);
		virtual const CMLDeleteObjectParams&	operator = (const CMLDeleteObjectParams& rSource);

		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_DELETE_OBJ; }

		CMLPropMember*					GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool							SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_DELETE_OBJECT_PARAMS_H__)
