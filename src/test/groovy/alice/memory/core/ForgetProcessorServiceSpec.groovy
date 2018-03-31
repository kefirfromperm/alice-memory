package alice.memory.core

import alice.memory.AliceUser
import alice.memory.DialogCommand
import alice.memory.Memory
import alice.memory.dao.AliceUserDaoService
import alice.memory.dao.MemoryDaoService
import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest

class ForgetProcessorServiceSpec extends HibernateSpec implements ServiceUnitTest<ForgetProcessorService>{

    def setup() {
        service.aliceUserDaoService = Mock(AliceUserDaoService)
        service.memoryDaoService = Mock(MemoryDaoService)
    }

    def cleanup() {
    }

    void "test forget something"() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.aliceUserDaoService.find('1234') >> user
        and: 'a memory'
            def memory = new Memory(text: 'Test response')
            memory.id = 42
        when:'forget'
            service.process(new DialogCommand(userId: '1234', payload: [id:42]))
        then:
            1 * service.memoryDaoService.forget(42, user)
    }
}
