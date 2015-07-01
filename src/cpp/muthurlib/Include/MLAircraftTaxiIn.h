//------------------------------------------------------------------------------
/*! \file	MLAircraftTaxiIn.h
//
//  Contains declaration of the CMLAircraftTaxiIn class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>01-19-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

#if !defined(__ML_AIRCRAFTTAXIIN_H__)
#define __ML_AIRCRAFTTAXIIN_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define	TAXI_IN_TIME		"taxiInTimeMSecs"			//!< Attribute name of a/c taxi-in time
#define TAXI_IN_GATE		"taxiInGate"				//!< Attribute Name of arrival runway

 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class CMLAircraftTaxiIn class events are generated when an aircraft 
//! leaves the gate to begin taxiing out for departure.
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAircraftTaxiIn : public CMLDataObject
{
protected:
	double m_dTaxiInTimeMSecs;						//!< Time in milliseconds when the aircraft begins taxiing to the runway
	char m_szTaxiInGate[ML_MAXLEN_PARKING_SPOT];	//!< Assigned gate 
public:
	CMLAircraftTaxiIn();
	CMLAircraftTaxiIn(const CMLAircraftTaxiIn& rSource);
	virtual ~CMLAircraftTaxiIn();
	virtual void Copy(const CMLAircraftTaxiIn& rSource);
	virtual void Reset();

	double  GetTaxiInTimeMSecs();				//!< Get the time in milliseconds when the aircraft begins taxiing to the runway
	char* GetTaxiInGate();					//!< Get the taxi in gate

	void SetTaxiInTimeMSecs(double);			//!< Set the time in milliseconds when the aircraft begins taxiing to the runway
	void SetTaxiInGate(char*);				//!< Set the taxi in gate

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_AC_TAXI_IN; } //!< Returns data object type

	CMLPropMember*	GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/
};


#endif