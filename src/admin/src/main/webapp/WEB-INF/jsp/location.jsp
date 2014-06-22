<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/locations_main.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.locations" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.locations" /></div>
        <div id="main">
            <form id="search_form" action="locations.htm" accept-charset="UTF-8" method="POST">
                <div class="ui-widget">
                    <label for="location_search"><fmt:message key="label.locations.search" /></label>
                    <span title="<fmt:message key="label.locations.search.info" />">
                        <img class="icon" src="images/info.png" />
                    </span>
                    <input id="location_search" />
                    <input class="button" type="submit" value="<fmt:message key="btn.select" />" id="btn_list_location" name="" />
                    <input class="button" type="submit" value="<fmt:message key="btn.edit" />" id="btn_edit_location" name="" />
                    <input type="hidden" id="location_id" name="" />
                    <span id="loading"><img src="images/loading.gif" /></span>

                </div>
            </form>
            <form action="locations.htm" accept-charset="UTF-8" method="POST">
                <!-- Libraries section begins -->
                <h2><fmt:message key="label.locations.libraries.title" /></h2>
                <table>
                    <tr>
                        <c:if test = "${fn:length(model.libraries) > 0}">
                            <td><fmt:message key="label.locations.libraries.dropdown" /></td>
                            <td>
                                <select id="select_library" name="select_library">
                                    <c:forEach items="${model.libraries}" var="library">
                                        <c:choose>
                                            <c:when test="${model.libraryId == library.locationId}">
                                                <option value="${library.locationId}" selected>${library.name}</option>
                                            </c:when>
                                            <c:otherwise>
                                                <option value="${library.locationId}">${library.name}</option>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </select>
                            </td>
                            <td><input class="button" type="submit" value="<fmt:message key="btn.list_collections" />" id="btn_list_library" name="btn_list_library" /></td>
                            <td><input class="button" type="submit" value="<fmt:message key="btn.edit" />" name="btn_edit_library" /></td>
                        </c:if>
                        <td><input class="button" type="submit" value="<fmt:message key="btn.add" />" name="btn_add_library" /></td>
                        <c:if test = "${fn:length(model.libraries) > 0}">
                            <td>
                                <input class="button" type="button" name="delete_library" value="<fmt:message key="btn.delete" />" />
                                <input type="submit" name="btn_delete_library" class="invisible" />
                            </td>
                        </c:if>
                    </tr>
                </table>
                <!-- Collections section begins -->
                <div id="level1">
                    <h2><fmt:message key="label.locations.collections.title" /></h2>
                    <table>
                        <tr>
                            <c:if test = "${fn:length(model.collections) > 0}">
                                <td><fmt:message key="label.locations.collections.dropdown" /></td>
                                <td>
                                    <select id="select_collection" name="select_collection">
                                        <c:forEach items="${model.collections}" var="collection">
                                            <c:choose>
                                                <c:when test="${model.collectionId == collection.locationId}">
                                                    <option value="${collection.locationId}" selected>${collection.name}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${collection.locationId}">${collection.name}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input class="button" type="submit" value="<fmt:message key="btn.list_shelves" />" id="btn_list_collection" name="btn_list_collection" /></td>
                                <td><input class="button" type="submit" value="<fmt:message key="btn.edit" />" name="btn_edit_collection" /></td>
                            </c:if>
                            <c:if test = "${fn:length(model.libraries) > 0}">
                                <td><input class="button" type="submit" value="<fmt:message key="btn.add" />" name="btn_add_collection" /></td>
                            </c:if>
                            <c:if test = "${fn:length(model.collections) > 0}">
                                <td>
                                    <input class="button" type="button" name="delete_collection" value="<fmt:message key="btn.delete" />" />
                                    <input type="submit" name="btn_delete_collection" class="invisible" />
                                </td>
                            </c:if>
                        </tr>
                    </table>
                </div>
                <!-- Shelves section begins -->
                <div id="level2">
                    <h2><fmt:message key="label.locations.shelves.title" /></h2>
                    <table>
                        <tr>
                            <c:if test = "${fn:length(model.shelves) > 0}">
                                <td><fmt:message key="label.locations.shelves.dropdown" /></td>
                                <td>
                                    <select id="select_shelf" name="select_shelf">
                                        <c:forEach items="${model.shelves}" var="shelf">
                                            <c:choose>
                                                <c:when test="${model.shelfId == shelf.locationId}">
                                                    <option value="${shelf.locationId}" selected>${shelf.name}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${shelf.locationId}">${shelf.name}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input class="button" type="submit" value="<fmt:message key="btn.edit" />" name="btn_edit_shelf" /></td>
                            </c:if>
                            <c:if test = "${fn:length(model.collections) > 0}">
                                <td><input class="button" type="submit" value="<fmt:message key="btn.add" />" name="btn_add_shelf" /></td>
                            </c:if>
                            <c:if test = "${fn:length(model.shelves) > 0}">
                                <td>
                                    <input class="button" type="button" name="delete_shelf" value="<fmt:message key="btn.delete" />" />
                                    <input type="submit" name="btn_delete_shelf" class="invisible" />
                                </td>
                            </c:if>
                        </tr>
                    </table>
                </div>
                <!-- Shelves section ends -->
                <!-- Collections section ends -->
                <!-- Libraries section ends -->
            </form>
        </div>
        <span id="owner" class="invisible">${model.owner}</span>
    </body>
</html>
