//
// Created by gwm on 2022/9/10.
//

#include "CodeInput.h"

CodeInput::CodeInput(int8_t *ptr, int32_t actualSize):ptr(ptr),actualSize(actualSize),position(0) {

}

bool CodeInput::isAtEnd() {
    return position == actualSize;
}

string CodeInput::readString() {
    int len = read32Int();
    if (len <= actualSize - position && len > 0){
        string result((char*)ptr + position,len);
        position += len;
        return result;
    }
    return nullptr;
}

CodeInput *CodeInput::readData() {
    return nullptr;
}

int32_t CodeInput::length() {
    return actualSize;
}

int32_t CodeInput::read32Int() {
    return 0;
}

void CodeInput::restore() {
    position = 0;
}

CodeInput::CodeInput(size_t size) {
    actualSize = size;
    position = 0;
    if (actualSize > 0)
        ptr = static_cast<int8_t *>(malloc(actualSize));
}

int8_t *CodeInput::data() {
    return ptr;
}
