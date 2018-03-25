package alice.memory.dao

import alice.memory.Memory
import alice.memory.AliceUser
import grails.gorm.services.Service
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@Service(Memory)
abstract class MemoryDaoService {
    abstract Memory saveMemory(AliceUser user, String text)

    @ReadOnly
    Memory findByUser(AliceUser user, int offset = 0) {
        return Memory.findAllByUser(user, [sort: 'dateCreated', order: 'desc', offset: offset])[0]
    }

    @Transactional
    boolean forget(long id, AliceUser user){
        Memory memory = Memory.findByIdAndUser(id, user)
        if(memory){
            memory.active = false
            memory.save()
            return true
        }else{
            return false
        }
    }
}
