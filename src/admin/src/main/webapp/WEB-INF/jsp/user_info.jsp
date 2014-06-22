<%@ include file="include.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.w3.org/MarkUp/SCHEMA/xhtml11.xsd" xml:lang="fi">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <jsp:include page="include_jquery_ui.jsp" />
        <link rel="stylesheet" href="css/style.css" type="text/css" media="screen" />
        <link rel="stylesheet" media="screen" type="text/css" href="scripts/colorpicker/colorpicker.css" />  
        <script type="text/javascript" src="scripts/colorpicker/colorpicker.js"></script>
        <script type="text/javascript" src="scripts/init_colorpicker.js"></script>
        <script type="text/javascript" src="scripts/user_info.js"></script>

        <title><fmt:message key="title.service" /> - <fmt:message key="title.userinfo" /></title>
    </head>
    <body>
        <jsp:include page="main_menu.jsp" />
        <div id="page_title"><fmt:message key="menu.userinfo" /></div>
        <div id="main">
            <div class="history">
                <fmt:message key="title.created" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${model.user.created}" /> (${model.user.creator}) |
                <fmt:message key="title.updated" /> <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${model.user.updated}" /> (${model.user.updater})
            </div>
            <c:if test = "${model.responseMsg != null}">
                <span class="error">
                    <img alt="Success" src="images/success.png" class='icon-success' title="Success" />
                    ${model.responseMsg}
                </span><br /><br />
            </c:if>
            <h2><fmt:message key="label.userinfo.subtitle" /></h2>
            <form action="userinfo.htm"  accept-charset="UTF-8" method="POST">
                <table id="userinfo_table">
                    <tr>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.firstname" /></td>
                        <td class="userinfo_table_col2">${model.user.firstName}</td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.lastname" /></td>
                        <td class="userinfo_table_col2">${model.user.lastName}</td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.username" /></td>
                        <td class="userinfo_table_col2">${model.user.username}</td>
                        <td class="add_location_table_col3"></td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.password.old" /></td>
                        <td class="userinfo_table_col2"><input type="password" name="password_old" /></td>
                        <td class="userinfo_table_col3">*
                            <c:if test = "${model.errorMsgPwOld != null}">
                                <span class="error">${model.errorMsgPwOld}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.password.new" /></td>
                        <td class="userinfo_table_col2"><input type="password" name="password_new" /></td>
                        <td class="userinfo_table_col3">*
                            <c:if test = "${model.errorMsgPwNew != null}">
                                <span class="error">${model.errorMsgPwNew}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.password.control" /></td>
                        <td class="userinfo_table_col2"><input type="password" name="password_new_control" /></td>
                        <td class="userinfo_table_col3">*
                            <c:if test = "${model.errorMsgPwCtrl != null}">
                                <span class="error">${model.errorMsgPwCtrl}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"></td>
                        <td class="userinfo_table_col2">
                            <input class="button" type="submit" name="save" value="<fmt:message key="btn.save" />" />
                        </td>
                        <td class="userinfo_table_col3"></td>
                    </tr>
                </table>
            </form>
            <br />
            <form action="userinfo.htm"  accept-charset="UTF-8" method="POST">
                <table>
                    <tr>
                        <c:choose>
                            <c:when test="${param.email != null}">
                                <c:set var="email" value="${param.email}" scope="request" />
                            </c:when>
                            <c:otherwise>
                                <c:set var="email" value="${model.user.email}" scope="request" />
                            </c:otherwise>
                        </c:choose>
                        <td class="userinfo_table_col1"><fmt:message key="label.userinfo.email" /></td>
                        <td class="userinfo_table_col2"><input type="text" id="userinfo_input_email" name="email" value="${email}" /></td>
                        <td class="userinfo_table_col3">*
                            <c:if test = "${model.errorMsgEmail != null}">
                                <span class="error">${model.errorMsgEmail}</span>
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="userinfo_table_col1"></td>
                        <td class="userinfo_table_col2">
                            <input class="button" type="submit" name="save_email" value="<fmt:message key="btn.save" />" />
                        </td>
                        <td class="userinfo_table_col3"></td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>
