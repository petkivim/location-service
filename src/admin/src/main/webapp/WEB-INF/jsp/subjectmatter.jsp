<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/subjectmatters_main.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.subjectmatters" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.subjectmatters" /></div>
        <div id="main">
            <form action="subjectmatters.htm"  accept-charset="UTF-8" method="POST">
                <table id="image_table_1">
                    <tr>
                        <td class="image_table_col1"><fmt:message key="label.subjectmatters.dropdown" /></td>
                        <td class="image_table_col2" colspan="2">
                            <select id="select_image" name="select_subject">
                                <c:forEach items="${model.subjects}" var="subject" varStatus="row">
                                    <c:choose>
                                        <c:when test="${row.index == 0}">
                                            <option value="0" class="optgroup">${model.subjects[0].language.name}</option>
                                        </c:when>
                                        <c:when test="${row.index > 0 && model.subjects[row.index].language.name ne  model.subjects[row.index-1].language.name}">
                                            <option value="0" class="optgroup">${model.subjects[row.index].language.name}</option>
                                        </c:when>
                                    </c:choose>
                                    <c:choose>
                                        <c:when test="${param.select_subject == subject.id}">
                                            <option value="${subject.id}" selected>${subject.indexTerm}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option class="koe" value="${subject.id}">${subject.indexTerm}</option>
                                        </c:otherwise>
                                    </c:choose>
                                    <c:if test="${row.index == fn:length(model.subjects) - 1}">
                                    </c:if>
                                </c:forEach>
                            </select>
                        </td>
                        <td class="image_table_col3">
                            <c:if test = "${fn:length(model.subjects) > 0}">
                                <input class="button" type="submit" name="btn_edit_subject" value="<fmt:message key="btn.edit" />" />
                            </c:if>
                            <input class="button" type="submit" name="btn_add_subject" value="<fmt:message key="btn.add" />" />
                            <c:if test = "${fn:length(model.subjects) > 0}">
                                <input class="button" type="button" name="btn_delete" value="<fmt:message key="btn.delete" />" />
                                <input type="submit" name="btn_delete_subject" class="invisible" />
                            </c:if>
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