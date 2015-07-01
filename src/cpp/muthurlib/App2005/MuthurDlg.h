#pragma once

#include "afxwin.h"
#include "atlconv.h"
#include "MLListCtrl.h"
#include "DVFlightPosition.h"
#include "DVAircraft.h"
//#include "DvKillAircraft.h"
#include "DVAircraftTaxiIn.h"
#include "DVAircraftTaxiOut.h"
#include "DVAircraftArrival.h"
#include "DVAircraftDeparture.h"
#include "DVFlightPlan.h"

#include <MLAmbassador.h>
#include <MLException.h>
#include <MLAmbassadorCallback.h>
#include <MLSystemCallback.h>
#include <MLObjectEventCallback.h>
#include <MLTimeManagementEventCallback.h>
#include <MLObjectEvent.h>
#include <MLTimeManagementEvent.h>
#include <MLObjectOwnerRelinquishEvent.h>
#include <MLEvent.h>
#include <MLRegisterParams.h>
#include <MLListFEMParams.h>
#include <MLAddDataSubParams.h>
//#include <MLAddDataPubParams.h>
#include <MLResumeFederationRequest.h>
#include <MLPauseFederationRequest.h>
#include <MLStartFederationRequest.h>

#include <MLRegisterResponse.h>
#include <MLListFEMResponse.h>
#include <MLJoinFedResponse.h>
#include <MLFedExecModel.h>
#include <MLAddDataSubResponse.h>
//#include <MLAddDataPubResponse.h>
#include <MLResumeFederationResponse.h>
#include <MLPauseFederationResponse.h>
#include <MLStartFederationResponse.h>


#include <MLAircraftTaxiOut.h>
#include <MLAircraftTaxiIn.h>
#include <MLFlightPlanData.h>
#include <MLSerialize.h>
#include <MLHelper.h>

#include <stdio.h>
#include <iostream>

using namespace std;

#define WM_UPDATE_CTRLS	(WM_USER + 1)
#define WM_PUBLISH_DATA	(WM_USER + 2)
#define WM_SUBSCRIBE_DATA	(WM_USER + 3)


// Identifies the execution state of the federation (Undefined, running or paused)
typedef enum
{
	NEXFED_BRIDGE_EXEC_STATE_UNDEFINED = 1,			// Current execution state of federation is not defined
	NEXFED_BRIDGE_RUNNING = 2,					// Federation is currently running
	NEXFED_BRIDGE_PAUSED = 3,					// Federation is currently paused
}ENexFedExecutionStates;

// Identifies an action for the federation to take
typedef enum
{
	NEXFED_BRIDGE_DIRECTIVE_STATE_UNDEFINED = 1,			// Undefined action federate should take
	NEXFED_BRIDGE_DIRECTIVE_STATE_START = 2,
	NEXFED_BRIDGE_DIRECTIVE_STATE_PAUSE = 3,
	NEXFED_BRIDGE_DIRECTIVE_STATE_RESUME = 4,
	NEXFED_BRIDGE_DIRECTIVE_STATE_RUN = 5,
}ENexFedDirectiveStates;

//	Identifies the current Muthur state
typedef enum
{
	MS_NOT_INITIALIZED = 0,	// Startup - not yet connected to ActiveMQ
	MS_NOT_REGISTERED,		// Not yet registered with Muthur
	MS_REGISTERED,			// Registered - need to list available FEMs
	MS_LISTED_FEMS,			// Requested listing of FEMs
	MS_JOINED,				// Picked FEM and requested join
	MS_ADDED_DATA_SUBS,		// Added data subscriptions
	//MS_CREATED_OBJECT,		// Object Created
	//MS_UPDATED_OBJECT,		// Object Updated
	//MS_DELETED_OBJECT,		// Object Deleted
	MS_RUNNING,				// Federation is running
}EMLStates;



class CMuthurDlg : public CDialog, public IMLAmbassadorCallback, public IMLSystemCallback,
	public IMLObjectEventCallback, public IMLTimeManagementEventCallback, public IMLObjectOwnershipRelinquishCallback
{
private:

	CMLPtrList			m_apFEModels; //!< The list of available models
	CMLHandle			m_hFRH;	//!< Federation Registration Handle 
	CMLHandle			m_hFEM;	//!< Federation Execution Model Handle 
	CMLHandle			m_hFEH;	//!< Federation Execution Handle
	EMLStates			m_eMLState;
	HICON				m_hIcon;
	CMLFedExecModel*	m_pFedExecModel;
	DWORD				m_dwPublishTimerId;
	DWORD				m_dwRefreshViewsTimerId;
	bool				m_bSubscribeEnabled;
	bool				m_bPublishEnabled;
	double				m_dGroundSpeedCounter;

	double				m_dCurrentSimTime;	//!< Represents the current UTC time (msecs) from muthur
	double			    m_dStartSimTime;    //!< Represents the UTC sim start time (msecs) from muthur
	bool				m_bIsStartTimeSet;	//!< indicates if start time has been set
	CMLListCtrlData*	m_pdvSubscribeData; //!< The data subscription being monitored
	CMLListCtrlData*	m_pdvPublishData; //!< The data publication being processed
	CMLListCtrlData*	m_pdvMetronSpawn; //!< The initial spawn data packet to be sent by metron
	CMLListCtrlData*	m_pdvNexsimSpawn; //!< The initial spawn data packet to be sent by NexSim


	CDVFlightPosition	m_dvFlightPosition;
	CDVFlightPosition	m_subFlightPosition;
	CDVAircraft	m_dvSpawnAircraft;
	CDVAircraft	m_subAircraft;
	/*CDVKillAircraft     m_dvKillAircraft;
	CDVKillAircraft     m_subKillAircraft;*/
	CDVAircraftTaxiIn	m_dvAcTaxiIn;
	CDVAircraftTaxiIn   m_subAcTaxiIn;
	CDVAircraftTaxiOut	m_dvAcTaxiOut;
	CDVAircraftTaxiOut  m_subAcTaxiOut;
	CDVAircraftArrival  m_dvAcArrival;
	CDVAircraftArrival  m_subAcArrival;
	CDVAircraftDeparture m_dvAcDeparture;
	CDVAircraftDeparture m_subAcDeparture;
	CDVFlightPlan		 m_dvAcFlightPlan;
	CDVFlightPlan		 m_subFlightPlan;

	//CStringArray		m_aPubClasses; //!< The list of available data classes for publication
	CStringArray		m_aSubClasses; //!< The list of available data classes for subscription
	CStringArray		m_aReqSubscriptions;//!< The list of requested subscription classes
	CStringArray		m_aDataActions;     //!< The list of allowed data actions (add, update & delete)
	CStringArray		m_aObjCallSigns;	//!< List of all callsigns of all the objects that are available.
	//CStringArray		m_aReqPublications;//!< The list of requested publication classes

	CStringArray		m_aDirective;	//!< Directive to apply (start, pause, resume)

	CSimpleMap<CComBSTR, CComBSTR> m_Objects;	//!< List of objects 
	CSimpleMap<CComBSTR, CComBSTR> m_ObjectsTypes; //!< List of objects and types
public:

	CMuthurDlg(CWnd* pParent = NULL);
	~CMuthurDlg();

	LONG				OnWMUpdateCtrls(WPARAM wParam, LPARAM lParam);
	LONG				OnWMPublishData(WPARAM wParam, LPARAM lParam);


protected:

	void				DoDataExchange(CDataExchange* pDX);	// DDX/DDV support
	void				ShowModel(CMLFedExecModel* pModel);
	void				FillClassList(CCheckListBox& rwndClasses, CStringArray* paClasses);
	void				FillClassesListBox(CListBox& rwndClasses, CStringArray* paClasses);
	void				FillClassesListBox(CComboBox& rwndClasses, CStringArray* paClasses);
	void				StartPubTimer();
	void				StopPubTimer();
	void				FreeFEMModels();
	void				SetDataViewData(bool bPublish);
	void				AddObject();
	void				UpdateObject();
	void				DeleteObject();
	void				TransferObject();
	void				RelinquishObject();

	CString ToString(CMLPropMember* pProps, char* pszPrefix = "");

	// IMLAmbassadorCallback interface
	void				OnSuccess(CMLEvent* pMLEvent);
	void				OnError(CMLException* pMLException);

	// IMLSystemCallback interface
	void				OnSystemEvent(CMLSystemEvent* pSysEvent);

	// IMLObjectEventCallback interface
	void				OnObjectEvent(CMLObjectEvent* pMLDataEvent);

	// IMLTimeEventCallback interface
	void	OnTimeUpdate(CMLTimeManagementEvent* pMLTimeManagementEvent);

	// IMLObjectOwnershipRelinquishCallback interface
	void OnRelinquishOwnership(CMLObjectOwnerRelinquishEvent* pMLObjectOwnerRelinquishEvent);

protected:
	// Generated message map functions
	virtual BOOL OnInitDialog();
	afx_msg void OnSysCommand(UINT nID, LPARAM lParam);
	afx_msg void OnPaint();
	afx_msg HCURSOR OnQueryDragIcon();
	DECLARE_MESSAGE_MAP()

public:
	afx_msg void OnSubscribeEnabled();
	//afx_msg void OnPublishEnabled();
	afx_msg void OnApplyDirective();
	afx_msg void OnApplyDataAction();
	afx_msg void OnTerminate();
	afx_msg void OnReadyToRun();
	afx_msg void OnAddSubs();
	//afx_msg void OnAddPubs();
	afx_msg void OnJoin();
	afx_msg void OnListFEM();
	afx_msg void OnRegister();
	afx_msg void OnInitialize();
	CButton m_ctrlInitialize;
	CButton m_ctrlRegister;
	CButton m_ctrlGetFEM;
	CButton m_ctrlJoin;
	CButton m_ctrlAddSubs;
	//CButton m_ctrlAddPubs;
	CButton m_ctrlReadyRun;
	CButton m_ctrlTerminate;
	//CButton m_ctrlPublishEnabled;
	CButton m_ctrlSubscribeEnabled;
	CListBox m_ctrlFEMList;
	CStatic m_ctrlFRH;
	CString m_strFRH;
	CStatic m_ctrlFEM;
	CString m_strFEM;
	CStatic m_ctrlFEH;
	CString m_strFEH;
	CEdit m_ctrlRegisterName;
	CString m_strRegisterName;
	CEdit m_ctrlMuthurAddress;
	CString m_strMuthurAddress;
	CEdit m_ctrlMuthurPort;
	CButton m_ctrlAppyDataAction;
	int m_iMuthurPort;
	CStatic m_ctrlFedName;
	CString m_strFedName;
	CString m_strFederationName;
	//CCheckListBox m_ctrlPubClasses;
	CCheckListBox m_ctrlSubClasses;
	CMLListCtrl m_ctrlPublicationData;
	CMLListCtrl m_ctrlSubscriptionData;
	CComboBox m_ctrlDirective;
	CString m_strPublishACID;
	CString m_strAttributeValue;
	CEdit m_ctrlPublishACID;
	CEdit m_ctrlAttributeValue;
	//CEdit m_ctrlPublishRate;
	int m_iPubRate;

	CComboBox m_ctrlSubscribeSelections;
	CComboBox m_ctrlObjectTypeSelections;
	CComboBox m_ctrlObjects;
	CComboBox m_ctrlDataActions;
	CComboBox m_ctrlAttributes;
	//CEdit m_ctrlPublishLatitude;
	//CString m_strPublishLatitude;
	//CEdit m_ctrlPublishLongitude;
	//CString m_strPublishLongitude;
	//CEdit m_ctrlPublishAltitude;
	//CString m_strPublishAltitude;
	//CEdit m_ctrlPublishHeading;
	//CString m_strPublishHeading;
	//CEdit m_ctrlPublishPitch;
	//CString m_strPublishPitch;
	//CEdit m_ctrlPublishRoll;
	//CString m_strPublishRoll;
	//CEdit m_ctrlPublishVerticalSpd;
	//CString m_strPublishVerticalSpd;
	//CEdit m_ctrlPublishOnGround;
	//CString m_strPublishOnGround;
	//CEdit m_ctrlPublishFrequency;
	//CString m_strPublishFrequency;
	//CEdit m_ctrlPublishSquawkCode;
	//CString m_strPublishSquawkCode;
	//CEdit m_ctrlPublishIdent;
	//CString m_strPublishIdent;


	CEdit m_ctrlSubscribeACID;
	CString m_strSubscribeACID;
	CEdit m_ctrlSubscribeLatitude;
	CString m_strSubscribeLatitude;
	CEdit m_ctrlSubscribeLongitude;
	CString m_strSubscribeLongitude;
	CEdit m_ctrlSubscribeAltitude;
	CString m_strSubscribeAltitude;
	CEdit m_ctrlSubscribeHeading;
	CString m_strSubscribeHeading;
	CEdit m_ctrlSubscribePitch;
	CString m_strSubscribePitch;
	CEdit m_ctrlSubscribeRoll;
	CString m_strSubscribeRoll;
	CEdit m_ctrlSubscribeVerticalSpd;
	CString m_strSubscribeVerticalSpd;
	CEdit m_ctrlSubscribeOnGround;
	CString m_strSubscribeOnGround;
	CEdit m_ctrlSubscribeFrequency;
	CString m_strSubscribeFrequency;
	CEdit m_ctrlSubscribeSquawkCode;
	CString m_strSubscribeSquawkCode;
	CEdit m_ctrlSubscribeIdent;
	CString m_strSubscribeIdent;
	CEdit m_ctrlTime;
	CString m_strTime;
	CEdit m_ctrlState;
	CString m_strState;
	CEdit m_ctrlCurrentDirective;
	CString m_strCurrentDirective;


	/*CEdit m_ctrlDirective;
	CString m_strDirective;*/
	CEdit m_ctrlPauseLength;
	CString m_strPauseLength;

public:
	afx_msg void OnFEMChanged();
	afx_msg void OnDblClkFEM();
	afx_msg void OnDestroy();
	afx_msg void OnTimer(UINT uEventId);
	afx_msg void OnSubscribeSelChanged();
	afx_msg void OnDirectiveSelChanged();
	afx_msg void OnDataActionSelChanged();
	afx_msg void OnObjectSelChanged();
	afx_msg void OnObjectTypeSelChanged();
	afx_msg void OnAttributeSelChanged();
};
