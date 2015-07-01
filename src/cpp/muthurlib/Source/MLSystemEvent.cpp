//------------------------------------------------------------------------------
/*! \file	MLSystemEvent.cpp
//
//  Contains the implementation of the CMLSystemEvent class
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

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLSystemEvent.h>
#include <MLAmbassador.h>
#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador	_theAmbassador;		//!< The singleton Ambassador instance
extern CReporter		_theReporter;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSystemEvent::CMLSystemEvent()
{
	m_ulIssuedTimestampMSecs = _theAmbassador.GetSysTimeMsecs();
	m_eEventType = ML_SYSTEM_EVENT_UNKOWN;
	m_bSucceeded = true;
	m_sErrorDescription = "";
	m_sTerminationReason = "";
	memset(m_szUUID, 0, sizeof(m_szUUID));
	memset(m_szEventName, 0, sizeof(m_szEventName));
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSystemEvent::CMLSystemEvent(const CMLSystemEvent& rSource)
{
	Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSystemEvent::~CMLSystemEvent()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::Copy(const CMLSystemEvent& rSource)
{
	m_bSucceeded = rSource.m_bSucceeded;
	m_eEventType = rSource.m_eEventType;
	m_ulIssuedTimestampMSecs = rSource.m_ulIssuedTimestampMSecs;
	m_fedRegHandle = rSource.m_fedExecHandle;
	m_fedExecHandle = rSource.m_fedRegHandle;
	m_sErrorDescription = rSource.m_sErrorDescription;
	m_sTerminationReason = rSource.m_sTerminationReason;
	
	lstrcpyn(m_szUUID, rSource.m_szUUID, sizeof(m_szUUID));
	lstrcpyn(m_szEventName, rSource.m_szEventName, sizeof(m_szEventName));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the error description (message)
//!
//!	Parameters:	None
//!
//!	Returns:	The error message supplied by Muthur
//------------------------------------------------------------------------------
char* CMLSystemEvent::GetErrorDescription()
{
	return (char*)m_sErrorDescription;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the system event name
//!
//!	Parameters:	None
//!
//!	Returns:	The event name used by Muthur
//------------------------------------------------------------------------------
char* CMLSystemEvent::GetEventName()
{
	return m_szEventName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the system event type
//!
//!	Parameters:	None
//!
//!	Returns:	The enumerated type identifier
//------------------------------------------------------------------------------
EMLSystemEventTypes CMLSystemEvent::GetEventType()
{
	if(m_eEventType != ML_SYSTEM_EVENT_UNKOWN)
		return m_eEventType;
	else
		return GetTypeFromName(m_szEventName);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLSystemEvent::GetFedExecHandle()
{
	return m_fedExecHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation registration handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLSystemEvent::GetFedRegHandle()
{
	return m_fedRegHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieves the success flags
//!
//!	Parameters:	None
//!
//!	Returns:	The success flag assigned by Muthur
//------------------------------------------------------------------------------
bool CMLSystemEvent::GetSucceeded()
{
	return m_bSucceeded;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the termination reason
//!
//!	Parameters:	None
//!
//!	Returns:	The termination reason supplied by Muthur
//------------------------------------------------------------------------------
char* CMLSystemEvent::GetTerminationReason()
{
	return (char*)m_sTerminationReason;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the associated system event type identifier
//!
//!	Parameters:	pszName - the event name
//!
//!	Returns:	The enumerated type identifier
//------------------------------------------------------------------------------
EMLSystemEventTypes CMLSystemEvent::GetTypeFromName(const char* pszName)
{
	EMLSystemEventTypes	eType = ML_SYSTEM_EVENT_UNKOWN;
	
	if((pszName != NULL) && (lstrlen(pszName) > 0))
	{
		if(lstrcmpi(pszName, ML_XML_SYSEVENT_NAME_TERMINATE_FED) == 0)
		{
			eType = ML_SYSTEM_TERMINATE_FEDERATION;
		}
		else if(lstrcmpi(pszName, ML_XML_SYSEVENT_NAME_RESET_REQUIRED) == 0)
		{
			eType = ML_SYSTEM_RESET_REQUIRED;
		}
		
	}
	
	return eType;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the unique id for this object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to this object
//------------------------------------------------------------------------------
char* CMLSystemEvent::GetUUID()
{
	return m_szUUID;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get an XML element descriptor that defines all 
//!				properties in the specified block
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!
//!	Returns:	The element used to construct the XML stream
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLSystemEvent::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:

			pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_CONTROL, "");

			pXmlBlock->AddChild("createTimeMSecs", m_ulIssuedTimestampMSecs);
			pXmlBlock->AddChild("success", m_bSucceeded);
			pXmlBlock->AddChild("eventSource", _theAmbassador.GetFederateName());
			pXmlBlock->AddChild("eventName", GetXMLName(szXmlValue, sizeof(szXmlValue)));
			pXmlBlock->AddChild("timeToLiveSecs", _theAmbassador.GetTimeOutSecs());
			pXmlBlock->AddChild("eventUUID", m_szUUID);
			pXmlBlock->AddChild("registrationHandle", _theAmbassador.GetFedRegHandle());

			break;
		
		case ML_XML_BLOCK_DATA:

			pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_DATA, "");
			pXmlBlock->AddChild("terminationReason", (char*)m_sTerminationReason);
			break;

		case ML_XML_BLOCK_ERROR:

			pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_ERROR, "");
			pXmlBlock->AddChild("errorDescription", (char*)m_sErrorDescription);

			break;

		default:
			
			_theReporter.Debug("", "GetXmlBlock", "Invalid XML Block Enumeration: #%d", eXmlBlock);
			if(pXmlBlock != NULL)
			{
				delete pXmlBlock;
				pXmlBlock = NULL;
			}
			break;

	}// switch(eXmlBlock)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the value of the XML name attribute for this
//!				event type.
//!
//!	Parameters:	\li pszName - the buffer in which to store the name
//!				\li iSize - the size of the specified buffer in bytes
//!
//!	Returns:	The buffer specified by the caller
//------------------------------------------------------------------------------
char* CMLSystemEvent::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);
	lstrcpy(pszName, "Unknown");	

	return pszName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a property member that defines the root node for
//!				an XML stream
//!
//!	Parameters:	None
//!
//!	Returns:	The member used to build the XML document's root node
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLSystemEvent::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;
	
	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	pXmlRoot->AddAttribute("type", "System");

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLSystemEvent& CMLSystemEvent::operator = (const CMLSystemEvent& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the error description
//!
//!	Parameters:	\li pszDescription - the descriptive text for the error
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetErrorDescription(char* pszDescription)
{
	if(pszDescription != NULL)
		m_sErrorDescription = pszDescription;
	else
		m_sErrorDescription = "";
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the event name
//!
//!	Parameters:	\li pszEventName - the name used to identify the event
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetEventName(char* pszEventName)
{
	if(pszEventName != NULL)
		lstrcpyn(m_szEventName, pszEventName, sizeof(m_szEventName));
	else
		memset(m_szEventName, 0, sizeof(m_szEventName));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the event type
//!
//!	Parameters:	\li eType - the enumerated type identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetEventType(EMLSystemEventTypes eType)
{
	m_eEventType = eType;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate execution handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetFedExecHandle(CMLHandle& rHandle)
{
	m_fedExecHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate registration handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetFedRegHandle(CMLHandle& rHandle)
{
	m_fedRegHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the flag to indicate the request succeeded
//!
//!	Parameters:	\li bSucceeded - true if the request succeeded
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetSucceeded(bool bSucceeded)
{
	m_bSucceeded = bSucceeded;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the termination reason
//!
//!	Parameters:	\li pszReason - the reason the terminate event is being fired
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetTerminationReason(char* pszReason)
{
	if(pszReason != NULL)
		m_sTerminationReason = pszReason;
	else
		m_sTerminationReason = "";
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the unique id for this object
//!
//!	Parameters:	\li pszUUID - the unique identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSystemEvent::SetUUID(char* pszUUID)
{
	if(pszUUID != NULL)
		lstrcpyn(m_szUUID, pszUUID, sizeof(m_szUUID));
	else
		memset(m_szUUID, 0, sizeof(m_szUUID));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of a class member using the specified
//!				XML element
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!				\li pMLPropMember - the element that contains the value
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLSystemEvent::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool bSuccessful = true;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:

			if(lstrcmpi("createTimeMSecs", pMLPropMember->GetName()) == 0)
			{
				_theReporter.Debug("CMLSystemEvent", "SetXmlValue", "pMLPropMember->GetName():%s", pMLPropMember->GetName());
				_theReporter.Debug("CMLSystemEvent", "SetXmlValue", "pMLPropMember->GetName():%s", pMLPropMember->GetValue());

				m_ulIssuedTimestampMSecs = pMLPropMember->AsUnsignedLong(); //(unsigned long)(pMLPropMember->AsLong());
			}
			else if(lstrcmpi("success", pMLPropMember->GetName()) == 0)
			{
				m_bSucceeded = pMLPropMember->AsBool();
			}
			else if(lstrcmpi("eventSource", pMLPropMember->GetName()) == 0)
			{
				// ignored for now - should always be Muthur
			}
			else if(lstrcmpi("eventName", pMLPropMember->GetName()) == 0)
			{
				lstrcpyn(m_szEventName, pMLPropMember->GetValue(), sizeof(m_szEventName));
				
				if(lstrlen(m_szEventName) > 0)
					m_eEventType = GetTypeFromName(m_szEventName);
			}
			else if(lstrcmpi("timeToLiveSecs", pMLPropMember->GetName()) == 0)
			{
				// ignored for now 
				bSuccessful = true;
			}
			else if(lstrcmpi("eventUUID", pMLPropMember->GetName()) == 0)
			{
				lstrcpyn(m_szUUID, pMLPropMember->GetValue(), sizeof(m_szUUID));
				bSuccessful = true;
			}
			else if(lstrcmpi("registrationHandle", pMLPropMember->GetName()) == 0)
			{
				// ignored for now - should match the FRH maintained by the Ambassador
				bSuccessful = true;
			}
			break;
		
		case ML_XML_BLOCK_DATA:

			if(lstrcmpi("terminationReason", pMLPropMember->GetName()) == 0)
			{
				m_sTerminationReason = pMLPropMember->GetValue();
			}
			break;

		case ML_XML_BLOCK_ERROR:

			if(lstrcmpi("errorDescription", pMLPropMember->GetName()) == 0)
			{
				m_sErrorDescription = pMLPropMember->GetValue();
			}
			break;

		default:
			
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
