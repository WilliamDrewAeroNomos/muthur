#if !defined(__DV_AC_TAXI_IN_H__)
#define __DV_AC_TAXI_IN_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "mlListCtrl.h"
#include <MLAircraftTaxiIn.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_AC_TAXI_IN_ACID						0							
#define DV_AC_TAXI_IN_TAIL_NUMBER				1												
#define DV_AC_TAXI_IN_TIME						2							
#define DV_AC_TAXI_IN_GATE						3
#define DV_AC_TAXI_IN_UUID						4
//------------------------------------------------------------------------------
//! Class used to display an AircraftTaxiIn object in the data view control 
//------------------------------------------------------------------------------
class CDVAircraftTaxiIn : public CMLListCtrlData
{
private:

	CMLAircraftTaxiIn			m_acTaxiIn;
	CObList						m_apElements;

public:

	CDVAircraftTaxiIn();
	virtual					   ~CDVAircraftTaxiIn();

	CMLDataObject*				GetDataObject(){ return &m_acTaxiIn; }
	EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_AC_TAXI_IN; }

	char*						GetACID(){ return m_acTaxiIn.GetACId(); }
	void						SetACID(char* pszACID){ m_acTaxiIn.SetACId(pszACID); }

	CObList*					GetElements(){ return &m_apElements; }
	CString						GetName(int iPropId);
	CString						GetValue(int iPropId);

	void						Randomize();
	void						Update(CMLDataObject* pData);

	void						SetPublishTaxiInTime(LPCSTR lpszTime)
	{ 
		if((lpszTime != NULL) && (lstrlen(lpszTime) > 0)) 
			m_acTaxiIn.SetTaxiInTimeMSecs(atol(lpszTime)); 
	}

	void					SetPublishAssignedGate(LPCSTR lpszGate)
	{ 
		if((lpszGate != NULL) && (lstrlen(lpszGate)) > 0)
			m_acTaxiIn.SetTaxiInGate((char*)lpszGate); 
	}	

	//char*						GetTaxiInDataObjectUUID(){ return m_acTaxiIn.GetDataObjectUUID(); }
	//void						SetTaxiInDataObjectUUID(char* pszUUID){ m_acTaxiIn.SetDataObjectUUID(pszUUID); }
};

#endif