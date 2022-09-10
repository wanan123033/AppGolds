cmake_minimum_required(VERSION 3.18.1)


project("appgodx")

add_library(
        godx-mmkv
        SHARED
        native-lib.cpp
        MMKV.h
        MMKV.cpp
        MMKV_Initialize.cpp
        MMKV_Android.cpp
        ThreadLock.h
        ThreadLock.cpp
        ScopedLock.h
        MomeryFile.h
        MomeryFile.cpp
        FileLock.h
        FileLock.cpp
        InterProgressLock.h
        InterProgressLock.cpp
        CodeInput.h
        CodeInput.cpp
        CodeOutput.h
        CodeOutput.cpp
        PButility.h
        ../log.h
)



find_library(
        log-lib

        log)


target_link_libraries(
        godx-mmkv

        ${log-lib}
        -lz
)