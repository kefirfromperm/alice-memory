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
            return nofFound(0, '')
        }

        int offset = readOffset(command)

        Memory memory

        String query = command.payload ? command.payload.query : command.text
        if (StringUtils.isNotBlank(query)) {
            memory = memoryDaoService.search(user, query, offset)
        } else {
            memory = memoryDaoService.findByUser(user, offset)
        }

        if (memory) {
            return new DialogResponse(
                    text: process(memory.text),
                    buttons: [
                            new Button(title: 'Ещё', payload: prepareMorePayload(offset, query), hide: true),
                            new Button(title: 'Забудь', payload: [command: 'forget', id: memory.id], hide: true)
                    ]
            )
        } else {
            return nofFound(offset, query)
        }
    }

    private static Map<String, ?> prepareMorePayload(int offset, String query) {
        Map<String, ?> morePayload = [command: 'more', offset: offset + 1]

        if (StringUtils.isNotBlank(query)) {
            morePayload.put('query', query)
        }

        return morePayload
    }

    private static int readOffset(DialogCommand command) {
        command.payload ? command.payload['offset'] as int : 0
    }

    private static DialogResponse nofFound(int offset, String query) {
        StringBuilder message = new StringBuilder()
        message.append('Я ')
        if (offset > 0) {
            message.append('больше ')
        }
        message.append('ничего не припоминаю')
        if (StringUtils.isNotBlank(query)) {
            message.append(' про ')
            message.append(query)
        }
        message.append('.')
        return new DialogResponse(text: message.toString())
    }

    String process(String text) {
        return StringUtils.capitalize(text)
    }
}
