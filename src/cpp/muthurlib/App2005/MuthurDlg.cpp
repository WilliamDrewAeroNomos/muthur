#include "stdafx.h"
#include "MuthurApp.h"
#include "MuthurDlg.h"
//#include "DVFlightPosition.h"

#define _OUTPUT_TIME_UPDATES 0

#include "MLHelper.h"
#include <MMSystem.h>

#ifdef _DEBUG
#define new DEBUG_NEW
#endif

BEGIN_MESSAGE_MAP(CMuthurDlg, CDialog)
	//{{AFX_MSG_MAP
	ON_WM_SYSCOMMAND()
	ON_WM_PAINT()
	ON_WM_QUERYDRAGICON()
	ON_WM_TIMER()
	ON_WM_DESTROY()
	//}}AFX_MSG_MAP
	ON_BN_CLICKED(IDC_TERMINATE, &CMuthurDlg::OnTerminate)
	ON_BN_CLICKED(IDC_READY_RUN, &CMuthurDlg::OnReadyToRun)
	ON_BN_CLICKED(IDC_ADD_SUBS, &CMuthurDlg::OnAddSubs)
	//ON_BN_CLICKED(IDC_ADD_PUBS, &CMuthurDlg::OnAddPubs)
	ON_BN_CLICKED(IDC_JOIN, &CMuthurDlg::OnJoin)
	ON_BN_CLICKED(IDC_LIST_FEM, &CMuthurDlg::OnListFEM)
	ON_BN_CLICKED(IDC_REGISTER, &CMuthurDlg::OnRegister)
	ON_BN_CLICKED(IDC_INITIALIZE, &CMuthurDlg::OnInitialize)
	ON_BN_CLICKED(IDC_SUBSCRIBE_ENABLED, &CMuthurDlg::OnSubscribeEnabled)
	//	ON_BN_CLICKED(IDC_PUBLISH_ENABLED, &CMuthurDlg::OnPublishEnabled)
	ON_BN_CLICKED(IDC_APPLY_DIRECTIVE, &CMuthurDlg::OnApplyDirective)
	ON_BN_CLICKED(IDC_APPLY_DATA_ACTION, &CMuthurDlg::OnApplyDataAction)
	ON_LBN_SELCHANGE(IDC_FEM_LIST, &CMuthurDlg::OnFEMChanged)
	ON_LBN_DBLCLK(IDC_FEM_LIST, &CMuthurDlg::OnDblClkFEM)
	ON_LBN_SELCHANGE(IDC_SUBSCRIBE_SELECTIONS, &CMuthurDlg::OnSubscribeSelChanged)
	ON_LBN_SELCHANGE(IDC_OBJECT_TYPE_SELECTIONS, &CMuthurDlg::OnObjectTypeSelChanged)
	ON_LBN_SELCHANGE(IDC_ATTRIBUTES, &CMuthurDlg::OnAttributeSelChanged)
	ON_LBN_SELCHANGE(IDC_DATA_ACTION, &CMuthurDlg::OnDataActionSelChanged)
	ON_LBN_SELCHANGE(IDC_OBJECTS, &CMuthurDlg::OnObjectSelChanged)
	ON_LBN_SELCHANGE(IDC_DIRECTIVE_SELECTIONS, &CMuthurDlg::OnDirectiveSelChanged)
	ON_MESSAGE(WM_UPDATE_CTRLS, &CMuthurDlg::OnWMUpdateCtrls)
	//ON_MESSAGE(WM_PUBLISH_DATA, &CMuthurDlg::OnWMPublishData)
END_MESSAGE_MAP()

//------------------------------------------------------------------------------
//!	Summary:	Callback for Window's multimedia timer messages
//!
//!	Parameters:	\li uTimeID - event identifier 
//!				\li dwUser - user supplied value when the event was created
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CALLBACK PublishTimerCallback(UINT uTimerID, UINT uReserved, DWORD dwUser, DWORD, DWORD)
{
	HWND hwnd = (HWND)((INT_PTR)dwUser);

	//	Post a message to the dialog
	if(IsWindow(hwnd))
		PostMessage(hwnd, WM_PUBLISH_DATA, 0, 0);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMuthurDlg::CMuthurDlg(CWnd* pParent) : CDialog(IDD_MUTHURAPP_DIALOG, pParent)
{	
	m_dGroundSpeedCounter = 0;
	
	m_dStartSimTime = -1;//((unsigned long)1331053200);
	m_dCurrentSimTime = -1;//m_ulStartSimTime;
	m_bIsStartTimeSet = false;
	m_pdvSubscribeData = NULL;
	m_pdvPublishData = NULL;
	m_pdvMetronSpawn = NULL;
	m_pdvNexsimSpawn = NULL;
	m_eMLState = MS_NOT_REGISTERED;
	m_iMuthurPort = 64646;
	m_iPubRate = 200;
	m_strRegisterName = "TestApp";
	m_strMuthurAddress = "192.168.5.1";
	m_strPublishACID = "AAL110";
	m_strAttributeValue = "";
	m_strFRH = "";
	m_strFEM = "";
	m_strFEH = "";
	m_strFedName = "";
	m_strFederationName = "";
	/*m_strPublishLatitude = "";
	m_strPublishLongitude = "";
	m_strPublishAltitude = "";
	m_strPublishHeading = "";
	m_strPublishPitch = "";
	m_strPublishRoll = "";
	m_strPublishVerticalSpd = "";
	m_strPublishOnGround = "";
	m_strPublishFrequency = "";
	m_strPublishSquawkCode = "";
	m_strPublishIdent = "";*/


	m_strSubscribeACID = "AAL110";
	m_strFedName = "";
	m_strFederationName = "";
	m_strAttributeValue = "";
	//m_strSubscribeLatitude = "";
	//m_strSubscribeLongitude = "";
	//m_strSubscribeAltitude = "";
	//m_strSubscribeHeading = "";
	//m_strSubscribePitch = "";
	//m_strSubscribeRoll = "";
	//m_strSubscribeVerticalSpd = "";
	//m_strSubscribeOnGround = "";
	//m_strSubscribeFrequency = "";
	//m_strSubscribeSquawkCode = "";
	//m_strSubscribeIdent = "";
	m_strTime = "";
	m_strState = "";
	m_strCurrentDirective = "";
	m_strPauseLength = "-1";

	m_pFedExecModel = NULL;
	m_dwPublishTimerId = 0;
	m_dwRefreshViewsTimerId = 0;
	m_bSubscribeEnabled = false;
	m_bPublishEnabled = false;

	m_aSubClasses.Add("CMLAircraft");
	m_aSubClasses.Add("CMLAircraftArrivalData");
	m_aSubClasses.Add("CMLAircraftDepartureData");
	m_aSubClasses.Add("CMLAircraftTaxiIn");
	m_aSubClasses.Add("CMLAircraftTaxiOut");
	m_aSubClasses.Add("CMLFlightPlan");
	m_aSubClasses.Add("CMLFlightPosition");

	m_ctrlSubClasses.SelItemRange(TRUE, 0, m_ctrlSubClasses.GetCount());

	m_aDirective.Add("Start");
	m_aDirective.Add("Pause");
	m_aDirective.Add("Resume");

	m_aDataActions.Add("Add");
	m_aDataActions.Add("Update");
	m_aDataActions.Add("Delete");
	m_aDataActions.Add("Relinquish Ownership");
	m_aDataActions.Add("Transfer Ownership");

	m_hIcon = AfxGetApp()->LoadIcon(IDR_MAINFRAME);
	m_Objects = CSimpleMap<CComBSTR, CComBSTR>();
	m_ObjectsTypes = CSimpleMap<CComBSTR, CComBSTR>();
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMuthurDlg::~CMuthurDlg()
{
	FreeFEMModels();
	m_pFedExecModel = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to deallocate the collection of FEMs
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::FreeFEMModels()
{
	PTRNODE Pos = NULL;
	CMLFedExecModel* pModel = NULL;

	//	Since the models were allocated by the app, we have to delete them before
	//	the owner list deletes them. Otherwise, there's a heap crash in debug mode
	//	due to the fact that this app is VS2005 but the library is VS2008
	Pos = m_apFEModels.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pModel = (CMLFedExecModel*)(m_apFEModels.GetNext(Pos))) != NULL)
			delete pModel;
	}

	m_apFEModels.RemoveAll(false);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to exchange data between child controls and class members
//!
//!	Parameters:	pDX - data exchange handler
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::DoDataExchange(CDataExchange* pDX)
{
	CDialog::DoDataExchange(pDX);

	DDX_Control(pDX, IDC_INITIALIZE, m_ctrlInitialize);
	DDX_Control(pDX, IDC_REGISTER, m_ctrlRegister);
	DDX_Control(pDX, IDC_LIST_FEM, m_ctrlGetFEM);
	DDX_Control(pDX, IDC_JOIN, m_ctrlJoin);
	DDX_Control(pDX, IDC_ADD_SUBS, m_ctrlAddSubs);
	//DDX_Control(pDX, IDC_ADD_PUBS, m_ctrlAddPubs);
	DDX_Control(pDX, IDC_READY_RUN, m_ctrlReadyRun);
	DDX_Control(pDX, IDC_TERMINATE, m_ctrlTerminate);
	//DDX_Control(pDX, IDC_PUBLISH_ENABLED, m_ctrlPublishEnabled);
	DDX_Control(pDX, IDC_SUBSCRIBE_ENABLED, m_ctrlSubscribeEnabled);
	DDX_Control(pDX, IDC_FEM_LIST, m_ctrlFEMList);
	DDX_Control(pDX, IDC_FRH, m_ctrlFRH);
	DDX_Control(pDX, IDC_FEM, m_ctrlFEM);
	DDX_Control(pDX, IDC_FEH, m_ctrlFEH);
	DDX_Control(pDX, IDC_FED_NAME, m_ctrlFedName);
	DDX_Control(pDX, IDC_REGISTER_NAME, m_ctrlRegisterName);
	DDX_Control(pDX, IDC_MUTHUR_ADDRESS, m_ctrlMuthurAddress);
	DDX_Control(pDX, IDC_PUBLISH_AC_ID, m_ctrlPublishACID);
	DDX_Control(pDX, IDC_ATTRIBUTE_VALUE, m_ctrlAttributeValue);
	DDX_Control(pDX, IDC_MUTHUR_PORT, m_ctrlMuthurPort);
	DDX_Control(pDX, IDC_SUB_CLASSES, m_ctrlSubClasses);
	DDX_Control(pDX, IDC_PUBLICATION_DATA, m_ctrlPublicationData);
	DDX_Control(pDX, IDC_SUBSCRIPTION_DATA, m_ctrlSubscriptionData);
	DDX_Control(pDX, IDC_DIRECTIVE_SELECTIONS, m_ctrlDirective);
	DDX_Control(pDX, IDC_SUBSCRIBE_ACID, m_ctrlSubscribeACID);
	DDX_Control(pDX, IDC_SUBSCRIBE_SELECTIONS, m_ctrlSubscribeSelections);
	DDX_Control(pDX, IDC_OBJECT_TYPE_SELECTIONS, m_ctrlObjectTypeSelections);
	DDX_Control(pDX, IDC_OBJECTS, m_ctrlObjects);
	DDX_Control(pDX, IDC_DATA_ACTION, m_ctrlDataActions);

	DDX_Control(pDX, IDC_APPLY_DATA_ACTION, m_ctrlAppyDataAction);
	DDX_Control(pDX, IDC_ATTRIBUTES, m_ctrlAttributes);
	DDX_Control(pDX, IDC_TIME, m_ctrlTime);
	DDX_Control(pDX, IDC_STATE, m_ctrlState);
	DDX_Control(pDX, IDC_DIRECTIVE, m_ctrlCurrentDirective);
	DDX_Control(pDX, IDC_LENGTH_OF_PAUSE_MSECS, m_ctrlPauseLength);

	DDX_Text(pDX, IDC_FEH, m_strFEH);
	DDX_Text(pDX, IDC_FEM, m_strFEM);
	DDX_Text(pDX, IDC_FRH, m_strFRH);
	DDX_Text(pDX, IDC_FED_NAME, m_strFedName);
	DDX_Text(pDX, IDC_FEDERATION_NAME, m_strFederationName);
	DDX_Text(pDX, IDC_MUTHUR_ADDRESS, m_strMuthurAddress);
	DDX_Text(pDX, IDC_MUTHUR_PORT, m_iMuthurPort);
	DDX_Text(pDX, IDC_REGISTER_NAME, m_strRegisterName);
	DDX_Text(pDX, IDC_PUBLISH_AC_ID, m_strPublishACID);
	DDX_Text(pDX, IDC_ATTRIBUTE_VALUE, m_strAttributeValue);

	DDX_Text(pDX, IDC_SUBSCRIBE_ACID, m_strSubscribeACID);
	DDX_Text(pDX, IDC_TIME, m_strTime);
	DDX_Text(pDX, IDC_DIRECTIVE, m_strState);
	DDX_Text(pDX, IDC_STATE, m_strState);
	DDX_Text(pDX, IDC_LENGTH_OF_PAUSE_MSECS, m_strPauseLength);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to initialize the dialog before it gets displayed
//!
//!	Parameters:	None
//!
//!	Returns:	TRUE for default focus assignment
//------------------------------------------------------------------------------
BOOL CMuthurDlg::OnInitDialog()
{
	CDialog::OnInitDialog();

	// Add "About..." menu item to system menu.
	ASSERT((IDM_ABOUTBOX & 0xFFF0) == IDM_ABOUTBOX);
	ASSERT(IDM_ABOUTBOX < 0xF000);

	CMenu* pSysMenu = GetSystemMenu(FALSE);
	if (pSysMenu != NULL)
	{
		CString strAboutMenu;
		strAboutMenu.LoadString(IDS_ABOUTBOX);
		if (!strAboutMenu.IsEmpty())
		{
			pSysMenu->AppendMenu(MF_SEPARATOR);
			pSysMenu->AppendMenu(MF_STRING, IDM_ABOUTBOX, strAboutMenu);
		}
	}

	// Set the icon for this dialog.  The framework does this automatically
	//  when the application's main window is not a dialog
	SetIcon(m_hIcon, TRUE);			// Set big icon
	SetIcon(m_hIcon, FALSE);		// Set small icon

	// This defines the columns in the data view list boxes
	CMLListCtrlElement listElement;
	m_ctrlPublicationData.Initialize(&listElement);
	m_ctrlSubscriptionData.Initialize(&listElement);
	m_ctrlDirective.Clear();

	//	Set the initial control states
	m_eMLState = MS_NOT_INITIALIZED;
	OnWMUpdateCtrls(0, 0);	

	//	Start the timer used to refresh the data views
	SetTimer(m_dwRefreshViewsTimerId, 100, NULL);

	return TRUE;  // return TRUE  unless you set the focus to a control
}

void CMuthurDlg::OnSysCommand(UINT nID, LPARAM lParam)
{
	if ((nID & 0xFFF0) == IDM_ABOUTBOX)
	{
		CDialog wndAbout(IDD_ABOUTBOX);
		wndAbout.DoModal();
	}
	else
	{
		CDialog::OnSysCommand(nID, lParam);
	}
}

// If you add a minimize button to your dialog, you will need the code below
//  to draw the icon.  For MFC applications using the document/view model,
//  this is automatically done for you by the framework.

void CMuthurDlg::OnPaint()
{
	if (IsIconic())
	{
		CPaintDC dc(this); // device context for painting

		SendMessage(WM_ICONERASEBKGND, reinterpret_cast<WPARAM>(dc.GetSafeHdc()), 0);

		// Center icon in client rectangle
		int cxIcon = GetSystemMetrics(SM_CXICON);
		int cyIcon = GetSystemMetrics(SM_CYICON);
		CRect rect;
		GetClientRect(&rect);
		int x = (rect.Width() - cxIcon + 1) / 2;
		int y = (rect.Height() - cyIcon + 1) / 2;

		// Draw the icon
		dc.DrawIcon(x, y, m_hIcon);
	}
	else
	{
		CDialog::OnPaint();
	}
}

// The system calls this function to obtain the cursor to display while the user drags
//  the minimized window.
HCURSOR CMuthurDlg::OnQueryDragIcon()
{
	return static_cast<HCURSOR>(m_hIcon);
}

CString CMuthurDlg::ToString(CMLPropMember* pProps, char* pszPrefix)
{
	CString strString = "";
	CString	strLine = "";
	CString	strPrefix = "";
	CMLPropMember* pChild = NULL;

	if(pszPrefix != NULL)
		strPrefix = pszPrefix;

	strLine.Format("%s%s = <%s>\n", strPrefix, pProps->GetName(), pProps->GetValue());
	strString = strLine;
	strPrefix += "    ";

	pChild = pProps->GetFirstChild();
	while(pChild != NULL)
	{
		strString += ToString(pChild, strPrefix.GetBuffer());
		pChild = pProps->GetNextChild();
	}

	return strString;	
}

void CMuthurDlg::OnInitialize()
{
	UpdateData(TRUE);

	if(CMLAmbassador::GetInstance().Initialize((char*)((LPCSTR)m_strMuthurAddress), m_iMuthurPort, 0) == true)
	{
		m_eMLState = MS_NOT_REGISTERED;
		OnWMUpdateCtrls(0, 0);	
	}
	else
	{
		CString Msg = "";
		Msg.Format("Unable to connect to Muthur using: %s:%d", m_strMuthurAddress, m_iMuthurPort);
		MessageBox(Msg, "Error", MB_ICONWARNING | MB_OK);
	}
}

/*
CString strXML = "\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<event type=\"dataObject\">\n<aircraftID><acid>N1213</acid><tailNumber>N1213</tailNumber><deptArptCode>KBWI</deptArptCode><arrArptCode>KBNA</arrArptCode></aircraftID><positionData><latitudeDegrees>41.0</latitudeDegrees><longitudeDegrees>467.0</longitudeDegrees><altitudeFt>334.0</altitudeFt><groundspeedKts>0.0</groundspeedKts><cruiseAltitudeFt>0.0</cruiseAltitudeFt><headingDegrees>0.0</headingDegrees><airspeedKts>0.0</airspeedKts><pitchDegrees>0.0</pitchDegrees><rollDegrees>0.0</rollDegrees><sector /><center /></positionData></event>";
CMLPropMember*	pPropTree = NULL;
bool			bSuccessful = false;

if((pPropTree = CMLHelper::GetPropTreeX(strXML.GetBuffer())) != NULL)
{
CString strProps = ToString(pPropTree, "");
MessageBox(strProps, "COOL", MB_OK);
}
else
{
MessageBox(strXML, "BOO", MB_OK);
}
*/

void CMuthurDlg::OnRegister()
{
	CMLRegisterParams params;

	UpdateData(TRUE);

	params.SetName(m_strRegisterName.GetBuffer());
	params.SetSystemCallback(this);
	params.SetAmbassadorCallback(this);
	params.SetTimeManagementCallback(this);


	if(CMLAmbassador::GetInstance().Register(&params) == false)
	{
		MessageBox("Unble to submit Register reguest", "Error", MB_ICONWARNING | MB_OK);
	}

	m_strRegisterName.ReleaseBuffer();
	m_strMuthurAddress.ReleaseBuffer();
}

void CMuthurDlg::OnListFEM()
{
	CMLListFEMParams params;

	params.SetFedRegHandle(m_hFRH);
	params.SetAmbassadorCallback(this);

	CMLAmbassador::GetInstance().ListFedExecModels(&params);
}

void CMuthurDlg::OnJoin()
{
	CMLJoinFedParams params;

	params.SetFedRegHandle(m_hFRH);
	params.SetAmbassadorCallback(this);
	params.SetFEM(*m_pFedExecModel);

	m_hFEM = m_pFedExecModel->GetFedExecModelHandle();
	m_strFEM = m_hFEM.GetMuthurId();
	m_strFederationName = m_pFedExecModel->GetName();

	CMLAmbassador::GetInstance().JoinFederation(&params);
}

//void CMuthurDlg::OnAddPubs()
//{
//	CMLAddDataPubParams params;
//
//	//	Get the classes selected by the user
//	FillClassList(m_ctrlPubClasses, &m_aReqPublications);
//
//	if(m_aReqPublications.GetCount() > 0)
//	{
//		params.SetFedRegHandle(m_hFRH);
//		params.SetFedExecHandle(m_hFEH);
//		params.SetAmbassadorCallback(this);
//
//		for(int i = 0; i <= m_aReqPublications.GetUpperBound(); i++)
//		{
//			params.AddClass(m_aReqPublications[i]);
//		}
//
//		CMLAmbassador::GetInstance().AddDataPublications(&params);
//
//	}
//	else
//	{
//		MessageBox("You must select one or more classes to publish");
//	}
//
//}

void CMuthurDlg::OnAddSubs()
{
	CMLAddDataSubParams params;

	//	Get the classes selected by the user
	FillClassList(m_ctrlSubClasses, &m_aReqSubscriptions);

	if(m_aReqSubscriptions.GetCount() > 0)
	{
		params.SetFedRegHandle(m_hFRH);
		params.SetFedExecHandle(m_hFEH);
		params.SetAmbassadorCallback(this);
		params.SetDataEventCallback(this);		
		params.SetObjectOwnershipRelinquishEventCallback(this);

		for(int i = 0; i <= m_aReqSubscriptions.GetUpperBound(); i++)
		{
			params.AddClass(m_aReqSubscriptions[i]);
		}

		CMLAmbassador::GetInstance().AddDataSubscriptions(&params);

	}
	else
	{
		MessageBox("You must select one or more classes to subscribe to");
	}

}

void CMuthurDlg::OnReadyToRun()
{
	CMLReadyParams params;

	params.SetFedRegHandle(m_hFRH);
	params.SetFedExecHandle(m_hFEH);
	params.SetAmbassadorCallback(this);

	CMLAmbassador::GetInstance().ReadyToRun(&params);
}

void CMuthurDlg::OnTerminate()
{
	CMLTerminateParams params;
	params.SetFedRegHandle(m_hFRH);
	params.SetFedExecHandle(m_hFEH);
	params.SetAmbassadorCallback(this);

	CMLAmbassador::GetInstance().Terminate(&params);
}

void CMuthurDlg::OnSuccess(CMLEvent* pMLEvent)
{
	USES_CONVERSION;
	CMLRegisterResponse*	pRegister = NULL;
	CMLListFEMResponse*		pListFEM = NULL;
	CMLJoinFedResponse*		pJoin = NULL;
	CMLFedExecModel*		pModel = NULL;
	//CMLAddDataPubResponse*	pAddPubs = NULL;
	CMLCreateObjectResponse* pCreateObj = NULL;
	CMLUpdateObjectResponse* pUpdateObj = NULL;
	CMLDeleteObjectResponse* pDeleteObj = NULL;
	CMLRelinquishObjectOwnershipResponse *pRel = NULL;
	CMLTransferObjectOwnershipResponse* pTrans = NULL;
	CMLAddDataSubResponse*	pAddSubs = NULL;
	CMLReadyResponse*		pReady = NULL;
	CMLStartFederationResponse* pStart = NULL;
	CMLPauseFederationResponse* pPause = NULL;
	CMLResumeFederationResponse* pResume = NULL;
	const char*				pszClass = NULL;
	int iIndex = -1;
	int iIndexType = -1;



	switch(pMLEvent->GetClass())
	{
	case ML_RESPONSE_CLASS_REGISTER:

		pRegister = (CMLRegisterResponse*)pMLEvent;
		m_hFRH = pRegister->GetFedRegHandle();
		m_strFRH = m_hFRH.GetMuthurId();
		m_strFedName = CMLAmbassador::GetInstance().GetFederateName();
		m_eMLState = MS_REGISTERED;
		break;

	case ML_RESPONSE_CLASS_LIST_FEM:

		pListFEM = (CMLListFEMResponse*)pMLEvent;

		FreeFEMModels();

		pModel = pListFEM->GetFirstFedExecModel();
		while(pModel != NULL)
		{
			m_apFEModels.AddTail(new CMLFedExecModel(*pModel)); // add copy to our local collection
			pModel = pListFEM->GetNextFedExecModel();
		}

		m_eMLState = MS_LISTED_FEMS;
		break;

	case ML_RESPONSE_CLASS_JOIN_FED:

		pJoin = (CMLJoinFedResponse*)pMLEvent;

		m_hFEH = pJoin->GetFedExecHandle();
		m_strFEH = m_hFEH.GetMuthurId();		
		m_eMLState = MS_JOINED;
		m_ctrlSubClasses.SelItemRange(TRUE, 0, m_ctrlSubClasses.GetCount());
		break;

	case ML_RESPONSE_CLASS_CREATE_OBJECT:

		pCreateObj = (CMLCreateObjectResponse*)pMLEvent;

		if(m_Objects.GetSize() > 0)
		{
			for(int i = 0; i < m_Objects.GetSize(); i++)
			{	
				char *tempStr = OLE2T(m_Objects.GetValueAt(i));
				m_aObjCallSigns.Add(tempStr);
			}
		}

		FillClassesListBox(m_ctrlObjects, &m_aObjCallSigns);
		//m_eMLState = MS_CREATED_OBJECT;
		break;

	case ML_RESPONSE_CLASS_UPDATE_OBJECT:

		pUpdateObj = (CMLUpdateObjectResponse*)pMLEvent;
		if(m_Objects.GetSize() > 0)
		{
			for(int i = 0; i < m_Objects.GetSize(); i++)
			{	
				char *tempStr = OLE2T(m_Objects.GetValueAt(i));
				m_aObjCallSigns.Add(tempStr);
			}
		}
		FillClassesListBox(m_ctrlObjects, &m_aObjCallSigns);	
		//m_eMLState = MS_UPDATED_OBJECT;
		break;

	case ML_RESPONSE_CLASS_DELETE_OBJECT:

		pDeleteObj = (CMLDeleteObjectResponse*)pMLEvent;

		if(pDeleteObj->GetDataObjectUUID())
		{
			iIndex = m_Objects.FindKey(pDeleteObj->GetDataObjectUUID());
			if(iIndex >= 0) iIndexType = m_ObjectsTypes.FindKey(m_Objects.GetKeyAt(iIndex));
		}
		if(iIndex >= 0) m_Objects.RemoveAt(iIndex);
		if(iIndexType >= 0) m_ObjectsTypes.RemoveAt(iIndexType);

		if(m_Objects.GetSize() > 0)
		{
			for(int i = 0; i < m_Objects.GetSize(); i++)
			{	
				char *tempStr = OLE2T(m_Objects.GetValueAt(i));
				m_aObjCallSigns.Add(tempStr);
			}
		}
		FillClassesListBox(m_ctrlObjects, &m_aObjCallSigns);	
		//m_eMLState = MS_DELETED_OBJECT;
		break;

	case ML_RESPONSE_CLASS_RELINQUISH_OBJECT_OWNERSHIP:
		pRel = (CMLRelinquishObjectOwnershipResponse*)pMLEvent;

		if(m_pdvPublishData && m_pdvPublishData->GetDataObject() && 
			CString(m_pdvPublishData->GetDataObject()->GetDataObjectUUID()).CompareNoCase(pRel->GetDataObjectUUID()) == 0)
		{   // Successfully relinquished object
			m_pdvPublishData->SetOwnership(false);
		}
		break;

	case ML_RESPONSE_CLASS_TRANSFER_OBJECT_OWNERSHIP:
		pTrans = (CMLTransferObjectOwnershipResponse*) pMLEvent;
		//Set m_pdvPublishData based on selected on in objects list
		if(m_pdvPublishData && m_pdvPublishData->GetDataObject() && 
			CString(m_pdvPublishData->GetDataObject()->GetDataObjectUUID()).CompareNoCase(pTrans->GetDataObjectUUID()) == 0)
		{   //Successfully transferred ownership of object to self
			m_pdvPublishData->SetOwnership(true);
		}
		break;

	case ML_RESPONSE_CLASS_ADD_DATA_SUB:

		pAddSubs = (CMLAddDataSubResponse*)pMLEvent;

		m_eMLState = MS_ADDED_DATA_SUBS;

		break;

	case ML_RESPONSE_CLASS_READY:

		pReady = (CMLReadyResponse*)pMLEvent;

		//	Enable / disable the data view controls for publish and subscribe 
		/*m_ctrlDataActions.SetCurSel(0);
		m_ctrlObjectTypeSelections.SetCurSel(0);
		m_ctrlSubscribeSelections.SetCurSel(0);*/
		//m_ctrlSubscribeSelections.SetCurSel(0);

		SetDataViewData(true);
		SetDataViewData(false);

		m_eMLState = MS_RUNNING;
		break;
	case ML_RESPONSE_CLASS_START_FED:
		pStart = (CMLStartFederationResponse*)pMLEvent;
		break;
	case ML_RESPONSE_CLASS_PAUSE_FED:
		pPause = (CMLPauseFederationResponse*)pMLEvent;		
		break;
	case ML_RESPONSE_CLASS_RESUME_FED:
		pResume = (CMLResumeFederationResponse*)pMLEvent;
		break;
	}

	// GUI updates should only be performed by the thread in which they were
	// created. This method is being called by an Ambassador thread so posting
	// a message allows us to return control before doing the update.
	PostMessage(WM_UPDATE_CTRLS, 0, 0);
}

void CMuthurDlg::OnError(CMLException* pMLException)
{
	USES_CONVERSION;
	MessageBox(pMLException->GetMessage(), "Ambassador Error", MB_OK);

	CMLCreateObjectResponse* pCreateObj = NULL;
	CMLUpdateObjectResponse* pUpdateObj = NULL;
	CMLDeleteObjectResponse* pDeleteObj = NULL;
	CMLRelinquishObjectOwnershipResponse* pRel = NULL;
	CMLTransferObjectOwnershipResponse* pTrans = NULL;

	const char*				pszClass = NULL;
	int i = 0;
	int iIndex = -1;
	int iIndexType = -1;
	if(pMLException->GetRequest())
	{
		switch(pMLException->GetRequest()->GetClass())
		{
			//Error received when trying to create an object; remove it from list of objects for this 
			//federate.
		case ML_RESPONSE_CLASS_CREATE_OBJECT:

			pCreateObj = (CMLCreateObjectResponse*)pMLException->GetRequest();

			if(pCreateObj->GetUUID())
			{
				// Finish switching keys and value - UUID should be key and not call sign
				iIndex = m_Objects.FindKey(pCreateObj->GetUUID());
				iIndexType = m_ObjectsTypes.FindKey(m_Objects.GetKeyAt(iIndex));
			}
			if(iIndex >= 0) m_Objects.RemoveAt(iIndex);
			if(iIndexType >=0) m_ObjectsTypes.RemoveAt(iIndexType);

			if(m_Objects.GetSize() > 0)
			{
				for(int i = 0; i < m_Objects.GetSize(); i++)
				{	
					char *tempStr = OLE2T(m_Objects.GetValueAt(i));
					m_aObjCallSigns.Add(tempStr);
				}
			}
			FillClassesListBox(m_ctrlObjects, &m_aObjCallSigns);
			break;

		case ML_RESPONSE_CLASS_UPDATE_OBJECT:
			pUpdateObj = (CMLUpdateObjectResponse*)pMLException->GetRequest();
			break;

		case ML_RESPONSE_CLASS_DELETE_OBJECT:
			pDeleteObj = (CMLDeleteObjectResponse*)pMLException->GetRequest();
			if(m_Objects.GetSize() > 0)
			{
				for(int i = 0; i < m_Objects.GetSize(); i++)
				{	
					char *tempStr = OLE2T(m_Objects.GetValueAt(i));
					m_aObjCallSigns.Add(tempStr);
				}
			}
			FillClassesListBox(m_ctrlObjects, &m_aObjCallSigns);	
			break;

		case ML_RESPONSE_CLASS_RELINQUISH_OBJECT_OWNERSHIP:
			pRel = (CMLRelinquishObjectOwnershipResponse*)pMLException->GetRequest();

			if(m_pdvPublishData && m_pdvPublishData->GetDataObject() && 
				CString(m_pdvPublishData->GetDataObject()->GetDataObjectUUID()).CompareNoCase(pRel->GetDataObjectUUID()) == 0)
			{   // Successfully relinquished object
				m_pdvPublishData->SetOwnership(true);
			}
			break;

		case ML_RESPONSE_CLASS_TRANSFER_OBJECT_OWNERSHIP:
			pTrans = (CMLTransferObjectOwnershipResponse*)pMLException->GetRequest();
			//Set m_pdvPublishData based on selected on in objects list
			if(m_pdvPublishData && m_pdvPublishData->GetDataObject() && 
				CString(m_pdvPublishData->GetDataObject()->GetDataObjectUUID()).CompareNoCase(pTrans->GetDataObjectUUID()) == 0)
			{   //Successfully transferred ownership of object to self
				m_pdvPublishData->SetOwnership(false);
			}
			break;

		}
	}

	// GUI updates should only be performed by the thread in which they were
	// created. This method is being called by an Ambassador thread so posting
	// a message allows us to return control before doing the update.
	PostMessage(WM_UPDATE_CTRLS, 0, 0);
}

void CMuthurDlg::OnSystemEvent(CMLSystemEvent* pSysEvent)
{
	CString strMsg = "";

	switch(pSysEvent->GetEventType())
	{
	case ML_SYSTEM_TERMINATE_FEDERATION:

		StopPubTimer();

		//	Return to preparing to Join
		m_strFEM = "";
		m_strFEH = "";
		m_strFederationName = "";
		m_pFedExecModel = NULL;
		//m_ctrlPubClasses.ResetContent();
		m_ctrlSubClasses.ResetContent();
		m_ctrlSubClasses.SelItemRange(TRUE, 0, m_ctrlSubClasses.GetCount());
		m_eMLState = MS_LISTED_FEMS;

		if(lstrlen(pSysEvent->GetTerminationReason()) > 0)
		{
			strMsg.Format("Terminating Federation\n\n%s\n\nReturning to Join", pSysEvent->GetTerminationReason());
		}
		else
		{
			strMsg = "Terminating federation for unknown reason.\n\nReturning to Join";
		}

		MessageBox(strMsg, "Terminate", MB_OK);

		PostMessage(WM_UPDATE_CTRLS, 0, 0);
		break;

	case ML_SYSTEM_RESET_REQUIRED:
		StopPubTimer();

		//	Return to preparing to Initialize
		m_pdvSubscribeData = NULL;
		m_pdvPublishData = NULL;
		m_eMLState = MS_NOT_REGISTERED;
		m_iMuthurPort = 64646;
		m_iPubRate = 1000;
		m_strRegisterName = "TestApp";
		m_strMuthurAddress = "192.168.5.1";
		m_strPublishACID = "AAL110";
		m_strAttributeValue = "";
		m_strFRH = "";
		m_strFEM = "";
		m_strFEH = "";
		m_strFedName = "";
		m_strFederationName = "";
		m_pFedExecModel = NULL;
		m_dwPublishTimerId = 0;
		m_bSubscribeEnabled = false;
		m_bPublishEnabled = false;

		m_aSubClasses.Add("CMLAircraft");
		m_aSubClasses.Add("CMLAircraftArrivalData");
		m_aSubClasses.Add("CMLAircraftDepartureData");
		m_aSubClasses.Add("CMLAircraftTaxiIn");
		m_aSubClasses.Add("CMLAircraftTaxiOut");
		m_aSubClasses.Add("CMLFlightPlan");
		m_aSubClasses.Add("CMLFlightPosition");

		//m_strFRH = "";
		//m_strFEM = "";
		//m_strFEH = "";
		//m_strFederationName = "";
		//m_pFedExecModel = NULL;
		//m_ctrlPubClasses.ResetContent();
		m_ctrlSubClasses.ResetContent();
		m_ctrlSubClasses.SelItemRange(TRUE, 0, m_ctrlSubClasses.GetCount());
		m_ctrlFEMList.ResetContent();
		m_eMLState = MS_NOT_INITIALIZED;

		MessageBox("Federation Termination Notification\n\nReturning to Initialize", "", MB_OK);

		//PostMessage(WM_UPDATE_ML_STATE, 0, 0);
		PostMessage(WM_UPDATE_CTRLS, 0, 0);
		break;

	}
}


//------------------------------------------------------------------------------
//!	Summary:	Called by the ambassador when an object ownership event occurs
//!
//!	Parameters:	\li pMLObjectOwnerRelinquishEvent - the Muthur object ownership event wrapper
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnRelinquishOwnership(CMLObjectOwnerRelinquishEvent* pMLObjectOwnerRelinquishEvent)
{
	if(m_bSubscribeEnabled && m_pdvSubscribeData && pMLObjectOwnerRelinquishEvent && 
		pMLObjectOwnerRelinquishEvent->GetDataObjectUUID())
	{
		m_ctrlSubscribeACID.GetWindowText(m_strSubscribeACID); // get the ACID filter
		if(m_pdvSubscribeData->GetDataObject() && m_pdvSubscribeData->GetDataObject()->GetDataObjectUUID() &&
			m_pdvSubscribeData->GetDataObject()->GetACId() && m_strSubscribeACID.GetLength() > 0 && 
			CString(m_pdvSubscribeData->GetDataObject()->GetACId()).CompareNoCase(m_strSubscribeACID) == 0
			&& m_pdvSubscribeData->GetDataObject()->GetDataObjectType() == m_pdvSubscribeData->GetClass())
		{
			m_pdvSubscribeData->SetOwnership(false);

		}		
	}
}


//------------------------------------------------------------------------------
//!	Summary:	Called by the ambassador when an object event occurs
//!
//!	Parameters:	\li pObjEvent - the Muthur object event wrapper
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnObjectEvent(CMLObjectEvent* pObjEvent)
{
	//MessageBox("Entered object event callback", "Debug", MB_OK);
	//SetDataViewData(false);
	if(m_bSubscribeEnabled && m_pdvSubscribeData && pObjEvent && pObjEvent->GetDataObject() && 
		pObjEvent->GetDataObject()->GetACId() && pObjEvent->GetDataObject()->GetDataObjectUUID())
	{
		if(m_pdvSubscribeData->GetClass() == pObjEvent->GetDataObject()->GetDataObjectType())
			m_ctrlSubscribeACID.GetWindowText(m_strSubscribeACID); // get the ACID filter

		// Received object add event from another federate but I do NOT own it
		if(pObjEvent->GetDataAction() && CString(pObjEvent->GetDataAction()).CompareNoCase("Add") == 0)
		{
			if((m_strSubscribeACID.GetLength() > 0) &&  
				(m_strSubscribeACID.CompareNoCase(pObjEvent->GetDataObject()->GetACId()) == 0))
			{
				if(m_Objects.FindKey(pObjEvent->GetDataObject()->GetDataObjectUUID()) < 0) 
				{	
					m_pdvSubscribeData->SetOwnership(false);
					m_Objects.Add(pObjEvent->GetDataObject()->GetDataObjectUUID(), pObjEvent->GetDataObject()->GetACId());
					m_ObjectsTypes.Add(pObjEvent->GetDataObject()->GetDataObjectUUID(), typeid((*pObjEvent->GetDataObject())).name());
					if(pObjEvent->GetDataObject()->GetDataObjectType() == m_pdvSubscribeData->GetClass())
						m_pdvSubscribeData->Update(pObjEvent->GetDataObject());	

				}
			}
		}
		// Received object update event from another federate but I do NOT own it
		else if(pObjEvent->GetDataAction() && CString(pObjEvent->GetDataAction()).CompareNoCase("Update") == 0)
		{
			if((m_strSubscribeACID.GetLength() > 0) && 
				(m_strSubscribeACID.CompareNoCase(pObjEvent->GetDataObject()->GetACId()) == 0))
			{
				
				if((m_Objects.FindKey(pObjEvent->GetDataObject()->GetDataObjectUUID()) >= 0) && 
					(pObjEvent->GetDataObject()->GetDataObjectType() == m_pdvSubscribeData->GetClass()))
				{
					m_pdvSubscribeData->SetOwnership(false);
					m_pdvSubscribeData->Update(pObjEvent->GetDataObject());	
				}
			}
		}
		// Received object delete event from another federate but I do NOT own it
		else if(pObjEvent->GetDataAction() && CString(pObjEvent->GetDataAction()).CompareNoCase("Delete") == 0)
		{	
			if((m_strSubscribeACID.GetLength() > 0) && 
				(m_strSubscribeACID.CompareNoCase(pObjEvent->GetDataObject()->GetACId()) == 0))
			{
				int iInd = -1;
				int iIndexType = -1;			
				if(m_Objects.FindKey(pObjEvent->GetDataObject()->GetDataObjectUUID()) >= 0)
				{
					iInd = m_Objects.FindKey(pObjEvent->GetDataObject()->GetDataObjectUUID());
					iIndexType = m_ObjectsTypes.FindKey(m_Objects.GetKeyAt(iInd));

					if(iInd >= 0) m_Objects.RemoveAt(iInd);
					if(iIndexType >= 0) m_ObjectsTypes.RemoveAt(iIndexType);
					if(pObjEvent->GetDataObject()->GetDataObjectType() == m_pdvSubscribeData->GetClass())
						m_pdvSubscribeData->Update(NULL);
				}
			}
		}
	}// if((m_pdvSubscribeData != NULL) && (pDataEvent != NULL) && (pDataEvent->GetDataObject() != NULL))
}

//------------------------------------------------------------------------------
//!	Summary:	Called by the ambassador when a time management event occurs
//!
//!	Parameters:	\li pMLTimeManagementEvent - the Muthur time management event wrapper
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnTimeUpdate(CMLTimeManagementEvent* pMLTimeManagementEvent)
{
	if(/*(m_bSubscribeEnabled == true) && (m_pdvSubscribeData != NULL) && */(pMLTimeManagementEvent != NULL)/* && (pDataEvent->GetDataObject() != NULL)*/)
	{
		if(!m_bIsStartTimeSet)
		{
			m_dStartSimTime = pMLTimeManagementEvent->GetTimeInterval();
			m_bIsStartTimeSet = true;
		}

		m_dCurrentSimTime = m_dStartSimTime = pMLTimeManagementEvent->GetTimeInterval();

		if(_OUTPUT_TIME_UPDATES)  // Set this #define to 1 to send the time interval updates to a file on the c drive (note this file will be appended and will not delete itself)
		{
			double dTimeInterval = pMLTimeManagementEvent->GetTimeInterval();
			FILE * pFile;
			string sFileName = "C:\\";
			sFileName.append(string(m_strRegisterName.GetBuffer()).c_str());
			sFileName.append("TimeIntervals.txt");
			fopen_s (&pFile, sFileName.c_str(), "a");

			char buffer [2056];
			int n, a=5, b=3;

			n = sprintf_s (buffer, sizeof(buffer), "%13.0f\n", dTimeInterval);
			if (pFile!=NULL)
			{
				fputs (buffer, pFile);
				fclose (pFile);
			}
		}
		char buffer[1028];
	//	if(pMLTimeManagementEvent->GetFederationExecutionDirective() == NEXFED_BRIDGE_DIRECTIVE_STATE_RESUME || pMLTimeManagementEvent->GetFederationExecutionDirective() == 
	//				NEXFED_BRIDGE_DIRECTIVE_STATE_RUN || pMLTimeManagementEvent->GetFederationExecutionDirective() == NEXFED_BRIDGE_DIRECTIVE_STATE_START)
	//	{
	///*		time_t tempDeptTime = static_cast<time_t>(m_ulCurrentSimTime);  
	//		struct tm t1;
	//		_gmtime64_s( &t1, &tempDeptTime );*/
	//		
	//		m_ulCurrentSimTime += ((unsigned long)1000); //pMLTimeManagementEvent->GetTimeInterval();
	//	}
		sprintf_s(buffer, sizeof(buffer), "%14.0f",  pMLTimeManagementEvent->GetTimeInterval());
		m_strTime.SetString(buffer);
		m_ctrlTime.SetWindowTextA(buffer);

		char buffer1[1028];
		sprintf_s(buffer1, sizeof(buffer1), "%d", pMLTimeManagementEvent->GetFederationExecutionState());
		m_strState.SetString(buffer1);
		m_ctrlState.SetWindowTextA(buffer1);

		char buffer2[1028];
		sprintf_s(buffer2, sizeof(buffer2), "%d", pMLTimeManagementEvent->GetFederationExecutionDirective());
		m_strCurrentDirective.SetString(buffer2);
		m_ctrlCurrentDirective.SetWindowTextA(buffer2);
	}// if((m_pdvSubscribeData != NULL) && (pDataEvent != NULL) && (pDataEvent->GetDataObject() != NULL))
}

//------------------------------------------------------------------------------
//!	Summary:	Called to handle custom WM_UPDATE_CTRLS messages sent to this
//!				window
//!
//!	Parameters:	\li wParam - not used
//!				\li lParam - not used
//!
//!	Returns:	Zero if handled
//------------------------------------------------------------------------------
LONG CMuthurDlg::OnWMUpdateCtrls(WPARAM wParam, LPARAM lParam)
{
	PTRNODE			 Pos = NULL;
	CMLFedExecModel* pModel = NULL;
	int				 iIdx = -1;

	if(m_eMLState == MS_LISTED_FEMS)
	{
		// Fill the list of avaiable models
		m_ctrlFEMList.ResetContent();
		m_pFedExecModel = NULL;

		Pos = m_apFEModels.GetHeadPosition();

		pModel = (CMLFedExecModel*)(m_apFEModels.GetHead());
		while(Pos != NULL)
		{
			pModel = (CMLFedExecModel*)(m_apFEModels.GetNext(Pos));

			if(pModel != NULL)
			{
				iIdx = m_ctrlFEMList.AddString(pModel->GetName());
				if(iIdx >= 0)
					m_ctrlFEMList.SetItemData(iIdx, (INT_PTR)pModel);
			}

		}// while(Pos != NULL)

		if(m_ctrlFEMList.GetCount() > 0)
			m_ctrlFEMList.SetCurSel(0);

		m_pFedExecModel = (CMLFedExecModel*)m_ctrlFEMList.GetItemData(0);

	}// if(m_eMLState == MS_LISTED_FEMS)

	else if(m_eMLState == MS_JOINED)
	{
		/*FillClassesListBox(m_ctrlPubClasses, &m_aPubClasses);	
		}
		else if(m_eMLState == MS_ADDED_DATA_PUBS)
		{*/

		FillClassesListBox(m_ctrlSubClasses, &m_aSubClasses);	
	}
	else if(m_eMLState == MS_ADDED_DATA_SUBS)
	{
		FillClassesListBox(m_ctrlSubscribeSelections, &m_aReqSubscriptions);
		FillClassesListBox(m_ctrlObjectTypeSelections, &m_aSubClasses);
	}

	m_ctrlFRH.EnableWindow(m_eMLState != MS_NOT_REGISTERED);
	m_ctrlFEM.EnableWindow((m_eMLState != MS_NOT_REGISTERED) && (m_eMLState != MS_REGISTERED));

	m_ctrlInitialize.EnableWindow(m_eMLState == MS_NOT_INITIALIZED);
	m_ctrlMuthurAddress.EnableWindow(m_ctrlInitialize.IsWindowEnabled());
	m_ctrlMuthurPort.EnableWindow(m_ctrlInitialize.IsWindowEnabled());

	m_ctrlRegister.EnableWindow(m_eMLState == MS_NOT_REGISTERED);
	m_ctrlRegisterName.EnableWindow(m_ctrlRegister.IsWindowEnabled());

	m_ctrlGetFEM.EnableWindow(m_eMLState == MS_REGISTERED);

	m_ctrlJoin.EnableWindow((m_eMLState == MS_LISTED_FEMS) && (m_pFedExecModel != NULL));

	//m_ctrlPubClasses.EnableWindow(m_eMLState == MS_JOINED);
	//m_ctrlAddPubs.EnableWindow(m_eMLState == MS_JOINED);

	m_ctrlSubClasses.EnableWindow(m_eMLState == MS_JOINED);
	m_ctrlAddSubs.EnableWindow(m_eMLState == MS_JOINED);

	m_ctrlReadyRun.EnableWindow(m_eMLState == MS_ADDED_DATA_SUBS);
	m_ctrlTerminate.EnableWindow(m_eMLState == MS_RUNNING);

	m_ctrlAppyDataAction.EnableWindow(m_eMLState == MS_RUNNING);


	//	Data view controls
	if(m_eMLState == MS_RUNNING)
	{
		FillClassesListBox(m_ctrlDirective, &m_aDirective);	
		FillClassesListBox(m_ctrlDataActions, &m_aDataActions);	
		if(m_aObjCallSigns.GetSize() > 0) 
			FillClassesListBox(m_ctrlObjects, &m_aObjCallSigns);	


		/*m_ctrlDataActions.SetCurSel(0);
		m_ctrlObjectTypeSelections.SetCurSel(0);
		m_ctrlSubscribeSelections.SetCurSel(0);*/
		/*CStringArray tempArray = CStringArray();
		tempArray.Add("temp");
		FillClassesListBox(m_ctrlAttributes, &tempArray);*/


		SetDataViewData(true);
		SetDataViewData(false);
	}
	else
	{
		m_pdvSubscribeData = NULL;
		m_pdvPublishData = NULL;
		m_pdvMetronSpawn = NULL;
		m_pdvNexsimSpawn = NULL;
	}

	if(m_pdvSubscribeData != NULL)
	{
		m_ctrlSubscribeEnabled.EnableWindow(TRUE);
		m_ctrlSubscribeACID.EnableWindow(TRUE);
		m_ctrlSubscriptionData.Add(m_pdvSubscribeData->GetElements(), TRUE);
		m_ctrlSubscriptionData.EnableWindow(TRUE);
		m_ctrlSubscribeSelections.EnableWindow(TRUE);	
		/*m_ctrlSubscribeLatitude.EnableWindow(TRUE);
		m_ctrlSubscribeLongitude.EnableWindow(TRUE);
		m_ctrlSubscribeAltitude.EnableWindow(TRUE);
		m_ctrlSubscribeHeading.EnableWindow(TRUE);
		m_ctrlSubscribePitch.EnableWindow(TRUE);
		m_ctrlSubscribeRoll.EnableWindow(TRUE);
		m_ctrlSubscribeVerticalSpd.EnableWindow(TRUE);
		m_ctrlSubscribeOnGround.EnableWindow(TRUE);
		m_ctrlSubscribeFrequency.EnableWindow(TRUE);
		m_ctrlSubscribeSquawkCode.EnableWindow(TRUE);
		m_ctrlSubscribeIdent.EnableWindow(TRUE);*/
	}
	else
	{
		m_ctrlSubscribeEnabled.EnableWindow(FALSE);
		m_ctrlSubscribeEnabled.SetCheck(0);
		m_ctrlSubscribeACID.EnableWindow(FALSE);
		m_ctrlSubscriptionData.Clear();
		m_ctrlSubscriptionData.EnableWindow(FALSE);
		m_ctrlSubscribeSelections.EnableWindow(m_eMLState == MS_RUNNING);	
		/*m_ctrlSubscribeLatitude.EnableWindow(FALSE);
		m_ctrlSubscribeLongitude.EnableWindow(FALSE);
		m_ctrlSubscribeAltitude.EnableWindow(FALSE);
		m_ctrlSubscribeHeading.EnableWindow(FALSE);
		m_ctrlSubscribePitch.EnableWindow(FALSE);
		m_ctrlSubscribeRoll.EnableWindow(FALSE);
		m_ctrlSubscribeVerticalSpd.EnableWindow(FALSE);
		m_ctrlSubscribeOnGround.EnableWindow(FALSE);
		m_ctrlSubscribeFrequency.EnableWindow(FALSE);
		m_ctrlSubscribeSquawkCode.EnableWindow(FALSE);
		m_ctrlSubscribeIdent.EnableWindow(FALSE);*/
	}

	if(m_pdvPublishData != NULL)
	{
		//m_ctrlPublishEnabled.EnableWindow(TRUE);
		m_ctrlPublishACID.EnableWindow(TRUE);
		m_ctrlAttributeValue.EnableWindow(TRUE);
		//m_ctrlPublishRate.EnableWindow(TRUE);
		m_ctrlPublicationData.Add(m_pdvPublishData->GetElements(), TRUE);
		m_ctrlPublicationData.EnableWindow(TRUE);
		m_ctrlObjectTypeSelections.EnableWindow(TRUE);	
		//m_ctrlAttributes.EnableWindow(TRUE);
		m_ctrlObjects.EnableWindow(TRUE);
		m_ctrlDataActions.EnableWindow(TRUE);
		/*m_ctrlPublishLatitude.EnableWindow(TRUE);
		m_ctrlPublishLongitude.EnableWindow(TRUE);
		m_ctrlPublishAltitude.EnableWindow(TRUE);
		m_ctrlPublishHeading.EnableWindow(TRUE);
		m_ctrlPublishPitch.EnableWindow(TRUE);
		m_ctrlPublishRoll.EnableWindow(TRUE);
		m_ctrlPublishVerticalSpd.EnableWindow(TRUE);
		m_ctrlPublishOnGround.EnableWindow(TRUE);
		m_ctrlPublishFrequency.EnableWindow(TRUE);
		m_ctrlPublishSquawkCode.EnableWindow(TRUE);
		m_ctrlPublishIdent.EnableWindow(TRUE);		*/
	}
	else
	{
		//m_ctrlPublishEnabled.EnableWindow(FALSE);
		//m_ctrlPublishEnabled.SetCheck(0);
		m_ctrlPublishACID.EnableWindow(FALSE);
		m_ctrlAttributeValue.EnableWindow(FALSE);
		//m_ctrlPublishRate.EnableWindow(FALSE);
		m_ctrlPublicationData.Clear();
		m_ctrlPublicationData.EnableWindow(FALSE);
		m_ctrlObjectTypeSelections.EnableWindow(m_eMLState == MS_RUNNING);	
		//m_ctrlAttributes.EnableWindow(m_eMLState == MS_RUNNING);	
		m_ctrlObjects.EnableWindow(m_eMLState == MS_RUNNING);	
		m_ctrlDataActions.EnableWindow(m_eMLState == MS_RUNNING);	
		/*m_ctrlPublishLatitude.EnableWindow(FALSE);
		m_ctrlPublishLongitude.EnableWindow(FALSE);
		m_ctrlPublishAltitude.EnableWindow(FALSE);
		m_ctrlPublishHeading.EnableWindow(FALSE);
		m_ctrlPublishPitch.EnableWindow(FALSE);
		m_ctrlPublishRoll.EnableWindow(FALSE);
		m_ctrlPublishVerticalSpd.EnableWindow(FALSE);
		m_ctrlPublishOnGround.EnableWindow(FALSE);
		m_ctrlPublishFrequency.EnableWindow(FALSE);
		m_ctrlPublishSquawkCode.EnableWindow(FALSE);
		m_ctrlPublishIdent.EnableWindow(FALSE);*/

	}

	m_ctrlTime.EnableWindow(TRUE);
	m_ctrlState.EnableWindow(TRUE);
	m_ctrlCurrentDirective.EnableWindow(TRUE);
	m_ctrlPauseLength.EnableWindow(TRUE);
	UpdateData(FALSE);
	return 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to handle custom WM_PUBLISH_DATA messages sent to this
//!				window
//!
//!	Parameters:	\li wParam - not used
//!				\li lParam - not used
//!
//!	Returns:	Zero if handled
//------------------------------------------------------------------------------
//LONG CMuthurDlg::OnWMPublishData(WPARAM wParam, LPARAM lParam)
//{
//	//	CMLPubDataParams	params;
//	//	CMLPubDataParams	params1;
//	//	CMLPubDataParams	params2;
//
////	CMLListCtrlData*	pdvData = NULL;
////	//CMLListCtrlData*	pdvData1 = NULL;
////	//CMLListCtrlData*	pdvData2 = NULL;
////
////	if((m_bPublishEnabled == true) && (m_pdvPublishData != NULL))
////	{
////		UpdateData(TRUE);
////
////		if(m_pdvMetronSpawn != NULL)
////		{
////			pdvData = m_pdvMetronSpawn;
////			//pdvData1 = m_pdvMetronSpawn;
////			m_pdvMetronSpawn = NULL;
////		}
////		else
////		{
////			pdvData = m_pdvPublishData;
//////			pdvData1 = m_pdvPublishData;
////		}
////
////		pdvData->Randomize();
////		// Need to load these from a list or file (mimic reposition)
////		/*pdvData->SetPublishLatitude("32.894940");
////		pdvData->SetPublishLongitude("-97.000794");
////		pdvData->SetPublishAltitude("1050");
////		pdvData->SetPublishHeading(m_strPublishHeading);
////		pdvData->SetPublishPitch(m_strPublishPitch);
////		pdvData->SetPublishRoll(m_strPublishRoll);		
////		pdvData->SetPublishOnGround("1");
////		pdvData->SetPublishFreq(m_strPublishFrequency);
////		pdvData->SetPublishSquawkCode(m_strPublishSquawkCode);
////		pdvData->SetPublishIdent(m_strPublishIdent);*/
////
////		//	Should we adjust the aircraft identifers
////		if(m_strPublishACID.GetLength() > 0)
////		{
////			pdvData->GetDataObject()->SetACTailNumber((char*)((LPCSTR)m_strPublishACID));
////			pdvData->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID));
////		}
////
////	//	params.SetAmbassadorCallback(this);
////	//	params.SetDataObject(pdvData->GetDataObject());
////	//	//CMLHelper::SaveXmlStream(params, "C:\\test.xml");
////	//	CMLAmbassador::GetInstance().PublishData(&params);
////
////
////	//	// Publish second a/c
////	//	//if(m_pdvMetronSpawn != NULL)
////	//	//{
////	//	//	/*pdvData1 = m_pdvMetronSpawn;*/
////	//	//	m_pdvMetronSpawn = NULL;
////	//	//}
////	//	//else
////	//	//{
////	//	//	pdvData = m_pdvPublishData;
////	//	//}
////
////	//	pdvData1->Randomize();
////	//	CString m_strPublishACID1 = "UAL101";
////	//	m_strPublishACID = m_strPublishACID1;
////	//	// Need to load these from a list or file (mimic reposition)
////	//	/*pdvData1->SetPublishLatitude("32.890270");
////	//	pdvData1->SetPublishLongitude("-97.063276");
////	//	pdvData1->SetPublishAltitude("1050");
////	//	pdvData1->SetPublishHeading(m_strPublishHeading);
////	//	pdvData1->SetPublishPitch(m_strPublishPitch);
////	//	pdvData1->SetPublishRoll(m_strPublishRoll);		
////	//	pdvData1->SetPublishOnGround("1");
////	//	pdvData1->SetPublishFreq(m_strPublishFrequency);
////	//	pdvData1->SetPublishSquawkCode(m_strPublishSquawkCode);
////	//	pdvData1->SetPublishIdent(m_strPublishIdent);*/
////
////	//	//	Should we adjust the aircraft identifers
////	//	if(m_strPublishACID1.GetLength() > 0)
////	//	{
////	//		pdvData1->GetDataObject()->SetACTailNumber((char*)((LPCSTR)m_strPublishACID1));
////	//		pdvData1->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID1));
////	//	}
////
////	//	params1.SetAmbassadorCallback(this);
////	//	params1.SetDataObject(pdvData1->GetDataObject());
////
////	//	CMLAmbassador::GetInstance().PublishData(&params1);
////
////
////	//	//Publish third a/c
////	//	//params2.SetAmbassadorCallback(this);
////	//	//pdvData2->SetACID("AAL200");
////	//	//pdvData2->SetPublishAltitude("200");
////	//	//pdvData2->SetPublishCenter("MEM");
////	//	//pdvData2->SetPublishFreq("11900");
////	//	//pdvData2->SetPublishHeading("300");
////	//	//pdvData2->SetPublishIdent("0");
////	//	//pdvData2->SetPublishLatitude("36.133346");
////	//	//pdvData2->SetPublishLongitude("-86.659389");
////	//	//pdvData2->SetPublishOnGround("1");
////	//	//pdvData2->SetPublishPitch("50");
////	//	//pdvData2->SetPublishRoll("20");
////	//	//pdvData2->SetPublishSector("77");
////	//	//pdvData2->SetPublishSquawkCode("4444");
////	//	//params2.SetAmbassadorCallback(this);
////	//	//params2.SetDataObject(pdvData2->GetDataObject());
////	//	//CMLAmbassador::GetInstance().PublishData(&params2);
////
////
////
////	}
////
////	return 0;
//}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects a new FEM in the list
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnFEMChanged()
{
	int iIdx = -1;

	if((iIdx = m_ctrlFEMList.GetCurSel()) >= 0)
		m_pFedExecModel = (CMLFedExecModel*)(m_ctrlFEMList.GetItemData(iIdx));
	else
		m_pFedExecModel = NULL;

	m_ctrlJoin.EnableWindow((m_eMLState == MS_LISTED_FEMS) && (m_pFedExecModel != NULL));
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user double click an FEM in the list box
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnDblClkFEM()
{
	int					iIdx = -1;
	CMLFedExecModel*	pModel = NULL;

	if((iIdx = m_ctrlFEMList.GetCurSel()) >= 0)
	{
		if((pModel = (CMLFedExecModel*)m_ctrlFEMList.GetItemData(iIdx)) != NULL)
		{
			ShowModel(pModel);
		}

	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to display the values associated with a model
//!
//!	Parameters:	\li pModel - the model to be displayed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::ShowModel(CMLFedExecModel* pModel)
{
	CString	strMsg = "";
	CString	strTmp = "";
	const char*	pRequired = NULL;
	int		i = 1;

	ASSERT(pModel != NULL);

	strTmp.Format("Name : %s\n", pModel->GetName());
	strMsg += strTmp;

	strTmp.Format("Description : %s\n", pModel->GetDescription());
	strMsg += strTmp;

	strTmp.Format("Handle : %s\n\n", pModel->GetFedExecModelHandle().GetMuthurId());
	strMsg += strTmp;

	strTmp.Format("LogicalStartTime : %d\n", pModel->GetLogicalStartTime());
	strMsg += strTmp;

	strTmp.Format("DefaultDurationWithinStartupProtocolMSecs : %d\n", pModel->GetDefaultDurationWithinStartupProtocolMSecs());
	strMsg += strTmp;

	strTmp.Format("DurationFederationExecutionMSecs : %d\n", pModel->GetDurationFederationExecutionMSecs());
	strMsg += strTmp;

	strTmp.Format("DurationJoinFederationMSecs : %d\n", pModel->GetDurationJoinFederationMSecs());
	strMsg += strTmp;

	strTmp.Format("DurationRegisterPublicationMSecs : %d\n", pModel->GetDurationRegisterPublicationMSecs());
	strMsg += strTmp;

	strTmp.Format("DurationRegisterSubscriptionMSecs : %d\n", pModel->GetDurationRegisterSubscriptionMSecs());
	strMsg += strTmp;

	strTmp.Format("DurationRegisterToRunMSecs : %d\n", pModel->GetDurationRegisterToRunMSecs());
	strMsg += strTmp;

	strTmp.Format("DurationTimeToWaitAfterTermination : %d\n\n", pModel->GetDurationTimeToWaitAfterTermination());
	strMsg += strTmp;

	pRequired = pModel->GetFirstRequiredFederate();
	while(pRequired != NULL)
	{
		strTmp.Format("Required Federate %d : %s\n", i++, pRequired);
		strMsg += strTmp;

		pRequired = pModel->GetNextRequiredFederate();
	}

	MessageBox(strMsg, "Federation Execution Model", MB_OK | MB_ICONINFORMATION);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to handle te WM_DESTROY message
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnDestroy()
{
	if(m_dwRefreshViewsTimerId >  0)
	{
		KillTimer(m_dwRefreshViewsTimerId);
	}

	if(m_eMLState == MS_RUNNING)
	{
		OnTerminate();
	}

	CMLAmbassador::GetInstance().Shutdown();

	__super::OnDestroy();
}

void CMuthurDlg::FillClassList(CCheckListBox& rwndClasses, CStringArray* paClasses)
{
	char szClass[128];

	ASSERT(paClasses != NULL);

	memset(szClass, 0, sizeof(szClass));
	paClasses->RemoveAll();

	for(int i = 0; i < rwndClasses.GetCount(); i++)
	{
		if(rwndClasses.GetCheck(i) != 0)
		{
			rwndClasses.GetText(i, szClass);

			if(lstrlen(szClass) > 0)
			{
				paClasses->Add(szClass);
				memset(szClass, 0, sizeof(szClass));
			}
		}

	}

}

void CMuthurDlg::FillClassesListBox(CListBox& rwndClasses, CStringArray* paClasses)
{
	rwndClasses.ResetContent();

	for(int i = 0; i <= paClasses->GetUpperBound(); i++)
	{
		rwndClasses.AddString((*paClasses)[i]);
	}

}

void CMuthurDlg::FillClassesListBox(CComboBox& rwndClasses, CStringArray* paClasses)
{
	rwndClasses.ResetContent();

	for(int i = 0; i <= paClasses->GetUpperBound(); i++)
	{
		rwndClasses.AddString((*paClasses)[i]);
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to start the timer used to trigger data publications
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::StartPubTimer()
{
	StopPubTimer();

	UpdateData(TRUE); // Get the user specified period

	if(m_iPubRate < 10)
		m_iPubRate = 10; // limit user to 100 hz

	m_dwPublishTimerId = timeSetEvent(m_iPubRate, 5, PublishTimerCallback, (DWORD)((INT_PTR)m_hWnd), TIME_PERIODIC);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to stop the timer used to trigger data publications
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::StopPubTimer()
{
	if(m_dwPublishTimerId > 0)
	{
		timeKillEvent(m_dwPublishTimerId);
		m_dwPublishTimerId = 0;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to assign the appropriate data object to the data view
//!
//!	Parameters:	\li bPublish - true for the Publish view, false for Subscribe
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::SetDataViewData(bool bPublish)
{
	int					iIdx = -1;
	CString				strClassName = "";
	CMLListCtrlData*	pdvData = NULL;
	EMLDataObjectClass	eClass = ML_DATAOBJECT_CLASS_BASE;

	//m_ctrlSubscribeSelections.SetCurSel(0);
	//m_ctrlObjectTypeSelections.SetCurSel(0);
	//m_ctrlObjects.SetCurSel(0);
	//m_ctrlDataActions.SetCurSel(0);

	//m_ctrlAttributes.SetCurSel(0);

	if(bPublish)
	{
		if((iIdx = m_ctrlObjectTypeSelections.GetCurSel()) >= 0)
		{
			m_ctrlObjectTypeSelections.GetWindowText(strClassName);
		}
		else
		{
			m_ctrlObjectTypeSelections.SetCurSel(0);
			m_ctrlObjectTypeSelections.GetWindowText(strClassName);
		}
	}
	else
	{
		if((iIdx = m_ctrlSubscribeSelections.GetCurSel()) >= 0)
		{
			m_ctrlSubscribeSelections.GetWindowText(strClassName);
		}
		else
		{
			m_ctrlSubscribeSelections.SetCurSel(0);
			m_ctrlSubscribeSelections.GetWindowText(strClassName);
		}
	}	

	//	Translate the name of the class to it's type identifier
	if(strClassName.GetLength() > 0)
		eClass = CMLDataObject::GetTypeFromName((char*)((LPCSTR)strClassName), true);


	//	Now select the correct class member
	switch(eClass)
	{
	case ML_DATAOBJECT_CLASS_FL_POSITION:

		pdvData = (bPublish == true) ? &m_dvFlightPosition : &m_subFlightPosition;
		break;

	case ML_DATAOBJECT_CLASS_AIRCRAFT:

		pdvData = (bPublish == true) ? &m_dvSpawnAircraft : &m_subAircraft;
		break;

	case ML_DATAOBJECT_CLASS_AC_TAXI_IN:
		pdvData = (bPublish == true) ? &m_dvAcTaxiIn : &m_subAcTaxiIn;
		break;

	case ML_DATAOBJECT_CLASS_AC_TAXI_OUT:
		pdvData = (bPublish == true) ? &m_dvAcTaxiOut : &m_subAcTaxiOut;
		break;

	case ML_DATAOBJECT_CLASS_AC_ARRIVAL:
		pdvData = (bPublish == true) ? &m_dvAcArrival : &m_subAcArrival;
		break;

	case ML_DATAOBJECT_CLASS_AC_DEPT:
		pdvData = (bPublish == true) ? &m_dvAcDeparture : &m_subAcDeparture;
		break;

	case ML_DATAOBJECT_CLASS_FP_FILED:
		pdvData = (bPublish == true) ? &m_dvAcFlightPlan : &m_subFlightPlan;
		break;

	default:
		MessageBox("No class identified in SetDataView", "Warning", MB_OK);
		break;

	}// switch(eClass)

	if(!pdvData/* != NULL*/)
		pdvData->Update(NULL);

	if(bPublish)
		m_pdvPublishData = pdvData;
	else 
		m_pdvSubscribeData = pdvData;	
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user clicks on the Subscribe pushbutton
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnSubscribeEnabled()
{
	if(m_pdvSubscribeData != NULL)
	{
		m_bSubscribeEnabled = (m_ctrlSubscribeEnabled.GetCheck() != 0);
	}
	else
	{
		m_bSubscribeEnabled = false;
	}	
	if(m_bSubscribeEnabled == true)
	{	

		/*m_strSubscribeLatitude = "36.122527";
		m_strSubscribeLongitude = "-86.670837";
		m_strSubscribeAltitude = "608";
		m_strSubscribeHeading = "-48.349112";
		m_strSubscribePitch = "";
		m_strSubscribeRoll = "";
		m_strSubscribeVerticalSpd = "";
		m_strSubscribeOnGround = "";
		m_strSubscribeFrequency = "";
		m_strSubscribeSquawkCode = "";
		m_strSubscribeIdent = "";*/
	}
	else
	{
		//m_pdvMetronSpawn = NULL;
		//StopPubTimer();
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called by OnApplyDataAction to add a new object
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::DeleteObject()
{
	USES_CONVERSION;
	CMLDeleteObjectParams params1;
	CMLListCtrlData*	pdvData1 = NULL;

	params1.SetFedRegHandle(m_hFRH);
	params1.SetFedExecHandle(m_hFEH);
	params1.SetAmbassadorCallback(this);


	if(m_pdvPublishData)
	{
		UpdateData(TRUE);

		if(m_pdvMetronSpawn != NULL)
		{
			pdvData1 = m_pdvMetronSpawn;
			m_pdvMetronSpawn = NULL;
		}
		else
		{
			pdvData1 = m_pdvPublishData;
		}

		//pdvData1->Randomize();

		//	Should we adjust the aircraft identifers
		if(m_strPublishACID.GetLength() > 0)
		{
			//pdvData1->GetDataObject()->SetACTailNumber((char*)((LPCSTR)m_strPublishACID));
			//pdvData1->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID));
		}
	}
	// Need to set this UUID
	for(int i = 0; i < m_ObjectsTypes.GetSize(); i++)
	{
		CString temp = typeid((*pdvData1->GetDataObject())).name();
		m_ctrlPublishACID.GetWindowText(m_strPublishACID);
		if(CString(OLE2A(m_ObjectsTypes.GetValueAt(i))).CompareNoCase(typeid((*pdvData1->GetDataObject())).name()) == 0)
		{
			for(int x = 0; x < m_Objects.GetSize(); x++)
			{
				if(m_strPublishACID && CString(OLE2A(m_ObjectsTypes.GetKeyAt(i))).CompareNoCase(CString(OLE2A(m_Objects.GetKeyAt(x)))) == 0
					&& CString(OLE2A(m_Objects.GetValueAt(x))).CompareNoCase(m_strPublishACID) == 0)
				{
					if(pdvData1->GetDataObject()) 
					{
						params1.SetDataObjUUID(OLE2A(m_Objects.GetKeyAt(i)));
						pdvData1->GetDataObject()->SetACId(OLE2A(m_Objects.GetValueAt(i)));
						pdvData1->GetDataObject()->SetDataObjectUUID(OLE2A(m_Objects.GetKeyAt(i)));
					}
				}				
			}
		}
	}
	//params1.SetDataObjUUID(pdvData1->GetDataObject()->GetDataObjectUUID());		

	// Check that we have already created the object and that we own it before trying to delete it.
	if(pdvData1->GetDataObject() && pdvData1->GetOwnership() && m_Objects.FindKey(pdvData1->GetDataObject()->GetDataObjectUUID()) >= 0
		&& params1.GetDataObjUUID())
	{
		CMLAmbassador::GetInstance().DeleteObject(&params1);
		//m_pdvPublishData->Update(pdvData1->GetDataObject());

		if(m_bPublishEnabled && m_pdvPublishData && 
			(m_strPublishACID.CompareNoCase(pdvData1->GetDataObject()->GetACId()) == 0)
			&& (pdvData1->GetDataObject()->GetDataObjectType() == m_pdvPublishData->GetClass()))
			m_pdvPublishData->Update(NULL);
	}
	else
	{
		MessageBox("You don't own this object or invalid UUID...not deleting object!", "Warning", MB_OK);
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called by OnApplyDataAction to update an object
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::UpdateObject()
{
	USES_CONVERSION;
	CMLUpdateObjectParams params1;
	CMLListCtrlData*	pdvData1 = NULL;

	params1.SetFedRegHandle(m_hFRH);
	params1.SetFedExecHandle(m_hFEH);
	params1.SetAmbassadorCallback(this);


	if(m_bPublishEnabled && m_pdvPublishData)
	{
		UpdateData(TRUE);

		if(m_pdvMetronSpawn != NULL)
		{
			pdvData1 = m_pdvMetronSpawn;
			m_pdvMetronSpawn = NULL;
		}
		else
		{
			pdvData1 = m_pdvPublishData;
		}

		pdvData1->Randomize();

		//	Should we adjust the aircraft identifers
		if(m_strPublishACID.GetLength() > 0)
		{
			pdvData1->GetDataObject()->SetACTailNumber("N1234");
			/*pdvData1->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID));*/
		}
	}

	// Need to set this UUID
	for(int i = 0; i < m_ObjectsTypes.GetSize(); i++)
	{
		if(CString(OLE2A(m_ObjectsTypes.GetValueAt(i))).CompareNoCase(typeid((*pdvData1->GetDataObject())).name()) == 0)
		{
			m_ctrlPublishACID.GetWindowText(m_strPublishACID);

			for(int x = 0; x < m_Objects.GetSize(); x++)
			{
				if(m_strPublishACID && CString(OLE2A(m_ObjectsTypes.GetKeyAt(i))).CompareNoCase(CString(OLE2A(m_Objects.GetKeyAt(x)))) == 0
					&& CString(OLE2A(m_Objects.GetValueAt(x))).CompareNoCase(m_strPublishACID) == 0)
				{
					if(pdvData1->GetDataObject()) 
					{
						pdvData1->GetDataObject()->SetACId(OLE2A(m_Objects.GetValueAt(i)));
						pdvData1->GetDataObject()->SetDataObjectUUID(OLE2A(m_Objects.GetKeyAt(i)));
					}
				}				
			}
		}
		if(pdvData1->GetDataObject()->GetDataObjectType() == ML_DATAOBJECT_CLASS_FL_POSITION)
		{
			((CMLFlightPosition*)pdvData1->GetDataObject())->SetGroundspeedKts(m_dGroundSpeedCounter);
			m_dGroundSpeedCounter++;
		}
	}


	/*if(pdvData1->GetDataObject()) 
	pdvData1->GetDataObject()->SetDataObjectUUID(pszUUID);*/


	params1.SetDataObject(pdvData1->GetDataObject());		

	// Check that we have already created the object and own it before trying to update it.
	if(pdvData1->GetDataObject() /*&& pdvData1->GetOwnership()*/ && m_Objects.FindVal(pdvData1->GetDataObject()->GetACId() >= 0)
		&& m_Objects.FindKey(pdvData1->GetDataObject()->GetDataObjectUUID()) >= 0 && 
		pdvData1->GetDataObject()->GetDataObjectUUID())
	{
		CMLAmbassador::GetInstance().UpdateObject(&params1);

		if(m_bPublishEnabled && m_pdvPublishData && params1.GetDataObject() && 
			(m_strPublishACID.CompareNoCase(params1.GetDataObject()->GetACId()) == 0) &&
			(params1.GetDataObject()->GetDataObjectType() == m_pdvPublishData->GetClass()))
			m_pdvPublishData->Update(params1.GetDataObject());
	}
	else
	{
		MessageBox("You don't own this object...not applying updates!", "Warning", MB_OK);
	}
	/*}
	else
	{
	MessageBox("Failed to update object...object is NULL!", "Warning", MB_OK);
	}
	}
	else
	{
	MessageBox("Invalid object...invalid UUID for this data object!", "Warning", MB_OK);
	}
	}
	else
	{
	MessageBox("Invalid object...callsign doesn't exist!", "Warning", MB_OK);
	}*/
}

//------------------------------------------------------------------------------
//!	Summary:	Called by OnApplyDataAction to add a new object
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::AddObject()
{

	CMLCreateObjectParams params1;
	CMLListCtrlData*	pdvData1 = NULL;

	params1.SetFedRegHandle(m_hFRH);
	params1.SetFedExecHandle(m_hFEH);
	params1.SetAmbassadorCallback(this);	

	if(m_bPublishEnabled && m_pdvPublishData)
	{
		UpdateData(TRUE);

		if(m_pdvMetronSpawn != NULL)
		{
			pdvData1 = m_pdvMetronSpawn;
			m_pdvMetronSpawn = NULL;
		}
		else
		{
			pdvData1 = m_pdvPublishData;
		}

		pdvData1->Randomize();

		pdvData1->SetOwnership(true); // Object is owned by creating federate automatically

		//	Should we adjust the aircraft identifers
		if(m_strPublishACID.GetLength() > 0)
		{
			//pdvData1->GetDataObject()->SetACTailNumber((char*)((LPCSTR)m_strPublishACID));
			pdvData1->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID));
		}
		char pszUUID[ML_MAXLEN_UUID];
		memset(pszUUID, 0, sizeof(pszUUID));
		CMLAmbassador::CreateUUID(pszUUID, sizeof(pszUUID));

		if(pdvData1->GetDataObject()) pdvData1->GetDataObject()->SetDataObjectUUID(pszUUID);

		if(pdvData1->GetDataObject()->GetDataObjectType() == ML_DATAOBJECT_CLASS_FP_FILED)
		{
			 ((CMLFlightPlan*)pdvData1->GetDataObject())->SetPlannedDepartureTimeMSecs(m_dStartSimTime + 30000);
			//	((unsigned long)1/(unsigned long)60000));

			((CMLFlightPlan*)pdvData1->GetDataObject())->SetPlannedArrivalTimeMSecs(m_dStartSimTime + 7200000);

			((CMLFlightPlan*)pdvData1->GetDataObject())->SetObjectCreatedTimestamp(m_dCurrentSimTime);
				//((unsigned long)60/(unsigned long)60000));
			
		//	CMLAmbassador::GetInstance().CreateObject(&params1);
		}
		else if(pdvData1->GetDataObject()->GetDataObjectType() == ML_DATAOBJECT_CLASS_AC_ARRIVAL)
		{
			((CMLAircraftArrivalData*)pdvData1->GetDataObject())->SetActualArrivalTimeMSecs(m_dCurrentSimTime);
			((CMLAircraftArrivalData*)pdvData1->GetDataObject())->SetObjectCreatedTimestamp(m_dCurrentSimTime);
		}
		else if(pdvData1->GetDataObject()->GetDataObjectType() == ML_DATAOBJECT_CLASS_AC_DEPT)
		{
			((CMLAircraftDepartureData*)pdvData1->GetDataObject())->SetActualDepartureTimeMSecs(m_dCurrentSimTime);
			((CMLAircraftDepartureData*)pdvData1->GetDataObject())->SetObjectCreatedTimestamp(m_dCurrentSimTime);
		}
		else if(pdvData1->GetDataObject()->GetDataObjectType() == ML_DATAOBJECT_CLASS_AC_TAXI_IN)
		{
			((CMLAircraftTaxiIn*)pdvData1->GetDataObject())->SetTaxiInTimeMSecs(m_dCurrentSimTime);
			((CMLAircraftTaxiIn*)pdvData1->GetDataObject())->SetObjectCreatedTimestamp(m_dCurrentSimTime);
		}
		else if(pdvData1->GetDataObject()->GetDataObjectType() == ML_DATAOBJECT_CLASS_AC_TAXI_OUT)
		{
			((CMLAircraftTaxiOut*)pdvData1->GetDataObject())->SetTaxiOutTimeMSecs(m_dCurrentSimTime);
			((CMLAircraftTaxiOut*)pdvData1->GetDataObject())->SetObjectCreatedTimestamp(m_dCurrentSimTime);
		}
	}
	params1.SetDataObject(pdvData1->GetDataObject());		
	//m_pdvPublishData = pdvData1;
	/*m_pdvMetronSpawn = &m_subAircraft;*/
	if(pdvData1->GetDataObject() && m_Objects.FindKey(pdvData1->GetDataObject()->GetDataObjectUUID()) < 0) 
	{
		m_Objects.Add(pdvData1->GetDataObject()->GetDataObjectUUID(), pdvData1->GetDataObject()->GetACId());
		m_ObjectsTypes.Add(pdvData1->GetDataObject()->GetDataObjectUUID(), typeid(*(pdvData1->GetDataObject())).name());

		CMLAmbassador::GetInstance().CreateObject(&params1);
		
		
		if(m_bPublishEnabled && m_pdvPublishData && params1.GetDataObject() && 
			m_pdvPublishData->GetClass() == pdvData1->GetDataObject()->GetDataObjectType() && 
			(m_strPublishACID.CompareNoCase(params1.GetDataObject()->GetACId()) == 0))
		{
			m_pdvPublishData->Update(params1.GetDataObject());
		}
	}
	else
	{
		MessageBox("Object with this call sign already exists...chose another call sign!", "Warning", MB_OK);
	}


}

//------------------------------------------------------------------------------
//!	Summary:	Called to request that ownership of an object be transferred to this 
//! federate.
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::TransferObject()
{
	USES_CONVERSION;
	CMLTransferObjectOwnershipParams params1;
	CMLListCtrlData*	pdvData1 = NULL;

	params1.SetFedRegHandle(m_hFRH);
	params1.SetFedExecHandle(m_hFEH);
	params1.SetAmbassadorCallback(this);


	if(m_pdvPublishData)
	{
		UpdateData(TRUE);

		if(m_pdvMetronSpawn != NULL)
		{
			pdvData1 = m_pdvMetronSpawn;
			m_pdvMetronSpawn = NULL;
		}
		else
		{
			pdvData1 = m_pdvPublishData;
		}

		pdvData1->Randomize();

		//	Should we adjust the aircraft identifers
		if(m_strPublishACID.GetLength() > 0)
		{
			//pdvData1->GetDataObject()->SetACTailNumber((char*)((LPCSTR)m_strPublishACID));
			//pdvData1->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID));
		}
	}
	// Need to set this UUID
	for(int i = 0; i < m_ObjectsTypes.GetSize(); i++)
	{
		if(CString(OLE2A(m_ObjectsTypes.GetValueAt(i))).CompareNoCase(typeid((*pdvData1->GetDataObject())).name()) == 0)
		{
			m_ctrlPublishACID.GetWindowText(m_strPublishACID);
			for(int x = 0; x < m_Objects.GetSize(); x++)
			{
				if(m_strPublishACID && CString(OLE2A(m_ObjectsTypes.GetKeyAt(i))).CompareNoCase(CString(OLE2A(m_Objects.GetKeyAt(x)))) == 0
					&& CString(OLE2A(m_Objects.GetValueAt(x))).CompareNoCase(m_strPublishACID) == 0)
				{
					if(pdvData1->GetDataObject()) 
					{
						params1.SetDataObjectUUID(OLE2A(m_Objects.GetKeyAt(i)));
						pdvData1->GetDataObject()->SetACId(OLE2A(m_Objects.GetValueAt(i)));
						pdvData1->GetDataObject()->SetDataObjectUUID(OLE2A(m_Objects.GetKeyAt(i)));
					}
				}				
			}
		}
	}	

	// Check that the object exists and we do NOT own it before trying to request ownership.
	if(pdvData1->GetDataObject() && !pdvData1->GetOwnership() &&  m_Objects.FindKey(pdvData1->GetDataObject()->GetDataObjectUUID()) >= 0)
		CMLAmbassador::GetInstance().TransferObject(&params1);
	else
		MessageBox("You already own this object...not requesting transfer!", "Warning", MB_OK);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to request that ownership of an object be relinquished
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::RelinquishObject()
{
	USES_CONVERSION;
	CMLRelinquishObjectOwnershipRequest params1;
	CMLListCtrlData*	pdvData1 = NULL;

	params1.SetFedRegHandle(m_hFRH);
	params1.SetFedExecHandle(m_hFEH);
	params1.SetAmbassadorCallback(this);


	if(m_bPublishEnabled && m_pdvPublishData)
	{
		UpdateData(TRUE);

		if(m_pdvMetronSpawn != NULL)
		{
			pdvData1 = m_pdvMetronSpawn;
			m_pdvMetronSpawn = NULL;
		}
		else
		{
			pdvData1 = m_pdvPublishData;
		}

		//pdvData1->Randomize();

		//	Should we adjust the aircraft identifers
		if(m_strPublishACID.GetLength() > 0)
		{
			//pdvData1->GetDataObject()->SetACTailNumber((char*)((LPCSTR)m_strPublishACID));
			//pdvData1->GetDataObject()->SetACId((char*)((LPCSTR)m_strPublishACID));
		}
	}
	// Need to set this UUID
	for(int i = 0; i < m_ObjectsTypes.GetSize(); i++)
	{
		if(CString(OLE2A(m_ObjectsTypes.GetValueAt(i))).CompareNoCase(typeid((*pdvData1->GetDataObject())).name()) == 0)
		{
			m_ctrlPublishACID.GetWindowText(m_strPublishACID);
			for(int x = 0; x < m_Objects.GetSize(); x++)
			{
				if(m_strPublishACID && CString(OLE2A(m_ObjectsTypes.GetKeyAt(i))).CompareNoCase(CString(OLE2A(m_Objects.GetKeyAt(x)))) == 0
					&& CString(OLE2A(m_Objects.GetValueAt(x))).CompareNoCase(m_strPublishACID) == 0)
				{
					if(pdvData1->GetDataObject()) 
					{
						params1.SetDataObjectUUID(OLE2A(m_Objects.GetKeyAt(i)));
						pdvData1->GetDataObject()->SetACId(OLE2A(m_Objects.GetValueAt(i)));
						pdvData1->GetDataObject()->SetDataObjectUUID(OLE2A(m_Objects.GetKeyAt(i)));
					}
				}				
			}
		}
	}		


	// Check that we have already created the object and own it before trying to relinquish it.
	if(pdvData1->GetDataObject() &&/* pdvData1->GetOwnership() &&*/ m_Objects.FindKey(pdvData1->GetDataObject()->GetDataObjectUUID()) >= 0
		&& params1.GetDataObjectUUID())
	{
		CMLAmbassador::GetInstance().RelinquishObject(&params1);
	}
	else
	{
		MessageBox("You don't own this object so it can't be relinquished!", "Warning", MB_OK);
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user clicks on the Apply Data Action Button
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnApplyDataAction()
{
	int	iIdx = -1;
	CString	strDataAction = "";

	/*if(m_pdvPublishData != NULL)
	{*/
	m_bPublishEnabled = true; //(m_ctrlPublishEnabled.GetCheck() != 0);
	/*}
	else
	{
	m_bPublishEnabled = false;
	}*/

	//m_pdvMetronSpawn = &m_subAircraft;

	if((iIdx = m_ctrlDataActions.GetCurSel()) >= 0)
		m_ctrlDataActions.GetWindowText(strDataAction);


	if(strDataAction.CompareNoCase("Add") == 0)			AddObject();
	else if(strDataAction.CompareNoCase("Update") == 0)	UpdateObject();
	else if(strDataAction.CompareNoCase("Delete") == 0)	DeleteObject();
	else if(strDataAction.CompareNoCase("Transfer Ownership") == 0)	TransferObject();
	else if(strDataAction.CompareNoCase("Relinquish Ownership") == 0)	RelinquishObject();
}


//------------------------------------------------------------------------------
//!	Summary:	Called when the user clicks on the Apply Directive
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnApplyDirective()
{
	int	iIdx = -1;
	CString				strDirective = "";

	if((iIdx = m_ctrlDirective.GetCurSel()) >= 0)
		m_ctrlDirective.GetWindowText(strDirective);


	if(strcmp(strDirective, "Start") == 0)
	{

		CMLStartFederationParams params;

		params.SetFedRegHandle(m_hFRH);
		params.SetFedExecHandle(m_hFEH);
		params.SetAmbassadorCallback(this);

		CMLAmbassador::GetInstance().StartFederation(&params);
	}
	else if(strcmp(strDirective, "Pause") == 0)
	{
		CMLPauseFederationParams params;

		params.SetFedRegHandle(m_hFRH);
		params.SetFedExecHandle(m_hFEH);
		params.SetAmbassadorCallback(this);		

		if(lstrlen(m_strPauseLength) > 0)
			params.SetLengthOfPause(atol(m_strPauseLength));

		CMLAmbassador::GetInstance().PauseFederation(&params);
	}
	else if(strcmp(strDirective, "Resume") == 0)
	{
		CMLResumeFederationParams params;

		params.SetFedRegHandle(m_hFRH);
		params.SetFedExecHandle(m_hFEH);
		params.SetAmbassadorCallback(this);

		CMLAmbassador::GetInstance().ResumeFederation(&params);
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user clicks on the Publish pushbutton
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
//void CMuthurDlg::OnPublishEnabled()
//{
//	if(m_pdvPublishData != NULL)
//	{
//		m_bPublishEnabled = (m_ctrlPublishEnabled.GetCheck() != 0);
//	}
//	else
//	{
//		m_bPublishEnabled = false;
//	}
//
//	if(m_bPublishEnabled == true)
//	{
//		//m_subAircraft.SetAircraftType("CRJ7");
//		switch(rand() % 4)
//		{
//		case 0: m_subAircraft.SetAircraftType("B737");
//			break;
//		case 1: m_subAircraft.SetAircraftType("B768");
//			break;
//		case 2: m_subAircraft.SetAircraftType("C172");
//			break;
//		case 3: m_subAircraft.SetAircraftType("B737");
//			break;
//		default: m_subAircraft.SetAircraftType("CRJ7");
//			break;
//		}
//		m_pdvMetronSpawn = &m_subAircraft;
//
//		//m_strPublishLatitude = "36.122527";
//		//m_strPublishLongitude = "-86.670837";
//		//m_strPublishAltitude = "608";
//		//m_strPublishHeading = "-48.349112";
//		//m_strPublishPitch = "";
//		//m_strPublishRoll = "";
//		//m_strPublishVerticalSpd = "250";
//		//m_strPublishOnGround = "true";
//		//m_strPublishFrequency = "1250";
//		//m_strPublishSquawkCode = "1234";
//		//m_strPublishIdent = "false";
//		StartPubTimer();
//	}
//	else
//	{
//		m_pdvMetronSpawn = NULL;
//		StopPubTimer();
//	}
//
//}

//------------------------------------------------------------------------------
//!	Summary:	Called to handle WM_TIMER messages sent to this window
//!
//!	Parameters:	\li uEventId - the timer event identifier
//!
//!	Returns:	Zero if handled
//------------------------------------------------------------------------------
void CMuthurDlg::OnTimer(UINT uEventId)
{
	// Should we refresh the publish data view?
	if(m_bPublishEnabled  && m_pdvPublishData && m_pdvPublishData->GetOwnership())
	{		
		//m_ctrlPublicationData.RedrawWindow(); // this causes memory leaks
		for(int i = 0; i < m_ctrlPublicationData.GetItemCount(); i++)
			m_ctrlPublicationData.Redraw(i);
	}

	// Should we refresh the subscribe data view?
	if(m_bSubscribeEnabled && m_pdvSubscribeData && !m_pdvSubscribeData->GetOwnership())
	{
		//MessageBox("Updating subscription data view", "Debug", MB_OK);
		//m_ctrlSubscriptionData.RedrawWindow(); // this causes memory leaks
		for(int i = 0; i < m_ctrlSubscriptionData.GetItemCount(); i++)
			m_ctrlSubscriptionData.Redraw(i);
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects a new subscription class
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnSubscribeSelChanged()
{
	OnWMUpdateCtrls(0,0);
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects a new directive class
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnDirectiveSelChanged()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects a new data action
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnDataActionSelChanged()
{
	//OnWMUpdateCtrls(0,0);
	//OnWMUpdateCtrls(0,0);
	//m_ctrlSubscribeSelections.SetCurSel(0);
	//m_ctrlObjectTypeSelections.SetCurSel(0);
	//m_ctrlObjects.SetCurSel(0);
	//m_ctrlDataActions.SetCurSel(0);
	//OnWMUpdateCtrls(0,0);
	////m_ctrlObjectTypeSelections.SetCurSel(0);

	//if(m_eMLState == MS_RUNNING)
	//{
	//	// Get action selected by user
	//	CString selDataAction = "";
	//	int iIdx = -1;
	//	if((iIdx = m_ctrlDataActions.GetCurSel()) >= 0)
	//		m_ctrlDataActions.GetWindowText(selDataAction);

	//	if(selDataAction)
	//	{
	//		if(selDataAction.CompareNoCase("Add") == 0)
	//		{
	//			m_strPublishACID = "";
	//			m_strAttributeValue = "";
	//			m_ctrlObjects.EnableWindow(FALSE);
	//			//m_ctrlAttributes.EnableWindow(FALSE);
	//			m_ctrlObjectTypeSelections.EnableWindow(TRUE);
	//			//if(m_ctrlObjectTypeSelections.GetCount() > 0) m_ctrlObjectTypeSelections.SetCurSel(0);
	//		}
	//		else if(selDataAction.CompareNoCase("Update") == 0)
	//		{
	//			m_strPublishACID = "";
	//			m_strAttributeValue = "";
	//			m_ctrlObjects.EnableWindow(TRUE);
	//			//m_ctrlAttributes.EnableWindow(TRUE);
	//			m_ctrlObjectTypeSelections.EnableWindow(TRUE);
	//			//if(m_ctrlObjectTypeSelections.GetCount() > 0) m_ctrlObjectTypeSelections.SetCurSel(0);
	//			//if(m_ctrlObjects.GetCount() > 0) m_ctrlObjects.SetCurSel(0);
	//			//if(m_ctrlAttributes.GetCount() > 0) m_ctrlAttributes.SetCurSel(0);
	//		}
	//		else if(selDataAction.CompareNoCase("Delete") == 0 || 
	//			selDataAction.CompareNoCase("Relinquish Ownership") == 0 ||
	//			selDataAction.CompareNoCase("Transfer Ownership") == 0)
	//		{
	//			m_strPublishACID = "";
	//			m_strAttributeValue = "";
	//			m_ctrlObjects.EnableWindow(TRUE);
	//			//m_ctrlAttributes.EnableWindow(FALSE);
	//			m_ctrlObjectTypeSelections.EnableWindow(FALSE);
	//			//if(m_ctrlObjectTypeSelections.GetCount() > 0) m_ctrlObjectTypeSelections.SetCurSel(0);
	//			//if(m_ctrlObjects.GetCount() > 0) m_ctrlObjects.SetCurSel(0);
	//			//if(m_ctrlAttributes.GetCount() > 0) m_ctrlAttributes.SetCurSel(0);
	//		}			
	//	}
	//}
	//else
	//{
	//	m_strPublishACID = "";
	//	m_strAttributeValue = "";
	//	m_ctrlPublishACID.EnableWindow(FALSE);
	//	m_ctrlAttributeValue.EnableWindow(FALSE);
	//	m_ctrlObjects.EnableWindow(FALSE);
	//	//m_ctrlAttributes.EnableWindow(FALSE);
	//	m_ctrlObjectTypeSelections.EnableWindow(FALSE);
	//	//if(m_ctrlObjectTypeSelections.GetCount() > 0) m_ctrlObjectTypeSelections.SetCurSel(0);
	//	//if(m_ctrlObjects.GetCount() > 0) m_ctrlObjects.SetCurSel(0);
	//	//if(m_ctrlAttributes.GetCount() > 0) m_ctrlAttributes.SetCurSel(0);		
	//}
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects an object
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnObjectSelChanged()
{
	OnWMUpdateCtrls(0,0);

	//USES_CONVERSION;
	//// Get selected call sign
	//// Populate Data View accordingly
	//// Set Call Sign field
	//// Set Attributes
	//if(m_eMLState == MS_RUNNING)
	//{
	//	// Get action selected by user
	//	CString selDataAction = "";
	//	int iIdx = -1;
	//	if((iIdx = m_ctrlDataActions.GetCurSel()) >= 0)
	//		m_ctrlDataActions.GetWindowText(selDataAction);

	//	if(selDataAction.CompareNoCase("Add") == 0)
	//	{
	//		MessageBox("Can't Add existing object!", "WARNING", MB_OK);
	//	}
	//	else
	//	{
	//		int iIdx = -1;
	//		if((iIdx = m_ctrlObjects.GetCurSel()) >= 0)
	//			m_ctrlObjects.GetWindowText(m_strPublishACID);

	//		//iIdx = -1;
	//		//CString selObjClass = "";
	//		//int index = m_ObjectsTypes.FindKey(A2OLE(m_strPublishACID.GetBuffer()));
	//		//if(index > -1) selObjClass = m_ObjectsTypes.GetValueAt(index);
	//		////m_ctrlObjectTypeSelections.SetWindowTextA(selObjClass);

	//		//CMLPtrList ptrList = CMLPtrList();
	//		//ptrList = CMLHelper::GetObjectAttributes(selObjClass);

	//		//PTRNODE ptrPos = ptrList.GetHeadPosition();
	//		////m_ctrlAttributes.Clear();
	//		//while(ptrPos != NULL)
	//		//{
	//		//	CMLString pAttribute = "";
	//		//	if((pAttribute = (CMLString*)(ptrList.GetNext(ptrPos)) ?
	//		//		*((CMLString*)(ptrList.GetNext(ptrPos))) : NULL) != NULL)
	//		//	{
	//		//		int iIdx = m_ctrlAttributes.AddString(pAttribute);
	//		//		if(iIdx >= 0) m_ctrlAttributes.SetItemData(iIdx, (INT_PTR)(&pAttribute));
	//		//	}
	//		//}
	//	}
	//}
	//else
	//{
	//	m_strPublishACID = "";
	//	m_strAttributeValue = "";
	//	m_ctrlPublishACID.EnableWindow(FALSE);
	//	m_ctrlAttributeValue.EnableWindow(FALSE);
	//	m_ctrlObjects.EnableWindow(FALSE);
	//	//m_ctrlAttributes.EnableWindow(FALSE);
	//	m_ctrlObjectTypeSelections.EnableWindow(FALSE);
	//	//if(m_ctrlObjectTypeSelections.GetCount() > 0) m_ctrlObjectTypeSelections.SetCurSel(0);
	//	//if(m_ctrlObjects.GetCount() > 0) m_ctrlObjects.SetCurSel(0);
	//	//if(m_ctrlAttributes.GetCount() > 0) m_ctrlAttributes.SetCurSel(0);		
	//}
}

//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects a new publication class
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnObjectTypeSelChanged()
{

	//OnWMUpdateCtrls(0,0);
}


//------------------------------------------------------------------------------
//!	Summary:	Called when the user selects an attribute
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMuthurDlg::OnAttributeSelChanged()
{
	//USES_CONVERSION;
	//// Get selected call sign
	//// Populate Data View accordingly
	//// Set Call Sign field
	//// Set Attributes
	//if(m_eMLState == MS_RUNNING)
	//{
	//	// Get action selected by user
	//	CString selDataAction = "";
	//	int iIdx = -1;
	//	if((iIdx = m_ctrlDataActions.GetCurSel()) >= 0)
	//		m_ctrlDataActions.GetWindowText(selDataAction);

	//	if(selDataAction.CompareNoCase("Add") == 0)
	//	{
	//		MessageBox("Can't Add existing object!", "WARNING", MB_OK);
	//	}
	//	else
	//	{
	//		int iIdx = -1;
	//		if((iIdx = m_ctrlObjects.GetCurSel()) >= 0)
	//			m_ctrlObjects.GetWindowText(m_strPublishACID);

	//		iIdx = -1;
	//		CString selObjClass = "";
	//		int index = m_ObjectsTypes.FindKey(A2OLE(m_strPublishACID.GetBuffer()));
	//		if(index > -1) selObjClass = m_ObjectsTypes.GetValueAt(index);
	//		m_strAttributeValue = "";

	//		//m_ctrlObjectTypeSelections.SetWindowTextA(selObjClass);

	//		/*CMLPtrList ptrList = CMLPtrList();
	//		ptrList = CMLHelper::GetObjectAttributes(selObjClass);

	//		PTRNODE ptrPos = ptrList.GetHeadPosition();
	//		m_ctrlAttributes.Clear();
	//		while(ptrPos != NULL)
	//		{
	//			CMLString pAttribute = "";
	//			if((pAttribute = ((CMLString*)(ptrList.GetNext(ptrPos)) ?
	//				*((CMLString*)(ptrList.GetNext(ptrPos))) : NULL)) != NULL)
	//			{
	//				CString selAttr = "";
	//				selAttr = 

	//			}
	//		}*/



	//	}


	//}
	//else
	//{
	//	m_strPublishACID = "";
	//	m_strAttributeValue = "";
	//	m_ctrlPublishACID.EnableWindow(FALSE);
	//	m_ctrlAttributeValue.EnableWindow(FALSE);
	//	m_ctrlObjects.EnableWindow(FALSE);
	//	//m_ctrlAttributes.EnableWindow(FALSE);
	//	m_ctrlObjectTypeSelections.EnableWindow(FALSE);
	//	//if(m_ctrlObjectTypeSelections.GetCount() > 0) m_ctrlObjectTypeSelections.SetCurSel(0);
	//	//if(m_ctrlObjects.GetCount() > 0) m_ctrlObjects.SetCurSel(0);
	//	//if(m_ctrlAttributes.GetCount() > 0) m_ctrlAttributes.SetCurSel(0);		
	//}
}
