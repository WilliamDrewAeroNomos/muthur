//------------------------------------------------------------------------------
/*! \file	TiXmlStream.h
//
//  Contains declaration of the CTiXmlStream class
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
#if !defined(__TI_XML_STREAM_H__)
#define __TI_XML_STREAM_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>

#include <tinyxml.h>	// tiny xml library headers
#include <tinystr.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------
class CMLPropMember;

//------------------------------------------------------------------------------
//! \brief Class used to create and manage a TinyXml document stream
//------------------------------------------------------------------------------
class CTiXmlStream
{
	private:
		
		TiXmlDocument					m_tiXmlDocument;
		TiXmlPrinter					m_tiPrinter;
		TiXmlElement*					m_pXmlRootNode;

	public:
			
										CTiXmlStream();
		virtual						   ~CTiXmlStream();
		
		void							Clear();

		CMLPropMember*					LoadStream(char* pszStream);
		bool							CreateStream(CMLPropMember* pMLXmlTree);
		bool							Save(char* pszFileSpec);
		char*							GetXmlString();

	private:

		TiXmlElement* 					Add(TiXmlElement* pXmlParent, CMLPropMember* pMLProp);
		CMLPropMember* 					GetMLElement(TiXmlElement* pTiXmlElement);
};

#endif // !defined(__TI_XML_STREAM_H__)
