<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title><fmt:message key="title.service" /></title>
    </head>
    <body>
        <div class="navi">
            <table>
                <tr>
                    <td class="navi_left">
                        <span class="title"><fmt:message key="title.service" /></span>
                    </td>
                    <td class="navi_right">
                    </td>
                </tr>
            </table>
        </div>
                    
        <div id="page_title"><fmt:message key="menu.error.404" /></div>
        <div id="main">   
            <img alt="Error"  src="public/important.png" class='icon-important' title="Error" />
            <fmt:message key="error.404" /><br /><br />
        </div>
    </body>
</html>
