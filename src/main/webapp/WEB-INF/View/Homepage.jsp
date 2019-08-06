<%--
  Created by IntelliJ IDEA.
  User: Francesco Alongi
  Date: 25/07/2019
  Time: 10:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false"%>
<html>
<head>
    <title>Homepage</title>
    <script src="${pageContext.request.contextPath}/Javascript/UploadDataModal.js"></script>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <!-- Bootstrap core CSS -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <!-- Material Design Bootstrap -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.8.7/css/mdb.min.css" rel="stylesheet">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/Stylesheet.css" type="text/css">

    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
    <!-- Bootstrap tooltips -->
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"></script>
    <!-- Bootstrap core JavaScript -->
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
</head>
<body onload="modalSetup()">

<nav class="navbar navbar-default" style="background-color: #e3f2fd;" >
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">Weather data analyzer</a>
        </div>
        <div class="navbuttons">
            <ul class = "nav navbar-nav navbar-right" id="ulNavBar">
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalUpload" onclick="retrieveStations()">Upload data</a>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modal">Download data</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="modal fade" id="modalUpload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div id="uploadDataModalContent" class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title w-100 font-weight-bold">Upload data</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="modalBody" class="modal-body mx-3">

            </div>
        </div>
    </div>
</div>



<div class="container">
    <div class="row">
        <div class="col">
            <h1>Hi</h1>
        </div>
    </div>
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.8.7/js/mdb.min.js"></script>
</body>
</html>
