//
// Created by zxw on 2022/9/9.
//

#ifndef APPGODX_INTERPROGRESSLOCK_H
#define APPGODX_INTERPROGRESSLOCK_H

#include "FileLock.h"

enum LockType {
    SharedLockType,    //共享锁 读锁，我读你也只能读，不可加写锁
    ExclusiveLockType, //排他锁 写锁，只能一个单位获得
};

class InterProgressLock {

public:
    InterProgressLock(FileLock *pLock, LockType type);
    void lock();
    void unlock();
private:
    FileLock* pLock;
    LockType type;
};


#endif //APPGODX_INTERPROGRESSLOCK_H
