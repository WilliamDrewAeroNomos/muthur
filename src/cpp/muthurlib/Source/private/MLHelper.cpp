//------------------------------------------------------------------------------
/*! \file	MLHelper.cpp
//
//  Contains the implementation of the CMLHelper class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>07-11-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLHelper.h>
#include <TiXmlStream.h>	// Tiny XML wrapper


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
CMLHelper::CMLHelper()
{
}		

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLHelper::~CMLHelper()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the Muthurlib XML tree for the specified object
//!
//!	Parameters:	\li pISerialize - the object being serialized
//!
//!	Returns:	the root element in the property tree
//!
//!	Remarks:	The caller is responsible for deallocation of the return object
//------------------------------------------------------------------------------
CMLPropMember* CMLHelper::GetPropTree(IMLSerialize* pISerialize)
{
	CMLPropMember*  pXmlRoot = NULL;
	CMLPropMember*  pXmlBlock = NULL;
	bool			bSuccessful = false;

	assert(pISerialize != NULL);

	try
	{
		while(bSuccessful == false)
		{
			if(pISerialize == NULL)
			{
				_theReporter.Debug("", "GetPropTree", "Invalid pISerialize parameter");
				break;
			}

			//	Create the root node
			if((pXmlRoot = pISerialize->GetXmlRoot()) == NULL)
			{
				_theReporter.Debug("", "GetPropTree", "Failed to create XML root node");
				break;
			}

			//	Add the control block
			if((pXmlBlock = pISerialize->GetXmlBlock(ML_XML_BLOCK_CONTROL)) != NULL)
			{
				pXmlRoot->AddChild(pXmlBlock);
			}
			else
			{
				_theReporter.Debug("", "GetPropTree", "Failed to create XML block for %s", ML_XML_BLOCK_NAME_CONTROL);
				break;
			}

			//	Add the data block
			if((pXmlBlock = pISerialize->GetXmlBlock(ML_XML_BLOCK_DATA)) != NULL)
			{
				pXmlRoot->AddChild(pXmlBlock);
			}
			else
			{
				_theReporter.Debug("", "GetPropTree", "Failed to create XML block for %s", ML_XML_BLOCK_NAME_DATA);
				break;
			}
		
			//	Add the error block
			if((pXmlBlock = pISerialize->GetXmlBlock(ML_XML_BLOCK_ERROR)) != NULL)
			{
				pXmlRoot->AddChild(pXmlBlock);
			}
			else
			{
				// Not all streams have this block
			}
		
			bSuccessful = true; // it's all good
		
		}// while(bSuccessful == false)

		// Clean up if necessary
		if((bSuccessful == false) && (pXmlRoot != NULL))
		{
			delete pXmlRoot;
			pXmlRoot = NULL;
		}

	}
	catch(char *pException)
	{
		_theReporter.Debug("", "GetPropTree", "Exception raised constructing the XML tree: <%s>", pException ? pException : "NULL");
	}
	catch(...)
	{
		_theReporter.Debug("", "GetPropTree", "Exception raised constructing the XML tree");
	}

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the C++ class name that is associated with the
//!				specified Muthur class name
//!
//!	Parameters:	\li pszMuthurClassName - the Muthur class name
//!
//!	Returns:	the associated C++ class name if there is one
//------------------------------------------------------------------------------
const char* CMLHelper::GetCPPClassName(const char* pszMuthurClassName)
{
	if(lstrcmpi("FlightPosition", pszMuthurClassName) == 0)
	{
		return "CMLFlightPosition";
	}
	else if(lstrcmpi("Aircraft", pszMuthurClassName) == 0)
	{
		return "CMLAircraft";
	}
	else if(lstrcmpi("AircraftArrival", pszMuthurClassName) == 0)
	{
		return "CMLAircraftArrivalData";
	}
	else if(lstrcmpi("FlightPlan", pszMuthurClassName) == 0)
	{
		return "CMLFlightPlan";
	}
	/*else if(lstrcmpi("KillAircraftData", pszMuthurClassName) == 0)
	{
		return "CMLKillAircraft";
	}*/
	else if(lstrcmpi("AircraftTaxiOut", pszMuthurClassName) == 0)
	{
		return "CMLAircraftTaxiOut";
	}
	else if(lstrcmpi("AircraftTaxiIn", pszMuthurClassName) == 0)
	{
		return "CMLAircraftTaxiIn";
	}
	else if(lstrcmpi("AircraftDeparture", pszMuthurClassName) == 0)
	{
		return "CMLAircraftDepartureData";
	}
	else
	{
		return pszMuthurClassName;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the Muthur class name that is associated with the
//!				specified typeid C++ class name (i.e. returned from typeid(OBJECT).name())
//!
//!	Parameters:	\li pszCPPClassName - the C++ class name
//!
//!	Returns:	the associated Muthur class name if there is one
//------------------------------------------------------------------------------
const char* CMLHelper::GetMuthurClassNameFromCppTypeId(const char* pszCPPClassName)
{
	if(lstrcmpi("class CMLFlightPosition", pszCPPClassName) == 0)
	{
		return "FlightPosition";
	}
	else if(lstrcmpi("class CMLAircraft", pszCPPClassName) == 0)
	{
		return "Aircraft";
	}
	else if(lstrcmpi("class CMLAircraftArrivalData", pszCPPClassName) == 0)
	{
		return "AircraftArrival";
	}
	else if(lstrcmpi("class CMLFlightPlan", pszCPPClassName) == 0)
	{
		return "FlightPlan";
	}
	/*else if(lstrcmpi("class CMLKillAircraft", pszCPPClassName) == 0)
	{
		return "KillAircraftData";
	}*/
	else if(lstrcmpi("class CMLAircraftTaxiOut", pszCPPClassName) == 0)
	{
		return "AircraftTaxiOut";
	}
	else if(lstrcmpi("class CMLAircraftTaxiIn", pszCPPClassName) == 0)
	{
		return "AircraftTaxiIn";
	}
	else if(lstrcmpi("class CMLAircraftDepartureData", pszCPPClassName) == 0)
	{
		return "AircraftDeparture";
	}
	else
	{
		return pszCPPClassName;
	}
}


//------------------------------------------------------------------------------
//!	Summary:	Called to get the Muthur class name that is associated with the
//!				specified C++ class name
//!
//!	Parameters:	\li pszCPPClassName - the C++ class name
//!
//!	Returns:	the associated Muthur class name if there is one
//------------------------------------------------------------------------------
const char* CMLHelper::GetMuthurClassName(const char* pszCPPClassName)
{
	if(lstrcmpi("CMLFlightPosition", pszCPPClassName) == 0)
	{
		return "FlightPosition";
	}
	else if(lstrcmpi("CMLAircraft", pszCPPClassName) == 0)
	{
		return "Aircraft";
	}
	else if(lstrcmpi("CMLAircraftArrivalData", pszCPPClassName) == 0)
	{
		return "AircraftArrival";
	}
	else if(lstrcmpi("CMLFlightPlan", pszCPPClassName) == 0)
	{
		return "FlightPlan";
	}
	/*else if(lstrcmpi("CMLKillAircraft", pszCPPClassName) == 0)
	{
		return "KillAircraftData";
	}*/
	else if(lstrcmpi("CMLAircraftTaxiOut", pszCPPClassName) == 0)
	{
		return "AircraftTaxiOut";
	}
	else if(lstrcmpi("CMLAircraftTaxiIn", pszCPPClassName) == 0)
	{
		return "AircraftTaxiIn";
	}
	else if(lstrcmpi("CMLAircraftDepartureData", pszCPPClassName) == 0)
	{
		return "AircraftDeparture";
	}
	else
	{
		return pszCPPClassName;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the Muthurlib XML tree from the specified XML
//!				stream
//!
//!	Parameters:	\li pXmlStream - the XML text stream
//!				\li pMLTree - the root element of the MuthurLib XML tree
//!
//!	Returns:	the root element in the property tree
//!
//!	Remarks:	The caller is responsible for deallocation of the return object
//------------------------------------------------------------------------------
CMLPropMember* CMLHelper::GetPropTree(char* pXmlStream)
{
	CTiXmlStream	tiXmlStream;
	CMLPropMember*	pXmlTree = NULL;
	
	assert(pXmlStream != NULL);
	
	if(pXmlStream != NULL)
	{
//		_theReporter.Debug("MLHelper", "GetPropTree", "pXmlStream:%s", pXmlStream);
		pXmlTree = tiXmlStream.LoadStream(pXmlStream);
	}

	return pXmlTree;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the XML stream used to represent property tree
//!
//!	Parameters:	\li pMLPropTree - the root of the MutherLib property tree
//!				\li pStream - buffer to contain the XML stream
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLHelper::GetXmlStream(CMLPropMember* pMLPropTree, CMLDataStream* pStream)
{
	CTiXmlStream	tiXmlStream;
	char*			pszTiString = NULL;
	char*			pszXmlStream = NULL;
	bool			bSuccessful = false;
	
	assert(pMLPropTree != NULL);
	assert(pStream != NULL);

	if((pMLPropTree != NULL) && (pStream != NULL))
	{
		if(tiXmlStream.CreateStream(pMLPropTree) == true)
		{
			pszTiString = tiXmlStream.GetXmlString();

			if((pszTiString != NULL) && (lstrlen(pszTiString) > 0))
			{
				pStream->SetStream(pszTiString);
				bSuccessful = true;
			}

		}

	}// if((pMLPropTree != NULL) && (pStream != NULL))

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the XML stream used to represent the object
//!
//!	Parameters:	\li pISerialize - the object to be streamed
//!				\li pStream - buffer to contain the XML stream
//!
//!	Returns:	The buffer containing the XML stream
//------------------------------------------------------------------------------
bool CMLHelper::GetXmlStream(IMLSerialize* pISerialize, CMLDataStream* pStream)
{
	CMLPropMember*	pPropTree = NULL;
	bool			bSuccessful = false;
	
	assert(pISerialize != NULL);
	assert(pStream != NULL);

	if((pISerialize != NULL) && (pStream != NULL))
	{
		//	Get the property tree used to represent the object
		if((pPropTree = GetPropTree(pISerialize)) != NULL)
		{
			bSuccessful = GetXmlStream(pPropTree, pStream);
			
			delete pPropTree;
		}
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to load the MutherLib property tree extracted from the
//!				serialized stream.
//!
//!	Parameters:	\li pISerialize - the object being serialized
//!				\li pMLPropTree - the tree created from the XML stream
//!
//!	Returns:	true if all nodes processed without error
//------------------------------------------------------------------------------
bool CMLHelper::LoadPropTree(IMLSerialize* pISerialize, CMLPropMember* pMLPropTree)
{
	CMLPropMember*	pChild;
	bool			bSuccessful = true;

	assert(pISerialize != NULL);
	assert(pMLPropTree != NULL);

	//	The caller's element should be the root. The child collection should
	//	contain the nodes for each or our primary XML blocks
	_theReporter.Debug("CMLHelper", "LoadPropTree", "debug1");
	if((pISerialize != NULL) && (pMLPropTree != NULL))
	{
		_theReporter.Debug("CMLHelper", "LoadPropTree", "debug2");
		pChild = pMLPropTree->GetFirstChild();
		while(pChild != NULL)
		{
			_theReporter.Debug("CMLHelper", "LoadPropTree", "debug3");
			if(lstrcmpi(ML_XML_BLOCK_NAME_CONTROL, pChild->GetName()) == 0)
			{
				_theReporter.Debug("CMLHelper", "LoadPropTree", "debug4");
				if(LoadXmlBlock(pISerialize, ML_XML_BLOCK_CONTROL, pChild) == false)
					bSuccessful = false;
			}
			
			else if(lstrcmpi(ML_XML_BLOCK_NAME_DATA, pChild->GetName()) == 0)
			{
				_theReporter.Debug("CMLHelper", "LoadPropTree", "debug5");
				if(LoadXmlBlock(pISerialize, ML_XML_BLOCK_DATA, pChild) == false)
					bSuccessful = false;
			}
			
			else if(lstrcmpi(ML_XML_BLOCK_NAME_ERROR, pChild->GetName()) == 0)
			{
				_theReporter.Debug("CMLHelper", "LoadPropTree", "debug6");
				if(LoadXmlBlock(pISerialize, ML_XML_BLOCK_ERROR, pChild) == false)
					bSuccessful = false;
			}
			
			else
			{
				_theReporter.Debug("", "LoadPropTree", "Uknown XML block: <%s>", pChild->GetName());
				bSuccessful = false;
			}

			pChild = pMLPropTree->GetNextChild();
		
		}// while(pChild != NULL)

	}// if(pMLPropTree != NULL)

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of all class members in the specified
//!				block
//!
//!	Parameters:	\li pISerialize - the object being serialized
//!				\li eXmlBlock - the enumerated block identifier
//!				\li pParent - the element that contains the child values
//!
//!	Returns:	true if all child elements are processed
//------------------------------------------------------------------------------
bool CMLHelper::LoadXmlBlock(IMLSerialize* pISerialize, EMLXMLBlock eXmlBlock, CMLPropMember* pParent)
{
	CMLPropMember*	pChild = NULL;
	bool			bSuccessful = true;

	assert(pISerialize != NULL);
	assert(pParent != NULL);

	//	The caller's element should be the block identifier node (e.g. <controlBlock> ... </controlBlock>)
	_theReporter.Debug("CMLHelper", "LoadXmlBlock", "debug1");
	if((pISerialize != NULL) && (pParent != NULL))
	{
		_theReporter.Debug("CMLHelper", "LoadXmlBlock", "debug2");
		//	Iterate all child elements
		pChild = pParent->GetFirstChild();
		while(pChild != NULL)
		{
			
			//	Set this value
			if(pISerialize->SetXmlValue(eXmlBlock, pChild) == false)
				bSuccessful = false; // one or more failed

			_theReporter.Debug("CMLHelper", "LoadXmlBlock", "debug3 name:%s value %s bSuccessful:%d type of iserialize:%s", 
				pChild->GetName(),pChild->GetValue(), bSuccessful, typeid(*pISerialize).name());
			//	Get the next child element
			pChild = pParent->GetNextChild();
		}

	}// if((pISerialize != NULL) && (pParent != NULL))

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of all class members using the
//!				specified stream
//!
//!	Parameters:	\li pISerialize - the object being serialized
//!				\li pXmlStream - the stream used to set the property values
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLHelper::LoadXmlStream(IMLSerialize* pISerialize, char* pXmlStream)
{
	CMLPropMember*	pMLPropTree = NULL;
	CMLPropMember*	pChild = NULL;
	bool			bSuccessful = true;

	assert(pISerialize != NULL);
	assert(pXmlStream != NULL);
	_theReporter.Debug("", "LoadXmlStream", "Debug1");
	if((pISerialize != NULL) && (pXmlStream != NULL))
	{
		if((pMLPropTree = GetPropTree(pXmlStream)) != NULL)
		{
			pChild = pMLPropTree->GetFirstChild();
			while(pChild != NULL)
			{
				if(lstrcmpi(ML_XML_BLOCK_NAME_CONTROL, pChild->GetName()) == 0)
				{
					if(LoadXmlBlock(pISerialize, ML_XML_BLOCK_CONTROL, pChild) == false)
						bSuccessful = false;
				}
				
				else if(lstrcmpi(ML_XML_BLOCK_NAME_DATA, pChild->GetName()) == 0)
				{
					if(LoadXmlBlock(pISerialize, ML_XML_BLOCK_DATA, pChild) == false)
						bSuccessful = false;
				}
				
				else if(lstrcmpi(ML_XML_BLOCK_NAME_ERROR, pChild->GetName()) == 0)
				{
					if(LoadXmlBlock(pISerialize, ML_XML_BLOCK_ERROR, pChild) == false)
						bSuccessful = false;
				}
				
				else
				{
					_theReporter.Debug("", "LoadXmlStream", "Unknown XML block: <%s>", pChild->GetName());
					bSuccessful = false;
				}

				pChild = pMLPropTree->GetNextChild();
			
			}// while(pChild != NULL)

			delete pMLPropTree;
			
		}// if((pMLPropTree = GetPropTree(pXmlStream)) != NULL)

	}// if((pMLTree = GetPropTree(pXmlStream)) != NULL)

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to save the XML stream used to represent the object
//!
//!	Parameters:	\li pISerialize - the object to be streamed
//!				\li pszFileSpec - fully qualified path to the output file
//!
//!	Returns:	The buffer containing the XML stream
//------------------------------------------------------------------------------
bool CMLHelper::SaveXmlStream(IMLSerialize* pISerialize, char* pszFileSpec)
{
	CMLDataStream	xmlStream;
	FILE*			fptr = NULL;
	bool			bSuccessful = false;
	
	assert(pISerialize != NULL);
	assert(pszFileSpec != NULL);

	if((pISerialize != NULL) && (pszFileSpec != NULL))
	{
		//	Get the xml stream
		if(GetXmlStream(pISerialize, &xmlStream) == true)
		{
			fopen_s(&fptr, pszFileSpec, "at");

			if(fptr != NULL)
			{
				fprintf(fptr, "\n%s\n", xmlStream.GetBuffer());
				fclose(fptr);
			}

		}
	}

	return bSuccessful;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to 
//!
//!	Parameters:	\li pszClassName - the cpp type_info name
//!
//!	Returns:	a list of the associated C++ attributes names 
//------------------------------------------------------------------------------
//CMLPtrList&CMLHelper::GetObjectAttributes(const char* pszClassName)
//{
//	assert(pszClassName);
//	if(lstrcmpi("CMLFlightPosition", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLFlightPosition flightPosition = CMLFlightPosition();
//		return flightPosition.GetAttributes();
//	}
//	else if(lstrcmpi("CMLAircraft", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLAircraft aircraft = CMLAircraft();
//		return aircraft.GetAttributes();
//	}
//	else if(lstrcmpi("CMLAircraftArrivalData", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLAircraftArrivalData acArrivalData = CMLAircraftArrivalData();
//		return acArrivalData.GetAttributes();
//	}
//	else if(lstrcmpi("CMLFlightPlan", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLFlightPlan flightPlan = CMLFlightPlan();
//		return flightPlan.GetAttributes();
//	}
//
//	else if(lstrcmpi("CMLAircraftTaxiOut", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLAircraftTaxiOut acTaxiOut = CMLAircraftTaxiOut();
//		return acTaxiOut.GetAttributes();
//	}
//	else if(lstrcmpi("CMLAircraftTaxiIn", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLAircraftTaxiIn acTaxiIn = CMLAircraftTaxiIn();
//		return acTaxiIn.GetAttributes();
//	}
//	else if(lstrcmpi("CMLAircraftDepartureData", GetCPPClassName(GetMuthurClassNameFromCppTypeId(pszClassName))) == 0)
//	{
//		CMLAircraftDepartureData acDept = CMLAircraftDepartureData();
//		return acDept.GetAttributes();
//	}
//	else
//	{
//		_theReporter.Warning("MLHelper","GetObjectAttributes", "Ambassador class name not found!");
//		CMLPtrList *ptrList = new CMLPtrList();
//		return *ptrList;
//		if(ptrList)
//		{
//			delete ptrList;
//			ptrList = NULL;
//		}
//	}
//}
