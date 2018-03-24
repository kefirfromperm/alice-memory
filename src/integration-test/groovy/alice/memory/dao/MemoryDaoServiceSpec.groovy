package alice.memory.dao

import alice.memory.User
import grails.testing.mixin.integration.Integration
import grails.transaction.Rollback
import spock.lang.Specification

@Integration
@Rollback
class MemoryDaoServiceSpec extends Specification{
    private static final String TEXT = 'Test text'

    MemoryDaoService memoryDaoService

    def setup() {
    }

    def cleanup() {
    }

    void "test save and find"() {
        given:'a user'
            User user = new User(yandexId: '1234').save()
        when:'create a memory'
            memoryDaoService.saveMemory(user, TEXT)
        then:'find by user give the text'
            memoryDaoService.findByUser(user).text == TEXT
    }
}
