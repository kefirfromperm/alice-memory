package alice.memory.dao

import alice.memory.AliceUser
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(AliceUser)
abstract class AliceUserDaoService {
    @Transactional
    AliceUser findOrSave(String yandexId) {
        return AliceUser.findOrSaveByYandexId(yandexId)
    }
}