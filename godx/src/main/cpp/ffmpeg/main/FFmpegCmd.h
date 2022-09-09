//
// Created by gwm on 2022/9/4.
//

#ifndef APPGODX_FFMPEGCMD_H
#define APPGODX_FFMPEGCMD_H
extern "C"{
#include <libavformat/avformat.h>
};
using namespace std;

class FFmpegCmd {

public:
    FFmpegCmd();
    void run(int argc, char *argv[]);
};


#endif //APPGODX_FFMPEGCMD_H
