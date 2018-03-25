package alice.memory

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class DialogResponse {
    String text
    List<Button> buttons
}
