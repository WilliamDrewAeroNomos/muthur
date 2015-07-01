//------------------------------------------------------------------------------
/*! \file	MLPtrList.h
//
//  Contains declaration of the CMLSafePtrList class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-20-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_SAFE_PTR_LIST_H__)
#define __ML_SAFE_PTR_LIST_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
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
//! \brief This class implements a thread safe linked list of object pointers
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLSafePtrList : public CMLPtrList
{
	private:

		CRITICAL_SECTION		m_criticalSection;	//!< Critical section to make the collection thread safe

	public:

								CMLSafePtrList();
		virtual				   ~CMLSafePtrList();
		
		void					Lock();
		void					Unlock();

		PTRNODE					GetHeadPosition();
		PTRNODE					GetTailPosition();
		PTRNODE					AddHead(void* pvObject);
		PTRNODE					AddTail(void* pvObject);
        PTRNODE					InsertBefore(PTRNODE Pos, void* pvObject);
        PTRNODE					InsertAfter(PTRNODE Pos, void* pvObject);
		void*					RemoveHead(bool bDelete);
        void*					RemoveTail(bool bDelete);
		void					RemoveAll(bool bDelete);
        void					RemoveAt(PTRNODE Pos, bool bDelete);
        void					SetAt(PTRNODE Pos, void* pvObject);
};

#endif // !defined(__ML_SAFE_PTR_LIST_H__)
