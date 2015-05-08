package hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * Created by chenpeng07 on 2015/5/4.
 */
public abstract class HibernateBase {
    private static final SessionFactory sessionFactory;//会话工厂，用于创建会话
    private static final ThreadLocal session = new ThreadLocal();

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

    public static Session currentSession() throws HibernateException {
        Session s = (Session)session.get();
        if (s == null || !s.isOpen() || !s.isConnected()) {
            if (s != null && s.isOpen()) {
                s.close();
            }
            s = sessionFactory.openSession();
            session.set(s);
        }
        //s.beginTransaction();
        return s;
    }

    public static void closeSession() throws HibernateException {
        Session s = (Session)session.get();
        if (s != null && s.isOpen()) {
            s.close();
        }
        session.set(null);
    }
}
