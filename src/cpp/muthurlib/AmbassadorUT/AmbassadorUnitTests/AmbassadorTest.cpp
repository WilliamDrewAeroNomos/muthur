#pragma warning( disable : 4290 )


#include <iostream>
#include <sstream>

#include "MQApi.h"			// ActiveMQ headers
//#include "../../../activemq/activemq-cpp-3.2.5/src/main/decaf/util/logging/LoggerDefines.h"
#include "WinUnit.h"

#include "MLHelper.h"

#include "MLKillAircraft.h"
#include "MLAircraftArrivalData.h"
#include "MLAircraftDepartureData.h"
#include "MLAircraftTaxiOut.h"
#include "MLFlightPlanFiled.h"
#include "MLFlightPosition.h"
#include "MLSpawnAircraft.h"

#include <MLRegisterParams.h>

#include "MLAmbassador.h"

namespace
{
	// Set to 1 to generate a test xml file for each request/response or data type or 0 to not generate these files.
	// Note that there is no automatic "cleanup" of these files (i.e. they are not deleted by these unit tests). 
	// These files are automatically saved to the C: drive and users are responsible for deleting them if so desired.
	// If the files are not deleted, subsequent execution of these unit tests will cause the newly generated contented
	// to be appended to the corresponding test xml file.
	bool GENERATE_TEST_XML_FILES  = false;
	// We first declare a fixture, then we can create SETUP and TEARDOWN functions,
	// and refer to it as the second parameter of the BEGIN_TESTF macro.
	// SETUP and TEARDOWN will be called before and after (respectively) running
	// any test that uses the fixture.
	FIXTURE(AmbassadorTestDataFixture);

	SETUP(AmbassadorTestDataFixture)
	{   		
	}

	TEARDOWN(AmbassadorTestDataFixture)
	{

	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Unit Tests for data objects
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	// ******************************************************************************************************************
	// Test a round trip serialization(create data object CMLKillAircraft, serialize to XML, and re-create an object from
	// XML.  Values assigned to fields in CMLKillAircraft should be the stay the same
	// ******************************************************************************************************************
	BEGIN_TESTF(AmbassadorGenerateKillAircraftXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLKillAircraft *ac = new CMLKillAircraft();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);
		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateKillAircraftXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLKillAircraft *ac2 = new CMLKillAircraft();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_STRING_EQUAL("DAL123", ac2->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac2->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac2->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac2->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac2->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac2->GetArrivalAirportCode());

		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAircraftArrivalData, serialize to XML, and re-create an object from
		// XML.  Values assigned to fields in CMLAircraftArrivalData should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorGenerateAircraftArrivalDataXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAircraftArrivalData *ac = new CMLAircraftArrivalData();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetActualArrivalTimeMSecs(99887766);

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(99887766, ac->GetActualArrivalTimeMSecs());


		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateAircraftArrivalDataXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLAircraftArrivalData *ac2 = new CMLAircraftArrivalData();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_STRING_EQUAL("DAL123", ac2->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac2->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac2->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac2->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac2->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac2->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(99887766, ac2->GetActualArrivalTimeMSecs());
		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAircraftDepartureData, serialize to XML, and re-create an object from
		// XML.  Values assigned to fields in CMLAircraftDepartureData should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorGenerateAircraftDepartureDataXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAircraftDepartureData *ac = new CMLAircraftDepartureData();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetActualDepartureTimeMSecs(99887766);

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(99887766, ac->GetActualDepartureTimeMSecs());


		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateAircraftDepartureDataXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLAircraftDepartureData *ac2 = new CMLAircraftDepartureData();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_STRING_EQUAL("DAL123", ac2->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac2->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac2->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac2->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac2->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac2->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(99887766, ac2->GetActualDepartureTimeMSecs());
		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAircraftTaxiOut, serialize to XML, and re-create an object from
		// XML.  Values assigned to fields in CMLAircraftTaxiOut should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorGenerateAircraftTaxiOutDataXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAircraftTaxiOut *ac = new CMLAircraftTaxiOut();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetTaxiOutTimeMSecs(99887766);

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(99887766, ac->GetTaxiOutTimeMSecs());


		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateAircraftTaxiOutDataXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLAircraftTaxiOut *ac2 = new CMLAircraftTaxiOut();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_STRING_EQUAL("DAL123", ac2->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac2->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac2->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac2->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac2->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac2->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(99887766, ac2->GetTaxiOutTimeMSecs());
		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLFlightPlanFiled, serialize to XML, and re-create an object from
		// XML.  Values assigned to fields in CMLFlightPlanFiled should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorGenerateFlightPlanFiledDataXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLFlightPlanFiled *ac = new CMLFlightPlanFiled();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetAircraftType("B737");				
		ac->SetPlannedDepartureTimeMSecs(113355);	
		ac->SetPlannedArrivalTimeMSecs(335577);		
		ac->SetCruiseSpeedKts(224466);				
		ac->SetCruiseAltitudeFeet(112233);			
		ac->SetRoute("BWI...IAD");						
		ac->SetDepartureCenter("66");				
		ac->SetArrivalCenter("45");				
		ac->SetDepartureFix("JAN");				
		ac->SetArrivalFix("BNA");					
		ac->SetPhysicalClass("heavy");				
		ac->SetWeightClass("large");					
		ac->SetEquipmentQualifier("Used");			
		ac->SetUserClass("owner");					
		ac->SetNumAircraft(3);					

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());
		WIN_ASSERT_STRING_EQUAL("B737", ac->GetAircraftType());
		WIN_ASSERT_EQUAL(113355, ac->GetPlannedDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(335577, ac->GetPlannedArrivalTimeMSecs());
		WIN_ASSERT_EQUAL(224466, ac->GetCruiseSpeedKts());
		WIN_ASSERT_EQUAL(112233, ac->GetCruiseAltitudeFeet());
		WIN_ASSERT_STRING_EQUAL("BWI...IAD", ac->GetRoute());
		WIN_ASSERT_STRING_EQUAL("66", ac->GetDepartureCenter());
		WIN_ASSERT_STRING_EQUAL("45", ac->GetArrivalCenter());
		WIN_ASSERT_STRING_EQUAL("JAN", ac->GetDepartureFix());
		WIN_ASSERT_STRING_EQUAL("BNA", ac->GetArrivalFix());
		WIN_ASSERT_STRING_EQUAL("heavy", ac->GetPhysicalClass());
		WIN_ASSERT_STRING_EQUAL("large", ac->GetWeightClass());
		WIN_ASSERT_STRING_EQUAL("Used", ac->GetEquipmentQualifier());
		WIN_ASSERT_EQUAL(3, ac->GetNumAircraft());


		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);


		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateFlightPlanFiledDataXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLFlightPlanFiled *ac2 = new CMLFlightPlanFiled();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_STRING_EQUAL("DAL123", ac2->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac2->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac2->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac2->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac2->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac2->GetArrivalAirportCode());
		WIN_ASSERT_STRING_EQUAL("B737", ac2->GetAircraftType());
		WIN_ASSERT_EQUAL(113355, ac2->GetPlannedDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(335577, ac2->GetPlannedArrivalTimeMSecs());
		WIN_ASSERT_EQUAL(224466, ac2->GetCruiseSpeedKts());
		WIN_ASSERT_EQUAL(112233, ac2->GetCruiseAltitudeFeet());
		WIN_ASSERT_STRING_EQUAL("BWI...IAD", ac2->GetRoute());
		WIN_ASSERT_STRING_EQUAL("66", ac2->GetDepartureCenter());
		WIN_ASSERT_STRING_EQUAL("45", ac2->GetArrivalCenter());
		WIN_ASSERT_STRING_EQUAL("JAN", ac2->GetDepartureFix());
		WIN_ASSERT_STRING_EQUAL("BNA", ac2->GetArrivalFix());
		WIN_ASSERT_STRING_EQUAL("heavy", ac2->GetPhysicalClass());
		WIN_ASSERT_STRING_EQUAL("large", ac2->GetWeightClass());
		WIN_ASSERT_STRING_EQUAL("Used", ac2->GetEquipmentQualifier());
		WIN_ASSERT_EQUAL(3, ac2->GetNumAircraft());
		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLFlightPosition, serialize to XML, and re-create an object from
		// XML.  Values assigned to fields in CMLFlightPosition should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorGenerateFlightPositionDataXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLFlightPosition *ac = new CMLFlightPosition();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetPitchDegrees(135246);	
		ac->SetCruiseAltitudeFeet(246135);
		ac->SetLatitudeDegrees(1324);	
		ac->SetLongitudeDegrees(2413);
		ac->SetAltitudeFt(567765);		
		ac->SetGroundspeedKts(678);	
		ac->SetHeadingDegrees(130);	
		ac->SetAirspeedKts(236);		
		ac->SetRollDegrees(156);		
		//		ac->SetYawDegrees(128);		
		ac->SetSector("N");			
		ac->SetCenter("66");			

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(135246, ac->GetPitchDegrees());
		WIN_ASSERT_EQUAL(246135, ac->GetCruiseAltitudeFeet());
		WIN_ASSERT_EQUAL(1324, ac->GetLatitudeDegrees());
		WIN_ASSERT_EQUAL(2413, ac->GetLongitudeDegrees());
		WIN_ASSERT_EQUAL(567765, ac->GetAltitudeFt());
		WIN_ASSERT_EQUAL(678, ac->GetGroundspeedKts());
		WIN_ASSERT_EQUAL(130, ac->GetHeadingDegrees());
		WIN_ASSERT_EQUAL(236, ac->GetAirspeedKts());
		WIN_ASSERT_EQUAL(156, ac->GetRollDegrees());
		//		WIN_ASSERT_EQUAL(128, ac->GetYawDegrees());
		WIN_ASSERT_STRING_EQUAL("N", ac->GetSector());
		WIN_ASSERT_STRING_EQUAL("66", ac->GetCenter());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);


		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateFlightPositionDataXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLFlightPosition *ac2 = new CMLFlightPosition();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_STRING_EQUAL("DAL123", ac2->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac2->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac2->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac2->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac2->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac2->GetArrivalAirportCode());
		WIN_ASSERT_EQUAL(135246, ac2->GetPitchDegrees());
		WIN_ASSERT_EQUAL(246135, ac2->GetCruiseAltitudeFeet());
		WIN_ASSERT_EQUAL(1324, ac2->GetLatitudeDegrees());
		WIN_ASSERT_EQUAL(2413, ac2->GetLongitudeDegrees());
		WIN_ASSERT_EQUAL(567765, ac2->GetAltitudeFt());
		WIN_ASSERT_EQUAL(678, ac2->GetGroundspeedKts());
		WIN_ASSERT_EQUAL(130, ac2->GetHeadingDegrees());
		WIN_ASSERT_EQUAL(236, ac2->GetAirspeedKts());
		WIN_ASSERT_EQUAL(156, ac2->GetRollDegrees());
		//		WIN_ASSERT_EQUAL(128, ac2->GetYawDegrees());
		WIN_ASSERT_STRING_EQUAL("N", ac2->GetSector());
		WIN_ASSERT_STRING_EQUAL("66", ac2->GetCenter());

		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLSpawnAircraft, serialize to XML, and re-create an object from
		// XML.  Values assigned to fields in CMLSpawnAircraft should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorGenerateCMLSpawnAircraftDataXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLSpawnAircraft *ac = new CMLSpawnAircraft();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetPitchDegrees(135246);	
		ac->SetLatitudeDegrees(1324);	
		ac->SetLongitudeDegrees(2413);
		ac->SetAltitudeFt(567765);		
		ac->SetGroundspeedKts(678);	
		ac->SetHeadingDegrees(130);	
		ac->SetAirspeedKts(236);		
		ac->SetRollDegrees(156);		
		//ac->SetYawDegrees(128);		
		ac->SetSector("N");			
		ac->SetCenter("66");			
		ac->SetActualDepartureTimeMSecs(99887766);
		ac->SetTaxiOutTimeMSecs(87766);
		ac->SetAircraftType("B737");				
		ac->SetPlannedDepartureTimeMSecs(113355);	
		ac->SetPlannedArrivalTimeMSecs(335577);		
		ac->SetCruiseSpeedKts(224466);				
		ac->SetCruiseAltitudeFeet(112233);			
		ac->SetRoute("BWI...IAD");						
		ac->SetDepartureCenter("66");				
		ac->SetArrivalCenter("45");				
		ac->SetDepartureFix("JAN");				
		ac->SetArrivalFix("BNA");					
		ac->SetPhysicalClass("heavy");				
		ac->SetWeightClass("large");					
		ac->SetEquipmentQualifier("Used");	
		ac->SetNumAircraft(3);

		WIN_ASSERT_STRING_EQUAL("DAL123", ac->GetACId());
		WIN_ASSERT_STRING_EQUAL("ABCD", ac->GetACTailNumber());
		WIN_ASSERT_EQUAL(5, ac->GetCreatedTimestamp());
		WIN_ASSERT_STRING_EQUAL("1234", ac->GetUUID());
		WIN_ASSERT_STRING_EQUAL("BWI", ac->GetDeptAirportCode());
		WIN_ASSERT_STRING_EQUAL("DFW", ac->GetArrivalAirportCode());

		WIN_ASSERT_EQUAL(135246, ac->GetPitchDegrees());
		WIN_ASSERT_EQUAL(112233, ac->GetCruiseAltitudeFeet());
		WIN_ASSERT_EQUAL(1324, ac->GetLatitudeDegrees());
		WIN_ASSERT_EQUAL(2413, ac->GetLongitudeDegrees());
		WIN_ASSERT_EQUAL(567765, ac->GetAltitudeFt());
		WIN_ASSERT_EQUAL(678, ac->GetGroundspeedKts());
		WIN_ASSERT_EQUAL(130, ac->GetHeadingDegrees());
		WIN_ASSERT_EQUAL(236, ac->GetAirspeedKts());
		WIN_ASSERT_EQUAL(156, ac->GetRollDegrees());
		//WIN_ASSERT_EQUAL(128, ac->GetYawDegrees());
		WIN_ASSERT_STRING_EQUAL("N", ac->GetSector());
		WIN_ASSERT_STRING_EQUAL("66", ac->GetCenter());
		WIN_ASSERT_EQUAL(99887766,ac->GetActualDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(87766,ac->GetTaxiOutTimeMSecs());
		WIN_ASSERT_STRING_EQUAL("B737", ac->GetAircraftType());
		WIN_ASSERT_EQUAL(113355, ac->GetPlannedDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(335577, ac->GetPlannedArrivalTimeMSecs());
		WIN_ASSERT_EQUAL(224466, ac->GetCruiseSpeedKts());
		WIN_ASSERT_EQUAL(112233, ac->GetCruiseAltitudeFeet());
		WIN_ASSERT_STRING_EQUAL("BWI...IAD", ac->GetRoute());
		WIN_ASSERT_EQUAL(87766, ac->GetTaxiOutTimeMSecs());
		WIN_ASSERT_STRING_EQUAL("66", ac->GetDepartureCenter());
		WIN_ASSERT_STRING_EQUAL("45", ac->GetArrivalCenter());
		WIN_ASSERT_STRING_EQUAL("JAN", ac->GetDepartureFix());
		WIN_ASSERT_STRING_EQUAL("BNA", ac->GetArrivalFix());
		WIN_ASSERT_STRING_EQUAL("heavy", ac->GetPhysicalClass());
		WIN_ASSERT_STRING_EQUAL("large", ac->GetWeightClass());
		WIN_ASSERT_STRING_EQUAL("Used", ac->GetEquipmentQualifier());
		WIN_ASSERT_EQUAL(3, ac->GetNumAircraft());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLAmbassador pAmbassador = CMLAmbassador::GetInstance();

		CMLHelper::GetXmlStream(ac, dStream);


		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(ac, "C:\\AmbassadorGenerateCMLSpawnAircraftDataXmlTest.xml");
		CMLPropMember *pPropTree = CMLHelper::GetPropTree(ac);	

		WIN_ASSERT_NOT_NULL(pPropTree);


		//pAmbassador
		cout << (dStream->GetBuffer() == NULL) << endl;
		dStream->SetRead(true);
		CMLSpawnAircraft *ac2 = new CMLSpawnAircraft();

		CMLHelper::LoadXmlStream(ac2, dStream->GetBuffer());


		WIN_ASSERT_NOT_NULL(ac2);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		WIN_ASSERT_EQUAL(135246, ac2->GetPitchDegrees());
		WIN_ASSERT_EQUAL(112233, ac2->GetCruiseAltitudeFeet());
		WIN_ASSERT_EQUAL(1324, ac2->GetLatitudeDegrees());
		WIN_ASSERT_EQUAL(2413, ac2->GetLongitudeDegrees());
		WIN_ASSERT_EQUAL(567765, ac2->GetAltitudeFt());
		WIN_ASSERT_EQUAL(678, ac2->GetGroundspeedKts());
		WIN_ASSERT_EQUAL(130, ac2->GetHeadingDegrees());
		WIN_ASSERT_EQUAL(236, ac2->GetAirspeedKts());
		WIN_ASSERT_EQUAL(156, ac2->GetRollDegrees());
		//WIN_ASSERT_EQUAL(128, ac2->GetYawDegrees());
		WIN_ASSERT_STRING_EQUAL("N", ac2->GetSector());
		WIN_ASSERT_STRING_EQUAL("66", ac2->GetCenter());
		WIN_ASSERT_EQUAL(99887766,ac2->GetActualDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(87766,ac2->GetTaxiOutTimeMSecs());
		WIN_ASSERT_STRING_EQUAL("B737", ac2->GetAircraftType());
		WIN_ASSERT_EQUAL(113355, ac2->GetPlannedDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(335577, ac2->GetPlannedArrivalTimeMSecs());
		WIN_ASSERT_EQUAL(224466, ac2->GetCruiseSpeedKts());
		WIN_ASSERT_EQUAL(112233, ac2->GetCruiseAltitudeFeet());
		WIN_ASSERT_STRING_EQUAL("BWI...IAD", ac2->GetRoute());
		WIN_ASSERT_EQUAL(87766, ac2->GetTaxiOutTimeMSecs());
		WIN_ASSERT_STRING_EQUAL("66", ac2->GetDepartureCenter());
		WIN_ASSERT_STRING_EQUAL("45", ac2->GetArrivalCenter());
		WIN_ASSERT_STRING_EQUAL("JAN", ac2->GetDepartureFix());
		WIN_ASSERT_STRING_EQUAL("BNA", ac2->GetArrivalFix());
		WIN_ASSERT_STRING_EQUAL("heavy", ac2->GetPhysicalClass());
		WIN_ASSERT_STRING_EQUAL("large", ac2->GetWeightClass());
		WIN_ASSERT_STRING_EQUAL("Used", ac2->GetEquipmentQualifier());
		WIN_ASSERT_EQUAL(3, ac2->GetNumAircraft());


		if(ac)
		{
			delete ac;
			ac = NULL;
		}
		if(ac2)
		{
			delete ac2;
			ac2 = NULL;
		}
	}
	END_TESTF

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// Unit Tests for request/response objects
		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLRegisterParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLRegisterParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorFederateRegistrationRequestXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLRegisterParams* regParams1 = new CMLRegisterParams();
		regParams1->SetName("FRASCA");

		WIN_ASSERT_NOT_NULL(regParams1);
		WIN_ASSERT_STRING_EQUAL("FRASCA", regParams1->GetName());
		WIN_ASSERT_STRING_EQUAL("NexSimFed", CMLAmbassador::GetInstance().GetFederateName());
		WIN_ASSERT_EQUAL(50, CMLAmbassador::GetInstance().GetTimeOutSecs());
		//WIN_ASSERT_STRING_EQUAL("FederateRegistrationRequest", regParams1->GetEventName());
		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(regParams1, dStream);
		/*cout << (dStream->GetBuffer() == NULL) << endl;*/

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(regParams1, "C:\\AmbassadorFederateRegistrationRequestXmlTest.xml");

		dStream->SetRead(true);

		CMLRegisterParams* regParams2 = new CMLRegisterParams();
		CMLHelper::LoadXmlStream(regParams2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(regParams2);
		WIN_ASSERT_STRING_EQUAL("FRASCA", regParams2->GetName());
		if(regParams1)
		{
			delete regParams1;
			regParams1 = NULL;
		}
		if(regParams2)
		{
			delete regParams2;
			regParams2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLRegisterResponse, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLRegisterResponse should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorFederateRegistrationResponseXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLRegisterResponse* respParams1 = new CMLRegisterResponse();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		respParams1->SetFedRegHandle(fedRegHandle);

		WIN_ASSERT_NOT_NULL(respParams1);
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", respParams1->GetFedRegHandle().GetMuthurId());
		//WIN_ASSERT_STRING_EQUAL("FederateRegistrationResponse", respParams1->GetEventName());
		/*WIN_ASSERT_STRING_EQUAL("NexSimFed", CMLAmbassador::GetInstance().GetFederateName());
		WIN_ASSERT_EQUAL(50, CMLAmbassador::GetInstance().GetTimeOutSecs());*/

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(respParams1, dStream);
		/*cout << (dStream->GetBuffer() == NULL) << endl;*/

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(respParams1, "C:\\AmbassadorFederateRegistrationResponseXmlTest.xml");

		dStream->SetRead(true);

		CMLRegisterResponse* respParams2 = new CMLRegisterResponse();
		CMLHelper::LoadXmlStream(respParams2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(respParams2);
		if(respParams1)
		{
			delete respParams1;
			respParams1 = NULL;
		}
		if(respParams2)
		{
			delete respParams2;
			respParams2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLListFEMParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLListFEMParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorListFedExecModelsRequestXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLListFEMParams* listFems1 = new CMLListFEMParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		listFems1->SetFedRegHandle(fedRegHandle);

		WIN_ASSERT_NOT_NULL(listFems1);
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", listFems1->GetFedRegHandle().GetMuthurId());
		//WIN_ASSERT_STRING_EQUAL("ListFedExecModelsRequest", listFems1->GetEventName());
		/*WIN_ASSERT_STRING_EQUAL("NexSimFed", CMLAmbassador::GetInstance().GetFederateName());
		WIN_ASSERT_EQUAL(50, CMLAmbassador::GetInstance().GetTimeOutSecs());*/

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(listFems1, dStream);
		/*cout << (dStream->GetBuffer() == NULL) << endl;*/

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(listFems1, "C:\\AmbassadorListFedExecModelsRequestXmlTest.xml");

		dStream->SetRead(true);

		CMLListFEMParams* listFems2 = new CMLListFEMParams();
		CMLHelper::LoadXmlStream(listFems2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(listFems2);

		if(listFems1)
		{
			delete listFems1;
			listFems1 = NULL;
		}
		if(listFems2)
		{
			delete listFems2;
			listFems2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLListFEMResponse, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLListFEMResponse should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorListFedExecModelsResponseXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLListFEMResponse* listFemsResp1 = new CMLListFEMResponse();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		CMLFedExecModel fem1 = CMLFedExecModel();
		fem1.SetName("Frasca NexSim integration");
		fem1.SetDescription("Federation of NexSim ATC and Frasca FTD");
		fem1.SetDurationFederationExecutionMSecs(3600000);
		fem1.SetDefaultDurationWithinStartupProtocolMSecs(30000);
		fem1.SetDurationJoinFederationMSecs(0);
		fem1.SetDurationRegisterPublicationMSecs(5);
		fem1.SetDurationRegisterSubscriptionMSecs(10);
		fem1.SetDurationRegisterToRunMSecs(15);
		fem1.SetDurationTimeToWaitAfterTermination(20);
		fem1.AddRequiredFederate("NexSim");
		fem1.AddRequiredFederate("FRASCA");

		CMLFedExecModel fem2 = CMLFedExecModel();
		fem2.SetName("Single Participant");
		fem2.SetDescription("Federation with a single participant");
		fem2.SetDurationFederationExecutionMSecs(3600000);
		fem2.SetDefaultDurationWithinStartupProtocolMSecs(30000);
		fem2.SetDurationJoinFederationMSecs(20);
		fem2.SetDurationRegisterPublicationMSecs(25);
		fem2.SetDurationRegisterSubscriptionMSecs(30);
		fem2.SetDurationRegisterToRunMSecs(35);
		fem2.SetDurationTimeToWaitAfterTermination(40);
		fem2.AddRequiredFederate("FRASCA");

		listFemsResp1->AddFedExecModel(fem1);
		listFemsResp1->AddFedExecModel(fem2);
		WIN_ASSERT_NOT_NULL(listFemsResp1);

		WIN_ASSERT_STRING_EQUAL("Frasca NexSim integration", fem1.GetName());
		WIN_ASSERT_STRING_EQUAL("Federation of NexSim ATC and Frasca FTD", fem1.GetDescription());
		WIN_ASSERT_EQUAL(3600000, fem1.GetDurationFederationExecutionMSecs());
		WIN_ASSERT_EQUAL(30000, fem1.GetDefaultDurationWithinStartupProtocolMSecs());
		WIN_ASSERT_EQUAL(0, fem1.GetDurationJoinFederationMSecs());
		WIN_ASSERT_EQUAL(5, fem1.GetDurationRegisterPublicationMSecs());
		WIN_ASSERT_EQUAL(10, fem1.GetDurationRegisterSubscriptionMSecs());
		WIN_ASSERT_EQUAL(15, fem1.GetDurationRegisterToRunMSecs());
		WIN_ASSERT_EQUAL(20, fem1.GetDurationTimeToWaitAfterTermination());
		WIN_ASSERT_EQUAL(2, fem1.GetRequiredFederates().GetCount());

		WIN_ASSERT_STRING_EQUAL("Single Participant", fem2.GetName());
		WIN_ASSERT_STRING_EQUAL("Federation with a single participant", fem2.GetDescription());
		WIN_ASSERT_EQUAL(3600000, fem2.GetDurationFederationExecutionMSecs());
		WIN_ASSERT_EQUAL(30000, fem2.GetDefaultDurationWithinStartupProtocolMSecs());
		WIN_ASSERT_EQUAL(20, fem2.GetDurationJoinFederationMSecs());
		WIN_ASSERT_EQUAL(25, fem2.GetDurationRegisterPublicationMSecs());
		WIN_ASSERT_EQUAL(30, fem2.GetDurationRegisterSubscriptionMSecs());
		WIN_ASSERT_EQUAL(35, fem2.GetDurationRegisterToRunMSecs());
		WIN_ASSERT_EQUAL(40, fem2.GetDurationTimeToWaitAfterTermination());
		WIN_ASSERT_EQUAL(1, fem2.GetRequiredFederates().GetCount());

		/*WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", listFemsResp1->GetFedRegHandle().GetMuthurId());*/
		//WIN_ASSERT_STRING_EQUAL("ListFedExecModelsRequest", listFems1->GetEventName());
		/*WIN_ASSERT_STRING_EQUAL("NexSimFed", CMLAmbassador::GetInstance().GetFederateName());
		WIN_ASSERT_EQUAL(50, CMLAmbassador::GetInstance().GetTimeOutSecs());*/

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(listFemsResp1, dStream);
		/*cout << (dStream->GetBuffer() == NULL) << endl;*/

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(listFemsResp1, "C:\\AmbassadorListFedExecModelsResponseXmlTest.xml");

		dStream->SetRead(true);

		CMLListFEMResponse* listFemsResp2 = new CMLListFEMResponse();
		CMLHelper::LoadXmlStream(listFemsResp2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(listFemsResp2);

		//	Now add a child for each FEM in the list
		CMLFedExecModel* pFedExecModel1 = listFemsResp2->GetFirstFedExecModel();
		CMLFedExecModel* pFedExecModel2 = NULL;
		while(pFedExecModel1 != NULL)
		{
			WIN_ASSERT_STRING_EQUAL("Frasca NexSim integration", pFedExecModel1->GetName());
			WIN_ASSERT_STRING_EQUAL("Federation of NexSim ATC and Frasca FTD", pFedExecModel1->GetDescription());
			WIN_ASSERT_EQUAL(3600000, pFedExecModel1->GetDurationFederationExecutionMSecs());
			WIN_ASSERT_EQUAL(30000, pFedExecModel1->GetDurationJoinFederationMSecs());
			WIN_ASSERT_EQUAL(0, pFedExecModel1->GetDefaultDurationWithinStartupProtocolMSecs());
			WIN_ASSERT_EQUAL(5, pFedExecModel1->GetDurationRegisterPublicationMSecs());
			WIN_ASSERT_EQUAL(10, pFedExecModel1->GetDurationRegisterSubscriptionMSecs());
			WIN_ASSERT_EQUAL(15, pFedExecModel1->GetDurationRegisterToRunMSecs());
			WIN_ASSERT_EQUAL(20, pFedExecModel1->GetDurationTimeToWaitAfterTermination());
			WIN_ASSERT_EQUAL(2, pFedExecModel1->GetRequiredFederates().GetCount());

			pFedExecModel2  = listFemsResp2->GetNextFedExecModel();
			WIN_ASSERT_STRING_EQUAL("Single Participant", pFedExecModel2->GetName());
			WIN_ASSERT_STRING_EQUAL("Federation with a single participant", pFedExecModel2->GetDescription());
			WIN_ASSERT_EQUAL(3600000, pFedExecModel2->GetDurationFederationExecutionMSecs());
			WIN_ASSERT_EQUAL(30000, pFedExecModel2->GetDefaultDurationWithinStartupProtocolMSecs());
			WIN_ASSERT_EQUAL(20, pFedExecModel2->GetDurationJoinFederationMSecs());
			WIN_ASSERT_EQUAL(25, pFedExecModel2->GetDurationRegisterPublicationMSecs());
			WIN_ASSERT_EQUAL(30, pFedExecModel2->GetDurationRegisterSubscriptionMSecs());
			WIN_ASSERT_EQUAL(35, pFedExecModel2->GetDurationRegisterToRunMSecs());
			WIN_ASSERT_EQUAL(40, pFedExecModel2->GetDurationTimeToWaitAfterTermination());
			WIN_ASSERT_EQUAL(1, pFedExecModel2->GetRequiredFederates().GetCount());

			if(pFedExecModel1)
			{
				delete pFedExecModel1;
				pFedExecModel1 = NULL;
			}
			if(pFedExecModel2)
			{
				delete pFedExecModel2;
				pFedExecModel2 = NULL;
			}
		}		

		if(listFemsResp1)
		{
			delete listFemsResp1;
			listFemsResp1 = NULL;
		}
		if(listFemsResp2)
		{
			delete listFemsResp2;
			listFemsResp2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLJoinFedParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLJoinFedParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorJoinFederationRequestXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);

		CMLJoinFedParams* joinFed1 = new CMLJoinFedParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		joinFed1->SetFedRegHandle(fedRegHandle);

		CMLFedExecModel fem1 = CMLFedExecModel();
		fem1.SetName("Frasca NexSim integration");
		fem1.SetDescription("Federation of NexSim ATC and Frasca FTD");
		fem1.SetDurationFederationExecutionMSecs(3600000);
		fem1.SetDefaultDurationWithinStartupProtocolMSecs(30000);
		fem1.SetDurationJoinFederationMSecs(0);
		fem1.SetDurationRegisterPublicationMSecs(5);
		fem1.SetDurationRegisterSubscriptionMSecs(10);
		fem1.SetDurationRegisterToRunMSecs(15);
		fem1.SetDurationTimeToWaitAfterTermination(20);
		fem1.AddRequiredFederate("NexSim");
		fem1.AddRequiredFederate("FRASCA");

		joinFed1->SetFEM(fem1);

		WIN_ASSERT_NOT_NULL(joinFed1);
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", joinFed1->GetFedRegHandle().GetMuthurId());

		WIN_ASSERT_STRING_EQUAL("Frasca NexSim integration", fem1.GetName());
		WIN_ASSERT_STRING_EQUAL("Federation of NexSim ATC and Frasca FTD", fem1.GetDescription());
		WIN_ASSERT_EQUAL(3600000, fem1.GetDurationFederationExecutionMSecs());
		WIN_ASSERT_EQUAL(30000, fem1.GetDefaultDurationWithinStartupProtocolMSecs());
		WIN_ASSERT_EQUAL(0, fem1.GetDurationJoinFederationMSecs());
		WIN_ASSERT_EQUAL(5, fem1.GetDurationRegisterPublicationMSecs());
		WIN_ASSERT_EQUAL(10, fem1.GetDurationRegisterSubscriptionMSecs());
		WIN_ASSERT_EQUAL(15, fem1.GetDurationRegisterToRunMSecs());
		WIN_ASSERT_EQUAL(20, fem1.GetDurationTimeToWaitAfterTermination());
		WIN_ASSERT_EQUAL(2, fem1.GetRequiredFederates().GetCount());


		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(joinFed1, dStream);
		WIN_ASSERT_NOT_NULL(dStream);
		//		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(joinFed1, "C:\\AmbassadorJoinFederationRequestXmlTest.xml");

		dStream->SetRead(true);

		CMLJoinFedParams* joinFed2 = new CMLJoinFedParams();
		CMLHelper::LoadXmlStream(joinFed2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(joinFed2);

		//	Now add a child for each FEM in the list
		CMLFedExecModel* pFedExecModel1 = new CMLFedExecModel(joinFed2->GetFEM());
		while(pFedExecModel1 != NULL)
		{
			WIN_ASSERT_STRING_EQUAL("Frasca NexSim integration", pFedExecModel1->GetName());
			WIN_ASSERT_STRING_EQUAL("Federation of NexSim ATC and Frasca FTD", pFedExecModel1->GetDescription());
			WIN_ASSERT_EQUAL(3600000, pFedExecModel1->GetDurationFederationExecutionMSecs());
			WIN_ASSERT_EQUAL(30000, pFedExecModel1->GetDefaultDurationWithinStartupProtocolMSecs());
			WIN_ASSERT_EQUAL(0, pFedExecModel1->GetDurationJoinFederationMSecs());
			WIN_ASSERT_EQUAL(5, pFedExecModel1->GetDurationRegisterPublicationMSecs());
			WIN_ASSERT_EQUAL(10, pFedExecModel1->GetDurationRegisterSubscriptionMSecs());
			WIN_ASSERT_EQUAL(15, pFedExecModel1->GetDurationRegisterToRunMSecs());
			WIN_ASSERT_EQUAL(20, pFedExecModel1->GetDurationTimeToWaitAfterTermination());
			WIN_ASSERT_EQUAL(2, pFedExecModel1->GetRequiredFederates().GetCount());
			if(pFedExecModel1)
			{
				delete pFedExecModel1;
				pFedExecModel1 = NULL;
			}
		}		

		if(joinFed1)
		{
			delete joinFed1;
			joinFed1 = NULL;
		}
		if(joinFed2)
		{
			delete joinFed2;
			joinFed2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLJoinFedResponse, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLJoinFedResponse should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorJoinFederationResponseXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLJoinFedResponse* joinFedResp1 = new CMLJoinFedResponse();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());

		WIN_ASSERT_NOT_NULL(joinFedResp1);

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(joinFedResp1, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(joinFedResp1, "C:\\AmbassadorJoinFederationResponseXmlTest.xml");

		dStream->SetRead(true);

		CMLJoinFedResponse* joinFedResp2 = new CMLJoinFedResponse();
		CMLHelper::LoadXmlStream(joinFedResp2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(joinFedResp2);

		if(joinFedResp1)
		{
			delete joinFedResp1;
			joinFedResp1 = NULL;
		}
		if(joinFedResp2)
		{
			delete joinFedResp2;
			joinFedResp2 = NULL;
		}
	}
	END_TESTF


		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAddDataPubParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLAddDataPubParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorRegisterPublicationRequestXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);

		CMLAddDataPubParams* addDataPub1 = new CMLAddDataPubParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecModelHandle = CMLHandle();
		fedExecModelHandle.SetMuthurId("49ea-95e5-a49fcce6588f-4ef3457e-bb31");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());

		addDataPub1->SetFedRegHandle(fedRegHandle);
		addDataPub1->SetFedExecModelHandle(fedExecModelHandle);
		addDataPub1->AddClass("FlightPositionData");
		addDataPub1->AddClass("SpawnAircraftData");
		addDataPub1->AddClass("KillAircraftData");
		//
		//joinFed1->SetFEM(fem1);

		WIN_ASSERT_NOT_NULL(addDataPub1);
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", addDataPub1->GetFedRegHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", addDataPub1->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_EQUAL(3, addDataPub1->GetClasses().GetCount());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(addDataPub1, dStream);
		WIN_ASSERT_NOT_NULL(dStream);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(addDataPub1, "C:\\AmbassadorRegisterPublicationRequestXmlTest.xml");

		dStream->SetRead(true);

		CMLAddDataPubParams* addDataPub2 = new CMLAddDataPubParams();
		CMLHelper::LoadXmlStream(addDataPub2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(addDataPub2);
		WIN_ASSERT_EQUAL(3, addDataPub2->GetClasses().GetCount());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", addDataPub2->GetFedExecModelHandle().GetMuthurId());
		if(addDataPub1)
		{
			delete addDataPub1;
			addDataPub1 = NULL;
		}
		if(addDataPub2)
		{
			delete addDataPub2;
			addDataPub2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAddDataPubResponse, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLAddDataPubResponse should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorRegisterPublicationResponseXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);

		CMLAddDataPubResponse* addDataPub1 = new CMLAddDataPubResponse();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecModelHandle = CMLHandle();
		fedExecModelHandle.SetMuthurId("49ea-95e5-a49fcce6588f-4ef3457e-bb31");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());

		addDataPub1->SetFedExecModelHandle(fedExecModelHandle);

		//		addDataPub1->SetFedRegHandle(fedRegHandle);
		addDataPub1->AddClass("FlightPositionData");
		addDataPub1->AddClass("SpawnAircraftData");
		addDataPub1->AddClass("KillAircraftData");
		//
		//joinFed1->SetFEM(fem1);

		WIN_ASSERT_NOT_NULL(addDataPub1);
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", addDataPub1->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_EQUAL(3, addDataPub1->GetClasses().GetCount());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(addDataPub1, dStream);
		WIN_ASSERT_NOT_NULL(dStream);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(addDataPub1, "C:\\AmbassadorRegisterPublicationResponseXmlTest.xml");

		dStream->SetRead(true);

		CMLAddDataPubResponse* addDataPub2 = new CMLAddDataPubResponse();
		CMLHelper::LoadXmlStream(addDataPub2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(addDataPub2);
		WIN_ASSERT_EQUAL(3, addDataPub2->GetClasses().GetCount());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", addDataPub2->GetFedExecModelHandle().GetMuthurId());
		if(addDataPub1)
		{
			delete addDataPub1;
			addDataPub1 = NULL;
		}
		if(addDataPub2)
		{
			delete addDataPub2;
			addDataPub2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAddDataSubParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLAddDataSubParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorDataSubscriptionRequestXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);

		CMLAddDataSubParams* addDataSub1 = new CMLAddDataSubParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecHandle = CMLHandle();
		fedExecHandle.SetMuthurId("bb31-49ea-95e5-a49fcce6588f-4ef3457e");
		CMLHandle fedExecModelHandle = CMLHandle();
		fedExecModelHandle.SetMuthurId("49ea-95e5-a49fcce6588f-4ef3457e-bb31");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());

		addDataSub1->SetFedRegHandle(fedRegHandle);
		addDataSub1->SetFedExecHandle(fedExecHandle);
		addDataSub1->SetFedExecModelHandle(fedExecModelHandle);
		addDataSub1->SetSubscriptionQueueName("ID:mbpro.local-53232-1308102883574-0:0:1");
		addDataSub1->AddClass("FlightPositionData");
		addDataSub1->AddClass("SpawnAircraftData");
		addDataSub1->AddClass("KillAircraftData");
		//
		//joinFed1->SetFEM(fem1);

		WIN_ASSERT_NOT_NULL(addDataSub1);
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", addDataSub1->GetFedRegHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("ID:mbpro.local-53232-1308102883574-0:0:1", addDataSub1->GetSubscriptionQueueName());
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", addDataSub1->GetFedExecHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", addDataSub1->GetFedExecModelHandle().GetMuthurId());
		//		WIN_ASSERT_EQUAL(3, addDataPub1->GetClasses().GetCount());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(addDataSub1, dStream);
		WIN_ASSERT_NOT_NULL(dStream);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(addDataSub1, "C:\\AmbassadorDataSubscriptionRequestXmlTest.xml");

		dStream->SetRead(true);

		CMLAddDataSubParams* addDataSub2 = new CMLAddDataSubParams();
		CMLHelper::LoadXmlStream(addDataSub2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(addDataSub2);
		//WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", addDataSub2->GetFedRegHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("ID:mbpro.local-53232-1308102883574-0:0:1", addDataSub2->GetSubscriptionQueueName());
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", addDataSub2->GetFedExecHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", addDataSub2->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_EQUAL(3, addDataSub2->GetClasses().GetCount());

		if(addDataSub1)
		{
			delete addDataSub1;
			addDataSub1 = NULL;
		}
		if(addDataSub2)
		{
			delete addDataSub2;
			addDataSub2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLAddDataSubResponse, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLAddDataSubResponse should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorDataSubscriptionResponseXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLAddDataSubResponse* addDataSubResp1 = new CMLAddDataSubResponse();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecHandle = CMLHandle();
		fedExecHandle.SetMuthurId("bb31-49ea-95e5-a49fcce6588f-4ef3457e");
		addDataSubResp1->SetFedExecHandle(fedExecHandle);

		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());

		WIN_ASSERT_NOT_NULL(addDataSubResp1);
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", addDataSubResp1->GetFedExecHandle().GetMuthurId());
		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(addDataSubResp1, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(addDataSubResp1, "C:\\AmbassadorDataSubscriptionResponseXmlTest.xml");

		dStream->SetRead(true);

		CMLAddDataSubResponse* addDataSubResp2 = new CMLAddDataSubResponse();
		CMLHelper::LoadXmlStream(addDataSubResp2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(addDataSubResp2);
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", addDataSubResp2->GetFedExecHandle().GetMuthurId());
		if(addDataSubResp1)
		{
			delete addDataSubResp1;
			addDataSubResp1 = NULL;
		}
		if(addDataSubResp2)
		{
			delete addDataSubResp2;
			addDataSubResp2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLReadyParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLReadyParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorReadyToRunRequestXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);

		CMLReadyParams* readyRun1 = new CMLReadyParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecModelHandle = CMLHandle();
		fedExecModelHandle.SetMuthurId("22cd5b9d-195d-4d86-8c01-520f1948acae");
		CMLHandle fedExecHandle = CMLHandle();
		fedExecHandle.SetMuthurId("bb31-49ea-95e5-a49fcce6588f-4ef3457e");
		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		readyRun1->SetFedRegHandle(fedRegHandle);
		readyRun1->SetFedExecModelHandle(fedExecModelHandle);
		readyRun1->SetFedExecHandle(fedExecHandle);
		//

		WIN_ASSERT_NOT_NULL(readyRun1);
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", readyRun1->GetFedRegHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("22cd5b9d-195d-4d86-8c01-520f1948acae", readyRun1->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", readyRun1->GetFedExecHandle().GetMuthurId());
		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(readyRun1, dStream);
		WIN_ASSERT_NOT_NULL(dStream);
		WIN_ASSERT_NOT_NULL(dStream->GetBuffer());

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(readyRun1, "C:\\AmbassadorReadyToRunRequestXmlTest.xml");

		dStream->SetRead(true);

		CMLAddDataSubParams* readyRun2 = new CMLAddDataSubParams();
		CMLHelper::LoadXmlStream(readyRun2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(readyRun2);
		//WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", addDataSub2->GetFedRegHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("4ef3457e-bb31-49ea-95e5-a49fcce6588f", readyRun1->GetFedRegHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("22cd5b9d-195d-4d86-8c01-520f1948acae", readyRun2->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", readyRun2->GetFedExecHandle().GetMuthurId());
		if(readyRun1)
		{
			delete readyRun1;
			readyRun1 = NULL;
		}
		if(readyRun2)
		{
			delete readyRun2;
			readyRun2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLReadyResponse, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLReadyResponse should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorReadyToRunResponseXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLReadyResponse* readyRunResp1 = new CMLReadyResponse();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecHandle = CMLHandle();
		fedExecHandle.SetMuthurId("bb31-49ea-95e5-a49fcce6588f-4ef3457e");

		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		readyRunResp1->SetFedExecHandle(fedExecHandle);

		WIN_ASSERT_NOT_NULL(readyRunResp1);

		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", readyRunResp1->GetFedExecHandle().GetMuthurId());

		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(readyRunResp1, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(readyRunResp1, "C:\\AmbassadorReadyToRunResponseXmlTest.xml");

		dStream->SetRead(true);

		CMLAddDataSubResponse* readyRunResp2 = new CMLAddDataSubResponse();
		CMLHelper::LoadXmlStream(readyRunResp2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(readyRunResp2);
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", readyRunResp2->GetFedExecHandle().GetMuthurId());
		if(readyRunResp1)
		{
			delete readyRunResp1;
			readyRunResp1 = NULL;
		}
		if(readyRunResp2)
		{
			delete readyRunResp2;
			readyRunResp2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLTerminateParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLTerminateParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorFederationTerminationEventXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLTerminateParams* terminateParams1 = new CMLTerminateParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecModelHandle = CMLHandle();
		fedExecModelHandle.SetMuthurId("49ea-95e5-a49fcce6588f-4ef3457e-bb31");
		CMLHandle fedExecHandle = CMLHandle();
		fedExecHandle.SetMuthurId("bb31-49ea-95e5-a49fcce6588f-4ef3457e");

		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		terminateParams1->SetFedExecHandle(fedExecHandle);
		terminateParams1->SetFedExecModelHandle(fedExecModelHandle);
		terminateParams1->SetRequestType(ML_REQUEST_SYSTEM);
		WIN_ASSERT_NOT_NULL(terminateParams1);

		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", terminateParams1->GetFedExecHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", terminateParams1->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_EQUAL(ML_REQUEST_SYSTEM, terminateParams1->GetRequestType());
		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(terminateParams1, dStream);

		if(GENERATE_TEST_XML_FILES == true)
			CMLHelper::SaveXmlStream(terminateParams1, "C:\\AmbassadorFederationTerminationEventXmlTest.xml");

		dStream->SetRead(true);

		CMLTerminateParams* terminateParams2 = new CMLTerminateParams();
		CMLHelper::LoadXmlStream(terminateParams2, dStream->GetBuffer());

		WIN_ASSERT_NOT_NULL(terminateParams2);
		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", terminateParams2->GetFedExecHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", terminateParams2->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_EQUAL(ML_REQUEST_SYSTEM, terminateParams2->GetRequestType());
		if(terminateParams1)
		{
			delete terminateParams1;
			terminateParams1 = NULL;
		}
		if(terminateParams2)
		{
			delete terminateParams2;
			terminateParams2 = NULL;
		}
	}
	END_TESTF

		//***************************************************************************************************************************
		// Test a round trip serialization(create data object CMLPubDataParams, serialize to XML, and re-creates as much of the object from
		// XML as possible.  Values assigned to fields in CMLPubDataParams should be the stay the same
		// ***************************************************************************************************************************
		BEGIN_TESTF(AmbassadorDataPublicationXmlTest, AmbassadorTestDataFixture)
	{
		//Uncomment below to see memory leaks 

#ifdef _DEBUG
		int tmpFlag = _CrtSetDbgFlag( _CRTDBG_REPORT_FLAG );
		tmpFlag |= _CRTDBG_CHECK_ALWAYS_DF;
		tmpFlag |= _CRTDBG_LEAK_CHECK_DF;
		_CrtSetDbgFlag( tmpFlag );
#endif


		CMLAmbassador::GetInstance().SetFederateName("NexSimFed");
		CMLAmbassador::GetInstance().SetTimeOutSecs(50);
		CMLPubDataParams* pubDataParams1 = new CMLPubDataParams();
		CMLHandle fedRegHandle = CMLHandle();
		fedRegHandle.SetMuthurId("4ef3457e-bb31-49ea-95e5-a49fcce6588f");
		CMLHandle fedExecModelHandle = CMLHandle();
		fedExecModelHandle.SetMuthurId("49ea-95e5-a49fcce6588f-4ef3457e-bb31");
		CMLHandle fedExecHandle = CMLHandle();
		fedExecHandle.SetMuthurId("bb31-49ea-95e5-a49fcce6588f-4ef3457e");

		CMLAmbassador::GetInstance().GetFedRegHandle().SetMuthurId(fedRegHandle.GetMuthurId());
		pubDataParams1->SetFedExecHandle(fedExecHandle);
		pubDataParams1->SetFedExecModelHandle(fedExecModelHandle);
		pubDataParams1->SetRequestType(ML_REQUEST_DATA);
		
		CMLSpawnAircraft *ac = new CMLSpawnAircraft();
		ac->SetACId("DAL123");
		ac->SetACTailNumber("ABCD");
		ac->SetCreatedTimestamp(5);
		ac->SetUUID("1234");
		ac->SetDeptAirportCode("BWI");
		ac->SetArrivalAirportCode("DFW");
		ac->SetPitchDegrees(135246);	
		ac->SetLatitudeDegrees(1324);	
		ac->SetLongitudeDegrees(2413);
		ac->SetAltitudeFt(567765);		
		ac->SetGroundspeedKts(678);	
		ac->SetHeadingDegrees(130);	
		ac->SetAirspeedKts(236);		
		ac->SetRollDegrees(156);		
		//ac->SetYawDegrees(128);		
		ac->SetSector("N");			
		ac->SetCenter("66");			
		ac->SetActualDepartureTimeMSecs(99887766);
		ac->SetTaxiOutTimeMSecs(87766);
		ac->SetAircraftType("B737");				
		ac->SetPlannedDepartureTimeMSecs(113355);	
		ac->SetPlannedArrivalTimeMSecs(335577);		
		ac->SetCruiseSpeedKts(224466);				
		ac->SetCruiseAltitudeFeet(112233);			
		ac->SetRoute("BWI...IAD");						
		ac->SetDepartureCenter("66");				
		ac->SetArrivalCenter("45");				
		ac->SetDepartureFix("JAN");				
		ac->SetArrivalFix("BNA");					
		ac->SetPhysicalClass("heavy");				
		ac->SetWeightClass("large");					
		ac->SetEquipmentQualifier("Used");	
		ac->SetNumAircraft(3);

		pubDataParams1->SetDataObject(ac);

		WIN_ASSERT_NOT_NULL(pubDataParams1);

		WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", pubDataParams1->GetFedExecHandle().GetMuthurId());
		WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", pubDataParams1->GetFedExecModelHandle().GetMuthurId());
		WIN_ASSERT_EQUAL(ML_REQUEST_DATA, pubDataParams1->GetRequestType());
		WIN_ASSERT_EQUAL(135246, ac->GetPitchDegrees());
		WIN_ASSERT_EQUAL(112233, ac->GetCruiseAltitudeFeet());
		WIN_ASSERT_EQUAL(1324, ac->GetLatitudeDegrees());
		WIN_ASSERT_EQUAL(2413, ac->GetLongitudeDegrees());
		WIN_ASSERT_EQUAL(567765, ac->GetAltitudeFt());
		WIN_ASSERT_EQUAL(678, ac->GetGroundspeedKts());
		WIN_ASSERT_EQUAL(130, ac->GetHeadingDegrees());
		WIN_ASSERT_EQUAL(236, ac->GetAirspeedKts());
		WIN_ASSERT_EQUAL(156, ac->GetRollDegrees());
		//WIN_ASSERT_EQUAL(128, ac->GetYawDegrees());
		WIN_ASSERT_STRING_EQUAL("N", ac->GetSector());
		WIN_ASSERT_STRING_EQUAL("66", ac->GetCenter());
		WIN_ASSERT_EQUAL(99887766,ac->GetActualDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(87766,ac->GetTaxiOutTimeMSecs());
		WIN_ASSERT_STRING_EQUAL("B737", ac->GetAircraftType());
		WIN_ASSERT_EQUAL(113355, ac->GetPlannedDepartureTimeMSecs());
		WIN_ASSERT_EQUAL(335577, ac->GetPlannedArrivalTimeMSecs());
		WIN_ASSERT_EQUAL(224466, ac->GetCruiseSpeedKts());
		WIN_ASSERT_EQUAL(112233, ac->GetCruiseAltitudeFeet());
		WIN_ASSERT_STRING_EQUAL("BWI...IAD", ac->GetRoute());
		WIN_ASSERT_EQUAL(87766, ac->GetTaxiOutTimeMSecs());
		WIN_ASSERT_STRING_EQUAL("66", ac->GetDepartureCenter());
		WIN_ASSERT_STRING_EQUAL("45", ac->GetArrivalCenter());
		WIN_ASSERT_STRING_EQUAL("JAN", ac->GetDepartureFix());
		WIN_ASSERT_STRING_EQUAL("BNA", ac->GetArrivalFix());
		WIN_ASSERT_STRING_EQUAL("heavy", ac->GetPhysicalClass());
		WIN_ASSERT_STRING_EQUAL("large", ac->GetWeightClass());
		WIN_ASSERT_STRING_EQUAL("Used", ac->GetEquipmentQualifier());
		WIN_ASSERT_EQUAL(3, ac->GetNumAircraft());
		CMLDataStream *dStream = new CMLDataStream();
		dStream->SetRead(false);

		CMLHelper::GetXmlStream(pubDataParams1, dStream);

		if(GENERATE_TEST_XML_FILES)
			CMLHelper::SaveXmlStream(pubDataParams1, "C:\\AmbassadorDataPublicationXmlTest.xml");

		dStream->SetRead(true);

		/*CMLPubDataParams* pubDataParams2 = new CMLPubDataParams();
		CMLHelper::LoadXmlStream(pubDataParams2, dStream->GetBuffer());*/

		//WIN_ASSERT_NOT_NULL(pubDataParams2);
		//WIN_ASSERT_STRING_EQUAL("bb31-49ea-95e5-a49fcce6588f-4ef3457e", pubDataParams2->GetFedExecHandle().GetMuthurId());
		//WIN_ASSERT_STRING_EQUAL("49ea-95e5-a49fcce6588f-4ef3457e-bb31", pubDataParams2->GetFedExecModelHandle().GetMuthurId());
		//WIN_ASSERT_EQUAL(ML_REQUEST_DATA, pubDataParams2->GetRequestType());
		if(pubDataParams1)
		{
			delete pubDataParams1;
			pubDataParams1 = NULL;
		}
		/*if(pubDataParams2)
		{
			delete pubDataParams2;
			pubDataParams2 = NULL;
		}*/
	}
	END_TESTF
}
