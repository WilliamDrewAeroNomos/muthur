// MuthurApp.h : main header file for the PROJECT_NAME application
//

#pragma once

#ifndef __AFXWIN_H__
	#error "include 'stdafx.h' before including this file for PCH"
#endif

#include "resource.h"		// main symbols


// CMuthurApp:
// See MuthurApp.cpp for the implementation of this class
//

class CMuthurApp : public CWinApp
{
public:
	CMuthurApp();

// Overrides
	public:
	virtual BOOL InitInstance();

// Implementation

	DECLARE_MESSAGE_MAP()
};

extern CMuthurApp theApp;
