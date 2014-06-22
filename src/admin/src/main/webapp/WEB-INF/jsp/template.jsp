<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/templates_main.js"></script>
        <title><fmt:message key="title.service" /> - <fmt:message key="title.templates" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.templates" /></div>
        <div id="main">
            <h2><fmt:message key="title.templates.sub.stylesheets" /></h2>
            <form action="templates.htm"  accept-charset="UTF-8" method="POST">
                <input type="hidden" name="select_lang" value="${model.language.code}" />
                <fmt:message key="label.templates.edit.css" /> <input class="button" type="submit" name="btn_edit_sys_css" id="btn_edit_css" value="<fmt:message key="btn.edit" />" />
            </form>
            <c:choose>
                <c:when test="${model.responseMsgCss != null}">
                    <br /><img alt="Success" src="images/success.png" class='icon-success' title="Success" />
                    <span class="error">${model.responseMsgCss}</span>
                </c:when>
                <c:when test="${model.errorMsgCss != null}">
                    <br /><img alt="Warning" src="images/warning.png" class='icon-warn' title="Warning" />
                    <span class="error">${model.errorMsgCss}</span>
                </c:when>
            </c:choose>           
            <br /><fmt:message key="label.templates.stylesheet.add" /><br /><br />
            <form action="templates.htm"  accept-charset="UTF-8" method="POST">
                <input type="text" name="css_new" size="27" value="" />.css
                <input type="hidden" name="select_lang" value="${model.language.code}" />
                <input class="button" type="submit" name="btn_add_css" value="<fmt:message key="btn.add" />" />
            </form><br />
            <c:choose>
                <c:when test="${fn:length(model.stylesheets) > 0}">
                    <form id="stylesheets_form" action="templates.htm"  accept-charset="UTF-8" method="POST">
                        <input type="hidden" name="select_lang" value="${model.language.code}" />
                        <table>
                            <tr>
                                <td><fmt:message key="label.templates.stylesheets.list.title" /></td>
                                <td>
                                    <select name="stylesheet">
                                        <c:forEach items="${model.stylesheets}" var="temp">
                                            <c:choose>
                                                <c:when test="${param.stylesheet eq temp}">
                                                    <option value="${temp}" selected>${temp}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${temp}">${temp}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input class="button" type="submit" name="btn_edit_css" value="<fmt:message key="btn.edit" />" /></td>
                                <td>
                                    <input class="button" type="button" name="delete_css" value="<fmt:message key="btn.delete" />" />
                                    <input type="submit" name="btn_delete_css" class="invisible" />
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when >
                <c:otherwise>
                    <fmt:message key="label.templates.stylesheets.list.title.zero" />
                </c:otherwise>
            </c:choose>
            <h2><fmt:message key="title.templates.sub.templates" /></h2>
            <form action="templates.htm"  accept-charset="UTF-8" method="POST">
                <input type="hidden" name="select_lang" value="${model.language.code}" />
                <fmt:message key="label.templates.edit.sys_template" /> <input class="button" type="submit" name="btn_edit_sys_template" id="btn_edit_sys_template" value="<fmt:message key="btn.edit" />" />
            </form><br />
            <form action="templates.htm"  accept-charset="UTF-8" method="POST">
                <table>
                    <tr>
                        <td><fmt:message key="label.templates.select.language" /></td>
                        <td>
                            <select id="select_lang" name="select_lang">
                                <c:forEach items="${model.languages}" var="lang">
                                    <c:choose>
                                        <c:when test="${param.select_lang eq lang.code}">
                                            <option value="${lang.code}" selected>${lang.name}</option>
                                        </c:when>
                                        <c:otherwise>
                                            <option value="${lang.code}">${lang.name}</option>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                            </select>
                        </td>
                        <td><input class="button" type="submit" name="btn_select_lang" id="btn_select_lang" value="<fmt:message key="btn.select" />" /></td>
                    </tr>
                </table>
            </form>
            <c:choose>
                <c:when test="${model.responseMsgTemplate != null}">
                    <br /><img alt="Success" src="images/success.png" class='icon-success' title="Success" />
                    <span class="error">${model.responseMsgTemplate}</span>
                </c:when>
                <c:when test="${model.errorMsgTemplate != null}">
                    <br /><img alt="Warning" src="images/warning.png" class='icon-warn' title="Warning" />
                    <span class="error">${model.errorMsgTemplate}</span>
                </c:when>
            </c:choose>
            <form action="templates.htm"  accept-charset="UTF-8" method="POST">
                <input type="hidden" name="template_type" value="library" />
                <input type="hidden" name="location_id" size="27" value="" />
                <input type="hidden" name="other_name" size="27" value="" />
                <input type="hidden" name="inc_col_code" size="27" value="false" />
                <input type="hidden" name="template_old" size="27" value="" />
                <input type="hidden" name="select_lang" value="${model.language.code}" />
                <input class="invisible" type="submit" name="btn_add_template" value="<fmt:message key="btn.add" />" />       
                <input class="invisible" type="submit" name="btn_rename_template" />
            </form>
            <c:if test="${model.language.code != null}">
                <br /><span class="pointer" id="open_add_dialog"><img src="images/add.png" class="icon-add"/><fmt:message key="label.templates.add" /></span>
            </c:if>
            <br /><br />
            <c:choose>
                <c:when test="${fn:length(model.templates) > 0}">
                    <form id="templates_form" action="templates.htm"  accept-charset="UTF-8" method="POST">
                        <input type="hidden" name="select_lang" value="${model.language.code}" />
                        <table>
                            <tr>
                                <td><fmt:message key="label.templates.list.title" /></td>
                                <td>
                                    <select name="template">
                                        <c:forEach items="${model.templates}" var="temp">
                                            <c:choose>
                                                <c:when test="${param.template eq temp.key}">
                                                    <option value="${temp.key}" selected>${temp.value}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${temp.key}">${temp.value}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input class="button" type="submit" name="btn_edit_template" value="<fmt:message key="btn.edit" />" /></td>
                                <td>
                                    <input class="button" type="button" name="rename_template" value="<fmt:message key="btn.rename" />" />                                    
                                </td>
                                <td>
                                    <input class="button" type="button" name="delete_template" value="<fmt:message key="btn.delete" />" />
                                    <input type="submit" name="btn_delete_template" class="invisible" />
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when >
                <c:otherwise>
                    <fmt:message key="label.templates.list.title.zero" />
                </c:otherwise>
            </c:choose>
            <h2><fmt:message key="title.templates.sub.scripts" /></h2>
            <c:choose>
                <c:when test="${model.responseMsgJs != null}">
                    <img alt="Success" src="images/success.png" class='icon-success' title="Success" />
                    <span class="error">${model.responseMsgJs}</span><br />
                </c:when>
                <c:when test="${model.errorMsgJs != null}">
                    <img alt="Warning" src="images/warning.png" class='icon-warn' title="Warning" />
                    <span class="error">${model.errorMsgJs}</span><br />
                </c:when>
            </c:choose>     
            <fmt:message key="label.templates.js.add" /><br /><br />
            <form action="templates.htm"  accept-charset="UTF-8" method="POST">
                <input type="text" name="js_new" size="27" value="" />.js
                <input type="hidden" name="select_lang" value="${model.language.code}" />
                <input class="button" type="submit" name="btn_add_js" value="<fmt:message key="btn.add" />" />               
            </form><br />
            <c:choose>
                <c:when test="${fn:length(model.scripts) > 0}">
                    <form id="scripts_form" action="templates.htm"  accept-charset="UTF-8" method="POST">
                        <input type="hidden" name="select_lang" value="${model.language.code}" />
                        <table>
                            <tr>
                                <td><fmt:message key="label.templates.js.list.title" /></td>
                                <td>
                                    <select name="js">
                                        <c:forEach items="${model.scripts}" var="temp">
                                            <c:choose>
                                                <c:when test="${param.js eq temp}">
                                                    <option value="${temp}" selected>${temp}</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${temp}">${temp}</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td><input class="button" type="submit" name="btn_edit_js" value="<fmt:message key="btn.edit" />" /></td>
                                <td>
                                    <input class="button" type="button" name="delete_js" value="<fmt:message key="btn.delete" />" />
                                    <input type="submit" name="btn_delete_js" class="invisible" />
                                </td>
                            </tr>
                        </table>
                    </form>
                </c:when >
                <c:otherwise>
                    <fmt:message key="label.templates.js.list.title.zero" />
                </c:otherwise>
            </c:choose>
        </div>
        <div class="locations" title="<fmt:message key="label.templates.add.title" />">
            <div id="radio">
                <input type="radio" id="radio1" name="type" value="library"/><label for="radio1"><fmt:message key="label.templates.add.radio1.label" /></label>
                <input type="radio" id="radio2" name="type" value="collection" /><label for="radio2"><fmt:message key="label.templates.add.radio2.label" /></label>
                <input type="radio" id="radio3" name="type" value="shelf" /><label for="radio3"><fmt:message key="label.templates.add.radio3.label" /></label>
                <input type="radio" id="radio4" name="type" value="not_available" /><label for="radio4"><fmt:message key="label.templates.add.radio4.label" /></label>
                <input type="radio" id="radio5" name="type" value="not_found" /><label for="radio5"><fmt:message key="label.templates.add.radio5.label" /></label>
                <input type="radio" id="radio6" name="type" value="other" /><label for="radio6"><fmt:message key="label.templates.add.radio6.label" /></label>
                <span id="loading"><img src="images/loading.gif" /></span>
            </div>    
            <div>                        
                <fmt:message key="label.templates.add.collectioncode" />
                <input type="checkbox" id="include_collection_code" /></div>
            <table>
                <tr>
                    <td><fmt:message key="label.templates.add.language" /></td>
                    <td>${model.language.name}</td>
                </tr>
                <tr>
                    <td><fmt:message key="label.templates.add.level" /></td>
                    <td><span id="template_level"></span></td>
                </tr>
                <tr>
                    <td><fmt:message key="label.templates.add.template" /></td>
                    <td>
                        <span id="template_name"></span>
                        <input type="text" name="template_other_name" class="invisible" />
                    </td>
                </tr>
            </table>
            <div class="libraries">
                <span class="popup_sub_title"><fmt:message key="label.templates.add.libraries" /></span>
            </div>
            <div class="collections">
                <span class="popup_sub_title"><fmt:message key="label.templates.add.collections" /></span>
            </div>
            <div class="shelves">
                <span class="popup_sub_title"><fmt:message key="label.templates.add.shelves" /></span>
            </div>
        </div>
        <span id="owner" class="invisible">${model.owner}</span>
    </body>
</html>
