//------------------------------------------------------------------------------
/*! \file	MLRequestParams.h
//
//  Contains declaration of the CMLRequestParams class
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
#if !defined(__ML_REQUEST_PARAMS_H__)
#define __ML_REQUEST_PARAMS_H__


//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLHandle.h>
#include <MLDataStream.h>
#include <MLPropMember.h>
#include <MLSerialize.h>


class IMLAmbassadorCallback;
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
//! \brief Base class for all Ambassador request parameters classes
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLRequestParams : public IMLSerialize
{
	char							m_pszEventName[ML_MAXLEN_EVENT_NAME];
	protected:
	
		unsigned long					m_ulIssuedTimestampMSecs;			//!< Time when the request was issued
		unsigned long					m_ulExpireTimeMSecs;				//!< Time when the request expires and timout error generated
		
		CMLHandle						m_fedRegHandle;						//!< Federation registration handle returned in response to Register() 
		CMLHandle						m_fedExecModelHandle;				//!< Federation execution model obtained from ListFedExecutionModels() 
		CMLHandle						m_fedExecHandle;					//!< Federation execution handle returned in response to JoinFederation() 
		char							m_szUUID[ML_MAXLEN_UUID];			//!< Unique object identifier
		EMLRequestTypeClass				m_RequestType;						//!< Indicates if this is a standard request event, a system event request or a data publication event request
		IMLAmbassadorCallback*			m_pIAmbassadorCallback;				//!< Ambassador callback interface for the federate being registered
	public:
			
										CMLRequestParams();
										CMLRequestParams(const CMLRequestParams& rSource);
						
		virtual						   ~CMLRequestParams();
		
		virtual unsigned long			GetIssuedTimestampMSecs();
		virtual unsigned long			GetExpireTimeMSecs();

		virtual CMLHandle				GetFedRegHandle();
		virtual CMLHandle				GetFedExecModelHandle();
		virtual CMLHandle				GetFedExecHandle();
		virtual char*					GetUUID();
		virtual IMLAmbassadorCallback*	GetAmbassadorCallback();
		virtual void					SetIssuedTimestampMSecs(unsigned long ulTimestampMSecs);
		virtual void					SetExpireTimeMSecs(unsigned long ulExpireTimeMSecs);
		virtual void					SetFedRegHandle(CMLHandle& rHandle);
		virtual void					SetFedExecModelHandle(CMLHandle& rHandle);
		virtual void					SetFedExecHandle(CMLHandle& rHandle);
		virtual void					SetUUID(char* pszUUID);
		virtual void					SetAmbassadorCallback(IMLAmbassadorCallback* pICallback);

		virtual void					Copy(const CMLRequestParams& rSource);
		virtual const CMLRequestParams&	operator = (const CMLRequestParams& rSource);
		
		virtual EMLRequestClass			GetClass() = 0;	//!< Must be overridden by derived classes
		
		//	Support for ISerialize interface
		virtual char*					GetXMLName(char* pszName, int iSize);
		virtual CMLPropMember*			GetXmlRoot();
		virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		virtual EMLRequestTypeClass		GetRequestType();
		virtual void					SetRequestType(EMLRequestTypeClass);

		virtual char* CMLRequestParams::GetEventName();
};

#endif // !defined(__ML_REQUEST_PARAMS_H__)
