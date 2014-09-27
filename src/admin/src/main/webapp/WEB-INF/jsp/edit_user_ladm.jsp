<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/ladmuser.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.users.edit" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.users.edit" /></div>
        <div id="main">
            <div class="history">
                <fmt:message key="title.created" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${userInfo.user.created}" /> (${userInfo.user.creator}) |
                <fmt:message key="title.updated" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${userInfo.user.updated}" /> (${userInfo.user.updater})
            </div>
            <form:form acceptCharset="UTF-8" method="POST" commandName="userInfo">
                <table id="add_user_table">
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.firstname" /></td>
                        <td class="add_user_table_col2"><form:input id="add_user_table_firstname_input" path="user.firstName" /></td>
                        <td class="add_location_table_col3">* <form:errors path="user.firstName" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.lastname" /></td>
                        <td class="add_user_table_col2"><form:input id="add_user_table_lastname_input" path="user.lastName" /></td>
                        <td class="add_location_table_col3">* <form:errors path="user.lastName" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.username" /></td>
                        <td class="add_user_table_col2"><form:input id="add_user_table_username_input" path="user.username" readonly="true" /></td>
                        <td class="add_location_table_col3">* <fmt:message key="label.users.edit.readonly" /><form:errors path="user.username" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.password" /></td>
                        <td class="add_user_table_col2"><form:input type="password" id="add_user_table_password_input" path="user.passwordUi" /></td>
                        <td class="add_user_table_col3">* <form:errors path="user.passwordUi" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.password.again" /></td>
                        <td class="add_user_table_col2"><form:input type="password" id="add_user_table_passwordControl_input" path="user.passwordControl" /></td>
                        <td class="add_user_table_col3">* <form:errors path="user.passwordControl" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.organization" /></td>
                        <td class="add_user_table_col2"><form:input id="add_user_table_organization_input" path="user.organization" /></td>
                        <td class="add_location_table_col3">* <form:errors path="user.organization" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"><fmt:message key="label.users.addedit.email" /></td>
                        <td class="add_user_table_col2"><form:input id="add_user_table_email_input" path="user.email" /></td>
                        <td class="add_location_table_col3">* <form:errors path="user.email" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_user_table_col1"></td>
                        <td class="add_user_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                            <input class="button" type="button" name="back" id="btn_back_user" value="<fmt:message key="btn.back" />" />
                        </td>
                        <td class="add_user_table_col3"></td>
                    </tr>
                </table>
                <input type="hidden" id="select_user" name="select_user" value="${param.select_user}" />
            </form:form>
        </div>
    </body>
</html>