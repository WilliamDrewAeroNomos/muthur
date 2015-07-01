//------------------------------------------------------------------------------
/*! \file	MLTransferObjectOwnershipRequest.cpp
//
//  Contains the implementation of the CMLTransferObjectOwnershipParams class
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
#include <MLTransferObjectOwnershipRequest.h>
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
CMLTransferObjectOwnershipParams::CMLTransferObjectOwnershipParams() 
:CMLRequestParams()
{	
	SetRequestType(ML_REQUEST_STANDARD);
	memset(m_szDataObjectUUID, 0, sizeof(m_szDataObjectUUID));
	//m_ulCreatedTimestampMSecs = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLTransferObjectOwnershipParams::CMLTransferObjectOwnershipParams(const CMLTransferObjectOwnershipParams& rSource)
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
CMLTransferObjectOwnershipParams::~CMLTransferObjectOwnershipParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLTransferObjectOwnershipParams::Copy(const CMLTransferObjectOwnershipParams& rSource)
{
	CMLRequestParams::Copy(rSource);

	lstrcpy(m_szDataObjectUUID, rSource.m_szDataObjectUUID);
	//m_ulCreatedTimestampMSecs = rSource.m_ulCreatedTimestampMSecs;

}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the UUID associated with the object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to the object
//------------------------------------------------------------------------------
char* CMLTransferObjectOwnershipParams::GetDataObjectUUID()
{
	return m_szDataObjectUUID;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLTransferObjectOwnershipParams& CMLTransferObjectOwnershipParams::operator = (const CMLTransferObjectOwnershipParams& rSource)
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
void CMLTransferObjectOwnershipParams::SetDataObjectUUID(char* pszUUID)
{
	if(pszUUID != NULL)
		lstrcpyn(m_szDataObjectUUID, pszUUID, sizeof(m_szDataObjectUUID));
	else
		memset(m_szDataObjectUUID, 0, sizeof(m_szDataObjectUUID));
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
CMLPropMember* CMLTransferObjectOwnershipParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
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
			//pXmlBlock->AddChild("createEventTimeMSecs", m_ulCreatedTimestampMSecs);
			break;

		case ML_XML_BLOCK_DATA:


			//pDataObjAsXml = m_pDataObject->GetXmlBlock(eXmlBlock);
			//pDataObjAsXml->SetName("dataObjectAsXML");
			//pXmlBlock->AddChild(pDataObjAsXml);


			//pXmlBlock->AddChild("dataType", m_pDataObject->GetXMLName(szDataType, sizeof(szDataType)));
			pXmlBlock->AddChild("dataObjectUUID", m_szDataObjectUUID);
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
bool CMLTransferObjectOwnershipParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = false;
	CMLPropMember*		pChild = NULL;
	CMLPropMember*		pCopypMLPropMember = pMLPropMember ? new CMLPropMember(*pMLPropMember) : 
		NULL;

	_theReqReporter.Debug("CMLTransferObjectOwnershipParams", "SetXmlValue", "pMLPropMember->GetName():%s",
		pMLPropMember->GetName());

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_DATA:		
		_theReqReporter.Debug("CMLTransferObjectOwnershipParams", "SetXmlValue", "pMLPropMember->GetName():%s",
			pMLPropMember->GetName());

		if(lstrcmpi("dataObjectUUID", pChild->GetName()) == 0)
		{	
			lstrcpyn(m_szDataObjectUUID, pChild->GetValue(), sizeof(m_szDataObjectUUID));
		}
		else if(lstrcmpi("federationExecutionModelHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecModelHandle.SetMuthurId(pMLPropMember->GetValue());
			bSuccessful = true;
		}

		break;

		//case ML_XML_BLOCK_CONTROL:
		//	if(lstrcmpi("createEventTimeMSecs", pMLPropMember->GetName()) == 0)
		//	{
		//		//m_ulCreatedTimestampMSecs = pMLPropMember->AsUnsignedLong(); //>AsLong();
		//		bSuccessful = true;
		//	}
		//	break;
	default:

		bSuccessful = CMLRequestParams::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}