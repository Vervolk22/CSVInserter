/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.servlet;

import java.io.File;
import java.util.List;
import java.io.IOException;
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
     * Processes request to import csv file, if request was POST type
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // reject if content is not multipart
        if (!ServletFileUpload.isMultipartContent(request)) { 
            processError(request, response);
            return;
        }
        
        try {
            // receive uploaded files
            List<FileItem> multiparts = new ServletFileUpload(
                new DiskFileItemFactory()).parseRequest(request);
            for(FileItem item : multiparts){
                String st = item.getName();
                // creating file with random name and saving it in uploads
                // directory. This way was chosen, because file can hold
                // billions of records, according to the task
                if(!item.isFormField() && !item.getName().equals("")) {
                    File file = new File(UPLOAD_DIRECTORY + File.separator + Math.random());
                    if (file.exists()) {
                        throw new IOException("Abnormal situation with filenames");
                    }
                    try {
                        file.getParentFile().mkdirs(); 
                        file.createNewFile();
                        item.write(file);
                        
                        // Importing saved file
                        DAO.loadCsvFile(file);
                    }
                    finally {
                        // And deleting file after import copmletion
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
    
    /**
     * Prints little error message and forwards to the import page
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException IOException if an I/O error occurs
     */
    private void processError(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException{
        request.setAttribute("error_message", 
                "Something went wrong. Please, try again");
        RequestDispatcher rd = request.getRequestDispatcher("import.jsp");
        rd.forward(request, response);
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
        return "Receives POST query to process csv file";
    }// </editor-fold>

}
