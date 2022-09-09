//
// Created by gwm on 2022/9/4.
//

#include "FFmpegCmd.h"
#include "../../log.h"

FFmpegCmd::FFmpegCmd() {
    avformat_network_init();
}

void FFmpegCmd::run(int argc, char *argv[]) {
    for (int i = 0; i < argc; ++i) {
        LOGE("%s",argv[i]);
    }
}
