var selectedStations = new Map();

function handleCheckBehaviour(checkboxId, selectId) {

    var select = document.getElementById(selectId);
    var checkbox = document.getElementById(checkboxId);
    if (checkbox.checked) {
        select.disabled = false;
        var dummyEvent = {};
        dummyEvent.currentTarget = select;
        handleSelectBehaviour(dummyEvent);
    } else {
        select.disabled = true;
        selectedStations.delete(select.id);
        if (checkEveryEntryEqual(selectedStations)) {
            var additionalFieldOption = getAdditionalFieldOption();
            switch (selectedStations.values().next().value) {
                case "City":
                    additionalFieldOption.innerText = "Pollution level";
                    break;
                case "Country":
                    additionalFieldOption.innerText = "Dew point";
                    break;
                case "Mountain":
                    additionalFieldOption.innerText = "Snow level";
                    break;
                case "Sea":
                    additionalFieldOption.innerText = "Uv radiation";
                    break;
            }
        } else {
            var additionalFieldOpt = document.getElementById("additionalFieldOption");
            if (typeof additionalFieldOpt !== "undefined")
                additionalFieldOpt.parentNode.removeChild(additionalFieldOpt);
        }
    }

    var selectWeatherDimension = document.getElementById("selectWeatherDimension");
    if (selectedStations.size)
        selectWeatherDimension.disabled = false;
    else selectWeatherDimension.disabled = true;
}


function handleSelectBehaviour(event) {
     var selectId = event.currentTarget.id;
     var stationType = event.currentTarget.selectedOptions[0].getAttribute("data-station-type");

     selectedStations.set(selectId, stationType);
     if (checkEveryEntryEqual(selectedStations)) {
         var additionalFieldOption = getAdditionalFieldOption();
         switch (stationType) {
             case "City":
                 additionalFieldOption.innerText = "Pollution level";
                 break;
             case "Country":
                 additionalFieldOption.innerText = "Dew point";
                 break;
             case "Mountain":
                 additionalFieldOption.innerText = "Snow level";
                 break;
             case "Sea":
                 additionalFieldOption.innerText = "Uv radiation";
                 break;
         }
     } else {
         var additionalFieldOpt = document.getElementById("additionalFieldOption");
         if (typeof additionalFieldOpt !== "undefined")
            additionalFieldOpt.parentNode.removeChild(additionalFieldOpt);
     }

}

function requestDataForGraph() {
    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    var queryString = "?";
    var checkboxes = document.querySelectorAll('[id^="checkboxForStation"]');
    var firstStat = true;
    for (var i = 0; i < checkboxes.length; i++) {
        if (checkboxes[i].checked) {
            var stationNumber = checkboxes[i].id.replace("checkboxForStation", "");
            var selectForStation = document.getElementById("selectForStation" + stationNumber);
            var idSelectedStation = selectForStation.options[selectForStation.selectedIndex].value;
            if (idSelectedStation !== selectForStation.options[0].value) {
                if (!firstStat) {
                    queryString = queryString.concat("&");
                } else {
                    firstStat = false;
                }
                queryString = queryString.concat("station", i.toString(), "=", idSelectedStation.toString());
            }

        }
    }

    var selectWeatherDimension = document.getElementById("selectWeatherDimension");
    var weatherDimension = selectWeatherDimension.options[selectWeatherDimension.selectedIndex].text
    if (weatherDimension !== selectWeatherDimension.options[0].text)
        queryString = queryString.concat("&weatherDimension=",weatherDimension.toString());

    var startDate = document.getElementById("timeFrameStartingDate").value;
    var endDate = document.getElementById("timeFrameEndingDate").value;

    if (startDate !== "" && endDate !== "") {
        queryString = queryString.concat("&startDate=", startDate.toString(), "&endDate=", endDate.toString());
    }

    var showAverage = document.getElementById("showAvgCheckbox").checked;
    if (showAverage) queryString = queryString.concat("&showAvg=", showAverage.toString());

    var showVar = document.getElementById("showVarCheckbox").checked;
    if (showVar) queryString = queryString.concat("&showVar=", showVar.toString());

    xhr.open('GET', getContextPath() + "/QueryDataGraph".concat(queryString));
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {

        }
    };
    xhr.send();
}

function checkEveryEntryEqual(map) {

    if (map.size > 0) {
        for (let [k, v] of map) {
            if (v !== map.values().next().value) return false
        }
        return true;
    } else return false;
}

function getAdditionalFieldOption() {
    var additionalFieldOption = document.getElementById("additionalFieldOption");
    if (additionalFieldOption === null) {
        additionalFieldOption = document.createElement("option");
        additionalFieldOption.setAttribute("id", "additionalFieldOption");
        document.getElementById("selectWeatherDimension").appendChild(additionalFieldOption);
    }
    return additionalFieldOption;
}


function homepageDatePickerSetup () {
    $('#divStartTimeFrame').datetimepicker({
        format: 'DD/MM/YYYY',
        useCurrent: false,
        keepOpen: true,
        allowInputToggle: true,
        ignoreReadonly: true,
        showTodayButton: true,
        viewMode: 'days'
    }).on('dp.change', function (selected) {
        var minDate = new Date(selected.date.valueOf());
        $('#divEndTimeFrame').data("DateTimePicker").minDate(minDate);
    });


    $('#divEndTimeFrame').datetimepicker({
        format: 'DD/MM/YYYY',
        useCurrent: false,
        keepOpen: true,
        allowInputToggle: true,
        ignoreReadonly: true,
        showTodayButton: true,
        viewMode: 'days'
    }).on('dp.change', function (selected) {
        var maxDate = new Date(selected.date.valueOf())
        $('#divStartTimeFrame').data("DateTimePicker").maxDate(maxDate);
    });
}