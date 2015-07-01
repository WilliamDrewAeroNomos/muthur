//------------------------------------------------------------------------------
/*! \file	MLAddDataPubResponse.h
//
//  Contains declaration of the CMLAddDataPubResponse class
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
#if !defined(__ML_ADD_DATA_PUB_RESPONSE_H__)
#define __ML_ADD_DATA_PUB_RESPONSE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
#
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
//! \brief Class wrapper for Ambassador's response to a request to add data
//!		   publications
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAddDataPubResponse : public CMLEvent
{	
	protected:
		
		CMLPtrList							m_apClasses;			//!< The collection of class names being published
		PTRNODE								m_posClasses;			//!< Local iterator for Classes collection
		CMLHandle							m_fedExecModelHandle;	//!< The federation execution model handle
	public:
			
											CMLAddDataPubResponse();
											CMLAddDataPubResponse(const CMLAddDataPubResponse& rSource);
						
		virtual							   ~CMLAddDataPubResponse();
		virtual CMLHandle					GetFedExecModelHandle();
		virtual void						SetFedExecModelHandle(CMLHandle& rHandle);

		virtual void						Copy(const CMLAddDataPubResponse& rSource);
		virtual const CMLAddDataPubResponse& operator = (const CMLAddDataPubResponse& rSource);

		virtual const CMLPtrList&			GetClasses() const;
		virtual CMLPtrList&					GetClasses();
		virtual const char*					GetFirstClass();
		virtual const char*					GetNextClass();
		virtual const char*					AddClass(const char* pszClassName);
		virtual int							GetClassCount();

		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_ADD_DATA_PUB; }
};

#endif // !defined(__ML_ADD_DATA_PUB_RESPONSE_H__)
