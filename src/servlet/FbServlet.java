package servlet;

import hibernate.DocsBean;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by chenpeng07 on 2015/5/6.
 */
@WebServlet(name = "FbServlet")
public class FbServlet extends HttpServlet {
    private DocsBean db = new DocsBean();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String id = request.getParameter("id");
        String fb = request.getParameter("fb");        
        boolean withFb = true;

        if (null == id){
            pw.println("Can't get id");
            return;
        }

        if (id.isEmpty()) {
            pw.println("Id is empty");
            return;
        }

        if (null == fb){
            withFb = false;
        }

        if (withFb && fb.isEmpty()) {
            pw.println("fb is empty");
            return;
        }

        Session s = db.currentSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            List<String> list = null;

            //没有指定fb时，获取用户反馈;否则记录反馈
            if (withFb) {
                db.setFb(s, id, fb);
            } else {
                list = db.getFb(s, id);
            }
            tx.commit();
            if (withFb) {
                pw.println("true");
            } else {
                for (String i : list){
                    pw.println(i);
                }
            }
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            if (withFb) {
                pw.println("false");
            }
            return;
        } finally {
            db.closeSession();
        }
    }
}
