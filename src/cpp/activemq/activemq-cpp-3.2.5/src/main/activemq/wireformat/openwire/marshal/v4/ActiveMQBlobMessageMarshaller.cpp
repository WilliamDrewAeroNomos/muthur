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

#include <activemq/wireformat/openwire/marshal/v4/ActiveMQBlobMessageMarshaller.h>

#include <activemq/commands/ActiveMQBlobMessage.h>
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
DataStructure* ActiveMQBlobMessageMarshaller::createObject() const {
    return new ActiveMQBlobMessage();
}

///////////////////////////////////////////////////////////////////////////////
unsigned char ActiveMQBlobMessageMarshaller::getDataStructureType() const {
    return ActiveMQBlobMessage::ID_ACTIVEMQBLOBMESSAGE;
}

///////////////////////////////////////////////////////////////////////////////
void ActiveMQBlobMessageMarshaller::tightUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        MessageMarshaller::tightUnmarshal( wireFormat, dataStructure, dataIn, bs );

        ActiveMQBlobMessage* info =
            dynamic_cast<ActiveMQBlobMessage*>( dataStructure );
        info->setRemoteBlobUrl( tightUnmarshalString( dataIn, bs ) );
        info->setMimeType( tightUnmarshalString( dataIn, bs ) );
        info->setDeletedByBroker( bs->readBoolean() );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
int ActiveMQBlobMessageMarshaller::tightMarshal1( OpenWireFormat* wireFormat, DataStructure* dataStructure, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        ActiveMQBlobMessage* info =
            dynamic_cast<ActiveMQBlobMessage*>( dataStructure );

        int rc = MessageMarshaller::tightMarshal1( wireFormat, dataStructure, bs );
        rc += tightMarshalString1( info->getRemoteBlobUrl(), bs );
        rc += tightMarshalString1( info->getMimeType(), bs );
        bs->writeBoolean( info->isDeletedByBroker() );

        return rc + 0;
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void ActiveMQBlobMessageMarshaller::tightMarshal2( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut, BooleanStream* bs ) throw( decaf::io::IOException ) {

    try {

        MessageMarshaller::tightMarshal2( wireFormat, dataStructure, dataOut, bs );

        ActiveMQBlobMessage* info =
            dynamic_cast<ActiveMQBlobMessage*>( dataStructure );
        tightMarshalString2( info->getRemoteBlobUrl(), dataOut, bs );
        tightMarshalString2( info->getMimeType(), dataOut, bs );
        bs->readBoolean();
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void ActiveMQBlobMessageMarshaller::looseUnmarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataInputStream* dataIn ) throw( decaf::io::IOException ) {

    try {

        MessageMarshaller::looseUnmarshal( wireFormat, dataStructure, dataIn );
        ActiveMQBlobMessage* info =
            dynamic_cast<ActiveMQBlobMessage*>( dataStructure );
        info->setRemoteBlobUrl( looseUnmarshalString( dataIn ) );
        info->setMimeType( looseUnmarshalString( dataIn ) );
        info->setDeletedByBroker( dataIn->readBoolean() );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

///////////////////////////////////////////////////////////////////////////////
void ActiveMQBlobMessageMarshaller::looseMarshal( OpenWireFormat* wireFormat, DataStructure* dataStructure, DataOutputStream* dataOut ) throw( decaf::io::IOException ) {

    try {

        ActiveMQBlobMessage* info =
            dynamic_cast<ActiveMQBlobMessage*>( dataStructure );
        MessageMarshaller::looseMarshal( wireFormat, dataStructure, dataOut );

        looseMarshalString( info->getRemoteBlobUrl(), dataOut );
        looseMarshalString( info->getMimeType(), dataOut );
        dataOut->writeBoolean( info->isDeletedByBroker() );
    }
    AMQ_CATCH_RETHROW( decaf::io::IOException )
    AMQ_CATCH_EXCEPTION_CONVERT( exceptions::ActiveMQException, decaf::io::IOException )
    AMQ_CATCHALL_THROW( decaf::io::IOException )
}

