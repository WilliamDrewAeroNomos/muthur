#if !defined(__DV_AC_DEPARTURE_H__)
#define __DV_AC_DEPARTURE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "mlListCtrl.h"
#include <MLAircraftDepartureData.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_AC_DEPARTURE_ACID					0							
#define DV_AC_DEPARTURE_TAIL_NUMBER				1
#define DV_AC_DEPARTURE_DEPARTURE_AIRPORT_CODE	2					
#define DV_AC_DEPARTURE_TIME					3							
#define DV_AC_DEPARTURE_RUNWAY					4
#define DV_AC_DEPARTURE_UUID					5
//------------------------------------------------------------------------------
//! Class used to display an AircraftDepartureData object in the data view control 
//------------------------------------------------------------------------------
class CDVAircraftDeparture : public CMLListCtrlData
{
private:

	CMLAircraftDepartureData	m_acDepart;
	CObList						m_apElements;

public:

	CDVAircraftDeparture();
	virtual					   ~CDVAircraftDeparture();

	CMLDataObject*				GetDataObject(){ return &m_acDepart; }
	EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_AC_DEPT; }

	char*						GetACID(){ return m_acDepart.GetACId(); }
	void						SetACID(char* pszACID){ m_acDepart.SetACId(pszACID); }

	CObList*					GetElements(){ return &m_apElements; }
	CString						GetName(int iPropId);
	CString						GetValue(int iPropId);

	void						Randomize();
	void						Update(CMLDataObject* pData);

	void						SetPublishDepartureTime(LPCSTR lpszTime)
	{ 
		if((lpszTime != NULL) && (lstrlen(lpszTime) > 0)) 
			m_acDepart.SetActualDepartureTimeMSecs(atol(lpszTime)); 
	}

	void					SetPublishAssignedRunway(LPCSTR lpszRunway)
	{ 
		if((lpszRunway != NULL) && (lstrlen(lpszRunway)) > 0)
			m_acDepart.SetAssignedRunway((char*)lpszRunway); 
	}	

	//char*						GetDeptDataObjectUUID(){ return m_acDepart.GetDataObjectUUID(); }
	//void						SetDeptDataObjectUUID(char* pszUUID){ m_acDepart.SetDataObjectUUID(pszUUID); }
};

#endif