package alice.memory.core

import alice.memory.*
import alice.memory.dao.AliceUserDaoService
import alice.memory.dao.MemoryDaoService
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.kefirsf.bb.TextProcessor

@Slf4j
@CompileStatic
class DialogService {
    AliceUserDaoService aliceUserDaoService
    MemoryDaoService memoryDaoService
    TextProcessor textProcessor

    DialogResponse call(RawCommand rawCommand) {
        DialogCommand command = determineCommand(rawCommand)

        AliceUser user = aliceUserDaoService.findOrSave(rawCommand.yandexId)
        DialogResponse response
        switch (command?.type) {
            case CommandType.REMEMBER:
                response = remember(user, command.text)
                break
            case CommandType.REMIND:
                response = remind(user)
                break
            default:
                response = new DialogResponse(text: 'Я могу запомнить и напомнить.')
        }
        return response
    }

    DialogResponse remember(AliceUser user, String text) {
        if (!StringUtils.isBlank(text)) {
            memoryDaoService.saveMemory(user, text)
            return new DialogResponse(text: 'Запомнила.')
        } else {
            return new DialogResponse(text: 'Что запомнить?')
        }
    }

    DialogResponse remind(AliceUser user) {
        Memory memory = memoryDaoService.findByUser(user)
        if (memory) {
            return new DialogResponse(
                    text: process(memory.text),
                    buttons: [
                            new Button(title: 'Ещё', payload: [offset: 1], hide: true),
                            new Button(title: 'Забудь', hide: true)
                    ]
            )
        } else {
            return new DialogResponse(text: 'Я ничего не припоминаю.')
        }
    }

    String process(String text) {
        return StringUtils.capitalize(text)
    }

    DialogCommand determineCommand(RawCommand rawCommand) {
        CommandType type = null
        int start = -1
        int length = 0

        CommandType.values().each { CommandType ct ->
            ct.phrases.each { String phrase ->
                int position = StringUtils.indexOfIgnoreCase(rawCommand.text, phrase)
                if (position >= 0 && (type == null || position < start)) {
                    type = ct
                    start = position
                    length = phrase.length()
                }
            }
        }

        if (type != null) {
            return new DialogCommand(
                    type: type,
                    text: rawCommand.text.substring(start + length).trim()
            )
        } else {
            return null
        }
    }
}
