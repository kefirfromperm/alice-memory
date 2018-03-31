package alice.memory

import alice.memory.core.DialogService
import groovy.util.logging.Slf4j

@Slf4j
class ApplicationController {
    static allowedMethods = [index: 'POST']

    DialogService dialogService

    def index() {
        RawCommand command = bind(request.JSON)
        ResponseModel model
        try {
            log.info("Request $command")

            DialogResponse dialogResponse = dialogService.call(command)

            log.info("Response to user ${command.userId}: ${dialogResponse}")

            model = new ResponseModel(
                    text: dialogResponse.text,
                    tts: dialogResponse.tts,
                    buttons: dialogResponse.buttons,
                    sessionId: command.sessionId,
                    messageId: command.messageId,
                    userId: command.userId
            )
        } catch (Exception e) {
            log.error(e.message, e)
            model = new ResponseModel(
                    text: 'Что-то пошло не так.',
                    sessionId: command.sessionId,
                    messageId: command.messageId,
                    userId: command.userId
            )
        }

        render model: [model: model], view: 'index'
    }

    private static RawCommand bind(json) {
        new RawCommand(
                messageId: json.session.message_id,
                sessionId: json.session.session_id,
                userId: json.session.user_id,
                text: json.request.original_utterance,
                payload: json.request.payload,
                buttonPressed: (json.request.type == 'ButtonPressed')
        )
    }
}
