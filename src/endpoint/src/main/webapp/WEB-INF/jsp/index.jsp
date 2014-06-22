<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%-- Prevent the creation of a session --%>
<%@ page session="false" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Paikannuspalvelu</title>
    </head>
    <body>
        <h1>Paikannuspalvelu</h1>
        <p>Aloitussivu.</p>
        <p><a href="LocationHandler?callno=P1&lang=fi&owner=FI-TEST">P1</a></p>
        <p><a href="LocationHandler?callno=P1 B&lang=fi&owner=FI-TEST">P1 B</a></p>
        <p><a href="LocationHandler?callno=P1 B 001 Kotler&status=0&lang=fi&owner=FI-TEST">P1 B 001 Kotler</a></p>
        <p><a href="LocationHandler?callno=P1 Ty%C3%B6 001 Kotler&status=0&lang=fi&owner=FI-TEST">P1 Ty&ouml; 001 Kotler</a></p>
        <p><a href="LocationHandler?callno=P1 Ty%F6 001 Kotler&status=0&lang=fi&owner=FI-TEST">P1 Ty&ouml; 001 Kotler</a></p>
        <p><a href="LocationHandler?callno=P1 Sak Adams&lang=fi&owner=FI-TEST">P1 Sak Adams</a></p>
        <p><a href="LocationHandler?callno=P1 Ref 001 Kotler&status=0&lang=fi&owner=FI-TEST">P1 Ref 001 Kotler</a></p>
        <p><a href="LocationHandler?callno=P1 Ref 043 Kotler&status=0&lang=fi&owner=FI-TEST">P1 Ref 043 Kotler</a></p>
        <p><a href="LocationHandler?callno=P1 TE ET KO OK Kotler&status=0&lang=fi&owner=FI-TEST">P1 TE ET KO OK</a></p>
        <p><a href="Exporter?owner=FI-TEST&type=callno&children=yes&search=P1&position=any">Exporter</a>
        <p><a href="LocationHandler?callno=P1 B 33-65&status=0&lang=fi&owner=FI-TEST">P1 B 33-65</a></p>
        <p><a href="LocationHandler?callno=P1 B 33%3A65&status=0&lang=fi&owner=FI-TEST">P1 B 33:65</a></p>
        <p><a href="LocationHandler?callno=P1 B 33%2F65&status=0&lang=fi&owner=FI-TEST">P1 B 33/65</a></p>
        <p><a href="LocationHandler?callno=P1 B 33.65&status=0&lang=fi&owner=FI-TEST">P1 B 33.65</a></p>
        <p><a href="LocationHandler?callno=P1 B 33%2665&status=0&lang=fi&owner=FI-TEST">P1 B 33&65</a></p>
        <p><a href="LocationHandler?callno=P1 B 33%2B65&status=0&lang=fi&owner=FI-TEST">P1 B 33+65</a></p>
        <p><a href="LocationHandler?callno=P1 B 33.3%2B65&status=0&lang=fi&owner=FI-TEST">P1 B 33.3+65</a></p>
    </body>
</html>
