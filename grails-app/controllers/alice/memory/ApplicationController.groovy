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

        String responseText = dialogService.call(userId, text)

        log.info("Response to user $userId: $responseText")

        render([
                response: [
                        text       : responseText,
                        end_session: false
                ],
                session : [
                        session_id: sessionId,
                        message_id: messageId,
                        user_id   : userId
                ],
                version : '1.0'
        ] as JSON)
    }
}
