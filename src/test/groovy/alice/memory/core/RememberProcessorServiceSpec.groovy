package alice.memory.core

import alice.memory.AliceUser
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import alice.memory.dao.AliceUserDaoService
import alice.memory.dao.MemoryDaoService
import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest

class RememberProcessorServiceSpec extends HibernateSpec implements ServiceUnitTest<RememberProcessorService>{

    def setup() {
        service.aliceUserDaoService = Mock(AliceUserDaoService)
        service.memoryDaoService = Mock(MemoryDaoService)
    }

    def cleanup() {
    }

    void 'remember'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.aliceUserDaoService.findOrSave('1234') >> user
        when: 'remember text'
            DialogResponse response = service.process(new DialogCommand(userId: '1234', text:'Test text'))
        then:
            1 * service.memoryDaoService.saveMemory(user, 'Test text')
            response.text == 'Запомнила.'
    }

    void 'remember empty text'() {
        when: 'remember empty text'
            DialogResponse response = service.process(new DialogCommand(userId: '1234', text:''))
        then:
            0 * service.aliceUserDaoService.findOrSave(_ as String)
            0 * service.memoryDaoService.saveMemory(_ as AliceUser, _ as String)
            response.text == 'Что запомнить?'
    }
}
