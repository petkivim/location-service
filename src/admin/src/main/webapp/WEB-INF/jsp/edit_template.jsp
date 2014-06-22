<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <link rel="stylesheet" href="scripts/CodeMirror-2.21/lib/codemirror.css" type="text/css" media="screen" />
        <script type="text/javascript" src="scripts/CodeMirror-2.21/lib/codemirror.js"></script>
        <script type="text/javascript" src="scripts/CodeMirror-2.21/mode/xml/xml.js"></script>
        <script type="text/javascript" src="scripts/CodeMirror-2.21/mode/javascript/javascript.js"></script>
        <script type="text/javascript" src="scripts/CodeMirror-2.21/mode/css/css.js"></script>
        <script type="text/javascript" src="scripts/CodeMirror-2.21/mode/htmlmixed/htmlmixed.js"></script>
        <script type="text/javascript" src="scripts/templates.js"></script>

        <title><fmt:message key="title.service" /> - <fmt:message key="title.templates.edit.template" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.templates.edit.template" /></div>
        <div id="templates_menu">
            <span  id="select_tags"><fmt:message key="label.templates.edit.template.tags" />
                <c:if test="${fn:length(model.tags) > 0}">
                    <img src="images/plus.png" class="icon-plus"/>
                </c:if>
            </span>
            <div id="available_tags">
                <ul>
                    <c:forEach var="entry" items="${model.tags}">
                        <li id="${entry.value}">${entry.key} <img src="images/add.png" class="icon-add"/></li>
                    </c:forEach>
                </ul>
            </div><br />
            <span  id="select_includes"><fmt:message key="label.templates.edit.template.includes" />
                <c:if test="${fn:length(model.includes) > 0}">
                    <img src="images/plus.png" class="icon-plus"/>
                </c:if>
            </span>
            <div id="available_includes">
                <ul>
                    <c:forEach items="${model.includes}" var="include">
                        <li id="${include}">${include} <img src="images/add.png" class="icon-add"/></li>
                    </c:forEach>
                </ul>
            </div><br />
            <span  id="select_stylesheets"><fmt:message key="label.templates.edit.template.css" />
                <c:if test="${fn:length(model.stylesheets) > 0}">
                    <img src="images/plus.png" class="icon-plus"/>
                </c:if>
            </span>
            <div id="available_stylesheets">
                <ul>
                    <li id="default"><fmt:message key="label.templates.edit.template.css.default" /> <img src="images/add.png" class="icon-add"/></li>
                    <c:forEach items="${model.stylesheets}" var="css">
                        <li id="${css}">${css} <img src="images/add.png" class="icon-add"/></li>
                    </c:forEach>
                </ul>
            </div><br />
            <span  id="select_scripts"><fmt:message key="label.templates.edit.template.js" />
                <c:if test="${fn:length(model.scripts) > 0}">
                    <img src="images/plus.png" class="icon-plus"/>
                </c:if>
            </span>
            <div id="available_scripts">
                <ul>
                    <c:forEach items="${model.scripts}" var="js">
                        <li id="${js}">${js} <img src="images/add.png" class="icon-add"/></li>
                    </c:forEach>
                </ul>
            </div><br />
            <span  id="select_scripts_plugins"><fmt:message key="label.templates.edit.template.js.plugins" />
                <c:if test="${fn:length(model.plugins) > 0}">
                    <img src="images/plus.png" class="icon-plus"/>
                </c:if>
            </span>
            <div id="available_scripts_plugins">
                <ul>
                    <c:forEach var="js" items="${model.plugins}">
                        <li id="${js}">${js} <img src="images/add.png" class="icon-add"/></li>
                    </c:forEach>
                </ul>
            </div><br />
            <span  id="select_scripts_sys"><fmt:message key="label.templates.edit.template.js.sys" />
                <c:if test="${fn:length(model.sysScripts) > 0}">
                    <img src="images/plus.png" class="icon-plus"/>
                </c:if>
            </span>
            <div id="available_scripts_sys">
                <ul>
                    <c:forEach items="${model.sysScripts}" var="js">
                        <li id="${js}">${js} <img src="images/add.png" class="icon-add"/></li>
                    </c:forEach>
                </ul>
            </div>
        </div>
        <div id="main">
            <c:if test="${model.msg != null}">
                <span class="error">${model.msg}</span><br /><br />
            </c:if>
            <form action="edittemplate.htm"  accept-charset="UTF-8" method="POST" id="form">
                <input type="hidden" name="lang" value="${model.language.code}" />
                <input type="hidden" name="template" value="${model.template}" />
                <input type="hidden" name="owner" value="${model.owner}" />
                <fieldset>
                    <legend>
                        <c:choose>
                            <c:when test="${param.sys == null}">
                                <fmt:message key="label.templates.edit.template.level" />
                                <span id="template_level">${model.templateLevel}</span> |
                                <fmt:message key="label.templates.edit.template.file" />
                                <span id="template_name">${model.templateName}</span> |
                                <fmt:message key="label.templates.edit.template.language" /> ${model.language.name}
                            </c:when>
                            <c:otherwise>
                                <fmt:message key="label.templates.edit.template.system" />
                            </c:otherwise>
                        </c:choose>
                    </legend>
                    <textarea id="contents" name="contents">${model.contents}</textarea><br />
                    <input class="button" type="submit" name="btn_save" value="<fmt:message key="btn.save" />" />
                    <input class="button" type="submit" name="btn_save_exit" value="<fmt:message key="btn.save.exit" />" />
                    <input class="button" type="submit" name="btn_back" value="<fmt:message key="btn.back" />" />
                </fieldset>
                <c:if test="${param.sys != null}">
                    <input type="hidden" name="sys" value="true" />
                </c:if>
                <input type="hidden" name="line" id="line" value="${param.line}" />
                <input type="hidden" name="ch" id="ch" value="${param.ch}" />
            </form>
        </div>
    </body>
</html>
