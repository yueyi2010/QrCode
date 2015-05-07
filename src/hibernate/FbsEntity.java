package hibernate;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by chenpeng07 on 2015/5/6.
 */
@Entity
@Table(name = "fbs", schema = "", catalog = "qrcode")
public class FbsEntity {
    private int fbid;
    private String id;
    private String fb;
    private Timestamp timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fbid", nullable = false, insertable = true, updatable = true)
    public int getFbid() {
        return fbid;
    }

    public void setFbid(int fbid) {
        this.fbid = fbid;
    }

    @Basic
    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "fb", nullable = false, insertable = true, updatable = true, length = 65535)
    public String getFb() {
        return fb;
    }

    public void setFb(String fb) {
        this.fb = fb;
    }

    @Basic
    @Column(name = "timestamp", nullable = false, insertable = true, updatable = true)
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FbsEntity fbsEntity = (FbsEntity) o;

        if (fbid != fbsEntity.fbid) return false;
        if (fb != null ? !fb.equals(fbsEntity.fb) : fbsEntity.fb != null) return false;
        if (id != null ? !id.equals(fbsEntity.id) : fbsEntity.id != null) return false;
        if (timestamp != null ? !timestamp.equals(fbsEntity.timestamp) : fbsEntity.timestamp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fbid;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (fb != null ? fb.hashCode() : 0);
        result = 31 * result + (timestamp != null ? timestamp.hashCode() : 0);
        return result;
    }
}
