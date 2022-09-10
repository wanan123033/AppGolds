//
// Created by gwm on 2022/9/10.
//
#include "MMKV.h"
#include "PButility.h"

using namespace pbu;
void MMKV::putInt(const char *key, int value) {
    size_t vSize = compileSize(value);
    CodeInput data(vSize);
    CodeOutput output(data.data(),data.length());
    output.writeInt32(value);
    auto itr = m_cache->find(key);
    if (itr != m_cache->end()){
        delete itr->second;
    }
    m_cache->emplace(key,&data);
    appendDataWithKey(key,&data);
}
int MMKV::getInt(const char *key) {
    auto itr = m_cache->find(key);
    if (itr != m_cache->end()){
        CodeInput* input = itr->second;
        int value = input->read32Int();
        input->restore();
        return value;
    }
    return 0;
}
string MMKV::getString(const char *key) {
    auto itr = m_cache->find(key);
    if (itr != m_cache->end()){
        CodeInput* input = itr->second;
        string value = input->readString();
        input->restore();
        return value;
    }
    return NULL;
}

void MMKV::putString(const char *key, string value) {
    size_t vSize = strlen(value.c_str());
    CodeInput data(vSize);
    CodeOutput output(data.data(),data.length());
    output.writeString(value);
    auto itr = m_cache->find(key);
    if (itr != m_cache->end()){
        delete itr->second;
    }
    m_cache->emplace(key,&data);
    appendDataWithKey(key,&data);
}
/**
 * 同步到文件
 * @param key
 * @param input
 */
void MMKV::appendDataWithKey(const char *key, CodeInput* value) {
    //1.检查文件容量
    size_t vSize = compileSize(key,value);
    //2.是否进行扩容
    if (vSize > m_output->speekLeft()){
        size_t needSize = compileSize(m_cache);
        needSize += Fixed32Size;
        if (needSize > m_file->getSize()){
            m_file->reSize(needSize);
        }
        //全量更新
        writeAcutalSize(needSize - Fixed32Size);
        delete m_output;
        m_output = new CodeOutput(m_ptr + Fixed32Size,m_file->getSize() - Fixed32Size);
        for (auto itr = m_cache->begin(); itr != m_cache->end(); ++itr) {
            auto key = itr->first;
            auto value = itr->second;
            m_output->writeString(key);
            m_output->writeData(value);
        }
    } else{
        //3.不需扩容 追加方式更新  如需扩容 全量更新的方式
        //增量更新
        writeAcutalSize(vSize + m_file->getActualSize());
        m_output->writeString(key);
        m_output->writeData(value);
    }
}
void MMKV::writeAcutalSize(size_t size) {
    memcpy(m_file->getPtr(),&size,Fixed32Size);
}