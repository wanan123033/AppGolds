//
// Created by zxw on 2022/9/9.
//

#ifndef APPGODX_MOMERYFILE_H
#define APPGODX_MOMERYFILE_H
#include <string>
using namespace std;

class MomeryFile {
public:
    MomeryFile(string path);

    int getFd();
};


#endif //APPGODX_MOMERYFILE_H
