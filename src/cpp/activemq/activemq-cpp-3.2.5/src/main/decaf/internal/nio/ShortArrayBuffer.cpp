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

#include "ShortArrayBuffer.h"

using namespace decaf;
using namespace decaf::lang;
using namespace decaf::lang::exceptions;
using namespace decaf::internal;
using namespace decaf::internal::nio;
using namespace decaf::internal::util;
using namespace decaf::nio;

///////////////////////////////////////////////////////////////////////////////
ShortArrayBuffer::ShortArrayBuffer( int size, bool readOnly )
    throw( decaf::lang::exceptions::IllegalArgumentException ) : ShortBuffer( size ){

    // Allocate using the ByteArray, not read-only initially.  Take a reference to it.
    // The capacity is the given capacity times the size of the stored datatype
    this->_array.reset( new ByteArrayAdapter( size * (int)sizeof(short) ) );
    this->offset = 0;
    this->length = size;
    this->readOnly = readOnly;
}

///////////////////////////////////////////////////////////////////////////////
ShortArrayBuffer::ShortArrayBuffer( short* array, int size, int offset, int length, bool readOnly )
    throw( decaf::lang::exceptions::IndexOutOfBoundsException,
           decaf::lang::exceptions::NullPointerException ) : ShortBuffer( length ) {

    try{

        if( offset < 0 || offset > size ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__, "Offset parameter if out of bounds, %d", offset );
        }

        if( length < 0 || offset + length > size ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__, "length parameter if out of bounds, %d", length );
        }

        // Allocate using the ByteArray, not read-only initially.
        this->_array.reset( new ByteArrayAdapter( array, length, false ) );
        this->offset = offset;
        this->length = length;
        this->readOnly = readOnly;
    }
    DECAF_CATCH_RETHROW( NullPointerException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, NullPointerException )
    DECAF_CATCHALL_THROW( NullPointerException )
}

///////////////////////////////////////////////////////////////////////////////
ShortArrayBuffer::ShortArrayBuffer( const Pointer<ByteArrayAdapter>& array,
                                    int offset, int length, bool readOnly )
    throw( decaf::lang::exceptions::IndexOutOfBoundsException,
           decaf::lang::exceptions::NullPointerException ) : ShortBuffer( length ) {

    try{

        if( offset < 0 || offset > array->getCapacity() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__, "Offset parameter if out of bounds, %d", offset );
        }

        if( length < 0 || offset + length > array->getCapacity() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__, "length parameter if out of bounds, %d", length );
        }

        // Allocate using the ByteArray, not read-only initially.
        this->_array = array;
        this->offset = offset;
        this->length = length;
        this->readOnly = readOnly;
    }
    DECAF_CATCH_RETHROW( NullPointerException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, NullPointerException )
    DECAF_CATCHALL_THROW( NullPointerException )
}

///////////////////////////////////////////////////////////////////////////////
ShortArrayBuffer::ShortArrayBuffer( const ShortArrayBuffer& other )
    : ShortBuffer( other ) {

    // get the byte buffer of the caller and take a reference
    this->_array = other._array;
    this->offset = other.offset;
    this->length = other.length;
    this->readOnly = other.readOnly;
}

////////////////////////////////////////////////////////////////////////////////
ShortArrayBuffer::~ShortArrayBuffer() {

    try{
    }
    DECAF_CATCH_NOTHROW( Exception )
    DECAF_CATCHALL_NOTHROW()
}

///////////////////////////////////////////////////////////////////////////////
short* ShortArrayBuffer::array()
    throw( decaf::lang::exceptions::UnsupportedOperationException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( !this->hasArray() ) {
            throw UnsupportedOperationException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::arrayOffset() - This Buffer has no backing array." );
        }

        if( this->isReadOnly() ) {
            throw ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::array - Buffer is Read-Only" );
        }

        return this->_array->getShortArray();
    }
    DECAF_CATCH_RETHROW( ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( UnsupportedOperationException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, UnsupportedOperationException )
    DECAF_CATCHALL_THROW( UnsupportedOperationException )
}

///////////////////////////////////////////////////////////////////////////////
int ShortArrayBuffer::arrayOffset()
    throw( decaf::lang::exceptions::UnsupportedOperationException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( !this->hasArray() ) {
            throw UnsupportedOperationException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::arrayOffset() - This Buffer has no backing array." );
        }

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::arrayOffset() - Buffer is Read Only." );
        }

        return this->offset;
    }
    DECAF_CATCH_RETHROW( ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( UnsupportedOperationException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, UnsupportedOperationException )
    DECAF_CATCHALL_THROW( UnsupportedOperationException )
}

///////////////////////////////////////////////////////////////////////////////
ShortBuffer* ShortArrayBuffer::asReadOnlyBuffer() const {

    try{

        ShortArrayBuffer* buffer = new ShortArrayBuffer( *this );
        buffer->setReadOnly( true );

        return buffer;
    }
    DECAF_CATCH_RETHROW( Exception )
    DECAF_CATCHALL_THROW( Exception )
}

///////////////////////////////////////////////////////////////////////////////
ShortBuffer& ShortArrayBuffer::compact() throw( decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::compact() - Buffer is Read Only." );
        }

        // copy from the current pos to the beginning all the remaining bytes
        // the set pos to the
        for( int ix = 0; ix < this->remaining(); ++ix ) {
            this->put( ix, this->get( this->position() + ix ) );
        }

        this->position( this->limit() - this->position() );
        this->limit( this->capacity() );
        this->_markSet = false;

        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::ReadOnlyBufferException )
    DECAF_CATCHALL_THROW( decaf::nio::ReadOnlyBufferException )
}

///////////////////////////////////////////////////////////////////////////////
ShortBuffer* ShortArrayBuffer::duplicate() {

    try{
        return new ShortArrayBuffer( *this );
    }
    DECAF_CATCH_RETHROW( Exception )
    DECAF_CATCHALL_THROW( Exception )
}

///////////////////////////////////////////////////////////////////////////////
short ShortArrayBuffer::get() throw ( decaf::nio::BufferUnderflowException ) {

    try{
        return this->get( this->_position++ );
    }
    DECAF_CATCH_RETHROW( BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, BufferUnderflowException )
    DECAF_CATCHALL_THROW( BufferUnderflowException )
}

///////////////////////////////////////////////////////////////////////////////
short ShortArrayBuffer::get( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException ) {

    try{

        if( index >= this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::get - Not enough data to fill request." );
        }

        return this->_array->getShort( offset + index );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ShortBuffer& ShortArrayBuffer::put( short value )
    throw( BufferOverflowException, ReadOnlyBufferException ) {

    try{

        this->put( this->_position++, value );
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( decaf::nio::BufferOverflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferOverflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferOverflowException )
}

////////////////////////////////////////////////////////////////////////////////
ShortBuffer& ShortArrayBuffer::put( int index, short value )
    throw( decaf::lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::put(i,i) - Buffer is Read Only." );
        }

        if( index >= this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ShortArrayBuffer::put(i,i) - Not enough data to fill request." );
        }

        this->_array->putShort( index + offset, value );

        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ShortBuffer* ShortArrayBuffer::slice() const {

    try{

        return new ShortArrayBuffer( this->_array,
                                     this->offset + this->position(),
                                     this->remaining(),
                                     this->isReadOnly() );
    }
    DECAF_CATCH_RETHROW( Exception )
    DECAF_CATCHALL_THROW( Exception )
}
