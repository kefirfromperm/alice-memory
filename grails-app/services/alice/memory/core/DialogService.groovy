package alice.memory.core

import alice.memory.Button
import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import alice.memory.Memory
import alice.memory.AliceUser
import alice.memory.dao.MemoryDaoService
import alice.memory.dao.AliceUserDaoService
import grails.gorm.transactions.ReadOnly
import grails.gorm.transactions.Transactional
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.kefirsf.bb.TextProcessor

@Slf4j
class DialogService {
    AliceUserDaoService aliceUserDaoService
    MemoryDaoService memoryDaoService
    TextProcessor textProcessor

    DialogResponse call(String userId, String text) {
        DialogCommand command = determineCommand(text)

        AliceUser user = aliceUserDaoService.findOrSave(userId)
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

    DialogCommand determineCommand(String text) {
        CommandType type = null
        int start = -1
        int length = 0

        CommandType.values().each { CommandType ct ->
            ct.phrases.each { String phrase ->
                int position = StringUtils.indexOfIgnoreCase(text, phrase)
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
                    text: text.substring(start + length).trim()
            )
        } else {
            return null
        }
    }
}
