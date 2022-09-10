//
// Created by zxw on 2022/9/9.
//
#include <jni.h>
#include "MMKV.h"

extern "C"
JNIEXPORT void JNICALL
Java_appgodx_mmkv_MMKV_jniInitialize(JNIEnv *env, jclass clazz, jstring root_path) {
    const char* rootDir = env->GetStringUTFChars(root_path,0);
    MMKV::initialize(rootDir);
    env->ReleaseStringUTFChars(root_path,rootDir);
}
extern "C"
JNIEXPORT jlong JNICALL
Java_appgodx_mmkv_MMKV_mmkvWithId(JNIEnv *env, jclass clazz, jstring mmap_id, jint mode) {
    const char* mmapId = env->GetStringUTFChars(mmap_id,0);
    auto mmkv = reinterpret_cast<jlong>(MMKV::mmkvWithId(mmapId, (MMKVMode) mode));
    return mmkv;
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_mmkv_MMKV_putInt(JNIEnv *env, jclass clazz, jlong handle, jstring key, jint value) {
    const char* cKey = env->GetStringUTFChars(key,0);
    MMKV* mmkv = reinterpret_cast<MMKV *>(handle);
    mmkv->putInt(cKey,value);
    env->ReleaseStringUTFChars(key,cKey);
}
extern "C"
JNIEXPORT jint JNICALL
Java_appgodx_mmkv_MMKV_getInt(JNIEnv *env, jclass clazz, jlong handle, jstring key) {
    const char* cKey = env->GetStringUTFChars(key,0);
    MMKV* mmkv = reinterpret_cast<MMKV *>(handle);
    int value = mmkv->getInt(cKey);
    env->ReleaseStringUTFChars(key,cKey);
    return value;
}
extern "C"
JNIEXPORT jstring JNICALL
Java_appgodx_mmkv_MMKV_getString(JNIEnv *env, jclass clazz, jlong handle, jstring key) {
    MMKV* mmkv = reinterpret_cast<MMKV *>(handle);
    const char* cKey = env->GetStringUTFChars(key,0);
    string str = mmkv->getString(cKey);
    jstring value = env->NewStringUTF(str.c_str());
    env->ReleaseStringUTFChars(key,cKey);
    return value;
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_mmkv_MMKV_putString(JNIEnv *env, jclass clazz, jlong handle, jstring key,
                                 jstring value) {
    MMKV* mmkv = reinterpret_cast<MMKV *>(handle);
    const char* cKey = env->GetStringUTFChars(key,0);
    const char* cValue = env->GetStringUTFChars(value,0);
    string str(cValue, strlen(cValue));
    mmkv->putString(cKey,str);
}