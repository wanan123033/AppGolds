//
// Created by gwm on 2022/9/4.
//

#ifndef APPGODX_BASECHANNEL_H
#define APPGODX_BASECHANNEL_H



#include "JavaCallHelper.h"

extern "C"{
#include <libavcodec/avcodec.h>
#include <libavformat/avformat.h>
};
class BaseChannel {

protected:
    BaseChannel(AVCodecID id, AVCodecContext *pContext, JavaCallHelper *pHelper, AVStream *pStream);
};


#endif //APPGODX_BASECHANNEL_H
