package alice.memory.core

import alice.memory.CommandType
import alice.memory.DialogCommand
import alice.memory.DialogResponse

interface CommandProcessor {
    CommandType getType()
    DialogResponse process(DialogCommand command)
}