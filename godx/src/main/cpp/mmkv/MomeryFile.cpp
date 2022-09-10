//
// Created by zxw on 2022/9/9.
//

#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include "MomeryFile.h"
#include "../log.h"

enum ReSizeMode{
    DOUBLE_GROWTH, // 翻倍扩容
    MINIMUM_GROWTH  //最小扩容
};
#define CURRENT_GROWTH DOUBLE_GROWTH

MomeryFile::MomeryFile(string path):path(path) {
    m_dic = new unordered_map<string,CodeInput*>;
}

int MomeryFile::getFd() {
    return fd;
}

void MomeryFile::loadFromFile() {
    fd = open(path.c_str(),O_RDWR|O_CREAT,S_IRWXU);
    if (fd < 0){
        LOGE("MMKV open file %s is fail!",path.c_str());
        return;
    }
    struct stat st = {0};
    if (fstat(fd,&st) != -1){
        m_size = st.st_size;
    }
    int pagesize = getpagesize();
    if (m_size % pagesize == 0 && m_size > 0){
        ptr = static_cast<int8_t *>(mmap(0, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0));
    }
    memcpy(&m_actualSize,ptr,Fixed32Size);
    if (m_actualSize > 0){
        if (m_actualSize + Fixed32Size <= m_size){
            reloadFromFile();
        }
    } else{
        m_actualSize = 0;
        ftruncate(fd,pagesize);
        m_size = pagesize;
        ptr = static_cast<int8_t *>(mmap(0, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0));
    }
}

void MomeryFile::reloadFromFile() {
    CodeInput input(ptr + Fixed32Size,m_actualSize);
    while (!input.isAtEnd()){
        string key = input.readString();
        if (key.length() > 0){
            CodeInput* value = input.readData();
            auto itr = m_dic->find(key);
            if (itr != m_dic->end()){
                delete itr->second;
                m_dic->erase(key);
            }
            if (value && value->length() > 0){
                m_dic->emplace(key,value);
            }
        }
    }
}

int8_t *MomeryFile::getPtr() {
    return ptr;
}

unordered_map<string, CodeInput *> *MomeryFile::getCache() {
    return m_dic;
}

int32_t MomeryFile::getActualSize() {
    return m_actualSize;
}

int MomeryFile::getSize() {
    return m_size;
}

void MomeryFile::reSize(size_t needSize) {
    int oldSize = m_size;
    do {
        if (CURRENT_GROWTH == DOUBLE_GROWTH){
            m_size *= 2;
        } else if (CURRENT_GROWTH == MINIMUM_GROWTH){
            m_size += getpagesize();
        }
    } while (needSize >= m_size);
    ftruncate(fd,m_size);
    munmap(ptr,oldSize);
    ptr = (int8_t *) mmap(ptr, m_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
}
