//------------------------------------------------------------------------------
/*! \file	MLUpdateObjectParams.h
//
//  Contains declaration of the CMLUpdateObjectParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-27-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_UPDATE_OBJECT_PARAMS_H__)
#define __ML_UPDATE_OBJECT_PARAMS_H__

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
//! \brief This class wraps the parameters used to update an object
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLUpdateObjectParams : public CMLRequestParams
{
	protected:
	
		CMLDataObject*					m_pDataObject;				//!< The data being created,updated or deleted

	public:
			
										CMLUpdateObjectParams(CMLDataObject* pDataObject = NULL);
										CMLUpdateObjectParams(const CMLUpdateObjectParams& rSource);
						
		virtual						   ~CMLUpdateObjectParams();
		
		virtual CMLDataObject*			GetDataObject();
		virtual void					SetDataObject(CMLDataObject* pDataObject);


		virtual void					Copy(const CMLUpdateObjectParams& rSource);
		virtual const CMLUpdateObjectParams&	operator = (const CMLUpdateObjectParams& rSource);

		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_UPDATE_OBJ; }

		CMLPropMember*					GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool							SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_UPDATE_OBJECT_PARAMS_H__)
