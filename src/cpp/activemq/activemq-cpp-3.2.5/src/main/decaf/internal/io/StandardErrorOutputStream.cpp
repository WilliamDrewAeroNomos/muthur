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

#include "StandardErrorOutputStream.h"

using namespace decaf;
using namespace decaf::lang;
using namespace decaf::lang::exceptions;
using namespace decaf::internal;
using namespace decaf::internal::io;

////////////////////////////////////////////////////////////////////////////////
StandardErrorOutputStream::StandardErrorOutputStream() {
}

////////////////////////////////////////////////////////////////////////////////
StandardErrorOutputStream::~StandardErrorOutputStream() {
}

////////////////////////////////////////////////////////////////////////////////
void StandardErrorOutputStream::doWriteByte( unsigned char c )
    throw ( decaf::io::IOException ) {

    std::cerr << c;
}

////////////////////////////////////////////////////////////////////////////////
void StandardErrorOutputStream::doWriteArrayBounded( const unsigned char* buffer, int size,
                                                     int offset, int length )
    throw ( decaf::io::IOException,
            decaf::lang::exceptions::NullPointerException,
            decaf::lang::exceptions::IndexOutOfBoundsException ) {

    if( length == 0 ) {
        return;
    }

    if( buffer == NULL ) {
        throw lang::exceptions::NullPointerException(
            __FILE__, __LINE__,
            "StandardErrorOutputStream::write - Passed buffer is null." );
    }

    if( size < 0 ) {
        throw IndexOutOfBoundsException(
            __FILE__, __LINE__, "size parameter out of Bounds: %d.", size );
    }

    if( offset > size || offset < 0 ) {
        throw IndexOutOfBoundsException(
            __FILE__, __LINE__, "offset parameter out of Bounds: %d.", offset );
    }

    if( length < 0 || length > size - offset ) {
        throw IndexOutOfBoundsException(
            __FILE__, __LINE__, "length parameter out of Bounds: %d.", length );
    }

    for( int i = 0; i < length; ++i ) {
        std::cerr << buffer[i+offset];
    }
}

////////////////////////////////////////////////////////////////////////////////
void StandardErrorOutputStream::flush() throw ( decaf::io::IOException ){
    std::cerr.flush();
}

////////////////////////////////////////////////////////////////////////////////
void StandardErrorOutputStream::close() throw ( decaf::io::IOException ){
    std::cerr.flush();
}
