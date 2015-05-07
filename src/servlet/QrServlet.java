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
public class QrServlet extends javax.servlet.http.HttpServlet {
    private DocsBean db = new DocsBean();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String id = request.getParameter("id");
        String qr = request.getParameter("qr");
        String allow = request.getParameter("allow");
        boolean withQr = true;

        if (null == id){
            pw.println("Can't get id");
            return;
        }

        if (id.isEmpty()) {
            pw.println("Id is empty");
            return;
        }

        if (null == qr){
            withQr = false;
        }

        if (withQr && (qr.isEmpty() || null == allow || allow.isEmpty())) {
            pw.println("Qr is empty, or allow is null or empty");
            return;
        }

        int allowInt = 0;
        if (withQr) {
            try {
                allowInt = Integer.parseInt(allow);
            } catch (Throwable t) {
                pw.println("Allow is not an integer");
                return;
            }
        }

        //没有指定qr时，判断该id是否激活;否则使用id和qr尝试激活
        Session s = db.currentSession();
        Transaction transaction = s.beginTransaction();
        int ret = withQr ? db.idQrActive(s, id, qr, allowInt) : db.isIdActive(s, id);

        try {
            transaction.commit();
        }
        catch (Throwable t){
            ret = withQr ? 0 : 1;
            transaction.rollback();
        }
        pw.print(ret);
        db.closeSession();
    }
}
