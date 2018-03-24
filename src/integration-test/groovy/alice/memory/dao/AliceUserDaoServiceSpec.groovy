package alice.memory.dao

import alice.memory.AliceUser
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import org.hibernate.SessionFactory
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

@Integration
@Rollback
class AliceUserDaoServiceSpec extends Specification {
    public static final String USER_YANDEX_ID = '1234'

    AliceUserDaoService aliceUserDaoService

    @Autowired
    SessionFactory sessionFactory

    void 'test create a user'() {
        when: 'create a user with an id'
            aliceUserDaoService.findOrSave(USER_YANDEX_ID)
            sessionFactory.currentSession.flush()
        then: 'there is one user'
            AliceUser.count() == 1
        when: 'create a user with the id again'
            aliceUserDaoService.findOrSave(USER_YANDEX_ID)
            sessionFactory.currentSession.flush()
        then: 'there is no new user'
            AliceUser.count() == 1
    }
}
