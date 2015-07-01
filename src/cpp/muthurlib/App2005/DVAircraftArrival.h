#if !defined(__DV_AC_ARRIVAL_H__)
#define __DV_AC_ARRIVAL_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "mlListCtrl.h"
#include <MLAircraftArrivalData.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_AC_ARRIVAL_ACID						0							
#define DV_AC_ARRIVAL_TAIL_NUMBER				1
#define DV_AC_ARRIVAL_ARRIVAL_AIRPORT_CODE		2															
#define DV_AC_ARRIVAL_TIME						3							
#define DV_AC_ARRIVAL_RUNWAY					4
#define DV_AC_ARRIVAL_UUID						5
//------------------------------------------------------------------------------
//! Class used to display an AircraftArrivalData object in the data view control 
//------------------------------------------------------------------------------
class CDVAircraftArrival : public CMLListCtrlData
{
private:

	CMLAircraftArrivalData		m_acArrival;
	CObList						m_apElements;

public:

	CDVAircraftArrival();
	virtual					   ~CDVAircraftArrival();

	CMLDataObject*				GetDataObject(){ return &m_acArrival; }
	EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_AC_ARRIVAL; }

	//char*						GetDataObjectUUID(){ return m_acArrival.GetDataObjectUUID(); }
	//void						SetDataObjectUUID(char* pszUUID){ m_acArrival.SetDataObjectUUID(pszUUID); }

	char*						GetACID(){ return m_acArrival.GetACId(); }
	void						SetACID(char* pszACID){ m_acArrival.SetACId(pszACID); }

	CObList*					GetElements(){ return &m_apElements; }
	CString						GetName(int iPropId);
	CString						GetValue(int iPropId);

	void						Randomize();
	void						Update(CMLDataObject* pData);

	void						SetPublishArrivalTime(LPCSTR lpszTime)
	{ 
		if((lpszTime != NULL) && (lstrlen(lpszTime) > 0)) 
			m_acArrival.SetActualArrivalTimeMSecs(atol(lpszTime)); 
	}

	void					SetPublishAssignedRunway(LPCSTR lpszRunway)
	{ 
		if((lpszRunway != NULL) && (lstrlen(lpszRunway)) > 0)
			m_acArrival.SetAssignedRunway((char*)lpszRunway); 
	}	
	void					SetPublishAssignedAirportCode(LPCSTR lpszAirport)
	{ 
		if((lpszAirport != NULL) && (lstrlen(lpszAirport)) > 0)
			m_acArrival.SetArrivalAirportCode((char*)lpszAirport); 
	}
};

#endif