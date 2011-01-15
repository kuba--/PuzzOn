<!--                                                      -->
<!-- Copyright (c) 2010 PuzzOn.net                        -->
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
    <head>
        <%@ include file="/head.jsp" %>
        <link rel="stylesheet" type="text/css" href="/content.css">        
    </head>
    <body>

        <center>

            <div id="root"">
                <%@ include file="/menu.jsp.shtml" %>                                
                <div class="widget">
                    <div class="header" ></div>
                    <%=resources.getSourceContent("/about/about.shtml")%>
                    <div class="footer" ></div>
                </div>                
                
                <div class="widget">
                    <div class="header" ></div>
                    <%=resources.getSourceContent("/about/techinfo.shtml")%>
                    <div class="footer" ></div>
                </div>
            </div>
        </center>
    </body>
</html>