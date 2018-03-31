package alice.memory.core

import alice.memory.DialogCommand
import alice.memory.DialogResponse
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class NoneProcessorServiceSpec extends Specification implements ServiceUnitTest<NoneProcessorService>{

    def setup() {
    }

    def cleanup() {
    }

    void 'test call without command'() {
        when: 'call without command'
            DialogResponse response = service.process(new DialogCommand())
        then:
            response.text == 'Я могу запомнить и напомнить.'
            response.tts == 'Я могу запомнить - и напомнить.'
    }
}
