package alice.memory

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString(includeNames = true, includePackage = false)
@EqualsAndHashCode
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
