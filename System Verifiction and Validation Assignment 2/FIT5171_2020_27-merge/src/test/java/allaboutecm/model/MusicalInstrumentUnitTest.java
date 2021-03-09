package allaboutecm.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class MusicalInstrumentUnitTest {
    @Test
    public void musicalInstrumentComparisonWithNull() {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");
        assertFalse(musicalInstrument.equals(null));
    }
    @Test
    public void musicalInstrumentComparisonWithObject() {
        MusicalInstrument musicalInstrument = new MusicalInstrument("Piano");
        assertFalse(musicalInstrument.equals("object"));
    }
}