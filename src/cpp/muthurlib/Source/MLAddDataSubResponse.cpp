//------------------------------------------------------------------------------
/*! \file	MLAddDataSubResponse.cpp
//
//  Contains the implementation of the CMLAddDataSubResponse class
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
#include <MLAddDataSubResponse.h>

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
CMLAddDataSubResponse::CMLAddDataSubResponse() : CMLEvent()
{
	m_iSubClassCount = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAddDataSubResponse::CMLAddDataSubResponse(const CMLAddDataSubResponse& rSource)
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
CMLAddDataSubResponse::~CMLAddDataSubResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataSubResponse::Copy(const CMLAddDataSubResponse& rSource)
{
	CMLEvent::Copy(rSource); // base class processing first

	m_iSubClassCount = rSource.m_iSubClassCount;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to check the total number of classes that were registered
//!
//!	Parameters:	None
//!
//!	Returns:	The total number registered
//------------------------------------------------------------------------------
int CMLAddDataSubResponse::GetSubClassCount()
{
	return m_iSubClassCount;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLAddDataSubResponse& CMLAddDataSubResponse::operator = (const CMLAddDataSubResponse& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the total number of subscribed classes
//!
//!	Parameters:	\li iSubClassCount - the number of classes subscribed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAddDataSubResponse::SetSubClassCount(int iSubClassCount)
{
	m_iSubClassCount = iSubClassCount;
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
CMLPropMember* CMLAddDataSubResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlClasses = NULL;
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
		case ML_XML_BLOCK_CONTROL:

			pXmlClasses = new CMLPropMember("federationExecutionHandle", m_fedExecHandle.GetMuthurId());
			pXmlBlock->AddChild(pXmlClasses);
			break;

		case ML_XML_BLOCK_DATA:

			// No additional control block properties for this derived class
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
bool CMLAddDataSubResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = true;
	CMLPropMember*		pChild = NULL;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:
		// No base class data in this block
		if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
		}
		else
		{
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
		}
		break;
	default:

		bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}