//------------------------------------------------------------------------------
/*! \file	MLEvent.h
//
//  Contains declaration of the CMLEvent class
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
#if !defined(__ML_EVENT_H__)
#define __ML_EVENT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLPropMember.h>
#include <MLSerialize.h>
#include <MLString.h>

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
//! \brief Base class for all derived Ambassador response classes
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLEvent : public IMLSerialize
{
protected:

	char						m_szEventName[ML_MAXLEN_EVENT_NAME];		//!< The name of the event
	char						m_szEventSource[ML_MAXLEN_SOURCE];	//!< The name of the event source
	CMLString					m_sErrorDescription;				//!< The error message if event request fails
	CMLHandle					m_fedRegHandle;						//!< Federation registration handle returned in response to Register() 
	CMLHandle					m_fedExecHandle;					//!< Federation execution handle returned in response to JoinFederation() 
	unsigned long				m_ulIssuedTimestampMSecs;			//!< Time when the request was issued
	char						m_szUUID[ML_MAXLEN_UUID];			//!< Unique object identifier
	bool						m_bSucceeded;						//!< True if the request succeeded

public:

								CMLEvent();
								CMLEvent(const CMLEvent& rSource);

	virtual					   ~CMLEvent();


	virtual void				Copy(const CMLEvent& rSource);
	virtual const CMLEvent&		operator = (const CMLEvent& rSource);

	virtual EMLResponseClass	GetClass() = 0;	//!< Must be overridden by derived classes

	virtual char*				GetEventName();
	virtual char*				GetEventSource();
	virtual char*				GetUUID();
	virtual char*				GetErrorDescription();
	virtual bool				GetSucceeded();
	virtual CMLHandle			GetFedRegHandle();
	virtual CMLHandle			GetFedExecHandle();
	
	virtual void				SetEventName(char* pszEventName);
	virtual void				SetEventSource(char* pszEventName);
	virtual void				SetUUID(char* pszUUID);
	virtual void				SetErrorDescription(char* pszDescription);
	virtual void				SetSucceeded(bool bSucceeded);
	virtual void				SetFedRegHandle(CMLHandle& rHandle);
	virtual void				SetFedExecHandle(CMLHandle& rHandle);

	//	Support for ISerialize interface
	virtual char*				GetXMLName(char* pszName, int iSize);
	virtual CMLPropMember*		GetXmlRoot();
	virtual CMLPropMember*		GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool				SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_EVENT_H__)
