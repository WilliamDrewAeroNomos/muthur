//------------------------------------------------------------------------------
/*! \file	MQApi.h
//
//  Contains inclusions and declarations used by the ActiveMQ API
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-11-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__MQ_API_H__)
#define __MQ_API_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------


#include <activemq/library/ActiveMQCPP.h>
#include <decaf/lang/Thread.h>
#include <decaf/lang/Runnable.h>
#include <decaf/util/concurrent/CountDownLatch.h>
#include <decaf/lang/Integer.h>
#include <decaf/lang/Long.h>
#include <decaf/lang/System.h>
#include <activemq/core/ActiveMQConnectionFactory.h>
#include <activemq/util/Config.h>
#include <cms/Connection.h>
#include <cms/Session.h>
#include <cms/TextMessage.h>
#include <cms/BytesMessage.h>
#include <cms/MapMessage.h>
#include <cms/ExceptionListener.h>
#include <cms/MessageListener.h>


#include <stdlib.h>
#include <stdio.h>
#include <iostream>
#include <memory>


using namespace activemq::core;
using namespace decaf::util::concurrent;
using namespace decaf::util;
using namespace decaf::io;
using namespace decaf::lang;
using namespace cms;
using namespace std;

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------


#endif // !defined(__MQ_API_H__)
