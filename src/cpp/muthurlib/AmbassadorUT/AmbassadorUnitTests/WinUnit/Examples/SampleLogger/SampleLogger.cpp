/*--------------------------------------------------------------------------*
  
  SampleLogger.cpp

  This file contains a sample implementation for a custom Logger for WinUnit.
  This implementation writes output to a file, whose path/name is specified
  in the Initialize function.

 *--------------------------------------------------------------------------*/
#include "WinUnitLogger.h"
#include <stdio.h>
#include <windows.h>

// We open this file in Initialize and close it in OutputFinalResults.
FILE* g_fileStream = NULL;

/// Initialize the logger with any required data.
WINUNIT_LOGGER_FUNCTION(void, Initialize)(const wchar_t* initializationString)
{
    if (initializationString == NULL || *initializationString == L'\0')
    {
        fwprintf(stderr, L"[SampleLogger] Filename expected.\n");
        return;
    }
    // Initialization string is expected to be a file name.
    errno_t result = _wfopen_s(&g_fileStream, initializationString, L"wtc");
    if (result != 0 || g_fileStream == NULL) 
    { 
        fwprintf(stderr, L"[SampleLogger] Error opening file for write: %s.\n", initializationString); 
    } 
}

/// Outputs the full path and just the name of the executable tests
/// are being run from, before running the tests.
WINUNIT_LOGGER_FUNCTION(void, OutputTestExecutableNamePreTest)(
    const wchar_t* fullPath, 
    const wchar_t* nameOfExecutable)
{
    if (g_fileStream)
    {
        fwprintf(g_fileStream, L"Executable: %s (%s).\n", nameOfExecutable, fullPath);
    }
}

/// Outputs the full path and just the name of the executable tests
/// are being run from, after running the tests.
WINUNIT_LOGGER_FUNCTION(void, OutputTestExecutableNamePostTest)(
    const wchar_t* fullPath, 
    const wchar_t* nameOfExecutable, 
    unsigned int succeeded,       ///< Number of tests succeeded
    unsigned int totalRun)        ///< Number of tests run
{
    if (g_fileStream)
    {
        fwprintf(g_fileStream, L"Executable: %s (%s): %d/%d.\n", 
            nameOfExecutable, fullPath, succeeded, totalRun);
    }
}

/// Outputs the name of the test about to be run.
WINUNIT_LOGGER_FUNCTION(void, OutputTestNamePreTest)(const char* testName)
{
    if (g_fileStream)
    {
        fprintf(g_fileStream, "Test: %s.\n", testName);
    }
}

/// Outputs the name of the test after it has been run, and whether 
/// or not it passed.
WINUNIT_LOGGER_FUNCTION(void, OutputTestNamePostTest)(
    const char* testName, 
    bool passed)
{
    if (g_fileStream)
    {
        fprintf(g_fileStream, "Test: %s (%s).\n", testName,
            passed? "PASSED" : "FAILED");
    }
}

/// Output a trace message.  This comes from any OutputDebugString
/// calls that have been made in the test executable.
/// @remarks Messages are expected to contain newlines where appropriate.
/// Message may be a continuation of a previous message.
WINUNIT_LOGGER_FUNCTION(void, OutputTrace)(const wchar_t* message)
{
    if (g_fileStream)
    {
        fwprintf(g_fileStream, L"INFO: %s", message);
    }
}

/// Output a message from a failed test.
/// @remarks Message is not expected to end with a newline.
WINUNIT_LOGGER_FUNCTION(void, OutputTestError)(const wchar_t* message)
{
    if (g_fileStream)
    {
        fwprintf(g_fileStream, L"ERROR: %s\n", message);
    }
}

/// Output an error message that describes an unexpected occurrence in the 
/// program (e.g. file not found).
/// @remarks The line passed in will be a complete error message, but does not
/// include a newline.
WINUNIT_LOGGER_FUNCTION(void, OutputProgramErrorLine)(
    const wchar_t* message
)
{
    if (g_fileStream)
    {
        fwprintf(g_fileStream, L"PROGRAM ERROR: %s.\n", message);
    }
}

/// Output final results from all tests run.
WINUNIT_LOGGER_FUNCTION(void, OutputFinalResults)(
    unsigned int succeeded, 
    unsigned int totalRun)
{
    if (g_fileStream != NULL)
    {
        fwprintf(g_fileStream, L"FINAL RESULTS: %d/%d.\n", succeeded, totalRun);
        fclose(g_fileStream);
    }
}

/// Set verbosity level.
WINUNIT_LOGGER_FUNCTION(void, SetVerbosity)(unsigned int verbosity)
{
    if (g_fileStream != NULL)
    {
        fwprintf(g_fileStream, L"Setting verbosity to %d.\n", verbosity);
    }
}
