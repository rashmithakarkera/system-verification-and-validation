package allaboutecm.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TrackUnitTest {

    private Track track;

    @BeforeEach
    public void setUp() {
        track = new Track("Malang", 4.8);
    }

    @Test
    @DisplayName("Track name cannot be null")
    public void trackNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setTrackName(null));
    }

    @Test
    @DisplayName("Track length cannot be null")
    public void trackLengthCannotBeNull() {
        assertThrows(NullPointerException.class, () -> track.setTrackLength(null));
    }
    @Test
    @DisplayName("Track is added successfully")
    public void newTrackIsadded() {
        String trackName = track.getTrackName();
        track.setTrackName("My Code Story");
        String trackName1 = track.getTrackName();
        assertNotEquals(trackName, trackName1);

    }
   @Test
   public void successfullygetTrackLength() {
        assertEquals(4.8,track.getTrackLength());
       }

}