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

        //没有指定fb时，获取用户反馈;否则记录反馈
        Session s = db.currentSession();
        Transaction transaction = s.beginTransaction();

        boolean success = true;
        if (withFb){
            db.setFb(s, id, fb);

            try{
                transaction.commit();
            }
            catch (Throwable t){
                success = false;
                transaction.rollback();
            }
            pw.println(success ? "true" : "false");
        }
        else{
            List<String> list = db.getFb(s, id);
            try{
                transaction.commit();
            }
            catch (Throwable t){
                success = false;
                transaction.rollback();
            }
            if (success){
                for (String i : list){
                    pw.println(i);
                }
            }
        }
        db.closeSession();
    }
}
