#if !defined(__DV_AC_TAXI_OUT_H__)
#define __DV_AC_TAXI_OUT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "mlListCtrl.h"
#include <MLAircraftTaxiOut.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
#define DV_AC_TAXI_OUT_ACID						0							
#define DV_AC_TAXI_OUT_TAIL_NUMBER				1									
#define DV_AC_TAXI_OUT_TIME						2							
#define DV_AC_TAXI_OUT_GATE						3
#define DV_AC_TAXI_OUT_UUID						4

//------------------------------------------------------------------------------
//! Class used to display an AircraftTaxiOut object in the data view control 
//------------------------------------------------------------------------------
class CDVAircraftTaxiOut : public CMLListCtrlData
{
private:

	CMLAircraftTaxiOut			m_acTaxiOut;
	CObList						m_apElements;

public:

	CDVAircraftTaxiOut();
	virtual					   ~CDVAircraftTaxiOut();

	CMLDataObject*				GetDataObject(){ return &m_acTaxiOut; }
	EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_AC_TAXI_OUT; }

	char*						GetACID(){ return m_acTaxiOut.GetACId(); }
	void						SetACID(char* pszACID){ m_acTaxiOut.SetACId(pszACID); }

	CObList*					GetElements(){ return &m_apElements; }
	CString						GetName(int iPropId);
	CString						GetValue(int iPropId);

	void						Randomize();
	void						Update(CMLDataObject* pData);

	void						SetPublishTaxiOutTime(LPCSTR lpszTime)
	{ 
		if((lpszTime != NULL) && (lstrlen(lpszTime) > 0)) 
			m_acTaxiOut.SetTaxiOutTimeMSecs(atol(lpszTime)); 
	}

	void					SetPublishAssignedGate(LPCSTR lpszGate)
	{ 
		if((lpszGate != NULL) && (lstrlen(lpszGate)) > 0)
			m_acTaxiOut.SetTaxiOutGate((char*)lpszGate); 
	}

	//char*						GetTaxiOutDataObjectUUID(){ return m_acTaxiOut.GetDataObjectUUID(); }
	//void						SetDataObjectUUID(char* pszUUID){ m_acTaxiOut.SetDataObjectUUID(pszUUID); }
};

#endif