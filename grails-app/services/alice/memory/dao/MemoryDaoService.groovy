package alice.memory.dao

import alice.memory.Memory
import alice.memory.AliceUser
import grails.gorm.services.Service

@Service(Memory)
interface MemoryDaoService {
    Memory saveMemory(AliceUser user, String text)

    Memory findByUser(AliceUser user)
}
