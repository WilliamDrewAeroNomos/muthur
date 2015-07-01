//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <Reporter.h>
#include <MLApi.h>

//-----------------------------------------------------------------------------
//	DEFINES & CONSTANTS
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! Summary:	Constructor
//! 
//! Parameters:	None
//! 
//! Returns:	None
//------------------------------------------------------------------------------
CReporter::CReporter()
{
	m_pAttached				= NULL;
	m_bErrorLogEnabled		= true;
	m_hErrorWnd				= NULL;
	m_hDebugWnd				= NULL;
	m_strModuleName			= "";
	m_strDebugFileSpec		= "";
	m_strErrorFileSpec		= "";
	m_strAppName			= "";
	m_uErrorMessageId		= 0;
	m_uDebugMessageId		= 0;
	
	//	Default is to turn off diagnostics in release builds
	#ifdef _DEBUG
		m_bDebugLogEnabled	= true;
	#else
		m_bDebugLogEnabled	= false;
	#endif
	
	//	Set the default names
	SetAppName(REPORTER_DEFAULT_APP_NAME);
	SetModuleName("");
}

//------------------------------------------------------------------------------
//! Summary:	Destructor
//! 
//! Parameters:	None
//! 
//! Returns:	None
//------------------------------------------------------------------------------
CReporter::~CReporter()
{
}

//------------------------------------------------------------------------------
//! Summary:	Called to report a critical error message
//! 
//! Parameters:	\li lpszModule - the name of the calling module
//! 			\li lpszMethod - the name of the calling method
//! 			\li lpszFormat - the printf style format string
//! 			\li ... - optional list of arguments 
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::Critical(char* pszModule, char* pszMethod, char* pszFormat, ...)
{
	if(IsEnabled(REPORTER_LEVEL_CRITICAL) == true)
	{
		//	Declare the variable list of arguements            
		va_list	Args;

		//	Insert the first variable arguement into the arguement list
		va_start(Args, pszFormat);

		//	Process the message
		OnError(REPORTER_LEVEL_CRITICAL, pszModule, pszMethod, pszFormat, Args);

		//	Clean up the arguement list
		va_end(Args);
		
	}// if(IsEnabled(REPORTER_LEVEL_CRITICAL) == true)	
}

//------------------------------------------------------------------------------
//! Summary:	Called to report a debug message
//! 
//! Parameters:	\li pszModule - the name of the calling module
//! 			\li pszMethod - the name of the calling method
//! 			\li pszFormat - the printf style format string
//! 			\li ... - optional list of arguments 
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::Debug(char* pszModule, char* pszMethod, char* pszFormat, ...)
{
	if(IsEnabled(REPORTER_LEVEL_DEBUG) == true)
	{
		//	Declare the variable list of arguements            
		va_list	Args;

		//	Insert the first variable arguement into the arguement list
		va_start(Args, pszFormat);

		//	Process the message
		OnDebug(REPORTER_LEVEL_DEBUG, pszModule, pszMethod, pszFormat, Args);

		//	Clean up the arguement list
		va_end(Args);
		
	}// if(IsEnabled(eLevel) == true)	
}

//------------------------------------------------------------------------------
//! Summary:	Standard C-style printf method for reporting errors
//! 
//! Parameters:	eLevel - enumerated message level identifier
//!				pszModule - the name of the calling module
//! 			pszMethod - the name of the calling method
//! 			pszFormat - the printf style format string
//! 			... - optional list of arguments 
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::Errorf(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszFormat, ...)
{
	if(IsEnabled(eLevel) == true)
	{
		//	Declare the variable list of arguements            
		va_list	Args;

		//	Insert the first variable arguement into the arguement list
		va_start(Args, pszFormat);

		//	Process the message
		OnError(eLevel, pszModule, pszMethod, pszFormat, Args);

		//	Clean up the arguement list
		va_end(Args);
		
	}// if(IsEnabled(eLevel) == true)	
}

//------------------------------------------------------------------------------
//! Summary:	Called to report a fatal error message
//! 
//! Parameters:	\li pszModule - the name of the calling module
//! 			\li pszMethod - the name of the calling method
//! 			\li pszFormat - the printf style format string
//! 			\li ... - optional list of arguments 
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::Fatal(char* pszModule, char* pszMethod, char* pszFormat, ...)
{
	if(IsEnabled(REPORTER_LEVEL_FATAL) == true)
	{
		//	Declare the variable list of arguements            
		va_list	Args;

		//	Insert the first variable arguement into the arguement list
		va_start(Args, pszFormat);

		//	Process the message
		OnError(REPORTER_LEVEL_FATAL, pszModule, pszMethod, pszFormat, Args);

		//	Clean up the arguement list
		va_end(Args);
		
	}// if(IsEnabled(REPORTER_LEVEL_FATAL) == true)	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the folder containing the host application
//!
//!	Parameters:	\li pszFolder - buffer in which to store the folder path
//!				\li iSize - size of the specified buffer
//!
//!	Returns:	The path to the application folder
//------------------------------------------------------------------------------
char* CReporter::GetAppFolder(char* pszFolder, int iSize)
{
	char	szFileSpec[1024];
	char*	pToken = NULL;

	//	Get the path to the application executable
	//
	//	NOTE:	We pass NULL instead of hInstance because we want the path
	//			to the application - not the OCX control or DLL
	if(GetModuleFileName(NULL, szFileSpec, sizeof(szFileSpec)) > 0)
	{
		GetLongPath(szFileSpec, szFileSpec, sizeof(szFileSpec));
	}

	//	Now strip the filename
	if(lstrlen(szFileSpec) > 0)
	{
		if((pToken = strrchr(szFileSpec, '\\')) != NULL)
			*(pToken + 1) = '\0';
		lstrcpyn(pszFolder, szFileSpec, iSize);
	}
	
	return pszFolder;
}

//------------------------------------------------------------------------------
//! Summary:	Called to set the default path for the desired log file
//! 
//! Parameters:	\li bErrors - true for error logging
//! 
//! Returns:	The fully qualified path to the desired log file
//------------------------------------------------------------------------------
std::string CReporter::GetDefaultFileSpec(bool bErrors)
{
	SYSTEMTIME	sysTime;
	char		szFolder[512];
	char		szDate[32];
	char		szFileSpec[1024];
	std::string	strFileSpec = "";

	//	Construct formatted date stamp
	GetLocalTime(&sysTime);
	sprintf_s(szDate, sizeof(szDate), "%04d%02d%02d", sysTime.wYear, sysTime.wMonth, sysTime.wDay);

	//	Assume the file is to be located in the application folder
	GetAppFolder(szFolder, sizeof(szFolder));
		
	//	Build the fully qualified path
	if(bErrors == true)
	{
		sprintf_s(szFileSpec, sizeof(szFileSpec), "%s%s_%s_%s%s", szFolder, m_strAppName.c_str(), REPORTER_DEFAULT_ERROR_FILENAME, szDate, REPORTER_DEFAULT_EXTENSION);
	}
	else
	{
		sprintf_s(szFileSpec, sizeof(szFileSpec), "%s%s_%s_%s%s", szFolder, m_strAppName.c_str(), REPORTER_DEFAULT_DEBUG_FILENAME, szDate, REPORTER_DEFAULT_EXTENSION);
	}
	
	strFileSpec = szFileSpec;
	return strFileSpec;
}

//------------------------------------------------------------------------------
//! Summary:	Called to get a fully qualified file specification for the
//! 			log file associated with the specified report level
//! 
//! Parameters:	\li eLevel - the enumerated report level identifier
//! 
//! Returns:	The fully qualified path to the associated log file
//------------------------------------------------------------------------------
std::string CReporter::GetFileSpec(EReporterLevel eLevel)
{
	std::string strFileSpec = "";
	
	//	Are we looking for the error log?
	if(IsError(eLevel))
	{
		//	Do we need to initialize the class member?
		if(m_strErrorFileSpec.length() == 0)
			SetErrorFileSpec(""); // Assigns default value
			
		strFileSpec = m_strErrorFileSpec;
	}
	else
	{
		if(m_strDebugFileSpec.length() == 0)
			SetDebugFileSpec("");
			
		strFileSpec = m_strDebugFileSpec;
	}
	
	return strFileSpec;
}

//------------------------------------------------------------------------------
//! Summary:	Called to get a fully qualified file specification using the
//! 			source path specified by the caller
//! 
//! Parameters:	\li lpszSource - the source path and/or name to use
//! 			\li bErrors - true if called for the error log file spec
//! 
//! Returns:	The fully qualified path
//------------------------------------------------------------------------------
std::string CReporter::GetFileSpec(char* pszSource, bool bErrors)
{
	std::string strFileSpec = "";
	char		szFolder[512];
	
	//	Did the caller provide a valid source path?
	if((pszSource != NULL) && (lstrlen(pszSource) > 0))
	{
		//	Did the caller specify an absolute path?
		if((strchr(pszSource, ':') != NULL) || (strncmp(pszSource, "\\\\", 2) == 0))
		{
			strFileSpec = pszSource;
		}
		else
		{
			//	Assume the file is to be located in the application folder
			strFileSpec = GetAppFolder(szFolder, sizeof(szFolder));
			strFileSpec += pszSource;
		}
			
	}
	else
	{
		//	Construct the default path specification
		strFileSpec = GetDefaultFileSpec(bErrors);
	}
	
	return strFileSpec;
}

//------------------------------------------------------------------------------
//! Summary:	Called to get the text label for the specified report level
//! 
//! Parameters:	\li eLevel - The enumerated report level
//! 
//! Returns:	The text description of the specified report level
//------------------------------------------------------------------------------
std::string CReporter::GetLabel(EReporterLevel eLevel)
{
	std::string strLabel = "";
	
	switch(eLevel)
	{
		case REPORTER_LEVEL_INFO:
			strLabel = "Information";
			break;
			
		case REPORTER_LEVEL_WARNING:
			strLabel = "Warning";
			break;
			
		case REPORTER_LEVEL_CRITICAL:
			strLabel = "Critical";
			break;
			
		case REPORTER_LEVEL_FATAL:
			strLabel = "Fatal";
			break;
			
		case REPORTER_LEVEL_DEBUG:
			strLabel = "Debug";
			break;		
			
		default:
		
			strLabel = "UNKNOWN";
			break;
			
	}//	switch(eLevel)
	
	return strLabel;
}

//------------------------------------------------------------------------------
//! Summary:	Called to get the string to be written to the log file
//! 
//! Parameters:	\li pszModule  - the name of the calling module
//! 			\li pszMethod  - the name of the calling method
//! 			\li pszMessage - the error / diagnostic message
//! 
//! Returns:	The formatted string appropriate for logging
//------------------------------------------------------------------------------
std::string CReporter::GetLogStr(char* pszModule, char* pszMethod, char* pszMessage)
{
	std::string	strModule = "";
	std::string	strMethod = "";
	std::string	strFormatted = "";
	char		szLogStr[2048];
	
	//	Assign the application name if the caller did not provide the module name
	if((pszModule != NULL) && (lstrlen(pszModule) > 0))
		strModule = pszModule;
	else
		strModule = m_strAppName.c_str();
		
	//	Insert a placeholder for the method if not specified by the caller
	if((pszMethod != NULL) && (lstrlen(pszMethod) > 0))
		strMethod = pszMethod;
	else
		strMethod = "(?)   "; // Default placeholder
		
	//	Construct the formatted string
	sprintf_s(szLogStr, sizeof(szLogStr), "%-16s\t%-16s\t%s", strModule.c_str(), strMethod.c_str(), pszMessage);
	strFormatted = szLogStr;
	
	return strFormatted;	
}

//------------------------------------------------------------------------------
//! Summary:	Called convert the 8.3 path to a fully qualified long path
//!
//!	Parameters:	\li pszSource - the path to be parsed
//!				\li pszExpanded  - the buffer in which to store the long path
//!				\li iSize - size of the buffer
//!
//!	Returns:	true if able to perform the conversion
//------------------------------------------------------------------------------
bool CReporter::GetLongPath(char* pszSource, char* pszExpanded, int iSize)
{
	WIN32_FIND_DATA	fd;
	HANDLE			hFile;
	char*			pToken;
	char			szBuffer[1024];
	char			szLongFilename[2048];
	char			szPrevious[2048];

	memset(szLongFilename, 0, sizeof(szLongFilename));
	memset(szPrevious, 0, sizeof(szPrevious));
	
	//	Initialize the working buffer
	if((pszSource != NULL) && (lstrlen(pszSource) > 0))
		lstrcpyn(szBuffer, pszSource, sizeof(szBuffer));
	else
		memset(szBuffer, 0, sizeof(szBuffer));

	while(1)
	{
		if((pToken = strrchr(szBuffer, '\\')) != 0)
		{
			if((hFile = FindFirstFile(szBuffer, &fd)) != 0)
			{
				//	Now substitute the caller's filename for the name returned by 
				//	the system
				if(lstrlen(szLongFilename) > 0)
				{
					lstrcpyn(szPrevious, szLongFilename, sizeof(szPrevious));
					_snprintf_s(szLongFilename, sizeof(szLongFilename), _TRUNCATE, "%s\\%s", fd.cFileName, szPrevious);
				}
				else
				{
					lstrcpyn(szLongFilename, fd.cFileName, sizeof(szLongFilename));
				}

				FindClose(hFile);				
			}
			else
			{
				//	Use the tokenized portion if not found
				//
				//	NOTE: This should only happen if the path doesn't exist
				if(lstrlen(szLongFilename) > 0)
				{
					lstrcpyn(szPrevious, szLongFilename, sizeof(szPrevious));
					_snprintf_s(szLongFilename, sizeof(szLongFilename), _TRUNCATE, "%s\\%s", (pToken + 1), szPrevious);
				}
				else
				{
					lstrcpyn(szLongFilename, (pToken + 1), sizeof(szLongFilename));
				}

			}
		
			//	Chop off this level
			*pToken = '\0';
		
		}
		else
		{
			//	We should be left with the UNC server or the drive specification
			if(lstrlen(szBuffer) > 0)
			{
				if(lstrlen(szLongFilename) > 0)
				{
					lstrcpyn(szPrevious, szLongFilename, sizeof(szPrevious));
					_snprintf_s(szLongFilename, sizeof(szLongFilename), _TRUNCATE, "%s\\%s", szBuffer, szPrevious);
				}
				else
				{
					lstrcpyn(szLongFilename, szBuffer, sizeof(szLongFilename));
				}
			}

			break;
		}

	}// while(1)

	if(lstrlen(szLongFilename) > 0)
		lstrcpyn(pszExpanded, szLongFilename, iSize);
	else
		lstrcpyn(pszExpanded, pszSource, iSize);

	return true;
}

//------------------------------------------------------------------------------
//! Summary:	Called to get the appropriate Windows MessageBox() icon for the
//! 			specified report level.
//! 
//! Parameters:	\li eLevel - The enumerated report level
//! 
//! Returns:	The icon used to display the message in a standard message box
//------------------------------------------------------------------------------
UINT CReporter::GetMsgBoxIcon(EReporterLevel eLevel)
{
	UINT uMBIcon = 0;

	switch(eLevel)
	{
		case REPORTER_LEVEL_INFO:
			uMBIcon = (MB_ICONINFORMATION | MB_OK);
			break;
			
		case REPORTER_LEVEL_WARNING:
			uMBIcon = (MB_ICONWARNING | MB_OK);
			break;
			
		case REPORTER_LEVEL_CRITICAL:
			uMBIcon = (MB_ICONEXCLAMATION | MB_OK);
			break;
						
		case REPORTER_LEVEL_FATAL:
			uMBIcon = (MB_ICONSTOP | MB_OK);
			break;
						
		case REPORTER_LEVEL_DEBUG:
			uMBIcon = (MB_ICONINFORMATION | MB_OK);
			break;
			
		default:
		
			uMBIcon = (MB_ICONQUESTION | MB_OK);
			break;
			
	}//	switch(eLevel)
	
	return uMBIcon;
}

//------------------------------------------------------------------------------
//! Summary:	Called to construct the text to be displayed in the message box
//! 
//! Parameters:	\li eLevel      - the enumerated reporter level
//!			\li pszModule  - the name of the calling module
//! 			\li pszMethod  - the name of the calling method
//! 			\li pszMessage - the message text
//! 
//! Returns:	The text to be displayed in the message box
//------------------------------------------------------------------------------
std::string CReporter::GetMsgBoxMessage(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszMessage)
{
	std::string	strMsgBox = "";
	char		szMsgBox[1024];
	
	//	Is this an error message?
	if(IsError(eLevel) == true)
	{
		strMsgBox = pszMessage;
	}
	else
	{
		//	Did the caller provide the module identifier?
		if((pszModule != NULL) && (lstrlen(pszModule) > 0))
		{
			sprintf_s(szMsgBox, sizeof(szMsgBox), "Module: %s\n", pszModule); 
			strMsgBox = szMsgBox;
		}

		//	Did the caller provide the method name?
		if((pszMethod != NULL) && (lstrlen(pszMethod) > 0))
		{
			strMsgBox += "Method: ";
			strMsgBox += pszMethod;
			strMsgBox += "\n";
		}
		
		if(strMsgBox.length() > 0)
		{
			strMsgBox += "\n";
		}
		
		//	Now append the formatted message
		strMsgBox += pszMessage;

	}// if(IsError(eLevel) == true)

	return strMsgBox;
}

//------------------------------------------------------------------------------
//! Summary:	Called to construct the title for a message box used to display
//!			the associated error / diagnostic message
//! 
//! Parameters:	\li eLevel - the enumerated report level of the message
//! 
//! Returns:	The appropriate title string for the message box
//------------------------------------------------------------------------------
std::string CReporter::GetMsgBoxTitle(EReporterLevel eLevel)
{
	std::string	strTitle = "";
	char		szTitle[128];

	//	Now append the report level identifier
	if(IsError(eLevel) == true)
	{
		strTitle += GetLabel(eLevel);
		
		switch(eLevel)
		{
			case REPORTER_LEVEL_CRITICAL:
			case REPORTER_LEVEL_FATAL:
				strTitle += " Error";
				break;
		}
						
	}
	else
	{
		sprintf_s(szTitle, sizeof(szTitle), "%s Diagnostics", m_strAppName.c_str());
		strTitle = szTitle;
	}
	
	return strTitle;
}

//------------------------------------------------------------------------------
//! Summary:	Called to get a time stamp associated with the current system
//! 			time
//! 
//! Parameters:	None
//! 
//! Returns:	The time stamp suitable for logging
//------------------------------------------------------------------------------
std::string CReporter::GetTimeStamp()
{
	SYSTEMTIME	sysTime;
	std::string	strStamp = "";
	char		szStamp[64];
	
	GetLocalTime(&sysTime);

	//	Construct formatted stamp
	sprintf_s(szStamp, sizeof(szStamp), "%02d:%02d:%02d.%03d", sysTime.wHour, 
	          sysTime.wMinute, sysTime.wSecond, sysTime.wMilliseconds);
		
	strStamp = szStamp;
	return strStamp;
}

//------------------------------------------------------------------------------
//! Summary:	Called to determine if messages of the specified label are
//! 			enabled and should be reported.
//! 
//! Parameters:	\li eLevel - The enumerated report level
//! 
//! Returns:	true if messages of the specified type should be reported
//------------------------------------------------------------------------------
bool CReporter::IsEnabled(EReporterLevel eLevel)
{
	bool bEnabled = false;
	
	//	Is this an error message?
	if(IsError(eLevel) == true)
	{
		if((m_hErrorWnd != NULL) && (IsWindow(m_hErrorWnd) != 0))
			bEnabled = true;
		else if(m_bErrorLogEnabled == true)
			bEnabled = true;
	}
	else
	{
		if((m_hDebugWnd != NULL) && (IsWindow(m_hDebugWnd) != 0))
			bEnabled = true;
		else if(m_bDebugLogEnabled == true)
			bEnabled = true;
	}
	
	return bEnabled;
}

//------------------------------------------------------------------------------
//! Summary:	Called to determine if the messages associated with the 
//! 			specified report level are error messages
//! 
//! Parameters:	\li eLevel - The enumerated report level
//! 
//! Returns:	true if messages of the specified type are errors
//------------------------------------------------------------------------------
bool CReporter::IsError(EReporterLevel eLevel)
{
	bool bError = false;
	
	switch(eLevel)
	{
		case REPORTER_LEVEL_INFO:
		case REPORTER_LEVEL_WARNING:
		case REPORTER_LEVEL_CRITICAL:
		case REPORTER_LEVEL_FATAL:
		
			bError = true;
			break;
			
		case REPORTER_LEVEL_DEBUG:
		
			bError = false;
			break;
			
		default:
		
			bError = false;
			break;
			
	}//	switch(eLevel)
	
	return bError;
}

//------------------------------------------------------------------------------
//! Summary:	Called to process a request to report a debug message
//! 
//! Parameters:	\li eLevel     - the enumerated report level of the message
//! 			\li pszModule - the name of the calling module
//! 			\li pszMethod - the name of the calling method
//! 			\li pszFormat - the printf style format string
//! 			\li paArgs	   - list of arguments 
//! 
//! Returns:	None
//! 
//! Remarks:	This method assumes the caller has already determined if debug
//! 			reporting has been enabled.
//------------------------------------------------------------------------------
void CReporter::OnDebug(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszFormat, va_list paArgs)
{
	char		szMessage[2048];
	std::string	strConsole = "";
	std::string	strModule = "";
	std::string	strLogStr = "";
	std::string	strMsgBox = "";
	
	//	Did the caller specify the module name?
	if((pszModule != NULL) && (lstrlen(pszModule) > 0))
		strModule = pszModule;
	else
		strModule = m_strModuleName;

	//	Should we bubble this message?
	if(m_pAttached != NULL)
	{
		m_pAttached->OnDebug(eLevel, (char*)(strModule.c_str()), pszMethod, pszFormat, paArgs);
	}
	else
	{
		//	Format the message
		if(paArgs != NULL)
			vsprintf_s(szMessage, sizeof(szMessage), pszFormat, paArgs);
		else
			lstrcpyn(szMessage, pszFormat, sizeof(szMessage));

		//	Save these values
		m_lastDebugMessage.m_eLevel     = eLevel;
		m_lastDebugMessage.m_strModule  = strModule;
		m_lastDebugMessage.m_strMethod  = pszMethod ? pszMethod : "";
		m_lastDebugMessage.m_strMessage = szMessage;
		
		//	Should we send to the log?
		if(m_bDebugLogEnabled == true)
		{
			strLogStr = GetLogStr((char*)(strModule.c_str()), pszMethod, szMessage);
			Write(eLevel, (char*)(strLogStr.c_str()));
		}
		
		if((m_hDebugWnd != NULL) && (IsWindow(m_hDebugWnd) != 0))
		{
			//	Are we posting a message?
			if(m_uDebugMessageId > 0)
			{
				::PostMessage(m_hDebugWnd, m_uDebugMessageId, 0, LPARAM(new CReporterMessage(m_lastDebugMessage)));
			}
			else
			{
				strMsgBox = GetMsgBoxMessage(eLevel, (char*)(strModule.c_str()), pszMethod, szMessage);
				MessageBox(m_hDebugWnd, (char*)(strMsgBox.c_str()),
						  (char*)(GetMsgBoxTitle(eLevel).c_str()), GetMsgBoxIcon(eLevel)); 
			}
			
		}
		else
		{
			strConsole = strModule;

			//	Did the caller provide the method name?
			if((pszMethod != NULL) && (lstrlen(pszMethod) > 0))
			{
				if(strConsole.length() > 0)
					strConsole += "::";
				strConsole += pszMethod;
			}
			
			if(strConsole.length() > 0)
			{
				strConsole += " -> ";
			}
			
			//	Now append the formatted message
			strConsole += szMessage;
			
			//	Send to the default debug stream
			OutputDebugString(strConsole.c_str());
			OutputDebugString("\n");
		}
			
	}// if(m_pAttached != NULL)
	
}

//------------------------------------------------------------------------------
//! 	Summary:	Called to process a request to report an error
//! 
//! 	Parameters:	\li eLevel     - the enumerated report level of the message
//! 				\li pszModule - the name of the calling module
//! 				\li pszMethod - the name of the calling method
//! 				\li pszFormat - the printf style format string
//! 				\li paArgs	   - list of arguments 
//! 
//! 	Returns:	None
//! 
//! 	Remarks:	This method assumes the caller has already determined if error
//! 				reporting has been enabled.
//------------------------------------------------------------------------------
void CReporter::OnError(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszFormat, va_list paArgs)
{
	char		szMessage[2048];
	std::string	strModule = "";
	std::string	strLogStr = "";
	std::string	strMsgBox = "";
	
	//	Did the caller specify the module name?
	if((pszModule != NULL) && (lstrlen(pszModule) > 0))
		strModule = pszModule;
	else
		strModule = m_strModuleName;

	//	Should we bubble this message?
	if(m_pAttached != NULL)
	{
		m_pAttached->OnError(eLevel, (char*)(strModule.c_str()), pszMethod, pszFormat, paArgs);
	}
	else
	{
		//	Format the message
		if(paArgs != NULL)
			vsprintf_s(szMessage, sizeof(szMessage),pszFormat, paArgs);
		else
			lstrcpyn(szMessage, pszFormat, sizeof(szMessage));
			
		//	Save these values
		m_lastErrorMessage.m_eLevel     = eLevel;
		m_lastErrorMessage.m_strModule  = strModule;
		m_lastErrorMessage.m_strMethod  = pszMethod ? pszMethod : "";
		m_lastErrorMessage.m_strMessage = szMessage;
		
		//	Should we send to the error log?
		if(m_bErrorLogEnabled == true)
		{
			strLogStr = GetLogStr((char*)(strModule.c_str()), pszMethod, szMessage);
			Write(eLevel, (char*)(strLogStr.c_str()));
		}

		// Do we have a valid error message window
		if((m_hErrorWnd != NULL) && (IsWindow(m_hErrorWnd) != 0))
		{
			//	Are we posting messages?
			if(m_uErrorMessageId > 0)
			{
				::PostMessage(m_hErrorWnd, m_uErrorMessageId, 0, LPARAM(new CReporterMessage(m_lastErrorMessage)));
			}
			else
			{
				strMsgBox = GetMsgBoxMessage(eLevel, (char*)(strModule.c_str()), pszMethod, szMessage);
				MessageBox(m_hErrorWnd, (char*)(strMsgBox.c_str()),
						   (char*)(GetMsgBoxTitle(eLevel).c_str()), GetMsgBoxIcon(eLevel)); 
			}
			
		}
	
	}// if(m_pAttached != NULL)
	
}

//------------------------------------------------------------------------------
//! Summary:	Called to set the name of the host application
//! 
//! Parameters:	\li pszAppName - the name of the application
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::SetAppName(char* pszAppName)
{
	if((pszAppName != NULL) && (lstrlen(pszAppName) > 0))
	{
		m_strAppName = pszAppName;
	}
	else
	{
		m_strAppName = REPORTER_DEFAULT_APP_NAME;
	}
}

//------------------------------------------------------------------------------
//! Summary:	Called to set the fully qualified path to the file used for
//! 			logging debug messages
//! 
//! Parameters:	\li pszFileSpec - the path to the debug log file
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::SetDebugFileSpec(char* pszFileSpec)
{
	m_strDebugFileSpec = GetFileSpec(pszFileSpec, false);
}

//------------------------------------------------------------------------------
//! Summary:	Called to set the fully qualified path to the file used for
//! 			error logging
//! 
//! Parameters:	\li pszFileSpec - the path to the error log file
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::SetErrorFileSpec(char* pszFileSpec)
{
	m_strErrorFileSpec = GetFileSpec(pszFileSpec, true);
}

//------------------------------------------------------------------------------
//! Summary:	Called to set the name used to identify the module associated
//!				with messages reported by this object.
//! 
//! Parameters:	\li pszName - the name to assign
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::SetModuleName(char* pszModuleName)
{
	if((pszModuleName != NULL) && (lstrlen(pszModuleName) > 0))
	{
		m_strModuleName = pszModuleName;
	}
	else
	{
		m_strModuleName = m_strAppName;
	}
}

//------------------------------------------------------------------------------
//! Summary:	Called to report a warning message
//! 
//! Parameters:	\li pszModule - the name of the calling module
//! 			\li pszMethod - the name of the calling method
//! 			\li pszFormat - the printf style format string
//! 			\li ... - optional list of arguments 
//! 
//! Returns:	None
//------------------------------------------------------------------------------
void CReporter::Warning(char* pszModule, char* pszMethod, char* pszFormat, ...)
{
	if(IsEnabled(REPORTER_LEVEL_WARNING) == true)
	{
		//	Declare the variable list of arguements            
		va_list	Args;

		//	Insert the first variable arguement into the arguement list
		va_start(Args, pszFormat);

		//	Process the message
		OnError(REPORTER_LEVEL_WARNING, pszModule, pszMethod, pszFormat, Args);

		//	Clean up the arguement list
		va_end(Args);
		
	}// if(IsEnabled(eLevel) == true)	
}
							
//------------------------------------------------------------------------------
// 	Summary:	Called to write the specified message to the associated log
// 
// 	Parameters:	eLevel - the enumerated report level of the message
// 				lpszFormat - the message to be written
// 
// 	Returns:	true if successful
//------------------------------------------------------------------------------
bool CReporter::Write(EReporterLevel eLevel, char* pszMessage)
{
	bool	bSuccessful = false;
	bool	bIncludeLabel = true;
	FILE*	fptr = NULL;
	std::string	strFileSpec = "";
	
	//	Get the appropriate file specification for this report level
	strFileSpec = GetFileSpec(eLevel);
	
	if(strFileSpec.length() > 0)
	{
		//	Are we logging a debug message?
		if(IsError(eLevel) == false)
		{
			//	Don't include the report level in the output if we have a separate debug log
			//
			//	NOTE:	We do this because we only have one debug level. If we add more than one
			//			we may want to change this to include the level in the debug log
			if(lstrcmpi(strFileSpec.c_str(), m_strErrorFileSpec.c_str()) != 0)
			{
				bIncludeLabel = false;
			}
			
		}// if(IsError(eLevel) == false)
		
		//	Open the log file
		if((fopen_s(&fptr, strFileSpec.c_str(), "at") == 0) && (fptr != NULL))
		{
			if(bIncludeLabel == true)
			{
				fprintf(fptr, "%s\t%-10s\t%s\n", GetTimeStamp().c_str(), GetLabel(eLevel).c_str(), pszMessage);
			}
			else
			{
				fprintf(fptr, "%s\t%s\n", GetTimeStamp().c_str(), pszMessage);
			}
			
			fclose(fptr);
			bSuccessful = true;
		
		}// if(fptr != NULL)
	
	}// if(strFileSpec.length() > 0)
	
	return bSuccessful;
}



