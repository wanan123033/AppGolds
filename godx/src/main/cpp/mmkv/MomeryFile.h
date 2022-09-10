//
// Created by zxw on 2022/9/9.
//

#ifndef APPGODX_MOMERYFILE_H
#define APPGODX_MOMERYFILE_H
#include <string>
#include <unordered_map>
#include "CodeInput.h"

using namespace std;
#define Fixed32Size 4
class MomeryFile {
public:
    MomeryFile(string path);

    int getFd();

    void loadFromFile();

    int8_t *getPtr();

    unordered_map<string, CodeInput *> *getCache();

    int32_t getActualSize();

    int getSize();

    void reSize(size_t needSize);

private:
    string path;
    int fd;
    long long int m_size;   //文件的总大小
    int8_t* ptr;  //文件映射
    int32_t m_actualSize;   //文件用了多少内存
    unordered_map<string,CodeInput*>* m_dic; //文件内容

    void reloadFromFile();
};


#endif //APPGODX_MOMERYFILE_H
