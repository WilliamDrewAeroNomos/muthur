//------------------------------------------------------------------------------
/*! \file	MLSafePtrQueue.h
//
//  This file contains the declaration of the CSafePtrQueue class
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
#ifndef __ML_SAFE_PTR_QUEUE_H__
#define __ML_SAFE_PTR_QUEUE_H__ 

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLSafePtrList.h>

//------------------------------------------------------------------------------
//	MACROS AND CONSTANTS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class that implements a thread safe queue of object pointers
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLSafePtrQueue
{
	public:

										CMLSafePtrQueue(unsigned short usMaxPtrs = 1);
		virtual						   ~CMLSafePtrQueue();

		bool							IsFull();
		bool							IsEmpty();
		long							GetSize();
		void							Flush();
		void							SetMaxPtrs(unsigned short usMaxPtrs);
		unsigned short					GetMaxPtrs();

		bool							Put(void* pvObject);
		bool							PutFront(void* pvObject);
		void*							Get();

	private:

		CMLSafePtrList					m_aObjPtrs;			//!< The safe collection of object pointers
		unsigned short					m_usMaxPtrs;		//!< Maximum number of pointers allowed in the queue
};

#endif //__ML_SAFE_PTR_QUEUE_H__
