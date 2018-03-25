package alice.memory

import groovy.transform.CompileStatic

@CompileStatic
enum CommandType {
    REMEMBER(['запомнить','запомни']),
    REMIND(['напомнить', 'напомни'])

    final Iterable<String> phrases

    CommandType(List<String> phrases) {
        this.phrases = Collections.unmodifiableList(phrases)
    }
}