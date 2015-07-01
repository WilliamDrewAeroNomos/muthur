//------------------------------------------------------------------------------
/*! \file	MLString.h
//
//  Contains declaration of the CMLString class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-28-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_STRING_H__)
#define __ML_STRING_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
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
//! \brief This class implements a variable length null terminated string
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLString
{
	public:

									CMLString(const char* pszString = NULL);
									CMLString(const CMLString& rString);
		virtual					   ~CMLString();

		virtual void				Empty();
		virtual int					GetLength();
		virtual bool				IsEmpty();

		virtual char				GetAt(int iIndex);
		virtual void				SetAt(int iIndex, char cChar);

		virtual int					Compare(const char* pszCompare);
		virtual int					CompareNoCase(const char* pszCompare);

		virtual void				MakeUpper();
        virtual void				MakeLower();

		virtual CMLString& 			Left(int iCount, CMLString& rSubString);
		virtual CMLString&			Right(int iCount, CMLString& rSubString);

		virtual int	  				operator == (const CMLString& rString);
		virtual int	  				operator < (const CMLString& rString);
		virtual int	  				operator > (const CMLString& rString);

        virtual const CMLString&	operator = (const CMLString& rString);
		virtual const CMLString& 	operator = (const char* pszString);
		virtual const CMLString& 	operator += (const char* pszString);

        virtual char				operator [] (int iIndex);

									operator const char*() const;
                                	operator char*();

	protected:

		char*						m_pszString;

		virtual void				Free();
		virtual char*				Alloc(unsigned int uLength);
        virtual char*				Set(const char* pszString);
        virtual char*				Add(const char* pszString);

};

#endif // !defined(__ML_STRING_H__)
