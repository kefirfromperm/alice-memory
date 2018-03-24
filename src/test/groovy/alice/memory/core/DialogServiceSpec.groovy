package alice.memory.core

import alice.memory.CommandType
import alice.memory.Memory
import alice.memory.AliceUser
import alice.memory.dao.MemoryDaoService
import alice.memory.dao.AliceUserDaoService
import grails.testing.services.ServiceUnitTest
import org.kefirsf.bb.BBProcessorFactory
import spock.lang.Specification
import spock.lang.Unroll

class DialogServiceSpec extends Specification implements ServiceUnitTest<DialogService> {
    def setup() {
        service.textProcessor = BBProcessorFactory.instance.create()
        service.aliceUserDaoService = Mock(AliceUserDaoService)
        service.memoryDaoService = Mock(MemoryDaoService)
    }

    def cleanup() {
    }

    @Unroll
    void "test determine command #command for \"#text\""(String text, CommandType command) {
        expect: "determine right command"
            command == service.determineCommand(text)?.type
        where:
            text                          | command
            'Запомнить'                   | CommandType.REMEMBER
            'Запомни'                     | CommandType.REMEMBER
            'Запомнить это'               | CommandType.REMEMBER
            'Запомни это'                 | CommandType.REMEMBER
            'Надо запомнить это'          | CommandType.REMEMBER
            'Напомнить'                   | CommandType.REMIND
            'Напомни'                     | CommandType.REMIND
            'Что-то с памятью моей стало' | null
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

    void 'remember'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
        when: 'remember text'
            String response = service.remember(user, 'Test text')
        then:
            1 * service.memoryDaoService.saveMemory(user, 'Test text')
            response == 'Запомнила.'
    }

    void 'remember empty text'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
        when: 'remember empty text'
            String response = service.remember(user, '')
        then:
            0 * service.memoryDaoService.saveMemory(user, 'Test text')
            response == 'Что запомнить?'
    }

    void 'test remind'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.memoryDaoService.findByUser(user) >> new Memory(text: 'Test response')
        when: 'call remind'
            String response = service.remind(user)
        then:
            response == 'Test response'
    }

    void 'test remind without any memories'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
            service.memoryDaoService.findByUser(user) >> null
        when: 'call remind'
            String response = service.remind(user)
        then:
            response == 'Я ничего не припоминаю.'
    }

    void 'test call without command'() {
        when: 'call without command'
            String response = service.call('1234', 'test')
        then:
            response == 'Я могу запомнить и напомнить.'
    }
}
