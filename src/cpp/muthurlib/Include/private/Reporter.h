#pragma once

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <string>
#include <windows.h>

//------------------------------------------------------------------------------
//	DEFINES & CONSTANTS
//------------------------------------------------------------------------------

#define REPORTER_DEFAULT_APP_NAME		"muthur"
#define REPORTER_DEFAULT_ERROR_FILENAME	"errors"
#define REPORTER_DEFAULT_DEBUG_FILENAME	"debug"
#define REPORTER_DEFAULT_EXTENSION		".txt"

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

/*! \enum EReportLevel
// Enumerated identifiers used to specify the severity of the message being
// reported by the application.
*/
typedef enum 
{
	REPORTER_LEVEL_INFO = 0,	//!< Message being reported is for general information
	REPORTER_LEVEL_WARNING,		//!< Condition should be reported but it's not major
	REPORTER_LEVEL_CRITICAL,	//!< Major error but application can proceed
	REPORTER_LEVEL_FATAL,		//!< Major error resulting in application termination
	REPORTER_LEVEL_DEBUG,		//!< Message is intended for diagnostic purposes

}EReporterLevel;

//------------------------------------------------------------------------------
//! \brief Class wrapper for an individual error or diagnostic message
//------------------------------------------------------------------------------
class CReporterMessage
{
	public:
	
		std::string			m_strModule;
		std::string			m_strMethod;
		std::string			m_strMessage;
		EReporterLevel		m_eLevel;
	
							CReporterMessage()
							{
								m_strModule  = "";
								m_strMethod  = "";
								m_strMessage = "";
								m_eLevel     = REPORTER_LEVEL_INFO;
							}
								
							CReporterMessage(const CReporterMessage& rSource)
							{
								m_strModule  = rSource.m_strModule;
								m_strMethod  = rSource.m_strMethod;
								m_strMessage = rSource.m_strMessage;
								m_eLevel     = rSource.m_eLevel;
							}	
	
};

//------------------------------------------------------------------------------
//! \brief Class used to report runtime diagnostic and error messages
//------------------------------------------------------------------------------
class CReporter
{
	private:			
	
		CReporter*			m_pAttached;			//!< Parent reporter attached to this reporter
		CReporterMessage	m_lastErrorMessage;		//!< The last error message processed by this reporter
		CReporterMessage	m_lastDebugMessage;		//!< The last debug message processed by this reporter
		std::string			m_strAppName;			//!< The name of the application
		std::string			m_strModuleName;		//!< The default name of the module associated with the report
		std::string			m_strErrorFileSpec;		//!< Fully qualified path to error log file
		std::string			m_strDebugFileSpec;		//!< Fully qualified path to debug log file
		bool				m_bErrorLogEnabled;		//!< Flag to enable/disable logging of error messages
		bool				m_bDebugLogEnabled;		//!< Flag to enable/disable logging of debug messages
		HWND				m_hErrorWnd;			//!< Handle to application window to use for error messages
		HWND				m_hDebugWnd;			//!< Handle to application window to use for debug messages
		UINT				m_uErrorMessageId;		//!< Message to be sent for error notifications
		UINT				m_uDebugMessageId;		//!< Message to be sent for debug notifications
		
	public:
	
							CReporter();
						   ~CReporter();
						   
		//	Public Access
		CReporter*			GetAttached(){ return m_pAttached; }
		CReporterMessage&	GetLastErrorMessage(){ return m_lastErrorMessage; }
		CReporterMessage&	GetLastDebugMessage(){ return m_lastDebugMessage; }
		std::string			GetErrorFileSpec(){ return m_strErrorFileSpec; }
		std::string			GetDebugFileSpec(){ return m_strDebugFileSpec; }
		std::string			GetAppName(){ return m_strAppName; }
		std::string			GetModuleName(){ return m_strModuleName; }
		bool				GetErrorLogEnabled(){ return m_bErrorLogEnabled; }
		bool				GetDebugLogEnabled(){ return m_bDebugLogEnabled; }
		HWND				GetErrorWnd(){ return m_hErrorWnd; }
		HWND				GetDebugWnd(){ return m_hDebugWnd; }
		UINT				GetErrorMessageId(){ return m_uErrorMessageId; }
		UINT				GetDebugMessageId(){ return m_uDebugMessageId; }
		
		void				SetAttached(CReporter* pAttach){ m_pAttached = pAttach; }
		void				SetErrorFileSpec(char* pszFileSpec);
		void				SetDebugFileSpec(char* pszFileSpec);
		void				SetAppName(char* pszAppName);
		void				SetModuleName(char* pszModuleName);
		void				SetErrorLogEnabled(bool bEnabled){ m_bErrorLogEnabled = bEnabled; }
		void				SetDebugLogEnabled(bool bEnabled){ m_bDebugLogEnabled = bEnabled; }
		void				SetErrorWnd(HWND hWnd){ m_hErrorWnd = hWnd; }
		void				SetDebugWnd(HWND hWnd){ m_hDebugWnd = hWnd; }
		void				SetErrorMessageId(UINT uMessageId){ m_uErrorMessageId = uMessageId; }
		void				SetDebugMessageId(UINT uMessageId){ m_uDebugMessageId = uMessageId; }
		
		//	Reporting methods
		void				Errorf(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszFormat, ...);
		void				Errorf(EReporterLevel eLevel, char* pszModule, char* pszMethod, UINT uFormatId, ...);
		void				Warning(char* pszModule, char* pszMethod, char* pszFormat, ...);
		void				Critical(char* pszModule, char* pszMethod, char* pszFormat, ...);
		void				Fatal(char* pszModule, char* pszMethod, char* pszFormat, ...);
		void				Debug(char* pszModule, char* pszMethod, char* pszFormat, ...);

	protected:
	
		void				OnError(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszFormat, va_list paArgs);
		void				OnDebug(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszFormat, va_list paArgs);

		char*				GetAppName(char* pszName, int iSize);
		char*				GetAppFolder(char* pszFolder, int iSize);
		std::string			GetDefaultFileSpec(bool bErrors);
		std::string			GetFileSpec(char* pszSource, bool bErrors);
		std::string			GetLabel(EReporterLevel eLevel);
		std::string			GetTimeStamp();
		std::string			GetFileSpec(EReporterLevel eLevel);
		std::string			GetLogStr(char* pszModule, char* pszMethod, char* pszMessage);
		std::string			GetMsgBoxMessage(EReporterLevel eLevel, char* pszModule, char* pszMethod, char* pszMessage);
		std::string			GetMsgBoxTitle(EReporterLevel eLevel);
		UINT				GetMsgBoxIcon(EReporterLevel eLevel);
		bool				IsEnabled(EReporterLevel eLevel);
		bool				IsError(EReporterLevel eLevel);
		bool				Write(EReporterLevel eLevel, char* pszMessage);
		bool				GetLongPath(char* pszSource, char* pszExpanded, int iSize);
		
};

