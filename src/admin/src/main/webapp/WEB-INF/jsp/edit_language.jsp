<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/languages.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.languages.edit" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.languages.edit" /></div>
        <div id="main">
            <div class="history">
                <fmt:message key="title.created" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${language.created}" /> (${language.creator}) |
                <fmt:message key="title.updated" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${language.updated}" /> (${language.updater})
            </div>
            <form:form acceptCharset="UTF-8" method="POST" commandName="language">
                <table id="add_language_table">
                    <tr>
                        <td class="add_language_table_col1"><fmt:message key="label.language.addedit.code" /></td>
                        <td class="add_language_table_col2"><form:input id="add_language_table_code_input" path="code" /></td>
                        <td class="add_language_table_col3">* <form:errors path="code" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_language_table_col1"><fmt:message key="label.language.addedit.name" /></td>
                        <td class="add_language_table_col2"><form:input id="add_language_table_name_input" path="name" /></td>
                        <td class="add_language_table_col3">* <form:errors path="name" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_language_table_col1"></td>
                        <td class="add_language_table_col2"></td>
                        <td class="add_language_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_language_table_col1"></td>
                        <td class="add_language_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                            <input class="button" type="button" name="back" id="btn_back_language" value="<fmt:message key="btn.back" />" />
                        </td>
                        <td class="add_language_table_col3"></td>
                    </tr>
                </table>
                <input type="hidden" id="select_language" name="select_language" value="${param.select_language}" />
            </form:form>
        </div>
    </body>
</html>