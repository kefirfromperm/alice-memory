package alice.memory.dao

import alice.memory.AliceUser
import alice.memory.Memory
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class MemoryDaoServiceSpec extends Specification {
    private static final String TEXT = 'Test text'

    MemoryDaoService memoryDaoService

    @Autowired
    SessionFactory sessionFactory

    void "test save and find"() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234').save()
        when: 'create a memory'
            memoryDaoService.saveMemory(user, TEXT)
            sessionFactory.currentSession.flush()
        then: 'there is a memory'
            Memory.count() == 1
        when: 'find by user give the text'
            Memory memory = memoryDaoService.findByUser(user)
        then:
            memory != null
            memory.text == TEXT
    }

    void 'test update'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234').save()
        and: 'a memory'
            Memory memory = new Memory(user: user, text: 'TEXT').save()
            sessionFactory.currentSession.flush()
        when: 'update active'
            memoryDaoService.forget(memory.id, user)
        then: 'memory is inactive'
            !Memory.get(memory.id).active
    }
}
