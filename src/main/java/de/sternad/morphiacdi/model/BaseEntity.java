package de.sternad.morphiacdi.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Version;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

public abstract class BaseEntity {

    @Id
    private ObjectId id;

    @Version
    private Long version;

    @Indexed
    private Date lastUpdated;

    @PrePersist
    void prePersist() {
        lastUpdated = Date.from(Instant.now().atOffset(ZoneOffset.UTC)
                .toInstant());
    }

    public ObjectId getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Date getLastUpdated() {
        return new Date(lastUpdated.getTime());
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "id=" + id.toString() +
                ", version=" + version +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
