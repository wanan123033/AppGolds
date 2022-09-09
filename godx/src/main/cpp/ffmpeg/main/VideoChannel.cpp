//
// Created by gwm on 2022/9/4.
//

#include "VideoChannel.h"

VideoChannel::VideoChannel(AVCodecID id, AVCodecContext *pContext, JavaCallHelper *pHelper,
                           AVStream *pStream, int fps): BaseChannel(id,pContext,pHelper,pStream) {

}

void VideoChannel::setWindow(ANativeWindow *pWindow) {
    this->pWindow = pWindow;
}

void VideoChannel::start() {

}
