#if !defined(__DV_FLIGHTPOSITION_H__)
#define __DV_FLIGHTPOSITION_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "mlListCtrl.h"
#include <MLFlightPosition.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_FLIGHT_POSITION_ACID						0							
#define DV_FLIGHT_POSITION_TAIL_NUMBER				1											
#define DV_FLIGHT_POSITION_LATITUDE_DEGREES			2							
#define DV_FLIGHT_POSITION_LONGITUDE_DEGREES		3
#define DV_FLIGHT_POSITION_ALTITUDE_FEET			4
#define DV_FLIGHT_POSITION_GND_SPEED_KNOTS			5															
#define DV_FLIGHT_POSITION_CRUISE_ALT_FEET			6
#define DV_FLIGHT_POSITION_AIR_SPEED_KNOTS			7																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																											
#define DV_FLIGHT_POSITION_HEADING_DEGREES			8
#define DV_FLIGHT_POSITION_PITCH_DEGREES			9
#define DV_FLIGHT_POSITION_ROLL_DEGREES				10		
#define DV_FLIGHT_POSITION_SECTOR					11
#define DV_FLIGHT_POSITION_CENTER					12		
#define DV_FLIGHT_POSITION_VERT_SPD					13
#define DV_FLIGHT_POSITION_ONGROUND					14
#define DV_FLIGHT_POSITION_FREQ						15
#define DV_FLIGHT_POSITION_SQ_CODE					16
#define DV_FLIGHT_POSITION_IDENT					17
#define DV_FLIGHT_POSITION_UUID						18
//------------------------------------------------------------------------------
//! Class used to display a FlightPositionData object in the data view control 
//------------------------------------------------------------------------------
class CDVFlightPosition : public CMLListCtrlData
{
private:

	double						m_dGroundSpeedCounter;
	CMLFlightPosition			m_flightPosition;
	CObList						m_apElements;

public:

	CDVFlightPosition();
	virtual					   ~CDVFlightPosition();

	CMLDataObject*				GetDataObject(){ return &m_flightPosition; }
	EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_FL_POSITION; }

	char*						GetACID(){ return m_flightPosition.GetACId(); }
	void						SetACID(char* pszACID){ m_flightPosition.SetACId(pszACID); }

	CObList*					GetElements(){ return &m_apElements; }
	CString						GetName(int iPropId);
	CString						GetValue(int iPropId);

	void						Randomize();
	void						Update(CMLDataObject* pData);

	/*void						SetPublishLatitude(LPCSTR lpszLatitude)
	{ 
		if((lpszLatitude != NULL) && (lstrlen(lpszLatitude) > 0)) 
			m_flightPosition.SetLatitudeDegrees(atof(lpszLatitude)); 
	}
	void						SetPublishLongitude(LPCSTR lpszLongitude)
	{ 
		if((lpszLongitude != NULL) && (lstrlen(lpszLongitude) > 0)) 
			m_flightPosition.SetLongitudeDegrees(atof(lpszLongitude)); 
	}
	void						SetPublishAltitude(LPCSTR lpszAltitude)
	{ 
		if((lpszAltitude != NULL) && (lstrlen(lpszAltitude) > 0)) 
			m_flightPosition.SetAltitudeFt(atof(lpszAltitude)); 
	}
	void						SetPublishHeading(LPCSTR lpszHeading)
	{ 
		if((lpszHeading != NULL) && (lstrlen(lpszHeading) > 0)) 
			m_flightPosition.SetHeadingDegrees(atof(lpszHeading)); 
	}
	void						SetPublishPitch(LPCSTR lpszPitch)
	{ 
		if((lpszPitch != NULL) && (lstrlen(lpszPitch) > 0)) 
			m_flightPosition.SetPitchDegrees(atof(lpszPitch)); 
	}
	void						SetPublishRoll(LPCSTR lpszRoll)
	{ 
		if((lpszRoll != NULL) && (lstrlen(lpszRoll) > 0)) 
			m_flightPosition.SetRollDegrees(atof(lpszRoll)); 
	}

	void					SetPublishSector(LPCSTR lpszSector)
	{ 
		if((lpszSector != NULL) && (lstrlen(lpszSector)) > 0)
			m_flightPosition.SetSector((char*)lpszSector); 
	}
	void					SetPublishCenter(LPCSTR lpszCenter)
	{ 
		if((lpszCenter != NULL) && (lstrlen(lpszCenter) > 0)) 
			m_flightPosition.SetCenter((char*)lpszCenter); 
	}
	void					SetPublishOnGround(LPCSTR lpszOnGround)
	{ 
		if((lpszOnGround != NULL) && (lstrlen(lpszOnGround) > 0)) 
			m_flightPosition.SetIsAcOnGround(atoi(lpszOnGround) == 1 ? true : false); 
	}
	void					SetPublishFreq(LPCSTR lpszFreq)
	{ 
		if((lpszFreq != NULL) && (lstrlen(lpszFreq) > 0)) 
			m_flightPosition.SetFrequency(atoi(lpszFreq)); 
	}
	void					SetPublishSquawkCode(LPCSTR lpszSquawkCode)
	{ 
		if((lpszSquawkCode != NULL) && (lstrlen(lpszSquawkCode) > 0)) 
			m_flightPosition.SetSquawkCode((char*)lpszSquawkCode); 
	}
	void					SetPublishIdent(LPCSTR lpszIdent)
	{ 
		if((lpszIdent != NULL) && (lstrlen(lpszIdent) > 0)) 
			m_flightPosition.SetIdent(atoi(lpszIdent) == 1 ? true : false); 
	}*/

	//char*						GetPosDataObjectUUID(){ return m_flightPosition.GetDataObjectUUID(); }
	//void						SetPosDataObjectUUID(char* pszUUID)
	//{ 
	//	if((pszUUID != NULL) && (lstrlen(pszUUID) > 0)) 
	//		m_flightPosition.SetDataObjectUUID(pszUUID); 
	//}
};

#endif