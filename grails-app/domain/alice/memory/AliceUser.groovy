package alice.memory

import java.time.LocalDateTime

class AliceUser {
    String yandexId

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static constraints = {
        yandexId nullable: false, blank: false
    }
}
