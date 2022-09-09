//
// Created by gwm on 2022/9/3.
//

#ifndef APPGODX_JAVACALLHELPER_H
#define APPGODX_JAVACALLHELPER_H


#include <jni.h>

class JavaCallHelper {

public:
    JavaCallHelper(_JavaVM *pVm, _JNIEnv *pEnv, _jclass *pJclass);
};


#endif //APPGODX_JAVACALLHELPER_H
