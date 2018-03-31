package alice.memory.core

import alice.memory.AliceUser
import alice.memory.Button
import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import alice.memory.Memory
import alice.memory.dao.AliceUserDaoService
import alice.memory.dao.MemoryDaoService
import grails.gorm.transactions.ReadOnly
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils

@Slf4j
@CompileStatic
class RemindProcessorService implements CommandProcessor {
    MemoryDaoService memoryDaoService
    AliceUserDaoService aliceUserDaoService

    @Override
    CommandType getType() {
        return CommandType.REMIND
    }

    @Override
    @ReadOnly
    DialogResponse process(DialogCommand command) {
        AliceUser user = aliceUserDaoService.find(command.userId)
        if (!user) {
            return nofFound()
        }

        int offset = command.payload ? command.payload['offset'] as int : 0
        Memory memory = memoryDaoService.findByUser(user, offset)
        if (!memory) {
            return nofFound()
        }

        return new DialogResponse(
                text: process(memory.text),
                buttons: [
                        new Button(title: 'Ещё', payload: [command: 'more', offset: offset + 1], hide: true),
                        new Button(title: 'Забудь', payload: [command: 'forget', id: memory.id], hide: true)
                ]
        )
    }

    private static DialogResponse nofFound() {
        return new DialogResponse(text: 'Я ничего не припоминаю.')
    }

    String process(String text) {
        return StringUtils.capitalize(text)
    }
}
