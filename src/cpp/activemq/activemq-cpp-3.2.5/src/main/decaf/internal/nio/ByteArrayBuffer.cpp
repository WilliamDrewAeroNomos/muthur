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

#include "ByteArrayBuffer.h"
#include "decaf/lang/Short.h"
#include "decaf/lang/Integer.h"
#include "decaf/lang/Long.h"
#include "decaf/lang/Float.h"
#include "decaf/lang/Double.h"
#include <string.h>

using namespace std;
using namespace decaf;
using namespace decaf::lang;
using namespace decaf::lang::exceptions;
using namespace decaf::internal::nio;

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer::ByteArrayBuffer( int size, bool readOnly )
    throw( decaf::lang::exceptions::IllegalArgumentException ) : decaf::nio::ByteBuffer( size ) {

    // Allocate using the ByteArray, not read-only initially.  Take a reference to it.
    this->_array.reset( new ByteArrayAdapter( size ) );
    this->offset = 0;
    this->length = size;
    this->readOnly = readOnly;
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer::ByteArrayBuffer( unsigned char* array, int size, int offset, int length, bool readOnly )
    throw( decaf::lang::exceptions::NullPointerException,
           decaf::lang::exceptions::IndexOutOfBoundsException ) :
    decaf::nio::ByteBuffer( length ) {

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
        this->_array.reset( new ByteArrayAdapter( array, size, false ) );
        this->offset = offset;
        this->length = length;
        this->readOnly = readOnly;
    }
    DECAF_CATCH_RETHROW( NullPointerException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, NullPointerException )
    DECAF_CATCHALL_THROW( NullPointerException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer::ByteArrayBuffer( const Pointer<ByteArrayAdapter>& array, int offset, int length, bool readOnly )
    throw( decaf::lang::exceptions::NullPointerException,
           decaf::lang::exceptions::IndexOutOfBoundsException ) : decaf::nio::ByteBuffer( length ) {

    try{

        if( offset < 0 || offset > array->getCapacity() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__, "Offset parameter if out of bounds, %d", offset );
        }

        if( length < 0 || offset + length > array->getCapacity() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__, "length parameter if out of bounds, %d", length );
        }

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

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer::ByteArrayBuffer( const ByteArrayBuffer& other )
    : decaf::nio::ByteBuffer( other ) {

    // get the byte buffer of the caller and take a reference
    this->_array = other._array;
    this->offset = other.offset;
    this->length = other.length;
    this->readOnly = other.readOnly;
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer::~ByteArrayBuffer() {

    try{
    }
    DECAF_CATCH_NOTHROW( Exception )
    DECAF_CATCHALL_NOTHROW()
}

////////////////////////////////////////////////////////////////////////////////
unsigned char* ByteArrayBuffer::array()
    throw( decaf::nio::ReadOnlyBufferException, UnsupportedOperationException ) {

    try{

        if( !this->hasArray() ) {
            throw UnsupportedOperationException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::arrayOffset() - This Buffer has no backing array." );
        }

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::array() - Buffer is Read Only." );
        }

        return this->_array->getByteArray();
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( UnsupportedOperationException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::ReadOnlyBufferException )
    DECAF_CATCHALL_THROW( decaf::nio::ReadOnlyBufferException )
}

////////////////////////////////////////////////////////////////////////////////
int ByteArrayBuffer::arrayOffset() const
    throw( decaf::nio::ReadOnlyBufferException,
           lang::exceptions::UnsupportedOperationException ) {

    try{

        if( !this->hasArray() ) {
            throw UnsupportedOperationException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::arrayOffset() - This Buffer has no backing array." );
        }

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::arrayOffset() - Buffer is Read Only." );
        }

        return this->offset;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( UnsupportedOperationException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::ReadOnlyBufferException )
    DECAF_CATCHALL_THROW( decaf::nio::ReadOnlyBufferException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer* ByteArrayBuffer::asReadOnlyBuffer() const {

    try{

        ByteArrayBuffer* buffer = new ByteArrayBuffer( *this );
        buffer->setReadOnly( true );

        return buffer;
    }
    DECAF_CATCH_RETHROW( Exception )
    DECAF_CATCHALL_THROW( Exception )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::compact() throw( decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::compact() - Buffer is Read Only." );
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

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer* ByteArrayBuffer::duplicate() {

    try{
        return new ByteArrayBuffer( *this );
    }
    DECAF_CATCH_RETHROW( Exception )
    DECAF_CATCHALL_THROW( Exception )
}

////////////////////////////////////////////////////////////////////////////////
unsigned char ByteArrayBuffer::get() const throw( decaf::nio::BufferUnderflowException ) {

    try{
        return this->get( this->_position++ );
    }
    DECAF_CATCH_RETHROW( decaf::nio::BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferUnderflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferUnderflowException )
}

////////////////////////////////////////////////////////////////////////////////
unsigned char ByteArrayBuffer::get( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException ) {

    try{

        if( ( index ) >= this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::get - Not enough data to fill request." );
        }

        return this->_array->get( offset + index );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
double ByteArrayBuffer::getDouble() throw( decaf::nio::BufferUnderflowException ) {

    try{

        unsigned long long lvalue = this->getLong();
        return Double::longBitsToDouble( lvalue );
    }
    DECAF_CATCH_RETHROW( decaf::nio::BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferUnderflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferUnderflowException )
}

////////////////////////////////////////////////////////////////////////////////
double ByteArrayBuffer::getDouble( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException ) {

    try{

        unsigned long long lvalue = this->getLong( index );
        return Double::longBitsToDouble( lvalue );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
float ByteArrayBuffer::getFloat() throw( decaf::nio::BufferUnderflowException ) {

    try{

        unsigned int ivalue = this->getInt();
        return Float::intBitsToFloat( ivalue );
    }
    DECAF_CATCH_RETHROW( decaf::nio::BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferUnderflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferUnderflowException )
}

////////////////////////////////////////////////////////////////////////////////
float ByteArrayBuffer::getFloat( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException ) {

    try{

        unsigned int ivalue = this->getInt( index );
        return Float::intBitsToFloat( ivalue );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
long long ByteArrayBuffer::getLong() throw( decaf::nio::BufferUnderflowException ) {

    try{

        long long value = this->getLong( this->_position );
        this->_position += (int)sizeof(value);
        return value;
    }
    DECAF_CATCH_RETHROW( decaf::nio::BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferUnderflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferUnderflowException )
}

////////////////////////////////////////////////////////////////////////////////
long long ByteArrayBuffer::getLong( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException ) {

    try{

        if( ( offset + index + (int)sizeof(long long) ) > this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::getLong(i) - Not enough data to fill a long long." );
        }

        return this->_array->getLongAt( index + offset );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
int ByteArrayBuffer::getInt() throw( decaf::nio::BufferUnderflowException )  {

    try{

        int value = this->getInt( this->_position );
        this->_position += (int)sizeof(value);
        return value;
    }
    DECAF_CATCH_RETHROW( decaf::nio::BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferUnderflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferUnderflowException );
}

////////////////////////////////////////////////////////////////////////////////
int ByteArrayBuffer::getInt( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException ) {

    try{

        if( (offset + index + (int)sizeof(int)) > this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::getInt(i) - Not enough data to fill an int." );
        };

        return this->_array->getIntAt( offset + index );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
short ByteArrayBuffer::getShort() throw( decaf::nio::BufferUnderflowException ) {

    try{

        short value = this->getShort( this->_position );
        this->_position += (int)sizeof(value);
        return value;
    }
    DECAF_CATCH_RETHROW( decaf::nio::BufferUnderflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferUnderflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferUnderflowException );
}

////////////////////////////////////////////////////////////////////////////////
short ByteArrayBuffer::getShort( int index ) const
    throw ( lang::exceptions::IndexOutOfBoundsException )  {

    try{

        if( (offset + index + (int)sizeof(short)) > this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::getShort(i) - Not enough data to fill a short." );
        }

        return this->_array->getShortAt( offset + index );
    }
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::put( unsigned char value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

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
ByteArrayBuffer& ByteArrayBuffer::put( int index, unsigned char value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::put(i,i) - Buffer is Read Only." );
        }

        if( index >= this->limit() ) {
            throw IndexOutOfBoundsException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::put(i,i) - Not enough data to fill request." );
        }

        this->_array->put( index + offset, value );

        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putChar( char value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

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
ByteArrayBuffer& ByteArrayBuffer::putChar( int index, char value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        this->put( index, value );
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putDouble( double value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putDouble( this->_position, value );
        this->_position += (int)sizeof(value);
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( decaf::nio::BufferOverflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferOverflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferOverflowException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putDouble( int index, double value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putLong( index, Double::doubleToLongBits( value ) );
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putFloat( float value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putFloat( this->_position, value );
        this->_position += (int)sizeof(value);
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( decaf::nio::BufferOverflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferOverflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferOverflowException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putFloat( int index, float value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putInt( index, Float::floatToIntBits( value ) );
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putLong( long long value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putLong( this->_position, value );
        this->_position += (int)sizeof(value);
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( decaf::nio::BufferOverflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferOverflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferOverflowException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putLong( int index, long long value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::putLong() - Buffer is Read Only." );
        }

        this->_array->putLongAt( index + offset, value );

        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putInt( int value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putInt( this->_position, value );
        this->_position += (int)sizeof(value);
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( decaf::nio::BufferOverflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferOverflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferOverflowException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putInt( int index, int value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::putInt() - Buffer is Read Only." );
        }

        this->_array->putIntAt( index + offset, value );

        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putShort( short value )
    throw( decaf::nio::BufferOverflowException, decaf::nio::ReadOnlyBufferException ) {

    try{

        this->putShort( this->_position, value );
        this->_position += (int)sizeof(value);
        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( decaf::nio::BufferOverflowException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, decaf::nio::BufferOverflowException )
    DECAF_CATCHALL_THROW( decaf::nio::BufferOverflowException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer& ByteArrayBuffer::putShort( int index, short value )
    throw( lang::exceptions::IndexOutOfBoundsException,
           decaf::nio::ReadOnlyBufferException ) {

    try{

        if( this->isReadOnly() ) {
            throw decaf::nio::ReadOnlyBufferException(
                __FILE__, __LINE__,
                "ByteArrayBuffer::putShort() - Buffer is Read Only." );
        }

        this->_array->putShortAt( index + offset, value );

        return *this;
    }
    DECAF_CATCH_RETHROW( decaf::nio::ReadOnlyBufferException )
    DECAF_CATCH_RETHROW( IndexOutOfBoundsException )
    DECAF_CATCH_EXCEPTION_CONVERT( Exception, IndexOutOfBoundsException )
    DECAF_CATCHALL_THROW( IndexOutOfBoundsException )
}

////////////////////////////////////////////////////////////////////////////////
ByteArrayBuffer* ByteArrayBuffer::slice() const {

    try{

        return new ByteArrayBuffer( this->_array,
                                    this->offset + this->position(),
                                    this->remaining(),
                                    this->isReadOnly() );
    }
    DECAF_CATCH_RETHROW( Exception )
    DECAF_CATCHALL_THROW( Exception )
}
