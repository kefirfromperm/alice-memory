package alice.memory.dao

import alice.memory.AliceUser
import alice.memory.Memory
import grails.gorm.services.Service
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional

@Service(Memory)
abstract class MemoryDaoService {
    abstract Memory saveMemory(AliceUser user, String text)

    @ReadOnly
    Memory findByUser(AliceUser user, int offset = 0) {
        return Memory.findAllByUserAndActive(user, true, [sort: 'dateCreated', order: 'desc', offset: offset])[0]
    }

    @ReadOnly
    Memory search(AliceUser user, String query, int offset = 0){
        return Memory.withCriteria(uniqueResult:true)  {
            eq('user', user)
            sqlRestriction('to_tsvector(\'russian\', text) @@ plainto_tsquery(\'russian\', ?)', [query])
            order('dateCreated', 'desc')
            firstResult(offset)
            maxResults(1)
        } as Memory
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
