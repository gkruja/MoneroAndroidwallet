# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)




message(STATUS ABI_INFO  = ${ANDROID_ABI})



# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

add_library( # Sets the name of the library.
             native-lib

             # Sets the library as a shared library.
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/Monero.cpp
              src/main/cpp/Monero.h
              src/main/cpp/native-lib.cpp
	      src/main/cpp/coffeecatch/coffeecatch.c
              src/main/cpp/coffeecatch/coffeejni.c)






# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.



set(distribution_DIR ${CMAKE_SOURCE_DIR}/../distribution)

############
# OPENSSL
############

add_library(crypto STATIC IMPORTED)
set_target_properties(crypto PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/OpenSSL/lib/${ANDROID_ABI}/libcrypto.so)

add_library(ssl STATIC IMPORTED)
set_target_properties(ssl PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/OpenSSL/lib/${ANDROID_ABI}/libssl.so)



############
# BOOST
############

add_library(boost_chrono STATIC IMPORTED)
set_target_properties(boost_chrono PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_chrono.a)

add_library(boost_date_time STATIC IMPORTED)
set_target_properties(boost_date_time PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_date_time.a)

add_library(boost_filesystem STATIC IMPORTED)
set_target_properties(boost_filesystem PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_filesystem.a)

add_library(boost_program_options STATIC IMPORTED)
set_target_properties(boost_program_options PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_program_options.a)

add_library(boost_regex STATIC IMPORTED)
set_target_properties(boost_regex PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_regex.a)

add_library(boost_serialization STATIC IMPORTED)
set_target_properties(boost_serialization PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_serialization.a)

add_library(boost_system STATIC IMPORTED)
set_target_properties(boost_system PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_system.a)

add_library(boost_thread STATIC IMPORTED)
set_target_properties(boost_thread PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_thread.a)

add_library(boost_wserialization STATIC IMPORTED)
set_target_properties(boost_wserialization PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Boost/lib/${ANDROID_ABI}/libboost_wserialization.a)






#############
# MONERO
#############



add_library(cncrypto SHARED IMPORTED)
set_target_properties(cncrypto PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libcncrypto.so)

add_library(cryptonote_core SHARED IMPORTED)
set_target_properties(cryptonote_core PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libcryptonote_core.so)

add_library(wallet SHARED IMPORTED)
set_target_properties(wallet PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libwallet.so)

add_library(blockchain_db SHARED IMPORTED)
set_target_properties(blockchain_db PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libblockchain_db.so)



add_library(cryptonote_protocol SHARED IMPORTED)
set_target_properties(cryptonote_protocol PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libcryptonote_protocol.so)

add_library(cryptonote_basic SHARED IMPORTED)
set_target_properties(cryptonote_basic PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libcryptonote_basic.so)

add_library(daemonizer SHARED IMPORTED)
set_target_properties(daemonizer PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libdaemonizer.so)

add_library(blocks STATIC IMPORTED)
set_target_properties(blocks PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libblocks.a)

add_library(lmdb SHARED IMPORTED)
set_target_properties(lmdb PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/liblmdb.so)

add_library(ringct SHARED IMPORTED)
set_target_properties(ringct PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libringct.so)

add_library(common SHARED IMPORTED)
set_target_properties(common PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libcommon.so)

add_library(mnemonics SHARED IMPORTED)
set_target_properties(mnemonics PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libmnemonics.so)

add_library(epee STATIC IMPORTED)
set_target_properties(epee PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libepee.a)

add_library(easylogging SHARED IMPORTED)
set_target_properties(easylogging PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libeasylogging.so)

add_library(unbound SHARED IMPORTED)
set_target_properties(unbound PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libunbound.so)


add_library(miniupnpc STATIC IMPORTED)
set_target_properties(miniupnpc PROPERTIES IMPORTED_LOCATION
    ${distribution_DIR}/Monero/lib/${ANDROID_ABI}/libminiupnpc.a)



find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )



include_directories(        ${distribution_DIR}/Monero/include/src
		                    ${distribution_DIR}/Monero/include/external
		                    ${distribution_DIR}/Monero/include/build
		                    ${distribution_DIR}/Monero/include/external/easylogging++
		                    ${distribution_DIR}/Monero/include/contrib/epee/include
		                    ${distribution_DIR}/Monero/include/external/db_drivers/liblmdb
                            ${distribution_DIR}/Boost/include
                            ${distribution_DIR}/OpenSSL/include)

target_include_directories(native-lib
                            PUBLIC
                            ${distribution_DIR}/Curllib
		                    ${distribution_DIR}/Monero/include/src
		                    ${distribution_DIR}/Monero/include/external
		                    ${distribution_DIR}/Monero/include/external/easylogging++
		                    ${distribution_DIR}/Monero/include/contrib/epee/include
		                    ${distribution_DIR}/Monero/include/external/db_drivers/liblmdb
                           ${distribution_DIR}/Boost/include
                           ${distribution_DIR}/OpenSSL/include)



message(STATUS distribution_DIR :  ${distribution_DIR})
# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
                       native-lib
                        android
			            crypto
                        ssl
                        boost_chrono
                        boost_date_time
                        boost_filesystem
                        boost_program_options
                        boost_regex
                        boost_serialization
                        boost_system
                        boost_thread
                        boost_wserialization
                        cncrypto
                        wallet
                        blockchain_db
                        cryptonote_core
                        cryptonote_protocol
                        cryptonote_basic
                        daemonizer
                        blocks
                        lmdb
                        ringct
                        common
                        mnemonics
                        epee
                        easylogging
                        unbound
                        miniupnpc

                       # Links the target library to the log library
                       # included in the NDK.
                       ${log-lib} )
