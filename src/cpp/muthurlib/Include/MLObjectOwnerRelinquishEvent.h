//------------------------------------------------------------------------------
/*! \file	MLObjectOwnerRelinquishEvent.h
//
//  Contains declaration of the CMLObjectOwnerRelinquishEvent class
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
#if !defined(__ML_OBJECT_OWNERSHIP_RELINQUISH_EVENT_H__)
#define __ML_OBJECT_OWNERSHIP_RELINQUISH_EVENT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
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
//! \brief This class wraps the information associated with an object ownership relinquish 
//!			event.
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLObjectOwnerRelinquishEvent : public IMLSerialize
{
	protected:
		char						m_szDataObjectUUID[ML_MAXLEN_UUID];		//!< Unique object identifier

	public:

									CMLObjectOwnerRelinquishEvent();
									CMLObjectOwnerRelinquishEvent(const CMLObjectOwnerRelinquishEvent& rSource);

		virtual					   ~CMLObjectOwnerRelinquishEvent();

		virtual void				Copy(const CMLObjectOwnerRelinquishEvent& rSource);
		virtual const CMLObjectOwnerRelinquishEvent&	operator = (const CMLObjectOwnerRelinquishEvent& rSource);

		virtual char*				GetDataObjectUUID();				//!< Get UUID of object to be deleted	
		virtual void				SetDataObjectUUID(char* pszUUID);	//!< Set UUID of object to be deleted

		//	Support for ISerialize interface
		virtual char*				GetXMLName(char* pszName, int iSize);
		virtual CMLPropMember*		GetXmlRoot();
		virtual CMLPropMember*		GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool				SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_OBJECT_OWNERSHIP_RELINQUISH_EVENT_H__)
