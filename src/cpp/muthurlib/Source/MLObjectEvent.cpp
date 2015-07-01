//------------------------------------------------------------------------------
/*! \file	MLObjectEvent.cpp
//
//  Contains the implementation of the CMLObjectEvent class
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
#include <MLObjectEvent.h>
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
CMLObjectEvent::CMLObjectEvent()
{
	m_pDataObject = NULL;
	m_ulIssuedTimestampMSecs = _theAmbassador.GetSysTimeMsecs();
	m_bSucceeded = true;
	memset(m_szEventName, 0, sizeof(m_szEventName));
	memset(m_szEventSource, 0, sizeof(m_szEventSource));
	memset(m_szUUID, 0, sizeof(m_szUUID));
	memset(m_szDataAction, 0, sizeof(m_szDataAction));	
	m_xmlDataObjectType = NULL;
	m_xmlDataObject = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLObjectEvent::CMLObjectEvent(const CMLObjectEvent& rSource)
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
CMLObjectEvent::~CMLObjectEvent()
{
	//	The event owns the object and is responsible for its release
	if(m_pDataObject != NULL)
		delete m_pDataObject;	

	//if(m_xmlDataObject != NULL)
	//	delete m_xmlDataObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectEvent::Copy(const CMLObjectEvent& rSource)
{
	m_pDataObject = rSource.m_pDataObject;
	m_bSucceeded = rSource.m_bSucceeded;
	m_fedRegHandle = rSource.m_fedExecHandle;
	m_fedExecHandle = rSource.m_fedExecHandle;
	m_fedExecModelHandle = rSource.m_fedExecModelHandle;
	m_ulIssuedTimestampMSecs = rSource.m_ulIssuedTimestampMSecs;
	lstrcpyn(m_szUUID, rSource.m_szUUID, sizeof(m_szUUID));
	lstrcpyn(m_szEventName, rSource.m_szEventName, sizeof(m_szEventName));
	lstrcpyn(m_szEventSource, rSource.m_szEventSource, sizeof(m_szEventSource));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the data object associated with this event
//!
//!	Parameters:	None
//!
//!	Returns:	The associated data object
//------------------------------------------------------------------------------
CMLDataObject* CMLObjectEvent::GetDataObject()
{
	return m_pDataObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the event name
//!
//!	Parameters:	None
//!
//!	Returns:	The event name used by Muthur
//------------------------------------------------------------------------------
char* CMLObjectEvent::GetEventName()
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
char* CMLObjectEvent::GetEventSource()
{
	return m_szEventSource;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the data action
//!
//!	Parameters:	None
//!
//!	Returns:	The data action
//------------------------------------------------------------------------------
char* CMLObjectEvent::GetDataAction()
{
	return m_szDataAction;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLObjectEvent::GetFedExecHandle()
{
	return m_fedExecHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution model handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLObjectEvent::GetFedExecModelHandle()
{
	return m_fedExecModelHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation registration handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLObjectEvent::GetFedRegHandle()
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
bool CMLObjectEvent::GetSucceeded()
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
char* CMLObjectEvent::GetUUID()
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
CMLPropMember* CMLObjectEvent::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:

		pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_CONTROL, "");

		pXmlBlock->AddChild("createTimeMSecs", m_ulIssuedTimestampMSecs);
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

		if(m_pDataObject != NULL)
			pXmlBlock->AddChild("dataType", m_pDataObject->GetXMLName(szXmlValue, sizeof(szXmlValue)));
		else
			pXmlBlock->AddChild("dataType", "null");

		pXmlBlock->AddChild("federationExecutionModelHandle", _theAmbassador.GetFedExecModelHandle());
		pXmlBlock->AddChild("dataAction", m_szDataAction);


		break;

	case ML_XML_BLOCK_ERROR:

		//pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_ERROR, "");
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
//!	Summary:	Called to get the XML that represents the data object
//!
//!	Parameters:	None
//!
//!	Returns:	The prop member containing the XML data
//------------------------------------------------------------------------------
CMLPropMember* CMLObjectEvent::GetXMLDataObject()
{
	return m_xmlDataObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the XML that represents the data object type
//!
//!	Parameters:	None
//!
//!	Returns:	The prop member containing the XML data type
//------------------------------------------------------------------------------
CMLPropMember* CMLObjectEvent::GetXMLDataObjectType()
{
	return m_xmlDataObjectType;
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
char* CMLObjectEvent::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);

	lstrcpyn(pszName, "DataPublicationEvent", iSize);
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
CMLPropMember* CMLObjectEvent::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;

	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	pXmlRoot->AddAttribute("type", "Data");

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to load the XML data object retrieved from the stream
//!
//!	Parameters:	None
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLObjectEvent::LoadXMLDataObject()
{
	CMLString		strXML;
	CMLString		strTempXML;
	CMLPropMember*	pPropTree = NULL;
	CMLPropMember*	pChild = NULL;
	bool			bSuccessful = false;

	char szName[256];

	//	We should have allocated an appropriate data object when we parsed the XML
	if(m_pDataObject && m_xmlDataObject)
	{
		CMLDataStream dataStream;
		CMLHelper::GetXmlStream(m_xmlDataObject, &dataStream);
		strTempXML = dataStream.GetBuffer();

		strXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		strXML += "<event type=\"xmlDataObject\">\n";

		strXML += strTempXML;
		strXML += "</event>";

		if((pPropTree = CMLHelper::GetPropTree(strXML)) != NULL)
		{
			pChild = pPropTree->GetFirstChild();
			CMLString temp = pChild ? pChild->GetName() : NULL;
			while(pChild != NULL)
			{
				m_pDataObject->SetMemberValues(pChild);
				pChild = pPropTree->GetNextChild();
			}

			delete pPropTree;
			bSuccessful = true;
		}
		else
		{
			MessageBox(NULL, strXML, "BAD", MB_OK);
		}
	}

	//	<?xml version="1.0" encoding="UTF-8"?>
	//<event xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" type="Aircraft"
	//	name="Data">

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLObjectEvent& CMLObjectEvent::operator = (const CMLObjectEvent& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the event name
//!
//!	Parameters:	\li pszEventName - the name used to identify the event
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectEvent::SetEventName(char* pszEventName)
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
void CMLObjectEvent::SetEventSource(char* pszEventSource)
{
	if(pszEventSource != NULL)
		lstrcpyn(m_szEventSource, pszEventSource, sizeof(m_szEventSource));
	else
		memset(m_szEventSource, 0, sizeof(m_szEventSource));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the data action
//!
//!	Parameters:	\li pszDataAction - the data action
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectEvent::SetDataAction(char* pszDataAction)
{
	if(pszDataAction != NULL)
		lstrcpyn(m_szDataAction, pszDataAction, sizeof(m_szDataAction));
	else
		memset(m_szDataAction, 0, sizeof(m_szDataAction));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate execution handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectEvent::SetFedExecHandle(CMLHandle& rHandle)
{
	m_fedExecHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate execution model handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectEvent::SetFedExecModelHandle(CMLHandle& rHandle)
{
	m_fedExecModelHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set federate registration handle
//!
//!	Parameters:	\li rHandle - the handle returned by the Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectEvent::SetFedRegHandle(CMLHandle& rHandle)
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
void CMLObjectEvent::SetSucceeded(bool bSucceeded)
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
void CMLObjectEvent::SetUUID(char* pszUUID)
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
bool CMLObjectEvent::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	CMLPropMember*	pObjProp = NULL;
	CMLPropMember*	pTypeProp = NULL;
	bool			bSuccessful = true;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:

		if(lstrcmpi("createTimeMSecs", pMLPropMember->GetName()) == 0)
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
		}
		else if(lstrcmpi("eventUUID", pMLPropMember->GetName()) == 0)
		{
			lstrcpyn(m_szUUID, pMLPropMember->GetValue(), sizeof(m_szUUID));
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
		_theReporter.Debug("", "MLObjectEvent", " 1. Name:%s Value:%s", pMLPropMember->GetName(),
			pMLPropMember->GetValue());
		if(lstrcmpi("dataType", pMLPropMember->GetName()) == 0)
		{
			//	Delete the existing object if there is one
			_theReporter.Debug("", "MLObjectEvent", " 2. Name:%s Value:%s", pMLPropMember->GetName(),
				pMLPropMember->GetValue());
			if(m_pDataObject != NULL)
			{
				delete m_pDataObject;
				m_pDataObject = NULL;
			}
			_theReporter.Debug("", "MLObjectEvent", " 3. Name:%s Value:%s", pMLPropMember->GetName(),
				pMLPropMember->GetValue());
			//if(m_xmlDataObjectType)
			//{
			//	delete m_xmlDataObjectType;
			//	m_xmlDataObjectType = NULL;
			//}
			/*_theReporter.Debug("", "MLObjectEvent", " 4. Name:%s Value:%s", pMLPropMember->GetName(),
			pMLPropMember->GetValue());*/
			//	Now allocate an appropriate object
			m_pDataObject = CMLDataObject::GetXMLTypeAsObject(pMLPropMember->GetValue());
			_theReporter.Debug("", "MLObjectEvent", " 5 Type of dataobject:%s", typeid(*m_pDataObject).name());
			if(m_pDataObject == NULL)
			{
				_theReporter.Debug("", "", "Unable to allocate data object for class named : <%s>", pMLPropMember->GetValue());
			}
			//else
			//{
			//	// Load the data object here if dataObjectAsXml node has been parsed already
			//	if(m_xmlDataObject)
			//	{
			//		CMLPtrList pChildren = m_xmlDataObject->GetChildren();
			//		PTRNODE			Pos = NULL;
			//		CMLPropMember*	pChild = NULL;
			//		int iTemp = pChildren.GetCount();
			//		Pos = pChildren.GetHeadPosition();
			//		if(Pos != NULL)
			//		{
			//			pChild = (CMLPropMember*)(pChildren.GetAt(Pos));
			//			while(pChild)
			//			{
			//				CMLString temp = pChild->GetName();
			//				m_pDataObject->SetMemberValues(pChild);
			//				Pos = Pos->pNext;
			//				pChild = (CMLPropMember*)(pChildren.GetAt(Pos));
			//			}
			//		}
			//	}
			//	else
			//	{
			//		// need to store temp copy of node and parse in dataObjectAsXml
			//		m_xmlDataObjectType = pMLPropMember ? new CMLPropMember(*pMLPropMember) : NULL;		
			//		_theReporter.Debug("", "MLObjectEvent", 
			//			"Not loading data object here...dataObjectAsXml node not parsed yet!");
			//	}
			//}

		}
		else if(lstrcmpi("dataObjectAsXML", pMLPropMember->GetName()) == 0)
		{
			//	Store the data until we finish parsing the XML
			//m_xmlDataObject = pMLPropMember->GetValue();
			/*m_xmlDataObject = pMLPropMember ? new CMLPropMember(*pMLPropMember) : NULL;	
			int iTemp = pMLPropMember->GetChildren().GetCount();
			_theReporter.Debug("", "MLObjectEvent", " 6. Name:%s Value:%s; Child Count:%d", m_xmlDataObject->GetName(),
			m_xmlDataObject->GetValue(), iTemp);

			char * tempValue = pMLPropMember->GetValue();
			_theReporter.Debug("", "MLObjectEvent", " 7.  Name:%s Value:%s; Child Count:%d", m_xmlDataObject->GetName(),
			m_xmlDataObject->GetValue(), iTemp);*/

			// If data object type is NULL then we know that dataObjectAsXml was parsed before
			// the data type so we know that we need to allocate and load the data object here.
			//if(m_xmlDataObjectType)
			//{
			//	Now allocate an appropriate object
			//m_pDataObject = CMLDataObject::GetXMLTypeAsObject(m_xmlDataObjectType->GetValue());
			//_theReporter.Debug("", "MLObjectEvent", " 8. Obj. Type Name:%s; Obj. TypeValue:%s; Child Count:%d", m_xmlDataObjectType->GetName(),
			//	m_xmlDataObjectType->GetValue(), iTemp);


			if(m_pDataObject == NULL)
			{
				_theReporter.Debug("", "",
					"Unable to allocate data object (in dataObjectAsXml) for class named : <%s>",
					m_xmlDataObjectType->GetValue());
			}
			else
			{
				//int iTemp = pMLPropMember->GetChildren().GetCount();
				_theReporter.Debug("", "MLObjectEvent", " 9.  Name:%s Value:%s", pMLPropMember->GetName(),
					pMLPropMember->GetValue());

				// Load the data object here if dataObjectAsXml node has been parsed already
				/*if(m_xmlDataObject)
				{*/
				/*CMLPtrList pChildren = pMLPropMember->GetChildren();
				PTRNODE			Pos = NULL;
				CMLPropMember*	pChild = NULL;
				int iTemp = pChildren.GetCount();
				_theReporter.Debug("", "MLObjectEvent", " 10.  Name:%s Value:%s; Child Count:%d", m_xmlDataObject->GetName(),
				m_xmlDataObject->GetValue(), iTemp);

				Pos = pChildren.GetHeadPosition();
				if(Pos != NULL)
				{
				pChild = (CMLPropMember*)(pChildren.GetAt(Pos));
				while(pChild)
				{
				CMLString temp = pChild->GetName();*/
				//if(pMLPropMember->GetFirstChild())
				CMLPropMember *pChild = pMLPropMember->GetFirstChild();
				while(pChild)
				{
					_theReporter.Debug("", "MLObjectEvent", " 9A.  Name:%s Value:%s", pChild->GetName(), pChild->GetValue());
					m_pDataObject->SetMemberValues(pChild);
					pChild = pMLPropMember->GetNextChild();
				}
				/*else
					_theReporter.Debug("CMLObjectEvent", "SetXmlValue", "First child is NULL!!");*/
				/*Pos = Pos->pNext;
				pChild = (CMLPropMember*)(pChildren.GetAt(Pos));
				}
				}*/
				//}
				//else
				//{
				//	// need to store temp copy of node and parse in dataObjectAsXml
				//	m_xmlDataObjectType = pMLPropMember ? new CMLPropMember(*pMLPropMember) : NULL;		
				//	_theReporter.Debug("", "MLObjectEvent", 
				//		"Not loading data object here...dataObjectAsXml node not parsed yet!");
			}
			//}
			//}		
		}
		else if(lstrcmpi("dataAction", pMLPropMember->GetName()) == 0)
		{
			lstrcpyn(m_szDataAction, pMLPropMember->GetValue(), sizeof(m_szDataAction));
		}
		break;

	case ML_XML_BLOCK_ERROR:

		break;

	default:

		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
