package hibernate;

import org.hibernate.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import hibernate.DocsEntity;

/**
 * Created by chenpeng07 on 2015/5/4.
 */
public class DocsBean extends HibernateBase {
    public DocsBean () throws HibernateException{
        super();
    }

    /**
     * 查询系统中所有的用户ID。
     */
    public List getAllIds(Session s)throws HibernateException
    {
        String queryString = "select distinct docs.id from DocsEntity as docs";
        Query query = s.createQuery(queryString);
        return query.list();
    }

    /**
     * 查询用户ID是否激活。
     */
    public int isIdActive(Session s, String id)throws HibernateException
    {
        String queryString = "select id from DocsEntity as docs where id = '" + id + "'";
        Query query = s.createQuery(queryString);

        //0表示已激活，1表示未激活
        int ret = query.list().isEmpty() ? 1 : 0;
        return ret;
    }

    /**
     * 根据用户ID和二维码激活用户。
     */
    public int idQrActive(Session s, String id, String qr, int allow)throws HibernateException
    {
        if (0 == isIdActive(s, id)){
            //该用户已经激活
            return 3;
        }

        String queryString = "select id from QrsEntity where qr = '" + qr +"'";
        if (s.createQuery(queryString).list().isEmpty()){
            //激活码无效，失败
            return 0;
        }

        String queryString2 = "select id from DocsEntity where qr = '" + qr +"'";
        List list = s.createQuery(queryString2).list();

        if (list.size() >= allow){
            //激活次数达到上限
            return 1;
        }
        else{
            //成功激活
            DocsEntity docsEntity = new DocsEntity();
            docsEntity.setId(id);
            docsEntity.setQr(qr);
            s.save(docsEntity);
            return 2;
        }
    }

    /**
     * 查询用户ID的反馈。
     */
    public List getFb(Session s, String id)throws HibernateException
    {
        String queryString = "select fb from FbsEntity where id = '" + id +"'";
        Query query = s.createQuery(queryString);
        return query.list();
    }

    /**
     * 记录用户ID的反馈。
     */
    public void setFb(Session s, String id, String fb)throws HibernateException
    {
        FbsEntity fbsEntity = new FbsEntity();
        fbsEntity.setId(id);
        fbsEntity.setFb(fb);
        fbsEntity.setTimestamp(new Timestamp(System.currentTimeMillis()));
        s.save(fbsEntity);
    }

    /**
     * 批量生成二维码。
     */
    public Vector<String> generateQrs(Session s,int num)throws HibernateException
    {
        Vector<String> vector = new Vector<String>(num);
        String queryString = "select qr from QrsEntity";
        int sigh1 = s.createQuery(queryString).list().size();
        long sigh2 = 0;
        long sigh = 0;

        Random random = new Random();

        for (int i = 0; i < num; i++) {
            QrsEntity qrsEntity = new QrsEntity();
            sigh1++;
            sigh2 = Math.abs(random.nextInt());
            sigh = sigh1;
            sigh = sigh << 32;
            sigh |= sigh2;
            String string = String.format("%016X", sigh);
            qrsEntity.setQr(string);
            s.save(qrsEntity);
            vector.add(string);
        }

        return vector;
    }
}
