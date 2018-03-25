package alice.memory

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Button {
    String title
    Map payload
    boolean hide
}
