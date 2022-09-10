//
// Created by gwm on 2022/9/10.
//

#include "CodeOutput.h"

void CodeOutput::writeInt32(int value) {
    if (value < 0){
        writeInt64(value);
    } else{
        while (true){
            if (value <= 0x7f){
                writeByte(value);
                break;
            } else{
                writeByte(value & 0x7f | 0x80);
                value >>= 7;
            }
        }
    }
}

CodeOutput::CodeOutput(int8_t *buf, int size):buf(buf),size(size),position(0) {

}

void CodeOutput::writeInt64(int64_t value) {
    uint64_t i = value;
    while (true){
        if ((i & ~0x7f) == 0){
            writeByte(i);
            break;
        } else{
            writeByte(i & 0x7f | 0x80);
            value >>= 7;
        }
    }
}

void CodeOutput::writeByte(int8_t value) {
    if (position > size){
        return;
    }
    buf[position++] = value;
}

void CodeOutput::writeString(string str) {
    size_t size = str.size();
    writeInt32(size);
    memcpy(buf + position,str.data(),size);
    position += size;
}

size_t CodeOutput::speekLeft() {
    return size - position;
}

void CodeOutput::writeData(CodeInput *data) {

}
