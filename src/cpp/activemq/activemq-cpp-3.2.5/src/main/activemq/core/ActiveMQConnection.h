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

#ifndef _ACTIVEMQ_CORE_ACTIVEMQCONNECTION_H_
#define _ACTIVEMQ_CORE_ACTIVEMQCONNECTION_H_

#include <cms/Connection.h>
#include <activemq/util/Config.h>
#include <activemq/core/ActiveMQConnectionMetaData.h>
#include <activemq/core/Dispatcher.h>
#include <activemq/commands/ActiveMQTempDestination.h>
#include <activemq/commands/ConnectionInfo.h>
#include <activemq/commands/ConsumerInfo.h>
#include <activemq/exceptions/ActiveMQException.h>
#include <activemq/transport/TransportListener.h>
#include <decaf/util/Properties.h>
#include <decaf/util/StlMap.h>
#include <decaf/util/StlSet.h>
#include <decaf/util/concurrent/atomic/AtomicBoolean.h>
#include <decaf/lang/exceptions/UnsupportedOperationException.h>
#include <decaf/lang/exceptions/NullPointerException.h>
#include <decaf/lang/exceptions/IllegalStateException.h>

#include <string>
#include <memory>

namespace activemq{
namespace core{

    using decaf::lang::Pointer;
    using decaf::util::concurrent::atomic::AtomicBoolean;

    class ActiveMQSession;
    class ActiveMQProducer;
    class ConnectionConfig;
    class PrefetchPolicy;
    class RedeliveryPolicy;

    /**
     * Concrete connection used for all connectors to the
     * ActiveMQ broker.
     *
     * @since 2.0
     */
    class AMQCPP_API ActiveMQConnection : public cms::Connection,
                                          public transport::TransportListener {
    private:

        typedef decaf::util::StlMap< Pointer<commands::ConsumerId>,
                                     Dispatcher*,
                                     commands::ConsumerId::COMPARATOR > DispatcherMap;

        typedef decaf::util::StlMap< Pointer<commands::ProducerId>,
                                     ActiveMQProducer*,
                                     commands::ProducerId::COMPARATOR > ProducerMap;

    private:

        ConnectionConfig* config;

        /**
         * The instance of ConnectionMetaData to return to clients.
         */
        std::auto_ptr<cms::ConnectionMetaData> connectionMetaData;

        /**
         * Indicates if this Connection is started
         */
        AtomicBoolean started;

        /**
         * Indicates that this connection has been closed, it is no longer
         * usable after this becomes true
         */
        AtomicBoolean closed;

        /**
         * Indicates that this connection has been closed, it is no longer
         * usable after this becomes true
         */
        AtomicBoolean closing;

        /**
         * Indicates that this connection's Transport has failed.
         */
        AtomicBoolean transportFailed;

        /**
         * Map of message dispatchers indexed by consumer id.
         */
        DispatcherMap dispatchers;

        /**
         * Map of message producers indexed by consumer id.
         */
        ProducerMap activeProducers;

        /**
         * Maintain the set of all active sessions.
         */
        decaf::util::StlSet<ActiveMQSession*> activeSessions;

        /**
         * Maintain the set of all active sessions.
         */
        decaf::util::StlSet<transport::TransportListener*> transportListeners;

    private:

        ActiveMQConnection( const ActiveMQConnection& );
        ActiveMQConnection& operator= ( const ActiveMQConnection& );

    public:

        /**
         * Constructor
         *
         * @param transport
         *        The Transport requested for this connection to the Broker.
         * @param properties
         *        The Properties that were defined for this connection
         */
        ActiveMQConnection( const Pointer<transport::Transport>& transport,
                            const Pointer<decaf::util::Properties>& properties );

        virtual ~ActiveMQConnection();

        /**
         * Removes the session resources for the given session
         * instance.
         * @param session The session to be unregistered from this connection.
         */
        virtual void removeSession( ActiveMQSession* session ) throw ( cms::CMSException );

        /**
         * Adds an active Producer to the Set of known producers.
         * @param producer - The Producer to add from the the known set.
         */
        virtual void addProducer( ActiveMQProducer* producer ) throw ( cms::CMSException );

        /**
         * Removes an active Producer to the Set of known producers.
         * @param producerId - The ProducerId to remove from the the known set.
         */
        virtual void removeProducer( const Pointer<commands::ProducerId>& producerId )
            throw ( cms::CMSException );

        /**
         * Adds a dispatcher for a consumer.
         * @param consumer - The consumer for which to register a dispatcher.
         * @param dispatcher - The dispatcher to handle incoming messages for the consumer.
         */
        virtual void addDispatcher(
            const Pointer<commands::ConsumerId>& consumer, Dispatcher* dispatcher )
                throw ( cms::CMSException );

        /**
         * Removes the dispatcher for a consumer.
         * @param consumer - The consumer for which to remove the dispatcher.
         */
        virtual void removeDispatcher( const Pointer<commands::ConsumerId>& consumer )
            throw ( cms::CMSException );

        /**
         * If supported sends a message pull request to the service provider asking
         * for the delivery of a new message.  This is used in the case where the
         * service provider has been configured with a zero prefetch or is only
         * capable of delivering messages on a pull basis.
         * @param consumer - the ConsumerInfo for the requesting Consumer.
         * @param timeout - the time that the client is willing to wait.
         */
        virtual void sendPullRequest( const commands::ConsumerInfo* consumer, long long timeout )
            throw ( exceptions::ActiveMQException );

        /**
         * Checks if this connection has been closed
         * @return true if the connection is closed
         */
        bool isClosed() const {
            return this->closed.get();
        }

        /**
         * Check if this connection has been started.
         * @return true if the start method has been called.
         */
        bool isStarted() const {
            return this->started.get();
        }

        /**
         * Checks if the Connection's Transport has failed
         * @return true if the Connection's Transport has failed.
         */
        bool isTransportFailed() const {
            return this->transportFailed.get();
        }

        /**
         * Requests that the Broker removes the given Destination.  Calling this
         * method implies that the client is finished with the Destination and that
         * no other messages will be sent or received for the given Destination.  The
         * Broker frees all resources it has associated with this Destination.
         *
         * @param destination
         *        The Destination the Broker will be requested to remove.
         *
         * @throws NullPointerException
         *         If the passed Destination is Null
         * @throws IllegalStateException
         *         If the connection is closed.
         * @throws UnsupportedOperationException
         *         If the wire format in use does not support this operation.
         * @throws ActiveMQException
         *         If any other error occurs during the attempt to destroy the destination.
         */
        virtual void destroyDestination( const commands::ActiveMQDestination* destination )
            throw( decaf::lang::exceptions::NullPointerException,
                   decaf::lang::exceptions::IllegalStateException,
                   decaf::lang::exceptions::UnsupportedOperationException,
                   activemq::exceptions::ActiveMQException );

        /**
         * Requests that the Broker removes the given Destination.  Calling this
         * method implies that the client is finished with the Destination and that
         * no other messages will be sent or received for the given Destination.  The
         * Broker frees all resources it has associated with this Destination.
         *
         * @param destination
         *        The CMS Destination the Broker will be requested to remove.
         *
         * @throws NullPointerException
         *         If the passed Destination is Null
         * @throws IllegalStateException
         *         If the connection is closed.
         * @throws UnsupportedOperationException
         *         If the wire format in use does not support this operation.
         * @throws ActiveMQException
         *         If any other error occurs during the attempt to destroy the destination.
         */
        virtual void destroyDestination( const cms::Destination* destination )
            throw( decaf::lang::exceptions::NullPointerException,
                   decaf::lang::exceptions::IllegalStateException,
                   decaf::lang::exceptions::UnsupportedOperationException,
                   activemq::exceptions::ActiveMQException );

    public:   // Connection Interface Methods

        /**
         * Gets the metadata for this connection.
         *
         * @returns the connection MetaData pointer ( caller does not own it ).
         *
         * @throws CMSException
         *         if the provider fails to get the connection metadata for this connection.
         *
         * @see ConnectionMetaData
         * @since 2.0
         */
        virtual const cms::ConnectionMetaData* getMetaData() const throw( cms::CMSException ) {
            return connectionMetaData.get();
        }

        /**
         * Creates a new Session to work for this Connection
         * @throws CMSException
         */
        virtual cms::Session* createSession() throw ( cms::CMSException );

        /**
         * {@inheritDoc}
         */
        virtual std::string getClientID() const;

        /**
         * {@inheritDoc}
         */
        virtual void setClientID( const std::string& clientID );

        /**
         * Creates a new Session to work for this Connection using the
         * specified acknowledgment mode
         * @param ackMode the Acknowledgment Mode to use.
         * @throws CMSException
         */
        virtual cms::Session* createSession( cms::Session::AcknowledgeMode ackMode )
            throw ( cms::CMSException );

        /**
         * Closes this connection as well as any Sessions
         * created from it (and those Sessions' consumers and
         * producers).
         * @throws CMSException
         */
        virtual void close() throw ( cms::CMSException );

        /**
         * Starts or (restarts) a connections delivery of incoming messages
         * @throws CMSException
         */
        virtual void start() throw ( cms::CMSException );

        /**
         * Stop the flow of incoming messages
         * @throws CMSException
         */
        virtual void stop() throw ( cms::CMSException );

        /**
         * Gets the registered Exception Listener for this connection
         * @return pointer to an exception listener or NULL
         */
        virtual cms::ExceptionListener* getExceptionListener() const;

        /**
         * Sets the registered Exception Listener for this connection
         * @param listener pointer to and <code>ExceptionListener</code>
         */
        virtual void setExceptionListener( cms::ExceptionListener* listener );

    public:   // Configuration Options

        /**
         * Sets the username that should be used when creating a new connection
         * @param username string
         */
        void setUsername( const std::string& username );

        /**
         * Gets the username that this factory will use when creating a new
         * connection instance.
         * @return username string, "" for default credentials
         */
        const std::string& getUsername() const;

        /**
         * Sets the password that should be used when creating a new connection
         * @param password string
         */
        void setPassword( const std::string& password );

        /**
         * Gets the password that this factory will use when creating a new
         * connection instance.
         * @return password string, "" for default credentials
         */
        const std::string& getPassword() const;

        /**
         * Sets the Client Id.
         * @param clientId - The new clientId value.
         */
        void setDefaultClientId( const std::string& clientId );

        /**
         * Sets the Broker URL that should be used when creating a new
         * connection instance
         * @param brokerURL string
         */
        void setBrokerURL( const std::string& brokerURL );

        /**
         * Gets the Broker URL that this factory will use when creating a new
         * connection instance.
         * @return brokerURL string
         */
        const std::string& getBrokerURL() const;

        /**
         * Sets the PrefetchPolicy instance that this factory should use when it creates
         * new Connection instances.  The PrefetchPolicy passed becomes the property of the
         * factory and will be deleted when the factory is destroyed.
         *
         * @param policy
         *      The new PrefetchPolicy that the ConnectionFactory should clone for Connections.
         */
        void setPrefetchPolicy( PrefetchPolicy* policy );

        /**
         * Gets the pointer to the current PrefetchPolicy that is in use by this ConnectionFactory.
         *
         * @returns a pointer to this objects PrefetchPolicy.
         */
        PrefetchPolicy* getPrefetchPolicy() const;

        /**
         * Sets the RedeliveryPolicy instance that this factory should use when it creates
         * new Connection instances.  The RedeliveryPolicy passed becomes the property of the
         * factory and will be deleted when the factory is destroyed.
         *
         * @param policy
         *      The new RedeliveryPolicy that the ConnectionFactory should clone for Connections.
         */
        void setRedeliveryPolicy( RedeliveryPolicy* policy );

        /**
         * Gets the pointer to the current RedeliveryPolicy that is in use by this ConnectionFactory.
         *
         * @returns a pointer to this objects RedeliveryPolicy.
         */
        RedeliveryPolicy* getRedeliveryPolicy() const;

        /**
         * @return The value of the dispatch asynchronously option sent to the broker.
         */
        bool isDispatchAsync() const;

        /**
         * Should messages be dispatched synchronously or asynchronously from the producer
         * thread for non-durable topics in the broker? For fast consumers set this to false.
         * For slow consumers set it to true so that dispatching will not block fast consumers. .
         *
         * @param value
         *        The value of the dispatch asynchronously option sent to the broker.
         */
        void setDispatchAsync( bool value );

        /**
         * Gets if the Connection should always send things Synchronously.
         *
         * @return true if sends should always be Synchronous.
         */
        bool isAlwaysSyncSend() const;

        /**
         * Sets if the Connection should always send things Synchronously.
         * @param value
         *        true if sends should always be Synchronous.
         */
        void setAlwaysSyncSend( bool value );

        /**
         * Gets if the useAsyncSend option is set
         * @returns true if on false if not.
         */
        bool isUseAsyncSend() const;

        /**
         * Sets the useAsyncSend option
         * @param value - true to activate, false to disable.
         */
        void setUseAsyncSend( bool value );

        /**
         * Gets if the Connection is configured for Message body compression.
         * @returns if the Message body will be Compressed or not.
         */
        bool isUseCompression() const;

        /**
         * Sets whether Message body compression is enabled.
         *
         * @param value
         *      Boolean indicating if Message body compression is enabled.
         */
        void setUseCompression( bool value );

        /**
         * Gets the assigned send timeout for this Connector
         * @return the send timeout configured in the connection uri
         */
        unsigned int getSendTimeout() const;

        /**
         * Sets the send timeout to use when sending Message objects, this will
         * cause all messages to be sent using a Synchronous request is non-zero.
         * @param timeout - The time to wait for a response.
         */
        void setSendTimeout( unsigned int timeout );

        /**
         * Gets the assigned close timeout for this Connector
         * @return the close timeout configured in the connection uri
         */
        unsigned int getCloseTimeout() const;

        /**
         * Sets the close timeout to use when sending the disconnect request.
         * @param timeout - The time to wait for a close message.
         */
        void setCloseTimeout( unsigned int timeout );

        /**
         * Gets the configured producer window size for Producers that are created
         * from this connector.  This only applies if there is no send timeout and the
         * producer is able to send asynchronously.
         * @return size in bytes of messages that this producer can produce before
         *         it must block and wait for ProducerAck messages to free resources.
         */
        unsigned int getProducerWindowSize() const;

        /**
         * Sets the size in Bytes of messages that a producer can send before it is blocked
         * to await a ProducerAck from the broker that frees enough memory to allow another
         * message to be sent.
         * @param windowSize - The size in bytes of the Producers memory window.
         */
        void setProducerWindowSize( unsigned int windowSize );

        /**
         * Get the Next available Session Id.
         * @return the next id in the sequence.
         */
        long long getNextSessionId();

        /**
         * Get the Next Temporary Destination Id
         * @return the next id in the sequence.
         */
        long long getNextTempDestinationId();

        /**
         * Get the Next Temporary Destination Id
         * @return the next id in the sequence.
         */
        long long getNextLocalTransactionId();

    public: // TransportListener

        /**
         * Adds a transport listener so that a client can be notified of events in
         * the underlying transport, client's are always notified after the event has
         * been processed by the Connection class.  Client's should ensure that the
         * registered listener does not block or take a long amount of time to execute
         * in order to not degrade performance of this Connection.
         *
         * @param transportListener
         *      The TransportListener instance to add to this Connection's set of listeners
         *      to notify of Transport events.
         */
        void addTransportListener( transport::TransportListener* transportListener );

        /**
         * Removes a registered TransportListener from the Connection's set of Transport
         * listeners, this listener will no longer receive any Transport related events.  The
         * caller is responsible for freeing the listener in all cases.
         *
         * @param transportListener
         *      The pointer to the TransportListener to remove from the set of listeners.
         */
        void removeTransportListener( transport::TransportListener* transportListener );

        /**
         * Event handler for the receipt of a non-response command from the
         * transport.
         * @param command the received command object.
         */
        virtual void onCommand( const Pointer<commands::Command>& command );

        /**
         * Event handler for an exception from a command transport.
         * @param ex The exception.
         */
        virtual void onException( const decaf::lang::Exception& ex );

        /**
         * The transport has suffered an interruption from which it hopes to recover
         */
        virtual void transportInterrupted();

        /**
         * The transport has resumed after an interruption
         */
        virtual void transportResumed();

    public:

        /**
         * Gets the ConnectionInfo for this Object, if the Connection is not open
         * than this method throws an exception
         */
        const commands::ConnectionInfo& getConnectionInfo() const
            throw( exceptions::ActiveMQException );

        /**
         * Gets the ConnectionId for this Object, if the Connection is not open
         * than this method throws an exception
         */
        const commands::ConnectionId& getConnectionId() const
            throw( exceptions::ActiveMQException );

        /**
         * Gets a reference to this object's Transport instance.
         *
         * @return a reference to the Transport that is in use by this Connection.
         */
        transport::Transport& getTransport() const;

        /**
         * Sends a oneway message.
         * @param command The message to send.
         * @throws ConnectorException if not currently connected, or
         * if the operation fails for any reason.
         */
        void oneway( Pointer<commands::Command> command )
            throw ( activemq::exceptions::ActiveMQException );

        /**
         * Sends a synchronous request and returns the response from the broker.
         * Converts any error responses into an exception.
         * @param command The request command.
         * @param timeout The time to wait for a response, default is zero or infinite.
         * @throws ConnectorException thrown if an error response was received
         * from the broker, or if any other error occurred.
         */
        void syncRequest( Pointer<commands::Command> command, unsigned int timeout = 0 )
            throw ( activemq::exceptions::ActiveMQException );

        /**
         * Notify the exception listener
         * @param ex the exception to fire
         */
        virtual void fire( const exceptions::ActiveMQException& ex );

        /**
         * Indicates that a Connection resource that is processing the transportInterrupted
         * event has completed.
         */
        void setTransportInterruptionProcessingComplete();

    private:

        // Sends a oneway disconnect message to the broker.
        void disconnect( long long lastDeliveredSequenceId ) throw ( activemq::exceptions::ActiveMQException );

        // Check for Closed State and Throw an exception if true.
        void checkClosed() const throw ( activemq::exceptions::ActiveMQException );

        // If its not been sent, then send the ConnectionInfo to the Broker.
        void ensureConnectionInfoSent();

        // Waits for all Consumers to handle the Transport Interrupted event.
        void waitForTransportInterruptionProcessingToComplete()
            throw ( decaf::lang::exceptions::InterruptedException );

        // Marks processing complete for a single caller when interruption processing completes.
        void signalInterruptionProcessingComplete()
            throw ( decaf::lang::exceptions::InterruptedException );

    };

}}

#endif /*_ACTIVEMQ_CORE_ACTIVEMQCONNECTION_H_*/
