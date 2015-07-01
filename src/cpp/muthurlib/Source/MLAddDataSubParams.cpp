//------------------------------------------------------------------------------
/*! \file	MLAddDataSubParams.cpp
//
//  Contains the implementation of the CMLAddDataSubParams class
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
#include <MLAddDataSubParams.h>
#include <MLHelper.h>

#include <Reporter.h>
extern CReporter _theReporter;

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pICallback - the data event callback interface
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAddDataSubParams::CMLAddDataSubParams(IMLObjectEventCallback* pICallback) 
{
	m_pIObjectEventCallback = NULL;	
	m_posClasses = NULL;
	m_subQueueName = "";

	if(pICallback != NULL)
		SetDataEventCallback(pICallback);

	memset(m_szMuthurOwnershipEventQueueName, 0, sizeof(m_szMuthurOwnershipEventQueueName));
	m_pIObjOwnershipRelinquishEventCallback = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAddDataSubParams::CMLAddDataSubParams(const CMLAddDataSubParams& rSource)
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
CMLAddDataSubParams::~CMLAddDataSubParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a class to the collection of class names
//!
//!	Parameters:	\li pszClassName - the name of the desired class
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
const char* CMLAddDataSubParams::AddClass(const char* pszClassName)
{
	CMLString* pString = NULL;

	if((pszClassName != NULL) && (lstrlen(pszClassName) > 0))
	{
		pString = new CMLString();
		*pString = pszClassName;
	
		m_apClasses.AddTail(pString);
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
void CMLAddDataSubParams::Copy(const CMLAddDataSubParams& rSource)
{
	PTRNODE		Pos = NULL;
	CMLString*	pClass = NULL;

	//	Perform base class processing first
	CMLRequestParams::Copy(rSource);
	
	m_subQueueName = rSource.m_subQueueName; 		
	m_pIObjectEventCallback = rSource.m_pIObjectEventCallback;


	//	Copy all the class names
	m_apClasses.RemoveAll(TRUE);
	const CMLPtrList& rClasses = rSource.m_apClasses;
	Pos = rClasses.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pClass = (CMLString*)(rClasses.GetNext(Pos))) != NULL)
		{
			AddClass(*pClass);
		}
	}

	lstrcpyn(m_szMuthurOwnershipEventQueueName, rSource.m_szMuthurOwnershipEventQueueName, sizeof(m_szMuthurOwnershipEventQueueName));
	m_pIObjOwnershipRelinquishEventCallback = rSource.m_pIObjOwnershipRelinquishEventCallback;
	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the number of classes to be published
//!
//!	Parameters:	None
//!
//!	Returns:	The number of entries in the Classes collection
//------------------------------------------------------------------------------
int CMLAddDataSubParams::GetClassCount()
{
	return m_apClasses.GetCount();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of class names
//!
//!	Parameters:	None
//!
//!	Returns:	The list of classes to be published
//------------------------------------------------------------------------------
const CMLPtrList& CMLAddDataSubParams::GetClasses() const
{
	return m_apClasses;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of class names
//!
//!	Parameters:	None
//!
//!	Returns:	The list of classes to be published
//------------------------------------------------------------------------------
CMLPtrList& CMLAddDataSubParams::GetClasses()
{
	return m_apClasses;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the DataEvent callback interface
//!
//!	Parameters:	None
//!
//!	Returns:	The ambassador callback interface used by this federate
//------------------------------------------------------------------------------
IMLObjectEventCallback* CMLAddDataSubParams::GetObjectEventCallback()
{
	return m_pIObjectEventCallback;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to get the Object Ownership Relinquish callback interface
//!
//!	Parameters:	None
//!
//!	Returns:	The object ownership relinquish callback interface interface used 
//!             by this federate.
//------------------------------------------------------------------------------
IMLObjectOwnershipRelinquishCallback* CMLAddDataSubParams::GetObjectOwnershipRelinquishEventCallback()
{
	return m_pIObjOwnershipRelinquishEventCallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the DataEvent callback interface
//!
//!	Parameters:	\li pICallback - the callback interface used for data events
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataSubParams::SetObjectOwnershipRelinquishEventCallback(IMLObjectOwnershipRelinquishCallback* 
																	pICallback)
{
	m_pIObjOwnershipRelinquishEventCallback = pICallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the muthur ownership event queue name
//!
//!	Parameters:	None
//!
//!	Returns:	The ownership event queue name
//------------------------------------------------------------------------------
char* CMLAddDataSubParams::GetMuthurOwnershipEventQueueName() 
{
	return m_szMuthurOwnershipEventQueueName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the muthur ownership event queue name
//!
//!	Parameters:	\li p - name of the muthur ownership event queue
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataSubParams::SetMuthurOwnershipEventQueueName(char* p)
{
	if(p != NULL)
		lstrcpyn(m_szMuthurOwnershipEventQueueName, p, sizeof(m_szMuthurOwnershipEventQueueName));
	else
		memset(m_szMuthurOwnershipEventQueueName, 0, sizeof(m_szMuthurOwnershipEventQueueName));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the first class in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the first class to be published
//------------------------------------------------------------------------------
const char* CMLAddDataSubParams::GetFirstClass()
{
	CMLString* pString = NULL;

	m_posClasses = m_apClasses.GetHeadPosition();
	if(m_posClasses != NULL)
	{
		pString = (CMLString*)(m_apClasses.GetNext(m_posClasses));
	}
	
	if(pString != NULL)
		return *pString;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the next class in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the next class to be published
//------------------------------------------------------------------------------
const char* CMLAddDataSubParams::GetNextClass()
{
	CMLString* pString = NULL;

	if(m_posClasses != NULL)
	{
		pString = (CMLString*)(m_apClasses.GetNext(m_posClasses));
	}
	
	if(pString != NULL)
		return *pString;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the subscription queue name
//!
//!	Parameters:	None
//!
//!	Returns:	The subscription queue name
//------------------------------------------------------------------------------
char* CMLAddDataSubParams::GetSubscriptionQueueName() 
{ 
	return m_subQueueName; 
}	

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLAddDataSubParams& CMLAddDataSubParams::operator = (const CMLAddDataSubParams& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the DataEvent callback interface
//!
//!	Parameters:	\li pICallback - the callback interface used for data events
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataSubParams::SetDataEventCallback(IMLObjectEventCallback* pICallback)
{
	m_pIObjectEventCallback = pICallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the subscription queue name
//!
//!	Parameters:	\li p - name of the subscription queue
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataSubParams::SetSubscriptionQueueName(char* p)					
{
	if(p != NULL)
		m_subQueueName = p;
	else
		m_subQueueName = "";
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
CMLPropMember* CMLAddDataSubParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*	pXmlBlock = NULL;
	CMLPropMember*	pXmlChild = NULL;
	const char*		pszClass = NULL;
	
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
		case ML_XML_BLOCK_CONTROL:

			break;

		case ML_XML_BLOCK_DATA:

			pXmlChild = new CMLPropMember("federationExecutionModelHandle", m_fedExecModelHandle.GetMuthurId());
			pXmlBlock->AddChild(pXmlChild);

			pXmlChild = new CMLPropMember("subscriptionQueueName", (char*)m_subQueueName);
			pXmlBlock->AddChild(pXmlChild);

			pXmlChild = new CMLPropMember("subscriptions");
			if(m_apClasses.GetCount() > 0)
			{
				pszClass = GetFirstClass();
				while(pszClass != NULL)
				{
					pXmlChild->AddChild("className", (char*)(CMLHelper::GetMuthurClassName(pszClass)));
					pszClass = GetNextClass();
				}
			}
			pXmlBlock->AddChild(pXmlChild);
			pXmlBlock->AddChild("ownershipEventQueueName", m_szMuthurOwnershipEventQueueName);
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
bool CMLAddDataSubParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	CMLPropMember*	pClass = NULL;
	bool			bSuccessful = true;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:
		
		break;

	case ML_XML_BLOCK_DATA:
	
		if(lstrcmpi("federationExecutionModelHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecModelHandle.SetMuthurId(pMLPropMember->GetValue());
		}
		else if(lstrcmpi("subscriptionQueueName", pMLPropMember->GetName()) == 0)
		{
			SetSubscriptionQueueName(pMLPropMember->GetValue());
		}
		else if(lstrcmpi("subscriptions", pMLPropMember->GetName()) == 0)
		{
			pClass = pMLPropMember->GetFirstChild();
			while(pClass != NULL)
			{
				if(lstrcmpi("className", pClass->GetName()) == 0)
				{
					AddClass(CMLHelper::GetCPPClassName(pClass->GetValue()));
				}
				pClass = pMLPropMember->GetNextChild();
			}
		}
		else if(lstrcmpi("ownershipEventQueueName", pMLPropMember->GetName()) == 0)
		{
			lstrcpyn(m_szMuthurOwnershipEventQueueName, pMLPropMember->GetValue(), sizeof(m_szMuthurOwnershipEventQueueName));
			_theReporter.Debug("CMLAddDataSubParams", "SetXmlValue", "m_szMuthurOwnershipEventQueueName:%s",
				m_szMuthurOwnershipEventQueueName);
			bSuccessful = true;
		}
		break;

	default:

		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
