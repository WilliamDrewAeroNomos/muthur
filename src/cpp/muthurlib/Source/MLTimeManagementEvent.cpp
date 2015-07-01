//------------------------------------------------------------------------------
/*! \file	MLTimeManagementEvent.cpp
//
//  Contains the implementation of the CMLTimeManagementEvent class
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

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLTimeManagementEvent.h>
#include <MLAmbassador.h>
#include <MLHelper.h>
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
CMLTimeManagementEvent::CMLTimeManagementEvent()
{
	m_dTimeIntervalMSecs = 0;	
	m_iFederationExecutionState = 0;
	m_iFederationExecutionDirective = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLTimeManagementEvent::CMLTimeManagementEvent(const CMLTimeManagementEvent& rSource)
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
CMLTimeManagementEvent::~CMLTimeManagementEvent()
{	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLTimeManagementEvent::Copy(const CMLTimeManagementEvent& rSource)
{
	m_dTimeIntervalMSecs = rSource.m_dTimeIntervalMSecs;	
	m_iFederationExecutionState = rSource.m_iFederationExecutionState;
	m_iFederationExecutionDirective = rSource.m_iFederationExecutionDirective;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time interval in msecs
//!
//!	Parameters:	None
//!
//!	Returns:	The time interval
//------------------------------------------------------------------------------
double CMLTimeManagementEvent::GetTimeInterval()
{
	return m_dTimeIntervalMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the current state of federation (UNDEFINED=0,RUNNING=1,PAUSED=2)
//!
//!	Parameters:	None
//!
//!	Returns:	The current state of federation (UNDEFINED=0,RUNNING=1,PAUSED=2)
//------------------------------------------------------------------------------
int CMLTimeManagementEvent::GetFederationExecutionState()
{
	return m_iFederationExecutionState;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the action the federate should take (UNDEFINED=0,START=1,PAUSE=2,RESUME=3,RUN=4)
//!
//!	Parameters:	None
//!
//!	Returns:	The action the federate should take (UNDEFINED=0,START=1,PAUSE=2,RESUME=3,RUN=4)
//------------------------------------------------------------------------------
int CMLTimeManagementEvent::GetFederationExecutionDirective()
{
	return m_iFederationExecutionDirective;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time interval
//!
//!	Parameters:	\li t - the time interval
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLTimeManagementEvent::SetTimeInterval(double t)
{
	m_dTimeIntervalMSecs = t;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the current state of federation (UNDEFINED=0,RUNNING=1,PAUSED=2)
//!
//!	Parameters:	\li t - the current state of federation (UNDEFINED=0,RUNNING=1,PAUSED=2)
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLTimeManagementEvent::SetFederationExecutionState(int t)
{
	m_iFederationExecutionState = t;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the action the federate should take (UNDEFINED=0,START=1,PAUSE=2,RESUME=3,RUN=4)
//!
//!	Parameters:	\li t - the action the federate should take (UNDEFINED=0,START=1,PAUSE=2,RESUME=3,RUN=4)
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLTimeManagementEvent::SetFederationExecutionDirective(int t)
{
	m_iFederationExecutionDirective = t;
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
CMLPropMember* CMLTimeManagementEvent::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:			

			break;
		
		case ML_XML_BLOCK_DATA:

			pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_DATA, "");
			pXmlBlock->AddChild("timeIntervalMSecs", m_dTimeIntervalMSecs);
			pXmlBlock->AddChild("federationExecutionState", m_iFederationExecutionState);
			pXmlBlock->AddChild("federationExecutionDirective", m_iFederationExecutionDirective);
			
			break;

		case ML_XML_BLOCK_ERROR:

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
char* CMLTimeManagementEvent::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);
	
	lstrcpyn(pszName, "TimeManagementRequest", iSize);
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
CMLPropMember* CMLTimeManagementEvent::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;
	
	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	pXmlRoot->AddAttribute("type", "Request");

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLTimeManagementEvent& CMLTimeManagementEvent::operator = (const CMLTimeManagementEvent& rSource)
{
	Copy(rSource);
	return *this;
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
bool CMLTimeManagementEvent::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	CMLPropMember*	pObjProp = NULL;
	CMLPropMember*	pTypeProp = NULL;
	bool			bSuccessful = true;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:
			
			break;
		
		case ML_XML_BLOCK_DATA:
						
			if(lstrcmpi("timeIntervalMSecs", pMLPropMember->GetName()) == 0)
			{
				m_dTimeIntervalMSecs = pMLPropMember->AsDouble();
				_theReporter.Debug("MLTimeManagementEvent", "SetXMLValue", "SETTING pMLPropMember->GetName():%s; pMLPropMember->GetValue():%s, m_dTimeIntervalMSecs:%14.0f", 
					pMLPropMember->GetName(), pMLPropMember->GetValue(), m_dTimeIntervalMSecs);
			
			}
			else if(lstrcmpi("federationExecutionState", pMLPropMember->GetName()) == 0)
			{
				m_iFederationExecutionState = pMLPropMember->AsInteger();
				/*_theReporter.Debug("MLTimeManagementEvent", "SetXMLValue", "SETTING pMLPropMember->GetName():%s; pMLPropMember->GetValue():%s, m_iFederationExecutionState:%d", 
					pMLPropMember->GetName(), pMLPropMember->GetValue(), m_iFederationExecutionState);*/
			
			}
			else if(lstrcmpi("federationExecutionDirective", pMLPropMember->GetName()) == 0)
			{
				m_iFederationExecutionDirective = pMLPropMember->AsInteger();
				/*_theReporter.Debug("MLTimeManagementEvent", "SetXMLValue", "SETTING pMLPropMember->GetName():%s; pMLPropMember->GetValue():%s, m_iFederationExecutionDirective:%d", 
					pMLPropMember->GetName(), pMLPropMember->GetValue(), m_iFederationExecutionDirective);*/
			
			}
			
			break;

		case ML_XML_BLOCK_ERROR:

			break;

		default:
			
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
