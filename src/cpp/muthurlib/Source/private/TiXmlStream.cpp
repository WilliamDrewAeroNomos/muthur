//------------------------------------------------------------------------------
/*! \file	TiXmlStream.cpp
//
//  Contains the implementation of the CTiXmlStream class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <TiXmlStream.h>
#include <MLPropMember.h>
#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CReporter _theReporter; //!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CTiXmlStream::CTiXmlStream()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CTiXmlStream::~CTiXmlStream()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a node to the stream using the descriptor 
//!				specified by the caller
//!
//!	Parameters:	\li pXmlParent - the parent node
//!				\li pMLProp - the Muthur descriptor
//!
//!	Returns:	the new TinyXml element object
//------------------------------------------------------------------------------
TiXmlElement* CTiXmlStream::Add(TiXmlElement* pTiXmlParent, CMLPropMember* pMLProp)
{
	TiXmlElement*	pTiXmlChild = NULL;
	CMLPropPair*	pAttribute = NULL;
	CMLPropMember*	pChild = NULL;
	bool			bSuccessful = false;

	try
	{
		if((pMLProp != NULL) && (lstrlen(pMLProp->GetName()) > 0))
		{
			//	Create the node for the new element
			pTiXmlChild = new TiXmlElement(pMLProp->GetName());
			if(lstrlen(pMLProp->GetValue()) > 0)
				pTiXmlChild->LinkEndChild(new TiXmlText(pMLProp->GetValue()));

			//	Add to the the parent's child collection 
			//
			//	NOTE: This will be NULL if adding the root node
			if(pTiXmlParent != NULL)
			{
				pTiXmlParent->LinkEndChild(pTiXmlChild);
			}

			//	Add the attributes for this element
			pAttribute = pMLProp->GetFirstAttribute();
			while(pAttribute != NULL)
			{
				pTiXmlChild->SetAttribute(pAttribute->GetName(), pAttribute->GetValue());
				pAttribute = pMLProp->GetNextAttribute();
			}

			//	Add the children for this element
			pChild = pMLProp->GetFirstChild();
			while(pChild != NULL)
			{
				Add(pTiXmlChild, pChild);
				pChild = pMLProp->GetNextChild();
			}

			bSuccessful = true;

		}// if((pXmlParent != NULL) && (pMLProp != NULL))
	}
	catch(char *pException)
	{
		_theReporter.Debug("", "Add", "Exception raised adding the XML elment : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "Add", "Exception raised adding the XML elment");
	}

	return bSuccessful ? pTiXmlChild : NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to clear the existing stream
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CTiXmlStream::Clear()
{
	try
	{
		m_pXmlRootNode = NULL;
		m_tiXmlDocument.Clear();
	}
	catch(char *pException)
	{
		_theReporter.Debug("", "Clear", "Exception raised clearing XML stream : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "Clear", "Exception raised clearing XML stream");
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to create the XML stream
//!
//!	Parameters:	\li pMLXmlTree - the MutherLib XML tree descriptor
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CTiXmlStream::CreateStream(CMLPropMember* pMLXmlTree)
{
	bool bSuccessful = false;

	try
	{
		TiXmlDeclaration*	pXmlDeclaration = NULL;
		TiXmlElement*		pXmlElement = NULL;
		CMLPropMember*		pChild = NULL;

		//	Clear the existing document
		Clear();

		if(lstrcmpi(pMLXmlTree->GetName(), "NULL") != 0)
		{
			pXmlDeclaration = new TiXmlDeclaration( "1.0", "UTF-8", "");
			m_tiXmlDocument.LinkEndChild(pXmlDeclaration);

			//	Create the root element
			if((m_pXmlRootNode = Add(NULL, pMLXmlTree)) != NULL)
			{
				m_tiXmlDocument.LinkEndChild(m_pXmlRootNode);
			}
			
			bSuccessful = true;
		}
		else
		{
			pChild = pMLXmlTree->GetFirstChild();
			while(pChild != NULL)
			{
				//	Create the root element
				if((pXmlElement = Add(NULL, pChild)) != NULL)
				{
					m_tiXmlDocument.LinkEndChild(pXmlElement);
				}
		
				pChild = pMLXmlTree->GetNextChild();
			}
			
			bSuccessful = true;
		}
	}
	catch(char *pException)
	{
		_theReporter.Debug("", "CreateStream", "Exception raised creating XML stream : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "CreateStream", "Exception raised creating XML stream");
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a MuthurLib element from the specified TinyXml
//!				element
//!
//!	Parameters:	\li pTiSource - the TinyXml element used as the source
//!
//!	Returns:	The representative MutherLib element
//------------------------------------------------------------------------------
CMLPropMember* CTiXmlStream::GetMLElement(TiXmlElement* pTiSource)
{
	CMLPropMember*	pMLElement = NULL;
	CMLPropMember*	pMLChild = NULL;
	TiXmlAttribute*	pTiAttribute = NULL;
	TiXmlNode*		pTiChild = NULL;

	try
	{
		pMLElement = new CMLPropMember((char*)pTiSource->Value(), (char*)pTiSource->GetText());

		//	Add the attributes
		pTiAttribute = pTiSource->FirstAttribute();
		while(pTiAttribute != NULL)
		{
			pMLElement->AddAttribute(new CMLPropPair((char*)pTiAttribute->Name(), (char*)pTiAttribute->Value()));
			pTiAttribute = pTiAttribute->Next();
		}

		//	Add the child elements
		pTiChild = pTiSource->FirstChild();
		while(pTiChild != NULL)
		{
			switch(pTiChild->Type())
			{
				case TiXmlNode::TINYXML_ELEMENT:

					if((pMLChild = GetMLElement((TiXmlElement*)pTiChild)) != NULL)
					{
						pMLElement->AddChild(pMLChild);
					}
					else
					{
						_theReporter.Debug("", "GetMLElement", "Unable to add child node named <%s>", pTiChild->Value());
					}
					break;

				case TiXmlNode::TINYXML_TEXT:
				
					// Ignore text nodes because we retrieved the text when we allocated
					// the MLElement object by calling GetText()
					break;

				case TiXmlNode::TINYXML_DECLARATION:
				case TiXmlNode::TINYXML_COMMENT:
				case TiXmlNode::TINYXML_DOCUMENT:
				case TiXmlNode::TINYXML_UNKNOWN:
				default:
					break;
			
			}

			pTiChild = pTiChild->NextSibling();
		
		}// switch(pTiChild->Type())
		
	}
	catch(char *pException)
	{
		_theReporter.Debug("", "GetMLElement", "Exception raised reading TinyXml element : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "GetMLElement", "Exception raised reading TinyXml element");
	}

	return pMLElement;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the XML text for the active document
//!
//!	Parameters:	None
//!
//!	Returns:	The XML text as a null terminated string
//------------------------------------------------------------------------------
char* CTiXmlStream::GetXmlString()
{
	try
	{
		m_tiPrinter.SetIndent( "\t" );
		//m_tiPrinter.SetStreamPrinting();

		m_tiXmlDocument.Accept(&m_tiPrinter);
		
		return ((char*)(m_tiPrinter.CStr()));
	}
	catch(char *pException)
	{
		_theReporter.Debug("", "GetXmlString", "Exception raised retrieving XML text : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "GetXmlString", "Exception raised retrieving XML text");
	}

	return NULL; // must have been an error if we made it to this point
}

//------------------------------------------------------------------------------
//!	Summary:	Called to load the requested XML stream
//!
//!	Parameters:	\li pszStream - the stream to be loaded
//!
//!	Returns:	The MuthurLib XML tree parsed from the stream
//------------------------------------------------------------------------------
CMLPropMember* CTiXmlStream::LoadStream(char* pszStream)
{
	CMLPropMember* pMLElement = NULL;
	
	try
	{
		//	Clear the existing document
		Clear();

		m_tiXmlDocument.Parse((const char*)pszStream, 0, TIXML_ENCODING_UNKNOWN);

		if((m_pXmlRootNode = m_tiXmlDocument.RootElement()) != NULL)
		{
			pMLElement = GetMLElement(m_pXmlRootNode);
		}
		else
		{
			_theReporter.Debug("", "LoadStream", "Unable to find root node in XML stream:\n\n%s\n", pszStream);
		}

	}
	catch(char *pException)
	{
		_theReporter.Debug("", "LoadStream", "Exception raised while loading XML stream : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "LoadStream", "Exception raised while loading XML stream");
	}

	return pMLElement;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to save the XML text to the specified file
//!
//!	Parameters:	\li pszFileSpec - fully qualified path specification to the file
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CTiXmlStream::Save(char* pszFileSpec)
{
	bool bSuccessful = false;

	try
	{
		//	Make sure the text is properly formatted
		if(GetXmlString() != NULL)
		{
			m_tiXmlDocument.SaveFile(pszFileSpec);
			bSuccessful = true;
		}
	}
	catch(char *pException)
	{
		_theReporter.Debug("", "Save", "Exception raised saving XML text to %s : <%s>", pszFileSpec, pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "Save", "Exception raised saving XML text to %s", pszFileSpec);
	}

	return bSuccessful; 
}

/*
CMLPropMember* CTiXmlStream::LoadStreamX(char* pszStream)
{
	CMLPropMember* pMLElement = NULL;
FILE* fptr = NULL;
fptr = fopen("f:\\muthur_packed_xml.xml", "at");
fprintf(fptr, "\n%s\n", pszStream);
fclose(fptr);	
	try
	{
		//	Clear the existing document
		Clear();

		m_tiXmlDocument.Parse((const char*)pszStream, 0, TIXML_ENCODING_UNKNOWN);
		
		//if(m_tiXmlDocument.Parse((const char*)pszStream, 0, TIXML_ENCODING_UNKNOWN) != NULL)
		//{
			if((m_pXmlRootNode = m_tiXmlDocument.RootElement()) != NULL)
			{
				pMLElement = GetMLElement(m_pXmlRootNode);
			}
			else
			{
				_theReporter.Debug("", "LoadStream", "Unable to find root node in XML stream:\n\n%s\n", pszStream);
			}
		//}
		//else
		//{
		//	_theReporter.Debug("", "LoadStream", "Failed to parse XML stream:\n\n%s\n", pszStream);
		//}

	}
	catch(char *pException)
	{
		_theReporter.Debug("", "LoadStream", "Exception raised while loading XML stream : <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "LoadStream", "Exception raised while loading XML stream");
	}

	return pMLElement;
}
*/
