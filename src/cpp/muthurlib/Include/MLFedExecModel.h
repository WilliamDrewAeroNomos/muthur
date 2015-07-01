//------------------------------------------------------------------------------
/*! \file	MLFedExecModel.h
//
//  Contains declaration of the CMLFedExecModel class
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
#if !defined(__ML_FED_EXEC_MODEL_H__)
#define __ML_FED_EXEC_MODEL_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLHandle.h>
#include <MLPropMember.h>
#include <MLPtrList.h>
#include <MLString.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class wrapper for an individual Federation Execution Model (FEM)
//------------------------------------------------------------------------------

class MUTHURLIB_API CMLFedExecModel
{
	protected:
	
		CMLHandle						m_fedExecModelHandle;											//!< FEM handle used to identify this model
		char							m_szName[ML_MAXLEN_FED_EXEC_MODEL_NAME];						//!< Name of this federation model
		char							m_szDescription[ML_MAXLEN_FED_EXEC_MODEL_DESCRIPTION];			//!< Description of this federation model
		long							m_lLogicalStartTime;											//!< Logical start time of this federation model
		long							m_lDefaultDurationWithinStartupProtocolMSecs;					//!< Default value used for the next 6 durations if none of those values need to be explicitly set
		long							m_lDurationFederationExecutionMSecs;							//!< Duration for federate to run
		long							m_lDurationJoinFederationMSecs;									//!< Duration to wait for all federates to join
		long							m_lDurationRegisterPublicationMSecs;							//!< Duration to wait for all publications to be registered
		long							m_lDurationRegisterSubscriptionMSecs;							//!< Duration to wait for all subscriptions to be registered
		long							m_lDurationRegisterToRunMSecs;									//<! Duration to wait for all registrations to run
		long							m_lDurationTimeToWaitAfterTermination;							//<! Duration to wait after termination message received prior to shutting down
		bool							m_bAutoStart;													//<! Determines whether or not federates will automatically start

		CMLPtrList						m_apReqFederates;												//!< The list of required federates
		PTRNODE							m_posReqFederates;												//!< Used for iteration of required federates

public:
	
										CMLFedExecModel();
										CMLFedExecModel(const CMLFedExecModel& rSource);
						
		virtual						   ~CMLFedExecModel();
		
		virtual CMLHandle				GetFedExecModelHandle();
		virtual char*					GetName();
		virtual char*					GetDescription();
		virtual long					GetLogicalStartTime();
		virtual long					GetDefaultDurationWithinStartupProtocolMSecs();
		virtual	long					GetDurationFederationExecutionMSecs();
		virtual long					GetDurationJoinFederationMSecs();
		virtual long					GetDurationRegisterPublicationMSecs();
		virtual long					GetDurationRegisterSubscriptionMSecs();
		virtual long					GetDurationRegisterToRunMSecs();
		virtual long					GetDurationTimeToWaitAfterTermination();
		virtual bool					GetAutoStart();
		
		virtual void					SetFedExecModelHandle(CMLHandle& rHandle);
		virtual void					SetName(char*);
		virtual void					SetDescription(char*);
		virtual void					SetLogicalStartTime(long);
		virtual void					SetDefaultDurationWithinStartupProtocolMSecs(long);
		virtual	void					SetDurationFederationExecutionMSecs(long);
		virtual void					SetDurationJoinFederationMSecs(long);
		virtual void					SetDurationRegisterPublicationMSecs(long);
		virtual void					SetDurationRegisterSubscriptionMSecs(long);
		virtual void					SetDurationRegisterToRunMSecs(long);
		virtual void					SetDurationTimeToWaitAfterTermination(long);
		virtual void					SetAutoStart(bool);

		virtual void					Reset();
		virtual void					Copy(const CMLFedExecModel& rSource);
		virtual const CMLFedExecModel&	operator = (const CMLFedExecModel& rSource);

		virtual const CMLPtrList&		GetRequiredFederates() const;
		virtual CMLPtrList&				GetRequiredFederates();
		virtual const char*				AddRequiredFederate(const char* pszName);
		virtual const char*				GetFirstRequiredFederate();
		virtual const char*				GetNextRequiredFederate();

		virtual CMLPropMember*			GetXmlMember();
		virtual bool					SetXmlValues(CMLPropMember* pFEMMember);
};

#endif // !defined(__ML_FED_EXEC_MODEL_H__)
