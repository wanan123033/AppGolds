//
// Created by gwm on 2022/9/10.
//

#ifndef APPGODX_PBUTILITY_H
#define APPGODX_PBUTILITY_H
namespace pbu{
    extern size_t compileSize(int value){
        if (value < 0){
            return 10;
        }
        if ((value & (0xffffffff << 7)) == 0){
            return 1;
        } else if ((value & (0xffffffff << 14)) == 0){
            return 2;
        } else if ((value & (0xffffffff << 21)) == 0){
            return 3;
        } else if ((value & (0xffffffff << 28)) == 0){
            return 4;
        }
        return 5;
    }

    extern size_t compileSize(const char *key, CodeInput* value) {
        int keyLength = strlen(key);
        int32_t size = keyLength + compileSize(keyLength);
        size += value->length() + compileSize(value->length());
        return size;
    }
    extern size_t compileSize(unordered_map<string,CodeInput*>* m_cache){
        return 0;
    }
}
#endif //APPGODX_PBUTILITY_H
