//------------------------------------------------------------------------------
/*! \file	MLReadyParams.h
//
//  Contains declaration of the CMLReadyParams class
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
#if !defined(__ML_READY_PARAMS_H__)
#define __ML_READY_PARAMS_H__

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

//------------------------------------------------------------------------------
//! \brief This class wraps the parameters used to register a federate
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLReadyParams : public CMLRequestParams
{
protected:
	char							m_szMuthurTimeManagementQueueName[ML_MAXLEN_MUTHUR_TM_QUEUE_NAME];		//!< Name of muthur time management queue

public:
	CMLReadyParams();
	CMLReadyParams(const CMLReadyParams& rSource);

	virtual						   ~CMLReadyParams();

	virtual void					Copy(const CMLReadyParams& rSource);
	virtual const CMLReadyParams&	operator = (const CMLReadyParams& rSource);

	EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_READY; }

	virtual char*					GetMuthurTimeManagementQueueName() ;
	virtual void					SetMuthurTimeManagementQueueName(char*);

	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_PARAMS_H__)
