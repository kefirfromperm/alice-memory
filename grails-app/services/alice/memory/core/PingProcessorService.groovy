package alice.memory.core

import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class PingProcessorService implements CommandProcessor {
    @Override
    CommandType getType() {
        return CommandType.PING
    }

    @Override
    DialogResponse process(DialogCommand command) {
        return new DialogResponse(text: 'pong')
    }
}
