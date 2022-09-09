//
// Created by zxw on 2022/9/9.
//

#ifndef APPGODX_FILELOCK_H
#define APPGODX_FILELOCK_H


class FileLock {

public:
    FileLock(int fd);

    void lock();

    void unlock();
};


#endif //APPGODX_FILELOCK_H
