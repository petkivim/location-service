<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <link rel="stylesheet" media="screen" type="text/css" href="scripts/colorpicker/colorpicker.css" />        
        <script type="text/javascript" src="scripts/userowner.js"></script>
        <script type="text/javascript" src="scripts/owners.js"></script>
        <script type="text/javascript" src="scripts/colorpicker/colorpicker.js"></script>
        <script type="text/javascript" src="scripts/init_colorpicker.js"></script>

        <title><fmt:message key="title.service" /> - <fmt:message key="title.owners.add" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.owners.add" /></div>
        <div id="main">
            <form:form acceptCharset="UTF-8" method="POST" commandName="owner">
                <table id="add_owner_table">
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.owners.addedit.code" /></td>
                        <td class="add_owner_table_col2"><form:input id="add_owner_table_code_input" path="code" /></td>
                        <td class="add_location_table_col3">* <form:errors path="code" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.owners.addedit.name" /></td>
                        <td class="add_owner_table_col2"><form:input id="add_owner_table_name_input" path="name" /></td>
                        <td class="add_owner_table_col3">* <form:errors path="name" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.owners.addedit.strategy" /></td>
                        <td class="add_owner_table_col2">
                            <form:select path="locatingStrategy">
                                <c:forEach items="${strategies}" var="strategy">
                                    <form:option value="${strategy}" label="${fn:toLowerCase(strategy)}" />
                                </c:forEach>
                            </form:select>
                        </td>
                        <td class="add_location_table_col3">* <form:errors path="locatingStrategy" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.owner.addedit.color" /></td>
                        <td class="add_owner_table_col2">#<form:input id="select_color" path="color" /></td>
                        <td class="add_owner_table_col3">* <form:errors path="color" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_owner_table_col1"><fmt:message key="label.owner.addedit.opacity" /></td>
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
                    <tr>
                        <td class="add_owner_table_col1"></td>
                        <td class="add_owner_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                            <input class="button" type="button" name="back" id="btn_back_owner" value="<fmt:message key="btn.back" />" />
                        </td>
                        <td class="add_owner_table_col3"></td>
                    </tr>
                </table>
            </form:form>
        </div>
    </body>
</html>