package allaboutecm.dataaccess.neo4j;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

class URLConvertorUnitTest {

    @Test
    public void whenURLIsNull() {
        URLConverter urlConverter = new URLConverter();
        assertNull( urlConverter.toEntityAttribute(null));
    }
    @Test
    public void whenAssignedProperURL() {
        URLConverter urlConverter = new URLConverter();
        URL url =  urlConverter.toEntityAttribute("https://www.ecmrecords.com/artists/1435045745/keith-jarrett");
        assertEquals(url.getAuthority(),"www.ecmrecords.com");
    }

    @Test
    public void throwExceptionWhenInvalidURLStringIsPassed()  throws IllegalArgumentException {
        URLConverter urlConverter = new URLConverter();
        assertThrows(IllegalArgumentException.class, () -> urlConverter.toEntityAttribute("addf"));

    }
}