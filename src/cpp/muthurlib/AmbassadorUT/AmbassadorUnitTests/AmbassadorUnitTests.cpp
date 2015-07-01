// AmbassadorUnitTests.cpp : Defines the exported functions for the DLL application.
//
#pragma warning( disable : 4290 )

#include "stdafx.h"
#include "AmbassadorUnitTests.h"


// This is an example of an exported variable
AMBASSADORUNITTESTS_API int nAmbassadorUnitTests=0;

// This is an example of an exported function.
AMBASSADORUNITTESTS_API int fnAmbassadorUnitTests(void)
{
	return 42;
}

// This is the constructor of a class that has been exported.
// see AmbassadorUnitTests.h for the class definition
CAmbassadorUnitTests::CAmbassadorUnitTests()
{
	return;
}

