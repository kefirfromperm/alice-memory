package alice.memory

import java.time.LocalDateTime

class AliceUser {
    String yandexId

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static constraints = {
        yandexId nullable: false, blank: false, maxSize: 64, unique: true
    }

    static mapping = {
        id generator: 'sequence', params: [sequence_name: 'seq_alice_user']
    }
}
