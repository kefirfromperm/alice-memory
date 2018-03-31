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

@Slf4j
@CompileStatic
class ForgetProcessorService implements CommandProcessor {
    MemoryDaoService memoryDaoService
    AliceUserDaoService aliceUserDaoService

    @Override
    CommandType getType() {
        return CommandType.FORGET
    }

    @Override
    @Transactional
    DialogResponse process(DialogCommand command) {
        AliceUser user = aliceUserDaoService.find(command.userId)
        if (user && memoryDaoService.forget(command.payload['id'] as long, user)) {
            return new DialogResponse(text: 'Забыла.')
        } else {
            return new DialogResponse(text: 'Я не смогла забыть.')
        }
    }
}
