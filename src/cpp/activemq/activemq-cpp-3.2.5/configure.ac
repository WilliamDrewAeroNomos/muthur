# ---------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ---------------------------------------------------------------------------

AC_PREREQ(2.61)

## --------------------------------
## Initialization macros.
## --------------------------------
AC_INIT(activemq-cpp, 3.2.5, dev@activemq.apache.org)
AC_CONFIG_AUX_DIR(config)
AC_CONFIG_SRCDIR(src/main/activemq/core/ActiveMQConnection.cpp)
AC_CONFIG_MACRO_DIR([m4])

## ------------------------------------------------------------
## Define the Version variables for the ActiveMQ-CPP Library
## ------------------------------------------------------------
ACTIVEMQ_LIBRARY_NAME=activemq-cpp
ACTIVEMQ_MAJOR_VERSION=3
ACTIVEMQ_MINOR_VERSION=2
ACTIVEMQ_PATCH_VERSION=5
ACTIVEMQ_VERSION=${ACTIVEMQ_MAJOR_VERSION}.${ACTIVEMQ_MINOR_VERSION}.${ACTIVEMQ_PATCH_VERSION}
ACTIVEMQ_API_VERSION=${ACTIVEMQ_VERSION}

## ------------------------------------------------------------
## Define the Version variables for the CMS API Library
## ------------------------------------------------------------
CMSAPI_MAJOR_VERSION=2
CMSAPI_MINOR_VERSION=2
CMSAPI_VERSION=${CMSAPI_MAJOR_VERSION}.${CMSAPI_MINOR_VERSION}

AC_SUBST(CMSAPI_MAJOR_VERSION)
AC_SUBST(CMSAPI_MINOR_VERSION)
AC_SUBST(CMSAPI_VERSION)

##
## When releasing a new version the ACTIVEMQ_LIBRARY_VERSION needs to be updated.
## The following rules apply to the numbers in the version.
##
## MAJOR: a change in the MAJOR number is necessary for anything that changes
##  binary compatibility (e.g. changing a function prototype or class definition),
##  or when major internal changes occur
## MINOR: a change in the MINOR number is necessary whenever new functionality is
##  added without breaking binary compatibility (e.g. adding a new class), or when
##  anything other than very minor internal changes occur
## REVISION: a change in the REVISION number is made for any other type of change
##  that does not justify a change to MINOR or MAJOR
##
ACTIVEMQ_LIBRARY_VERSION=12:5:0

AC_SUBST(ACTIVEMQ_LIBRARY_NAME)
AC_SUBST(ACTIVEMQ_VERSION)
AC_SUBST(ACTIVEMQ_MAJOR_VERSION)
AC_SUBST(ACTIVEMQ_MINOR_VERSION)
AC_SUBST(ACTIVEMQ_PATCH_VERSION)
AC_SUBST(ACTIVEMQ_LIBRARY_VERSION)
AC_SUBST(ACTIVEMQ_API_VERSION)

## Value used in the generated files.
PACKAGE=$ACTIVEMQ_LIBRARY_NAME
VERSION=$ACTIVEMQ_VERSION

AM_INIT_AUTOMAKE([subdir-objects])
AM_CONFIG_HEADER(config.h)

# Gives us access to the host_os environment variable
AC_CANONICAL_HOST

echo "Configuring ActiveMQ-CPP library"
echo "   Platform: $host_os"
echo "   CPU: $host_cpu"
echo "   Vendor: $host_vendor"

## -----------------------------------------------
## Checks for programs.
## -----------------------------------------------

AC_PROG_CC
AC_PROG_CXX
AC_LIBTOOL_WIN32_DLL
AC_PROG_LIBTOOL
AC_SUBST([LIBTOOL_DEPS])

AM_SANITY_CHECK
AC_LANG_CPLUSPLUS

AC_FUNC_MEMCMP
AC_FUNC_VPRINTF
AC_HEADER_STDC
AC_C_CONST
AC_TYPE_SIZE_T
AC_C_BIGENDIAN
AC_CHECK_SIZEOF(short)
AC_CHECK_SIZEOF(int)
AC_CHECK_SIZEOF(long)

AC_CHECK_SIZEOF(char, 1)
AC_CHECK_SIZEOF(short, 2)
AC_CHECK_SIZEOF(int, 4)
AC_CHECK_SIZEOF(long long, 8)
AC_CHECK_SIZEOF(float, 4)
AC_CHECK_SIZEOF(double, 8)

AC_CHECK_HEADERS([objbase.h])
AC_CHECK_HEADERS([repcdce.h])
AC_CHECK_HEADERS([signal.h])
AC_CHECK_HEADERS([string.h])
AC_CHECK_HEADERS([sched.h])
AC_CHECK_HEADERS([time.h])
AC_CHECK_HEADERS([limits.h])
AC_CHECK_HEADERS([sys/filio.h])
AC_CHECK_HEADERS([sys/ioctl.h])
AC_CHECK_HEADERS([sys/select.h])
AC_CHECK_HEADERS([sys/time.h])
AC_CHECK_HEADERS([sys/timeb.h])
AC_CHECK_HEADERS([sys/wait.h])
AC_CHECK_HEADERS([sys/types.h])
AC_CHECK_HEADERS([sys/sysctl.h])
AC_CHECK_HEADERS([sys/resource.h])
AC_CHECK_HEADERS([pthread.h])
AC_CHECK_HEADERS([errno.h])
AC_CHECK_HEADERS([semaphore.h])

AC_CHECK_FUNCS([ioctl select gettimeofday time ftime random srandom])

AMQ_FIND_CPPUNIT( 1.10.2, cppunit=yes, cppunit=no;
    AC_MSG_RESULT([no. Unit and Integration tests disabled])
)
AM_CONDITIONAL(BUILD_CPPUNIT_TESTS, test x$cppunit = xyes)

# Absolute source/build directory
decaf_srcdir=`(cd $srcdir && pwd)`
decaf_builddir=`pwd`
AC_SUBST(decaf_srcdir)
AC_SUBST(decaf_builddir)

if test "$decaf_builddir" != "$decaf_srcdir"; then
  USE_VPATH=1
  DECAF_CONFIG_LOCATION=build
else
  DECAF_CONFIG_LOCATION=source
fi

AC_SUBST(DECAF_CONFIG_LOCATION)

PLAT_CXXFLAGS=
PLAT_LIBS=

# Detect the presence of pthreads and the correct linker settings.
DECAF_CHECK_PTHREADS()

case "${host_os}" in

  *darwin* ) ## Mac OS X configuration

    ;;

  *solaris* ) ## Solaris configuration

    PLAT_LIBS="-lm -luuid -lsocket -lrt"

    if ! test "$GCC" = "yes"; then
       # Assuming Sun Compiler and adding in optimization
       # because autoconf can't seem to figure it out.
       PLAT_CXXFLAGS="-mt -w -O5 -library=stlport4"
    fi
    ;;

  *hpux10* )

    if ! test "$GCC" = "yes"; then
       # Assuming aCC Compiler unknown yet if any additional settings are needed.
       PLAT_CXXFLAGS=""
    fi
    ;;

  *hpux11* )

    if ! test "$GCC" = "yes"; then
       # Assuming aCC Compiler unknown yet if any additional settings are needed.
       PLAT_CXXFLAGS="-Wc,-ansi_for_scope,on"
    fi
    ;;

  *linux*) ## Unix configuration

    ;;

esac

if test "$GCC" = "yes"; then
   PLAT_CXXFLAGS="-ansi -pedantic"
fi

## find and configure the Apache Decaf Library
## DECAF_CONFIGURE_DECAF - Future use for decaf as a standalone lib
## Flags for building the decaf cpp library
## find and configure the APR, and APR Utils
## find and configure the OpenSSL library if present

AC_ARG_ENABLE([ssl],
             [AS_HELP_STRING([--disable-ssl],
                [disable SSL support (default is enabled if the OpenSSL libraries and headers are found)])],
             [use_ssl=$enableval],
             [use_ssl=yes])

DECAF_CONFIGURE_APR

if test "$use_ssl" = "yes"; then
    DECAF_CONFIGURE_SSL
fi

DECAF_CXXFLAGS="$APR_CPPFLAGS $APR_INCLUDES $APU_INCLUDES $OPENSSL_INCLUDES"
DECAF_LIBS="$APR_LIBS $APR_LDFLAGS $APU_LIBS $APU_LDFLAGS $OPENSSL_LIBS $OPENSSL_LDFLAGS"

AC_SUBST([DECAF_CXXFLAGS])
AC_SUBST([DECAF_LIBS])

## Flags for building the test applications.
DECAF_TEST_CXXFLAGS="$DECAF_CXXFLAGS $APR_CPPFLAGS $APR_INCLUDES $APU_INCLUDES"
AC_SUBST([DECAF_TEST_CXXFLAGS])

## Flags for building the activemq-cpp library
AC_SUBST([AMQ_CXXFLAGS])
AC_SUBST([AMQ_LIBS])

if test "$GCC" = "yes"; then
   AMQ_CXXFLAGS="$PLAT_CXXFLAGS $DECAF_INCLUDES $DECAF_CXXFLAGS -W -Wall -Wextra -Wconversion -fPIC -fstrict-aliasing -Wstrict-aliasing=2 -Wno-long-long"
else
   AMQ_CXXFLAGS="$PLAT_CXXFLAGS $DECAF_INCLUDES $DECAF_CXXFLAGS -PIC"
fi

AMQ_LIBS="$PLAT_LIBS $DECAF_LIBS"

if test "$GCC" = "yes"; then
   AMQ_TEST_CXXFLAGS="$AMQ_CXXFLAGS $DECAF_INCLUDES $DECAF_CXXFLAGS -Wno-non-virtual-dtor -Wno-unused-parameter -Wno-uninitialized"
else
   AMQ_TEST_CXXFLAGS="$AMQ_CXXFLAGS $DECAF_INCLUDES $DECAF_CXXFLAGS"
fi

AMQ_TEST_LIBS="../main/libactivemq-cpp.la $DECAF_LIBS"

## Flags for building the test applications.
AC_SUBST([AMQ_TEST_CXXFLAGS])
AC_SUBST([AMQ_TEST_LIBS])

# Check to see if the compiler supports the visibility flags.
# If so, define HAVE_VISIBILITY_OPTIONS
CHECK_VISIBILITY()

## Not all platforms define addrinfo and related functions.  If this platform
## supports addrinfo, defines HAVE_STRUCT_ADDRINFO.
CHECK_ADDRINFO()

## Execute Doxygen macros
DX_HTML_FEATURE(ON)
DX_CHM_FEATURE(OFF)
DX_CHI_FEATURE(OFF)
DX_MAN_FEATURE(OFF)
DX_RTF_FEATURE(OFF)
DX_XML_FEATURE(OFF)
DX_PDF_FEATURE(OFF)
DX_PS_FEATURE(OFF)
DX_INIT_DOXYGEN(activemq-cpp, doxygen.cfg, doc)

## -----------------------------------------------------
## configuration
## Generates Makefile's, configuration files and scripts
## -----------------------------------------------------

AC_OUTPUT_COMMANDS([chmod +x activemqcpp-config],[])

AC_CONFIG_FILES(Makefile)
AC_CONFIG_FILES(activemq-cpp.pc)
AC_CONFIG_FILES(src/main/Makefile)
AC_CONFIG_FILES(src/examples/Makefile)
AC_CONFIG_FILES(activemqcpp-config)

if test x$cppunit = xyes
then
  AC_CONFIG_FILES(src/test/Makefile)
  AC_CONFIG_FILES(src/test-integration/Makefile)
  AC_CONFIG_FILES(src/test-benchmarks/Makefile)
fi

AC_OUTPUT
