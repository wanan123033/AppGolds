//
// Created by zxw on 2022/9/9.
//

#include "MMKV.h"
#include "ScopedLock.h"

MMKV::MMKV(const char *mmapId, MMKVMode mode):
        m_mmapId(mmapId),
        m_mode(mode),
        m_path(g_rootDir + "/" + mmapId),
        m_crcPath(m_path+".crc"),
        m_mateFile(new MomeryFile(m_crcPath)),
        m_file(new MomeryFile(m_path)),
        m_fileLock(m_mateFile->getFd()),
        m_readLock(&m_fileLock,SharedLockType),
        m_writeLock(&m_fileLock,ExclusiveLockType),
        isInterProgress(mode & MMKV_MULTI_PROCESS)
{
    SCOPEDLOCK(m_readLock);
    loadFromFile();
}

void MMKV::loadFromFile() {
    m_file->loadFromFile();
    m_ptr = m_file->getPtr();
    m_cache = m_file->getCache();
    m_output = new CodeOutput(m_ptr + Fixed32Size + m_file->getActualSize(),m_file->getSize() - Fixed32Size - m_file->getActualSize());
}

