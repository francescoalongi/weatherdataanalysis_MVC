<%--
  Created by IntelliJ IDEA.
  User: nicolo
  Date: 27/08/19
  Time: 22:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error page</title>

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
    <script type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"></script>
    <script type="text/javascript"
            src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css"
          href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"/>
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.8.7/css/mdb.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/Stylesheet.css" type="text/css">
</head>
<body>
<nav class="navbar navbar-default" style="background-color: #e3f2fd;">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">Weather data analyzer</a>
        </div>
        <div class="navbuttons">
            <ul class="nav navbar-nav navbar-right" id="ulNavBar">
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalRegisterStation"
                       onclick="fillModal(this)">Register station</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalUpload"
                       onclick="fillModal(this)">Upload data</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalDownload"
                       onclick="fillModal(this)">Download data</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalCreateStation">Create new
                        station</a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container-fluid mt-5 ml-3">
    <div class="row justify-content-center">
        <div class="col">
            <div id="displayError">
                <p><%= (String) request.getAttribute("Error") %>
                </p>
            </div>
        </div>
    </div>
</div>
</body>
</html>
