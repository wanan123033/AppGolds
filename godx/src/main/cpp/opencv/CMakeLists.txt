cmake_minimum_required(VERSION 3.18.1)


project("appgodx")

set(DIR ../../../../libs)

include_directories(${CMAKE_SOURCE_DIR}/opencv/)

add_library(opencv
        SHARED
        IMPORTED)
set_target_properties(opencv
        PROPERTIES IMPORTED_LOCATION
        ${DIR}/${CMAKE_ANDROID_ARCH_ABI}/libopencv_java4.so)
add_library(
        godx-opencv

        SHARED

        native-lib.cpp)



find_library(
        log-lib

        log)


target_link_libraries(
        godx-opencv

        ${log-lib})