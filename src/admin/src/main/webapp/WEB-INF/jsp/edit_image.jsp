<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/ibox/ibox.js"></script>
        <script type="text/javascript" src="scripts/illustrations.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.illustrations.edit.image" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.illustrations.edit.image" /></div>
        <div id="main">
            <div class="history">
                <fmt:message key="title.created" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${image.created}" /> (${image.creator}) |
                <fmt:message key="title.updated" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${image.updated}" /> (${image.updater})
            </div>
            <form:form acceptCharset="UTF-8" method="POST" commandName="image" enctype="multipart/form-data">
                <table id="add_image_table">
                    <tr>
                        <td class="add_image_table_col1"><fmt:message key="label.illustrations.addedit.description" /></td>
                        <td class="add_image_table_col2"><form:input id="add_image_table_description_input" path="description" /></td>
                        <td class="add_location_table_col3">* <form:errors path="description" cssClass="error" /></td>
                    </tr>
                    <c:if test = "${external == true}">
                        <tr>
                            <td class="add_image_table_col1"><fmt:message key="label.illustrations.addedit.url" /></td>
                            <td class="add_image_table_col2"><form:input id="add_image_table_url_input" path="url" /></td>
                            <td class="add_image_table_col3">* <form:errors path="url" cssClass="error" /></td>
                        </tr>
                    </c:if>
                    <c:if test = "${external == false}">
                        <tr>
                            <td class="add_image_table_col1"><fmt:message key="label.illustrations.addedit.image" /></td>
                            <td class="add_image_table_col2">
                                <input readonly="readonly" type="text" value="${image.path}"/>
                            </td>
                            <td class="add_image_table_col3"><fmt:message key="label.illustrations.addedit.noteditable" /></td>
                        </tr>
                        <tr>
                            <td class="add_image_table_col1"><fmt:message key="label.illustrations.addedit.upload" /></td>
                            <td class="add_image_table_col2"><form:input path="file" type="file"/></td>
                            <td class="add_image_table_col3"><form:errors path="file" cssClass="error" /></td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="add_image_table_col1"></td>
                        <td class="add_image_table_col2"><a href="${imagesPath}${image.path}" rel="ibox" id="show_image"><fmt:message key="label.illustrations.addedit.image.show" /></a></td>
                        <td class="add_image_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_image_table_col1"></td>
                        <td class="add_image_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                            <input class="button" type="button" name="back" id="btn_back_image" value="<fmt:message key="btn.back" />" />
                        </td>
                        <td class="add_image_table_col3"></td>
                    </tr>
                </table>
                <input type="hidden" id="imageId" name="imageId" value="${param.imageId}" />
            </form:form>
        </div>
    </body>
</html>