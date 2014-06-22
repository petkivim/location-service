<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="public/login.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.login" /></title>
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

        <div id="page_title"><fmt:message key="title.login" /></div>
        <div id="main">
            <fmt:message key="label.login.message" /><br /><br />
            <c:if test='${not empty param["auth"]}'>
                <div class="error">
                    <img alt="Error"  src="public/important.png" class='icon-important' title="Error" />
                    <fmt:message key="label.login.failed" />
                    <img alt="Error"  src="public/important.png" class='icon-important' title="Error" />
                    <br /><br />
                </div>
            </c:if>
            <div>
                <form method="POST" action="j_security_check">
                    <table>
                        <tr>
                            <td><fmt:message key="label.login.user" /></td>
                            <td><input type="text" name="j_username" /></td>
                        </tr>
                        <tr>
                            <td><fmt:message key="label.login.password" /></td>
                            <td><input type="password" name="j_password" /></td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input class="button" type="submit" value="<fmt:message key="btn.login" />" /></td>
                        </tr>
                    </table>
                </form>
                <div class="browser_support">
                    <fmt:message key="label.login.browser.support" /><br /><br />
                    <fmt:message key="label.login.note" />
                </div>
            </div>
        </div>
        <div id="copyright">
            <fmt:message key="label.login.copyright" /><br />
            <fmt:message key="label.login.version" /><br />
            <fmt:message key="label.login.update" />
        </div>
    </body>
</html>
