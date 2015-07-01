//------------------------------------------------------------------------------
/*! \file	MLListFEMResponse.cpp
//
//  Contains the implementation of the CMLListFEMResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLListFEMResponse.h>
#include <MLFedExecModel.h>
#include <MLListFEMResponse.h>
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
CMLListFEMResponse::CMLListFEMResponse()
{
	m_posFedExecModels = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLListFEMResponse::CMLListFEMResponse(const CMLListFEMResponse& rSource)
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
CMLListFEMResponse::~CMLListFEMResponse()
{
	m_apFedExecModels.RemoveAll(TRUE);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add an FEM to the local collection
//!
//!	Parameters:	\li pAddFEM - the FEM object to be added
//!
//!	Returns:	The FEM object added to the list
//------------------------------------------------------------------------------
CMLFedExecModel* CMLListFEMResponse::AddFedExecModel(const CMLFedExecModel& rAddFEM)
{
	CMLFedExecModel* pAddFEM = NULL;

	pAddFEM = new CMLFedExecModel(rAddFEM);
	m_apFedExecModels.AddTail(pAddFEM);

	return pAddFEM;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLListFEMResponse::Copy(const CMLListFEMResponse& rSource)
{
	PTRNODE				Pos = NULL;
	CMLFedExecModel*	pFedExecModel = NULL;

	//	Perform base class processing first
	CMLEvent::Copy(rSource);

	//	Copy all the FEM objects
	m_apFedExecModels.RemoveAll(TRUE);
	const CMLPtrList& rFedExecModels = rSource.m_apFedExecModels;
	Pos = rFedExecModels.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pFedExecModel = (CMLFedExecModel*)(rFedExecModels.GetNext(Pos))) != NULL)
		{
			AddFedExecModel(*pFedExecModel); // add a copy
		}
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of available federation execution models
//!
//!	Parameters:	None
//!
//!	Returns:	The list of FEM object pointers
//------------------------------------------------------------------------------
const CMLPtrList& CMLListFEMResponse::GetFedExecModels() const
{
	return m_apFedExecModels;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of available federation execution models
//!
//!	Parameters:	None
//!
//!	Returns:	The list of FEM object pointers
//------------------------------------------------------------------------------
CMLPtrList& CMLListFEMResponse::GetFedExecModels()
{
	return m_apFedExecModels;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLListFEMResponse& CMLListFEMResponse::operator = (const CMLListFEMResponse& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the first FEM in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first FEM object in the local collection
//------------------------------------------------------------------------------
CMLFedExecModel* CMLListFEMResponse::GetFirstFedExecModel()
{
	CMLFedExecModel* pFEM = NULL;

	m_posFedExecModels = m_apFedExecModels.GetHeadPosition();
	if(m_posFedExecModels != NULL)
	{
		pFEM = (CMLFedExecModel*)(m_apFedExecModels.GetNext(m_posFedExecModels));
	}
	
	return pFEM;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the next FEM in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The next FEM object in the local collection
//------------------------------------------------------------------------------
CMLFedExecModel* CMLListFEMResponse::GetNextFedExecModel()
{
	CMLFedExecModel* pFEM = NULL;

	if(m_posFedExecModels != NULL)
	{
		pFEM = (CMLFedExecModel*)(m_apFedExecModels.GetNext(m_posFedExecModels));
	}
	
	return pFEM;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get an XML element descriptor that defines all 
//!				properties in the specified block
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!
//!	Returns:	The element used to construct the XML stream
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLListFEMResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*		pXmlBlock = NULL;
	CMLPropMember*		pXmlFEMS = NULL;
	CMLPropMember*		pXmlChild = NULL;
	CMLFedExecModel*	pFedExecModel = NULL;

	//	Use the base class to allocate the object
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				// No additional control block properties for this derived class
				break;
			
			case ML_XML_BLOCK_DATA:

				//	Create the child to represent the FEM group
				pXmlFEMS = new CMLPropMember("fems", "");
				pXmlBlock->AddChild(pXmlFEMS);
				
				//	Now add a child for each FEM in the list
				pFedExecModel = GetFirstFedExecModel();
				while(pFedExecModel != NULL)
				{
					pXmlChild = pFedExecModel->GetXmlMember();
					if(pXmlChild != NULL)
						pXmlFEMS->AddChild(pXmlChild);
						
					pFedExecModel = GetNextFedExecModel();
				}
				
				break;

		}// switch(eXmlBlock)
	
	}

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of a class member using the specified
//!				XML element
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!				\li pMLPropMember - the element that contains the value
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLListFEMResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = true;
	CMLPropMember*		pChild = NULL;
	CMLFedExecModel*	pFedExecModel = NULL;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_DATA:

			//	Is this the FEM group?
			if(lstrcmpi(pMLPropMember->GetName(), "fems") == 0)
			{
				//	Each child is an FEM
				pChild = pMLPropMember->GetFirstChild();
				while(pChild != NULL)
				{
					pFedExecModel = new CMLFedExecModel();
					if(pFedExecModel->SetXmlValues(pChild) == true)
					{
						m_apFedExecModels.AddTail(pFedExecModel);
					}
					else
					{
						delete pFedExecModel;
					}
						
					pChild = pMLPropMember->GetNextChild();
				}
				
			}
			else
			{
				//	Let the base class handle it
				bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
			}
			break;

		case ML_XML_BLOCK_CONTROL:
		default:
			
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}

