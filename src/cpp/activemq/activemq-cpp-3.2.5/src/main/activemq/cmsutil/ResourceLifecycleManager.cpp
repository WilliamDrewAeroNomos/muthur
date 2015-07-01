/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include "ResourceLifecycleManager.h"

#include <activemq/exceptions/ActiveMQException.h>
#include <activemq/util/CMSExceptionSupport.h>

using namespace activemq::cmsutil;

////////////////////////////////////////////////////////////////////////////////
ResourceLifecycleManager::ResourceLifecycleManager() {
}

////////////////////////////////////////////////////////////////////////////////
ResourceLifecycleManager::~ResourceLifecycleManager() {

    try {

        // Destroy all the resources
        destroy();
    }
    AMQ_CATCHALL_NOTHROW()
}

////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::releaseAll() {

    connections.clear();
    sessions.clear();
    destinations.clear();
    producers.clear();
    consumers.clear();
}

////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::destroy() throw ( cms::CMSException ) {

    try{
        // Close all the connections.
        std::auto_ptr< decaf::util::Iterator< cms::Connection* > > connIter(
            connections.iterator() );

        while( connIter->hasNext() ) {
            cms::Connection* conn = connIter->next();
            try {
                conn->close();
            } catch(...){}
        }

        // Destroy the producers.
        std::auto_ptr< decaf::util::Iterator< cms::MessageProducer* > > prodIter(
            producers.iterator() );

        while( prodIter->hasNext() ) {
            cms::MessageProducer* producer = prodIter->next();
            try {
                delete producer;
            } catch( ... ) {}
        }

        // Destroy the consumers.
        std::auto_ptr< decaf::util::Iterator< cms::MessageConsumer* > > consIter(
            consumers.iterator() );

        while( consIter->hasNext() ) {
            cms::MessageConsumer* consumer = consIter->next();
            try {
                delete consumer;
            } catch( ... ) {}
        }

        // Destroy the destinations.
        std::auto_ptr< decaf::util::Iterator< cms::Destination* > > destIter(
            destinations.iterator() );

        while( destIter->hasNext() ) {
            cms::Destination* dest = destIter->next();
            try {
                delete dest;
            } catch( ... ) {}
        }

        // Destroy the sessions.
        std::auto_ptr< decaf::util::Iterator< cms::Session* > > sessIter(
            sessions.iterator() );

        while( sessIter->hasNext() ) {
            cms::Session* session = sessIter->next();
            try {
                delete session;
            } catch( ... ) {}
        }

        // Destroy the connections,
        connIter.reset( connections.iterator() );

        while( connIter->hasNext() ) {
            cms::Connection* conn = connIter->next();
            try {
                delete conn;
            } catch( ... ) {}
        }

        // Empty all the lists.
        releaseAll();
    }
    AMQ_CATCH_ALL_THROW_CMSEXCEPTION()
}

////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::addConnection( cms::Connection* connection )
    throw ( cms::CMSException ) {

    try{
        // Add the connection to the list.
        synchronized( &connections ) {
            connections.add( connection );
        }
    }
    AMQ_CATCH_ALL_THROW_CMSEXCEPTION()
}

////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::addSession( cms::Session* session )
    throw ( cms::CMSException ) {

    try{
        // Add the session to the list.
        synchronized( &sessions ) {
            sessions.add( session );
        }
    }
    AMQ_CATCH_ALL_THROW_CMSEXCEPTION()
}


////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::addDestination( cms::Destination* dest )
    throw ( cms::CMSException ) {

    try{
        // Add the destination to the list.
        synchronized( &destinations ) {
            destinations.add( dest );
        }
    }
    AMQ_CATCH_ALL_THROW_CMSEXCEPTION()
}

////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::addMessageProducer( cms::MessageProducer* producer )
    throw ( cms::CMSException ) {

    try{
        // Add the producer to the list.
        synchronized( &producers ) {
            producers.add( producer );
        }
    }
    AMQ_CATCH_ALL_THROW_CMSEXCEPTION()
}

////////////////////////////////////////////////////////////////////////////////
void ResourceLifecycleManager::addMessageConsumer( cms::MessageConsumer* consumer )
    throw ( cms::CMSException ) {

    try{
        // Add the consumer to the list.
        synchronized( &consumers ) {
            consumers.add( consumer );
        }
    }
    AMQ_CATCH_ALL_THROW_CMSEXCEPTION()
}
