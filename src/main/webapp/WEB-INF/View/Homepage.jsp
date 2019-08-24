<%@ page import="org.codehaus.jackson.JsonNode" %>
<%@ page import="org.codehaus.jackson.map.ObjectMapper" %><%--
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

    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.4/umd/popper.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/js/bootstrap-datetimepicker.min.js"></script>
    <script src="https://cdn.plot.ly/plotly-latest.min.js"></script>

    <!-- Custom scripts -->
    <script>
        var stationListJSON = <%=request.getAttribute("stations")%>;
    </script>
    <script src="${pageContext.request.contextPath}/Javascript/UploadDataModal.js"></script>
    <script src="${pageContext.request.contextPath}/Javascript/CreateStationModal.js"></script>
    <script src="${pageContext.request.contextPath}/Javascript/HelperFunctions.js"></script>
    <script src="${pageContext.request.contextPath}/Javascript/HomepageHandler.js"></script>

    <link href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.3.1/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datetimepicker/4.17.47/css/bootstrap-datetimepicker.css">
    <link rel="stylesheet" type="text/css" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.8.7/css/mdb.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/Stylesheet/Stylesheet.css" type="text/css">
</head>
<body>

<nav class="navbar navbar-default" style="background-color: #e3f2fd;" >
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="${pageContext.request.contextPath}">Weather data analyzer</a>
        </div>
        <div class="navbuttons">
            <ul class = "nav navbar-nav navbar-right" id="ulNavBar">
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalUpload" onclick="fillUploadModal()">Upload data</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalDownload" onclick="fillDownloadModal()">Download data</a>
                </li>
                <li>
                    <a href="" class="btn btn-default" data-toggle="modal" data-target="#modalCreateStation">Create new station</a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<% if (request.getAttribute("outcomeUpload") != null) {%>
<div aria-live="polite" aria-atomic="true" style="position: relative;" >
    <div class="toast"  id="uploadDataToast" data-delay="4000">
        <div class="toast-header">
            <strong class="mr-auto">Upload data </strong>
            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="toast-body">
            <%= request.getAttribute("outcomeUpload") %>
        </div>
    </div>
</div>
<% }%>

<div aria-live="polite" aria-atomic="true" style="position: relative;" >
    <div class="toast toast-create-station" id="toastCreateStation" data-delay="4000">
        <div class="toast-header">
            <strong class="mr-auto">Create station </strong>
            <button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
        <div class="toast-body" id="toastCreateStationBody">

        </div>
    </div>
</div>


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


<div class="modal fade" id="modalDownload" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div id="downloadDataModalContent" class="modal-content">
            <div class="modal-header text-center">
                <h4 class="modal-title w-100 font-weight-bold">Download data</h4>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div id="downloadDataModalBody" class="modal-body mx-3">

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
                <button class="btn btn-indigo" onclick="checkCreateStationFieldCorrectness()">Create station</button>
            </div>
        </div>
    </div>
</div>

<div class="container-fluid mt-5 ml-3">
    <%
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree((String) request.getAttribute("stations"));
        if (jsonNode.getElements().hasNext()) {
    %>
    <div class="row">
        <div class="col">
            <div id="timeSeriesPlot">

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <p>Select a checkbox for each station you want to display and then select the station</p>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <div id="containerSelects">

            </div>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <p>Select the weather dimension</p>
        </div>
    </div>
    <div class="row mb-2">
        <div class="col-md-auto">
            <select id="selectWeatherDimension" class="browser-default custom-select" disabled>
                <option selected>Select the weather dimension</option>
                <option>Temperature</option>
                <option>Pressure</option>
                <option>Humidity</option>
                <option>Rain</option>
                <option>Wind module</option>
                <option>Wind direction</option>
            </select>
        </div>
    </div>

    <div class="row">
        <div class="col">
            <p>Select the time frame</p>
        </div>
    </div>

   <div class="row mb-3">
       <div class="col-md-auto">
           <div class="input-group date startdate" id="divStartTimeFrame">
               <input placeholder="Starting date" type="text" id="timeFrameStartingDate" class="form-control">
           </div>
       </div>
       <div class="col-md-auto">
           <div class="input-group date enddate" id="divEndTimeFrame">
               <input placeholder="Ending date" type="text" id="timeFrameEndingDate" class="form-control">
           </div>
       </div>
   </div>

    <div class="row mb-3">
        <div class="col-md-auto">
            <div class="custom-control custom-checkbox custom-control-inline">
                <input type="checkbox" class="custom-control-input" id="showAvgCheckbox">
                <label class="custom-control-label" for="showAvgCheckbox">Show average</label>
            </div>
        </div>
        <div class="col-md-auto">
            <div class="custom-control custom-checkbox custom-control-inline">
                <input type="checkbox" class="custom-control-input" id="showVarCheckbox">
                <label class="custom-control-label" for="showVarCheckbox">Show variance</label>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-auto">
            <button type="button" class="btn btn-primary" onclick="requestDataForGraph()">Show graph</button>
        </div>
    </div>
    <%
        } else {
    %>
    <div class="row">
        <div class="col">
            <p>No stations has been created yet. Click <a data-toggle="modal" href="#modalCreateStation">here</a> to create a new one.</p>
        </div>
    </div>
    <%
        }
    %>
</div>

<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mdbootstrap/4.8.7/js/mdb.min.js"></script>
<script src="${pageContext.request.contextPath}/Javascript/DownloadDataModal.js"></script>
<script src="${pageContext.request.contextPath}/Javascript/WindowSetUp.js"></script>
</body>
</html>
