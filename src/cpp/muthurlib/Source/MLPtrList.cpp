//------------------------------------------------------------------------------
/*! \file	MLPtrList.cpp
//
//  Contains the implementation of the CMLPtrList class
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

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLPtrList.h>

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
CMLPtrList::CMLPtrList()
{
	m_headPosition = NULL;
	m_tailPosition = NULL;
	m_iCount = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPtrList::~CMLPtrList()
{
	RemoveAll(TRUE); // Default is to remove and delete
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a pointer to the head of the list
//!
//!	Parameters:	\li pvObject - pointer to the caller's object
//!
//!	Returns:	The position node bound to the object just added
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::AddHead(void* pvObject)
{
	PTRNODE Pos = new SListNode;

	assert(pvObject != NULL);
	assert(Pos != NULL);

	//	Initialize the new position
	Pos->pvObject = pvObject;
	Pos->pPrev = NULL;

	//	Do we already have a head node?
	if(m_headPosition != NULL)
	{
		Pos->pNext = m_headPosition;
		m_headPosition->pPrev = Pos;
		m_headPosition = Pos;
	}
	else
	{
		Pos->pNext = NULL;
		m_headPosition = Pos;
		m_tailPosition = Pos;
	}

	m_iCount++;

	return m_headPosition;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a pointer to the end of the list
//!
//!	Parameters:	\li pvObject - pointer to the caller's object
//!
//!	Returns:	The position node bound to the object just added
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::AddTail(void* pvObject)
{
	PTRNODE Pos;

	assert(pvObject != NULL);

	//	Do we already have a tail node?
	if(m_tailPosition != NULL)
	{
		Pos = new SListNode;
		assert(Pos != NULL);

		//	Initialize the new position
		Pos->pvObject = pvObject;
		Pos->pNext = NULL;
		Pos->pPrev = m_tailPosition;

		m_tailPosition->pNext = Pos;
		m_tailPosition = Pos;

		m_iCount++;

        return m_tailPosition;
	}
	else
	{
		return AddHead(pvObject);
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the position of the specified object in the list
//!
//!	Parameters:	\li pvObject - the object pointer to search for
//!
//!	Returns:	The position node bound to the object if found
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::Find(void* pvObject) const
{
	PTRNODE Pos;

	Pos = m_headPosition;
	while(Pos != NULL)
	{
		if(Pos->pvObject == pvObject)
			break;
		else
			Pos = Pos->pNext;
	}

	return Pos;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the object pointer at the specified position
//!
//!	Parameters:	\li Pos - the position node if the desired object
//!
//!	Returns:	The position node bound to the object just added
//------------------------------------------------------------------------------
void* CMLPtrList::GetAt(PTRNODE Pos)
{
	if(IsValidPosition(Pos))
		return Pos->pvObject;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the number of pointers in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The total number of object pointers in the list
//------------------------------------------------------------------------------
int CMLPtrList::GetCount() const
{
	return m_iCount;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the object pointer at the head of the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first object pointer in the list
//------------------------------------------------------------------------------
void* CMLPtrList::GetHead() const
{
	if(m_headPosition != NULL)
		return m_headPosition->pvObject;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the position node at the head of the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first position node in the list
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::GetHeadPosition() const
{
	return m_headPosition;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the position node at the head of the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first position node in the list
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::GetHeadPosition()
{
	return m_headPosition;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the next object pointer in the list
//!
//!	Parameters:	\li rPos - the position node used as a reference for the next
//!
//!	Returns:	The next object pointer in the list
//------------------------------------------------------------------------------
void* CMLPtrList::GetNext(PTRNODE& rPos) const
{
	void* pvObject = NULL;

	//	Is the caller's position valid?
	if(IsValidPosition(rPos))
	{
		pvObject = rPos->pvObject;
		rPos = rPos->pNext;
	}

	return pvObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the previous object pointer in the list
//!
//!	Parameters:	\li rPos - the position node used as reference for the previous
//!
//!	Returns:	The previous object pointer in the list
//------------------------------------------------------------------------------
void* CMLPtrList::GetPrev(PTRNODE& rPos) const
{
	void* pvObject = NULL;

	//	Is the caller's position valid?
	if(IsValidPosition(rPos))
	{
		pvObject = rPos->pvObject;
		rPos = rPos->pPrev;
	}

	return pvObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the last object pointer in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The last object pointer in the list
//------------------------------------------------------------------------------
void* CMLPtrList::GetTail() const
{
	if(m_tailPosition != NULL)
		return m_tailPosition->pvObject;
	else
		return NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the last position node in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The last position node in the list
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::GetTailPosition() const
{
	return m_tailPosition;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the last position node in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The last position node in the list
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::GetTailPosition()
{
	return m_tailPosition;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to insert an object into the list
//!
//!	Parameters:	\li Pos - the position node used to determine the insertion point
//!
//!	Returns:	The position node of the object inserted in the list
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::InsertAfter(PTRNODE Pos, void* pvObject)
{
	PTRNODE Insert = NULL;;

	if(IsValidPosition(Pos))
	{
		Insert = new SListNode;
		assert(Insert);

		//	Initialize the new position
		Insert->pvObject = pvObject;
		Insert->pNext = Pos->pNext;
		Insert->pPrev = Pos;

		//	Reset the other link pointers
		if(Insert->pPrev)
			Insert->pPrev->pNext = Insert;
		if(Insert->pNext)
			Insert->pNext->pPrev = Insert;

		m_iCount++;
	}

	return Insert;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to insert an object into the list
//!
//!	Parameters:	\li Pos - the position node used to determine the insertion point
//!
//!	Returns:	The position node of the object inserted in the list
//------------------------------------------------------------------------------
PTRNODE CMLPtrList::InsertBefore(PTRNODE Pos, void* pvObject)
{
	PTRNODE Insert = NULL;;

	if(IsValidPosition(Pos))
	{
		Insert = new SListNode;
		assert(Insert);

		//	Initialize the new position
		Insert->pvObject = pvObject;
		Insert->pPrev = Pos->pPrev;
		Insert->pNext = Pos;

		//	Reset the other link pointers
		if(Insert->pPrev)
			Insert->pPrev->pNext = Insert;
		if(Insert->pNext)
			Insert->pNext->pPrev = Insert;

		m_iCount++;
	}

	return Insert;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the list is empty
//!
//!	Parameters:	None
//!
//!	Returns:	true if empty
//------------------------------------------------------------------------------
bool CMLPtrList::IsEmpty() const
{
	if(m_iCount == 0)
		return true;
	else
		return false;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the specified position node is valid
//!
//!	Parameters:	\li Pos - the node to be verified
//!
//!	Returns:	true if valid
//------------------------------------------------------------------------------
bool CMLPtrList::IsValidPosition(PTRNODE Pos) const
{
#if defined(_DEBUG)
	PTRNODE Current;
				
	Current = m_headPosition;
	while(Current != NULL)
	{
		if(Current == Pos)
			return true;
		else
			Current = Current->pNext;
	}
	return false;
#else
	if(Pos != NULL)
		return true;
	else
		return false;
#endif
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove all object pointers in the list
//!
//!	Parameters:	\li bDelete - TRUE to delete objects when they are removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPtrList::RemoveAll(bool bDelete)
{
	PTRNODE Pos;

	Pos = m_headPosition;
	while(Pos != NULL)
	{
		m_headPosition = Pos->pNext;
		
		if(bDelete && (Pos->pvObject != NULL))
			delete Pos->pvObject;
		delete Pos;

		Pos = m_headPosition;
	}

	m_headPosition = NULL;
	m_tailPosition = NULL;
	m_iCount = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the object at the specified position
//!
//!	Parameters:	\li bDelete - TRUE to delete the object when it's removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPtrList::RemoveAt(PTRNODE Pos, bool bDelete)
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
		if(Pos->pNext)
			Pos->pNext->pPrev = Pos->pPrev;
		if(Pos->pPrev)
			Pos->pPrev->pNext = Pos->pNext;

		if(bDelete && (Pos->pvObject != NULL))
			delete Pos->pvObject;
		delete Pos;

		m_iCount--;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the object at the head position
//!
//!	Parameters:	\li bDelete - TRUE to delete the object when it's removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void* CMLPtrList::RemoveHead(bool bDelete)
{
	PTRNODE	Pos;
	void*		pvObject = NULL;

	//	Do we have a head element?
	if(m_headPosition != NULL)
	{
		pvObject = m_headPosition->pvObject;

		//	Is this the only element in the list?
		if(m_headPosition->pNext == NULL)
		{
			delete m_headPosition;
			m_headPosition = NULL;
			m_tailPosition = NULL;
			m_iCount = 0;
		}
		else
		{
			Pos = m_headPosition;
			m_headPosition = m_headPosition->pNext;
			m_headPosition->pPrev = NULL;
			m_iCount--;
			delete Pos;
		}
	
		if((bDelete == true) && (pvObject != NULL))
		{
			delete pvObject;
			pvObject = NULL;
		}

	}

	return pvObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the object at the tail position
//!
//!	Parameters:	\li bDelete - TRUE to delete the object when it's removed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void* CMLPtrList::RemoveTail(bool bDelete)
{
	PTRNODE 	Pos;
	void*		pvObject = NULL;

	//	Do we have a tail element?
	if(m_tailPosition != NULL)
	{
		pvObject = m_tailPosition->pvObject;

		//	Is this the only element in the list?
		if(m_tailPosition->pPrev == NULL)
		{
			delete m_tailPosition;
			m_headPosition = NULL;
			m_tailPosition = NULL;
			m_iCount = 0;
		}
		else
		{
			Pos = m_tailPosition;
			m_tailPosition = m_tailPosition->pPrev;
			m_tailPosition->pNext = NULL;
			m_iCount--;
			delete Pos;
		}

		if((bDelete == true) && (pvObject != NULL))
		{
			delete pvObject;
			pvObject = NULL;
		}

	}

	return pvObject;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to remove the specified object
//!
//!	Parameters:	\li pvObject - the object to be removed
//!				\li bDelete - true to delete the object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPtrList::Remove(void* pvObject, bool bDelete)
{
	PTRNODE Pos = NULL;
	
	if((Pos = Find(pvObject)) != NULL)
		RemoveAt(Pos, bDelete);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the object pointer at the specified position
//!
//!	Parameters:	\li Pos - the position of the object in the list
//!				\li pvObject - the object pointer to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPtrList::SetAt(PTRNODE Pos, void* pvObject)
{
	if(IsValidPosition(Pos))
		Pos->pvObject = pvObject;
}




