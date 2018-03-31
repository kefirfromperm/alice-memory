package alice.memory

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includePackage = false)
@EqualsAndHashCode
@CompileStatic
class ResponseModel {
    String text
    String tts
    List<Button> buttons = null
    long messageId
    String userId
    String sessionId
}
