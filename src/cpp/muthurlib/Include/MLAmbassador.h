//------------------------------------------------------------------------------
/*! \file	MLAmbassador.h
//
//  Contains declaration of the CMLAmbassador class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_AMBASSADOR_H__)
#define __ML_AMBASSADOR_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>

#include <MLSerialize.h>
#include <MLSafePtrList.h>
#include <MLObjectEventCallback.h>
#include <MLTimeManagementEventCallback.h>
#include <MLObjectOwnershipRelinquishCallback.h>

#include <MLRegisterParams.h>
#include <MLListFEMParams.h>
#include <MLJoinFedParams.h>
#include <MLAddDataSubParams.h>
#include <MLReadyParams.h>
#include <MLCreateObjectParams.h>
#include <MLUpdateObjectParams.h>
#include <MLDeleteObjectParams.h>

#include <MLTerminateParams.h>
#include <MLStartFederationRequest.h>
#include <MLPauseFederationRequest.h>
#include <MLResumeFederationRequest.h>
#include <MLTransferObjectOwnershipRequest.h>
#include <MLRelinquishObjectOwnershipRequest.h>

#include <MLRegisterResponse.h>
#include <MLListFEMResponse.h>
#include <MLJoinFedResponse.h>
#include <MLAddDataSubResponse.h>
#include <MLCreateObjectResponse.h>
#include <MLUpdateObjectResponse.h>
#include <MLDeleteObjectResponse.h>
#include <MLRelinquishObjectOwnershipResponse.h>
#include <MLTransferObjectOwnershipResponse.h>
#include <MLReadyResponse.h>

#include <MLStartFederationResponse.h>
#include <MLPauseFederationResponse.h>
#include <MLResumeFederationResponse.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define ML_AMBASSADOR_SYSTEM_TIMEOUT_MSECS 30000					//!< Represents the maximum amount of time (msecs) for the system to continue running without receiving a heartbeat message from Muthur
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
class CMQWrapper; //<! Forward declaration of ActiveMQ wrapper

//------------------------------------------------------------------------------
//! \brief Class used to manage requests from the client application
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAmbassador
{
	private:
	
		
		CMQWrapper*				m_pMQWrapper;													//!< The wrapper for the ActiveMQ services
		unsigned int			m_uCheckPendingTimerID;											//!< ID of timer event for pending message triage
		CMLSafePtrList			m_pendingRequests;												//!< The collection of pending requests
		char					m_szMuthurHeartbeatQueueName[ML_MAXLEN_MUTHUR_HB_QUEUE_NAME];	//!< Name of muthur heartbeat queue
		char					m_szMuthurTimeMgmtQueueName[ML_MAXLEN_MUTHUR_TM_QUEUE_NAME];	//!< Name of muthur heartbeat queue		
		int						m_iFrequencyOfBeats;											//!< Frequency of heartbeats 
		unsigned long			m_ulMuthurHeartbeatTimeoutTimeMSecs;							//!< Time when the system will timeout because it has not received a heartbeat from muthur
	protected:
		
		int						m_iTimeOutSecs;									//!< Default timeout for singleton requests (Register, ListFEM)
		char					m_szFederateName[ML_MAXLEN_FEDERATE_NAME];		//!< The name of the federate using this Ambassador
		CMLHandle				m_fedRegHandle;									//!< Federation registration handle returned in response to Register() 
		CMLFedExecModel			m_fedExecModel;									//!< Federation execution model being executed
		CMLHandle				m_fedExecHandle;								//!< Federate execution handle
		IMLSystemCallback*		m_pISystemCallback;								//!< Callback interface for system events supplied by federate
		IMLObjectEventCallback*	m_pIObjectEventCallback;						//!< Callback interface for data events
		IMLTimeManagementEventCallback *m_pITimeMgmtUpdateCallback;				//!< Callback interface for time management update events
		IMLObjectOwnershipRelinquishCallback *m_pIOwnershipRelinquishCallback;	//!< Callback interface for object ownership relinquish event

		bool					m_bEnableLogging;				//!< TRUE to indicate logging is enable; FALSE to indicate it is not (Default is FALSE)	
		bool					m_bIsHeartbeatEnabled;			//!< TRUE to indicate check for muthur heartbeat is enabled; FALSE if it is not enabled (Default is FALSE)
	public:
								CMLAmbassador();
		virtual				   ~CMLAmbassador();
		
		static CMLAmbassador&	GetInstance();

		char*					GetFederateName();
		int						GetTimeOutSecs();
		unsigned long			GetSysTimeMsecs();
		CMLFedExecModel			GetFedExecModel();
		CMLHandle				GetFedRegHandle();
		CMLHandle				GetFedExecModelHandle();
		CMLHandle				GetFedExecHandle();
		bool					GetIsHeartbeatEnabled();

		void					SetFederateName(char* pszName);
		void					SetTimeOutSecs(int iSecs);
		void					SetIsHeartbeatEnabled(bool);

		void					Shutdown();
		bool					Initialize(char* pszMuthurUrl, int iMuthurPort, unsigned long ulWaitMsecs);
		bool					GetInitialized();
		
		bool					Register(CMLRegisterParams* pParams);
		bool					ListFedExecModels(CMLListFEMParams* pParams);
		bool					JoinFederation(CMLJoinFedParams* pParams);
		bool					AddDataSubscriptions(CMLAddDataSubParams* pParams);
//		bool					AddDataPublications(CMLAddDataPubParams* pParams);
		bool					ReadyToRun(CMLReadyParams* pParams);
		bool					StartFederation(CMLStartFederationParams* pParams);
		bool					PauseFederation(CMLPauseFederationParams* pParams);
		bool					ResumeFederation(CMLResumeFederationParams* pParams);
		bool					CreateObject(CMLCreateObjectParams* pParams);
		bool					UpdateObject(CMLUpdateObjectParams* pParams);
		bool					DeleteObject(CMLDeleteObjectParams* pParams);
		bool					RequestTransferObject(CMLTransferObjectOwnershipParams* pParams);
		bool					RelinquishObject(CMLRelinquishObjectOwnershipRequest* pParams);
		bool					TransferObject(CMLTransferObjectOwnershipParams* pParams);
		bool					Terminate(CMLTerminateParams* pParams);

		//	Helpers
		static char*			CreateUUID(char* pszBuffer, int iSize);
		
		//	Internals
		void					OnMMTimer(unsigned int uTimerID, unsigned long ulUser);
		void					OnMQResponse(const char* pszResponse);
		void					OnMQObjectAction(const char* pszDataPub);
		void					OnMQSysMessage(const char* pszSysMessage);
		void					OnMQFedEvent(const char* pszFedEvent);
		void					OnMQException(const char* pszExMessage);
		void					OnMQMuthurHeartbeat(const char* pszDataPub);
		void					OnMQMuthurTimeManagementUpdate(const char* pszTimeMgmtUpdate);
		void					OnMQMuthurObjectOwnershipReliniquish(const char* pszObjOwnRel);


		void					CheckPending();
		void					CheckMuthurStatus();

		void					EnableLogging(bool);
		
	protected:
	
		void					SetExpiration(CMLRequestParams* pRequest);
		void					OnRequestError(CMLRequestParams* pRequest, char* pszErrorMessage);
		CMLEvent*				GetResponseFromStream(const char* pszXmlStream);
		CMLEvent*				GetResponseFromName(const char* pszEventName);
		CMLRequestParams*		GetPending(char* pszUUID, bool bRemove, bool bDelete);
		CMLRequestParams*		AddPending(CMLRequestParams* pRequest);
		CMLRequestParams*		SendRequest(CMLRequestParams* pRequest, int iReplyTo);
		long					GetTimeOutMsec(CMLRequestParams* pRequest);
};

#endif // !defined(__ML_AMBASSADOR_H__)
