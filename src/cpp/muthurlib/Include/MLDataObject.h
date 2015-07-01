//------------------------------------------------------------------------------
/*! \file	MLDataObject.h
//
//  Contains declaration of the CMLDataObject class
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
#if !defined(__ML_DATAOBJECT_H__)
#define __ML_DATAOBJECT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataStream.h>
#include <MLPropMember.h>
#include <MLSerialize.h>
#include <MLPtrList.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
#define	CREATED_TIMESTAMP_MSECS_ATTRIB_NAME "objectCreateTimeMSecs"		//!< Attribute name of this create time
#define UUID_ATTR_NAME "dataObjectUUID"									//!< Attribute Name of UUID
#define	AC_TAIL_NUMBER_ATTR_NAME "tailNumber"							//!< Attribute Name of AC Tail Number
#define	AC_ID_ATTR_NAME "callSign"										//<! Attribute Name of AC ID

//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Base class from which all Muthur data classes are derived
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLDataObject : public IMLSerialize
{
protected:
	
	char							m_szDataObjectUUID[ML_MAXLEN_UUID];						//!< Unique identifier
	char							m_szACTailNumber[ML_MAXLEN_AIRCRAFT_TAIL_NUMBER];		//!< Aircraft tail number
	char							m_szACId[ML_MAXLEN_AIRCRAFT_ID];						//!< Aircraft ID or call sign
	double							m_dObjectCreateTimeMSecs;								//!< Time when the object was created

	//CMLPtrList						m_apAttributes;											//!< The list of attributes associated with this object type
	//PTRNODE							m_posAttributes;										//!< Used for iteration of attributes list
public:

	CMLDataObject();
	CMLDataObject(const CMLDataObject& rSource);
	virtual							~CMLDataObject();
	static CMLDataObject*			GetXMLTypeAsObject(char* pszType);
	static EMLDataObjectClass		GetTypeFromName(char* pszName, bool bIsCPP);

	/*virtual const CMLPtrList&		GetAttributes() const;
	virtual CMLPtrList&				GetAttributes();
	virtual const char*				GetFirstAttribute();
	virtual const char*				GetNextAttribute();
	virtual const char*				AddAttribute(const char*);*/
	
	virtual double					GetObjectCreatedTimestamp();
	virtual char*					GetDataObjectUUID();
	virtual char*					GetACTailNumber();
	virtual char*					GetACId();

	virtual EMLDataObjectClass		GetDataObjectType() = 0;								//!< Gets the Data Object Type (i.e. name of derived class)


	virtual void					SetObjectCreatedTimestamp(double);
	
	virtual void					SetDataObjectUUID(char* pszUUID);
	virtual void					SetACTailNumber(char*);
	virtual void					SetACId(char*);	

	virtual void					Reset();
	virtual void					Copy(const CMLDataObject& rSource);

	virtual const CMLDataObject&	operator = (const CMLDataObject& rSource);

	// Support for IMLSerialize interface
	virtual char*					GetXMLName(char* pszName, int iSize);
	virtual CMLPropMember*			GetXmlRoot();
	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

	virtual bool					SetMemberValues(CMLPropMember* pPropValues);
};

#endif // !defined(__ML_DATAOBJECT_H__)
