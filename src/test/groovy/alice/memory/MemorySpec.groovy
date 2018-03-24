package alice.memory

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class MemorySpec extends Specification implements DomainUnitTest<Memory> {

    def setup() {
    }

    def cleanup() {
    }

    void "test correct memory validation"() {
        given: 'a correct memory'
            User user = new User(yandexId: '1234')
            Memory memory = new Memory(user: user, text: 'test text')
        when: 'validate'
            boolean valid = memory.validate()
        then: 'it\'s valid'
            valid
    }

    void 'test memory with null user'() {
        given: 'a memory without user'
            Memory memory = new Memory(text: 'test text')
        when: 'validate'
            boolean valid = memory.validate()
        then: 'it\'s valid'
            !valid
            memory.hasErrors()
            memory.errors.getFieldError('user').code == 'nullable'
    }

    @Unroll
    void 'test memory with text \"#text\"'(String text, String code) {
        given:
            "a memory with text $text"
            User user = new User(yandexId: '1234')
            Memory memory = new Memory(user: user, text: text)
        when: 'validate'
            boolean valid = memory.validate()
        then: 'it\'s valid'
            !valid
            memory.hasErrors()
            memory.errors.getFieldError('text').code == code
        where:
            text | code
            null | 'nullable'
            ''   | 'nullable'
            ' '  | 'nullable'
    }
}
