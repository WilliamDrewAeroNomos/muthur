/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <activemq/commands/ShutdownInfo.h>
#include <activemq/exceptions/ActiveMQException.h>
#include <activemq/state/CommandVisitor.h>
#include <decaf/lang/exceptions/NullPointerException.h>

using namespace std;
using namespace activemq;
using namespace activemq::exceptions;
using namespace activemq::commands;
using namespace decaf::lang;
using namespace decaf::lang::exceptions;

/*
 *
 *  Command code for OpenWire format for ShutdownInfo
 *
 *  NOTE!: This file is auto generated - do not modify!
 *         if you need to make a change, please see the Java Classes in the
 *         activemq-cpp-openwire-generator module
 *
 */

////////////////////////////////////////////////////////////////////////////////
ShutdownInfo::ShutdownInfo() 
    : BaseCommand() {

}

////////////////////////////////////////////////////////////////////////////////
ShutdownInfo::~ShutdownInfo() {
}

////////////////////////////////////////////////////////////////////////////////
ShutdownInfo* ShutdownInfo::cloneDataStructure() const {
    std::auto_ptr<ShutdownInfo> shutdownInfo( new ShutdownInfo() );

    // Copy the data from the base class or classes
    shutdownInfo->copyDataStructure( this );

    return shutdownInfo.release();
}

////////////////////////////////////////////////////////////////////////////////
void ShutdownInfo::copyDataStructure( const DataStructure* src ) {

    // Protect against invalid self assignment.
    if( this == src ) {
        return;
    }

    const ShutdownInfo* srcPtr = dynamic_cast<const ShutdownInfo*>( src );

    if( srcPtr == NULL || src == NULL ) {
        throw decaf::lang::exceptions::NullPointerException(
            __FILE__, __LINE__,
            "ShutdownInfo::copyDataStructure - src is NULL or invalid" );
    }

    // Copy the data of the base class or classes
    BaseCommand::copyDataStructure( src );

}

////////////////////////////////////////////////////////////////////////////////
unsigned char ShutdownInfo::getDataStructureType() const {
    return ShutdownInfo::ID_SHUTDOWNINFO;
}

////////////////////////////////////////////////////////////////////////////////
std::string ShutdownInfo::toString() const {

    ostringstream stream;

    stream << "ShutdownInfo { "
           << "commandId = " << this->getCommandId() << ", "
           << "responseRequired = " << boolalpha << this->isResponseRequired();
    stream << " }";

    return stream.str();
}

////////////////////////////////////////////////////////////////////////////////
bool ShutdownInfo::equals( const DataStructure* value ) const {

    if( this == value ) {
        return true;
    }

    const ShutdownInfo* valuePtr = dynamic_cast<const ShutdownInfo*>( value );

    if( valuePtr == NULL || value == NULL ) {
        return false;
    }

    if( !BaseCommand::equals( value ) ) {
        return false;
    }
    return true;
}

////////////////////////////////////////////////////////////////////////////////
decaf::lang::Pointer<commands::Command> ShutdownInfo::visit( activemq::state::CommandVisitor* visitor ) 
    throw( activemq::exceptions::ActiveMQException ) {

    return visitor->processShutdownInfo( this );
}
