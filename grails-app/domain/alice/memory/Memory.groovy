package alice.memory

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

import java.time.LocalDateTime

@ToString(includeNames = true, includePackage = false)
@EqualsAndHashCode
class Memory {
    AliceUser user
    String text
    boolean active = true

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static belongsTo = [user: AliceUser]

    static constraints = {
        user nullable: false
        text nullable: false, blank: false, maxSize: 1024
    }

    static mapping = {
        id generator: 'sequence', params: [sequence_name: 'seq_memory']
    }
}
