package alice.memory.dao

import alice.memory.Memory
import alice.memory.User
import grails.gorm.services.Service

@Service(Memory)
interface MemoryDaoService {
    Memory saveMemory(User user, String text)

    Memory findByUser(User user)
}
