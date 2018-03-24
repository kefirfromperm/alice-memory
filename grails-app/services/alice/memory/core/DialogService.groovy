package alice.memory.core

import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.Memory
import alice.memory.User
import alice.memory.dao.MemoryDaoService
import alice.memory.dao.UserDaoService
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.kefirsf.bb.TextProcessor

@Slf4j
class DialogService {
    UserDaoService userDaoService
    MemoryDaoService memoryDaoService
    TextProcessor textProcessor

    String call(String userId, String text) {
        DialogCommand command = determineCommand(text)

        User user = userDaoService.findOrSave(userId)
        String response
        switch (command?.type) {
            case CommandType.REMEMBER:
                response = remember(user, command.text)
                break
            case CommandType.REMIND:
                response = remind(user)
                break
            default:
                response = 'Я могу запомнить и напомнить.'
        }
        return response
    }

    String remember(User user, String text) {
        if (!StringUtils.isBlank(text)) {
            memoryDaoService.saveMemory(user, text)
            return 'Запомнила.'
        } else {
            return 'Что запомнить?'
        }
    }

    String remind(User user) {
        Memory memory = memoryDaoService.findByUser(user)
        if (memory) {
            return process(memory.text)
        } else {
            return 'Я ничего не припоминаю.'
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
