#if !defined(__DV_AC_KILL_H__)
#define __DV_AC_KILL_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "mlListCtrl.h"
#include <MLKillAircraft.h>

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
#define DV_AC_TAXI_IN_DEPARTURE_AIRPORT_CODE	2
#define DV_AC_TAXI_IN_ARRIVAL_AIRPORT_CODE		3															

//------------------------------------------------------------------------------
//! Class used to display an AircraftTaxiIn object in the data view control 
//------------------------------------------------------------------------------
class CDVKillAircraft : public CMLListCtrlData
{
	private:

		CMLKillAircraft			m_acKillAc;
		CObList					m_apElements;
	
	public:

									CDVKillAircraft();
		virtual					   ~CDVKillAircraft();
		
		CMLDataObject*				GetDataObject(){ return &m_acKillAc; }
		EMLDataObjectClass			GetClass(){ return ML_DATAOBJECT_CLASS_KILL_AC; }
		
		char*						GetACID(){ return m_acKillAc.GetACId(); }
		void						SetACID(char* pszACID){ m_acKillAc.SetACId(pszACID); }
		
		CObList*					GetElements(){ return &m_apElements; }
		CString						GetName(int iPropId);
		CString						GetValue(int iPropId);

		void						Randomize();
		void						Update(CMLDataObject* pData);		
};

#endif