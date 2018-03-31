package alice.memory.core

import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.DialogResponse
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

@Slf4j
@CompileStatic
class NoneProcessorService implements CommandProcessor {
    @Override
    CommandType getType() {
        return CommandType.NONE
    }

    @Override
    DialogResponse process(DialogCommand command) {
        return new DialogResponse(
                text: 'Я могу запомнить и напомнить.',
                tts: 'Я могу запомнить - и напомнить.'
        )
    }
}
