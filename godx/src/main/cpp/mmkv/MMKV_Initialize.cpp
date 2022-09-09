//
// Created by zxw on 2022/9/9.
//
#include <sys/stat.h>
#include "MMKV.h"
#include "ScopedLock.h"

void MMKV::initialize(const char *rootDir) {
    g_instanceDir = new unordered_map<string,MMKV*>;
    g_rootDir = rootDir;
    mkdir(rootDir,777);
}

MMKV *MMKV::mmkvWithId(const char *mmapId, MMKVMode mode) {
    SCOPEDLOCK(g_instanceLock);
    auto itr = g_instanceDir->find(mmapId);
    if (itr != g_instanceDir->end()){
        auto mmkv = itr->second;
        return mmkv;
    }
    auto mmkv = new MMKV(mmapId,mode);
    (*g_instanceDir)[mmapId] = mmkv;
    return mmkv;
}






