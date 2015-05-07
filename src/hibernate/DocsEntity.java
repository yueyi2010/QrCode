package hibernate;

import javax.persistence.*;

/**
 * Created by chenpeng07 on 2015/5/6.
 */
@Entity
@Table(name = "docs", schema = "", catalog = "qrcode")
public class DocsEntity {
    private String id;
    private String qr;

    @Id
    @Column(name = "id", nullable = false, insertable = true, updatable = true, length = 64)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "qr", nullable = true, insertable = true, updatable = true, length = 16)
    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DocsEntity that = (DocsEntity) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (qr != null ? !qr.equals(that.qr) : that.qr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (qr != null ? qr.hashCode() : 0);
        return result;
    }
}
