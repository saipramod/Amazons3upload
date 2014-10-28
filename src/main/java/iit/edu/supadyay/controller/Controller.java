/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package iit.edu.supadyay.controller;

import iit.edu.supadyay.s3.S3upload;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author supramo
 */
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
                 maxFileSize=1024*1024*10,      // 10MB
                 maxRequestSize=1024*1024*50)   // 50MB
public class Controller extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(Controller.class.getName());
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Controller</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Controller at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        switch (request.getServletPath()) {
            case "/":
                List messages = new ArrayList<String>();
                List bucketNames = new ArrayList();
                try {
                    bucketNames = S3upload.listOfBuckets();
                }
                catch (Exception e){
                    messages.add("Invalid Credentials");
                    request.setAttribute("messages", messages);
                    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                    break;
                }
                
                //LOG.log(Level.WARNING, "isa ia" + bucketNames.toString());
                request.setAttribute("listofbuckets", bucketNames);
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                
                break;
            case "/bucket":
                List objectNames = new ArrayList();
                String bucketname = request.getParameter("name");
                System.out.println("bucketname is :" + bucketname);
                objectNames = S3upload.listOfObjects(bucketname);
                //objectNames.add("you click on me");
                request.setAttribute("listofobjects", objectNames);
                
                session.setAttribute("bucketchosen", bucketname);
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                break;
            case "/object":
                if (session.getAttribute("bucketchosen") == null){
                    request.getRequestDispatcher("/").forward(request, response);
                    break;
                }
                String objectname = request.getParameter("name");
                String cobucketname = session.getAttribute("bucketchosen").toString();
                System.out.println(cobucketname);
                String url = "https://"+cobucketname+".s3.amazonaws.com/"+objectname;
                //String url = "https://s3.amazonaws.com/"+cobucketname+"/"+objectname;
                System.out.println(url);
                response.getWriter().println("<a href='/'>Go Back</a>");
                System.out.println(objectname);
                String ext = getFileExtension(objectname);
                System.out.println(ext);
                if (ext.toLowerCase(Locale.ENGLISH).equals(".png") || ext.toLowerCase(Locale.ENGLISH).equals(".jpg") || ext.toLowerCase(Locale.ENGLISH).equals(".jpeg")){
                    response.getWriter().println("<br><img src='"+url +"'>");
                    break;
                }
                else{
                    response.getWriter().println("<br>This is not an image and you cannot view it");
                    break;
                }
                
                
            default:
                response.getWriter().println("<a href='"+request.getContextPath() +"'>Page not found</a>");
                break;
        }
    }
    
    private static String getFilename(Part part) {
    for (String cd : part.getHeader("content-disposition").split(";")) {
        if (cd.trim().startsWith("filename")) {
            String filename = cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            return filename.substring(filename.lastIndexOf('/') + 1).substring(filename.lastIndexOf('\\') + 1); // MSIE fix.
        }
    }
    return null;
    }

    private static String extractFileName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");
    for (String s : items) {
        if (s.trim().startsWith("filename")) {
            return s.substring(s.indexOf("=") + 2, s.length()-1);
        }
    }
    return "";
}
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch (request.getServletPath()) {
            case "/upload":
                List messages = new ArrayList<String>();
                String filePath = "";
                String bucketName = request.getParameter("bucket");
                Part filePart = request.getPart("uploadFile");
                String filename = Controller.getFilename(filePart);
                InputStream filecontent = filePart.getInputStream();
                List bucketNames = new ArrayList();
                //bucketNames = S3upload.listOfBuckets();
                //LOG.log(Level.WARNING, "isa ia" + bucketNames.toString());
                try {
                    bucketNames = S3upload.listOfBuckets();
                }
                catch (Exception e){
                    messages.add("Invalid Credentials");
                    request.setAttribute("messages", messages);
                    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                    break;
                }
                request.setAttribute("listofbuckets", bucketNames);        
                LOG.log(Level.WARNING,"file name is :" + filename);
                if (bucketName.trim().isEmpty()){
                    messages.add("bucketname cannot be empty");
                }
                if (filename.trim().isEmpty()){
                    //LOG.log(Level.WARNING ,"setting firstname message");
                    messages.add("please upload a file");
                }
                request.setAttribute("messages", messages);
                if ((messages.size() > 0) ){
                    request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                    break;
                }
                
                try {
                    File file = new File(filename);
                    if (!file.exists()) {
                            file.createNewFile();
                    }
                    String ext = getFileExtension(filename);
                    if (ext.toLowerCase(Locale.ENGLISH).equals(".png") || ext.toLowerCase(Locale.ENGLISH).equals(".jpg") || ext.toLowerCase(Locale.ENGLISH).equals(".jpeg")){
                        BufferedImage imBuff = ImageIO.read(filecontent);
                        //File outputfile = new File(filename);                    
                        ImageIO.write(imBuff, "png", file);
                    }
                    else{
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
                        int content;
                        while ((content = filecontent.read()) != -1) {
                            bw.write(content);
                        }
                    
                        bw.close();
                        
                    }
                    
                    /*for (Part part : request.getParts()) {
                       //System.out.println(part.getSize());
                        //System.out.println(part.getContentType());
                        //System.out.println(part.getName());
                        part.write(filename);
                    }*/
                    //FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    filePath = file.getAbsoluteFile().getAbsolutePath();
                    LOG.log(Level.WARNING,file.getAbsolutePath());

                    if (S3upload.upload(bucketName, filePath, filename)){
                        messages.add("FILE Successfully Uploaded");
                        request.setAttribute("messages", messages);
                        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                        break;
                    }
                    else{
                        messages.add("File not uploaded, please try again");
                        request.setAttribute("messages", messages);
                        request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                        break;
                    }
                        
                } 
                catch (Exception ex) {
                   //Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex.getMessage());
                    messages.add("File not uploaded, please try again");
                   request.setAttribute("messages", messages);
                   request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                   break;
                }
        }
    }
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
