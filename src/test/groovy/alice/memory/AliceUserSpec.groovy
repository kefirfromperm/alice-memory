package alice.memory

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class AliceUserSpec extends Specification implements DomainUnitTest<AliceUser> {

    def setup() {
    }

    def cleanup() {
    }

    void "test correct user"() {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: '1234')
        expect: "fix me"
            user.validate()
    }

    @Unroll
    void 'test bad user with id #yandexId'(String yandexId, String code) {
        given: 'a user'
            AliceUser user = new AliceUser(yandexId: yandexId)
        when: "fix me"
            boolean valid = user.validate()
        then: 'not valid'
            !valid
            user.hasErrors()
            user.errors.getFieldError('yandexId').code == code
        where:
            yandexId | code
            null     | 'nullable'
            ''       | 'nullable'
            ' '      | 'nullable'
    }
}
