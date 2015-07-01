//------------------------------------------------------------------------------
/*! \file	MLSafePtrList.cpp
//
//  Contains the implementation of the CMLSafePtrList class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>07-20-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLSafePtrList.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSafePtrList::CMLSafePtrList() : CMLPtrList()
{
	InitializeCriticalSection(&m_criticalSection);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSafePtrList::~CMLSafePtrList()
{
	RemoveAll(TRUE); 
	//DeleteCriticalSection(&m_criticalSection);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a pointer to the head of the list
//!
//!	Parameters:	\li pvObject - pointer to the caller's object
//!
//!	Returns:	The position node bound to the object just added
//------------------------------------------------------------------------------
PTRNODE CMLSafePtrList::AddHead(void* pvObject)
{
	PTRNODE Pos = NULL;


	Lock();
	Pos = CMLPtrList::AddHead(pvObject);
	Unlock();
	
	return Pos;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a pointer to the end of the list
//!
//!	Parameters:	\li pvObject - pointer to the caller's object
//!
//!	Returns:	The position node bound to the object just added
//------------------------------------------------------------------------------
PTRNODE CMLSafePtrList::AddTail(void* pvObject)
{
	if(m_tailPosition != NULL)
	{
		Lock();
		CMLPtrList::AddTail(pvObject);
		Unlock();

        return m_tailPosition;
	}
	else
	{
		return AddHead(pvObject);
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the position node at the head of the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first position node in the list
//------------------------------------------------------------------------------
PTRNODE CMLSafePtrList::GetHeadPosition()
{
	PTRNODE Pos = NULL;

	Lock();
	Pos = CMLPtrList::GetHeadPosition();
	Unlock();
	
	return Pos;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the last position node in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The last position node in the list
//------------------------------------------------------------------------------
PTRNODE CMLSafePtrList::GetTailPosition()
{
	PTRNODE Pos = NULL;

	Lock();
	Pos = CMLPtrList::GetTailPosition();
	Unlock();
	
	return Pos;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to insert an object into the list
//!
//!	Parameters:	\li Pos - the position node used to determine the insertion point
//!
//!	Returns:	The position node of the object inserted in the list
//------------------------------------------------------------------------------
PTRNODE CMLSafePtrList::InsertAfter(PTRNODE Pos, void* pvObject)
{
	PTRNODE InsertPos = NULL;;

	Lock();
	InsertPos = CMLPtrList::InsertAfter(Pos, pvObject);
	Unlock();

	return InsertPos;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to insert an object into the list
//!
//!	Parameters:	\li Pos - the position node used to determine the insertion point
//!
//!	Returns:	The position node of the object inserted in the list
//------------------------------------------------------------------------------
PTRNODE CMLSafePtrList::InsertBefore(PTRNODE Pos, void* pvObject)
{
	PTRNODE InsertPos = NULL;;

	Lock();
	InsertPos = CMLPtrList::InsertBefore(Pos, pvObject);
	Unlock();

	return InsertPos;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to lock access to objects in this list
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrList::Lock()
{
	EnterCriticalSection(&m_criticalSection);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove all object pointers in the list
//!
//!	Parameters:	\li bDelete - TRUE to delete objects when they are removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrList::RemoveAll(bool bDelete)
{
	Lock();
	CMLPtrList::RemoveAll(bDelete);
	Unlock();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the object at the specified position
//!
//!	Parameters:	\li bDelete - TRUE to delete the object when it's removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrList::RemoveAt(PTRNODE Pos, bool bDelete)
{
	if(!IsValidPosition(Pos))
		return;

	//	Is this the head position?
	if(Pos == m_headPosition)
	{
		RemoveHead(bDelete);
	}

	//	Is this the tail position?
	else if(Pos == m_tailPosition)
	{
		RemoveTail(bDelete);
	}

	//	Must be somewhere in the list
	else
	{
		Lock();
		CMLPtrList::RemoveAt(Pos, bDelete);
		Unlock();
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the object at the head position
//!
//!	Parameters:	\li bDelete - TRUE to delete the object when it's removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void* CMLSafePtrList::RemoveHead(bool bDelete)
{
	void* pvObject = NULL;

	Lock();
	pvObject = CMLPtrList::RemoveHead(bDelete);
	Unlock();

	return pvObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the object at the tail position
//!
//!	Parameters:	\li bDelete - TRUE to delete the object when it's removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void* CMLSafePtrList::RemoveTail(bool bDelete)
{
	void* pvObject = NULL;

	Lock();
	pvObject = CMLPtrList::RemoveTail(bDelete);
	Unlock();

	return pvObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the object pointer at the specified position
//!
//!	Parameters:	\li Pos - the position of the object in the list
//!				\li pvObject - the object pointer to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrList::SetAt(PTRNODE Pos, void* pvObject)
{
	Lock();
	CMLPtrList::SetAt(Pos, pvObject);
	Unlock();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to unlock access to objects in this list
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrList::Unlock()
{
	LeaveCriticalSection(&m_criticalSection);
}




