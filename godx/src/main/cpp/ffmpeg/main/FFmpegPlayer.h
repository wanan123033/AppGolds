//
// Created by gwm on 2022/9/3.
//

#ifndef APPGODX_FFMPEGPLAYER_H
#define APPGODX_FFMPEGPLAYER_H


#include <android/native_window.h>
#include "JavaCallHelper.h"
#include "VideoChannel.h"

extern "C"{
#include <libavformat/avformat.h>
};

class FFmpegPlayer {
    friend void* _prepare(void* args);
public:
    FFmpegPlayer(JavaCallHelper *pHelper);

    void setDataSource(const char *path);

    void prepare();

    void setWindow(ANativeWindow *pWindow);

    void start();

private:
    const char* path;
    int64_t duration;
    AVFormatContext* fmt_ctx = NULL;
    VideoChannel* videoChannel;
    pthread_t prepareTask;
    void prepareAsync();

    JavaCallHelper *helper;
};


#endif //APPGODX_FFMPEGPLAYER_H
