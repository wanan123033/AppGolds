//
// Created by gwm on 2022/9/10.
//

#ifndef APPGODX_CODEOUTPUT_H
#define APPGODX_CODEOUTPUT_H


#include <cstdint>
#include <string>
#include "CodeInput.h"

using namespace std;

class CodeOutput {

public:
    CodeOutput(int8_t *buf, int size);

    void writeInt32(int value);

    void writeString(string str);

    size_t speekLeft();

    void writeData(CodeInput *data);
    void writeInt64(int64_t value);
    void writeByte(int8_t value);
private:
    int8_t *buf;
    int size;



    int position;
};


#endif //APPGODX_CODEOUTPUT_H
