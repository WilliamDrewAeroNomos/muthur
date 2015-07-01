//------------------------------------------------------------------------------
/*! \file	MLSafePtrQueue.cpp
//
//  This file contains the implementation of the CMLSafePtrQueue class
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
#include <stdafx.h>
#include <assert.h>
#include <MLSafePtrQueue.h>

//------------------------------------------------------------------------------
//	CONSTANTS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	usMaxPtrs - the maximum number of pointers to be queued
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSafePtrQueue::CMLSafePtrQueue(unsigned short usMaxPtrs)
{
	m_usMaxPtrs = usMaxPtrs;
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSafePtrQueue::~CMLSafePtrQueue()
{
	Flush();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to flush the contents of the queue
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrQueue::Flush()
{
	m_aObjPtrs.RemoveAll(true);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the first pointer in the queue
//!
//!	Parameters:	None
//!
//!	Returns:	The first object pointer in the queue
//------------------------------------------------------------------------------
void* CMLSafePtrQueue::Get()
{
	void* pvObject = NULL;
	
	if(GetSize() > 0)
	{
		pvObject = m_aObjPtrs.RemoveHead(false);
	}
	
	return pvObject;		
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the max number of pointers allowed in the queue
//!
//!	Parameters:	None
//!
//!	Returns:	The maximum number of pointers
//------------------------------------------------------------------------------
unsigned short CMLSafePtrQueue::GetMaxPtrs()
{
	return m_usMaxPtrs;		
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the number of pointers in the queue
//!
//!	Parameters:	None
//!
//!	Returns:	The number of pointers in the queue
//------------------------------------------------------------------------------
long CMLSafePtrQueue::GetSize()
{
	return m_aObjPtrs.GetCount();		
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the queue is empty
//!
//!	Parameters:	None
//!
//!	Returns:	true if empty
//------------------------------------------------------------------------------
bool CMLSafePtrQueue::IsEmpty()
{
	return (GetSize() == 0);		
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the queue is full
//!
//!	Parameters:	None
//!
//!	Returns:	true if full
//------------------------------------------------------------------------------
bool CMLSafePtrQueue::IsFull()
{
	return (GetSize() >= m_usMaxPtrs);		
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add the specified pointer at the end of the queue
//!
//!	Parameters:	pvObject = the pointer to be added
//!
//!	Returns:	TRUE if successful
//!	
//!	Remarks:	If the queue is full when this method is called, the oldest 
//				pointer in the queue is removed to make room for this pointer
//------------------------------------------------------------------------------
bool CMLSafePtrQueue::Put(void* pvObject)
{
	if(IsFull() == TRUE)
	{
		m_aObjPtrs.RemoveHead(true);
	}

	return (m_aObjPtrs.AddTail(pvObject) != NULL);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add the specified pointer at the front of the queue
//!
//!	Parameters:	pvObject = the pointer to be added
//!
//!	Returns:	TRUE if successful
//!	
//!	Remarks:	If the queue is full when this method is called, the last 
//				pointer in the queue is removed to make room for this pointer
//------------------------------------------------------------------------------
bool CMLSafePtrQueue::PutFront(void* pvObject)
{
	if(IsFull() == TRUE)
	{
		m_aObjPtrs.RemoveTail(true);
	}
	
	return (m_aObjPtrs.AddHead(pvObject) != NULL);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the maximum number of pointers allowed in the queue
//!
//!	Parameters:	\li usMaxPtrs - the maximum number of pointers
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSafePtrQueue::SetMaxPtrs(unsigned short usMaxPtrs)
{
	//	Just checking ....
	assert(usMaxPtrs > 0);
	if(usMaxPtrs == 0)
		usMaxPtrs = 2; // Assign default just in case
	
	m_usMaxPtrs = usMaxPtrs;
	
	//	The queue may have gotten smaller so we need to remove any pointers
	//	that exceed the limit
	while(IsFull() == TRUE)
	{
		m_aObjPtrs.RemoveAt(0, true);
	}
	
}

