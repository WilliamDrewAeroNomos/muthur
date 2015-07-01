//------------------------------------------------------------------------------
/*! \file	MLPropMember.cpp
//
//  Contains the implementation of the CMLPropPair class
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
#include <MLPropMember.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li pszValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, char* pszValue) :
			   CMLPropPair(pszName, pszValue)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li bValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, bool bValue) :
			   CMLPropPair(pszName, bValue)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li rMLHandle - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, CMLHandle& rMLHandle) :
			   CMLPropPair(pszName, rMLHandle)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li dValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, double dValue, int iPrecision) :
			   CMLPropPair(pszName, dValue, iPrecision)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li fValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, float fValue, int iPrecision) :
			   CMLPropPair(pszName, fValue, iPrecision)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li iValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, int iValue) :
			   CMLPropPair(pszName, iValue)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li lValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, long lValue) :
			   CMLPropPair(pszName, lValue)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li ulValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(char* pszName, unsigned long ulValue) :
			   CMLPropPair(pszName, ulValue)
{
	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::CMLPropMember(const CMLPropMember& rSource)
{
	Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropMember::~CMLPropMember()
{
	Clear(TRUE); // Flush the collections and destroy the objects
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li bValue - the value to be assigned to the element
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, bool bValue)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, bValue);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li pszValue - the value to be assigned to the element
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, char* pszValue)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, pszValue);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li rMLHandle - the value to be assigned to the element
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, CMLHandle& rMLHandle)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, rMLHandle);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li dValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, double dValue, int iPrecision)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, dValue, iPrecision);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li fValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, float fValue, int iPrecision)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, fValue, iPrecision);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li iValue - the value to be assigned
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, int iValue)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, iValue);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li lValue - the value to be assigned
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(char* pszName, long lValue)
{
	CMLPropPair* pAttribute = new CMLPropPair(pszName, lValue);
	return AddAttribute(pAttribute);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li pAttribute - the Attribute to be added
//!
//!	Returns:	The Attribute object specified by the caller
//!
//!	Remarks:	This method is reserved for internal library use. The object
//!				passed by the caller will be destroyed when the list 
//!				container is destroyed.
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(CMLPropPair* pAttribute)
{
	assert(pAttribute != NULL);

	if(pAttribute != NULL)
		m_apAttributes.AddTail(pAttribute);

	return pAttribute;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an entry to the collection of Attribute nodes
//!
//!	Parameters:	\li rAttribute - the Attribute to be added
//!
//!	Returns:	The Attribute object added to the collection
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::AddAttribute(const CMLPropPair& rAttribute)
{
	CMLPropPair* pAttribute = NULL;

	pAttribute = new CMLPropPair(rAttribute);
	m_apAttributes.AddTail(pAttribute);

	return pAttribute;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li bValue - the value to be assigned to the element
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, bool bValue)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, bValue);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li pszValue - the value to be assigned to the element
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, char* pszValue)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, pszValue);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li rMLHandle - the value to be assigned to the element
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, CMLHandle& rMLHandle)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, rMLHandle);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li dValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, double dValue, int iPrecision)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, dValue, iPrecision);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li fValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, float fValue, int iPrecision)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, fValue, iPrecision);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li iValue - the value to be assigned
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, int iValue)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, iValue);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li lValue - the value to be assigned
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, long lValue)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, lValue);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pszName - the name used to identify the element
//!				\li ulValue - the value to be assigned
//!
//!	Returns:	The new element object
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(char* pszName, unsigned long ulValue)
{
	CMLPropMember* pChild = new CMLPropMember(pszName, ulValue);
	return AddChild(pChild);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li pChild - the child to be added
//!
//!	Returns:	The child object specified by the caller
//!
//!	Remarks:	This method is reserved for internal library use. The object
//!				passed by the caller will be destroyed when the list 
//!				container is destroyed.
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(CMLPropMember* pChild)
{
	assert(pChild != NULL);

	if(pChild != NULL)
		m_apChildren.AddTail(pChild);

	return pChild;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an element to the collection of child elements
//!
//!	Parameters:	\li rChild - the child to be added
//!
//!	Returns:	The object added to the collection
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::AddChild(const CMLPropMember& rChild)
{
	CMLPropMember* pChild = NULL;

	pChild = new CMLPropMember(rChild);
	m_apChildren.AddTail(pChild);

	return pChild;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to clear the child elements and attributes
//!
//!	Parameters:	\li bDelete - true to delete the objects in the collections
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropMember::Clear(bool bDelete)
{
	PTRNODE			Pos = NULL;
	CMLPropMember*	pChild = NULL;
	CMLPropPair*		pAttribute = NULL;

	//	Clear all the child elements
	Pos = m_apChildren.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pChild = (CMLPropMember*)m_apChildren.GetNext(Pos)) != NULL)
		{
			pChild->Clear(bDelete);
			if(bDelete == true)
				delete pChild;
		}

	}

	//	Clear all the attributes
	Pos = m_apAttributes.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pAttribute = (CMLPropPair*)m_apAttributes.GetNext(Pos)) != NULL)
		{
			pAttribute->Clear();
			if(bDelete == true)
				delete pAttribute;
		}

	}

	m_apChildren.RemoveAll(FALSE);
	m_apAttributes.RemoveAll(FALSE);

	m_posChildren = NULL;
	m_posAttributes = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropMember::Copy(const CMLPropMember& rSource)
{
	PTRNODE			Pos = NULL;
	CMLPropMember*	pChild = NULL;
	CMLPropPair*	pAttribute = NULL;

	Clear(true); // clear the existing collections

	CMLPropPair::Copy(rSource); // base class copy

	//	Copy all the child elements
	const CMLPtrList& rChildren = rSource.m_apChildren;
	Pos = rChildren.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pChild = (CMLPropMember*)(rChildren.GetNext(Pos))) != NULL)
		{
			AddChild(new CMLPropMember(*pChild));
		}
	}

	//	Copy all the attributes
	const CMLPtrList& rAttributes = rSource.m_apAttributes;
	Pos = rAttributes.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pAttribute = (CMLPropPair*)(rAttributes.GetNext(Pos))) != NULL)
		{
			AddAttribute(new CMLPropPair(*pAttribute));
		}
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the attribute with the specified name
//!
//!	Parameters:	\li pszName - the name of the attribute
//!
//!	Returns:	The attribute that matches the requested name
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::FindAttribute(char* pszName)
{
	PTRNODE			Pos = NULL;
	CMLPropPair*	pAttribute = NULL;

	Pos = m_apAttributes.GetHeadPosition();
	if(Pos != NULL)
	{
		pAttribute = (CMLPropPair*)(m_apAttributes.GetNext(Pos));
	
		if(lstrcmpi(pAttribute->GetName(), pszName) == 0)
			return pAttribute;
	}
	
	return pAttribute; // not found
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the child with the specified name
//!
//!	Parameters:	\li pszName - the name of the child
//!
//!	Returns:	The child that matches the requested name
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::FindChild(char* pszName)
{
	PTRNODE			Pos = NULL;
	CMLPropMember*	pChild = NULL;

	Pos = m_apChildren.GetHeadPosition();
	if(Pos != NULL)
	{
		pChild = (CMLPropMember*)(m_apChildren.GetNext(Pos));
	
		if(lstrcmpi(pChild->GetName(), pszName) == 0)
			return pChild;
	}
	
	return NULL; // not found
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the collection of attribute nodes
//!
//!	Parameters:	None
//!
//!	Returns:	The collection of attributes for this element
//------------------------------------------------------------------------------
const CMLPtrList& CMLPropMember::GetAttributes() const
{
	return m_apAttributes;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the collection of attribute nodes
//!
//!	Parameters:	None
//!
//!	Returns:	The collection of attributes for this element
//------------------------------------------------------------------------------
CMLPtrList& CMLPropMember::GetAttributes()
{
	return m_apAttributes;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the collection of child elements
//!
//!	Parameters:	None
//!
//!	Returns:	The child elements collection
//------------------------------------------------------------------------------
const CMLPtrList& CMLPropMember::GetChildren() const
{
	return m_apChildren;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the collection of child elements
//!
//!	Parameters:	None
//!
//!	Returns:	The child elements collection
//------------------------------------------------------------------------------
CMLPtrList& CMLPropMember::GetChildren()
{
	return m_apChildren;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLPropMember& CMLPropMember::operator = (const CMLPropMember& rSource)
{
	Copy(rSource);															
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the first attribute node
//!
//!	Parameters:	None
//!
//!	Returns:	The first attribute node in the collection
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::GetFirstAttribute()
{
	CMLPropPair* pAttribute = NULL;

	m_posAttributes = m_apAttributes.GetHeadPosition();
	if(m_posAttributes != NULL)
	{
		pAttribute = (CMLPropPair*)(m_apAttributes.GetNext(m_posAttributes));
	}
	return pAttribute;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the first child element
//!
//!	Parameters:	None
//!
//!	Returns:	The first child element in the collection
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::GetFirstChild()
{
	CMLPropMember* pChild = NULL;

	m_posChildren = m_apChildren.GetHeadPosition();
	if(m_posChildren != NULL)
	{
		pChild = (CMLPropMember*)(m_apChildren.GetNext(m_posChildren));
	}
	return pChild;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the next attribute
//!
//!	Parameters:	None
//!
//!	Returns:	The next attribute in the collection
//------------------------------------------------------------------------------
CMLPropPair* CMLPropMember::GetNextAttribute()
{
	CMLPropPair* pAttribute = NULL;
	if(m_posAttributes != NULL)
	{
		pAttribute = (CMLPropPair*)(m_apAttributes.GetNext(m_posAttributes));
	}
	return pAttribute;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the next child element
//!
//!	Parameters:	None
//!
//!	Returns:	The next child element in the collection
//------------------------------------------------------------------------------
CMLPropMember* CMLPropMember::GetNextChild()
{
	CMLPropMember* pChild = NULL;
	if(m_posChildren != NULL)
	{
		pChild = (CMLPropMember*)(m_apChildren.GetNext(m_posChildren));
	}
	return pChild;
}

