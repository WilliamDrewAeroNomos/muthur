//------------------------------------------------------------------------------
/*! \file	MLAmbassador.cpp
//
//  Contains the implementation of the CMLAmbassador class
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
#include <MLAmbassador.h>
#include <MLEvent.h>
#include <MLAmbassadorCallback.h>
#include <MLRegisterResponse.h>
#include <MLListFEMResponse.h>
#include <MLJoinFedResponse.h>
#include <MLStartFederationResponse.h>
#include <MLPauseFederationRequest.h>
#include <MLResumeFederationRequest.h>
#include <MLObjectEvent.h>
#include <MLTimeManagementEvent.h>
#include <MLObjectOwnerRelinquishEvent.h>
#include <MLException.h>
#include <MLHelper.h>
#include <MLSystemEvent.h>
#include <MLSystemCallback.h>

#include <MQWrapper.h>		// Wrapper class for ActiveMQ-CPP services

#include <Reporter.h>

#include <Objbase.h>
#include <MMSystem.h> // Multimedia timer services
//
//#include <iostream>
//#include <fstream>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
//#define _DEBUG 1   //Uncomment this line to enable debugging
CReporter			_theReporter;	//!< The diagnostics / error reporter for the ambassador
CMLAmbassador		_theAmbassador;	//!< The singleton Ambassador instance

//------------------------------------------------------------------------------
//!	Summary:	Callback for Window's multimedia timer messages
//!
//!	Parameters:	\li uTimeID - event identifier 
//!				\li dwUser - user supplied value when the event was created
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CALLBACK AmbassadorTimerCallback(UINT uTimerID, UINT uReserved, DWORD dwUser, DWORD, DWORD)
{
	//	Forward the event to the global Ambassador
	_theAmbassador.OnMMTimer(uTimerID, dwUser);
}


//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAmbassador::CMLAmbassador()
{
	activemq::library::ActiveMQCPP::initializeLibrary(); // Initialize ActiveMQCpp libraries

	m_bEnableLogging = false;
	m_bEnableLogging = false;
	/*Logger *LOG = Logger::getLogger(typeid(this).name());

	if(LOG)
	{
	cout << "LOG not Null" << endl;
	LOG->debug("MLAmbassador.cpp", 1, "CMLAmbassador", typeid(this).name());
	}
	else
	{
	cout << "LOG is Null!!" << endl;
	}*/
	//LOG->debug("MLAmbassador", 999, "GetInstance", "TEST MESSAGE");
	//Logger copyLog = *LOG;
	//	LOGDECAF_DEBUG(copyLog, "TEST", true);
	//StreamHandler *streamHandler = new StreamHandler();

	//outputStream->
	//streamHandler->setOuputStream(
	//LOG->debug("MLAmbassador", 1, "CMLAmbassador", "TEST");
	/*decaf::util::logging::Level l = logger->getLevel();
	logger->setLevel(l.FINEST);*/


	m_pISystemCallback = NULL;
	m_pIObjectEventCallback = NULL;
	m_pITimeMgmtUpdateCallback = NULL;
	m_pIOwnershipRelinquishCallback = NULL;
	m_iTimeOutSecs = 30; // default is 30 seconds
	m_uCheckPendingTimerID = 0;
	m_fedRegHandle.SetMuthurId("waiting_registration");
	memset(m_szFederateName, 0, sizeof(m_szFederateName));
	memset(m_szMuthurHeartbeatQueueName, 0, sizeof(m_szMuthurHeartbeatQueueName));
	memset(m_szMuthurTimeMgmtQueueName, 0, sizeof(m_szMuthurTimeMgmtQueueName));
	m_iFrequencyOfBeats = 0;
	m_ulMuthurHeartbeatTimeoutTimeMSecs = GetSysTimeMsecs() + ML_AMBASSADOR_SYSTEM_TIMEOUT_MSECS;
	//m_ulCurrentSimTime = -1;
	//	Set the name used to identify this module
	_theReporter.SetModuleName("MUTHUR");
	_theReporter.SetErrorLogEnabled(true);

	_theReporter.SetErrorFileSpec("C:\\Muthur_error.txt");
	_theReporter.SetDebugFileSpec("C:\\Muthur_debug.txt");


	_theReporter.SetDebugLogEnabled(false);

}		

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAmbassador::~CMLAmbassador()
{
	Shutdown();

	activemq::library::ActiveMQCPP::shutdownLibrary();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to enable logging
//!
//!	Parameters:	\li bIsEnabled - indicates whether or not logging should be enabled
//!
//!	Returns:	N/A
//------------------------------------------------------------------------------
void CMLAmbassador::EnableLogging(bool bIsEnabled)
{
	m_bEnableLogging = bIsEnabled;
	/*if(bIsEnabled) logger->setLevel(logging::Level::DEBUG);
	else logger->setLevel(logging::Level::OFF);*/
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get if heartbeat is enabled
//!
//!	Parameters:	\li none
//!
//!	Returns:	bool indicating true if check for heartbeat is enabled and false if it is not
//------------------------------------------------------------------------------
bool CMLAmbassador::GetIsHeartbeatEnabled()
{
	return m_bIsHeartbeatEnabled;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get if heartbeat is enabled
//!
//!	Parameters:	\li none
//!
//!	Returns:	bool indicating true if check for heartbeat is enabled and false if it is not
//------------------------------------------------------------------------------
void CMLAmbassador::SetIsHeartbeatEnabled(bool b)
{
	m_bIsHeartbeatEnabled = b;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to create a new UUID
//!
//!	Parameters:	\li pszBuffer - buffer in which to store the UUID string
//!				\li iSize - the size of the buffer in bytes
//!
//!	Returns:	The buffer supplied by the caller
//------------------------------------------------------------------------------
char* CMLAmbassador::CreateUUID(char* pszBuffer, int iSize)
{
	OLECHAR		oleGUID[128];
	GUID		guid;
	int			i = 0;
	int			iLength = 0;

	memset(pszBuffer, 0, iSize); // initialize the buffer

	CoCreateGuid(&guid);

	//	Convert the GUID to an OLE string
	StringFromGUID2(guid, oleGUID, 128);

	//	Convert the OLE string to an ANSI string
	WideCharToMultiByte(CP_UTF8, 0, oleGUID, -1, pszBuffer, iSize, 0, 0);

	//	Strip the leading and trailing brackets
	if(lstrlen(pszBuffer) > 2)
	{
		if(pszBuffer[0] == '{')
			lstrcpy(pszBuffer, (pszBuffer + 1));
		if(pszBuffer[lstrlen(pszBuffer) - 1] == '}')
			pszBuffer[lstrlen(pszBuffer) - 1] = '\0';

		iLength = lstrlen(pszBuffer);
		for(i = 0; i < iLength; i++) // convert to lower case
			pszBuffer[i] = tolower(pszBuffer[i]);
	}

	return pszBuffer;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the federate bound to this ambassador
//!
//!	Parameters:	None
//!
//!	Returns:	The current federate name
//------------------------------------------------------------------------------
char* CMLAmbassador::GetFederateName()
{
	return m_szFederateName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the handle assigned to this federate by Muthur
//!
//!	Parameters:	None
//!
//!	Returns:	The federate registration handle assigned by Muthur
//------------------------------------------------------------------------------
CMLHandle CMLAmbassador::GetFedRegHandle()
{
	return m_fedRegHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the execution handle assigned by Muthur
//!
//!	Parameters:	None
//!
//!	Returns:	The federate execution handle assigned by Muthur
//------------------------------------------------------------------------------
CMLHandle CMLAmbassador::GetFedExecHandle()
{
	return m_fedExecHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the execution model selected for this session
//!
//!	Parameters:	None
//!
//!	Returns:	The federation execution model selected for this session
//------------------------------------------------------------------------------
CMLFedExecModel CMLAmbassador::GetFedExecModel()
{
	return m_fedExecModel;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the handle of the active execution model
//!
//!	Parameters:	None
//!
//!	Returns:	The handle of the active federation execution model
//------------------------------------------------------------------------------
CMLHandle CMLAmbassador::GetFedExecModelHandle()
{
	return m_fedExecModel.GetFedExecModelHandle();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get access to the singleton instance of this class
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAmbassador& CMLAmbassador::GetInstance()
{
	return _theAmbassador;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a response object to pass to the federate
//!
//!	Parameters:	\li pszEventName - the name that identifies the event
//!
//!	Returns:	A response object for the requested event
//------------------------------------------------------------------------------
CMLEvent* CMLAmbassador::GetResponseFromName(const char* pszEventName)
{
	CMLEvent* pResponse = NULL;

	if(lstrcmpi(pszEventName, "FederateRegistrationResponse") == 0)
	{
		pResponse = new CMLRegisterResponse();
	}
	else if(lstrcmpi(pszEventName, "ListFedExecModelsResponse") == 0)
	{
		pResponse = new CMLListFEMResponse();
	}
	else if(lstrcmpi(pszEventName, "JoinFederationResponse") == 0)
	{
		pResponse = new CMLJoinFedResponse();
	}
	else if(lstrcmpi(pszEventName, "CreateObjectResponse") == 0)
	{
		pResponse = new CMLCreateObjectResponse();
	}
	else if(lstrcmpi(pszEventName, "UpdateObjectResponse") == 0)
	{
		pResponse = new CMLUpdateObjectResponse();
	}
	else if(lstrcmpi(pszEventName, "DeleteObjectResponse") == 0)
	{
		pResponse = new CMLDeleteObjectResponse();
	}
	else if(lstrcmpi(pszEventName, "RelinquishObjectOwnershipResponse") == 0)
	{
		pResponse = new CMLRelinquishObjectOwnershipResponse();
	}
	else if(lstrcmpi(pszEventName, "TransferObjectOwnershipResponse") == 0)
	{
		pResponse = new CMLTransferObjectOwnershipResponse();
	}
	else if(lstrcmpi(pszEventName, "DataSubscriptionResponse") == 0)
	{
		pResponse = new CMLAddDataSubResponse();
	}
	else if(lstrcmpi(pszEventName, "ReadyToRunResponse") == 0)
	{
		pResponse = new CMLReadyResponse();
	}
	else if(lstrcmpi(pszEventName, "StartFederationResponse") == 0)
	{
		pResponse = new CMLStartFederationResponse();
	}
	else if(lstrcmpi(pszEventName, "PauseFederationResponse") == 0)
	{
		pResponse = new CMLPauseFederationResponse();
	}
	else if(lstrcmpi(pszEventName, "ResumeFederationResponse") == 0)
	{
		pResponse = new CMLResumeFederationResponse();
	}
	else
	{
		assert(false);
	}

	return pResponse;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a response object to pass to the federate
//!
//!	Parameters:	\li pszXmlStream - the XML stream sent from Muthur
//!
//!	Returns:	A populated response object
//------------------------------------------------------------------------------
CMLEvent* CMLAmbassador::GetResponseFromStream(const char* pszXmlStream)
{
	CMLPropMember*	pProps = NULL;
	CMLPropMember*	pCtrlBlock = NULL;
	CMLPropMember*	pChild = NULL;
	CMLEvent*		pResponse = NULL;

	//	Get the property tree for the object
	if((pProps = CMLHelper::GetPropTree((char*)pszXmlStream)) != NULL)
	{
		// First we have to locate the <controlBlock> node
		pCtrlBlock = pProps->GetFirstChild();
		while(pCtrlBlock != NULL)
		{
			if(lstrcmpi(ML_XML_BLOCK_NAME_CONTROL, pCtrlBlock->GetName()) == 0)
			{
				_theReporter.Debug("Ambassador", "GetResponseFromStream", "debug1; m_szFederateName:%s", m_szFederateName);
				// Search the control block children for the event type
				pChild = pCtrlBlock->GetFirstChild();
				while(pChild != NULL)
				{
					_theReporter.Debug("Ambassador", "GetResponseFromStream", "debug2; pChild->GetName():%s; m_szFederateName:%s", 
						pChild->GetName(), m_szFederateName);
					//	Is this the child we're looking for?
					if(lstrcmpi("eventName", pChild->GetName()) == 0)
					{
						_theReporter.Debug("Ambassador", "GetResponseFromStream", "debug3; m_szFederateName:%s", m_szFederateName);
						if((pResponse = GetResponseFromName(pChild->GetValue())) != NULL)
						{
							_theReporter.Debug("Ambassador", "GetResponseFromStream", "debug4; m_szFederateName:%s", m_szFederateName);
							CMLHelper::LoadPropTree(pResponse, pProps);
						}
						else
						{
							_theReporter.Debug("", "GetResponseFromStream", "Unable to locate response class named: <%s>", pChild->GetValue());
						}
						break;
					}
					else
					{					
						pChild = pCtrlBlock->GetNextChild();
					}
				}

				break; // done searching
			}
			else
			{
				pCtrlBlock = pProps->GetNextChild();
			}

		}// while(pCtrlBlock != NULL)	

		delete pProps;

	}// if((pProps = CMLHelper::GetPropTree((char*)pszXmlStream)) != NULL)

	return pResponse;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the current system time in milliseconds
//!
//!	Parameters:	None
//!
//!	Returns:	The current time in milliseconds
//------------------------------------------------------------------------------
unsigned long CMLAmbassador::GetSysTimeMsecs()
{
	return timeGetTime();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to time out value for the specified request
//!
//!	Parameters:	\li pRequest - the request being issued by the federate
//!
//!	Returns:	The appropriate time out value
//------------------------------------------------------------------------------
long CMLAmbassador::GetTimeOutMsec(CMLRequestParams* pRequest)
{
	return (this->GetTimeOutSecs() * 1000);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the time out value in seconds
//!
//!	Parameters:	None
//!
//!	Returns:	The current time out value
//------------------------------------------------------------------------------
int CMLAmbassador::GetTimeOutSecs()
{
	return m_iTimeOutSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add publications to data classes
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
//bool CMLAmbassador::AddDataPublications(CMLAddDataPubParams* pParams)
//{
//	bool bSuccessful = false;
//	
//	assert(pParams != NULL);
//
//	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
//	{
//		// Make sure we've set the correct FRH
//		pParams->SetFedRegHandle(m_fedRegHandle);
//		pParams->SetFedExecHandle(m_fedExecHandle);
//		pParams->SetFedExecModelHandle(GetFedExecModelHandle());
//	
//		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
//		{
//			bSuccessful = true;
//		}
//	}
//
//	return bSuccessful;
//}	

//------------------------------------------------------------------------------
//!	Summary:	Called to add subscriptions to data classes
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::AddDataSubscriptions(CMLAddDataSubParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];
	char	szMuthurOwnershipQueue[128];

	assert(pParams != NULL);
	assert(pParams->GetClassCount() > 0);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		//	Get the name of the queue for data subscriptions
		m_pMQWrapper->GetDataSubQueueName(szSubQueue, sizeof(szSubQueue));

		pParams->SetSubscriptionQueueName(szSubQueue);

		m_pIObjectEventCallback = pParams->GetObjectEventCallback();	

		m_pMQWrapper->GetMuthurOwnershipEventQueueName(szMuthurOwnershipQueue, sizeof(szMuthurOwnershipQueue));
		pParams->SetMuthurOwnershipEventQueueName(szMuthurOwnershipQueue);

		m_pIOwnershipRelinquishCallback = pParams->GetObjectOwnershipRelinquishEventCallback();

		assert(m_pIObjectEventCallback != NULL);

		if(SendRequest(pParams, MQ_REPLY_TO_DATA_EVENTS) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}	

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of pending requests
//!
//!	Parameters:	\li pRequest - the request being issued by the federate
//!
//!	Returns:	The entry added to the pending requests collection
//------------------------------------------------------------------------------
CMLRequestParams* CMLAmbassador::AddPending(CMLRequestParams* pRequest)
{
	CMLRequestParams* pPending = NULL;

	assert(pRequest != NULL);

	//	Create a copy of the original request
	if(pRequest != NULL)
	{
		switch(pRequest->GetClass())
		{
		case ML_REQUEST_CLASS_REGISTER:
			pPending = new CMLRegisterParams(*((CMLRegisterParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_LIST_FEM:
			pPending = new CMLListFEMParams(*((CMLListFEMParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_JOIN_FED:
			pPending = new CMLJoinFedParams(*((CMLJoinFedParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_ADD_DATA_SUB:
			pPending = new CMLAddDataSubParams(*((CMLAddDataSubParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_CREATE_OBJ:
			pPending = new CMLCreateObjectParams(*((CMLCreateObjectParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_DELETE_OBJ:
			pPending = new CMLDeleteObjectParams(*((CMLDeleteObjectParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_UPDATE_OBJ:
			pPending = new CMLUpdateObjectParams(*((CMLUpdateObjectParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_TRANSFER_OWNERSHIP:
			pPending = new CMLTransferObjectOwnershipParams(*((CMLTransferObjectOwnershipParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP:
			pPending = new CMLTransferObjectOwnershipParams(*((CMLTransferObjectOwnershipParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_READY:
			pPending = new CMLReadyParams(*((CMLReadyParams*)pRequest));
			break;

		case ML_REQUEST_START_FEDERATIONS:
			pPending = new CMLStartFederationParams(*((CMLStartFederationParams*)pRequest));
			break;

		case ML_REQUEST_PAUSE_FEDERATIONS:
			pPending = new CMLPauseFederationParams(*((CMLPauseFederationParams*)pRequest));
			break;

		case ML_REQUEST_RESUME_FEDERATIONS:
			pPending = new CMLResumeFederationParams(*((CMLResumeFederationParams*)pRequest));
			break;

		case ML_REQUEST_CLASS_TERMINATE:

			//	We don't expect responses from these requests
			break;

		default:
			assert(false);
			break;
		}

	}// if(pRequest != NULL)

	if(pPending != NULL)// Add to the local collection
	{
		_theReporter.Debug("CMLRequestParams", "AddPending", "Adding:%s", pPending->GetUUID());
		m_pendingRequests.AddTail(pPending);
	}

	return pPending;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to check the status of muthur 
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::CheckMuthurStatus()
{
	PTRNODE				Pos = NULL;
	CMLRequestParams*	pRequest = NULL;
	CMLPtrList			timedOut;
	unsigned long		ulSysTime = GetSysTimeMsecs();

	if(ulSysTime > m_ulMuthurHeartbeatTimeoutTimeMSecs)
	{
		if(m_pISystemCallback != NULL)
		{
			// For now we assume this is a termination event
			CMLSystemEvent sysEvent;
			sysEvent.SetEventType(ML_SYSTEM_RESET_REQUIRED);

			if(m_pISystemCallback != NULL) // someone may have terminated
			{
				_theReporter.Debug("MLAmbassador", "OnMMTimer", "Debug1...system event termination!");
				m_pISystemCallback->OnSystemEvent(&sysEvent);
				Shutdown();
			}
		}

		//	_theReporter.Debug("MLAmbassador", "OnMMTimer", "Debug2  ulSysTime:%d", ulSysTime);

	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to check pending requests to see if any have timed out
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::CheckPending()
{
	PTRNODE				Pos = NULL;
	CMLRequestParams*	pRequest = NULL;
	CMLPtrList			timedOut;
	unsigned long		ulSysTime = GetSysTimeMsecs();

	//	Locate all requests that have timed out
	m_pendingRequests.Lock();

	Pos = m_pendingRequests.GetHeadPosition();
	while(Pos != NULL)
	{
		pRequest = (CMLRequestParams*)(m_pendingRequests.GetNext(Pos));

		if(pRequest != NULL)
		{
			//	Has a time out been established for this request?
			_theReporter.Debug("Ambassador", "CheckPending", "RequestType:[%d] TimeOutTime:[%ld]", pRequest->GetRequestType(),
				pRequest->GetExpireTimeMSecs());

			if(pRequest->GetExpireTimeMSecs() > 0)
			{
				// Have we timed out?
				if(pRequest->GetExpireTimeMSecs() < ulSysTime)
				{
					timedOut.AddTail(pRequest);
				}

			}

		}

	}// while(Pos != NULL)

	m_pendingRequests.Unlock();

	//	Have any timed out?
	if(timedOut.GetCount() > 0)
	{
		Pos = timedOut.GetHeadPosition();
		while(Pos != NULL)
		{
			if((pRequest = (CMLRequestParams*)(timedOut.GetNext(Pos))) != NULL)
			{
				m_pendingRequests.Remove(pRequest, false); // we'll delete when finished

				if((pRequest->GetExpireTimeMSecs() > 0) && (pRequest->GetAmbassadorCallback() != NULL))
				{
					OnRequestError(pRequest, "Ambassador: the request has timed out");
				}
				else
				{
					delete pRequest;// Didn't assign to an exception so delete here
				}

			}// if((pRequest = (CMLRequestParams*)(timedOut.GetNext(Pos))) != NULL)

		}// while(Pos != NULL)

		timedOut.RemoveAll(FALSE); // we've already deleted the object or assigned to an exception

	}// if(timedOut.GetCount() > 0)

}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the pending request with the specifid UUID
//!
//!	Parameters:	\li pszUUID - the unique request identifier
//!				\li bRemove - true to remove from the list
//!				\li bDelete - true to delete the object after removal
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRequestParams* CMLAmbassador::GetPending(char* pszUUID, bool bRemove, bool bDelete)
{
	PTRNODE				Pos = NULL;
	CMLRequestParams*	pRequest = NULL;
	bool				bFound = false;

	//	Lock the list while we search for the request
	m_pendingRequests.Lock();

	Pos = m_pendingRequests.GetHeadPosition();
	while((Pos != NULL) && (bFound == false))
	{
		pRequest = (CMLRequestParams*)(m_pendingRequests.GetNext(Pos));

		if((pRequest != NULL) && (lstrcmpi(pRequest->GetUUID(), pszUUID) == 0))
		{
			bFound = true;
		}

	}// while((Pos != NULL) && (bFound == false))

	m_pendingRequests.Unlock();

	//	Are we supposed to remove?
	if((bFound == true) && (pRequest != NULL) && (bRemove == true))
	{
		m_pendingRequests.Remove(pRequest, false);

		if(bDelete == true)
		{
			delete pRequest;
			pRequest = NULL;
		}

	}

	return bFound ? pRequest : NULL;	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to initialize the ActiveMQ interface
//!
//!	Parameters:	\li pszMuthurUri - Uri to Muthur server
//!				\li iPort - the port number used by Muthur server
//!				\li ulWaitSecs - the amount of time to wait in milliseconds
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::Initialize(char* pszMuthurUrl, int iMuthurPort, unsigned long ulWaitMsecs)
{
	//	Have we already allocated the ActiveMQ wrapper
	if(m_pMQWrapper != NULL)
	{
		//	Has the host address changed?
		if((lstrcmpi(pszMuthurUrl, m_pMQWrapper->GetMuthurUrl()) != 0) || (iMuthurPort != m_pMQWrapper->GetMuthurPort()))
		{
			m_pMQWrapper->Shutdown();
			delete m_pMQWrapper;
			m_pMQWrapper = NULL;
		}
	}

	if(m_pMQWrapper == NULL)
		m_pMQWrapper = new CMQWrapper(this);

	m_pMQWrapper->SetMuthurUrl(pszMuthurUrl);
	m_pMQWrapper->SetMuthurPort(iMuthurPort);

	return m_pMQWrapper->Initialize(ulWaitMsecs);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if initialized and ready for operation
//!
//!	Parameters:	None
//!
//!	Returns:	true if initialized
//------------------------------------------------------------------------------
bool CMLAmbassador::GetInitialized()
{
	return ((m_pMQWrapper != NULL) && (m_pMQWrapper->GetInitialized() == true));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to join a federation
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::JoinFederation(CMLJoinFedParams* pParams)
{
	bool bSuccessful = false;

	assert(pParams != NULL);
	assert(pParams->GetFedExecModelHandle().IsValid());

	if((GetInitialized() == true) && (m_fedRegHandle.IsValid() == true))
	{
		// Make sure we've set the correct FRH
		pParams->SetFedRegHandle(m_fedRegHandle);

		// Store the model we'll be using
		m_fedExecModel = pParams->GetFEM();

		if(SendRequest(pParams, MQ_REPLY_TO_FED_EVENTS) != NULL)
		{
			bSuccessful = true;
		}
	}
	return bSuccessful;
}	

//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of Federation Execution Models (FEM)
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::ListFedExecModels(CMLListFEMParams* pParams)
{
	bool bSuccessful = false;

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedRegHandle.IsValid() == true))
	{
		// Make sure we've set the correct FRH
		pParams->SetFedRegHandle(m_fedRegHandle);

		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}
	return bSuccessful; 
}

//------------------------------------------------------------------------------
//!	Summary:	Callback for Window's multimedia timer messages
//!
//!	Parameters:	\li uTimeID - event identifier 
//!				\li dwUser - user supplied value when the event was created
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMMTimer(unsigned int uTimerID, unsigned long ulUser)
{
	//	Are we supposed to initialize the ActiveMQ interface?
	if(m_uCheckPendingTimerID == uTimerID)
	{
		CheckPending();
		//_theReporter.Debug("MLAmbassador", "OnMMTimer", "m_bIsHeartbeatEnabled:%d", m_bIsHeartbeatEnabled);
		if(m_bIsHeartbeatEnabled) CheckMuthurStatus();
	}	
}

//------------------------------------------------------------------------------
//!	Summary:	Called by MQ wrapper when muthur heartbeat message received
//!
//!	Parameters:	\li pszDataPub - the message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQMuthurHeartbeat(const char* pszDataPub)
{
	//CMLObjectEvent dataEvent;

	while(1)
	{
		// reset timer to system time plus max time elapses (30 sec.) because we received a heartbeat message from muthur
		//	_theReporter.Debug("MLAmbassador", "OnMQMuthurHeartbeat", "Before - Resetting timer...received muthur heartbeat message. m_ulMuthurHeartbeatTimeoutTimeMSecs:%d", 
		//		m_ulMuthurHeartbeatTimeoutTimeMSecs);
		m_ulMuthurHeartbeatTimeoutTimeMSecs = GetSysTimeMsecs() + ML_AMBASSADOR_SYSTEM_TIMEOUT_MSECS;
		//	_theReporter.Debug("MLAmbassador", "OnMQMuthurHeartbeat", "After - Resetting timer...received muthur heartbeat message. m_ulMuthurHeartbeatTimeoutTimeMSecs:%d", 
		//		m_ulMuthurHeartbeatTimeoutTimeMSecs);

		break; // all done...

	}// while(1)

}

//------------------------------------------------------------------------------
//!	Summary:	Called by MQ wrapper when a muthur time management update is received
//!
//!	Parameters:	\li pszDataPub - the message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQMuthurObjectOwnershipReliniquish(const char* pszObjOwnRel)
{
	CMLObjectOwnerRelinquishEvent relinquishEvent;

	_theReporter.Debug("MLAmbassador", "OnMQMuthurObjectOwnershipReliniquish", "Debug1");
	if(m_pIOwnershipRelinquishCallback != NULL)
	{
		if(CMLHelper::LoadXmlStream(&relinquishEvent, (char*)pszObjOwnRel) == true)
		{
			if(m_pIOwnershipRelinquishCallback != NULL) // someone may have terminated
				m_pIOwnershipRelinquishCallback->OnRelinquishOwnership(&relinquishEvent);
		}
		else
		{
			_theReporter.Warning("MLAmbassador", "OnMQMuthurObjectOwnershipReliniquish", "ERROR parsing xml!!");
		}
	}
	else
	{
		_theReporter.Warning("MLAmbassador", "OnMQMuthurObjectOwnershipReliniquish", "ERROR callback NULL!!");
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called by MQ wrapper when a muthur time management update is received
//!
//!	Parameters:	\li pszDataPub - the message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQMuthurTimeManagementUpdate(const char* pszTimeMgmt)
{
	CMLTimeManagementEvent tmEvent;

	_theReporter.Debug("MLAmbassador", "OnMQMuthurTimeManagementUpdate", "Debug1");
	if(m_pITimeMgmtUpdateCallback != NULL)
	{
		//FILE * pFile;
		//string sFileName = "C:\\MuthurTimeIntervals.txt";
		//fopen_s (&pFile, sFileName.c_str(), "a");

		//char buffer [2056];
		//sprintf_s (buffer, sizeof(buffer), "%s\n\n", pszTimeMgmt);
		//if (pFile!=NULL)
		//{
		//	fputs (buffer, pFile);
		//	fclose (pFile);
		//}
		//_theReporter.Debug("MLAmbassador", "OnMQMuthurTimeManagementUpdate", "pszTimeMgmt:%s", pszTimeMgmt);
		if(CMLHelper::LoadXmlStream(&tmEvent, (char*)pszTimeMgmt) == true)
		{
			//m_ulCurrentSimTime = tmEvent.GetTimeInterval();
			//_theReporter.Debug("MLAmbassador", "OnMQMuthurTimeManagementUpdate", "Debug3");
			if(m_pITimeMgmtUpdateCallback != NULL) // someone may have terminated
				m_pITimeMgmtUpdateCallback->OnTimeUpdate(&tmEvent);
		}
		else
		{
			_theReporter.Warning("MLAmbassador", "OnMQMuthurTimeManagementUpdate", "ERROR parsing xml!!");
		}
	}
	else
	{
		_theReporter.Warning("MLAmbassador", "OnMQMuthurTimeManagementUpdate", "ERROR callback NULL!!");
	}
}


//------------------------------------------------------------------------------
//!	Summary:	Called by MQ wrapper when a object is created, updated or deleted
//!
//!	Parameters:	\li pszObjEvent - the message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQObjectAction(const char* pszObjEvent)
{
	CMLObjectEvent objEvent;

	while(1)
	{
		//_theReporter.Debug("MLAmbassador", "OnMQObjectAction", "m_szFederateName:%s Debug1", m_szFederateName);
		//	Don't bother if no callback available
		if(m_pIObjectEventCallback == NULL)
			break;

		//_theReporter.Debug("MLAmbassador", "OnMQObjectAction", "m_szFederateName:%s Debug2 obj event callback is NOT null!", m_szFederateName);
		//	Use the XML to allocate and initialize a response object
		if(CMLHelper::LoadXmlStream(&objEvent, (char*)pszObjEvent) == false)
			break;

		//_theReporter.Debug("MLAmbassador", "OnMQObjectAction", "m_szFederateName:%s Debug3 Loaded xml stream", m_szFederateName);
		//	We should not have an empty data object now
		if(objEvent.GetDataObject() == NULL)
			break;

		//_theReporter.Debug("MLAmbassador", "OnMQObjectAction", "m_szFederateName:%s Debug4 - Data object NOT NULL!", m_szFederateName);
		//	Load the XML data object extracted from this pub message
		/*if(dataEvent.LoadXMLDataObject() == false)
		break;*/
		//_theReporter.Debug("Ambassador", "OnMQObjectAction", "m_szFederateName:%s Event Source:%s", 
		//			m_szFederateName, objEvent.GetEventSource());

		//_theReporter.Debug("MLAmbassador", "OnMQObjectAction", "m_szFederateName:%s Debug5", m_szFederateName);
		//
		if(m_pIObjectEventCallback != NULL) // someone may have terminated
			m_pIObjectEventCallback->OnObjectEvent(&objEvent);

		//_theReporter.Debug("MLAmbassador", "OnMQObjectAction", "m_szFederateName:%s Debug6", m_szFederateName);

		break; // all done...

	}// while(1)

}

//------------------------------------------------------------------------------
//!	Summary:	Called when an exception is raised by the ActiveMQ wrapper
//!
//!	Parameters:	\li pszExMessage - the exception's error message
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQException(const char* pszExMessage)
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called when a message comes in on the FedEvents queue
//!
//!	Parameters:	\li pszResponse - the response message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQFedEvent(const char* pszFedEvent)
{
	CMLSystemEvent sysEvent;

	if(m_pISystemCallback != NULL)
	{
		if(CMLHelper::LoadXmlStream(&sysEvent, (char*)pszFedEvent) == true)
		{
			if(m_pISystemCallback != NULL) // someone may have terminated
				m_pISystemCallback->OnSystemEvent(&sysEvent);
		}
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called by MQ wrapper when Ambassador response message received
//!
//!	Parameters:	\li pszResponse - the response message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQResponse(const char* pszResponse)
{
	CMLEvent*			pResponse = NULL;
	CMLRequestParams*	pRequest = NULL;
	bool				bDelete = false;

	while(1)
	{//MessageBox(NULL, pszResponse, "OnMQResponse", MB_OK);
		//	Use the XML to allocate and initialize a response object
		_theReporter.Debug("CMLAmbassador", "OnMQResponse", "debug1; m_szFederateName:%s", m_szFederateName);
		if((pResponse = GetResponseFromStream(pszResponse)) == NULL)
			break;
		_theReporter.Debug("CMLAmbassador", "OnMQResponse", "debug2; m_szFederateName:%s", m_szFederateName);
		//	Get the original request from our collection of pending requests
		if((pRequest = GetPending(pResponse->GetUUID(), true, false)) == NULL)
		{
			_theReporter.Debug("CMLAmbassador", "OnMQResponse", "debug3; m_szFederateName:%s", m_szFederateName);
			break; // must have already timed out
		}
		_theReporter.Debug("CMLAmbassador", "OnMQResponse", "debug4; m_szFederateName:%s", m_szFederateName);	
		//	Did this request succeed?
		if(pResponse->GetSucceeded() == true)
		{
			//	Should we update any of our local values?
			switch(pResponse->GetClass())
			{
				_theReporter.Debug("Ambassador", "OnMQResponse", "Event Source:%s m_szFederateName:%s",
					pResponse->GetEventSource(), m_szFederateName);
			case ML_RESPONSE_CLASS_REGISTER:

				m_fedRegHandle = ((CMLRegisterResponse*)pResponse)->GetFedRegHandle();
				break;

			case ML_RESPONSE_CLASS_JOIN_FED:

				m_fedExecHandle = ((CMLJoinFedResponse*)pResponse)->GetFedExecHandle();
				break;

			case ML_RESPONSE_CLASS_CREATE_OBJECT:
				_theReporter.Debug("Ambassador", "OnMQResponse", "Response is type create object response; m_szFederateName:%s", m_szFederateName);
				break;
			case ML_RESPONSE_CLASS_BASE:
				_theReporter.Debug("Ambassador", "OnMQResponse", "Response is of type base class; m_szFederateName:%s", m_szFederateName);

				break;

			}// switch(pResponse->GetClass())
			_theReporter.Debug("CMLAmbassador", "OnMQResponse", "debug5; m_szFederateName:%s", m_szFederateName);
			if(m_pISystemCallback != NULL) // someone may have terminated
			{
				//	Notify the federate
				if(pRequest->GetAmbassadorCallback() != NULL)
				{
					_theReporter.Debug("CMLAmbassador", "OnMQResponse", "Calling OnSuccess; m_szFederateName:%s", m_szFederateName);
					pRequest->GetAmbassadorCallback()->OnSuccess(pResponse);
				}
			}

		}
		else
		{
			_theReporter.Debug("CMLAmbassador", "OnMQResponse", "Calling OnError; m_szFederateName:%s", m_szFederateName);
			// The call to OnRequestError() assigns the original request to 
			// a CMLException object. This prevents us from deleting it here
			if(pRequest->GetAmbassadorCallback() != NULL)
			{
				bDelete = true;
			}

			// The call to OnRequestError() assigns the original request to 
			OnRequestError(pRequest, pResponse->GetErrorDescription());
			_theReporter.Debug("CMLAmbassador", "OnMQResponse", "debug8");
			if(bDelete)
			{
				pRequest = NULL;
			}

		}// if(pResponse->GetSucceeded() == true)

		break; // all done...

	}// while(1)

	// Clean up
	if(pResponse != NULL)
		delete pResponse;
	if(pRequest != NULL)
		delete pRequest;	
}

//------------------------------------------------------------------------------
//!	Summary:	Called by MQ wrapper when a system message is received
//!
//!	Parameters:	\li pszSysMessage - the system message text 
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnMQSysMessage(const char* pszSysMessage)
{
	CMLSystemEvent sysEvent;

	if(m_pISystemCallback != NULL)
	{
		if(CMLHelper::LoadXmlStream(&sysEvent, (char*)pszSysMessage) == true)
		{
			if(m_pISystemCallback != NULL) // someone may have terminated
				m_pISystemCallback->OnSystemEvent(&sysEvent);
		}
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to handle all request errors
//!
//!	Parameters:	\li pRequest - the originating request
//!				\li pszErrorMessage - the error message
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::OnRequestError(CMLRequestParams* pRequest, char* pszErrorMessage)
{
	CMLException Ex;

	assert(pRequest != NULL);
	_theReporter.Debug("CMLAmbassador", "OnRequestError", "debug1");
	if(pRequest != NULL)
	{
		//	Should we reset any of our local values?
		switch(pRequest->GetClass())
		{
		case ML_REQUEST_CLASS_REGISTER:

			m_fedRegHandle.Reset();
			break;

		case ML_REQUEST_CLASS_LIST_FEM:

			break;// nothing to do here

		case ML_REQUEST_CLASS_JOIN_FED:
		case ML_REQUEST_CLASS_CREATE_OBJ:
		case ML_REQUEST_CLASS_DELETE_OBJ:
		case ML_REQUEST_CLASS_UPDATE_OBJ:
		case ML_REQUEST_CLASS_TRANSFER_OWNERSHIP:
		case ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP:
		case ML_REQUEST_CLASS_ADD_DATA_SUB:
		case ML_REQUEST_CLASS_READY:
		default:

			m_fedExecHandle.Reset();
			m_fedExecModel.Reset();
			break;

		}// switch(pRequest->GetClass())

		//	Notify the federate if we have a callback interface
		if(pRequest->GetAmbassadorCallback() != NULL)
		{
			Ex.SetRequest(pRequest);

			if((pszErrorMessage != NULL) && (lstrlen(pszErrorMessage) > 0))
				Ex.SetMessage(pszErrorMessage);
			else
				Ex.SetMessage("no message");

			pRequest->GetAmbassadorCallback()->OnError(&Ex);
		}

	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate to request than an object's ownership be transferred
//! to this federate.
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::TransferObject(CMLTransferObjectOwnershipParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(pParams->GetUUID())
			_theReporter.Debug("Ambassador", "TransferObject", "UUID:%s",
			pParams->GetUUID());
		else
			_theReporter.Warning("Ambassador", "TransferObject", 
			"Attempting to delete object with invalid UUID!!!");


		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
			bSuccessful = true;
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate to relinquish an object's ownership.
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::RelinquishObject(CMLRelinquishObjectOwnershipRequest* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(pParams->GetDataObjectUUID())
			_theReporter.Debug("Ambassador", "RelinquishObject", "UUID:%s",
			pParams->GetDataObjectUUID());
		else
			_theReporter.Warning("Ambassador", "RelinquishObject", 
			"Attempting to delete object with invalid UUID!!!");


		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
			bSuccessful = true;
	}

	return bSuccessful;
}
//------------------------------------------------------------------------------
//!	Summary:	Called by a federate to request a transfer of an object's ownership.
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::RequestTransferObject(CMLTransferObjectOwnershipParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(pParams->GetUUID())
			_theReporter.Debug("Ambassador", "RequestTransferObject", "UUID:%s",
			pParams->GetUUID());
		else
			_theReporter.Warning("Ambassador", "RequestTransferObject", 
			"Attempting to delete object with invalid UUID!!!");


		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
			bSuccessful = true;
	}

	return bSuccessful;
}
//------------------------------------------------------------------------------
//!	Summary:	Called by a federate to update a data object
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::DeleteObject(CMLDeleteObjectParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(pParams->GetUUID())
			_theReporter.Debug("Ambassador", "DeleteObject", "UUID:%s",
			pParams->GetUUID());
		else
			_theReporter.Warning("Ambassador", "DeleteObject", 
			"Attempting to delete object with invalid UUID!!!");



		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate to update a data object
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::UpdateObject(CMLUpdateObjectParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(pParams->GetDataObject() && pParams->GetDataObject()->GetDataObjectUUID())
		{
			_theReporter.Debug("Ambassador", "UpdateObject", "CallSign:%s;TailNumber:%s;UUID:%s",
				pParams->GetDataObject()->GetACId(), pParams->GetDataObject()->GetACTailNumber(),
				pParams->GetDataObject()->GetDataObjectUUID());
		}
		else
			_theReporter.Warning("Ambassador", "UpdateObject", "Data Object or UUID is NULL!");



		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}	
//------------------------------------------------------------------------------
//!	Summary:	Called by a federate to create a data object
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::CreateObject(CMLCreateObjectParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(pParams->GetDataObject() && pParams->GetDataObject()->GetDataObjectUUID())
		{
			_theReporter.Debug("Ambassador", "CreateObject", "CallSign:%s;TailNumber:%s;UUID:%s;",
				pParams->GetDataObject()->GetACId(), pParams->GetDataObject()->GetACTailNumber(),
				pParams->GetDataObject()->GetDataObjectUUID());

			_theReporter.Debug("Ambassador", "CreateObject", "Data Object Type:%s;",
				typeid((*pParams->GetDataObject())).name());
		}
		else
			_theReporter.Warning("Ambassador", "CreateObject", "Data Object or UUID is NULL!");



		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}	

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate when it's ready to send a start federates message
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::StartFederation(CMLStartFederationParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];
	char szMuthurTMQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		//m_pMQWrapper->GetMuthurTimeManagementQueueName(szMuthurTMQueue, sizeof(szMuthurTMQueue));
		//pParams->SetMuthurTimeManagementQueueName(szMuthurTMQueue);

		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());
		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}	

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate when it's ready to send a pause federates message
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::PauseFederation(CMLPauseFederationParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];
	char szMuthurTMQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		//m_pMQWrapper->GetMuthurTimeManagementQueueName(szMuthurTMQueue, sizeof(szMuthurTMQueue));
		//pParams->SetMuthurTimeManagementQueueName(szMuthurTMQueue);

		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());
		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate when it's ready to send a resume federates message
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::ResumeFederation(CMLResumeFederationParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];
	char szMuthurTMQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		//m_pMQWrapper->GetMuthurTimeManagementQueueName(szMuthurTMQueue, sizeof(szMuthurTMQueue));
		//pParams->SetMuthurTimeManagementQueueName(szMuthurTMQueue);

		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());
		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate when it's ready to run
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::ReadyToRun(CMLReadyParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];
	char szMuthurTMQueue[128];	

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles & queue names
		m_pMQWrapper->GetMuthurTimeManagementQueueName(szMuthurTMQueue, sizeof(szMuthurTMQueue));
		pParams->SetMuthurTimeManagementQueueName(szMuthurTMQueue);		

		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());
		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}	

//------------------------------------------------------------------------------
//!	Summary:	Called to register a federate
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	None
//------------------------------------------------------------------------------
bool CMLAmbassador::Register(CMLRegisterParams* pParams)
{
	bool bSuccessful = false;
	char szMuthurHBQueue[128];


	assert(pParams != NULL);
	assert(pParams->GetName() != NULL);
	assert(lstrlen(pParams->GetName()) > 0);
	assert(pParams->GetAmbassadorCallback() != NULL);

	if(GetInitialized() == true)
	{
		m_pMQWrapper->GetMuthurHeartbeatQueueName(szMuthurHBQueue, sizeof(szMuthurHBQueue));
		pParams->SetMuthurHeartbeatQueueName(szMuthurHBQueue);
		pParams->SetFrequencyOfHeartbeats(ML_FREQ_OF_MUTHUR_HEARTBEATS);

		//	_theReporter.Debug("MLAmbassador", "Register", "Debug1.");
		//	Save the name of the federate
		lstrcpyn(m_szFederateName, pParams->GetName(), sizeof(m_szFederateName));
		lstrcpyn(m_szMuthurHeartbeatQueueName, pParams->GetMuthurHeartbeatQueueName(), sizeof(m_szMuthurHeartbeatQueueName));
		m_iFrequencyOfBeats =  pParams->GetFrequencyOfHeartbeats();		

		m_pISystemCallback = pParams->GetSystemCallback();
		assert(m_pISystemCallback != NULL);

		m_pITimeMgmtUpdateCallback = pParams->GetTimeManagementCallback();

		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			//			_theReporter.Debug("MLAmbassador", "Register", "Intializing timer - m_uCheckPendingTimerID.");
			if(m_uCheckPendingTimerID == 0)
				m_uCheckPendingTimerID = timeSetEvent(1000, 50, AmbassadorTimerCallback, (DWORD)this, TIME_PERIODIC);




			bSuccessful = true;
		}

	}

	return bSuccessful;	
}	

//------------------------------------------------------------------------------
//!	Summary:	Called to send a request to Muthur
//!
//!	Parameters:	\li pRequest - the request to be sent
//!				\li iReplyTo - the MQ reply to queue identifier
//!
//!	Returns:	The request added to the list of pending requests
//------------------------------------------------------------------------------
CMLRequestParams* CMLAmbassador::SendRequest(CMLRequestParams* pRequest, int iReplyTo)
{
	CMLRequestParams*	pPending = NULL;
	CMLDataStream		dataStream;
	bool				bSuccessful = false;

	assert(pRequest != NULL);

	if((pRequest != NULL) && (GetInitialized() == true))
	{
		//	Set the time stamps
		SetExpiration(pRequest);
		if(CMLHelper::GetXmlStream(pRequest, &dataStream) == true)
		{
			//	Add to the list of pending requests
			//
			//	NOTE: We do this BEFORE we actually send the request so that it's in the list if the
			//		  response should come from Muthur before this call returns
			if((pPending = AddPending(pRequest)) != NULL)
			{
				/*if(m_ulCurrentSimTime > -1)
				{
					char tempFileName[256];
					sprintf_s(tempFileName, sizeof(tempFileName), "C:\\ObjectAction_%ld.xml", m_ulCurrentSimTime);
					CMLHelper::SaveXmlStream(pRequest, tempFileName);
				}*/
				//CMLHelper::SaveXmlStream(pRequest, "C:\\ObjectAction1.xml");
				_theReporter.Debug("Ambassador", "CheckPending", "1 RequestType:[%d] TimeOutTime:[%ld]", pRequest->GetRequestType(),
					pRequest->GetExpireTimeMSecs());
				if(m_pMQWrapper->SendRequest((const char*)(dataStream.GetBuffer()), pRequest->GetUUID(), iReplyTo) == true)
				{
					bSuccessful = true;
				}
				else
				{
					m_pendingRequests.Remove(pPending, true);
					pPending = NULL;
				}			
			}
			else
			{
				// Since no pending assume no response is expected. Go ahead and send
				/*if(m_ulCurrentSimTime > -1)
				{
					char tempFileName[256];
					sprintf_s(tempFileName, sizeof(tempFileName), "C:\\ObjectAction_%ld.xml", m_ulCurrentSimTime);
					CMLHelper::SaveXmlStream(pRequest, tempFileName);
				}*/
				//CMLHelper::SaveXmlStream(pRequest, "C:\\ObjectAction2.xml");
				_theReporter.Debug("Ambassador", "CheckPending", "2 RequestType:[%d] TimeOutTime:[%ld]", pRequest->GetRequestType(),
					pRequest->GetExpireTimeMSecs());
				if(m_pMQWrapper->SendRequest((const char*)(dataStream.GetBuffer()), pRequest->GetUUID(), iReplyTo) == true)
				{
					bSuccessful = true;
				}
			}

		}// if(CMLHelper::GetXmlStream(pRequest, &dataStream) == true)

	}// if((pRequest != NULL) && (IsInitialized() == true))

	return pPending;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the expiration time for the specified request
//!
//!	Parameters:	\li pRequest - the request that is being submitted
///!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::SetExpiration(CMLRequestParams* pRequest)
{
	CMLRequestParams*	pPending = NULL;
	CMLDataStream		dataStream;
	bool				bSuccessful = false;

	assert(pRequest != NULL);

	pRequest->SetIssuedTimestampMSecs(GetSysTimeMsecs());

	switch(pRequest->GetClass())
	{
		//	Timeout values for these requests are controlled locally
	//case ML_REQUEST_CLASS_CREATE_OBJ:
	//case ML_REQUEST_CLASS_DELETE_OBJ:
	//case ML_REQUEST_CLASS_UPDATE_OBJ:
	//case ML_REQUEST_CLASS_TRANSFER_OWNERSHIP:
	//case ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP:
	case ML_REQUEST_CLASS_REGISTER:
	case ML_REQUEST_CLASS_LIST_FEM:

		pRequest->SetExpireTimeMSecs(pRequest->GetIssuedTimestampMSecs() + GetTimeOutMsec(pRequest));
		break;

		// Timeouts for these requests are controlled by Muthur and the active model
	case ML_REQUEST_CLASS_JOIN_FED:
	case ML_REQUEST_CLASS_ADD_DATA_SUB:
	case ML_REQUEST_CLASS_CREATE_OBJ:
	case ML_REQUEST_CLASS_DELETE_OBJ:
	case ML_REQUEST_CLASS_UPDATE_OBJ:
	case ML_REQUEST_CLASS_TRANSFER_OWNERSHIP:
	case ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP:
	case ML_REQUEST_CLASS_READY:
	case ML_REQUEST_CLASS_TERMINATE:
	default:

		pRequest->SetExpireTimeMSecs(0);
		break;
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the federate name
//!
//!	Parameters:	\li pszName - the name of the active federate
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::SetFederateName(char* pszName)
{
	if(pszName != NULL)
	{
		lstrcpyn(m_szFederateName, pszName, sizeof(m_szFederateName));
	}
	else
	{
		memset(m_szFederateName, 0, sizeof(m_szFederateName));
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time out value in seconds
//!
//!	Parameters:	iSecs - the time out in seconds
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::SetTimeOutSecs(int iSecs)
{
	m_iTimeOutSecs = iSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to shut down the Ambassador
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAmbassador::Shutdown()
{
	m_pISystemCallback = NULL;
	m_pIObjectEventCallback = NULL;
	m_pITimeMgmtUpdateCallback = NULL;
	m_pIOwnershipRelinquishCallback = NULL;

	if(m_uCheckPendingTimerID > 0)
	{
		timeKillEvent(m_uCheckPendingTimerID);
		m_uCheckPendingTimerID = 0;
	}

	if(m_pMQWrapper != NULL)
	{
		m_pMQWrapper->Shutdown();
		delete m_pMQWrapper;
		m_pMQWrapper = NULL;
	}

	// Flush the collection of pending requests
	m_pendingRequests.RemoveAll(TRUE);

	/*if(logger)
	{
	delete logger;
	logger = NULL;
	}*/

	m_fedRegHandle.SetMuthurId("");
	m_fedExecModel.SetFedExecModelHandle(CMLHandle(""));
}

//------------------------------------------------------------------------------
//!	Summary:	Called by a federate when it wants to terminate the federation
//!
//!	Parameters:	\li pParams - the method parameters
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLAmbassador::Terminate(CMLTerminateParams* pParams)
{
	bool	bSuccessful = false;
	char	szSubQueue[128];

	assert(pParams != NULL);

	if((GetInitialized() == true) && (m_fedExecHandle.IsValid() == true))
	{
		// Make sure we've set the correct handles
		pParams->SetFedRegHandle(m_fedRegHandle);
		pParams->SetFedExecHandle(m_fedExecHandle);
		pParams->SetFedExecModelHandle(GetFedExecModelHandle());

		if(SendRequest(pParams, MQ_REPLY_TO_DEFAULT) != NULL)
		{
			bSuccessful = true;
		}
	}

	return bSuccessful;
}	

