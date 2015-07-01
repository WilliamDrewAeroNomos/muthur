//------------------------------------------------------------------------------
/*! \file	MLRequestParams.cpp
//
//  Contains the implementation of the CMLRequestParams class
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
#include <MLRequestParams.h>
#include <MLAmbassador.h>
#include <Reporter.h>
//#include <TiXmlStream.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
#define _DEBUG_DATAREQ 1
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
CReporter		_theReqReporter;	//!< The diagnostics / error reporter
extern CReporter		_theReporter;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRequestParams::CMLRequestParams()
{
	m_pIAmbassadorCallback = NULL;

	m_ulIssuedTimestampMSecs = _theAmbassador.GetSysTimeMsecs();
	m_ulExpireTimeMSecs = 0;
//	m_ulMuthurHeartbeatTimeoutTimeMSecs = 0;
	m_RequestType = ML_REQUEST_STANDARD;
	memset(m_pszEventName, 0, sizeof(m_pszEventName));
	CMLAmbassador::CreateUUID(m_szUUID, sizeof(m_szUUID));

	_theReqReporter.SetAttached(&_theReporter);
	_theReqReporter.SetModuleName("Muthur");
	_theReqReporter.SetErrorLogEnabled(true);	
	
	_theReqReporter.SetErrorFileSpec("C:\\RequestParams_error.txt");
	_theReqReporter.SetDebugFileSpec("C:\\RequestParams_debug.txt");

	#ifdef _DEBUG_DATAREQ
		_theReqReporter.SetDebugLogEnabled(true);
	#else
		_theReqReporter.SetDebugLogEnabled(false);
	#endif
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRequestParams::CMLRequestParams(const CMLRequestParams& rSource)
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
CMLRequestParams::~CMLRequestParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::Copy(const CMLRequestParams& rSource)
{
	m_pIAmbassadorCallback = rSource.m_pIAmbassadorCallback;
	m_ulIssuedTimestampMSecs = rSource.m_ulIssuedTimestampMSecs;
	m_ulExpireTimeMSecs = rSource.m_ulExpireTimeMSecs;
//	m_ulMuthurHeartbeatTimeoutTimeMSecs = rSource.m_ulMuthurHeartbeatTimeoutTimeMSecs;
	m_fedRegHandle = rSource.m_fedRegHandle;		
	m_fedExecModelHandle = rSource.m_fedExecModelHandle;		
	m_fedExecHandle = rSource.m_fedExecHandle;		
	m_RequestType = rSource.m_RequestType;
	lstrcpyn(m_pszEventName, rSource.m_pszEventName, sizeof(m_pszEventName));
	lstrcpyn(m_szUUID, rSource.m_szUUID, sizeof(m_szUUID));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the Ambassador callback interface
//!
//!	Parameters:	None
//!
//!	Returns:	The ambassador callback interface used by this federate
//------------------------------------------------------------------------------
IMLAmbassadorCallback* CMLRequestParams::GetAmbassadorCallback()
{
	return m_pIAmbassadorCallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the time this request will time out
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds
//------------------------------------------------------------------------------
unsigned long CMLRequestParams::GetExpireTimeMSecs()
{
	return m_ulExpireTimeMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLRequestParams::GetFedExecHandle()
{
	return m_fedExecHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the request type
//!
//!	Parameters:	None
//!
//!	Returns:	The request type
//------------------------------------------------------------------------------
EMLRequestTypeClass CMLRequestParams::GetRequestType()
{
	return m_RequestType;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the request type
//!
//!	Parameters:	The request type
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetRequestType(EMLRequestTypeClass e)
{
	m_RequestType = e;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution model
//!
//!	Parameters:	None
//!
//!	Returns:	The model obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLRequestParams::GetFedExecModelHandle()
{
	return m_fedExecModelHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation registration handle
//!
//!	Parameters:	None
//!
//!	Returns:	The model obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLRequestParams::GetFedRegHandle()
{
	return m_fedRegHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the time this request was issued
//!
//!	Parameters:	None
//!
//!	Returns:	The time issued in milliseconds
//------------------------------------------------------------------------------
unsigned long CMLRequestParams::GetIssuedTimestampMSecs()
{
	return m_ulIssuedTimestampMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the unique id for this object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to this object
//------------------------------------------------------------------------------
char* CMLRequestParams::GetUUID()
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
CMLPropMember* CMLRequestParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:

		pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_CONTROL, "");

		pXmlBlock->AddChild("createEventTimeMSecs", m_ulIssuedTimestampMSecs);
		pXmlBlock->AddChild("success", true);
		pXmlBlock->AddChild("eventSource", _theAmbassador.GetFederateName());
		pXmlBlock->AddChild("eventName", GetXMLName(szXmlValue, sizeof(szXmlValue)));
		pXmlBlock->AddChild("timeToLiveSecs", _theAmbassador.GetTimeOutSecs());
		pXmlBlock->AddChild("eventUUID", m_szUUID);

		if(_theAmbassador.GetFedRegHandle().IsValid() == true)
			pXmlBlock->AddChild("registrationHandle", _theAmbassador.GetFedRegHandle());

		//if(_theAmbassador.GetFedExecModelHandle().IsValid() == true)
		//	pXmlBlock->AddChild("federationExecutionModelHandle", _theAmbassador.GetFedExecModelHandle());

		if(_theAmbassador.GetFedExecHandle().IsValid() == true)
			pXmlBlock->AddChild("federationExecutionHandle", _theAmbassador.GetFedExecHandle());

		break;

	case ML_XML_BLOCK_DATA:

		pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_DATA, "");

		// No base class data in this block
		break;

	case ML_XML_BLOCK_ERROR:

		// Request classes don't have this block
		break;

	default:

		_theReqReporter.Debug("", "GetXmlBlock", "Invalid XML Block Enumeration: #%d", eXmlBlock);
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
char* CMLRequestParams::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);

	switch(GetClass())
	{
	case ML_REQUEST_CLASS_REGISTER:

		lstrcpyn(pszName, "FederateRegistrationRequest", iSize);
		break;

	case ML_REQUEST_CLASS_LIST_FEM:

		lstrcpyn(pszName, "ListFedExecModelsRequest", iSize);
		break;

	case ML_REQUEST_CLASS_JOIN_FED:

		lstrcpyn(pszName, "JoinFederationRequest", iSize);
		break;

	case ML_REQUEST_CLASS_ADD_DATA_SUB:

		lstrcpyn(pszName, "DataSubscriptionRequest", iSize);
		break;

	case ML_REQUEST_CLASS_CREATE_OBJ:

		lstrcpyn(pszName, "CreateObjectRequest", iSize);
		break;

	case ML_REQUEST_CLASS_DELETE_OBJ:

		lstrcpyn(pszName, "DeleteObjectRequest", iSize);
		break;

	case ML_REQUEST_CLASS_UPDATE_OBJ:

		lstrcpyn(pszName, "UpdateObjectRequest", iSize);
		break;

	case ML_REQUEST_CLASS_TRANSFER_OWNERSHIP:

		lstrcpyn(pszName, "TransferObjectOwnershipRequest", iSize);
		break;

	case ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP:

		lstrcpyn(pszName, "RelinquishObjectOwnershipRequest", iSize);
		break;

	case ML_REQUEST_CLASS_READY:

		lstrcpyn(pszName, "ReadyToRunRequest", iSize);
		break;
	
	case ML_REQUEST_CLASS_TERMINATE:

		lstrcpyn(pszName, "FederationTerminationEvent", iSize);
		break;

	case ML_REQUEST_START_FEDERATIONS:
		lstrcpyn(pszName, "StartFederationRequest", iSize);
		break;

	case ML_REQUEST_PAUSE_FEDERATIONS:
		lstrcpyn(pszName, "PauseFederationRequest", iSize);
		break;

	case ML_REQUEST_RESUME_FEDERATIONS:
		lstrcpyn(pszName, "ResumeFederationRequest", iSize);
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
CMLPropMember* CMLRequestParams::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;

	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");

	switch(m_RequestType)
	{
	case ML_REQUEST_STANDARD:
		pXmlRoot->AddAttribute("type", "Request");
		break;
	case ML_REQUEST_SYSTEM:
		pXmlRoot->AddAttribute("type", "System");
		break;
	case ML_REQUEST_DATA:
		pXmlRoot->AddAttribute("type", "Data");
		break;
	default:
		pXmlRoot->AddAttribute("type", "Request");
		break;
	}

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLRequestParams& CMLRequestParams::operator = (const CMLRequestParams& rSource)
{
	Copy(rSource);
	return *this;
}

//!	Summary:	Called to set the Ambassador callback interface
//!
//!	Parameters:	\li pICallback - the callback to be invoked by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetAmbassadorCallback(IMLAmbassadorCallback* pICallback)
{
	m_pIAmbassadorCallback = pICallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time the request times out
//!
//!	Parameters:	\li ulExpireTimeMSecs - the time in milliseconds
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetExpireTimeMSecs(unsigned long ulExpireTimeMSecs)
{
	m_ulExpireTimeMSecs = ulExpireTimeMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate execution handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetFedExecHandle(CMLHandle& rHandle)
{
	m_fedExecHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate execution model identifier
//!
//!	Parameters:	\li rHandle - the FEM handle returned by the administrator
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetFedExecModelHandle(CMLHandle& rHandle)
{
	m_fedExecModelHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time stamp when the object was created
//!
//!	Parameters:	\li rHandle - time stamp in milliseconds
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetFedRegHandle(CMLHandle& rHandle)
{
	m_fedRegHandle = rHandle;		
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the unique identifier for this object
//!
//!	Parameters:	\li pszUUID - the unique ID assigned to this object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetIssuedTimestampMSecs(unsigned long ulTimestampMSecs)
{
	m_ulIssuedTimestampMSecs = ulTimestampMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the unique id for this object
//!
//!	Parameters:	\li pszUUID - the unique identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRequestParams::SetUUID(char* pszUUID)
{
	if(pszUUID != NULL)
		lstrcpyn(m_szUUID, pszUUID, sizeof(m_szUUID));
	else
		memset(m_szUUID, 0, sizeof(m_szUUID));
}

char* CMLRequestParams::GetEventName() { return m_pszEventName; }

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of a class member using the specified
//!				XML element
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!				\li pMLPropMember - the element that contains the value
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLRequestParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool bSuccessful = false;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:

		if(lstrcmpi("createEventTimeMSecs", pMLPropMember->GetName()) == 0)
		{
			m_ulIssuedTimestampMSecs = pMLPropMember->AsUnsignedLong();//(unsigned long)(pMLPropMember->AsLong());
			bSuccessful = true;
		}
		else if(lstrcmpi("success", pMLPropMember->GetName()) == 0)
		{
			// ignored for now
			bSuccessful = true;
		}
		else if(lstrcmpi("eventSource", pMLPropMember->GetName()) == 0)
		{
			// ignored for now - should always be this federate
			bSuccessful = true;
		}
		else if(lstrcmpi("eventName", pMLPropMember->GetName()) == 0)
		{
			lstrcpyn(m_pszEventName, pMLPropMember->GetValue(), sizeof(m_pszEventName));
			bSuccessful = true;
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
			m_fedRegHandle.SetMuthurId(pMLPropMember->GetValue());
			bSuccessful = true;
		}
		else if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
			bSuccessful = true;
		}
		else if(lstrcmpi("federationExecutionModelHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecModelHandle.SetMuthurId(pMLPropMember->GetValue());
			bSuccessful = true;
		}
		break;

	case ML_XML_BLOCK_DATA:

		// No base class data in this block
		break;

	default:

		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}

