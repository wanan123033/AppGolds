cmake_minimum_required(VERSION 3.18.1)


project("appgodx")


add_library(
        godx-rtmp

        SHARED

        native-lib.cpp)



find_library(
        log-lib

        log)


target_link_libraries(
        godx-rtmp

        ${log-lib})