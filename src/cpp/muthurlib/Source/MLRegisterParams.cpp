//------------------------------------------------------------------------------
/*! \file	MLRegisterParams.cpp
//
//  Contains the implementation of the CMLRegisterParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLRegisterParams.h>
#include <MLAmbassador.h>
#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
CReporter		_theReporterRegParams;	//!< The global diagnostics / error reporter
extern CReporter _theReporter;	//!< The global diagnostics / error reporter
//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - buffer containing the name of the federate
//!	Parameters:	\li pICallback - the callback interface for system events
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRegisterParams::CMLRegisterParams(char* pszName, IMLSystemCallback* pICallback, IMLTimeManagementEventCallback *pITimeMgmtCallback) 
				  :CMLRequestParams() 
{
	m_pISystemCallback = NULL;
	m_pITimeMgmtUpdateCallback = NULL;
	memset(m_szName, 0, sizeof(m_szName));
	
	if(pszName != NULL)
		SetName(pszName);
	if(pICallback != NULL)
		SetSystemCallback(pICallback);
	if(pITimeMgmtCallback != NULL)
		SetTimeManagementCallback(pITimeMgmtCallback);

	memset(m_szMuthurHeartbeatQueueName, 0, sizeof(m_szMuthurHeartbeatQueueName));
	m_iFrequencyOfBeats = ML_FREQ_OF_MUTHUR_HEARTBEATS;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRegisterParams::CMLRegisterParams(const CMLRegisterParams& rSource)
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
CMLRegisterParams::~CMLRegisterParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterParams::Copy(const CMLRegisterParams& rSource)
{
	//	Perform base class processing first
	CMLRequestParams::Copy(rSource);

	m_pITimeMgmtUpdateCallback = rSource.m_pITimeMgmtUpdateCallback;
	m_pISystemCallback = rSource.m_pISystemCallback;
	lstrcpyn(m_szName, rSource.m_szName, sizeof(m_szName));
	lstrcpyn(m_szMuthurHeartbeatQueueName, rSource.m_szMuthurHeartbeatQueueName, sizeof(m_szMuthurHeartbeatQueueName));
	m_iFrequencyOfBeats = rSource.m_iFrequencyOfBeats;	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name used to identify the federate
//!
//!	Parameters:	None
//!
//!	Returns:	The federate name
//------------------------------------------------------------------------------
char* CMLRegisterParams::GetName()
{
	return m_szName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the System callback interface
//!
//!	Parameters:	None
//!
//!	Returns:	The current system event callback interface
//------------------------------------------------------------------------------
IMLSystemCallback* CMLRegisterParams::GetSystemCallback()
{
	return m_pISystemCallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the time management callback interface
//!
//!	Parameters:	None
//!
//!	Returns:	The current time management callback interface
//------------------------------------------------------------------------------
IMLTimeManagementEventCallback* CMLRegisterParams::GetTimeManagementCallback()
{
	return m_pITimeMgmtUpdateCallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the muthur heartbeat queue name
//!
//!	Parameters:	None
//!
//!	Returns:	The subscription queue name
//------------------------------------------------------------------------------
char* CMLRegisterParams::GetMuthurHeartbeatQueueName() 
{ 
	return m_szMuthurHeartbeatQueueName; 
}	

//------------------------------------------------------------------------------
//!	Summary:	Called to set the muthur heartbeat queue name
//!
//!	Parameters:	\li p - name of the muthur heartbeat queue
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterParams::SetMuthurHeartbeatQueueName(char* p)					
{
	if(p != NULL)
		lstrcpyn(m_szMuthurHeartbeatQueueName, p, sizeof(m_szMuthurHeartbeatQueueName));
	else
		memset(m_szMuthurHeartbeatQueueName, 0, sizeof(m_szMuthurHeartbeatQueueName));
	}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the frequency of muthur heartbeats
//!
//!	Parameters:	None
//!
//!	Returns:	The subscription queue name
//------------------------------------------------------------------------------
int	CMLRegisterParams::GetFrequencyOfHeartbeats()
{
	return m_iFrequencyOfBeats;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the frequency of the heartbeats
//!
//!	Parameters:	\li p - frequency of heartbeats
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterParams::SetFrequencyOfHeartbeats(int f)
{
	m_iFrequencyOfBeats = f;
}

///------------------------------------------------------------------------------
//!	Summary:	Called to get an XML element descriptor that defines all 
//!				properties in the specified block
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!
//!	Returns:	The element used to construct the XML stream
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLRegisterParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;

	//	Use the base class to allocate the object
	if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				// No additional control block properties for this derived class
				break;
			
			case ML_XML_BLOCK_DATA:

				pXmlBlock->AddChild("federateName", m_szName);
			
				_theReporter.Debug("MLRegisterParams", "GetXmlBlock", "m_szMuthurHeartbeatQueueName:%s federateHeartBeatIntervalSecs:%d",
					m_szMuthurHeartbeatQueueName, m_iFrequencyOfBeats);
				pXmlBlock->AddChild("federateEventQueueName", m_szMuthurHeartbeatQueueName);
				pXmlBlock->AddChild("federateHeartBeatIntervalSecs", m_iFrequencyOfBeats);

				break;

		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLRequestParams::GetXmlElement(eXmlBlock)) != NULL)

	return pXmlBlock;
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
bool CMLRegisterParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool bSuccessful = false;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:
			// No base class data in this block
			break;
		
		case ML_XML_BLOCK_DATA:

			
			if(lstrcmpi("federateName", pMLPropMember->GetName()) == 0)
			{
				lstrcpyn(m_szName, pMLPropMember->GetValue(), sizeof(m_szName));
				bSuccessful = true;
			}
			else if(lstrcmpi("federateEventQueueName", pMLPropMember->GetName()) == 0)
			{
				lstrcpyn(m_szMuthurHeartbeatQueueName, pMLPropMember->GetValue(), sizeof(m_szMuthurHeartbeatQueueName));
				bSuccessful = true;
			}
			else if(lstrcmpi("federateHeartBeatIntervalSecs", pMLPropMember->GetName()) == 0)
			{
				m_iFrequencyOfBeats = pMLPropMember->AsInteger();
				bSuccessful = true;
			}
			
			_theReporter.Debug("MLRegisterParams", "SetXmlValue", "m_szMuthurHeartbeatQueueName:%s federateHeartBeatIntervalSecs:%d",
					m_szMuthurHeartbeatQueueName, m_iFrequencyOfBeats);
			break;

		default:
			
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the Ambassador callback interface
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLRegisterParams& CMLRegisterParams::operator = (const CMLRegisterParams& rSource)
{
	Copy(rSource);
	return *this;
}



//------------------------------------------------------------------------------
//!	Summary:	Called to set the name used to identify the federate
//!
//!	Parameters:	\li pszName - the name of the federate
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterParams::SetName(char* pszName)
{
	if(pszName != NULL)
		lstrcpyn(m_szName, pszName, sizeof(m_szName));
	else
		memset(m_szName, 0, sizeof(m_szName));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the System callback interface
//!
//!	Parameters:	\li pICallback - the callback to be invoked by the System
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterParams::SetSystemCallback(IMLSystemCallback* pICallback)
{
	m_pISystemCallback = pICallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time management callback interface
//!
//!	Parameters:	\li pICallback - the callback to be invoked by the time management system
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterParams::SetTimeManagementCallback(IMLTimeManagementEventCallback* pICallback)
{
	m_pITimeMgmtUpdateCallback = pICallback;
}

