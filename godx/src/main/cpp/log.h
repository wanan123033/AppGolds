//
// Created by gwm on 2022/9/3.
//

#ifndef APPGODX_LOG_H
#define APPGODX_LOG_H
#include <android/log.h>

#define LOG_TAG "debug"
#define LOGI(fmt, args...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, fmt, ##args)
#define LOGD(fmt, args...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, fmt, ##args)
#define LOGE(fmt, args...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, fmt, ##args)
#endif //APPGODX_LOG_H
