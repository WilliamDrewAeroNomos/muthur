//------------------------------------------------------------------------------
/*! \file	MLObjectEvent.h
//
//  Contains declaration of the CMLObjectEvent class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>08-02-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_OBJECT_EVENT_H__)
#define __ML_OBJECT_EVENT_H__

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
//! \brief This class wraps the information associated with a object event
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLObjectEvent : public IMLSerialize
{
	protected:

		char						m_szEventName[ML_MAXLEN_EVENT_NAME];	//!< The name of the event
		char						m_szEventSource[ML_MAXLEN_SOURCE];		//!< The name of the event source
		CMLHandle					m_fedRegHandle;							//!< Federation registration handle returned in response to Register() 
		CMLHandle					m_fedExecHandle;						//!< Federation execution handle returned in response to JoinFederation() 
		CMLHandle					m_fedExecModelHandle;					//!< Federation execution model handle returned in response to ListFEM() 
		unsigned long				m_ulIssuedTimestampMSecs;				//!< Time when the request was issued
		char						m_szUUID[ML_MAXLEN_UUID];				//!< Unique event identifier
		bool						m_bSucceeded;							//!< True if the request succeeded
		CMLDataObject*				m_pDataObject;							//!< The data object associated with the event
		CMLPropMember*				m_xmlDataObject;						//!< XML tree that represents the data object
		CMLPropMember*				m_xmlDataObjectType;					//!< XML tree that represents the data object
		char						m_szDataAction[ML_MAXLEN_DATA_ACTION];	//!< The data action (add,update or delete)
	public:

									CMLObjectEvent();
									CMLObjectEvent(const CMLObjectEvent& rSource);

		virtual					   ~CMLObjectEvent();

		virtual void				Copy(const CMLObjectEvent& rSource);
		virtual const CMLObjectEvent&	operator = (const CMLObjectEvent& rSource);

		virtual char*				GetEventName();
		virtual char*				GetEventSource();
		virtual char*				GetDataAction();
		virtual char*				GetUUID();
		virtual CMLPropMember* 		GetXMLDataObject();
		virtual CMLPropMember* 		GetXMLDataObjectType();
		virtual bool				GetSucceeded();
		virtual CMLHandle			GetFedRegHandle();
		virtual CMLHandle			GetFedExecHandle();
		virtual CMLHandle			GetFedExecModelHandle();
		virtual CMLDataObject*		GetDataObject();

		virtual void				SetEventName(char* pszEventName);
		virtual void				SetEventSource(char* pszEventName);
		virtual void				SetDataAction(char* pszAction);
		virtual void				SetUUID(char* pszUUID);
		virtual void				SetSucceeded(bool bSucceeded);
		virtual void				SetFedRegHandle(CMLHandle& rHandle);
		virtual void				SetFedExecHandle(CMLHandle& rHandle);
		virtual void				SetFedExecModelHandle(CMLHandle& rHandle);
		
		virtual bool				LoadXMLDataObject();

		//	Support for ISerialize interface
		virtual char*				GetXMLName(char* pszName, int iSize);
		virtual CMLPropMember*		GetXmlRoot();
		virtual CMLPropMember*		GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool				SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_OBJECT_EVENT_H__)
