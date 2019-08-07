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
    <!-- Custom scripts -->
    <script src="${pageContext.request.contextPath}/Javascript/UploadDataModal.js"></script>
    <script src="${pageContext.request.contextPath}/Javascript/CreateStationModal.js"></script>

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
<body onload="uploadDataModalSetup()">

<nav class="navbar navbar-default" style="background-color: #e3f2fd;" >
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">Weather data analyzer</a>
        </div>
        <div class="navbuttons">
            <ul class = "nav navbar-nav navbar-right" id="ulNavBar">
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalUpload" onclick="retrieveStations()">Upload data</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modal">Download data</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalCreateStation">Create new station</a>
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
            <div id="uploadDataModalBody" class="modal-body mx-3">

            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="modalCreateStation" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div id="createStationModalContent" class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title w-100 font-weight-bold">Create station</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="createStationModalBody" class="modal-body mx-3">
                <p>Insert below the station's details</p>
                <input type="text" id="createStationName" class="form-control mb-3" placeholder="Name">
                <div class="form-row">
                    <div class="col">
                        <input type="text" id="createStationLatitude" class="form-control mb-3" placeholder="Latitude">
                    </div>
                    <div class="col">
                        <input type="text" id="createStationLongitude" class="form-control mb-3" placeholder="Longitude">
                    </div>
                </div>
                <input type="text" id="createStationAltitude" class="form-control mb-3" placeholder="Altitude">

                <p>Station's type:</p>

                <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" class="custom-control-input" id="radioCity" name="groupStationType" onclick="changeAdditionalFieldPlaceholder(this.id)" checked>
                    <label class="custom-control-label" for="radioCity">City</label>
                </div>
                <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" class="custom-control-input" id="radioCountry" name="groupStationType" onclick="changeAdditionalFieldPlaceholder(this.id)">
                    <label class="custom-control-label" for="radioCountry">Country</label>
                </div>
                <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" class="custom-control-input" id="radioMountain" name="groupStationType" onclick="changeAdditionalFieldPlaceholder(this.id)">
                    <label class="custom-control-label" for="radioMountain">Mountain</label>
                </div>
                <div class="custom-control custom-radio custom-control-inline">
                    <input type="radio" class="custom-control-input" id="radioSea" name="groupStationType" onclick="changeAdditionalFieldPlaceholder(this.id)">
                    <label class="custom-control-label" for="radioSea">Sea</label>
                </div>

                <p class="mt-3">Insert the unit of measurement through which the station will provide new data</p>

                <input type="text" id="createStationTemperature" class="form-control mb-3" placeholder="Temperature">
                <input type="text" id="createStationPressure" class="form-control mb-3" placeholder="Pressure">
                <input type="text" id="createStationHumidity" class="form-control mb-3" placeholder="Humidity">
                <input type="text" id="createStationRain" class="form-control mb-3" placeholder="Rain">
                <div class="form-row">
                    <div class="col">
                        <input type="text" id="createStationWindModule" class="form-control mb-3" placeholder="Wind module">
                    </div>
                    <div class="col">
                        <input type="text" id="createStationWindDirection" class="form-control mb-3" placeholder="Wind direction">
                    </div>
                </div>
                <input type="text" id="createStationAdditionalField" class="form-control mb-3" placeholder="Pollution level">
            </div>
            <div class="modal-footer d-flex justify-content-center">
                <button class="btn btn-indigo" onclick="createStation(event)">Create station</button>
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
