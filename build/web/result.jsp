<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% if (request.getAttribute("doctype")==null) { %>
<%  %>
<% } else { %>
<%= request.getAttribute("doctype") %>
<% } %>

<html>
    <head>
        <title>NBA Player Information Dashboard</title>
        <link rel="stylesheet" href="https://unpkg.com/sakura.css/css/sakura.css" type="text/css">
    </head>
    <body>
        <img src="http://www.stickpng.com/assets/images/58428defa6515b1e0ad75ab4.png">
        
        <h4>The most interested age by users:</h4>
        <h4><%= request.getAttribute("mostSearchAge")%></h4><br>
        
        <h4>Total usage of this application till today:</h4>
        <h4><%= request.getAttribute("mcSize")%></h4><br>
        
        <h4>Server average response time:</h4>
        <h4><%= request.getAttribute("avgrspTime")%> seconds.</h4><br>
        
        <h4>Full logs:</h4>
        <h4><%= request.getAttribute("fullLog")%></h4><br>
    </body>
</html>

