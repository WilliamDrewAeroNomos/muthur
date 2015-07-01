//------------------------------------------------------------------------------
/*! \file	MLAircraftTaxiOut.h
//
//  Contains declaration of the CMLAircraftTaxiOut class
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

#if !defined(__ML_AIRCRAFTTAXIOUT_H__)
#define __ML_AIRCRAFTTAXIOUT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define	TAXI_OUT_TIME		"taxiOutTimeMSecs"			//!< Attribute name of a/c taxi-out time
#define TAXI_OUT_GATE		"taxiOutGate"				//!< Attribute Name of assigned gate
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class CMLAircraftTaxiOut class events are generated when an aircraft 
//! leaves the gate to begin taxiing out for departure.
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAircraftTaxiOut : public CMLDataObject
{
protected:
	double m_dTaxiOutTimeMSecs;						//!< Time in milliseconds when the aircraft begins taxiing to the runway
	char m_szTaxiOutGate[ML_MAXLEN_PARKING_SPOT];	//!< Assigned gate 
public:
	CMLAircraftTaxiOut();
	CMLAircraftTaxiOut(const CMLAircraftTaxiOut& rSource);
	virtual ~CMLAircraftTaxiOut();
	virtual void Copy(const CMLAircraftTaxiOut& rSource);
	virtual void Reset();

	double GetTaxiOutTimeMSecs();				//!< Get the time in milliseconds when the aircraft begins taxiing to the runway
	char* GetTaxiOutGate();					//!< Get the taxi out gate

	void SetTaxiOutTimeMSecs(double);			//!< Set the time in milliseconds when the aircraft begins taxiing to the runway
	void SetTaxiOutGate(char*);				//!< Set the taxi out gate

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_AC_TAXI_OUT; } //!< Returns data object type

	CMLPropMember*	GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/
};


#endif