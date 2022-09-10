//
// Created by zxw on 2022/9/9.
//

#ifndef APPGODX_MMKV_H
#define APPGODX_MMKV_H
#include <jni.h>
#include "ThreadLock.h"
#include "MomeryFile.h"
#include "InterProgressLock.h"
#include "FileLock.h"
#include "CodeOutput.h"

using namespace std;
enum MMKVMode : uint32_t {
    MMKV_SINGLE_PROCESS = 0x1,
    MMKV_MULTI_PROCESS = 0x2,
};

class MMKV {

public:
    MMKV(const char *mmapId, MMKVMode mode);

    static void initialize(const char *rootDir);


    static MMKV* mmkvWithId(const char *mmapId, MMKVMode mode);

    void putInt(const char *key, int value);

    int getInt(const char *key);

    string getString(const char *key);

    void putString(const char *key, string value);

private:


    string m_mmapId;  //mmap ID
    MMKVMode m_mode;  //mmap 文件模式
    string m_path;     //mmap 文件路径
    string m_crcPath;  //mmap crc文件路径
    MomeryFile* m_mateFile;  //mmap crc 文件
    MomeryFile* m_file;  //mmap文件
    ThreadLock* m_lock;  //线程锁
    InterProgressLock m_readLock;  //文件读锁
    InterProgressLock m_writeLock;  //文件写锁
    FileLock m_fileLock;  //文件锁
    bool isInterProgress; //是否跨进程
    int8_t * m_ptr;


    void loadFromFile();

    unordered_map<string,CodeInput*>* m_cache;

    void appendDataWithKey(const char *key, CodeInput* input);

    CodeOutput *m_output;

    void writeAcutalSize(size_t size);
};

static unordered_map<string,MMKV*>* g_instanceDir;
static string g_rootDir;
static ThreadLock g_instanceLock;
#endif //APPGODX_MMKV_H
