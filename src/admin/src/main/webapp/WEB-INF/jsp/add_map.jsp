<%@ include file="include.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <link rel="stylesheet" media="screen" type="text/css" href="scripts/colorpicker/colorpicker.css" />
        <script type="text/javascript" src="scripts/illustrations.js"></script>
        <script type="text/javascript" src="scripts/maps.js"></script>
        <script type="text/javascript" src="scripts/colorpicker/colorpicker.js"></script>
        <script type="text/javascript" src="scripts/init_colorpicker.js"></script>

        <title><fmt:message key="title.service" /> - <fmt:message key="title.illustrations.add.map" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.illustrations.add.map" /></div>
        <div id="main">
            <form:form acceptCharset="UTF-8" method="POST" commandName="map" enctype="multipart/form-data">
                <table id="add_map_table">
                    <tr>
                        <td class="add_map_table_col1"><fmt:message key="label.illustrations.addedit.description" /></td>
                        <td class="add_map_table_col2"><form:input id="add_map_table_description_input" path="description" /></td>
                        <td class="add_map_table_col3">* <form:errors path="description" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_map_table_col1"><fmt:message key="label.illustrations.addedit.color" /></td>
                        <td class="add_map_table_col2">#<form:input id="select_color" path="color" /></td>
                        <td class="add_map_table_col3"><form:errors path="color" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_map_table_col1"><fmt:message key="label.illustrations.addedit.opacity" /></td>
                        <td class="add_map_table_col2">
                            <span id="slider_label"></span>
                            <form:input id="add_map_table_opacity_input" class="invisible" path="opacity" />
                        </td>
                        <td class="add_owner_table_col3"><form:errors path="opacity" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_map_table_col1"></td>
                        <td class="add_map_table_col2"><div id="slider"></div></td>
                        <td class="add_map_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_map_table_col1"><fmt:message key="label.illustrations.addedit.url" /></td>
                        <td class="add_map_table_col2"><form:input id="add_map_table_url_input" path="url" /></td>
                        <td class="add_map_table_col3"><form:errors path="url" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_map_table_col1"></td>
                        <td class="add_map_table_col2"><fmt:message key="label.illustrations.addedit.or" /></td>
                        <td class="add_map_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_map_table_col1"><fmt:message key="label.illustrations.addedit.map.select" /></td>
                        <td class="add_map_table_col2">
                            <form:select path="filePath" id="select_map">
                                <form:option value="" label="-"/>
                                <form:options items="${files}" />
                            </form:select>
                        </td>
                        <td class="add_map_table_col3">* <form:errors path="path" cssClass="error" /></td>
                    </tr>
                    <c:if test="${fn:length(map.files) > 0}">
                        <tr>
                            <td class="add_map_table_col1"></td>
                            <td class="add_map_table_col2"><fmt:message key="label.illustrations.addedit.or" /></td>
                            <td class="add_map_table_col3"></td>
                        </tr>
                        <c:forEach var="entry" items="${map.files}">
                            <tr>
                                <td class="add_map_table_col1"><fmt:message key="label.illustrations.addedit.upload" /> (${languages[entry.key].name})</td>
                                <td class="add_map_table_col2"><form:input path="files[${entry.key}]" type="file"/></td>
                                <td class="add_map_table_col3"><form:errors path="files[${entry.key}]" cssClass="error" /></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <tr>
                        <td class="add_map_table_col1"></td>
                        <td class="add_map_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                            <input class="button" type="button" name="back" id="btn_back_map" value="<fmt:message key="btn.back" />" />
                        </td>
                        <td class="add_map_table_col3"></td>
                    </tr>
                </table>
            </form:form>
        </div>
    </body>
</html>