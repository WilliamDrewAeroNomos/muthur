//------------------------------------------------------------------------------
/*! \file	MLTimeManagementEvent.h
//
//  Contains declaration of the CMLTimeManagementEvent class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>10-10-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_TIMEMANAGEMENT_EVENT_H__)
#define __ML_TIMEMANAGEMENT_EVENT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
#include <MLDataObject.h>

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
//! \brief This class wraps the information associated with a data event
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLTimeManagementEvent : public IMLSerialize
{
	protected:

		double						m_dTimeIntervalMSecs;			//!< Time Interval in msecs as sent my muthur for the purpose of syncing the time of the federates
		int							m_iFederationExecutionState;	//!< Indicates current state of federation (UNDEFINED=0,RUNNING=1,PAUSED=2)
		int							m_iFederationExecutionDirective;//!< Indicates an action the federate should take (UNDEFINED=0,START=1,PAUSE=2,RESUME=3,RUN=4)

 
	public:

									CMLTimeManagementEvent();
									CMLTimeManagementEvent(const CMLTimeManagementEvent& rSource);

		virtual					   ~CMLTimeManagementEvent();

		virtual void				Copy(const CMLTimeManagementEvent& rSource);
		virtual const CMLTimeManagementEvent&	operator = (const CMLTimeManagementEvent& rSource);

		virtual double			    GetTimeInterval();
		virtual int					GetFederationExecutionState();
		virtual int					GetFederationExecutionDirective();

		virtual void				SetTimeInterval(double);		
		virtual void				SetFederationExecutionState(int);
		virtual void				SetFederationExecutionDirective(int);

		//	Support for ISerialize interface
		virtual char*				GetXMLName(char* pszName, int iSize);
		virtual CMLPropMember*		GetXmlRoot();
		virtual CMLPropMember*		GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool				SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_DATA_EVENT_H__)
