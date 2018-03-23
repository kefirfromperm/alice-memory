package alice.memory

import grails.testing.gorm.DomainUnitTest
import org.junit.Ignore
import spock.lang.Specification

@Ignore
class MemorySpec extends Specification implements DomainUnitTest<Memory> {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
        expect:"fix me"
            true == false
    }
}
