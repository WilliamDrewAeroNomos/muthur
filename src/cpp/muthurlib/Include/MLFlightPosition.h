//------------------------------------------------------------------------------
/*! \file	MLFlightPosition.h
//
//  Contains declaration of the CMLFlightPosition class
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

#if !defined(__ML_FLIGHTPOSITION_H__)
#define __ML_FLIGHTPOSITION_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define LATITUDE		"latitudeDegrees"					//!< Attribute name of latitude 
#define LONGITUDE		"longitudeDegrees"					//!< Attribute name of longitude
#define ALTITUDE		"altitudeFt"						//!< Attribute name of altitude
#define GROUNDSPEED		"groundspeedKts"					//!< Attribute name of ground speed
#define CRUISE_ALT_FP	"cruiseAltitudeFt"					//!< Attribute name of cruise altitude
#define HEADING			"headingDegrees"					//!< Attribute name of heading
#define AIRSPEED		"airspeedKts"						//!< Attribute name of airspeed
#define PITCH			"pitchDegrees"						//!< Attribute name of pitch 
#define YAW				"yawDegrees"						//!< Attribute name of yaw
#define ROLL			"rollDegrees"						//!< Attribute name of roll
#define SECTOR			"sector"							//!< Attribute name of sector
#define CENTER			"center"							//!< Attribute name of center
#define VERTSPEED		"verticalspeedKts"					//!< Attribute name of vertical speed
#define ONGROUND		"aircraftOnGround"					//!< Attribute name of a/c on ground
#define TRANSFREQ		"aircraftTransmissionFrequency"		//!< Attribute name of transmission frequency
#define SQUAWKCODE		"squawkCode"						//!< Attribute name of squawk code
#define IDENT_AC		"ident"								//!< Attribute name of ident
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class CMLFlightPosition class objects provide an update on the flight
//! position of a previously spawned aircraft whether in the air or on the ground. 
//! In order to ensure that the updates can be applied to the current aircraft the appropriate 
//! key attributes such as callSign and tailNumber need to match the corresponding current active 
//! aircraft. This matching of key attributes applies to all updates against current aircraft. 
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLFlightPosition : public CMLDataObject
{
protected:
	double m_dbLatitudeDegrees;										//!< Current latitude of an aircraft’s location in decimal format
	double m_dbLongitudeDegrees;									//!< Current longitude of an aircraft’s location in decimal format
	double m_dbAltitudeFt;											//!< Current altitude (above MSL) of the aircraft in feet
	double m_dbGroundspeedKts;										//!< Ground speed in knots
	double m_dbHeadingDegrees;										//!< Aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
	double m_dCruiseAltFeet;										//!< Cruise altitude of the air craft in feet
	double m_dbAirspeedKts;											//!< Current air speed of the aircraft in knots
	double m_dbPitchDegrees;										//!< Pitch Degrees
	double m_dbRollDegrees;											//!< Angle between the reference plane and the aircraft’s +y body axis along a plane perpendicular to the aircraft’s x body axis.
	char m_szSector[ML_MAXLEN_SECTOR];								//!< Name of the air traffic controlled sector (center, terminal or tower) which an aircraft is current flying.
	char m_szCenter[ML_MAXLEN_CENTER];								//!< Name of the EnRoute center in which the aircraft is currently flying
	double m_dbVerticalSpeedKnots;									//!< Represents the difference between the air and ground speed (assuming no wind)
	bool m_bAcOnGround;												//!< Indicates whether or not the A/C is on the ground (True=is on ground; False=not on ground)
	int m_iFrequency;												//!< Frequency in MHz on which A/C is transmitting 
	char m_szSquawkCode[ML_MAXLEN_SQUAWK];							//!< Transponder code assigned to A/C (represented with 4 chars)
	bool m_bIdent;													//!< Allows an A/C to identify itself to the radar equipment resulting in the aircraft's blip "blossoming" on the radar scope (True=identify self; False=do not identify self)
public:
	CMLFlightPosition();
	CMLFlightPosition(const CMLFlightPosition& rSource);
	virtual ~CMLFlightPosition();
	virtual void Copy(const CMLFlightPosition& rSource);
	virtual void Reset();

	double GetLatitudeDegrees();			//!< Get the current latitude of an aircraft’s location in decimal format
	double GetLongitudeDegrees();			//!< Get the current longitude of an aircraft’s location in decimal format
	double GetAltitudeFt();					//!< Get the current altitude (above MSL) of the aircraft in feet
	double GetGroundspeedKts();				//!< Get the ground speed in knots
	double GetHeadingDegrees();				//!< Get the aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
	double GetAirspeedKts();				//!< Get the current air speed of the aircraft in knots
	double GetRollDegrees();				//!< Get the angle between the reference plane and the aircraft’s +y body axis along a plane perpendicular to the aircraft’s x body axis.
	char* GetSector();						//!< Get the name of the air traffic controlled sector (center, terminal or tower) which an aircraft is current flying.
	char* GetCenter();						//!< Get the name of the EnRoute center in which the aircraft is currently flying
	double GetPitchDegrees();				//!< Get the pitch degrees
	double GetFlightPosCruiseAltitudeFeet();			//!< Get the cruise altitude of the air craft in feet
	double GetVerticalSpeed();				//!< Get the difference between the air and ground speed (assuming no wind)
	bool GetIsAcOnGround();					//!< Get if the A/C is on the ground
	int GetFrequency();						//!< Get the frequency in MHz on which A/C is transmitting
	char* GetSquawkCode();					//!< Get the transponder code assigned to the A/C
	bool GetIdent();						//!< Get bool representing whether or not A/C is identifying itself on radar scope

	void SetPitchDegrees(double);				//!< Set the pitch in degrees
	void SetLatitudeDegrees(double);			//!< Set the current latitude of an aircraft’s location in decimal format
	void SetLongitudeDegrees(double);			//!< Set the current longitude of an aircraft’s location in decimal format
	void SetAltitudeFt(double);					//!< Set the current altitude (above MSL) of the aircraft in feet
	void SetGroundspeedKts(double);				//!< Set the ground speed in knots
	void SetHeadingDegrees(double);				//!< Set the aircraft heading from 0 to 360 degrees with 0/360 being the magnetic north.
	void SetAirspeedKts(double);				//!< Set the current air speed of the aircraft in knots
	void SetRollDegrees(double);				//!< Set the angle between the reference plane and the aircraft’s +y body axis along a plane perpendicular to the aircraft’s x body axis.
	void SetSector(char*);						//!< Set the name of the air traffic controlled sector (center, terminal or tower) which an aircraft is current flying.
	void SetCenter(char*);						//!< Set the name of the EnRoute center in which the aircraft is currently flying
	void SetVerticalSpeed(double);				//!< Set the difference between the air and ground speed (assuming no wind)
	void SetIsAcOnGround(bool);					//!< Set if the A/C is on the ground
	void SetFrequency(int);						//!< Set the frequency in MHz on which A/C is transmitting
	void SetSquawkCode(char*);					//!< Set the transponder code assigned to the A/C
	void SetIdent(bool);						//!< Set bool representing whether or not A/C is identifying itself on radar scope
	void SetFlightPosCruiseAltitudeFeet(double d);//!< Set thr cruise altitude in feet

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_FL_POSITION; } //!< Returns data object type

	CMLPropMember* GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/
};


#endif