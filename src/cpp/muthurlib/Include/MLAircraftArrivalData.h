//------------------------------------------------------------------------------
/*! \file	MLAircraftArrivalData.h
//
//  Contains declaration of the CMLAircraftArrivalData class
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

#if !defined(__ML_AIRCRAFTARRIVALDATA_H__)
#define __ML_AIRCRAFTARRIVALDATA_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define	ACTUAL_ARRIVAL_TIME "actualArrivalTimeMSecs"	//!< Attribute name of actual arrival time
#define ARRIVAL_RUNWAY		"arrivalRunway"				//!< Attribute Name of arrival runway
#define	ARRIVAL_AIRPORT		"arrivalAirportCode"		//!< Attribute Name of arrival airport code

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class CMLAircraftArrivalData events should be generated when an 
//! aircraft arrives at its destination. The actual definition of arrival
//! is left up to the initiator of the event but is generally held to be 
//! when the aircraft has touched its wheels on the runway. 
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAircraftArrivalData : public CMLDataObject
{
protected:
	double			m_dActualArrivalTimeMSecs;								//!< Time in milliseconds when the aircraft actually arrived at the airport
	char			m_szAssignedRunway[ML_MAXLEN_ASSIGNED_RUNWAY];			//!< Assigned runway 
	char			m_szArrivalAirportCode[ML_MAXLEN_ARRIVAL_AIRPORT_CODE];	//!< Destination airport for the aircraft when it completes its flight plan
	
	//CMLPtrList		m_apAttributes;											//!< The list of attributes associated with this object type
	//PTRNODE			m_posAttributes;										//!< Used for iteration of attributes list

public:
	CMLAircraftArrivalData();
	CMLAircraftArrivalData(const CMLAircraftArrivalData& rSource);
	virtual ~CMLAircraftArrivalData();
	virtual void Copy(const CMLAircraftArrivalData& rSource);
	virtual void Reset();


	double GetActualArrivalTimeMSecs();		//!< Get the time in milliseconds when the aircraft actually arrived at the airport
	char* GetAssignedRunway();				//!< Get the assigned runway
	char* GetArrivalAirportCode();			//!< Get the destination airport for the aircraft when it completes its flight plan

	void SetActualArrivalTimeMSecs(double);	//!< Set the time in milliseconds when the aircraft actually arrived at the airport
	void SetAssignedRunway(char*);			//!< Set the assigned runway
	void SetArrivalAirportCode(char*);		//!< Set the destination airport for the aircraft when it completes its flight plan

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_AC_ARRIVAL; } //!< Returns data object type

	CMLPropMember*	GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	//virtual const CMLPtrList&			GetAttributes() const;
	//virtual CMLPtrList&					GetAttributes();
	//virtual const char*					GetFirstAttribute();
	//virtual const char*					GetNextAttribute();
	//virtual const char*					AddAttribute(const char*);
};


#endif