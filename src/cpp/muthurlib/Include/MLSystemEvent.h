//------------------------------------------------------------------------------
/*! \file	MLSystemEvent.h
//
//  Contains declaration of the CMLSystemEvent class
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
#if !defined(__ML_SYSTEM_EVENT_H__)
#define __ML_SYSTEM_EVENT_H__

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
class MUTHURLIB_API CMLSystemEvent : public IMLSerialize
{
protected:

	char							m_szEventName[ML_MAXLEN_EVENT_NAME];	//!< The name of the event
	EMLSystemEventTypes				m_eEventType;					//!< The enumerated event type identifier
	CMLString						m_sErrorDescription;			//!< The error message if event request fails
	CMLString						m_sTerminationReason;			//!< The reason for federation termination
	CMLHandle						m_fedRegHandle;					//!< Federation registration handle returned in response to Register() 
	CMLHandle						m_fedExecHandle;				//!< Federation execution handle returned in response to JoinFederation() 
	unsigned long					m_ulIssuedTimestampMSecs;		//!< Time when the request was issued
	char							m_szUUID[ML_MAXLEN_UUID];		//!< Unique object identifier
	bool							m_bSucceeded;					//!< True if the request succeeded

public:

									CMLSystemEvent();
									CMLSystemEvent(const CMLSystemEvent& rSource);

	virtual						   ~CMLSystemEvent();

																																												
	virtual char*					GetEventName();
	virtual EMLSystemEventTypes		GetEventType();
	virtual char*					GetUUID();
	virtual char*					GetErrorDescription();
	virtual char*					GetTerminationReason();
	virtual bool					GetSucceeded();
	virtual CMLHandle				GetFedRegHandle();
	virtual CMLHandle				GetFedExecHandle();
	virtual EMLSystemEventTypes		GetTypeFromName(const char* pszName);
	
	virtual void					SetEventName(char* pszEventName);
	virtual void					SetEventType(EMLSystemEventTypes eType);
	virtual void					SetUUID(char* pszUUID);
	virtual void					SetErrorDescription(char* pszDescription);
	virtual void					SetTerminationReason(char* pszReason);
	virtual void					SetSucceeded(bool bSucceeded);
	virtual void					SetFedRegHandle(CMLHandle& rHandle);
	virtual void					SetFedExecHandle(CMLHandle& rHandle);

	virtual void					Copy(const CMLSystemEvent& rSource);
	virtual const CMLSystemEvent&	operator = (const CMLSystemEvent& rSource);

	//	Support for ISerialize interface
	virtual char*					GetXMLName(char* pszName, int iSize);
	virtual CMLPropMember*			GetXmlRoot();
	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_SYSTEM_EVENT_H__)
