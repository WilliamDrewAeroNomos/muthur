//------------------------------------------------------------------------------
/*! \file	MLAddDataPubParams.h
//
//  Contains declaration of the CMLAddDataPubParams class
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
#if !defined(__ML_ADD_DATA_PUB_PARAMS_H__)
#define __ML_ADD_DATA_PUB_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
#include <MLRequestParams.h>
#include <MLDataEventCallback.h>

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
//! \brief This class wraps the parameters used notify the Ambassador which
//!		   data classes this federate will publish
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAddDataPubParams : public CMLRequestParams
{
	protected:
		
		CMLPtrList							m_apClasses;		//!< The collection of class names to be published
		PTRNODE								m_posClasses;		//!< Local iterator for Classes collection
	
	public:
			
											CMLAddDataPubParams(IMLDataEventCallback* pICallback = NULL);
											CMLAddDataPubParams(const CMLAddDataPubParams& rSource);
						
		virtual							   ~CMLAddDataPubParams();
		
		virtual void						Copy(const CMLAddDataPubParams& rSource);
		virtual const CMLAddDataPubParams&	operator = (const CMLAddDataPubParams& rSource);

		virtual const CMLPtrList&			GetClasses() const;
		virtual CMLPtrList&					GetClasses();
		virtual const char*					GetFirstClass();
		virtual const char*					GetNextClass();
		virtual const char*					AddClass(const char* pszClassName);
		virtual int							GetClassCount();

		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLRequestClass						GetClass(){ return ML_REQUEST_CLASS_ADD_DATA_PUB; }
};

#endif // !defined(__ML_ADD_DATA_PUB_PARAMS_H__)
