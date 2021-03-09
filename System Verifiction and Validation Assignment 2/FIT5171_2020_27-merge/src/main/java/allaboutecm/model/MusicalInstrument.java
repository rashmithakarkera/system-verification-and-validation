package allaboutecm.model;

import org.neo4j.ogm.annotation.Property;

import java.util.Objects;

public class MusicalInstrument extends Entity {

    @Property(name="instrumentName")
    private String name;

    public MusicalInstrument() {
    }

    public MusicalInstrument(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicalInstrument that = (MusicalInstrument) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
