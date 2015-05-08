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
        String pathName = request.getParameter("path");

        if (null == num || num.isEmpty()){
            pw.println("Can't get num or num is empty");
            return;
        }

        if (null == pathName || pathName.isEmpty()) {
            pw.println("Can't get path or path is empty");
            return;
        }

        int numInt = 0;
        try {
            numInt = Integer.parseInt(num);
        }
        catch (Throwable t){
            pw.println("num must be int");
            return;
        }

        if (numInt <= 0){
            pw.println("num must be larger than 0");
            return;
        }

        File pathFile = new File(pathName);
        if (!pathFile.isDirectory()) {
            pw.println("path is not valid : " + pathName);
            return;
        }

        Session s = db.currentSession();
        Transaction transaction = s.beginTransaction();
        Vector<String> vector = db.generateQrs(s, numInt);

        boolean success = true;

        try{
            transaction.commit();
        }
        catch(Throwable t){
            t.printStackTrace();
            success = false;
            transaction.rollback();
        }
        //db.closeSession();

        if (!success){
            pw.println("Transaction commit failed, may another job is generating qrcodes. Try later! ");
            return;
        }

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
                pw.println("Generate qrcode "+index+"\tfailed!");
            }

            pw.println("Generate qrcode "+index+"\tsucceed!\tQRCODE:\t"+ content +"\tPATH:\t"+ fileName);
        }
    }
}
