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

#include <activemq/wireformat/openwire/marshal/v2/ProducerIdMarshaller.h>

#include <activemq/commands/ProducerId.h>
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
using namespace activemq::wireformat::openwire::marshal::v2;
using namespace decaf;
using namespace decaf::io;
using namespace decaf::lang;

///////////////////////////////////////////////////////////////////////////////
DataStructure* ProducerIdMarshaller::createObject() const {
    return new ProducerId();
}

///////////////////////////////////////////////////////////////////////////////
unsigned char ProducerIdMarshaller::getDataStructureType() const {
    return ProducerId::ID_PRODUCERID;
}

///////////////////////////////////////////////////////////////////////////////
void ProducerIdMarshaller::tightUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        BaseDataStreamMarshaller::tightUnmarshal( wireFormat, dataStructure, dataIn, bs );

        ProducerId* info =
            dynamic_cast<ProducerId*>( dataStructure );
        info->setConnectionId( tightUnmarshalString( dataIn, bs ) );
        info->setValue( tightUnmarshalLong( wireFormat, dataIn, bs ) );
        info->setSessionId( tightUnmarshalLong( wireFormat, dataIn, bs ) );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
int ProducerIdMarshaller::tightMarshal1( OpenWireFormat* wireFormat, DataStructure* dataStructure, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        ProducerId* info =
            dynamic_cast<ProducerId*>( dataStructure );

        int rc = BaseDataStreamMarshaller::tightMarshal1( wireFormat, dataStructure, bs );
        rc += tightMarshalString1( info->getConnectionId(), bs );
        rc += tightMarshalLong1( wireFormat, info->getValue(), bs );
        rc += tightMarshalLong1( wireFormat, info->getSessionId(), bs );

        return rc + 0;
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void ProducerIdMarshaller::tightMarshal2( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        BaseDataStreamMarshaller::tightMarshal2( wireFormat, dataStructure, dataOut, bs );

        ProducerId* info =
            dynamic_cast<ProducerId*>( dataStructure );
        tightMarshalString2( info->getConnectionId(), dataOut, bs );
        tightMarshalLong2( wireFormat, info->getValue(), dataOut, bs );
        tightMarshalLong2( wireFormat, info->getSessionId(), dataOut, bs );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void ProducerIdMarshaller::looseUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn ) throw( decaf::io::IOException ) {

    try {

        BaseDataStreamMarshaller::looseUnmarshal( wireFormat, dataStructure, dataIn );
        ProducerId* info =
            dynamic_cast<ProducerId*>( dataStructure );
        info->setConnectionId( looseUnmarshalString( dataIn ) );
        info->setValue( looseUnmarshalLong( wireFormat, dataIn ) );
        info->setSessionId( looseUnmarshalLong( wireFormat, dataIn ) );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void ProducerIdMarshaller::looseMarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut ) throw( decaf::io::IOException ) {

    try {

        ProducerId* info =
            dynamic_cast<ProducerId*>( dataStructure );
        BaseDataStreamMarshaller::looseMarshal( wireFormat, dataStructure, dataOut );

        looseMarshalString( info->getConnectionId(), dataOut );
        looseMarshalLong( wireFormat, info->getValue(), dataOut );
        looseMarshalLong( wireFormat, info->getSessionId(), dataOut );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

