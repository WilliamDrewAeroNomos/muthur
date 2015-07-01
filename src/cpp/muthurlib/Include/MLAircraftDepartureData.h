//------------------------------------------------------------------------------
/*! \file	MLAircraftDepartureData.h
//
//  Contains declaration of the CMLAircraftDepartureData class
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

#if !defined(__ML_AIRCRAFTDEPTDATA_H__)
#define __ML_AIRCRAFTDEPTDATA_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define	ACTUAL_DEPT_TIME	"actualDepartureTimeMSecs"		//!< Attribute name of actual departure time
#define DEPT_RUNWAY			"departureRunway"				//!< Attribute Name of departure runway
#define	DEPT_AIRPORT		"departureAirportCode"			//!< Attribute Name of departure airport code
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class CMLAircraftDepartureData events should be generated when the
//! aircraft departs from the airport. The actual definition for departure
//! is left up to the initiator of the event but is generally held to be 
//! when the wheels leave the runway and the plane moves into local control. 
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAircraftDepartureData : public CMLDataObject
{
protected:	
	double m_dActualDepartureTimeMSecs;						//!< Time in milliseconds when the aircraft actually departed the airport
	char m_szAssignedRunway[ML_MAXLEN_ASSIGNED_RUNWAY];		//!< Assigned runway 
	char m_szDeptAirportCode[ML_MAXLEN_DEPT_AIRPORT_CODE];	//!< Airport from which the aircraft is departing
public:
	CMLAircraftDepartureData();
	CMLAircraftDepartureData(const CMLAircraftDepartureData& rSource);
	virtual ~CMLAircraftDepartureData();
	virtual void Copy(const CMLAircraftDepartureData& rSource);
	virtual void Reset();

	double GetActualDepartureTimeMSecs();		//!< Get the time in milliseconds when the aircraft actually departed the airport
	char* GetAssignedRunway();				//!< Get the assigned runway
	char* GetDeptAirportCode();				//!< Get the airport from which the aircraft is departing

	void SetActualDepartureTimeMSecs(double);		//!< Set the time in milliseconds when the aircraft actually departed the airport
	void SetAssignedRunway(char*);				//!< Set the assigned runway
	void SetDeptAirportCode(char*);				//!< Set the airport from which the aircraft is departing

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_AC_DEPT; } //!< Returns data object type

	CMLPropMember*	GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/
};


#endif