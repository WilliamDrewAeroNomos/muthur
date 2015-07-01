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

#include <activemq/wireformat/openwire/marshal/v4/LocalTransactionIdMarshaller.h>

#include <activemq/commands/LocalTransactionId.h>
#include <activemq/exceptions/ActiveMQException.h>
#include <decaf/lang/Pointer.h>

//
//     NOTE!: This file is autogenerated - do not modify!
//            if you need to make a change, please see the Java Classes in the
//            activemq-core module
//

using namespace std;
using namespace activemq;
using namespace activemq::exceptions;
using namespace activemq::commands;
using namespace activemq::wireformat;
using namespace activemq::wireformat::openwire;
using namespace activemq::wireformat::openwire::marshal;
using namespace activemq::wireformat::openwire::utils;
using namespace activemq::wireformat::openwire::marshal::v4;
using namespace decaf;
using namespace decaf::io;
using namespace decaf::lang;

///////////////////////////////////////////////////////////////////////////////
DataStructure* LocalTransactionIdMarshaller::createObject() const {
    return new LocalTransactionId();
}

///////////////////////////////////////////////////////////////////////////////
unsigned char LocalTransactionIdMarshaller::getDataStructureType() const {
    return LocalTransactionId::ID_LOCALTRANSACTIONID;
}

///////////////////////////////////////////////////////////////////////////////
void LocalTransactionIdMarshaller::tightUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        TransactionIdMarshaller::tightUnmarshal( wireFormat, dataStructure, dataIn, bs );

        LocalTransactionId* info =
            dynamic_cast<LocalTransactionId*>( dataStructure );
        info->setValue( tightUnmarshalLong( wireFormat, dataIn, bs ) );
        info->setConnectionId( Pointer<ConnectionId>( dynamic_cast< ConnectionId* >(
            tightUnmarshalCachedObject( wireFormat, dataIn, bs ) ) ) );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
int LocalTransactionIdMarshaller::tightMarshal1( OpenWireFormat* wireFormat, DataStructure* dataStructure, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        LocalTransactionId* info =
            dynamic_cast<LocalTransactionId*>( dataStructure );

        int rc = TransactionIdMarshaller::tightMarshal1( wireFormat, dataStructure, bs );
        rc += tightMarshalLong1( wireFormat, info->getValue(), bs );
        rc += tightMarshalCachedObject1( wireFormat, info->getConnectionId().get(), bs );

        return rc + 0;
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void LocalTransactionIdMarshaller::tightMarshal2( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        TransactionIdMarshaller::tightMarshal2( wireFormat, dataStructure, dataOut, bs );

        LocalTransactionId* info =
            dynamic_cast<LocalTransactionId*>( dataStructure );
        tightMarshalLong2( wireFormat, info->getValue(), dataOut, bs );
        tightMarshalCachedObject2( wireFormat, info->getConnectionId().get(), dataOut, bs );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void LocalTransactionIdMarshaller::looseUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn ) throw( decaf::io::IOException ) {

    try {

        TransactionIdMarshaller::looseUnmarshal( wireFormat, dataStructure, dataIn );
        LocalTransactionId* info =
            dynamic_cast<LocalTransactionId*>( dataStructure );
        info->setValue( looseUnmarshalLong( wireFormat, dataIn ) );
        info->setConnectionId( Pointer<ConnectionId>( dynamic_cast< ConnectionId* >( 
            looseUnmarshalCachedObject( wireFormat, dataIn ) ) ) );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void LocalTransactionIdMarshaller::looseMarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut ) throw( decaf::io::IOException ) {

    try {

        LocalTransactionId* info =
            dynamic_cast<LocalTransactionId*>( dataStructure );
        TransactionIdMarshaller::looseMarshal( wireFormat, dataStructure, dataOut );

        looseMarshalLong( wireFormat, info->getValue(), dataOut );
        looseMarshalCachedObject( wireFormat, info->getConnectionId().get(), dataOut );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

