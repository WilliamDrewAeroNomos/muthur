//------------------------------------------------------------------------------
/*! \file	MLPtrList.h
//
//  Contains declaration of the CMLPtrList class
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
#if !defined(__ML_PTR_LIST_H__)
#define __ML_PTR_LIST_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLHandle.h>
#include <MLDataStream.h>

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
//! \brief Class used to manage nodes in a MuthurLib object pointer linked list
//------------------------------------------------------------------------------
typedef struct SListNode
{
	void*				pvObject;	//!< Pointer to the object at this PTRNODE
	struct SListNode*	pPrev;		//!< Pointer to the previous node in the list
	struct SListNode*	pNext;		//!< Pointer to the next node in the list

	SListNode()
	{
		pvObject = NULL;
		pPrev = NULL;
		pNext = NULL;
	}

}SListNode;

typedef SListNode* PTRNODE;

//------------------------------------------------------------------------------
//! \brief This class implements a linked list of object pointers
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLPtrList
{
	private:


	public:

								CMLPtrList();
		virtual				   ~CMLPtrList();

		virtual PTRNODE			Find(void* pvObject) const;
		virtual PTRNODE			GetHeadPosition() const;
		virtual PTRNODE			GetHeadPosition();
		virtual PTRNODE			GetTailPosition() const;
		virtual PTRNODE			GetTailPosition();
		virtual PTRNODE			AddHead(void* pvObject);
		virtual PTRNODE			AddTail(void* pvObject);
        virtual PTRNODE			InsertBefore(PTRNODE Pos, void* pvObject);
        virtual PTRNODE			InsertAfter(PTRNODE Pos, void* pvObject);
		virtual bool			IsEmpty() const;
		virtual int				GetCount() const;
		virtual void*			GetHead() const;
		virtual void*			GetTail() const;
		virtual void*			GetAt(PTRNODE Pos);
		virtual void*			GetNext(PTRNODE& rPos) const;
        virtual void*			GetPrev(PTRNODE& rPos) const;
		virtual void*			RemoveHead(bool bDelete);
        virtual void*			RemoveTail(bool bDelete);
		virtual void			RemoveAll(bool bDelete);
		virtual void			Remove(void* pvObject, bool bDelete);
		virtual void			RemoveAt(PTRNODE Pos, bool bDelete);
        virtual void			SetAt(PTRNODE Pos, void* pvObject);

	protected:

		PTRNODE					m_headPosition;
		PTRNODE					m_tailPosition;
		int						m_iCount;

        virtual bool			IsValidPosition(PTRNODE Pos) const;
};

#endif // !defined(__ML_PTR_LIST_H__)
