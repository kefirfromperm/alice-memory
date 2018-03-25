package alice.memory

import alice.memory.core.DialogService
import grails.converters.JSON
import groovy.util.logging.Slf4j

@Slf4j
class ApplicationController {
    static allowedMethods = [index: 'POST']

    DialogService dialogService

    def index() {
        def json = request.JSON

        String userId = json.session.user_id
        long messageId = json.session.message_id
        String sessionId = json.session.session_id
        String text = json.request.original_utterance

        log.info("Request from user $userId: $text")

        DialogResponse dialogResponse = dialogService.call(userId, text)

        log.info("Response to user $userId: ${dialogResponse}")

        def protocolResponse = [
                text       : dialogResponse.text,
                end_session: false
        ]

        if (dialogResponse.buttons) {
            protocolResponse.buttons = dialogResponse.buttons.collect { Button button ->
                def protocolButton = [
                        title: button.title,
                        hide : button.hide
                ]

                if (button.payload) {
                    protocolButton.payload = button.payload
                }

                return protocolButton
            }
        }

        render([
                response: protocolResponse,
                session : [
                        session_id: sessionId,
                        message_id: messageId,
                        user_id   : userId
                ],
                version : '1.0'
        ] as JSON)
    }
}
