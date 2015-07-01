//------------------------------------------------------------------------------
/*! \file	MLReadyParams.h
//
//  Contains declaration of the CMLStartFederationRequest class
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
#if !defined(__ML_START_FEDERATION_PARAMS_H__)
#define __ML_START_FEDERATION_PARAMS_H__

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
//! \brief This class wraps the parameters used to start the federates (for testing only)
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLStartFederationParams : public CMLRequestParams
{
protected:
	
public:
	CMLStartFederationParams();
	CMLStartFederationParams(const CMLStartFederationParams& rSource);

	virtual						   ~CMLStartFederationParams();

	

	virtual void					Copy(const CMLStartFederationParams& rSource);
	virtual const CMLStartFederationParams&	operator = (const CMLStartFederationParams& rSource);

	EMLRequestClass					GetClass(){ return ML_REQUEST_START_FEDERATIONS; }

	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_PARAMS_H__)
