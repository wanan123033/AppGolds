//
// Created by gwm on 2022/9/4.
//

#ifndef APPGODX_VIDEOCHANNEL_H
#define APPGODX_VIDEOCHANNEL_H

#include <android/native_window.h>
#include "BaseChannel.h"
#include "JavaCallHelper.h"

class VideoChannel : BaseChannel{

public:
    VideoChannel(AVCodecID id, AVCodecContext *pContext, JavaCallHelper *pHelper, AVStream *pStream,
                 int fps);

    void setWindow(ANativeWindow *pWindow);

    void start();

private:
    ANativeWindow *pWindow;
};


#endif //APPGODX_VIDEOCHANNEL_H
