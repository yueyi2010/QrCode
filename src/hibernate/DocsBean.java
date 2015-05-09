package hibernate;

import org.hibernate.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by chenpeng07 on 2015/5/4.
 */
public class DocsBean extends HibernateBase {
    private static long current_qrs = 0;

    static {
        Session s = currentSession();
        Transaction transaction = s.beginTransaction();
        String queryString = "select count(*) from QrsEntity";
        Query query = s.createQuery(queryString);
        current_qrs = (Long) query.uniqueResult();
        transaction.commit();
        System.out.println("current_qrs:" + current_qrs);
        //closeSession();
    }

    public DocsBean () throws HibernateException{
        super();
    }

    private static synchronized long getAndModifyCurrentQrs(int num) {
        long old = current_qrs;
        current_qrs += num;
        return old;
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
    public int idQrActive(Session s, String id, String qr) throws HibernateException
    {
        if (0 == isIdActive(s, id)){
            //该用户已经激活
            return 3;
        }

        String queryString = "select allow from QrsEntity qrs where qr = '" + qr + "'";
        Query query = s.createQuery(queryString);
        List<Integer> list = query.list();

        if (0 == list.size()) {
            //激活码无效，失败
            return 0;
        }

        int allow = list.iterator().next();

        String queryString2 = "from DocsEntity as docs where docs.qr = '" + qr + "'";
        Query query2 = s.createQuery(queryString2);
        query2.setLockMode("docs", LockMode.PESSIMISTIC_WRITE);
        long used_num2 = query2.list().size();

        /*String queryString2 = "select count(*) as num from docs where qr = '" + qr + "'";
        Query query2 = s.createSQLQuery(queryString2).addScalar("num", LongType.INSTANCE);
        long used_num2 = (Long)query2.uniqueResult();*/

        System.out.println("ok");

        if (used_num2 >= allow) {
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
    public Vector<String> generateQrs(Session s, int num, int allow) throws HibernateException
    {
        Vector<String> vector = new Vector<String>(num);
        long sigh1 = getAndModifyCurrentQrs(num);
        System.out.println(sigh1);
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
            qrsEntity.setAllow(allow);
            s.save(qrsEntity);
            vector.add(string);
        }

        return vector;
    }
}
