package alice.memory

import java.time.LocalDateTime

class Memory {
    AliceUser user
    String text

    LocalDateTime dateCreated
    LocalDateTime lastUpdated

    static belongsTo = [user:AliceUser]

    static constraints = {
        user nullable: false
        text nullable: false, blank: false
    }
}
