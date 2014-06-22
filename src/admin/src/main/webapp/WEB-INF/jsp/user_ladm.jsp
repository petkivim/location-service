<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/userowner_main.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.users" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.users.ladm" /></div>
        <div id="main">
            <form action="ladmuser.htm"  accept-charset="UTF-8" method="POST">
                <table id="user_owner_table">
                    <tr>
                        <td class="table_sub_title"><fmt:message key="label.users.title" /></td>
                    </tr>
                    <tr>
                        <td class="user_owner_table_col1"><fmt:message key="label.users.dropdown" /></td>
                        <td class="user_owner_table_col2" colspan="2">
                            <select id="select_user" name="select_user">
                                <c:forEach items="${model.users}" var="user">
                                    <c:choose>
                                        <c:when test="${param.select_user eq user.username}">
                                            <option value="${user.username}" selected>${user.username}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${user.username}">${user.username}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </td>
                        <td class="user_owner_table_col3">
                            <input class="button" type="submit" name="btn_edit_user" value="<fmt:message key="btn.edit" />" />
                            <input class="button" type="submit" name="btn_add_user" value="<fmt:message key="btn.add" />" />
                            <input class="button" type="button" name="delete_user" value="<fmt:message key="btn.delete" />" />
                            <input type="submit" name="btn_delete_user" class="invisible" />
                        </td>
                    </tr>
                    <c:if test = "${model.errorMsg != null}">
                        <tr>
                            <td colspan="4" class="error">
                                <img alt="Error"  src="images/important.png" class='icon-important' title="Error" />
                                ${model.errorMsg}
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </div>
    </body>
</html>