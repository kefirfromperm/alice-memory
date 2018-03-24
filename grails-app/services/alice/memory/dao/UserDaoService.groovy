package alice.memory.dao

import alice.memory.User
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(User)
abstract class UserDaoService {
    @Transactional
    User findOrSave(String yandexId) {
        return User.findOrSaveByYandexId(yandexId)
    }
}