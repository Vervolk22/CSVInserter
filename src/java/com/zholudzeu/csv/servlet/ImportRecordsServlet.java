/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.servlet;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;

import com.zholudzeu.csv.dal.DAO;

/**
 *
 * @author andrey
 */
public class ImportRecordsServlet extends HttpServlet {
    private final String UPLOAD_DIRECTORY = 
            System.getProperty("user.dir") + "/uploads";
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
        if (!ServletFileUpload.isMultipartContent(request)) { 
            processError(request, response);
            return;
        }
        
        try {
            List<FileItem> multiparts = new ServletFileUpload(
                new DiskFileItemFactory()).parseRequest(request);
            for(FileItem item : multiparts){
                String st = item.getName();
                if(!item.isFormField() && !item.getName().equals("")) {
                    File file = new File(UPLOAD_DIRECTORY + File.separator + Math.random());
                    if (file.exists()) {
                        throw new IOException("Abnormal situation with filenames");
                    }
                    try {
                        file.getParentFile().mkdirs(); 
                        file.createNewFile();
                        item.write(file);
                    
                        DAO.loadCsvFile(file);
                    }
                    finally {
                        file.delete();
                    }
                } 
                else { 
                    throw new FileUploadException("File not received");
                }
            }
            response.sendRedirect("ViewRecordsServlet");
        } catch (Exception e) {
            System.err.print(e.getMessage());
            processError(request, response);
        }
    }
    
    private void processError(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<h3 style=\"color:red\">");
            out.println("Something went wrong. Please try again");
            out.println("</h3>");
            
            RequestDispatcher rd = request.getRequestDispatcher("import.jsp");
            rd.include(request, response);
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
        response.sendRedirect("import.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
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
