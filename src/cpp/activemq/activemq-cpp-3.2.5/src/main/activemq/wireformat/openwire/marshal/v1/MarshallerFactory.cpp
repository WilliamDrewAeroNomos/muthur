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
#include <activemq/wireformat/openwire/marshal/v1/MarshallerFactory.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQBlobMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQBytesMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQMapMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQObjectMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQQueueMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQStreamMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQTempQueueMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQTempTopicMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQTextMessageMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ActiveMQTopicMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/BrokerIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/BrokerInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConnectionControlMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConnectionErrorMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConnectionIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConnectionInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConsumerControlMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConsumerIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ConsumerInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ControlCommandMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/DataArrayResponseMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/DataResponseMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/DestinationInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/DiscoveryEventMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ExceptionResponseMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/FlushCommandMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/IntegerResponseMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/JournalQueueAckMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/JournalTopicAckMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/JournalTraceMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/JournalTransactionMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/KeepAliveInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/LastPartialCommandMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/LocalTransactionIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/MessageAckMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/MessageDispatchMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/MessageDispatchNotificationMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/MessageIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/MessagePullMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/NetworkBridgeFilterMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/PartialCommandMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ProducerAckMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ProducerIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ProducerInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/RemoveInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/RemoveSubscriptionInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ReplayCommandMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ResponseMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/SessionIdMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/SessionInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/ShutdownInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/SubscriptionInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/TransactionInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/WireFormatInfoMarshaller.h>
#include <activemq/wireformat/openwire/marshal/v1/XATransactionIdMarshaller.h>

/*
 *
 *  Command and marshaling code for OpenWire format for MarshallerFactory
 *
 *
 *  NOTE!: This file is autogenerated - do not modify!
 *         if you need to make a change, please see the Java Classes
 *         in the activemq-openwire-generator module
 *
 */

using namespace activemq;
using namespace activemq::wireformat;
using namespace activemq::wireformat::openwire;
using namespace activemq::wireformat::openwire::marshal;
using namespace activemq::wireformat::openwire::marshal::v1;

///////////////////////////////////////////////////////////////////////////////
void MarshallerFactory::configure( OpenWireFormat* format ) {

    format->addMarshaller( new ActiveMQBlobMessageMarshaller() );
    format->addMarshaller( new ActiveMQBytesMessageMarshaller() );
    format->addMarshaller( new ActiveMQMapMessageMarshaller() );
    format->addMarshaller( new ActiveMQMessageMarshaller() );
    format->addMarshaller( new ActiveMQObjectMessageMarshaller() );
    format->addMarshaller( new ActiveMQQueueMarshaller() );
    format->addMarshaller( new ActiveMQStreamMessageMarshaller() );
    format->addMarshaller( new ActiveMQTempQueueMarshaller() );
    format->addMarshaller( new ActiveMQTempTopicMarshaller() );
    format->addMarshaller( new ActiveMQTextMessageMarshaller() );
    format->addMarshaller( new ActiveMQTopicMarshaller() );
    format->addMarshaller( new BrokerIdMarshaller() );
    format->addMarshaller( new BrokerInfoMarshaller() );
    format->addMarshaller( new ConnectionControlMarshaller() );
    format->addMarshaller( new ConnectionErrorMarshaller() );
    format->addMarshaller( new ConnectionIdMarshaller() );
    format->addMarshaller( new ConnectionInfoMarshaller() );
    format->addMarshaller( new ConsumerControlMarshaller() );
    format->addMarshaller( new ConsumerIdMarshaller() );
    format->addMarshaller( new ConsumerInfoMarshaller() );
    format->addMarshaller( new ControlCommandMarshaller() );
    format->addMarshaller( new DataArrayResponseMarshaller() );
    format->addMarshaller( new DataResponseMarshaller() );
    format->addMarshaller( new DestinationInfoMarshaller() );
    format->addMarshaller( new DiscoveryEventMarshaller() );
    format->addMarshaller( new ExceptionResponseMarshaller() );
    format->addMarshaller( new FlushCommandMarshaller() );
    format->addMarshaller( new IntegerResponseMarshaller() );
    format->addMarshaller( new JournalQueueAckMarshaller() );
    format->addMarshaller( new JournalTopicAckMarshaller() );
    format->addMarshaller( new JournalTraceMarshaller() );
    format->addMarshaller( new JournalTransactionMarshaller() );
    format->addMarshaller( new KeepAliveInfoMarshaller() );
    format->addMarshaller( new LastPartialCommandMarshaller() );
    format->addMarshaller( new LocalTransactionIdMarshaller() );
    format->addMarshaller( new MessageAckMarshaller() );
    format->addMarshaller( new MessageDispatchMarshaller() );
    format->addMarshaller( new MessageDispatchNotificationMarshaller() );
    format->addMarshaller( new MessageIdMarshaller() );
    format->addMarshaller( new MessagePullMarshaller() );
    format->addMarshaller( new NetworkBridgeFilterMarshaller() );
    format->addMarshaller( new PartialCommandMarshaller() );
    format->addMarshaller( new ProducerAckMarshaller() );
    format->addMarshaller( new ProducerIdMarshaller() );
    format->addMarshaller( new ProducerInfoMarshaller() );
    format->addMarshaller( new RemoveInfoMarshaller() );
    format->addMarshaller( new RemoveSubscriptionInfoMarshaller() );
    format->addMarshaller( new ReplayCommandMarshaller() );
    format->addMarshaller( new ResponseMarshaller() );
    format->addMarshaller( new SessionIdMarshaller() );
    format->addMarshaller( new SessionInfoMarshaller() );
    format->addMarshaller( new ShutdownInfoMarshaller() );
    format->addMarshaller( new SubscriptionInfoMarshaller() );
    format->addMarshaller( new TransactionInfoMarshaller() );
    format->addMarshaller( new WireFormatInfoMarshaller() );
    format->addMarshaller( new XATransactionIdMarshaller() );
}

