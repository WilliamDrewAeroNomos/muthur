//------------------------------------------------------------------------------
/*! \file	MLEvent.cpp
//
//  Contains the implementation of the CMLEvent class
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
#include <MLEvent.h>
#include <MLAmbassador.h>
#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
extern CReporter		_theReporter;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLEvent::CMLEvent()
{
	m_ulIssuedTimestampMSecs = _theAmbassador.GetSysTimeMsecs();
	m_bSucceeded = true;
	m_sErrorDescription = "";
	memset(m_szEventName, 0, sizeof(m_szEventName));
	memset(m_szEventSource, 0, sizeof(m_szEventSource));
	memset(m_szUUID, 0, sizeof(m_szUUID));
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLEvent::CMLEvent(const CMLEvent& rSource)
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
CMLEvent::~CMLEvent()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLEvent::Copy(const CMLEvent& rSource)
{
	m_bSucceeded = rSource.m_bSucceeded;
	m_fedRegHandle = rSource.m_fedExecHandle;
	m_fedExecHandle = rSource.m_fedExecHandle;
	m_ulIssuedTimestampMSecs = rSource.m_ulIssuedTimestampMSecs;
	m_sErrorDescription = rSource.m_sErrorDescription;
	lstrcpyn(m_szUUID, rSource.m_szUUID, sizeof(m_szUUID));
	lstrcpyn(m_szEventName, rSource.m_szEventName, sizeof(m_szEventName));
	lstrcpyn(m_szEventSource, rSource.m_szEventSource, sizeof(m_szEventSource));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the error description (message)
//!
//!	Parameters:	None
//!
//!	Returns:	The error message supplied by Muthur
//------------------------------------------------------------------------------
char* CMLEvent::GetErrorDescription()
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
char* CMLEvent::GetEventName()
{
	return m_szEventName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the event source name
//!
//!	Parameters:	None
//!
//!	Returns:	The event source
//------------------------------------------------------------------------------
char* CMLEvent::GetEventSource()
{
	return m_szEventSource;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLEvent::GetFedExecHandle()
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
CMLHandle CMLEvent::GetFedRegHandle()
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
bool CMLEvent::GetSucceeded()
{
	return m_bSucceeded;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the unique id for this object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to this object
//------------------------------------------------------------------------------
char* CMLEvent::GetUUID()
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
CMLPropMember* CMLEvent::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:

			pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_CONTROL, "");

			pXmlBlock->AddChild("createEventTimeMSecs", m_ulIssuedTimestampMSecs);
			pXmlBlock->AddChild("success", m_bSucceeded);
			pXmlBlock->AddChild("eventName", GetXMLName(szXmlValue, sizeof(szXmlValue)));
			pXmlBlock->AddChild("timeToLiveSecs", _theAmbassador.GetTimeOutSecs());
			pXmlBlock->AddChild("eventUUID", m_szUUID);
			
			if(lstrlen(m_szEventSource) > 0)
				pXmlBlock->AddChild("eventSource", m_szEventSource);
			else
				pXmlBlock->AddChild("eventSource", _theAmbassador.GetFederateName());
			
			if(_theAmbassador.GetFedRegHandle().IsValid() == true)
				pXmlBlock->AddChild("registrationHandle", _theAmbassador.GetFedRegHandle());

			if(_theAmbassador.GetFedExecHandle().IsValid() == true)
				pXmlBlock->AddChild("federationExecutionHandle", _theAmbassador.GetFedExecHandle());

			break;
		
		case ML_XML_BLOCK_DATA:

			pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_DATA, "");

			// No base class data in this block
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
char* CMLEvent::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);
	
	switch(GetClass())
	{
		case ML_RESPONSE_CLASS_REGISTER:
		
			lstrcpyn(pszName, "FederateRegistrationResponse", iSize);
			break;
			
		case ML_RESPONSE_CLASS_LIST_FEM:
		
			lstrcpyn(pszName, "ListFedExecModelsResponse", iSize);
			break;
			
		case ML_RESPONSE_CLASS_JOIN_FED:
		
			lstrcpyn(pszName, "JoinFederationResponse", iSize);
			break;
			
		case ML_RESPONSE_CLASS_ADD_DATA_SUB:
		
			lstrcpyn(pszName, "DataSubscriptionResponse", iSize);
			break;
			
		case ML_RESPONSE_CLASS_CREATE_OBJECT:
		
			lstrcpyn(pszName, "CreateObjectResponse", iSize);
			break;

		case ML_RESPONSE_CLASS_UPDATE_OBJECT:
			lstrcpyn(pszName, "UpdateObjectResponse", iSize);
			break;
			
		case ML_RESPONSE_CLASS_DELETE_OBJECT:
			lstrcpyn(pszName, "DeleteObjectResponse", iSize);
			break;

		case ML_RESPONSE_CLASS_TRANSFER_OBJECT_OWNERSHIP:
			lstrcpyn(pszName, "TransferObjectOwnershipResponse", iSize);
			break;

		case ML_RESPONSE_CLASS_RELINQUISH_OBJECT_OWNERSHIP:
			lstrcpyn(pszName, "RelinquishObjectOwnershipResponse", iSize);
			break;

		case ML_RESPONSE_CLASS_READY:
		
			lstrcpyn(pszName, "ReadyToRunResponse", iSize);
			break;

		case ML_RESPONSE_CLASS_START_FED:
			lstrcpyn(pszName, "StartFederationResponse", iSize);	
			break;

		case ML_RESPONSE_CLASS_PAUSE_FED:
			lstrcpyn(pszName, "PauseFederationResponse", iSize);
			break;

		case ML_RESPONSE_CLASS_RESUME_FED:
			lstrcpyn(pszName, "ResumeFederationResponse", iSize);
			break;
			
		default:
		
			assert(0);
			pszName[0] = '\0';
			break;
			
	}

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
CMLPropMember* CMLEvent::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;
	
	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	pXmlRoot->AddAttribute("type", "Response");

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLEvent& CMLEvent::operator = (const CMLEvent& rSource)
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
void CMLEvent::SetErrorDescription(char* pszDescription)
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
void CMLEvent::SetEventName(char* pszEventName)
{
	if(pszEventName != NULL)
		lstrcpyn(m_szEventName, pszEventName, sizeof(m_szEventName));
	else
		memset(m_szEventName, 0, sizeof(m_szEventName));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the event source name
//!
//!	Parameters:	\li pszEventSource - the name used to identify the source 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLEvent::SetEventSource(char* pszEventSource)
{
	if(pszEventSource != NULL)
		lstrcpyn(m_szEventSource, pszEventSource, sizeof(m_szEventSource));
	else
		memset(m_szEventSource, 0, sizeof(m_szEventSource));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate execution handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLEvent::SetFedExecHandle(CMLHandle& rHandle)
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
void CMLEvent::SetFedRegHandle(CMLHandle& rHandle)
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
void CMLEvent::SetSucceeded(bool bSucceeded)
{
	m_bSucceeded = bSucceeded;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the unique id for this object
//!
//!	Parameters:	\li pszUUID - the unique identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLEvent::SetUUID(char* pszUUID)
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
bool CMLEvent::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool bSuccessful = true;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:

			if(lstrcmpi("createEventTimeMSecs", pMLPropMember->GetName()) == 0)
			{
				m_ulIssuedTimestampMSecs = pMLPropMember->AsUnsignedLong();//(unsigned long)(pMLPropMember->AsLong());
			}
			else if(lstrcmpi("success", pMLPropMember->GetName()) == 0)
			{
				m_bSucceeded = pMLPropMember->AsBool();
			}
			else if(lstrcmpi("eventSource", pMLPropMember->GetName()) == 0)
			{
				lstrcpyn(m_szEventSource, pMLPropMember->GetValue(), sizeof(m_szEventSource));
			}
			else if(lstrcmpi("eventName", pMLPropMember->GetName()) == 0)
			{
				lstrcpyn(m_szEventName, pMLPropMember->GetValue(), sizeof(m_szEventName));
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
				// Should match the FRH maintained by the Ambassador
				m_fedRegHandle.SetMuthurId(pMLPropMember->GetValue());
			}
			else if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
			{
				// Should match the FEH maintained by the Ambassador
				m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
			}
			break;
		
		case ML_XML_BLOCK_DATA:

			// No base class data in this block
			break;

		case ML_XML_BLOCK_ERROR:

			if(lstrcmpi("errorDescription", pMLPropMember->GetName()) == 0)
			{
				m_sErrorDescription = pMLPropMember->GetValue();
				bSuccessful = true;
			}
			break;

		default:
			
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
