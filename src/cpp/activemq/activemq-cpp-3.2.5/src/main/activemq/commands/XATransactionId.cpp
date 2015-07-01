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

#include <activemq/commands/XATransactionId.h>
#include <activemq/exceptions/ActiveMQException.h>
#include <activemq/state/CommandVisitor.h>
#include <apr_strings.h>
#include <decaf/lang/exceptions/NullPointerException.h>

using namespace std;
using namespace activemq;
using namespace activemq::exceptions;
using namespace activemq::commands;
using namespace decaf::lang;
using namespace decaf::lang::exceptions;

/*
 *
 *  Command code for OpenWire format for XATransactionId
 *
 *  NOTE!: This file is auto generated - do not modify!
 *         if you need to make a change, please see the Java Classes in the
 *         activemq-cpp-openwire-generator module
 *
 */

////////////////////////////////////////////////////////////////////////////////
XATransactionId::XATransactionId() 
    : TransactionId(), formatId(0), globalTransactionId(), branchQualifier() {

}

////////////////////////////////////////////////////////////////////////////////
XATransactionId::XATransactionId( const XATransactionId& other )
    : TransactionId(), formatId(0), globalTransactionId(), branchQualifier() {

    this->copyDataStructure( &other );
}

////////////////////////////////////////////////////////////////////////////////
XATransactionId::~XATransactionId() {
}

////////////////////////////////////////////////////////////////////////////////
XATransactionId* XATransactionId::cloneDataStructure() const {
    std::auto_ptr<XATransactionId> xATransactionId( new XATransactionId() );

    // Copy the data from the base class or classes
    xATransactionId->copyDataStructure( this );

    return xATransactionId.release();
}

////////////////////////////////////////////////////////////////////////////////
void XATransactionId::copyDataStructure( const DataStructure* src ) {

    // Protect against invalid self assignment.
    if( this == src ) {
        return;
    }

    const XATransactionId* srcPtr = dynamic_cast<const XATransactionId*>( src );

    if( srcPtr == NULL || src == NULL ) {
        throw decaf::lang::exceptions::NullPointerException(
            __FILE__, __LINE__,
            "XATransactionId::copyDataStructure - src is NULL or invalid" );
    }

    // Copy the data of the base class or classes
    TransactionId::copyDataStructure( src );

    this->setFormatId( srcPtr->getFormatId() );
    this->setGlobalTransactionId( srcPtr->getGlobalTransactionId() );
    this->setBranchQualifier( srcPtr->getBranchQualifier() );
}

////////////////////////////////////////////////////////////////////////////////
unsigned char XATransactionId::getDataStructureType() const {
    return XATransactionId::ID_XATRANSACTIONID;
}

////////////////////////////////////////////////////////////////////////////////
std::string XATransactionId::toString() const {

    ostringstream stream;

    stream << "XATransactionId { ";
    stream << "FormatId = " << this->getFormatId();
    stream << ", ";
    stream << "GlobalTransactionId = ";
    if( this->getGlobalTransactionId().size() > 0 ) {
        stream << "[";
        for( size_t iglobalTransactionId = 0; iglobalTransactionId < this->getGlobalTransactionId().size(); ++iglobalTransactionId ) {
            stream << this->getGlobalTransactionId()[iglobalTransactionId] << ",";
        }
        stream << "]";
    } else {
        stream << "NULL";
    }
    stream << ", ";
    stream << "BranchQualifier = ";
    if( this->getBranchQualifier().size() > 0 ) {
        stream << "[";
        for( size_t ibranchQualifier = 0; ibranchQualifier < this->getBranchQualifier().size(); ++ibranchQualifier ) {
            stream << this->getBranchQualifier()[ibranchQualifier] << ",";
        }
        stream << "]";
    } else {
        stream << "NULL";
    }
    stream << " }";

    return stream.str();
}

////////////////////////////////////////////////////////////////////////////////
bool XATransactionId::equals( const DataStructure* value ) const {

    if( this == value ) {
        return true;
    }

    const XATransactionId* valuePtr = dynamic_cast<const XATransactionId*>( value );

    if( valuePtr == NULL || value == NULL ) {
        return false;
    }

    if( this->getFormatId() != valuePtr->getFormatId() ) {
        return false;
    }
    for( size_t iglobalTransactionId = 0; iglobalTransactionId < this->getGlobalTransactionId().size(); ++iglobalTransactionId ) {
        if( this->getGlobalTransactionId()[iglobalTransactionId] != valuePtr->getGlobalTransactionId()[iglobalTransactionId] ) {
            return false;
        }
    }
    for( size_t ibranchQualifier = 0; ibranchQualifier < this->getBranchQualifier().size(); ++ibranchQualifier ) {
        if( this->getBranchQualifier()[ibranchQualifier] != valuePtr->getBranchQualifier()[ibranchQualifier] ) {
            return false;
        }
    }
    if( !TransactionId::equals( value ) ) {
        return false;
    }
    return true;
}

////////////////////////////////////////////////////////////////////////////////
int XATransactionId::getFormatId() const {
    return formatId;
}

////////////////////////////////////////////////////////////////////////////////
void XATransactionId::setFormatId( int formatId ) {
    this->formatId = formatId;
}

////////////////////////////////////////////////////////////////////////////////
const std::vector<unsigned char>& XATransactionId::getGlobalTransactionId() const {
    return globalTransactionId;
}

////////////////////////////////////////////////////////////////////////////////
std::vector<unsigned char>& XATransactionId::getGlobalTransactionId() {
    return globalTransactionId;
}

////////////////////////////////////////////////////////////////////////////////
void XATransactionId::setGlobalTransactionId( const std::vector<unsigned char>& globalTransactionId ) {
    this->globalTransactionId = globalTransactionId;
}

////////////////////////////////////////////////////////////////////////////////
const std::vector<unsigned char>& XATransactionId::getBranchQualifier() const {
    return branchQualifier;
}

////////////////////////////////////////////////////////////////////////////////
std::vector<unsigned char>& XATransactionId::getBranchQualifier() {
    return branchQualifier;
}

////////////////////////////////////////////////////////////////////////////////
void XATransactionId::setBranchQualifier( const std::vector<unsigned char>& branchQualifier ) {
    this->branchQualifier = branchQualifier;
}

////////////////////////////////////////////////////////////////////////////////
int XATransactionId::compareTo( const XATransactionId& value ) const {

    if( this == &value ) {
        return 0;
    }

    if( this->formatId > value.formatId ) {
        return 1;
    } else if( this->formatId < value.formatId ) {
        return -1;
    }

    if( this->globalTransactionId > value.globalTransactionId ) {
        return 1;
    } else if( this->globalTransactionId < value.globalTransactionId ) {
        return -1;
    }

    if( this->branchQualifier > value.branchQualifier ) {
        return 1;
    } else if( this->branchQualifier < value.branchQualifier ) {
        return -1;
    }

    return 0;
}

////////////////////////////////////////////////////////////////////////////////
bool XATransactionId::equals( const XATransactionId& value ) const {
    return this->equals( &value );
}

////////////////////////////////////////////////////////////////////////////////
bool XATransactionId::operator==( const XATransactionId& value ) const {
    return this->compareTo( value ) == 0;
}

////////////////////////////////////////////////////////////////////////////////
bool XATransactionId::operator<( const XATransactionId& value ) const {
    return this->compareTo( value ) < 0;
}

////////////////////////////////////////////////////////////////////////////////
XATransactionId& XATransactionId::operator= ( const XATransactionId& other ) {
    this->copyDataStructure( &other );
    return *this;
}

