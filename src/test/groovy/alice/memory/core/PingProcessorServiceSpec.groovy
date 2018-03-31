package alice.memory.core

import alice.memory.DialogCommand
import alice.memory.DialogResponse
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PingProcessorServiceSpec extends Specification implements ServiceUnitTest<PingProcessorService>{

    def setup() {
    }

    def cleanup() {
    }

    void 'test ping'() {
        when: 'call without command'
            DialogResponse response = service.process(new DialogCommand())
        then:
            response.text == 'pong'
    }
}
