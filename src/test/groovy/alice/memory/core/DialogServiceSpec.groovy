package alice.memory.core

import alice.memory.CommandType
import alice.memory.DialogResponse
import alice.memory.Memory
import alice.memory.AliceUser
import alice.memory.RawCommand
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
            command == service.determineCommand(new RawCommand(text: text))?.type
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
    void 'test determine button command #command'(String command, CommandType commandType) {
        expect: "determine right command"
            commandType == service.determineCommand(
                    new RawCommand(buttonPressed: true, payload: [command: command])
            )?.type
        where:
            command  | commandType
            'more'   | CommandType.REMIND
            'forget' | CommandType.FORGET
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
            DialogResponse response = service.remember(user, 'Test text')
        then:
            1 * service.memoryDaoService.saveMemory(user, 'Test text')
            response.text == 'Запомнила.'
    }

    void 'remember empty text'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
        when: 'remember empty text'
            DialogResponse response = service.remember(user, '')
        then:
            0 * service.memoryDaoService.saveMemory(user, 'Test text')
            response.text == 'Что запомнить?'
    }

    void 'test remind'() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
        and: 'a memory'
            def memory = new Memory(text: 'Test response')
            memory.id = 42
            service.memoryDaoService.findByUser(user, 0) >> memory
        when: 'call remind'
            DialogResponse response = service.remind(user, 0)
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
            service.memoryDaoService.findByUser(user, 0) >> null
        when: 'call remind'
            DialogResponse response = service.remind(user, 0)
        then:

            response.text == 'Я ничего не припоминаю.'
    }

    void 'test remind offset change'(){
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
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
            DialogResponse response = service.remind(user, 1)
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

    void 'test call without command'() {
        when: 'call without command'
            DialogResponse response = service.call(new RawCommand(yandexId: '1234', text: 'test'))
        then:
            response.text == 'Я могу запомнить и напомнить.'
    }
}
