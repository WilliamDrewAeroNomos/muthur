//------------------------------------------------------------------------------
/*! \file	MLDeleteObjectParams.cpp
//
//  Contains the implementation of the CMLDeleteObjectParams class
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
#include <MLDeleteObjectParams.h>
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
CMLDeleteObjectParams::CMLDeleteObjectParams() 
			     :CMLRequestParams()
{	
	SetRequestType(ML_REQUEST_STANDARD);
	memset(m_szDataObjUUID, 0, sizeof(m_szDataObjUUID));
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLDeleteObjectParams::CMLDeleteObjectParams(const CMLDeleteObjectParams& rSource)
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
CMLDeleteObjectParams::~CMLDeleteObjectParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDeleteObjectParams::Copy(const CMLDeleteObjectParams& rSource)
{
	CMLRequestParams::Copy(rSource);

	lstrcpy(m_szDataObjUUID, rSource.m_szDataObjUUID);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the UUID associated with the object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to the object
//------------------------------------------------------------------------------
char* CMLDeleteObjectParams::GetDataObjUUID()
{
	return m_szDataObjUUID;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLDeleteObjectParams& CMLDeleteObjectParams::operator = (const CMLDeleteObjectParams& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the unique identifier for this object
//!
//!	Parameters:	\li pszUUID - the unique ID assigned to this object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDeleteObjectParams::SetDataObjUUID(char* pszUUID)
{
	if(pszUUID != NULL)
		lstrcpyn(m_szDataObjUUID, pszUUID, sizeof(m_szDataObjUUID));
	else
		memset(m_szDataObjUUID, 0, sizeof(m_szDataObjUUID));
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
CMLPropMember* CMLDeleteObjectParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
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
				

				//pDataObjAsXml = m_pDataObject->GetXmlBlock(eXmlBlock);
				//pDataObjAsXml->SetName("dataObjectAsXML");
				//pXmlBlock->AddChild(pDataObjAsXml);


				//pXmlBlock->AddChild("dataType", m_pDataObject->GetXMLName(szDataType, sizeof(szDataType)));
				pXmlBlock->AddChild("dataObjectUUID", m_szDataObjUUID);
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
bool CMLDeleteObjectParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = false;
	CMLPropMember*		pChild = NULL;
	CMLPropMember*		pCopypMLPropMember = pMLPropMember ? new CMLPropMember(*pMLPropMember) : 
		NULL;

	_theReqReporter.Debug("CMLDeleteObjectParams", "SetXmlValue", "pMLPropMember->GetName():%s",
		pMLPropMember->GetName());

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_DATA:		
		_theReqReporter.Debug("CMLDeleteObjectParams", "SetXmlValue", "pMLPropMember->GetName():%s",
			pMLPropMember->GetName());
		
		if(lstrcmpi("dataObjectUUID", pChild->GetName()) == 0)
		{	
			lstrcpyn(m_szDataObjUUID, pChild->GetValue(), sizeof(m_szDataObjUUID));
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