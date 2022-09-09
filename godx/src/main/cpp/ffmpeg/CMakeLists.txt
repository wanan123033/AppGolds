cmake_minimum_required(VERSION 3.18.1)


project("appgodx")

include_directories(${CMAKE_SOURCE_DIR}/ffmpeg)
if(${CMAKE_ANDROID_ARCH_ABI} MATCHES "armeabi-v7a")
    include_directories(${CMAKE_SOURCE_DIR}/ffmpeg/armeabi-v7a/)
endif()
add_library( ffmpeg
        SHARED
        IMPORTED )
set_target_properties( ffmpeg
        PROPERTIES IMPORTED_LOCATION
        ${CMAKE_SOURCE_DIR}/../../../libs/${CMAKE_ANDROID_ARCH_ABI}/libffmpeg.so)
add_library(
        godx-ffmpeg
        SHARED
        ../log.h
        native-lib.cpp
        main/BaseChannel.h
        main/BaseChannel.cpp
        main/FFmpegPlayer.h
        main/FFmpegPlayer.cpp
        main/JavaCallHelper.h
        main/JavaCallHelper.cpp
        main/VideoChannel.h
        main/VideoChannel.cpp
        main/FFmpegCmd.h
        main/FFmpegCmd.cpp
)



find_library(
        log-lib

        log)


target_link_libraries(
        godx-ffmpeg
        ffmpeg
        -landroid
        ${log-lib})