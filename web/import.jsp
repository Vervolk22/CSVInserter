<%-- 
    Document   : index
    Created on : Nov 13, 2015, 7:19:14 PM
    Author     : andrey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' href='main.css' type='text/css'/>
        <title>Import records</title>
    </head>
    <body>
        <div id="menu">
            <jsp:include page="menu.jsp"/>
        </div>
        <div>
            ${errorMessage}
            <h1>Import CSV file page</h1>
            <div>
                <h3>Please, chose the csv file</h3>
                <form action="ImportRecordsServlet" method="POST" 
                      enctype="multipart/form-data">
                    <input type="file" name="file" id="file"/><p/>
                    <input type="submit" value="Import" />
                </form>
            </div>
        </div>
    </body>
</html>
