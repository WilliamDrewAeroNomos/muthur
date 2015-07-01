//------------------------------------------------------------------------------
/*! \file	MLAircraftData.h
//
//  Contains declaration of the CMLAircraft class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

#if !defined(__ML_AIRCRAFT_H__)
#define __ML_AIRCRAFT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataObject.h>

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
//! \brief Class CMLAircraft class represents a new aircraft that can be added
//! to the simulation. 
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLAircraft : public CMLDataObject
{
protected:
public:
	CMLAircraft();
	CMLAircraft(const CMLAircraft& rSource);
	virtual ~CMLAircraft();
	virtual void Copy(const CMLAircraft& rSource);
	virtual void Reset();

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_AIRCRAFT; } //!< Returns data object type

	CMLPropMember*	GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/
};


#endif