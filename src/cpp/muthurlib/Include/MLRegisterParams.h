//------------------------------------------------------------------------------
/*! \file	MLRegisterParams.h
//
//  Contains declaration of the CMLRegisterParams class
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
#if !defined(__ML_REGISTER_PARAMS_H__)
#define __ML_REGISTER_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//	Forward declarations
class IMLSystemCallback;
class IMLTimeManagementEventCallback;

//------------------------------------------------------------------------------
//! \brief This class wraps the parameters used to register a federate
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLRegisterParams : public CMLRequestParams
{
	protected:
	
		IMLSystemCallback*						m_pISystemCallback;												//!< Ambassador callback interface for system events
		IMLTimeManagementEventCallback*			m_pITimeMgmtUpdateCallback;										//!< Callback interface for time management update events
		char									m_szName[ML_MAXLEN_FEDERATE_NAME];								//!< Name used to identify the federate being registered
		char									m_szMuthurHeartbeatQueueName[ML_MAXLEN_MUTHUR_HB_QUEUE_NAME];	//!< Name of muthur heartbeat queue
		int										m_iFrequencyOfBeats;											//!< Frequency of heartbeats 
		
		

	public:
			
											CMLRegisterParams(char* pszName = NULL, IMLSystemCallback* pICallback = NULL, 
												IMLTimeManagementEventCallback *pITimeMgmtCallback = NULL);
											CMLRegisterParams(const CMLRegisterParams& rSource);
						
		virtual							   ~CMLRegisterParams();
		
		virtual char*						GetName();
		virtual IMLSystemCallback*			GetSystemCallback();
		virtual IMLTimeManagementEventCallback* GetTimeManagementCallback();

		virtual char*						GetMuthurHeartbeatQueueName();
		virtual void						SetMuthurHeartbeatQueueName(char*);

		virtual int							GetFrequencyOfHeartbeats();
		virtual void						SetFrequencyOfHeartbeats(int);
		
		virtual void						SetName(char* pszName);
		virtual void						SetSystemCallback(IMLSystemCallback* pICallback);
		virtual void						SetTimeManagementCallback(IMLTimeManagementEventCallback* pICallback);

		virtual void						Copy(const CMLRegisterParams& rSource);
		virtual const CMLRegisterParams&	operator = (const CMLRegisterParams& rSource);

		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool    SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLRequestClass						GetClass(){ return ML_REQUEST_CLASS_REGISTER; }
};

#endif // !defined(__ML_REGISTER_PARAMS_H__)
