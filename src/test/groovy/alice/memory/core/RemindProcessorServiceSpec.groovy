package alice.memory.core

import alice.memory.AliceUser
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import alice.memory.Memory
import alice.memory.dao.AliceUserDaoService
import alice.memory.dao.MemoryDaoService
import grails.test.hibernate.HibernateSpec
import grails.testing.services.ServiceUnitTest
import spock.lang.Unroll

class RemindProcessorServiceSpec extends HibernateSpec implements ServiceUnitTest<RemindProcessorService>{

    def setup() {
        service.aliceUserDaoService = Mock(AliceUserDaoService)
        service.memoryDaoService = Mock(MemoryDaoService)
    }

    def cleanup() {
    }

    @Unroll
    void 'test process text #text'(String expected, String text) {
        expect:
            expected == service.process(text)
        where:
            expected              | text
            'Привет'              | 'Привет'
            'Привет медвед'       | 'привет медвед'
            'Мне сегодня к врачу' | 'Мне сегодня к врачу'
    }

    void 'test remind'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.aliceUserDaoService.find('1234') >> user
        and: 'a memory'
            def memory = new Memory(text: 'Test response')
            memory.id = 42
            service.memoryDaoService.findByUser(user, 0) >> memory
        when: 'call remind'
            DialogResponse response = service.process(new DialogCommand(userId: '1234', payload: [offset: 0]))
        then:
            response.text == 'Test response'
            response.buttons.size() == 2
            response.buttons[0].title == 'Ещё'
            response.buttons[0].payload.command == 'more'
            response.buttons[0].payload.offset == 1
            response.buttons[1].title == 'Забудь'
            response.buttons[1].payload.command == 'forget'
            response.buttons[1].payload.id == 42
    }

    void 'test remind without any memories'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.aliceUserDaoService.find('1234') >> user
            service.memoryDaoService.findByUser(user, 0) >> null
        when: 'call remind'
            DialogResponse response = service.process(new DialogCommand(userId: '1234', payload: [offset: 0]))
        then:
            response.text == 'Я ничего не припоминаю.'
    }

    void 'test remind offset change'(){
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.aliceUserDaoService.find('1234') >> user
        and: 'memories'
            def memory1 = new Memory(text: 'Test response 1')
            memory1.id = 42
            def memory2 = new Memory(text: 'Test response 2')
            memory2.id = 43
            def memory3 = new Memory(text: 'Test response 3')
            memory3.id = 44
            service.memoryDaoService.findByUser(user, 0) >> memory3
            service.memoryDaoService.findByUser(user, 1) >> memory2
            service.memoryDaoService.findByUser(user, 2) >> memory1
        when: 'call remind'
            DialogResponse response = service.process(new DialogCommand(userId: '1234', payload: [offset: 1]))
        then:
            response.text == 'Test response 2'
            response.buttons.size() == 2
            response.buttons[0].title == 'Ещё'
            response.buttons[0].payload.command == 'more'
            response.buttons[0].payload.offset == 2
            response.buttons[1].title == 'Забудь'
            response.buttons[1].payload.command == 'forget'
            response.buttons[1].payload.id == 43
    }
}
