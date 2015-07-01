//------------------------------------------------------------------------------
/*! \file	MLAddDataPubParams.cpp
//
//  Contains the implementation of the CMLAddDataPubParams class
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
//test

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLAddDataPubParams.h>
#include <MLEvent.h>
#include <MLAmbassador.h>
#include <MLHelper.h>

#include <Reporter.h>
extern CReporter _theReporter;
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
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
CMLAddDataPubParams::CMLAddDataPubParams(IMLDataEventCallback* pICallback)
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
CMLAddDataPubParams::CMLAddDataPubParams(const CMLAddDataPubParams& rSource)
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
CMLAddDataPubParams::~CMLAddDataPubParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a class to the collection of class names
//!
//!	Parameters:	\li pszClassName - the name of the desired class
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
const char* CMLAddDataPubParams::AddClass(const char* pszClassName)
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
void CMLAddDataPubParams::Copy(const CMLAddDataPubParams& rSource)
{
	PTRNODE		Pos = NULL;
	CMLString*	pClass = NULL;

	//	Perform base class processing first
	CMLRequestParams::Copy(rSource);

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
	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the number of classes to be published
//!
//!	Parameters:	None
//!
//!	Returns:	The number of entries in the Classes collection
//------------------------------------------------------------------------------
int CMLAddDataPubParams::GetClassCount()
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
const CMLPtrList& CMLAddDataPubParams::GetClasses() const
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
CMLPtrList& CMLAddDataPubParams::GetClasses()
{
	return m_apClasses;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the name of the first class in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the first class to be published
//------------------------------------------------------------------------------
const char* CMLAddDataPubParams::GetFirstClass()
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
const char* CMLAddDataPubParams::GetNextClass()
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
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLAddDataPubParams& CMLAddDataPubParams::operator = (const CMLAddDataPubParams& rSource)
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
CMLPropMember* CMLAddDataPubParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
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
bool CMLAddDataPubParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
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

			break;

		default:

			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}

