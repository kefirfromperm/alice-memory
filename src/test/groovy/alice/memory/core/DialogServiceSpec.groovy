package alice.memory.core

import alice.memory.CommandType
import alice.memory.DialogResponse
import alice.memory.RawCommand
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class DialogServiceSpec extends Specification implements ServiceUnitTest<DialogService> {
    def setup() {
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
            'Что-то с памятью моей стало' | CommandType.NONE
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
}
