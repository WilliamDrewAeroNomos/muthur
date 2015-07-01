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

#include <activemq/wireformat/openwire/marshal/v6/DataResponseMarshaller.h>

#include <activemq/commands/DataResponse.h>
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
using namespace activemq::wireformat::openwire::marshal::v6;
using namespace decaf;
using namespace decaf::io;
using namespace decaf::lang;

///////////////////////////////////////////////////////////////////////////////
DataStructure* DataResponseMarshaller::createObject() const {
    return new DataResponse();
}

///////////////////////////////////////////////////////////////////////////////
unsigned char DataResponseMarshaller::getDataStructureType() const {
    return DataResponse::ID_DATARESPONSE;
}

///////////////////////////////////////////////////////////////////////////////
void DataResponseMarshaller::tightUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        ResponseMarshaller::tightUnmarshal( wireFormat, dataStructure, dataIn, bs );

        DataResponse* info =
            dynamic_cast<DataResponse*>( dataStructure );
        info->setData( Pointer<DataStructure>( dynamic_cast< DataStructure* >(
            tightUnmarshalNestedObject( wireFormat, dataIn, bs ) ) ) );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
int DataResponseMarshaller::tightMarshal1( OpenWireFormat* wireFormat, DataStructure* dataStructure, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        DataResponse* info =
            dynamic_cast<DataResponse*>( dataStructure );

        int rc = ResponseMarshaller::tightMarshal1( wireFormat, dataStructure, bs );
        rc += tightMarshalNestedObject1( wireFormat, info->getData().get(), bs );

        return rc + 0;
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void DataResponseMarshaller::tightMarshal2( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        ResponseMarshaller::tightMarshal2( wireFormat, dataStructure, dataOut, bs );

        DataResponse* info =
            dynamic_cast<DataResponse*>( dataStructure );
        tightMarshalNestedObject2( wireFormat, info->getData().get(), dataOut, bs );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void DataResponseMarshaller::looseUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn ) throw( decaf::io::IOException ) {

    try {

        ResponseMarshaller::looseUnmarshal( wireFormat, dataStructure, dataIn );
        DataResponse* info =
            dynamic_cast<DataResponse*>( dataStructure );
        info->setData( Pointer<DataStructure>( dynamic_cast< DataStructure* >( 
            looseUnmarshalNestedObject( wireFormat, dataIn ) ) ) );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void DataResponseMarshaller::looseMarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut ) throw( decaf::io::IOException ) {

    try {

        DataResponse* info =
            dynamic_cast<DataResponse*>( dataStructure );
        ResponseMarshaller::looseMarshal( wireFormat, dataStructure, dataOut );

        looseMarshalNestedObject( wireFormat, info->getData().get(), dataOut );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

