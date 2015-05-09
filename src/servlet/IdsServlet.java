package servlet;

import hibernate.DocsBean;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by chenpeng07 on 2015/4/30.
 */
@javax.servlet.annotation.WebServlet(name = "Servlet")
public class IdsServlet extends javax.servlet.http.HttpServlet {
    private DocsBean db = new DocsBean();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter pw = response.getWriter();
        Session s = db.currentSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            List<String> list = db.getAllIds(s);
            tx.commit();
            for (String i : list) {
                pw.println(i);
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return;
        } finally {
            db.closeSession();
        }
    }
}
