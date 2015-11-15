/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zholudzeu.csv.servlet;

import java.util.ArrayList;
import java.io.IOException;
import java.io.PrintWriter;
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
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, 
            HttpServletResponse response, int page, int orderBy)
            throws ServletException, IOException {
        String s = "<div> <table> <tr>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + (orderBy % 2 + 1) + "\">Id</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + (orderBy % 2 + 3) + "\">Name</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + (orderBy % 2 + 5) + "\">Surname</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + (orderBy % 2 + 7) + "\">Login</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + (orderBy % 2 + 9) + "\">Email</a></td>";
        s += "<td><a href=\"ViewRecordsServlet?page=" + page + 
                "&orderBy=" + (orderBy % 2 + 11) + "\">Phone number</a></td>";
        
        s += "</tr>";
        ArrayList<User> list = DAO.getRecords(page, RECORDS_BY_PAGE, orderBy);
        for(User usr : list) {
            s += "<tr>";
            s += "<td>" + usr.id + "</td><td>" + usr.name + "</td><td>" + 
                    usr.surname + "</td><td>" + usr.login + "</td><td>" + 
                    usr.email + "</td><td>" + usr.phoneNumber;
            s += "</tr>";
        }
        s += "</table><br/>";
        s += "<div class=\"navigator\"> ";
        s += createNavigator(page, orderBy);
        s += "</div></div>";
            
        request.setAttribute("users_table", s);
        RequestDispatcher rd = request.getRequestDispatcher("list.jsp");
        rd.forward(request, response);
    }
    
    protected String createNavigator(int page, int orderBy) {
        String s = "";
        int count = DAO.getRecordsCount();
        int pages = ((count - 1) / RECORDS_BY_PAGE) + 1;
        int pos1 = page - 5 < 1 ? 1 : page - 5;
        int pos2 = page + 5 > pages ? pages : page + 5;
        for (int i = pos1; i < page; i++) {
            s += "<a href=\"ViewRecordsServlet?page=" + i + 
                "&orderBy=" + orderBy + "\"> " + i + " </a>";
        }
        s += " " + page + " ";
        for (int i = page + 1; i <= pos2; i++) {
            s += "<a href=\"ViewRecordsServlet?page=" + i + 
                "&orderBy=" + orderBy + "\"> " + i + " </a>";
        }
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
        return "Short description";
    }// </editor-fold>

}
