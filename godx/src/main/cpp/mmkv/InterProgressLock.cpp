//
// Created by zxw on 2022/9/9.
//

#include "InterProgressLock.h"

InterProgressLock::InterProgressLock(FileLock *pLock, LockType type):pLock(pLock),type(type) {

}

void InterProgressLock::lock() {
    pLock->lock();
}

void InterProgressLock::unlock() {
    pLock->unlock();
}
