<%@page import="com.pkrete.locationservice.admin.model.user.UserGroup"%>
<%@ include file="include.jsp" %>
<div class="navi">
    <table>
        <tr>
            <td class="navi_left">
                <span class="menu_item"><a href="index.htm"><fmt:message key="menu.main" /></a></span>
                <span class="menu_item"><a href="locations.htm"><fmt:message key="menu.locations" /></a></span>
                <span class="menu_item"><a href="illustrations.htm"><fmt:message key="menu.illustrations" /></a></span>
                <span class="menu_item"><a href="languages.htm"><fmt:message key="menu.languages" /></a></span>
                <span class="menu_item"><a href="templates.htm"><fmt:message key="menu.templates" /></a></span>
                <span class="menu_item"><a href="subjectmatters.htm"><fmt:message key="menu.subjectmatters" /></a></span>               
            </td>
            <td class="navi_right">
                <span class="menu_item"><a href="statistics.htm"><fmt:message key="menu.statistics" /></a></span>
                <% if (request.isUserInRole(UserGroup.ADMIN.toString())) {%>
                    <span class="menu_item"><a href="userowner.htm"><fmt:message key="menu.users" /></a></span>
                <% } else {%>
                    <span class="menu_item"><a href="editsettings.htm"><fmt:message key="menu.settings" /></a></span>
                    <span class="menu_item"><a href="userinfo.htm"><fmt:message key="menu.userinfo" /></a></span>
                    <% if (request.isUserInRole(UserGroup.LOCAL_ADMIN.toString())) {%>
                        <span class="menu_item"><a href="ladmuser.htm"><fmt:message key="menu.users.ladm" /></a></span>
                    <% }%>
                <% }%>
                <span class="menu_item"><a href="logout.htm"><fmt:message key="menu.logout" /></a></span>
            </td>
        </tr>
    </table>
</div>