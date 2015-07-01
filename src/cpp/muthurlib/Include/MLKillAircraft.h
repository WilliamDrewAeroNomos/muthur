//------------------------------------------------------------------------------
/*! \file	MLKillAircraft.h
//
//  Contains declaration of the CMLKillAircraft class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-01-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

#if !defined(__ML_KILLAIRCRAFT_H__)
#define __ML_KILLAIRCRAFT_H__

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
//! \brief Class CMLKillAircraft objects are created when a user deletes or 
//! removes an aircraft. The attributes are either the unique ID taken from
//! the particular object or a match on the aircraft tail number. Since these 
//! are key attributes of the object no additional attributes are required. 
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLKillAircraft : public CMLDataObject
{
protected:
public:
	CMLKillAircraft();
	CMLKillAircraft(const CMLKillAircraft& rSource);

	virtual ~CMLKillAircraft();
	virtual void Copy(const CMLKillAircraft& rSource);
	virtual void Reset();

	EMLDataObjectClass GetDataObjectType() { return  ML_DATAOBJECT_CLASS_KILL_AC; } //!< Returns data object type

	void BuildStandardPropertyList();

	CMLPropMember*	GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool	SetMemberValues(CMLPropMember* pPropValues);
};


#endif