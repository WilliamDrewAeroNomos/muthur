//------------------------------------------------------------------------------
/*! \file	MLFedExecModel.cpp
//
//  Contains the implementation of the CMLFedExecModel class
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
#include <MLFedExecModel.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLFedExecModel::CMLFedExecModel()
{
	Reset();
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLFedExecModel::CMLFedExecModel(const CMLFedExecModel& rSource)
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
CMLFedExecModel::~CMLFedExecModel()
{
	m_apReqFederates.RemoveAll(TRUE);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a name to the list of required federates
//!
//!	Parameters:	\li pszName - the name of the required federate
//!
//!	Returns:	The string containing the name
//------------------------------------------------------------------------------
const char* CMLFedExecModel::AddRequiredFederate(const char* pszName)
{
	CMLString* pString = NULL;

	if((pszName != NULL) && (lstrlen(pszName) > 0))
	{
		pString = new CMLString();
		*pString = pszName;
	
		m_apReqFederates.AddTail(pString);
	}

	return *pString;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::Copy(const CMLFedExecModel& rSource)
{
	PTRNODE		Pos = NULL;
	CMLString*	pReqFederate = NULL;

	m_fedExecModelHandle = rSource.m_fedExecModelHandle;
	lstrcpyn(m_szName, rSource.m_szName, sizeof(m_szName));
	lstrcpyn(m_szDescription, rSource.m_szDescription, sizeof(m_szDescription));
	m_lLogicalStartTime = rSource.m_lLogicalStartTime;										
	m_lDefaultDurationWithinStartupProtocolMSecs = rSource.m_lDefaultDurationWithinStartupProtocolMSecs;				
	m_lDurationFederationExecutionMSecs = rSource.m_lDurationFederationExecutionMSecs;						
	m_lDurationJoinFederationMSecs = rSource.m_lDurationJoinFederationMSecs;								
	m_lDurationRegisterPublicationMSecs = rSource.m_lDurationRegisterPublicationMSecs;						
	m_lDurationRegisterSubscriptionMSecs = rSource.m_lDurationRegisterSubscriptionMSecs;						
	m_lDurationRegisterToRunMSecs = rSource.m_lDurationRegisterToRunMSecs;								
	m_lDurationTimeToWaitAfterTermination = rSource.m_lDurationTimeToWaitAfterTermination;	
	m_bAutoStart = rSource.m_bAutoStart;

	//	Copy all the required federates
	m_apReqFederates.RemoveAll(TRUE);
	const CMLPtrList& rReqFederates = rSource.m_apReqFederates;
	Pos = rReqFederates.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pReqFederate = (CMLString*)(rReqFederates.GetNext(Pos))) != NULL)
		{
			AddRequiredFederate(*pReqFederate);
		}
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the model handle
//!
//!	Parameters:	None
//!
//!	Returns:	The model handle established by the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLFedExecModel::GetFedExecModelHandle()
{
	return m_fedExecModelHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of this model
//!
//!	Parameters:	None
//!
//!	Returns:	The name assigned to this execution model
//------------------------------------------------------------------------------
char* CMLFedExecModel::GetName()
{
	return m_szName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the decription of this model
//!
//!	Parameters:	None
//!
//!	Returns:	The description of this execution model
//------------------------------------------------------------------------------
char* CMLFedExecModel::GetDescription()
{ 
	return m_szDescription;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the first required federate
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the first required federate
//------------------------------------------------------------------------------
const char* CMLFedExecModel::GetFirstRequiredFederate()
{
	CMLString* pString = NULL;

	m_posReqFederates = m_apReqFederates.GetHeadPosition();
	if(m_posReqFederates != NULL)
	{
		pString = (CMLString*)(m_apReqFederates.GetNext(m_posReqFederates));
	}
	
	if(pString != NULL)
		return *pString;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the logical start time of this model
//!
//!	Parameters:	None
//!
//!	Returns:	The logical start time of this model
//------------------------------------------------------------------------------
long CMLFedExecModel::GetLogicalStartTime() 
{ 
	return m_lLogicalStartTime;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the default value used for the next 6 durations if none of 
//! those values need to be explicitly set.
//!
//!	Parameters:	None
//!
//!	Returns:	The default value used for the next 6 durations if none of 
//! those values need to be explicitly set.
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDefaultDurationWithinStartupProtocolMSecs()
{
	return m_lDefaultDurationWithinStartupProtocolMSecs; 
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the duration for federate to run
//!
//!	Parameters:	None
//!
//!	Returns:	The duration for federate to run
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDurationFederationExecutionMSecs()
{ 
	return m_lDurationFederationExecutionMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the duration to wait for all federates to join
//!
//!	Parameters:	None
//!
//!	Returns:	The duration to wait for all federates to join
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDurationJoinFederationMSecs()
{
	return m_lDurationJoinFederationMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the duration to wait for all publications to be registered
//!
//!	Parameters:	None
//!
//!	Returns:	The duration to wait for all publications to be registered
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDurationRegisterPublicationMSecs()
{
	return m_lDurationRegisterPublicationMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the duration to wait for all subscriptions to be registered
//!
//!	Parameters:	None
//!
//!	Returns:	The duration to wait for all subscriptions to be registered
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDurationRegisterSubscriptionMSecs()
{
	return m_lDurationRegisterSubscriptionMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the duration to wait for all registrations to run
//!
//!	Parameters:	None
//!
//!	Returns:	The duration to wait for all registrations to run
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDurationRegisterToRunMSecs()
{
	return m_lDurationRegisterToRunMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the duration to wait after termination message received prior to shutting down
//!
//!	Parameters:	None
//!
//!	Returns:	The duration to wait after termination message received prior to shutting down
//------------------------------------------------------------------------------
long CMLFedExecModel::GetDurationTimeToWaitAfterTermination()
{
	return m_lDurationTimeToWaitAfterTermination;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the value of auto start
//!
//!	Parameters:	None
//!
//!	Returns:	The value of autostart
//------------------------------------------------------------------------------
bool CMLFedExecModel::GetAutoStart()
{
	return m_bAutoStart;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the next required federate
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the next required federate
//------------------------------------------------------------------------------
const char* CMLFedExecModel::GetNextRequiredFederate()
{
	CMLString* pString = NULL;

	if(m_posReqFederates != NULL)
	{
		pString = (CMLString*)(m_apReqFederates.GetNext(m_posReqFederates));
	}
	
	if(pString != NULL)
		return *pString;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the collection of required federate names
//!
//!	Parameters:	None
//!
//!	Returns:	The collection of required federate names
//------------------------------------------------------------------------------
const CMLPtrList& CMLFedExecModel::GetRequiredFederates() const
{
	return m_apReqFederates;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the collection of required federate names
//!
//!	Parameters:	None
//!
//!	Returns:	The collection of required federate names
//------------------------------------------------------------------------------
CMLPtrList& CMLFedExecModel::GetRequiredFederates()
{
	return m_apReqFederates;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to a property member used to serialize this object to
//!				XML
//!
//!	Parameters:	None
//!
//!	Returns:	A property member for XML serialization
//------------------------------------------------------------------------------
CMLPropMember* CMLFedExecModel::GetXmlMember()
{
	CMLPropMember*	pXmlModel = NULL;
	CMLPropMember*	pXmlReqFederates = NULL;
	const char*		pReqFederate = NULL;

	//	Create the root node for the model
	pXmlModel = new CMLPropMember("fedExecModel");

	pXmlModel->AddChild("femName", m_szName);
	pXmlModel->AddChild("description", m_szDescription);
	pXmlModel->AddChild("fedExecModelUUID", m_fedExecModelHandle);
	pXmlModel->AddChild("durationFederationExecutionMSecs", m_lDurationFederationExecutionMSecs);
	pXmlModel->AddChild("defaultDurationWithinStartupProtocolMSecs", m_lDefaultDurationWithinStartupProtocolMSecs);
	pXmlModel->AddChild("durationJoinFederationMSecs", m_lDurationJoinFederationMSecs);
	pXmlModel->AddChild("durationRegisterPublicationMSecs", m_lDurationRegisterPublicationMSecs);
	pXmlModel->AddChild("durationRegisterSubscriptionMSecs", m_lDurationRegisterSubscriptionMSecs);
	pXmlModel->AddChild("durationRegisterToRunMSecs", m_lDurationRegisterToRunMSecs);
	pXmlModel->AddChild("durationTimeToWaitAfterTermination", m_lDurationTimeToWaitAfterTermination);
	pXmlModel->AddChild("autoStart", m_bAutoStart);
	pXmlModel->AddChild("fedExecModelLogicalStartTimeMSecs", m_lLogicalStartTime);
	
	//	Add a child for the required federates
	if(m_apReqFederates.GetCount() > 0)
	{
		pXmlReqFederates = new CMLPropMember("requiredFederates");
	
		pReqFederate = GetFirstRequiredFederate();
		
		while(pReqFederate != NULL)
		{
			pXmlReqFederates->AddChild("requiredFederate", (char*)pReqFederate);

			pReqFederate = GetNextRequiredFederate();
		}

		pXmlModel->AddChild(pXmlReqFederates);
	}
	
	return pXmlModel;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLFedExecModel& CMLFedExecModel::operator = (const CMLFedExecModel& rSource)
{
	Copy(rSource);															
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::Reset()
{
	memset(m_szName, 0, sizeof(m_szName));		
	memset(m_szDescription, 0, sizeof(m_szDescription));
	m_lLogicalStartTime = 0;										
	m_lDefaultDurationWithinStartupProtocolMSecs = 0;				
	m_lDurationFederationExecutionMSecs = 0;						
	m_lDurationJoinFederationMSecs = 0;								
	m_lDurationRegisterPublicationMSecs = 0;						
	m_lDurationRegisterSubscriptionMSecs = 0;						
	m_lDurationRegisterToRunMSecs = 0;								
	m_lDurationTimeToWaitAfterTermination = 0;						
	m_bAutoStart = true;
	m_posReqFederates = NULL;
	m_apReqFederates.RemoveAll(TRUE);
	m_fedExecModelHandle.SetMuthurId("");
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the handle used to identify this model
//!
//!	Parameters:	\li rHandle - the handle used by Ambassador to identify this model
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetFedExecModelHandle(CMLHandle& rHandle)
{
	m_fedExecModelHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name of the model
//!
//!	Parameters:	\li pszName - the buffer containing the model name
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetName(char* pszName)
{
	if(pszName != NULL)
		lstrcpyn(m_szName, pszName, sizeof(m_szName));
	else
		memset(m_szName, 0, sizeof(m_szName));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the decription of this model
//!
//!	Parameters:	\li c - the buffer containing the decription of this model
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDescription(char* c)
{
	if(c != NULL)
		lstrcpyn(m_szDescription, c, sizeof(m_szDescription));
	else
		memset(m_szDescription, 0, sizeof(m_szDescription));
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the logical start time of this model
//!
//!	Parameters:	\li l - the logical start time of this model
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetLogicalStartTime(long l)
{
	m_lLogicalStartTime = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the default value used for the next 6 durations if none of 
//! those values need to be explicitly set.
//!
//!	Parameters:	\li l - the default value used for the next 6 durations if none of 
//! those values need to be explicitly set.
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDefaultDurationWithinStartupProtocolMSecs(long l)
{
	m_lDefaultDurationWithinStartupProtocolMSecs = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the duration for federate to run
//!
//!	Parameters:	\li l - duration for federate to run
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDurationFederationExecutionMSecs(long l)
{
	m_lDurationFederationExecutionMSecs = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the duration to wait for all federates to join
//!
//!	Parameters:	\li l - the duration to wait for all federates to join
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDurationJoinFederationMSecs(long l)
{
	m_lDurationJoinFederationMSecs = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the duration to wait for all publications to be registered
//!
//!	Parameters:	\li l - the duration to wait for all publications to be registered
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDurationRegisterPublicationMSecs(long l)
{
	m_lDurationRegisterPublicationMSecs = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the duration to wait for all subscriptions to be registered
//!
//!	Parameters:	\li l - the duration to wait for all subscriptions to be registered
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDurationRegisterSubscriptionMSecs(long l)
{
	m_lDurationRegisterSubscriptionMSecs = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the nduration to wait for all registrations to run
//!
//!	Parameters:	\li l - duration to wait for all registrations to run
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDurationRegisterToRunMSecs(long l)
{
	m_lDurationRegisterToRunMSecs = l;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the duration to wait after termination message received prior to shutting down
//!
//!	Parameters:	\li l - the duration to wait after termination message received prior to shutting down
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetDurationTimeToWaitAfterTermination(long l)
{
	m_lDurationTimeToWaitAfterTermination = l;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the auto start value
//!
//!	Parameters:	\li l - the autostart value
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFedExecModel::SetAutoStart(bool b)
{
	m_bAutoStart = b;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set the values using the member constructed from
//!				an XML data stream
//!
//!	Parameters:	\li pFEMMember - the root FEM member
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLFedExecModel::SetXmlValues(CMLPropMember* pFEMMember)
{
	CMLPropMember*	pChild = NULL;
	CMLPropMember*	pReqFederate = NULL;
	bool			bSuccessful = false;

	assert(pFEMMember != NULL);
	assert(lstrcmpi(pFEMMember->GetName(), "fedExecModel") == 0);
	
	//	The propertly member passed by the caller should be the root node for
	//	the execution model
	if((pFEMMember != NULL) && (lstrcmpi(pFEMMember->GetName(), "fedExecModel") == 0))
	{
		pChild = pFEMMember->GetFirstChild();
		while(pChild != NULL)
		{
			if(lstrcmpi("femName", pChild->GetName()) == 0)
			{
				lstrcpyn(m_szName, pChild->GetValue(), sizeof(m_szName));
			}
			else if(lstrcmpi("description", pChild->GetName()) == 0)
			{
				lstrcpyn(m_szDescription, pChild->GetValue(), sizeof(m_szDescription));
			}
			else if(lstrcmpi("fedExecModelUUID", pChild->GetName()) == 0)
			{
				m_fedExecModelHandle.SetMuthurId(pChild->GetValue());
			}
			else if(lstrcmpi("durationFederationExecutionMSecs", pChild->GetName()) == 0)
			{
				m_lDurationFederationExecutionMSecs = pChild->AsLong();
			}
			else if(lstrcmpi("durationJoinFederationMSecs", pChild->GetName()) == 0)
			{
				m_lDurationJoinFederationMSecs = pChild->AsLong();
			}
			else if(lstrcmpi("durationRegisterPublicationMSecs", pChild->GetName()) == 0)
			{
				m_lDurationRegisterPublicationMSecs = pChild->AsLong();
			}
			else if(lstrcmpi("durationRegisterSubscriptionMSecs", pChild->GetName()) == 0)
			{
				m_lDurationRegisterSubscriptionMSecs = pChild->AsLong();
			}
			else if(lstrcmpi("durationRegisterToRunMSecs", pChild->GetName()) == 0)
			{
				m_lDurationRegisterToRunMSecs = pChild->AsLong();
			}
			else if(lstrcmpi("durationTimeToWaitAfterTermination", pChild->GetName()) == 0)
			{
				m_lDurationTimeToWaitAfterTermination = pChild->AsLong();
			}
			else if(lstrcmpi("autoStart", pChild->GetName()) == 0)
			{
				m_bAutoStart = pChild->AsBool();
			}
			else if(lstrcmpi("fedExecModelLogicalStartTimeMSecs", pChild->GetName()) == 0)
			{
				m_lLogicalStartTime = pChild->AsLong();
			}
			else if(lstrcmpi("defaultDurationWithinStartupProtocolMSecs", pChild->GetName()) == 0)
			{
				m_lDefaultDurationWithinStartupProtocolMSecs = pChild->AsLong();
			}
			
			else if(lstrcmpi("requiredFederates", pChild->GetName()) == 0)
			{
				pReqFederate = pChild->GetFirstChild();
				while(pReqFederate != NULL)
				{
					if(lstrcmpi("requiredFederate", pReqFederate->GetName()) == 0)
					{
						AddRequiredFederate(pReqFederate->GetValue());
					}
					pReqFederate = pChild->GetNextChild();
				}
			}			
			pChild = pFEMMember->GetNextChild();
		
		}// while(pChild != NULL)	
		
		bSuccessful = true;
	
	}// if((pMLPropMember != NULL) && (lstrcmpi(pMLPropMember->GetName(), "fedExecModel") == 0))

	return bSuccessful;
}

