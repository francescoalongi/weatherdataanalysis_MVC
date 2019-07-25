<%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 25/07/2019
  Time: 09:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/Stylesheet.css" type="text/css">
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<nav class="navbar navbar-default" style="background-color: #e3f2fd;">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">Weather data analyzer</a>
        </div>

        <ul class = "nav navbar-nav navbar-right" id="ulNavBar">
            <li><span class="glyphicon glyphicon-menu-hamburger"></span></li>
        </ul>
    </div>
</nav>