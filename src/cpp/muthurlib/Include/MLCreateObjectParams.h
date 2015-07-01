//------------------------------------------------------------------------------
/*! \file	MLCreateObjectParams.h
//
//  Contains declaration of the CMLCreateObjectParams class
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
#if !defined(__ML_CREATE_OBJECT_PARAMS_H__)
#define __ML_CREATE_OBJECT_PARAMS_H__

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
//! \brief This class wraps the parameters used to create an object
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLCreateObjectParams : public CMLRequestParams
{
	protected:
	
		CMLDataObject*					m_pDataObject;				//!< The data being created,updated or deleted

	public:
			
										CMLCreateObjectParams(CMLDataObject* pDataObject = NULL);
										CMLCreateObjectParams(const CMLCreateObjectParams& rSource);
						
		virtual						   ~CMLCreateObjectParams();
		
		virtual CMLDataObject*			GetDataObject();
		virtual void					SetDataObject(CMLDataObject* pDataObject);

		//virtual bool					GetReceivePublishedData();
		//virtual void					SetReceivePublishedData(bool);

		virtual void					Copy(const CMLCreateObjectParams& rSource);
		virtual const CMLCreateObjectParams&	operator = (const CMLCreateObjectParams& rSource);

		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_CREATE_OBJ; }

		CMLPropMember*					GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool							SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_CREATE_OBJECT_PARAMS_H__)
