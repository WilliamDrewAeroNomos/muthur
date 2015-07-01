//------------------------------------------------------------------------------
/*! \file	MLApi.h
//
//  Contains definitions and prototypes for the MuthurLib library
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_API_H__)
#define __ML_API_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#ifndef VC_EXTRALEAN
#define VC_EXTRALEAN            // Exclude rarely-used stuff from Windows headers
#endif

#include <windows.h>
#include <stdio.h>
#include <stdlib.h>
#include <cstdlib>
#include <assert.h>
#include <string>

//#include <Reporter.h>


//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

#if defined (MUTHURLIB_EXPORT)
	#define MUTHURLIB_API __declspec(dllexport)
#else
	#define MUTHURLIB_API __declspec(dllimport)
#endif

//	Character buffer extents (includes NULL terminator)
#define ML_MAXLEN_FEDERATE_NAME					64
#define ML_MAXLEN_MUTHUR_HB_QUEUE_NAME			128
#define ML_MAXLEN_MUTHUR_TM_QUEUE_NAME			128
#define ML_MAXLEN_MUTHUR_OWNERSHIP_QUEUE_NAME	128
#define ML_MAXLEN_FED_EXEC_MODEL_NAME			64
#define ML_MAXLEN_FED_EXEC_MODEL_DESCRIPTION	128
#define ML_MAXLEN_UUID							64
#define ML_MAXLEN_AIRCRAFT_TAIL_NUMBER			32
#define ML_MAXLEN_AIRCRAFT_ID					32
#define ML_MAXLEN_AIRPORT_ID					32
#define ML_MAXLEN_DEPT_AIRPORT_CODE				32
#define ML_MAXLEN_ARRIVAL_AIRPORT_CODE			32
#define ML_MAXLEN_SECTOR						32
#define ML_MAXLEN_CENTER						32
#define ML_MAXLEN_SOURCE						64
#define ML_MAXLEN_AIRCRAFT_TYPE					32
#define ML_MAXLEN_ROUTE							128
#define ML_MAXLEN_DEPT_CENTER					32
#define ML_MAXLEN_ARRIVAL_CENTER				32
#define ML_MAXLEN_DEPT_FIX						32
#define ML_MAXLEN_ARRIVAL_FIX					32
#define ML_MAXLEN_PHYSICAL_CLASS				32
#define ML_MAXLEN_WEIGHT_CLASS					32
#define ML_MAXLEN_USER_CLASS					32
#define ML_MAXLEN_EQUIPMENT_QUALIFIER			32
#define ML_MAXLEN_ID							16
#define ML_MAXLEN_EVENT_NAME					64
#define ML_MAXLEN_TYPE							16
#define ML_MAXLEN_DATAOBJECT_TYPE				128
#define ML_MAXLEN_CUSTOM_BUFFERS				100
#define ML_MAXLEN_STANDARD_BUFFERS				100
#define ML_MAXLEN_HANDLE_ID						64
#define ML_MAXLEN_MUTHUR_URL					256
#define ML_MAXLEN_SQUAWK						8
#define ML_MAXLEN_FREQ							8
#define ML_MAXLEN_PARKING_SPOT					8
#define ML_MAXLEN_ASSIGNED_RUNWAY				8
#define ML_MAXLEN_DATA_ACTION					8

#
#define ML_XML_ROOT_ATTR_NAME					"Properties"
#define ML_XML_PROP_ATTR_NAME					"Property"
#define ML_XML_MEMBER_ID_ATTR_NAME				"MemberId"
#define ML_XML_PROPERTY_TYPE					"Type"

#define ML_XML_DEFAULT_DOUBLE_PRECISION			8
#define ML_XML_DEFAULT_SINGLE_PRECISION			4

#define ML_XML_BLOCK_NAME_CONTROL				"controlBlock"
#define ML_XML_BLOCK_NAME_DATA					"dataBlock"
#define ML_XML_BLOCK_NAME_ERROR					"errorBlock"

#define ML_XML_DATA_BLOCK_AIRCRAFT_ID			"aircraftID"
#define ML_XML_DATA_BLOCK_AIRPORT_ID			"airportID"
#define ML_XML_DATA_BLOCK_TAXI_OUT				"taxiOutData"
#define ML_XML_DATA_BLOCK_TAXI_IN				"taxiInData"
#define ML_XML_DATA_BLOCK_AC_ARRIVAL_DATA		"arrivalData"
#define ML_XML_DATA_BLOCK_AC_DEPT_DATA			"departureData"
#define ML_XML_DATA_BLOCK_FLIGHT_PLAN			"flightPlanData"
#define ML_XML_DATA_BLOCK_POSITION_DATA			"positionData"
#define ML_XML_DATA_BLOCK_RUNWAY_DATA			"runway"


#define ML_DEFAULT_MUTHUR_URL					"LocalHost"
#define ML_DEFAULT_MUTHUR_PORT					61616

#define ML_FREQ_OF_MUTHUR_HEARTBEATS			5   

#define ML_XML_SYSEVENT_NAME_TERMINATE_FED		"FederationTerminationEvent"
#define ML_XML_SYSEVENT_NAME_RESET_REQUIRED		"ResetRequiredEvent"

//------------------------------------------------------------------------------
//	CONSTANTS
//------------------------------------------------------------------------------

/*! \enum EMLRequestClass
// These values are used to identify the various classes derived from CMLEvent. 
// Each derived class has its own enumerated identifier
*/
typedef enum
{
	ML_REQUEST_CLASS_BASE = 0,				//!< CMLEvent class identifier
	ML_REQUEST_CLASS_REGISTER,				//!< CMLRegisterRequest class identifier
	ML_REQUEST_CLASS_LIST_FEM,				//!< CMLListFEMRequest class identifier
	ML_REQUEST_CLASS_JOIN_FED,				//!< CMLJoinFedRequest class identifier
	ML_REQUEST_CLASS_ADD_DATA_SUB,			//!< CMLAddDataSubRequest class identifier
	ML_REQUEST_CLASS_READY,					//!< CMLReadyRequest class identifier
	ML_REQUEST_CLASS_CREATE_OBJ,			//!< CMLCreateObjectParams class identifier
	ML_REQUEST_CLASS_DELETE_OBJ,			//!< CMLDeleteObjectParams class identifier
	ML_REQUEST_CLASS_UPDATE_OBJ,			//!< CMLUpdateObjectParams class identifier
	ML_REQUEST_CLASS_TRANSFER_OWNERSHIP,	//!< CMLTransferObjectOwnershipParams class identifier
	ML_REQUEST_CLASS_RELINQUISH_OWNERSHIP,	//!< CMLRelinquishObjectOwnershipParams class identifier
	ML_REQUEST_CLASS_TERMINATE,				//!< CMLTerminateRequest class identifier
	ML_REQUEST_START_FEDERATIONS,			//!< CMLStartFederationParams class identifier
	ML_REQUEST_PAUSE_FEDERATIONS,			//!< CMLStartFederationParams class identifier
	ML_REQUEST_RESUME_FEDERATIONS,			//!< CMLResumeFederationParams class identifier
}EMLRequestClass;

/*! \enum EMLRequestTypeClass
// These values are used to identify the types of request events: request, data or system
*/
typedef enum
{
	ML_REQUEST_STANDARD = 0,				//!< Standard request event
	ML_REQUEST_DATA,						//!< Data Publication event request
	ML_REQUEST_SYSTEM,						//!< System event request
}EMLRequestTypeClass;

/*! \enum EMLResponseClass
// These values are used to identify the various classes derived from CMLEvent. 
// Each derived class has its own enumerated identifier
*/
typedef enum
{
	ML_RESPONSE_CLASS_BASE = 0,							//!< CMLEvent class identifier
	ML_RESPONSE_CLASS_REGISTER,							//!< CMLRegisterResponse class identifier
	ML_RESPONSE_CLASS_LIST_FEM,							//!< CMLListFEMResponse class identifier
	ML_RESPONSE_CLASS_JOIN_FED,							//!< CMLJoinFedResponse class identifier
	ML_RESPONSE_CLASS_ADD_DATA_SUB,						//!< CMLAddDataSubResponse class identifier
	ML_RESPONSE_CLASS_CREATE_OBJECT,					//!< CMLCreateObjectResponse class identifier
	ML_RESPONSE_CLASS_UPDATE_OBJECT,					//!< CMLUpdateObjectResponse class identifier
	ML_RESPONSE_CLASS_DELETE_OBJECT,					//!< CMLDeleteObjectResponse class identifier
	ML_RESPONSE_CLASS_RELINQUISH_OBJECT_OWNERSHIP,		//!< CMLTransferObjectOwnershipResponse class identifier
	ML_RESPONSE_CLASS_TRANSFER_OBJECT_OWNERSHIP,		//!< CMLTransferObjectOwnershipResponse class identifier
	ML_RESPONSE_CLASS_READY,							//!< CMLReadyResponse class identifier
	ML_RESPONSE_CLASS_START_FED,						//!< CMLStartFederationResponse class identifier
	ML_RESPONSE_CLASS_PAUSE_FED,						//!< CMLPauseFederationResponse class identifier
	ML_RESPONSE_CLASS_RESUME_FED,						//!< CMLResumeFederationResponse class identifier
}EMLResponseClass;

/*! \enum EMLSystemEventTypes
// These values are used to identify the types of possible system events
*/
typedef enum
{
	ML_SYSTEM_EVENT_UNKOWN = 0,			//!< Terminate the active federation (either on error or normal shutdown)
	ML_SYSTEM_TERMINATE_FEDERATION,		//!< Terminate the active federation (either on error or normal shutdown)
	ML_SYSTEM_RESET_REQUIRED,			//!< Reset is required, usually the result of loosing comms with Muthur
}EMLSystemEventTypes;

/*! \enum EMLDataObjectClass
// These values are used to identify the various classes derived from CMLDataObject. 
// Each derived class has its own enumerated identifier
*/
typedef enum
{
	ML_DATAOBJECT_CLASS_BASE = 0,				//!< CMLDataObject class identifier
	ML_DATAOBJECT_CLASS_AC_ARRIVAL,				//!< CMLAircraftArrivalData class identifier
	ML_DATAOBJECT_CLASS_AC_DEPT,				//!< CMLAircraftDepartureData class identifier
	ML_DATAOBJECT_CLASS_AC_TAXI_OUT,			//!< CMLAircraftTaxiOut class identifier
	ML_DATAOBJECT_CLASS_AC_TAXI_IN,				//!< CMLAircraftTaxiIn class identifier
	ML_DATAOBJECT_CLASS_FP_FILED,				//!< CMLFlightPlan class identifier
	ML_DATAOBJECT_CLASS_FL_POSITION,			//!< CMLFlightPosition class identifier
	ML_DATAOBJECT_CLASS_AIRCRAFT,				//!< CMLAircraft class identifier
	ML_DATAOBJECT_CLASS_AIRPORT_CONFIG,			//!< CMLAirportConfiguration class identifier
}EMLDataObjectClass;

typedef enum
{
	ML_EVENT_STATUS_UNKNOWN = 0,	//!< Uninitialized
	ML_EVENT_STATUS_PROCESSED,		//!< Request was processed
	ML_EVENT_STATUS_IGNORED,		//!< Request was ignored

}EMLEventStatus;

/*! \enum EMLXMLBlock
// These values are used to identify XML property blocks
*/
typedef enum
{
	ML_XML_BLOCK_UNKNOWN = 0,
	ML_XML_BLOCK_CONTROL,	
	ML_XML_BLOCK_DATA,
	ML_XML_BLOCK_ERROR,
	ML_XML_BLOCK_AIRCRAFT_ID,
	ML_XML_BLOCK_AC_ARRIVAL,
}EMLXMLBlock;

#define ML_ADD_DATAOBJECT_STRING			"Add"
#define ML_UPDATE_DATAOBJECT_STRING			"Update"
#define ML_DELETE_DATAOBJECT_STRING			"Delete"

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------


#endif // !defined(__ML_API_H__)
