package hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by chenpeng07 on 2015/5/6.
 */
@Entity
@Table(name = "qrs", schema = "", catalog = "qrcode")
public class QrsEntity {
    private String qr;

    @Id
    @Column(name = "qr", nullable = false, insertable = true, updatable = true, length = 16)
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

        QrsEntity qrsEntity = (QrsEntity) o;

        if (qr != null ? !qr.equals(qrsEntity.qr) : qrsEntity.qr != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return qr != null ? qr.hashCode() : 0;
    }
}
