//------------------------------------------------------------------------------
/*! \file	MLAircraftTaxiOut.cpp
//
//  Contains the implementation of the CMLAircraftTaxiOut class
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
#include "MLAircraftTaxiOut.h"
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
CMLAircraftTaxiOut::CMLAircraftTaxiOut()
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
CMLAircraftTaxiOut::CMLAircraftTaxiOut(const CMLAircraftTaxiOut& rSource)
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
CMLAircraftTaxiOut::~CMLAircraftTaxiOut()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftTaxiOut::Copy(const CMLAircraftTaxiOut& rSource)
{	
	PTRNODE				Pos = NULL;
	CMLString	*pAttribute = NULL;

	CMLDataObject::Copy(rSource);
	m_dTaxiOutTimeMSecs = rSource.m_dTaxiOutTimeMSecs;	
	lstrcpy(m_szTaxiOutGate, rSource.m_szTaxiOutGate); 

	//	Copy all the attributes
	//const CMLPtrList& rAttributes = rSource.m_apAttributes;
	//Pos = rAttributes.GetHeadPosition();
	//while(Pos != NULL)
	//{
	//	if((pAttribute = (CMLString*)(rAttributes.GetNext(Pos))) != NULL)
	//	{
	//		AddAttribute(*pAttribute); // add a copy
	//	}
	//}
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
CMLPropMember* CMLAircraftTaxiOut::GetXmlBlock(EMLXMLBlock eXmlBlock)
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

				//	Create a child for the taxi out data
				pXmlChild = new CMLPropMember(ML_XML_DATA_BLOCK_TAXI_OUT, "");
				pXmlChild->AddChild(TAXI_OUT_TIME, m_dTaxiOutTimeMSecs, 0);
				pXmlChild->AddChild(TAXI_OUT_GATE, m_szTaxiOutGate);
				pXmlBlock->AddChild(pXmlChild); // add <taxiOutData> subblock
				
				break;

		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLRequestParams::GetXmlElement(eXmlBlock)) != NULL)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftTaxiOut::Reset()
{				
	CMLDataObject::Reset();
	m_dTaxiOutTimeMSecs = 0;
	memset(m_szTaxiOutGate, 0, sizeof(m_szTaxiOutGate));
	
	/*AddAttribute(TAXI_OUT_TIME);
	AddAttribute(TAXI_OUT_GATE);*/
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the gate where the aircraft is assigned to park
//!
//!	Parameters:	None
//!
//!	Returns:	Center where the gate where the aircraft is assigned to park
//------------------------------------------------------------------------------
char* CMLAircraftTaxiOut::GetTaxiOutGate() { return m_szTaxiOutGate; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time in milliseconds when the aircraft begins taxiing to the runway
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds when the aircraft begins taxiing to the runway
//------------------------------------------------------------------------------
double CMLAircraftTaxiOut::GetTaxiOutTimeMSecs() { return m_dTaxiOutTimeMSecs; }				

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time in milliseconds when the aircraft begins taxiing to the runway
//!
//!	Parameters:	\li l - time in milliseconds when the aircraft begins taxiing to the runway
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftTaxiOut::SetTaxiOutTimeMSecs(double d)	
{
	m_dTaxiOutTimeMSecs = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the gate where the aircraft is assigned to park
//!
//!	Parameters:	\li p - the gate where the aircraft is assigned to park
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftTaxiOut::SetTaxiOutGate(char* p)			
{
	if(p != NULL)
		lstrcpyn(m_szTaxiOutGate, p, sizeof(m_szTaxiOutGate));
	else
		memset(m_szTaxiOutGate, 0, sizeof(m_szTaxiOutGate));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the values of the class members that belong to the
//!				specified data group
//!
//!	Parameters:	\li pPropValues - the element that contains the values
//!
//!	Returns:	true if processed
//------------------------------------------------------------------------------
bool CMLAircraftTaxiOut::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild = NULL;
	bool			bProcessed = false;

	// Is this the <taxiOutData> group?
	if(lstrcmpi(ML_XML_DATA_BLOCK_TAXI_OUT, pPropValues->GetName()) == 0)
	{
		// Process all the child members
		pChild = pPropValues->GetFirstChild();
		while(pChild != NULL)
		{
			if(lstrcmpi(TAXI_OUT_TIME, pChild->GetName()) == 0)
			{
				m_dTaxiOutTimeMSecs = pChild->AsDouble();
			}
			else if(lstrcmpi(TAXI_OUT_GATE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szTaxiOutGate, pChild->GetValue());
			}
			
			pChild = pPropValues->GetNextChild();
		
		}// while(pChild != NULL)
		
		bProcessed = true;
		
	}
	else
	{
		//	Let the base class handle it
		bProcessed = CMLDataObject::SetMemberValues(pPropValues);
	}

	return bProcessed;
}


//------------------------------------------------------------------------------
//!	Summary:	Called to get a list of attributes associated with this class
//!
//!	Parameters:	None
//!
//!	Returns:	The a list of attributes associated with this class
//------------------------------------------------------------------------------
//CMLPtrList& CMLAircraftTaxiOut::GetAttributes()
//{
//	return m_apAttributes;
//}
//
//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of attributes associated with this object
//!
//!	Parameters:	None
//!
//!	Returns:	The list of attributes associated with this object
//------------------------------------------------------------------------------
//const CMLPtrList& CMLAircraftTaxiOut::GetAttributes() const
//{	
//	return m_apAttributes;
//}
//
//------------------------------------------------------------------------------
//!	Summary:	Called to get the first attribute in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first attribute in the local collection
//------------------------------------------------------------------------------
//const char* CMLAircraftTaxiOut::GetFirstAttribute()
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
//------------------------------------------------------------------------------
//!	Summary:	Called to get the next attribute in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The next FEM object in the local collection
//------------------------------------------------------------------------------
//const char* CMLAircraftTaxiOut::GetNextAttribute()
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
//------------------------------------------------------------------------------
//!	Summary:	Called to add an attribute
//!
//!	Parameters:	\li rAddAttr - the attribute to be added
//!
//!	Returns:	The attribute added to the list
//------------------------------------------------------------------------------
//const char* CMLAircraftTaxiOut::AddAttribute(const char* pszAttribute)
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
