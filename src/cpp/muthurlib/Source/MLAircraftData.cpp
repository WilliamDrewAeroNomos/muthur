//------------------------------------------------------------------------------
/*! \file	MLSpawnAircraft.cpp
//
//  Contains the implementation of the CMLAircraft class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "MLAircraftData.h"
#include <Reporter.h>
CReporter _theReporterSpawnAC;	//!< The global diagnostics / error reporter
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
CMLAircraft::CMLAircraft()
{
	//	Assign defaults
	//CMLDataObject::Reset();
	Reset();
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAircraft::CMLAircraft(const CMLAircraft& rSource)
{
	//CMLDataObject::Copy(rSource);
	Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAircraft::~CMLAircraft()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraft::Copy(const CMLAircraft& rSource)
{	
	CMLDataObject::Copy(rSource);	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraft::Reset()
{
	CMLDataObject::Reset();	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the values of the class members that belong to the
//!				specified data group
//!
//!	Parameters:	\li pPropValues - the element that contains the values
//!
//!	Returns:	true if processed
//------------------------------------------------------------------------------
bool CMLAircraft::SetMemberValues(CMLPropMember* pPropValues)
{
	bool bProcessed = false;
	bProcessed = CMLDataObject::SetMemberValues(pPropValues); 
	return bProcessed;
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
CMLPropMember* CMLAircraft::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlChild = NULL;

	//	Use the base class to allocate the object
	if((pXmlBlock = CMLDataObject::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				// No additional control block properties for this derived class
				break;
			
			case ML_XML_BLOCK_DATA:
				
				break;
			

		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLRequestParams::GetXmlElement(eXmlBlock)) != NULL)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a list of attributes associated with this class
//!
//!	Parameters:	None
//!
//!	Returns:	The a list of attributes associated with this class
//------------------------------------------------------------------------------
//CMLPtrList& CMLAircraft::GetAttributes()
//{
//	return m_apAttributes;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to get the list of attributes associated with this object
////!
////!	Parameters:	None
////!
////!	Returns:	The list of attributes associated with this object
////------------------------------------------------------------------------------
//const CMLPtrList& CMLAircraft::GetAttributes() const
//{	
//	return m_apAttributes;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to get the first attribute in the list
////!
////!	Parameters:	None
////!
////!	Returns:	The first attribute in the local collection
////------------------------------------------------------------------------------
//const char* CMLAircraft::GetFirstAttribute()
//{
//	CMLString* pString = NULL;
//
//	m_posAttributes = m_apAttributes.GetHeadPosition();
//	if(m_posAttributes != NULL)
//	{
//		pString = (CMLString*)(m_apAttributes.GetNext(m_posAttributes));
//	}
//	
//	if(pString != NULL)
//		return *pString;
//	else
//		return NULL;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to get the next attribute in the list
////!
////!	Parameters:	None
////!
////!	Returns:	The next FEM object in the local collection
////------------------------------------------------------------------------------
//const char* CMLAircraft::GetNextAttribute()
//{
//	CMLString* pString = NULL;
//
//	if(m_posAttributes != NULL)
//	{
//		pString = (CMLString*)(m_apAttributes.GetNext(m_posAttributes));
//	}
//	
//	if(pString != NULL)
//		return *pString;
//	else
//		return NULL;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to add an attribute
////!
////!	Parameters:	\li rAddAttr - the attribute to be added
////!
////!	Returns:	The attribute added to the list
////------------------------------------------------------------------------------
//const char* CMLAircraft::AddAttribute(const char* pszAttribute)
//{
//	CMLString* pString = NULL;
//
//	if((pszAttribute != NULL) && (lstrlen(pszAttribute) > 0))
//	{
//		pString = new CMLString();
//		*pString = pszAttribute;
//	
//		m_apAttributes.AddTail(pString);
//	}
//
//	return *pString;
//}