<%-- 
    Document   : list
    Created on : Nov 14, 2015, 10:01:04 AM
    Author     : andrey
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel='stylesheet' href='main.css' type='text/css'/>
        <title>View  records</title>
    </head>
    <body>
        <div id="menu">
            <jsp:include page="menu.jsp"/>
        </div>
        <div>
            <h1>View users page</h1>
            ${users_table}
        </div>
    </body>
</html>
