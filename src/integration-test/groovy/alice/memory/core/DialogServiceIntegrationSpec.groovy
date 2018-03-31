package alice.memory.core

import alice.memory.DialogResponse
import alice.memory.RawCommand
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
class DialogServiceIntegrationSpec extends Specification {
    DialogService dialogService

    void 'test call without command'() {
        when: 'call without command'
            DialogResponse response = dialogService.call(new RawCommand(userId: '1234', text: 'test'))
        then:
            response.text == 'Я могу запомнить и напомнить.'
            response.tts == 'Я могу запомнить - и напомнить.'
    }
}
