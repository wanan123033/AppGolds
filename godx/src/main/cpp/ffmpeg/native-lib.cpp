#include <jni.h>
#include "main/FFmpegPlayer.h"
#include "main/JavaCallHelper.h"
#include "../log.h"
#include <android/native_window_jni.h>
#include <main/FFmpegCmd.h>

JavaVM* jvm;
JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved){
    jvm = vm;
    return JNI_VERSION_1_4;
}
extern "C"
JNIEXPORT jlong JNICALL
Java_appgodx_ffmpeg_FFmpegPlayer_nativeInit(JNIEnv *env, jclass clazz) {
    return reinterpret_cast<jlong>(new FFmpegPlayer(new JavaCallHelper(jvm, env, clazz)));
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_ffmpeg_FFmpegPlayer_setDataSource(JNIEnv *env, jobject thiz, jlong handle,
                                               jstring path) {
    FFmpegPlayer* player = reinterpret_cast<FFmpegPlayer *>(handle);
    const char* str = env->GetStringUTFChars(path,0);
    player->setDataSource(str);
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_ffmpeg_FFmpegPlayer_prepare(JNIEnv *env, jobject thiz, jlong handle) {
    FFmpegPlayer* player = reinterpret_cast<FFmpegPlayer *>(handle);
    player->prepare();
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_ffmpeg_FFmpegPlayer_setSurface(JNIEnv *env, jobject thiz, jlong handle,
                                            jobject holder) {
    FFmpegPlayer* player = reinterpret_cast<FFmpegPlayer *>(handle);
    ANativeWindow* window = ANativeWindow_fromSurface(env,holder);
    player->setWindow(window);
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_ffmpeg_FFmpegPlayer_start(JNIEnv *env, jobject thiz, jlong handle) {
    FFmpegPlayer* player = reinterpret_cast<FFmpegPlayer *>(handle);
    player->start();
}
extern "C"
JNIEXPORT void JNICALL
Java_appgodx_ffmpeg_FFmpegCmd_exec(JNIEnv *env, jclass clazz, jobjectArray command) {
    int argc = env->GetArrayLength(command);
    char** argv = static_cast<char **>(malloc(argc * sizeof(char *)));
    for (int i = 0; i < argc; ++i) {
        jstring jstr = static_cast<jstring>(env->GetObjectArrayElement(command, i));
        const char* c = env->GetStringUTFChars(jstr,0);
        argv[i] = static_cast<char *>(malloc(sizeof(char) * strlen(c) + 1));
        strcpy(argv[i],c);
        env->ReleaseStringUTFChars(jstr,c);
    }
    FFmpegCmd* ffcmd = new FFmpegCmd();
    ffcmd->run(argc,argv);
}