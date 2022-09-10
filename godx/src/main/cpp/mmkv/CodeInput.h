//
// Created by gwm on 2022/9/10.
//

#ifndef APPGODX_CODEINPUT_H
#define APPGODX_CODEINPUT_H


#include <cstdint>
#include <string>

using namespace std;

class CodeInput {

public:
    CodeInput(int8_t *ptr, int32_t actualSize);

    CodeInput(size_t size);

    bool isAtEnd();

    string readString();

    CodeInput *readData();

    int32_t length();

    int32_t read32Int();

    void restore();

    int8_t * data();

private:
    int8_t *ptr;
    int32_t actualSize;
    int position;

};


#endif //APPGODX_CODEINPUT_H
