package servlet;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import hibernate.DocsBean;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

/**
 * Created by chenpeng07 on 2015/4/30.
 */
@javax.servlet.annotation.WebServlet(name = "Servlet")
public class GenerateQrsServlet extends javax.servlet.http.HttpServlet {
    private DocsBean db = new DocsBean();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        PrintWriter pw = response.getWriter();
        String num = request.getParameter("num");
        String allow = request.getParameter("allow");
        //String type = request.getParameter("type");
        String type = "0";
        String pathName = request.getParameter("path");

        if (null == num || num.isEmpty() || null == type || type.isEmpty() || null == allow || allow.isEmpty()) {
            pw.println("Can't get num or allow");
            return;
        }

        int numInt = 0;
        int allowInt = 0;
        int typeInt = 0;

        try {
            numInt = Integer.parseInt(num);
            allowInt = Integer.parseInt(allow);
            typeInt = Integer.parseInt(type);
        } catch (Throwable t) {
            t.printStackTrace();
            pw.println("Num or allow is not a number");
            return;
        }

        if (numInt <= 0 || allowInt <= 0 || typeInt < 0 || typeInt > 1) {
            pw.println("Num or allow is valid");
            return;
        }

        if (typeInt == 0) {
            if (null == pathName || pathName.isEmpty()) {
                pw.println("Can't get path");
                return;
            }
            File pathFile = new File(pathName);
            if (!pathFile.isDirectory()) {
                pw.println("Path is not valid : " + pathName);
                return;
            }
        }

        Vector<String> vector = null;

        Session s = db.currentSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            vector = db.generateQrs(s, numInt, allowInt);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return;
        } finally {
            db.closeSession();
        }

        if (0 == typeInt) {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            int index = 0;
            //生成二维码图片
            for (String content : vector) {
                index++;
                String fileName = pathName + "/" + content + ".jpg";
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400);
                    FileOutputStream fStream = new FileOutputStream(new File(fileName));
                    MatrixToImageWriter.writeToStream(bitMatrix, "jpg", fStream);
                    fStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                pw.println(content);
            }
        } else {
            for (String content : vector) {
                pw.println(content);
            }
        }
    }
}
