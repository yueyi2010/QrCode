package hibernate;

import javax.persistence.*;

/**
 * Created by chenpeng07 on 2015/5/9.
 */
@Entity
@Table(name = "qrs", schema = "", catalog = "qrcode")
public class QrsEntity {
    private String qr;
    private int allow;

    @Id
    @Column(name = "qr", nullable = false, insertable = true, updatable = true, length = 16)
    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }

    @Basic
    @Column(name = "allow", nullable = false, insertable = true, updatable = true)
    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QrsEntity qrsEntity = (QrsEntity) o;

        if (allow != qrsEntity.allow) return false;
        if (qr != null ? !qr.equals(qrsEntity.qr) : qrsEntity.qr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = qr != null ? qr.hashCode() : 0;
        result = 31 * result + allow;
        return result;
    }
}
