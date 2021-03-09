package allaboutecm.model;

import static org.apache.commons.lang3.Validate.notNull;


/**
 * Represents a track released in an album by ECM records.
 */
public class Track extends Entity {
    private String trackName;
    private Double trackLength;

    /**
     * Parameterised Constructor for Track Class
     *
     * @param trackName
     * @param trackLength
     *
     */
    public Track(String trackName, Double trackLength) {
        notNull(trackName);
        notNull(trackLength);

        this.trackName = trackName;

        setTrackLength(trackLength);
    }

    /**
     * Accessor Method for trackLength
     *
     * @return trackLength
     */
    public Double getTrackLength() {
        return trackLength;
    }

    /**
     * Mutator Method for trackLength
     *
     * @param trackLength
     */
    public void setTrackLength(Double trackLength) {
        notNull(trackLength);
        this.trackLength = trackLength;
    }

    /**
     * Accessor Method for trackName
     *
     * @return trackName
     */
    public String getTrackName() {
        return trackName;
    }

    /**
     * Mutator Method for trackName
     *
     * @param trackName
     */
    public void setTrackName(String trackName) {
        notNull(trackName);
        this.trackName = trackName;
    }
}
