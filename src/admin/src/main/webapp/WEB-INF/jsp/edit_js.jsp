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
        <script type="text/javascript" src="scripts/CodeMirror-2.21/mode/javascript/javascript.js"></script>
        <script type="text/javascript" src="scripts/scripts.js"></script>

        <title><fmt:message key="title.service" /> - <fmt:message key="title.templates.edit.js" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="title.templates.edit.js" /></div>
        <div id="main">
            <form action="editjs.htm"  accept-charset="UTF-8" method="POST" id="form">
                <input type="hidden" name="lang" value="${model.language}" />
                <input type="hidden" name="js" value="${model.js}" />
                <fieldset>
                    <legend><fmt:message key="label.templates.edit.js.file" /> ${model.js}</legend>
                    <textarea id="contents" name="contents">${model.contents}</textarea><br />
                    <input class="button" type="submit" name="btn_save" value="<fmt:message key="btn.save" />" />
                    <input class="button" type="submit" name="btn_save_exit" value="<fmt:message key="btn.save.exit" />" />
                    <input class="button" type="submit" name="btn_back" value="<fmt:message key="btn.back" />" />
                </fieldset>
                <input type="hidden" name="line" id="line" value="${param.line}" />
                <input type="hidden" name="ch" id="ch" value="${param.ch}" />
            </form>
        </div>
    </body>
</html>
