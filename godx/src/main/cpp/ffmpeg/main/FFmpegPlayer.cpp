//
// Created by gwm on 2022/9/3.
//
#include <pthread.h>
#include "FFmpegPlayer.h"
#include "../../log.h"

void* _prepare(void* args){
    FFmpegPlayer* player = static_cast<FFmpegPlayer *>(args);
    player->prepareAsync();
    return 0;
}

FFmpegPlayer::FFmpegPlayer(JavaCallHelper *pHelper) {
    this->helper = pHelper;
    avformat_network_init();
}

void FFmpegPlayer::setDataSource(const char *path) {
    this->path = path;
}

void FFmpegPlayer::prepare() {
    pthread_create(&prepareTask,NULL,_prepare, this);
}

void FFmpegPlayer::prepareAsync() {
    LOGE("path=%s",path);
    int ret = avformat_open_input(&fmt_ctx,path,NULL,NULL);
    if (ret < 0){
        LOGE("fail open file %s,error:%s",path, av_err2str(ret));
        return;
    }
    ret = avformat_open_input(&fmt_ctx,path,NULL,NULL);
    if (ret != 0){
        LOGE("fail open file %s,error:%s",path, av_err2str(ret));
        return;
    }
    ret = avformat_find_stream_info(fmt_ctx,NULL);
    if (ret < 0){
        LOGE("fail find stream file %s,error:%s",path, av_err2str(ret));
        return;
    }
    duration = fmt_ctx->duration / AV_TIME_BASE;
    for (int i = 0; i < fmt_ctx->nb_streams; ++i) {
        AVStream* stream = fmt_ctx->streams[i];
        AVCodecParameters* codecParameters = stream->codecpar;
        AVCodec* codec = avcodec_find_decoder(codecParameters->codec_id);
        if (!codec){
            LOGE("codec is null");
            return;
        }
        AVCodecContext* codecContext = avcodec_alloc_context3(codec);
        if (avcodec_parameters_to_context(codecContext,codecParameters) < 0){
            LOGE("fail to avcodec_parameters_to_context");
            return;
        }
        if (avcodec_open2(codecContext,codec,NULL) != 0){
            LOGE("fail to avcodec_open2");
            return;
        }
        if (codecParameters->codec_type == AVMEDIA_TYPE_AUDIO){

        } else if (codecParameters->codec_type == AVMEDIA_TYPE_VIDEO){
            int fps = av_q2d(stream->avg_frame_rate);
            videoChannel = new VideoChannel(codecParameters->codec_id,codecContext,helper,stream,fps);
        }
    }
}

void FFmpegPlayer::setWindow(ANativeWindow *pWindow) {
    if (videoChannel){
        videoChannel->setWindow(pWindow);
    }
}

void FFmpegPlayer::start() {
    if (videoChannel){
        videoChannel->start();
    }
}
