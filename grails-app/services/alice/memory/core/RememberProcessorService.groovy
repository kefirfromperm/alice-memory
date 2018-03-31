package alice.memory.core

import alice.memory.AliceUser
import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import alice.memory.dao.AliceUserDaoService
import alice.memory.dao.MemoryDaoService
import grails.gorm.transactions.Transactional
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils

@Slf4j
@CompileStatic
class RememberProcessorService implements CommandProcessor {
    MemoryDaoService memoryDaoService
    AliceUserDaoService aliceUserDaoService

    @Override
    CommandType getType() {
        return CommandType.REMEMBER
    }

    @Override
    @Transactional
    DialogResponse process(DialogCommand command) {
        if (!StringUtils.isBlank(command.text)) {
            AliceUser user = aliceUserDaoService.findOrSave(command.userId)
            memoryDaoService.saveMemory(user, command.text)
            return new DialogResponse(text: 'Запомнила.')
        } else {
            return new DialogResponse(text: 'Что запомнить?')
        }
    }
}
