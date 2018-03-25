package alice.memory

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
@CompileStatic
class RawCommand {
    String yandexId
    String text
    Map payload
    boolean buttonPressed
}
