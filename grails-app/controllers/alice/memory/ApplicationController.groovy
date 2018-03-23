package alice.memory

import alice.memory.core.DialogService
import grails.converters.JSON

class ApplicationController {
    DialogService dialogService

    def index() {
        def json = request.JSON

        String userId = json.session.user_id
        long messageId = json.session.message_id
        String sessionId = json.session.session_id
        String text = json.request.original_utterance

        String responseText = dialogService.call(userId, text)

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
