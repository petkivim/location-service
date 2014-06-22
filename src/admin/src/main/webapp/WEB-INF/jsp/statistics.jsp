<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />       
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/datepicker.js"></script>
        <script type="text/javascript" src="scripts/statistics.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.statistics" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.statistics" /></div>
        <div id="main">
            <form action="getstatistics.htm"  accept-charset="UTF-8" method="POST">
                <table>
                    <tr>
                        <td><fmt:message key="label.statistics.type" /></td>
                        <td>
                            <div id="type">
                                <input type="radio" id="type1" name="type" value="both" <c:if test="${param.type eq 'both' || param.type == null}">checked</c:if>/><label for="type1"><fmt:message key="label.statistics.type.both" /></label>
                                <input type="radio" id="type2" name="type" value="handler" <c:if test="${param.type eq 'handler'}">checked</c:if>/><label for="type2"><fmt:message key="label.statistics.type.handler" /></label>
                                <input type="radio" id="type3" name="type" value="exporter" <c:if test="${param.type eq 'exporter'}">checked</c:if>/><label for="type3"><fmt:message key="label.statistics.type.exporter" /></label>
                            </div> 
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="label.statistics.period" /></td>
                        <td>
                            <input type="text" name="from" id="from" class="date" value="${param.from}" />
                            -
                            <input type="text" name="to" id="to" class="date" value="${param.to}" />
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="label.statistics.group" /></td>
                        <td>
                            <div id="group">
                                <input type="radio" id="group1" name="group" value="d" <c:if test="${param.group eq 'd'}">checked</c:if>/><label for="group1"><fmt:message key="label.statistics.day" /></label>
                                <input type="radio" id="group2" name="group" value="m" <c:if test="${param.group eq 'm' || param.group == null}">checked</c:if>/><label for="group2"><fmt:message key="label.statistics.month" /></label>
                                <input type="radio" id="group3" name="group" value="y" <c:if test="${param.group eq 'y'}">checked</c:if>/><label for="group3"><fmt:message key="label.statistics.year" /></label>
                            </div> 
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="label.statistics.order" /></td>
                        <td>
                            <div id="order">
                                <input type="radio" id="order1" name="order" value="asc" <c:if test="${param.order eq 'asc'}">checked</c:if>/><label for="order1"><fmt:message key="label.statistics.order.asc" /></label>
                                <input type="radio" id="order2" name="order" value="desc" <c:if test="${param.order eq 'desc' || param.order == null}">checked</c:if>/><label for="order2"><fmt:message key="label.statistics.order.desc" /></label>
                            </div> 
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="label.statistics.output" /></td>
                        <td>
                            <div id="order">
                                <input type="radio" id="output1" name="output" value="screen" <c:if test="${param.output eq 'screen' || param.output == null}">checked</c:if>/><label for="output1"><fmt:message key="label.statistics.output.screen" /></label>
                                <input type="radio" id="output2" name="output" value="file" <c:if test="${param.output eq 'file'}">checked</c:if>/><label for="output2"><fmt:message key="label.statistics.output.file" /></label>
                                </div> 
                            </td>
                        </tr>
                        <tr>
                            <td></td>
                            <td><input class="button" type="submit" name="search" value="<fmt:message key="btn.search" />" /></td>
                    </tr>
                </table>
                <c:choose>
                    <c:when test="${fn:length(model.stats) == 0 && model.stats != null}">
                        <h2>Results</h2>
                        <fmt:message key="label.statistics.results.zero" />
                    </c:when>
                    <c:when test="${fn:length(model.stats) > 0}">    
                        <h2>Results</h2>
                        <table id="statistics_table">
                            <tr>
                                <th class="statistics_table_col_1">
                                    <c:choose>
                                        <c:when test="${param.group eq 'd' || param.group == null}">
                                            <fmt:message key="label.statistics.day" />
                                        </c:when>
                                        <c:when test="${param.group eq 'm'}">
                                            <fmt:message key="label.statistics.month" />
                                        </c:when>
                                        <c:when test="${param.group eq 'y'}">
                                            <fmt:message key="label.statistics.year" />
                                        </c:when>
                                    </c:choose>
                                </th>
                                <th class="statistics_table_col_2"><fmt:message key="label.statistics.results.column1" /></th>
                                <th class="statistics_table_col_3"><fmt:message key="label.statistics.results.column2" /></th>
                            </tr>
                            <c:forEach items="${model.stats}" var="stat">
                                <tr>
                                    <td>${stat[2]}</td>
                                    <td>${stat[0]}</td>
                                    <td>${stat[1]}</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </c:when>
                </c:choose>
            </form>
        </div>
    </body>
</html>
