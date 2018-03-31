package alice.memory

import groovy.transform.CompileStatic

@CompileStatic
enum CommandType {
    REMEMBER(['запомнить','запомни']),
    REMIND(['напомнить', 'напомни']),
    FORGET,
    NONE,
    PING(['ping'])

    final Iterable<String> phrases

    CommandType(){
        this.phrases = Collections.emptyList()
    }

    CommandType(List<String> phrases) {
        this.phrases = Collections.unmodifiableList(phrases)
    }
}