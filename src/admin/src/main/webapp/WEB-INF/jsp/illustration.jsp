<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/illustrations_main.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.illustrations" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.illustrations" /></div>
        <div id="main">
            <form action="illustrations.htm"  accept-charset="UTF-8" method="POST">
                <table id="image_table_1">
                    <tr>
                        <td class="table_sub_title"><fmt:message key="label.illustrations.images.title" /></td>
                    </tr>
                    <tr>                      
                        <td class="image_table_col1"><fmt:message key="label.illustrations.images.dropdown" /></td>
                        <td class="image_table_col2" colspan="2">
                            <select id="select_image" name="select_image">
                                <c:forEach items="${model.images}" var="image">
                                    <c:choose>
                                        <c:when test="${param.select_image == image.id}">
                                            <option value="${image.id}" selected>${image.description}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${image.id}">${image.description}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </td>
                        <td class="image_table_col3">
                            <c:if test = "${fn:length(model.images) > 0}">
                                <input class="button" type="submit" name="btn_edit_img" value="<fmt:message key="btn.edit" />" />
                            </c:if>
                            <input class="button" type="submit" name="btn_add_img" value="<fmt:message key="btn.add" />" />
                            <c:if test = "${fn:length(model.images) > 0}">
                                <input class="button" type="button" name="delete_img" value="<fmt:message key="btn.delete" />" />
                                <input type="submit" name="btn_delete_img" class="invisible" />
                            </c:if>
                        </td>
                    </tr>
                    <c:if test = "${model.errorMsgImg != null}">
                        <tr>
                            <td colspan="4" class="error">
                                <img alt="Error"  src="images/important.png" class='icon-important' title="Error" />
                                ${model.errorMsgImg}
                            </td>
                        </tr>
                    </c:if>
                    <tr>
                        <td class="table_sub_title"><fmt:message key="label.illustrations.maps.title" /></td>
                    </tr>
                    <tr>
                        <td class="image_table_col1"><fmt:message key="label.illustrations.maps.dropdown" /></td>
                        <td class="image_table_col2" colspan="2">
                            <select id="select_map" name="select_map">
                                <c:forEach items="${model.maps}" var="map">
                                    <c:choose>
                                        <c:when test="${param.select_map == map.id}">
                                            <option value="${map.id}" selected>${map.description}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${map.id}">${map.description}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </td>
                        <td class="image_table_col3">
                            <c:if test = "${fn:length(model.maps) > 0}">
                                <input class="button" type="submit" name="btn_edit_map" value="<fmt:message key="btn.edit" />" />
                            </c:if>
                            <input class="button" type="submit" name="btn_add_map" value="<fmt:message key="btn.add" />" />
                            <c:if test = "${fn:length(model.maps) > 0}">
                                <input class="button" type="button" name="delete_map" value="<fmt:message key="btn.delete" />" />
                                <input type="submit" name="btn_delete_map" class="invisible" />
                            </c:if>
                        </td>
                    </tr>
                    <c:if test = "${model.errorMsgMap != null}">
                        <tr>
                            <td colspan="4" class="error">
                                <img alt="Error"  src="images/important.png" class='icon-important' title="Error" />
                                ${model.errorMsgMap}
                            </td>
                        </tr>
                    </c:if>
                </table>
            </form>
        </div>
    </body>
</html>