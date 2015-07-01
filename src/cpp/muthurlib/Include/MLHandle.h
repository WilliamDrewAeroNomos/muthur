//------------------------------------------------------------------------------
/*! \file	MLHandle.h
//
//  Contains declaration of the CMLHandle class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-12-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_HANDLE_H__)
#define __ML_HANDLE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <string>
#include <MLApi.h>

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
//! \brief Base class for all derived Ambassador response classes
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLHandle
{
	protected:
	
		char						m_szMuthurId[ML_MAXLEN_HANDLE_ID]; //!< The ID assigned by Muthur

	public:
									CMLHandle(char* pszMuthurId = "");
									CMLHandle(const CMLHandle& rSource);
						
		virtual					   ~CMLHandle();
		
		virtual char*				GetMuthurId();
		virtual void				SetMuthurId(char* pszMuthurId);
		
		virtual void				Copy(const CMLHandle& rSource);
		virtual const CMLHandle&	operator = (const CMLHandle& rSource);

		virtual bool				IsEqual(const CMLHandle& rCompare);
		virtual bool				operator == (const CMLHandle& rCompare);
		
		virtual void				Reset();
		virtual bool				IsValid();
};

#endif // !defined(__ML_HANDLE_H__)
