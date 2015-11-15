/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.servlet;

import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zholudzeu.csv.dal.DAO;
import com.zholudzeu.csv.dal.User;
import javax.servlet.RequestDispatcher;

/**
 *
 * @author andrey
 */
@WebServlet(name = "ViewRecordsServlet", urlPatterns = {"/ViewRecordsServlet"})
public class ViewRecordsServlet extends HttpServlet {

    private static final int RECORDS_BY_PAGE = 5;
    /**
     * Processes only GET request and generates html table, to insert it 
     * later in the list.jsp.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // Originally, i wanted to make all this work in the list.jsp, but 
    // there was some error, i couldn't load jstl taglib, even with
    // all necessary jars. So, i decided to go this way.
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response, int page, int orderBy)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("list.jsp");
        // Generating th row with all sort-allowing urls
        // 1 - sorting by 1st column Ascended
        // 2 - sorting by 1st column Descended, and so on...
        int[] array = {1, 3, 5, 7, 9, 11};
        array[(orderBy - 1) / 2] = orderBy + (orderBy % 2 == 0 ? -1 : 1);
        String s = "<div> <table> <tr>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + array[0] + "\">Id</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + array[1] + "\">Name</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + array[2] + "\">Surname</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + array[3] + "\">Login</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + array[4] + "\">Email</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + array[5] + "\">Phone number</a></td>";
        
        // Inserting values into the table
        s += "</tr>";
        ArrayList<User> list = DAO.getRecords(page, RECORDS_BY_PAGE, orderBy);
        if (list == null) {
            rd.forward(request, response);
        }
        for(User usr : list) {
            s += "<tr>";
            s += "<td>" + usr.id + "</td><td>" + usr.name + "</td><td>" + 
                    usr.surname + "</td><td>" + usr.login + "</td><td>" + 
                    usr.email + "</td><td>" + usr.phoneNumber;
            s += "</tr>";
        }
        // Create navigator at the bottom of the table
        s += "</table><br/>";
        s += "<div class=\"navigator\"> ";
        s += createNavigator(page, orderBy);
        s += "</div></div>";
        
        // Forward to the jsp and set users_table attribute
        request.setAttribute("users_table", s);
        rd.forward(request, response);
    }
    
    /**
     * 
     * @param page Current page
     * @param orderBy Number, representing the column and asc/desc order
     * @return String, representing navigator html
     */
    protected String createNavigator(int page, int orderBy) {
        String s = "";
        // Get pages count and page ranges to show in nagigator
        int count = DAO.getRecordsCount();
        int pages = ((count - 1) / RECORDS_BY_PAGE) + 1;
        int pos1 = page - 5 < 1 ? 1 : page - 5;
        int pos2 = page + 5 > pages ? pages : page + 5;
        // To the 1st page
        s += "<a href=\"ViewRecordsServlet?page=1" + 
                "&orderBy=" + orderBy + "\"> <<< </a>";
        // 5 or less previous pages
        for (int i = pos1; i < page; i++) {
            s += "<a href=\"ViewRecordsServlet?page=" + i + 
                "&orderBy=" + orderBy + "\"> " + i + " </a>";
        }
        // Current page
        s += " " + page + " ";
        // 5 or less next pages
        for (int i = page + 1; i <= pos2; i++) {
            s += "<a href=\"ViewRecordsServlet?page=" + i + 
                "&orderBy=" + orderBy + "\"> " + i + " </a>";
        }
        // To the last page
        s += "<a href=\"ViewRecordsServlet?page=" + pages + 
                "&orderBy=" + orderBy + "\"> >>> </a>";
        return s;
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
        String s = request.getParameter("page");
        int page = s == null ? 1 : Integer.parseInt(s);
        s = request.getParameter("orderBy");
        int orderBy = s == null ? 1 : Integer.parseInt(s);
        processRequest(request, response, page, orderBy);
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
        processRequest(request, response, 1, 1);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Creates html table to show some records, and forwards to list.jsp";
    }// </editor-fold>

}
