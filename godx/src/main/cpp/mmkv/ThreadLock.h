//
// Created by zxw on 2022/9/9.
//

#ifndef APPGODX_THREADLOCK_H
#define APPGODX_THREADLOCK_H
#include <pthread.h>

class ThreadLock {
public:
    ThreadLock();
    ~ThreadLock();
    void lock();
    void unlock();
private:
    pthread_mutex_t m_lock;
};


#endif //APPGODX_THREADLOCK_H
