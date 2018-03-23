package alice.memory

import java.time.LocalDateTime

class Memory {
    User user
    String text

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static belongsTo = [user:User]

    static constraints = {
        text nullable: false, blank: false
    }
}
