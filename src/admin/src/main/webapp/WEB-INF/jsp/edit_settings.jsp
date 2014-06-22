<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <link rel="stylesheet" media="screen" type="text/css" href="scripts/colorpicker/colorpicker.css" />
        <script type="text/javascript" src="scripts/owners.js"></script>
        <script type="text/javascript" src="scripts/colorpicker/colorpicker.js"></script>
        <script type="text/javascript" src="scripts/init_colorpicker.js"></script>

        <title><fmt:message key="title.service" /> - <fmt:message key="title.settings" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.settings" /></div>
        <div id="main">
            <div class="history">
                <fmt:message key="title.created" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${owner.created}" /> (${owner.creator}) |
                <fmt:message key="title.updated" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${owner.updated}" /> (${owner.updater})
            </div>
            <form:form acceptCharset="UTF-8" method="POST" commandName="owner" action="editsettings.htm">
                <c:if test="${param.success != null}">
                    <img alt="Success" src="images/success.png" class='icon-success' title="Success" />
                    <fmt:message key="label.settings.updated" />
                </c:if>
                <h2><fmt:message key="label.settings.drawing.title" /></h2>
                <table id="add_owner_table">
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.settings.drawing.color" /></td>
                        <td class="add_owner_table_col2">#<form:input id="select_color" path="color" /></td>
                        <td class="add_owner_table_col3">* <form:errors path="color" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.settings.drawing.opacity" /></td>
                        <td class="add_owner_table_col2">
                            <span id="slider_label"></span>
                            <form:input id="add_owner_table_opacity_input" class="invisible" path="opacity" />
                        </td>
                        <td class="add_owner_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"></td>
                        <td class="add_owner_table_col2"><div id="slider"></div></td>
                        <td class="add_owner_table_col3">* <form:errors path="opacity" cssClass="error" /></td>
                    </tr>
                </table>
                <h2><fmt:message key="label.settings.preprocessing.exceptions.title" /></h2>
                <table class="owner_exceptions">
                    <tr>
                        <td class="add_exception"><span class="pointer"><img class="icon-add" src="images/add.png"/><fmt:message key="label.owner.notfound.exceptions.add" /></span></td>
                    </tr>
                    <tr>
                        <td class="add_owner_exceptions_table_col1_title"><fmt:message key="label.settings.notfound.exceptions.col1" /></td>
                        <td class="add_owner_exceptions_table_col2_title"><fmt:message key="label.settings.notfound.exceptions.col2" /></td>
                        <td class="add_owner_exceptions_table_col3_title"><fmt:message key="label.settings.notfound.exceptions.col3" /></td>
                        <td class="add_owner_table_col4"></td>
                    </tr>
                    <c:forEach items="${owner.preprocessingRedirects}" var="mod" varStatus="row">
                        <tr>
                            <td class="add_owner_exceptions_table_col1"><form:checkbox path="preprocessingRedirects[${row.index}].isActive"/></td>
                            <td class="add_owner_exceptions_table_col2"><form:input path="preprocessingRedirects[${row.index}].condition" /></td>
                            <td class="add_owner_exceptions_table_col3"><form:input path="preprocessingRedirects[${row.index}].operation" /></td>
                            <td class="add_owner_table_col4">
                                <img title="<fmt:message key="label.owner.notfound.exceptions.delete" />" class="icon-delete" src="images/delete.png"/>
                                <form:errors path="preprocessingRedirects[${row.index}].condition" cssClass="error" />
                                <form:errors path="preprocessingRedirects[${row.index}].operation" cssClass="error" />
                            </td>
                        </tr>
                    </c:forEach>
                </table><br />
                <h2><fmt:message key="label.settings.notfound.exceptions.title" /></h2>
                <table class="owner_exceptions">
                    <tr>
                        <td class="add_exception"><span class="pointer"><img class="icon-add" src="images/add.png"/><fmt:message key="label.owner.notfound.exceptions.add" /></span></td>
                    </tr>
                    <tr>
                        <td class="add_owner_exceptions_table_col1_title"><fmt:message key="label.settings.notfound.exceptions.col1" /></td>
                        <td class="add_owner_exceptions_table_col2_title"><fmt:message key="label.settings.notfound.exceptions.col2" /></td>
                        <td class="add_owner_exceptions_table_col3_title"><fmt:message key="label.settings.notfound.exceptions.col3" /></td>
                        <td class="add_owner_table_col4"></td>
                    </tr>
                    <c:forEach items="${owner.notFoundRedirects}" var="mod" varStatus="row">
                        <tr>
                            <td class="add_owner_exceptions_table_col1"><form:checkbox path="notFoundRedirects[${row.index}].isActive"/></td>
                            <td class="add_owner_exceptions_table_col2"><form:input path="notFoundRedirects[${row.index}].condition" /></td>
                            <td class="add_owner_exceptions_table_col3"><form:input path="notFoundRedirects[${row.index}].operation" /></td>
                            <td class="add_owner_table_col4">
                                <img title="<fmt:message key="label.owner.notfound.exceptions.delete" />" class="icon-delete" src="images/delete.png"/>
                                <form:errors path="notFoundRedirects[${row.index}].condition" cssClass="error" />
                                <form:errors path="notFoundRedirects[${row.index}].operation" cssClass="error" />
                            </td>
                        </tr>
                    </c:forEach>
                </table><br />
                <h2><fmt:message key="label.settings.exporter.title" /></h2>
                <table>
                    <tr>
                        <td class="add_owner_exporter_table_col1"><fmt:message key="label.settings.exporter.visibility" /></td>
                        <td class="add_owner_exporter_table_col2"><form:checkbox path="exporterVisible"/></td>
                        <td class="add_owner_exporter_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_owner_exporter_table_col1"><fmt:message key="label.settings.exporter.ips" /></td>
                        <td class="add_owner_exporter_table_col2"><form:textarea path="allowedIPs" class="ip_list" /></td>
                        <td class="add_owner_exporter_table_col3"><form:errors path="allowedIPs" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"></td>
                        <td class="add_owner_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                        </td>
                        <td class="add_owner_table_col3"></td>
                    </tr>
                </table>
            </form:form>
        </div>
    </body>
</html>