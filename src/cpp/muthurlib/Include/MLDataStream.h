//------------------------------------------------------------------------------
/*! \file	MLDataStream.h
//
//  Contains declaration of the CMLDataStream class
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
#if !defined(__ML_DATASTREAM_H__)
#define __ML_DATASTREAM_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <string>

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
//! \brief Class used to serialize CMLDataObject derived classes
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLDataStream
{
	protected:
	
		bool							m_bRead;		//!< true if reading object data, false if writing object data
		char*							m_pBuffer;		//!< the buffer containing the data stream
		unsigned long					m_ulSize;		//!< the size of the data stream in bytes


	public:
			
										CMLDataStream();
										CMLDataStream(const CMLDataStream& rSource);
						
		virtual						   ~CMLDataStream();
			virtual void					FreeBuffer();
	
		virtual bool					GetRead();
		virtual char*					GetBuffer();
		virtual unsigned long			GetSize();
		
		virtual void					SetRead(bool bRead);
		virtual void					SetStream(char* pBuffer, unsigned long ulSize = 0);
		
		virtual void					Copy(const CMLDataStream& rSource);
		virtual const CMLDataStream&	operator = (const CMLDataStream& rSource);
};

#endif // !defined(__ML_DATASTREAM_H__)
