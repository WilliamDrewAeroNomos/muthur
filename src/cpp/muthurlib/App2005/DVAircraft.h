#if !defined(__DV_AIRCRAFT_H__)
#define __DV_AIRCRAFT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "MLListCtrl.h"
#include <MLAircraftData.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_AIRCRAFT_ACID					0							
#define DV_AIRCRAFT_TAIL_NUMBER				1
#define DV_AIRCRAFT_UUID					2
/*#define DV_SPAWN_AIRCRAFT_DEPARTURE_AIRPORT_CODE	2
#define DV_SPAWN_AIRCRAFT_ARRIVAL_AIRPORT_CODE		3															
#define DV_SPAWN_AIRCRAFT_LATITUDE_DEGREES			4							
#define DV_SPAWN_AIRCRAFT_LONGITUDE_DEGREES			5
#define DV_SPAWN_AIRCRAFT_GND_SPEED_KNOTS			6															
#define DV_SPAWN_AIRCRAFT_ALTITUDE_FEET				7
#define DV_SPAWN_AIRCRAFT_CRUISE_SPEED_KNOTS		8
#define DV_SPAWN_AIRCRAFT_CRUISE_ALT_FEET			9
#define DV_SPAWN_AIRCRAFT_AIR_SPEED_KNOTS			10																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																											
#define DV_SPAWN_AIRCRAFT_HEADING_DEGREES			11
#define DV_SPAWN_AIRCRAFT_PITCH_DEGREES				12
#define DV_SPAWN_AIRCRAFT_ROLL_DEGREES				13		
#define DV_SPAWN_AIRCRAFT_SECTOR					14
#define DV_SPAWN_AIRCRAFT_CENTER					15		
#define DV_SPAWN_AIRCRAFT_SOURCE					16
#define DV_SPAWN_AIRCRAFT_AIRCRAFT_TYPE				17															
#define DV_SPAWN_AIRCRAFT_PLANNED_DEPARTURE_TIME	18
#define DV_SPAWN_AIRCRAFT_PLANNED_ARRIVAL_TIME		19																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																											
#define DV_SPAWN_AIRCRAFT_ROUTE						20
#define DV_SPAWN_AIRCRAFT_DEPARTURE_CENTER			21		
#define DV_SPAWN_AIRCRAFT_ARRIVAL_CENTER			22
#define DV_SPAWN_AIRCRAFT_DEPARTURE_FIX				23		
#define DV_SPAWN_AIRCRAFT_ARRIVAL_FIX				24		
#define DV_SPAWN_AIRCRAFT_PHYSICAL_CLASS			25		
#define DV_SPAWN_AIRCRAFT_WEIGHT_CLASS				26		
#define DV_SPAWN_AIRCRAFT_EQUIPMENT					27		
#define DV_SPAWN_AIRCRAFT_USER_CLASS				28		
#define DV_SPAWN_AIRCRAFT_TAXI_OUT_TIME_MS			29		
#define DV_SPAWN_AIRCRAFT_ACTUAL_DEPARTURE_TIME_MS	30	*/	

//------------------------------------------------------------------------------
//! Class used to display an aircraft object in the data view control 
//------------------------------------------------------------------------------
class CDVAircraft : public CMLListCtrlData
{
	private:

		CMLAircraft					m_Aircraft;
		CObList						m_apElements;
	
	public:

									CDVAircraft();
		virtual					   ~CDVAircraft();

		CMLDataObject*				GetDataObject(){ return &m_Aircraft; }
		EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_AIRCRAFT; }
		
		char*						GetACID(){ return m_Aircraft.GetACId(); }
		void						SetACID(char* pszACID){ m_Aircraft.SetACId(pszACID); }

		char*						GetTailNumber(){ return m_Aircraft.GetACTailNumber(); }
		void						SetTailNumber(char* pszTailNumber){ m_Aircraft.SetACTailNumber(pszTailNumber); }

		char*						GetDataObjectUUID(){ return m_Aircraft.GetDataObjectUUID(); }
		void						SetDataObjectUUID(char* pszUUID){ m_Aircraft.SetDataObjectUUID(pszUUID); }
		
		CObList*					GetElements(){ return &m_apElements; }
		CString						GetName(int iPropId);
		CString						GetValue(int iPropId);
		
		void						Randomize();
		void						Update(CMLDataObject* pData);

	/*	void						SetPublishLatitude(LPCSTR lpszLatitude)
									{ 
										if((lpszLatitude != NULL) && (lstrlen(lpszLatitude) > 0)) 
											m_Aircraft.SetLatitudeDegrees(atof(lpszLatitude)); 
									}

		void					SetAircraftType(LPCSTR lpszAcType)
								{
									if((lpszAcType != NULL) && (lstrlen(lpszAcType) > 0)) 
											m_Aircraft.SetAircraftType((char*)lpszAcType);
									if((lpszAcType != NULL) && (lstrlen(lpszAcType) > 0)) 
											m_Aircraft.SetEquipmentQualifier((char*)lpszAcType); 
								}

		void						SetPublishLongitude(LPCSTR lpszLongitude)
									{ 
										if((lpszLongitude != NULL) && (lstrlen(lpszLongitude) > 0)) 
											m_Aircraft.SetLongitudeDegrees(atof(lpszLongitude)); 
									}
		void						SetPublishAltitude(LPCSTR lpszAltitude)
									{ 
										if((lpszAltitude != NULL) && (lstrlen(lpszAltitude) > 0)) 
											m_Aircraft.SetAltitudeFt(atof(lpszAltitude)); 
									}
		void						SetPublishHeading(LPCSTR lpszHeading)
									{ 
										if((lpszHeading != NULL) && (lstrlen(lpszHeading) > 0)) 
											m_Aircraft.SetHeadingDegrees(atof(lpszHeading)); 
									}
		void						SetPublishPitch(LPCSTR lpszPitch)
									{ 
										if((lpszPitch != NULL) && (lstrlen(lpszPitch) > 0)) 
											m_Aircraft.SetPitchDegrees(atof(lpszPitch)); 
									}
		void						SetPublishRoll(LPCSTR lpszRoll)
									{ 
										if((lpszRoll != NULL) && (lstrlen(lpszRoll) > 0)) 
											m_Aircraft.SetRollDegrees(atof(lpszRoll)); 
									}*/
};

#endif