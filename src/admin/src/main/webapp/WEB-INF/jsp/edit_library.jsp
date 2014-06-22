<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/ibox/ibox.js"></script>
        <script type="text/javascript" src="scripts/locations.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.locations.edit.library" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.locations.edit.library" /></div>
        <div id="main">
            <div class="history">
                <fmt:message key="title.created" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${library.created}" /> (${library.creator}) |
                <fmt:message key="title.updated" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${library.updated}" /> (${library.updater})
            </div>
            <form:form acceptCharset="UTF-8" method="POST" commandName="library">
                <table id="add_location_table">
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.location_code" /></td>
                        <td class="add_location_table_col2"><form:input id="add_location_table_locationCode_input" path="locationCode" /></td>
                        <td class="add_location_table_col3"><form:errors path="locationCode" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.name" /></td>
                        <td class="add_location_table_col2"><form:input id="add_location_table_name_input" path="name" /></td>
                        <td class="add_location_table_col3">* <form:errors path="name" cssClass="error" /></td>
                    </tr>
                    <c:forEach items="${library.descriptions}" var="i" varStatus="itemsRow">
                        <tr>
                            <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.description" /> (${i.language.name})</td>
                            <td class="add_location_table_col2">
                                <form:input path="descriptions[${itemsRow.index}].description" class="add_location_table_description_input"/>
                            </td>
                            <td class="add_location_table_col3"><form:errors path="descriptions[${itemsRow.index}].description" cssClass="error" /></td>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${library.notes}" var="i" varStatus="itemsRow">
                        <tr>
                            <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.note" /> (${i.language.name})</td>
                            <td class="add_location_table_col2">
                                <form:textarea path="notes[${itemsRow.index}].note" class="add_location_table_note_input" />
                            </td>
                            <td class="add_location_table_col3"><form:errors path="notes[${itemsRow.index}].note" cssClass="error" /></td>
                        </tr>
                    </c:forEach>
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.floor" /></td>
                        <td class="add_location_table_col2"><form:input id="add_location_table_floor_input" path="floor" /></td>
                        <td class="add_location_table_col3"><form:errors path="floor" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.staffnotepri" /></td>
                        <td class="add_location_table_col2"><form:textarea path="staffNotePri" class="staffnote" /></td>
                        <td class="add_location_table_col3"><form:errors path="staffNotePri" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.staffnotesec" /></td>
                        <td class="add_location_table_col2"><form:textarea path="staffNoteSec" class="staffnote" /></td>
                        <td class="add_location_table_col3"><form:errors path="staffNoteSec" cssClass="error" /></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.image.dropdown" /></td>
                        <td class="add_location_table_col2" colspan="2">
                            <form:select path="image" id="select_image">
                                <form:option value="-1" label="-"/>
                                <form:options items="${images}" itemValue="id" itemLabel="description"/>
                            </form:select>
                            <a href="" rel="ibox" id="show_image"><fmt:message key="label.illustrations.addedit.image.show" /></a>
                        </td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"><fmt:message key="label.locations.addedit.map.dropdown" /></td>
                        <td class="add_location_table_col2" colspan="2">
                            <form:select path="map" id="select_map">
                                <form:option value="-1" label="-"/>
                                <form:options items="${maps}" itemValue="id" itemLabel="description"/>
                            </form:select>
                        </td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"></td>
                        <td class="add_location_table_col2">
                            <span id="open_map_dialog" class="pointer">
                                <img src="images/add.png" class="icon-add"/><fmt:message key="label.locations.addedit.areas.add" />
                            </span>
                        </td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"></td>
                        <td class="add_location_table_col2"></td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="add_location_table_col1"></td>
                        <td class="add_location_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                            <input class="button" type="button" name="back" id="btn_back_library" value="<fmt:message key="btn.back" />" />
                        </td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                </table>
                <input type="hidden" name="used_areas" id="used_areas" value="" />
                <div id="map_popup" class="invisible" title="<fmt:message key="label.locations.coordinates.popup.title" />">
                    <jsp:include page="coordinates_menu.jsp" />
                    <div id="areas_menu">
                        <b><fmt:message key="label.locations.coordinates.areas" /></b>
                        <div id="remove_area"></div>
                    </div>
                    <div id="map_div"></div>
                </div>
                <div id="area_container">
                    <c:forEach items="${areas}" var="area">
                        <script type="text/javascript">draw(${area.areaId}, ${area.x1}, ${area.y1}, ${area.x2}, ${area.y2}, ${area.angle});</script>
                    </c:forEach>
                </div>
                <input type="hidden" id="select_library" name="select_library" value="${param.select_library}" />
            </form:form>
        </div>
        <div class="invisible">
            <span id="images_path">${imagesPath}</span>
            <c:forEach items="${images}" var="img">
                <span id="image-${img.id}">${img.path}</span>
            </c:forEach>
            <c:forEach items="${maps}" var="map">
                <span id="map-${map.id}">${map.isGoogleMap}</span>
            </c:forEach>
        </div>
    </body>
</html>