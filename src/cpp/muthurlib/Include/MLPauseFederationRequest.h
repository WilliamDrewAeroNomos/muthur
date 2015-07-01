//------------------------------------------------------------------------------
/*! \file	MLPauseFederationParams.h
//
//  Contains declaration of the CMLPauseFederationParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>12-11-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_PAUSE_FEDERATION_PARAMS_H__)
#define __ML_PAUSE_FEDERATION_PARAMS_H__

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
//! \brief This class wraps the parameters used to pause the federates (for testing only)
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLPauseFederationParams : public CMLRequestParams
{
protected:
	long							m_lLengthOfPauseMSecs; //!< Length of Pause in M Secs
public:
	CMLPauseFederationParams();
	CMLPauseFederationParams(const CMLPauseFederationParams& rSource);

	virtual						   ~CMLPauseFederationParams();

	virtual long					GetLengthOfPause();
	virtual void					SetLengthOfPause(long);
	

	virtual void					Copy(const CMLPauseFederationParams& rSource);
	virtual const CMLPauseFederationParams&	operator = (const CMLPauseFederationParams& rSource);

	EMLRequestClass					GetClass(){ return ML_REQUEST_PAUSE_FEDERATIONS; }

	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_READY_PARAMS_H__)
