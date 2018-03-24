package alice.memory.dao

import alice.memory.User
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@Integration
@Rollback
class UserDaoServiceSpec extends Specification {
    public static final String USER_YANDEX_ID = '1234'
    UserDaoService userDaoService

    void 'test create a user'() {
        when: 'create a user with an id'
            userDaoService.findOrSave(USER_YANDEX_ID)
        then: 'there is one user'
            User.count() == 1
        when: 'create a user with the id again'
            userDaoService.findOrSave(USER_YANDEX_ID)
        then: 'there is no new user'
            User.count() == 1
    }
}
