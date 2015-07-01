#if !defined(__DV_FLIGHT_PLAN_H__)
#define __DV_FLIGHT_PLAN_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "MLListCtrl.h"
#include <MLFlightPlanData.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_FLIGHT_PLAN_ACID						0							
#define DV_FLIGHT_PLAN_TAIL_NUMBER				1
#define DV_FLIGHT_PLAN_UUID						2
#define DV_FLIGHT_PLAN_CRUISE_SPEED_KNOTS		3
#define DV_FLIGHT_PLAN_CRUISE_ALT_FEET			4
#define DV_FLIGHT_PLAN_PLANNED_DEPARTURE_TIME	5
#define DV_FLIGHT_PLAN_PLANNED_ARRIVAL_TIME		6																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																											
#define DV_FLIGHT_PLAN_ROUTE					7
#define DV_FLIGHT_PLAN_DEPARTURE_CENTER			8		
#define DV_FLIGHT_PLAN_ARRIVAL_CENTER			9
#define DV_FLIGHT_PLAN_DEPARTURE_FIX			10		
#define DV_FLIGHT_PLAN_ARRIVAL_FIX				11		
#define DV_FLIGHT_PLAN_PHYSICAL_CLASS			12		
#define DV_FLIGHT_PLAN_WEIGHT_CLASS				13	
#define DV_FLIGHT_PLAN_AIRCRAFT_TYPE			14
#define DV_FLIGHT_PLAN_USER_CLASS				15
#define DV_FLIGHT_PLAN_EQUIP_QUAL				16
#define DV_FLIGHT_PLAN_PLANNED_DEPARTURE_GATE	17
#define DV_FLIGHT_PLAN_PLANNED_ARRIVAL_GATE		18
#define DV_FLIGHT_PLAN_PLANNED_DEPARTURE_RWY	19
#define DV_FLIGHT_PLAN_PLANNED_ARRIVAL_RWY		20
//------------------------------------------------------------------------------
//! Class used to display an aircraft object in the data view control 
//------------------------------------------------------------------------------
class CDVFlightPlan : public CMLListCtrlData
{
	private:

		CMLFlightPlan				m_FlightPlan;
		CObList						m_apElements;
	
	public:

									CDVFlightPlan();
		virtual					   ~CDVFlightPlan();

		CMLDataObject*				GetDataObject(){ return &m_FlightPlan; }
		EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_FP_FILED; }
		
		char*						GetACID(){ return m_FlightPlan.GetACId(); }
		void						SetACID(char* pszACID){ m_FlightPlan.SetACId(pszACID); }

		char*						GetTailNumber(){ return m_FlightPlan.GetACTailNumber(); }
		void						SetTailNumber(char* pszTailNumber){ m_FlightPlan.SetACTailNumber(pszTailNumber); }

		//char*						GetFPDataObjectUUID(){ return m_FlightPlan.GetDataObjectUUID(); }
		//void						SetFPDataObjectUUID(char* pszUUID){ m_FlightPlan.SetDataObjectUUID(pszUUID); }
		
		CObList*					GetElements(){ return &m_apElements; }
		CString						GetName(int iPropId);
		CString						GetValue(int iPropId);
		
		void						Randomize();
		void						Update(CMLDataObject* pData);

	/*	void						SetPublishLatitude(LPCSTR lpszLatitude)
									{ 
										if((lpszLatitude != NULL) && (lstrlen(lpszLatitude) > 0)) 
											m_FlightPlan.SetLatitudeDegrees(atof(lpszLatitude)); 
									}

		void					SetAircraftType(LPCSTR lpszAcType)
								{
									if((lpszAcType != NULL) && (lstrlen(lpszAcType) > 0)) 
											m_FlightPlan.SetAircraftType((char*)lpszAcType);
									if((lpszAcType != NULL) && (lstrlen(lpszAcType) > 0)) 
											m_FlightPlan.SetEquipmentQualifier((char*)lpszAcType); 
								}

		void						SetPublishLongitude(LPCSTR lpszLongitude)
									{ 
										if((lpszLongitude != NULL) && (lstrlen(lpszLongitude) > 0)) 
											m_FlightPlan.SetLongitudeDegrees(atof(lpszLongitude)); 
									}
		void						SetPublishAltitude(LPCSTR lpszAltitude)
									{ 
										if((lpszAltitude != NULL) && (lstrlen(lpszAltitude) > 0)) 
											m_FlightPlan.SetAltitudeFt(atof(lpszAltitude)); 
									}
		void						SetPublishHeading(LPCSTR lpszHeading)
									{ 
										if((lpszHeading != NULL) && (lstrlen(lpszHeading) > 0)) 
											m_FlightPlan.SetHeadingDegrees(atof(lpszHeading)); 
									}
		void						SetPublishPitch(LPCSTR lpszPitch)
									{ 
										if((lpszPitch != NULL) && (lstrlen(lpszPitch) > 0)) 
											m_FlightPlan.SetPitchDegrees(atof(lpszPitch)); 
									}
		void						SetPublishRoll(LPCSTR lpszRoll)
									{ 
										if((lpszRoll != NULL) && (lstrlen(lpszRoll) > 0)) 
											m_FlightPlan.SetRollDegrees(atof(lpszRoll)); 
									}*/
};

#endif