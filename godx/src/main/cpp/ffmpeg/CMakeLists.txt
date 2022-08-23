cmake_minimum_required(VERSION 3.18.1)


project("appgodx")


add_library(
        godx-ffmpeg

        SHARED

        native-lib.cpp)



find_library(
        log-lib

        log)


target_link_libraries(
        godx-ffmpeg

        ${log-lib})