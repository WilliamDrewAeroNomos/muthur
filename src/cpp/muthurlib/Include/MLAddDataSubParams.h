//------------------------------------------------------------------------------
/*! \file	MLAddDataSubParams.h
//
//  Contains declaration of the CMLAddDataSubParams class
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
#if !defined(__ML_ADD_DATA_SUB_PARAMS_H__)
#define __ML_ADD_DATA_SUB_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>
#include <MLObjectEventCallback.h>
#include <MLString.h>
#include <MLObjectOwnershipRelinquishCallback.h>
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
//!		   data classes this federate will subscribe to
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAddDataSubParams : public CMLRequestParams
{
	protected:
		
		CMLPtrList								m_apClasses;							//!< The collection of class names to be subscribed to
		PTRNODE									m_posClasses;							//!< Local iterator for classes collection
		CMLString								m_subQueueName;							//!< Name of subscription queue
		IMLObjectEventCallback*					m_pIObjectEventCallback;				//!< Ambassador callback interface for object events
		char									m_szMuthurOwnershipEventQueueName[ML_MAXLEN_MUTHUR_OWNERSHIP_QUEUE_NAME];		//!< Name of muthur ownership event queue
		IMLObjectOwnershipRelinquishCallback*	m_pIObjOwnershipRelinquishEventCallback;										//!< Object Event Relinquish callback interface for object events	

	public:
			
															CMLAddDataSubParams(IMLObjectEventCallback* pICallback = NULL);
															CMLAddDataSubParams(const CMLAddDataSubParams& rSource);
						
		virtual												~CMLAddDataSubParams();
		
		virtual IMLObjectEventCallback*						GetObjectEventCallback();
		virtual char*										GetSubscriptionQueueName();
		
		virtual void										SetDataEventCallback(IMLObjectEventCallback* pICallback);
		virtual void										SetSubscriptionQueueName(char*);

		
		virtual IMLObjectOwnershipRelinquishCallback*		GetObjectOwnershipRelinquishEventCallback();
		virtual void										SetObjectOwnershipRelinquishEventCallback(IMLObjectOwnershipRelinquishCallback* pICallback);

		virtual char*										GetMuthurOwnershipEventQueueName() ;
		virtual void										SetMuthurOwnershipEventQueueName(char*);

		virtual void										Copy(const CMLAddDataSubParams& rSource);
		virtual const CMLAddDataSubParams&	operator = (const CMLAddDataSubParams& rSource);

		virtual const CMLPtrList&							GetClasses() const;
		virtual CMLPtrList&									GetClasses();
		virtual const char*									GetFirstClass();
		virtual const char*									GetNextClass();
		virtual const char*									AddClass(const char* pszClassName);
		virtual int											GetClassCount();

		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool								SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLRequestClass						GetClass(){ return ML_REQUEST_CLASS_ADD_DATA_SUB; }
};

#endif // !defined(__ML_ADD_DATA_SUB_PARAMS_H__)
