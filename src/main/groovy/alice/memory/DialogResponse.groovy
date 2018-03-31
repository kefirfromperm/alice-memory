package alice.memory

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
@EqualsAndHashCode
@CompileStatic
class DialogResponse {
    String text
    String tts
    List<Button> buttons
}
