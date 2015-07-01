//------------------------------------------------------------------------------
/*! \file	MLFlightPosition.cpp
//
//  Contains the implementation of the CMLFlightPosition class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-01-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "MLFlightPosition.h"
#include "float.h"
#include "Reporter.h"
//#include <MLAmbassador.h>
//extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
extern CReporter		_theReporter;	//!< The global diagnostics / error reporter
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
CMLFlightPosition::CMLFlightPosition()
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
CMLFlightPosition::CMLFlightPosition(const CMLFlightPosition& rSource)
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
CMLFlightPosition::~CMLFlightPosition()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::Copy(const CMLFlightPosition& rSource)
{	
	CMLDataObject::Copy(rSource);
	
	m_dbPitchDegrees = rSource.m_dbPitchDegrees;
	m_dCruiseAltFeet = rSource.m_dCruiseAltFeet;
	m_dbLatitudeDegrees = rSource.m_dbLatitudeDegrees;										
	m_dbLongitudeDegrees = rSource.m_dbLongitudeDegrees;									
	m_dbAltitudeFt = rSource.m_dbAltitudeFt;													
	m_dbGroundspeedKts = rSource.m_dbGroundspeedKts;												
	m_dbHeadingDegrees = rSource.m_dbHeadingDegrees;													
	m_dbAirspeedKts = rSource.m_dbAirspeedKts;															
	m_dbRollDegrees = rSource.m_dbRollDegrees;												
	lstrcpy(m_szSector, rSource.m_szSector); 								
	lstrcpy(m_szCenter, rSource.m_szCenter); 	
	m_dbVerticalSpeedKnots = rSource.m_dbVerticalSpeedKnots;		
	m_bAcOnGround = rSource.m_bAcOnGround;
	m_iFrequency = rSource.m_iFrequency;		
	lstrcpy(m_szSquawkCode, rSource.m_szSquawkCode); 	
	m_bIdent = rSource.m_bIdent;
	
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
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::Reset()
{
	CMLDataObject::Reset();
	
	m_dbPitchDegrees = 0;
	m_dCruiseAltFeet = 0;
	m_dbLatitudeDegrees = 0;										
	m_dbLongitudeDegrees = 0;									
	m_dbAltitudeFt = 0;											
	m_dbGroundspeedKts = 0;									
	m_dbHeadingDegrees = 0;									
	m_dbAirspeedKts = 0;											
	m_dbRollDegrees = 0;											
	memset(m_szSector, 0, sizeof(m_szSector));
	memset(m_szCenter, 0, sizeof(m_szCenter));	
	m_dbVerticalSpeedKnots = 0;
	m_bAcOnGround = true;
	m_iFrequency = 0;
	memset(m_szSquawkCode, 0, sizeof(m_szSquawkCode));	
	m_bIdent = false;		

	/*AddAttribute(LATITUDE);
	AddAttribute(LONGITUDE);
	AddAttribute(ALTITUDE);
	AddAttribute(GROUNDSPEED);
	AddAttribute(CRUISE_ALT);
	AddAttribute(HEADING);
	AddAttribute(AIRSPEED);
	AddAttribute(PITCH);
	AddAttribute(ROLL);
	AddAttribute(SECTOR);
	AddAttribute(IDENT);
	AddAttribute(CENTER);
	AddAttribute(VERTSPEED);
	AddAttribute(ONGROUND);
	AddAttribute(TRANSFREQ);
	AddAttribute(SQUAWKCODE);
	AddAttribute(IDENT);*/
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the difference between the air and ground speed (assuming no wind)
//!
//!	Parameters:	None
//!
//!	Returns:	The difference between the air and ground speed (assuming no wind)
//------------------------------------------------------------------------------
double CMLFlightPosition::GetVerticalSpeed()
{
	return m_dbVerticalSpeedKnots;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve if the A/C is on the ground
//!
//!	Parameters:	None
//!
//!	Returns:	If the A/C is on the ground
//------------------------------------------------------------------------------
bool CMLFlightPosition::GetIsAcOnGround()					
{
	return m_bAcOnGround;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the frequency in MHz on which A/C is transmitting
//!
//!	Parameters:	None
//!
//!	Returns:	The frequency in MHz on which A/C is transmitting
//------------------------------------------------------------------------------
int CMLFlightPosition::GetFrequency()
{
	return m_iFrequency;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the transponder code assigned to the A/C
//!
//!	Parameters:	None
//!
//!	Returns:	The transponder code assigned to the A/C
//------------------------------------------------------------------------------
char* CMLFlightPosition::GetSquawkCode()
{
	return m_szSquawkCode;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the bool representing whether or not A/C is identifying itself on radar scope
//!
//!	Parameters:	None
//!
//!	Returns:	bool representing whether or not A/C is identifying itself on radar scope
//------------------------------------------------------------------------------
bool CMLFlightPosition::GetIdent()
{
	return m_bIdent;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the current latitude of an aircraft’s location in decimal format
//!
//!	Parameters:	None
//!
//!	Returns:	The current latitude of an aircraft’s location in decimal format
//------------------------------------------------------------------------------
double CMLFlightPosition::GetLatitudeDegrees() { return m_dbLatitudeDegrees; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the current longitude of an aircraft’s location in decimal format
//!
//!	Parameters:	None
//!
//!	Returns:	The current longitude of an aircraft’s location in decimal format
//------------------------------------------------------------------------------
double CMLFlightPosition::GetLongitudeDegrees() { return m_dbLongitudeDegrees; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the current altitude (above MSL) of the aircraft in feet
//!
//!	Parameters:	None
//!
//!	Returns:	The current altitude (above MSL) of the aircraft in feet
//------------------------------------------------------------------------------
double CMLFlightPosition::GetAltitudeFt() { return m_dbAltitudeFt; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the ground speed in knots
//!
//!	Parameters:	None
//!
//!	Returns:	The ground speed in knots
//------------------------------------------------------------------------------
double CMLFlightPosition::GetGroundspeedKts() { return m_dbGroundspeedKts; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
//!
//!	Parameters:	None
//!
//!	Returns:	The aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
//------------------------------------------------------------------------------
double CMLFlightPosition::GetHeadingDegrees() { return m_dbHeadingDegrees; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the current air speed of the aircraft in knots
//!
//!	Parameters:	None
//!
//!	Returns:	The current air speed of the aircraft in knots
//------------------------------------------------------------------------------
double CMLFlightPosition::GetAirspeedKts() { return m_dbAirspeedKts; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the angle between the reference plane and the aircraft’s +y body axis along 
//!				a plane perpendicular to the aircraft’s x body axis.
//!
//!	Parameters:	None
//!
//!	Returns:	The angle between the reference plane and the aircraft’s +y body axis along a plane perpendicular to 
//!				the aircraft’s x body axis.
//------------------------------------------------------------------------------
double CMLFlightPosition::GetRollDegrees() { return m_dbRollDegrees; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the name of the air traffic controlled sector (center, terminal or tower) which an 
//!				aircraft is current flying.
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the air traffic controlled sector (center, terminal or tower) which an aircraft is current flying.
//------------------------------------------------------------------------------
char* CMLFlightPosition::GetSector() { return m_szSector; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the name of the EnRoute center in which the aircraft is currently flying
//!
//!	Parameters:	None
//!
//!	Returns:	The name of the EnRoute center in which the aircraft is currently flying
//------------------------------------------------------------------------------
char* CMLFlightPosition::GetCenter() { return m_szCenter; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the pitch in degrees
//!
//!	Parameters:	None
//!
//!	Returns:	The pitch in degrees
//------------------------------------------------------------------------------
double CMLFlightPosition::GetPitchDegrees() { return m_dbPitchDegrees; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the cruise altitude of the air craft in feet
//!
//!	Parameters:	None
//!
//!	Returns:	The name of cruise altitude of the air craft in feet
//------------------------------------------------------------------------------
double CMLFlightPosition::GetFlightPosCruiseAltitudeFeet() { return m_dCruiseAltFeet; }	


//------------------------------------------------------------------------------
//!	Summary:	Called to set the current latitude of an aircraft’s location in decimal format
//!
//!	Parameters:	\li d - current latitude of an aircraft’s location in decimal format
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetFlightPosCruiseAltitudeFeet(double d)	
{
	m_dCruiseAltFeet = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the current latitude of an aircraft’s location in decimal format
//!
//!	Parameters:	\li d - current latitude of an aircraft’s location in decimal format
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetLatitudeDegrees(double d)	
{
	m_dbLatitudeDegrees = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the current longitude of an aircraft’s location in decimal format
//!
//!	Parameters:	\li d - current longitude of an aircraft’s location in decimal format
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetLongitudeDegrees(double d)	
{
	m_dbLongitudeDegrees = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the current altitude (above MSL) of the aircraft in feet
//!
//!	Parameters:	\li d - current altitude (above MSL) of the aircraft in feet
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetAltitudeFt(double d)			
{
	m_dbAltitudeFt = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the ground speed in knots
//!
//!	Parameters:	\li d - ground speed in knots
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetGroundspeedKts(double d)				//!< Set the 
{
	m_dbGroundspeedKts = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
//!
//!	Parameters:	\li d - aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetHeadingDegrees(double d)
{
	m_dbHeadingDegrees = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the current air speed of the aircraft in knots
//!
//!	Parameters:	\li d - current air speed of the aircraft in knots
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetAirspeedKts(double d)			
{
	m_dbAirspeedKts = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the angle between the reference plane and the aircraft’s +y body axis 
//!				along a plane perpendicular to the aircraft’s x body axis.
//!
//!	Parameters:	\li d - angle between the reference plane and the aircraft’s +y body axis 
//!						along a plane perpendicular to the aircraft’s x body axis.
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetRollDegrees(double d)			
{
	m_dbRollDegrees = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the the pitch in degrees
//!
//!	Parameters:	\li d -  the pitch in degrees
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetPitchDegrees(double d) 
{
	m_dbPitchDegrees = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name of the air traffic controlled sector (center, terminal or tower) which an aircraft is current flying.
//!
//!	Parameters:	\li p - name of the air traffic controlled sector (center, terminal or tower) which an aircraft is current flying.
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetSector(char* p)					
{
	if(p != NULL)
		lstrcpyn(m_szSector, p, sizeof(m_szSector));
	else
		memset(m_szSector, 0, sizeof(m_szSector));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name of the EnRoute center in which the aircraft is currently flying
//!
//!	Parameters:	\li p - name of the EnRoute center in which the aircraft is currently flying
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetCenter(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szCenter, p, sizeof(m_szCenter));
	else
		memset(m_szCenter, 0, sizeof(m_szCenter));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the difference between the air and ground speed (assuming no wind)
//!
//!	Parameters:	\li d - the difference between the air and ground speed (assuming no wind)
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetVerticalSpeed(double d)
{
	m_dbVerticalSpeedKnots = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set if the A/C is on the ground
//!
//!	Parameters:	\li b - if the A/C is on the ground
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetIsAcOnGround(bool b)
{
	m_bAcOnGround = b;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the frequency in MHz on which A/C is transmitting
//!
//!	Parameters:	\li f - the frequency in MHz on which A/C is transmitting
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetFrequency(int f)
{
	m_iFrequency = f;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the transponder code assigned to the A/C
//!
//!	Parameters:	\li s - the transponder code assigned to the A/C
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetSquawkCode(char* s)
{
	if(s != NULL)
		lstrcpyn(m_szSquawkCode, s, sizeof(m_szSquawkCode));
	else
		memset(m_szSquawkCode, 0, sizeof(m_szSquawkCode));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set bool representing whether or not A/C is identifying itself on radar scope
//!
//!	Parameters:	\li b - bool representing whether or not A/C is identifying itself on radar scope
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPosition::SetIdent(bool b)
{
	m_bIdent = b;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the values of the class members that belong to the
//!				specified data group
//!
//!	Parameters:	\li pPropValues - the element that contains the values
//!
//!	Returns:	true if processed
//------------------------------------------------------------------------------
bool CMLFlightPosition::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild = NULL;
	bool			bProcessed = false;

	// Is this the <positionData> group?
	if(lstrcmpi(ML_XML_DATA_BLOCK_POSITION_DATA, pPropValues->GetName()) == 0)
	{
		// Process all the child members
		pChild = pPropValues->GetFirstChild();
		
		while(pChild != NULL)
		{
			_theReporter.Debug("CMLFlightPosition", "SetMemberValues", "pChild->GetName():%s", pChild->GetName());
			_theReporter.Debug("CMLFlightPosition", "SetMemberValues", "pChild->GetValue():%s", pChild->GetValue());
			if(lstrcmpi(LATITUDE, pChild->GetName()) == 0)
			{
				m_dbLatitudeDegrees = pChild->AsDouble();
			}
			else if(lstrcmpi(LONGITUDE, pChild->GetName()) == 0)
			{
				m_dbLongitudeDegrees = pChild->AsDouble();
			}
			else if(lstrcmpi(ALTITUDE, pChild->GetName()) == 0)
			{
				m_dbAltitudeFt = pChild->AsDouble();
			} 
			else if(lstrcmpi(GROUNDSPEED, pChild->GetName()) == 0)
			{
				m_dbGroundspeedKts = pChild->AsDouble();
			} 
			else if(lstrcmpi(CRUISE_ALT_FP, pChild->GetName()) == 0)
			{
				m_dCruiseAltFeet = pChild->AsDouble();
			} 
			else if(lstrcmpi(YAW, pChild->GetName()) == 0)
			{
			} 
			else if(lstrcmpi(HEADING, pChild->GetName()) == 0)
			{
				m_dbHeadingDegrees = pChild->AsDouble();
			} 
			else if(lstrcmpi(AIRSPEED, pChild->GetName()) == 0)
			{
				m_dbAirspeedKts = pChild->AsDouble();
			} 
			else if(lstrcmpi(PITCH, pChild->GetName()) == 0)
			{
				m_dbPitchDegrees = pChild->AsDouble();
			} 
			else if(lstrcmpi(ROLL, pChild->GetName()) == 0)
			{
				m_dbRollDegrees = pChild->AsDouble();
			}
			else if(lstrcmpi(SECTOR, pChild->GetName()) == 0)
			{
				lstrcpy(m_szSector, pChild->GetValue());
			}
			else if(lstrcmpi(CENTER, pChild->GetName()) == 0)
			{
				lstrcpy(m_szCenter, pChild->GetValue());
			}			
			else if(lstrcmpi(VERTSPEED, pChild->GetName()) == 0)
			{
				m_dbVerticalSpeedKnots = pChild->AsDouble();
			}
			else if(lstrcmpi(ONGROUND, pChild->GetName()) == 0)
			{
				m_bAcOnGround = pChild->AsBool();
			}
			else if(lstrcmpi(TRANSFREQ, pChild->GetName()) == 0)
			{
				m_iFrequency = pChild->AsInteger();
			}
			else if(lstrcmpi(SQUAWKCODE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szSquawkCode, pChild->GetValue());
			}
			else if(lstrcmpi(IDENT_AC, pChild->GetName()) == 0)
			{
				m_bIdent = pChild->AsBool();
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
//!	Summary:	Called to get an XML element descriptor that defines all 
//!				properties in the specified block
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!
//!	Returns:	The element used to construct the XML stream
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLFlightPosition::GetXmlBlock(EMLXMLBlock eXmlBlock)
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

				//	Create a child for the position data and add it to the base class element
				pXmlChild = new CMLPropMember(ML_XML_DATA_BLOCK_POSITION_DATA, "");

				pXmlChild->AddChild(LATITUDE, m_dbLatitudeDegrees);
				pXmlChild->AddChild(LONGITUDE, m_dbLongitudeDegrees);
				pXmlChild->AddChild(ALTITUDE, m_dbAltitudeFt);
				pXmlChild->AddChild(GROUNDSPEED, m_dbGroundspeedKts);
				pXmlChild->AddChild(CRUISE_ALT_FP, m_dCruiseAltFeet);
				pXmlChild->AddChild(HEADING, m_dbHeadingDegrees);
				pXmlChild->AddChild(AIRSPEED, m_dbAirspeedKts);
				pXmlChild->AddChild(PITCH, m_dbPitchDegrees);
				pXmlChild->AddChild(YAW, m_dbHeadingDegrees);  // Yaw is the same as heading
				pXmlChild->AddChild(ROLL, m_dbRollDegrees);
				pXmlChild->AddChild(SECTOR, m_szSector);
				pXmlChild->AddChild(CENTER, m_szCenter);				
				pXmlChild->AddChild(VERTSPEED, m_dbVerticalSpeedKnots);
				pXmlChild->AddChild(ONGROUND, m_bAcOnGround);
				pXmlChild->AddChild(TRANSFREQ, m_iFrequency);
				pXmlChild->AddChild(SQUAWKCODE, m_szSquawkCode);
				pXmlChild->AddChild(IDENT_AC, m_bIdent);
				pXmlBlock->AddChild(pXmlChild); 
				
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
//CMLPtrList& CMLFlightPosition::GetAttributes()
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
//const CMLPtrList& CMLFlightPosition::GetAttributes() const
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
//const char* CMLFlightPosition::GetFirstAttribute()
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
//const char* CMLFlightPosition::GetNextAttribute()
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
//const char* CMLFlightPosition::AddAttribute(const char* pszAttribute)
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