package alice.memory

import java.time.LocalDateTime

class User {
    String yandexId

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static constraints = {
        yandexId nullable: false, blank: false
    }
}
