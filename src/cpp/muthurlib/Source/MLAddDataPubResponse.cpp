//------------------------------------------------------------------------------
/*! \file	MLAddDataPubResponse.cpp
//
//  Contains the implementation of the CMLAddDataPubResponse class
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
#include <MLAddDataPubResponse.h>
#include <MLHelper.h>

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
CMLAddDataPubResponse::CMLAddDataPubResponse() : CMLEvent()
{
	m_posClasses = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAddDataPubResponse::CMLAddDataPubResponse(const CMLAddDataPubResponse& rSource)
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
CMLAddDataPubResponse::~CMLAddDataPubResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a class to the collection of class names
//!
//!	Parameters:	\li pszClassName - the name of the desired class
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
const char* CMLAddDataPubResponse::AddClass(const char* pszClassName)
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
void CMLAddDataPubResponse::Copy(const CMLAddDataPubResponse& rSource)
{
	PTRNODE		Pos = NULL;
	CMLString*	pClass = NULL;

	CMLEvent::Copy(rSource); // base class processing first
	
	m_fedExecModelHandle = rSource.m_fedExecModelHandle;

	//	Copy all the published classes
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
	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the number of classes to be published
//!
//!	Parameters:	None
//!
//!	Returns:	The number of entries in the Classes collection
//------------------------------------------------------------------------------
int CMLAddDataPubResponse::GetClassCount()
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
const CMLPtrList& CMLAddDataPubResponse::GetClasses() const
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
CMLPtrList& CMLAddDataPubResponse::GetClasses()
{
	return m_apClasses;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federation execution model handle
//!
//!	Parameters:	None
//!
//!	Returns:	The handle obtained from the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLAddDataPubResponse::GetFedExecModelHandle()
{
	return m_fedExecModelHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the first class in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the first class to be published
//------------------------------------------------------------------------------
const char* CMLAddDataPubResponse::GetFirstClass()
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
const char* CMLAddDataPubResponse::GetNextClass()
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
//!	Summary:	Called to set federate execution model identifier
//!
//!	Parameters:	\li rHandle - the FEM handle returned by the administrator
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataPubResponse::SetFedExecModelHandle(CMLHandle& rHandle)
{
	m_fedExecModelHandle = rHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLAddDataPubResponse& CMLAddDataPubResponse::operator = (const CMLAddDataPubResponse& rSource)
{
	Copy(rSource);
	return *this;
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
CMLPropMember* CMLAddDataPubResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*	pXmlBlock = NULL;
	CMLPropMember*	pXmlChild = NULL;
	const char*		pszClass = NULL;
	
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				// No additional control block properties for this derived class
				break;

			case ML_XML_BLOCK_DATA:
				
				pXmlChild = new CMLPropMember("federationExecutionModelHandle", m_fedExecModelHandle.GetMuthurId());
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
bool CMLAddDataPubResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	CMLPropMember*	pClass = NULL;
	bool			bSuccessful = true;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_DATA:

			if(lstrcmpi("federationExecutionModelHandle", pMLPropMember->GetName()) == 0)
			{
				m_fedExecModelHandle.SetMuthurId(pMLPropMember->GetValue());
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
			else
			{
				bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
			}

			break;

		default:

			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}