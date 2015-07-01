//------------------------------------------------------------------------------
/*! \file	MLListFEMResponse.h
//
//  Contains declaration of the CMLListFEMResponse class
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
#if !defined(__ML_LIST_FEM_RESPONSE_H__)
#define __ML_LIST_FEM_RESPONSE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>
#include <MLFedExecModel.h>
#include <MLPtrList.h>

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
//! \brief Class wrapper for the Ambassador's response to a ListFedExecModels 
//!		   request
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLListFEMResponse : public CMLEvent
{
	protected:
	
		CMLPtrList							m_apFedExecModels;	//!< The list of available federation execution models
		PTRNODE								m_posFedExecModels;	//!< Used for iteration of federation execution models

	public:
			
											CMLListFEMResponse();
											CMLListFEMResponse(const CMLListFEMResponse& rSource);
						
		virtual							   ~CMLListFEMResponse();
		
	
		virtual void						Copy(const CMLListFEMResponse& rSource);
		virtual const CMLListFEMResponse&	operator = (const CMLListFEMResponse& rSource);

		virtual const CMLPtrList&			GetFedExecModels() const;
		virtual CMLPtrList&					GetFedExecModels();
		virtual CMLFedExecModel*			AddFedExecModel(const CMLFedExecModel& rAddFEM);
		virtual CMLFedExecModel*			GetFirstFedExecModel();
		virtual CMLFedExecModel*			GetNextFedExecModel();

		EMLResponseClass					GetClass(){ return ML_RESPONSE_CLASS_LIST_FEM; }

		virtual CMLPropMember*				GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool						SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_LIST_FEM_RESPONSE_H__)
