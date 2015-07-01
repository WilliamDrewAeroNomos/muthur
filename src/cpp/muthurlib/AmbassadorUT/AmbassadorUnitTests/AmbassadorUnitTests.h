// The following ifdef block is the standard way of creating macros which make exporting 
// from a DLL simpler. All files within this DLL are compiled with the AMBASSADORUNITTESTS_EXPORTS
// symbol defined on the command line. this symbol should not be defined on any project
// that uses this DLL. This way any other project whose source files include this file see 
// AMBASSADORUNITTESTS_API functions as being imported from a DLL, whereas this DLL sees symbols
// defined with this macro as being exported.
#ifdef AMBASSADORUNITTESTS_EXPORTS
#define AMBASSADORUNITTESTS_API __declspec(dllexport)
#else
#define AMBASSADORUNITTESTS_API __declspec(dllimport)
#endif

// This class is exported from the AmbassadorUnitTests.dll
class AMBASSADORUNITTESTS_API CAmbassadorUnitTests {
public:
	CAmbassadorUnitTests(void);
	// TODO: add your methods here.
};

extern AMBASSADORUNITTESTS_API int nAmbassadorUnitTests;

AMBASSADORUNITTESTS_API int fnAmbassadorUnitTests(void);
