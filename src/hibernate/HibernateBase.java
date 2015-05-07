package hibernate;

import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.service.*;

/**
 * Created by chenpeng07 on 2015/5/4.
 */
public abstract class HibernateBase {
    private static final SessionFactory sessionFactory;//会话工厂，用于创建会话

    static {
        try {
            Configuration cfg = new Configuration().configure();
            ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder();
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.applySettings(cfg.getProperties()).buildServiceRegistry();
            sessionFactory = cfg.buildSessionFactory(serviceRegistry);

        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static final ThreadLocal session = new ThreadLocal();

    public static Session currentSession() throws HibernateException {
        Session s = (Session)session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            session.set(s);
        }
        //s.beginTransaction();
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session)session.get();
        if (s != null) {
            s.close();
        }
        session.set(null);
    }
}
