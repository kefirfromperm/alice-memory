package alice.memory.dao

import alice.memory.AliceUser
import alice.memory.Memory
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification
import spock.lang.Unroll

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

    void 'test forget'() {
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

    @Unroll
    void 'test search "#query" in "#text"'(String text, String query, boolean found) {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234').save()
        and: 'a memory'
            Memory memory = new Memory(user: user, text: text).save()
            sessionFactory.currentSession.flush()
        when: 'search by #query'
            Memory result = memoryDaoService.search(user, query, 0)
        then: 'depends of found flag'
            found ^ (result == null)
            !found || (memory.id == result.id)
        where:
            text                               | query         | found
            'Купить яйца, хлеб, молоко'        | 'Что купить?' | true
            'Билеты на самолет'                | 'самолет'     | true
            'Кошка за мышкой, мышка за кошкой' | 'Кошки мышки' | true
            'Кот, собака'                      | 'Медведь'     | false
    }

    void 'test pagination'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234').save()
        and: 'a memory'
            Memory memory1 = new Memory(user: user, text: 'Купить яйца хлеб молоко').save()
        and: 'one more'
            Memory memory2 = new Memory(user: user, text: 'Купить билеты на самолет').save()
            sessionFactory.currentSession.flush()
        when: 'search by query offset=0'
            Memory result0 = memoryDaoService.search(user, 'Купить', 0)
        then:
            result0.id == memory2.id
        when: 'search by query offset=1'
            Memory result1 = memoryDaoService.search(user, 'Купить', 1)
        then:
            result1.id == memory1.id
    }
}
