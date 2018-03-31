package alice.memory.core

import alice.memory.*
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.BeansException
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import java.util.concurrent.ConcurrentHashMap

@Slf4j
@CompileStatic
class DialogService implements ApplicationContextAware, InitializingBean, DisposableBean {
    ApplicationContext applicationContext

    private Map<CommandType, CommandProcessor> commandProcessors

    @Override
    void afterPropertiesSet() throws Exception {
        Map<CommandType, CommandProcessor> map = [:]
        applicationContext.getBeansOfType(CommandProcessor).each { String key, CommandProcessor value ->
            map.put(value.type, value)
        }
        commandProcessors = Collections.unmodifiableMap(map)
    }

    @Override
    void destroy() throws Exception {
        commandProcessors = null
    }

    DialogResponse call(RawCommand rawCommand) {
        TypeAndText typeAndText = determineCommand(rawCommand)

        DialogCommand command = new DialogCommand(
                userId: rawCommand.userId,
                text: typeAndText.text,
                payload: rawCommand.payload
        )

        return commandProcessors.get(typeAndText.type).process(command)
    }

    TypeAndText determineCommand(RawCommand rawCommand) {
        if (rawCommand.buttonPressed) {
            String command = rawCommand.payload['command']
            if (command == 'more') {
                return new TypeAndText(type: CommandType.REMIND, text: '')
            } else if (command == 'forget') {
                return new TypeAndText(type: CommandType.FORGET, text: '')
            } else {
                return new TypeAndText(type: CommandType.NONE, text: '')
            }
        } else {
            return determineCommandByText(rawCommand.text)
        }
    }

    private static TypeAndText determineCommandByText(String text) {
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
            return new TypeAndText(
                    type: type,
                    text: text.substring(start + length).trim()
            )
        } else {
            return new TypeAndText(type: CommandType.NONE, text: text)
        }
    }
}
