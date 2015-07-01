//------------------------------------------------------------------------------
/*! \file	MLResumeFederationParams.h
//
//  Contains declaration of the CMLResumeFederationParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>12-10-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_RESUME_FEDERATION_PARAMS_H__)
#define __ML_RESUME_FEDERATION_PARAMS_H__

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
//! \brief This class wraps the parameters used to resume the federates (for testing only)
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLResumeFederationParams : public CMLRequestParams
{
protected:	
public:
	CMLResumeFederationParams();
	CMLResumeFederationParams(const CMLResumeFederationParams& rSource);

	virtual						   ~CMLResumeFederationParams();

	virtual void					Copy(const CMLResumeFederationParams& rSource);
	virtual const CMLResumeFederationParams&	operator = (const CMLResumeFederationParams& rSource);

	EMLRequestClass					GetClass(){ return ML_REQUEST_RESUME_FEDERATIONS; }

	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_PARAMS_H__)
