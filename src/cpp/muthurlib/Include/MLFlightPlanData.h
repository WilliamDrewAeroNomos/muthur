//------------------------------------------------------------------------------
/*! \file	MLFlightPlanData.h
//
//  Contains declaration of the CMLFlightPlan class
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

#if !defined(__ML_FLIGHTPLANFILED_H__)
#define __ML_FLIGHTPLANFILED_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define FP_SOURCE			"source"						//!< Attribute name of System or federate that supplied this flight
#define AC_TYPE				"aircraftType"					//!< Attribute name of a/c type designator
#define PLANNED_DEPT_TIME	"plannedDepartureTimeMSecs"		//!< Attribute name of planned departure time
#define PLANNED_DEPT_RW		"plannedDepartureRunway"		//!< Attribute name of departure runway
#define PLANNED_DEPT_GATE	"plannedTaxiOutGate"			//!< Attribute name of parking spot
#define PLANNED_ARR_TIME	"plannedArrivalTimeMSecs"		//!< Attribute name of planned arrival time
#define PLANNED_ARR_RW		"plannedArrivalRunway"			//!< Attribute name of planned arrival runway
#define PLANNED_ARR_GATE	"plannedTaxiInGate"				//!< Attribute name of planned parking spot
#define CRUISE_SPEED		"cruiseSpeedKts"				//!< Attribute name of cruise speed
#define CRUISE_ALT			"cruiseAltitudeFt"				//!< Attribute name of cruise altitude
#define ROUTE_PLAN			"routePlan"						//!< Attribute name of route plan
#define DEPT_CENTER			"departureCenter"				//!< Attribute name of departure center
#define ARR_CENTER			"arrivalCenter"					//!< Attribute name of arrival center
#define DEPT_FIX			"departureFix"					//!< Attribute name of departure fix
#define ARR_FIX				"arrivalFix"					//!< Attribute name of arrival fix
#define PHYS_AC_CLASS		"physicalAircraftClass"			//!< Attribute name of physical a/c class
#define WEIGHT_AC_CLSS		"weightAircraftClass"			//!< Attribute name of a/c weight class
#define USER_AC_CLASS		"userAircraftClass"				//!< Attribute name of user a/c class
#define NUM_AC				"numOfAircraft"					//!< Attribute name of # of a/c
#define AC_EQUIP_QUAL		"airborneEquipmentQualifier"	//!< Attribute name of aircraft's available equipment

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class CMLFlightPlan class objects are used to update the flight 
//! plans or routes of a current aircraft that match the key attributes. 
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLFlightPlan : public CMLDataObject
{
protected:	
	char m_szSource[ML_MAXLEN_SOURCE];								//!< System or federate that supplied this flight plan
	char m_szAircraftType[ML_MAXLEN_AIRCRAFT_TYPE];					//!< Airplane type designator
	double m_dPlannedDepartureTimeMSecs;								//!< UTC Time in milliseconds when the flight plans to depart the airport, which will be defined for now as when the aircraft pushes back from the gate.
	char m_szPlannedDeptRunway[ML_MAXLEN_ASSIGNED_RUNWAY];			//!< Planned departure runway 
	char m_szPlannedDeptGate[ML_MAXLEN_PARKING_SPOT];				//!< Planned Departure gate 
	double m_dPlannedArrivalTimeMSecs;								//!< UTC Time in milliseconds that the aircraft plans to arrive at its destination airport. Arrival time is defined as when the aircraft arrives at the gate.
	char m_szPlannedArrRunway[ML_MAXLEN_ASSIGNED_RUNWAY];			//!< Planned arrival runway 
	char m_szPlannedArrGate[ML_MAXLEN_PARKING_SPOT];				//!< Planned Arrival gate 
	double m_dbCruiseSpeedKts;										//!< Cruise speed of the air craft in knots
	double m_dCruiseAltitudeFeet;									//!< Cruise altitude of the air craft in feet
	char m_szRoute[ML_MAXLEN_ROUTE];								//!< Flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
	char m_szDepartureCenter[ML_MAXLEN_DEPT_CENTER];				//!< Center from which the aircraft departed
	char m_szArrivalCenter[ML_MAXLEN_ARRIVAL_CENTER];				//!< Center where the aircraft is destined to arrive
	char m_szDepartureFix[ML_MAXLEN_DEPT_FIX];						//!< Reserved for future use
	char m_szArrivalFix[ML_MAXLEN_ARRIVAL_FIX];						//!< Reserved for future use
	char m_szPhysicalClass[ML_MAXLEN_PHYSICAL_CLASS];				//!< Physical category of the aircraft
	char m_szWeightClass[ML_MAXLEN_WEIGHT_CLASS];					//!< Weight class of the air craft
	char m_szEquipmentQualifier[ML_MAXLEN_EQUIPMENT_QUALIFIER];		//!< Identifies the aircraft's available equipment
	char m_szUserClass[ML_MAXLEN_USER_CLASS];						//!< Reserved for future use
	int m_iNumAircraft;												//!< Reserved for future use
	
public:
	CMLFlightPlan();
	CMLFlightPlan(const CMLFlightPlan& rSource);
	virtual ~CMLFlightPlan();
	virtual void Copy(const CMLFlightPlan& rSource);
	virtual void Reset();

	char* GetSource();						//!< Get the system or federate that supplied this flight
	char* GetAircraftType();				//!< Get the airplane type designator
	double GetPlannedDepartureTimeMSecs();	//!< Get the time in milliseconds when the flight plans to depart the airport, which will be defined for now as when the aircraft pushes back from the gate.
	double GetPlannedArrivalTimeMSecs();		//!< Get the time in milliseconds that the aircraft plans to arrive at its destination airport. Arrival time is defined as when the aircraft arrives at the gate.
	double GetCruiseSpeedKts();				//!< Get the cruise speed of the air craft in knots
	double GetCruiseAltitudeFeet();			//!< Get the cruise altitude of the air craft in feet
	char* GetRoute();						//!< Get the flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
	char* GetDepartureCenter();				//!< Get the center from which the aircraft departed
	char* GetArrivalCenter();				//!< Center where the aircraft is destined to arrive
	char* GetDepartureFix();				//!< Reserved for future use
	char* GetArrivalFix();					//!< Reserved for future use
	char* GetPhysicalClass();				//!< Get the physical category of the aircraft
	char* GetWeightClass();					//!< Get the weight class of the air craft
	char* GetEquipmentQualifier();			//!< Get the aircraft's available equipment
	char* GetUserClass();					//!< Reserved for future use
	int GetNumAircraft();					//!< Reserved for future use
	char* GetPlannedDeptGate();				//!< Get planned departure gate
	char* GetPlannedArrGate();				//!< Get planned arrival gate
	char* GetPlannedDeptRunway();			//!< Get planned departure runway
	char* GetPlannedArrRunway();			//!< Get planned arrival runway

	void SetSource(char*);						//!< Set the system or federate that supplied this flight
	void SetAircraftType(char*);				//!< Set the airplane type designator
	void SetPlannedDepartureTimeMSecs(double);	//!< Set the time in milliseconds when the flight plans to depart the airport, which will be defined for now as when the aircraft pushes back from the gate.
	void SetPlannedArrivalTimeMSecs(double);	//!< Set the time in milliseconds that the aircraft plans to arrive at its destination airport. Arrival time is defined as when the aircraft arrives at the gate.
	void SetCruiseSpeedKts(double);				//!< Set the cruise speed of the air craft in knots
	void SetCruiseAltitudeFeet(double);			//!< Set the cruise altitude of the air craft in feet
	void SetRoute(char*);						//!< Set the flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
	void SetDepartureCenter(char*);				//!< Set the center from which the aircraft departed
	void SetArrivalCenter(char*);				//!< Center where the aircraft is destined to arrive
	void SetDepartureFix(char*);				//!< Reserved for future use
	void SetArrivalFix(char*);					//!< Reserved for future use
	void SetPhysicalClass(char*);				//!< Set the physical category of the aircraft
	void SetWeightClass(char*);					//!< Set the weight class of the air craft
	void SetEquipmentQualifier(char*);			//!< Set the aircraft's available equipment
	void SetUserClass(char*);					//!< Reserved for future use
	void SetNumAircraft(int);					//!< Reserved for future use
	void SetPlannedDeptGate(char*);				//!< Set the planned dept gate
	void SetPlannedArrGate(char*);				//!< Set the planned arrival gate
	void SetPlannedDeptRunway(char*);			//!< Set the planned dept runway
	void SetPlannedArrRunway(char*);			//!< Set the planned arrival runway

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_FP_FILED; } //!< Returns data object type

	CMLPropMember* GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/

};
#endif