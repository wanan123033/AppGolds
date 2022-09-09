//
// Created by zxw on 2022/9/9.
//

#include "ThreadLock.h"

ThreadLock::ThreadLock() {
    pthread_mutexattr_t attr;
    pthread_mutexattr_init(&attr);

    pthread_mutexattr_settype(&attr,PTHREAD_MUTEX_RECURSIVE);
    pthread_mutex_init(&m_lock,&attr);
    pthread_mutexattr_destroy(&attr);
}

ThreadLock::~ThreadLock() {
    pthread_mutex_destroy(&m_lock);
}

void ThreadLock::lock() {
    pthread_mutex_lock(&m_lock);
}

void ThreadLock::unlock() {
    pthread_mutex_unlock(&m_lock);
}

