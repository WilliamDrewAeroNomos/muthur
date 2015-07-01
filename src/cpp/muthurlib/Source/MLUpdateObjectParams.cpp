//------------------------------------------------------------------------------
/*! \file	MLUpdateObjectParams.cpp
//
//  Contains the implementation of the CMLUpdateObjectParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-27-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLUpdateObjectParams.h>
#include <MLHelper.h>
#include <MLAmbassador.h>
#include <Reporter.h>
//#define _DEBUG_DATAPUB 1   //Uncomment this line to enable debugging

extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
extern CReporter		_theReqReporter;	//!< The global diagnostics / error reporter


//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pDataObject - the data to be published
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLUpdateObjectParams::CMLUpdateObjectParams(CMLDataObject* pDataObject) 
			     :CMLRequestParams()
{
	m_pDataObject = NULL;
	SetRequestType(ML_REQUEST_STANDARD);
	if(pDataObject != NULL)
		SetDataObject(pDataObject);

}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLUpdateObjectParams::CMLUpdateObjectParams(const CMLUpdateObjectParams& rSource)
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
CMLUpdateObjectParams::~CMLUpdateObjectParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLUpdateObjectParams::Copy(const CMLUpdateObjectParams& rSource)
{
	CMLRequestParams::Copy(rSource);

	m_pDataObject = rSource.m_pDataObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the data to be published
//!
//!	Parameters:	None
//!
//!	Returns:	The data object being published
//------------------------------------------------------------------------------
CMLDataObject* CMLUpdateObjectParams::GetDataObject()
{
	return m_pDataObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLUpdateObjectParams& CMLUpdateObjectParams::operator = (const CMLUpdateObjectParams& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the data to be published
//!
//!	Parameters:	\li pDataObject - the data object used to publish the data
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLUpdateObjectParams::SetDataObject(CMLDataObject* pDataObject)
{
	m_pDataObject = pDataObject;
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
CMLPropMember* CMLUpdateObjectParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*	pXmlBlock = NULL;
	CMLPropMember*	pXmlClasses = NULL;
	CMLPropMember*	pDataObjAsXml = NULL;
	CMLDataStream	dataStream;
	char			szDataType[128];
	
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:
				break;
			
			case ML_XML_BLOCK_DATA:
				

				pDataObjAsXml = m_pDataObject->GetXmlBlock(eXmlBlock);
				pDataObjAsXml->SetName("dataObjectAsXML");
				pXmlBlock->AddChild(pDataObjAsXml);


				pXmlBlock->AddChild("dataType", m_pDataObject->GetXMLName(szDataType, sizeof(szDataType)));
				pXmlBlock->AddChild("federationExecutionModelHandle", m_fedExecModelHandle.GetMuthurId());
				
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
bool CMLUpdateObjectParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = false;
	CMLPropMember*		pChild = NULL;
	CMLPropMember*		pCopypMLPropMember = pMLPropMember ? new CMLPropMember(*pMLPropMember) : NULL;

	_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "pMLPropMember->GetName():%s", pMLPropMember->GetName());

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_DATA:		
		_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "pMLPropMember->GetName():%s", pMLPropMember->GetName());
		
		// Need to allocate appropriate data object based on XML first then populate it
		if(pCopypMLPropMember)
		{
			while(pCopypMLPropMember)
			{
				_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "1 pCopypMLPropMember->GetName():%s", pCopypMLPropMember->GetName());
				if(lstrcmpi("dataType", pCopypMLPropMember->GetName()) == 0)
				{
					char *pDataType = pCopypMLPropMember->GetName();
					m_pDataObject = CMLDataObject::GetXMLTypeAsObject(pDataType);
					break;
				}
				else
				{
					
					pCopypMLPropMember = pCopypMLPropMember->GetNextChild();
					_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "2 pCopypMLPropMember->GetName():%s", pCopypMLPropMember->GetName());
					continue;
				}

			}
		}

		if(lstrcmpi("dataObjectAsXML", pMLPropMember->GetName()) == 0)
		{
			CMLPropMember *pChild = pMLPropMember->GetFirstChild();
			if(pChild)
			{
				_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "pChild->GetName():%s; pChild->GetValue():%s",
					pChild->GetName(), pChild->GetValue());
			}
			else
			{
				_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "pChild is NULL!!");
			}
			
			if(m_pDataObject)
				//m_pDataObject->SetMemberValues(pChild);
				m_pDataObject->SetMemberValues(pMLPropMember);
			else
				_theReqReporter.Debug("CMLUpdateObjectParams", "SetXmlValue", "m_pDataObject is NULL!!");
		}
		else if(lstrcmpi("federationExecutionModelHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecModelHandle.SetMuthurId(pMLPropMember->GetValue());
			bSuccessful = true;
		}
		
		break;	
	default:

		bSuccessful = CMLRequestParams::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}