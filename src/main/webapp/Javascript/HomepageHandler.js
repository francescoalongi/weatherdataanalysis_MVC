var selectedStationsType = new Map();
var selectedStationsId = [];

function handleSelectBehaviour(event) {
    var selectId = event.currentTarget.id;
    var stationType = event.currentTarget.selectedOptions[0].getAttribute("data-station-type");

    if (event.currentTarget.selectedOptions[0].innerText === event.currentTarget.options[0].innerText)
        selectedStationsType.delete(selectId);
    else selectedStationsType.set(selectId, stationType);
    if (checkEveryEntryEqual(selectedStationsType)) {
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
        if (additionalFieldOpt)
            additionalFieldOpt.parentNode.removeChild(additionalFieldOpt);
    }

    var selectForStation = $('#' + selectId.toString());
    var previousSelection = selectForStation.data('pre');
    if (previousSelection !== "none") {
        removeElementFromArray(selectedStationsId, previousSelection)
    }

    if (selectForStation.val() !== "none") {
        selectedStationsId.push(selectForStation.val());
    } else {
        removeElementFromArray(selectedStationsId, previousSelection)
    }
    selectForStation.data('pre', selectForStation.val());

    var selects = document.querySelectorAll('[id^="selectForStation"]');
    for (var i = 0; i < selects.length; i++) {
        for (var j = 0; j < selects[i].options.length; j++) {
            if (selectedStationsId.includes(selects[i].options[j].value)) selects[i].options[j].disabled = true;
            else selects[i].options[j].disabled = false;
        }
    }

    var selectWeatherDimension = document.getElementById("selectWeatherDimension");
    if (selectedStationsType.size)
        selectWeatherDimension.disabled = false;
    else selectWeatherDimension.disabled = true;

}

function removeElementFromArray(array, elementToBeRemoved) {
    var index = array.indexOf(elementToBeRemoved);
    if (index > -1) {
        array.splice(index, 1);
    }
}

function requestDataForGraph() {
    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    var queryString = "?";
    var selects = document.querySelectorAll('[id^="selectForStation"]');
    var firstPar = true;
    for (var i = 0, j = 0; i < selects.length; i++) {
        if (selects[i].selectedOptions[0].value !== selects[i].options[0].value) {
            var idSelectedStation = selects[i].options[selects[i].selectedIndex].value;
                if (!firstPar) {
                    queryString = queryString.concat("&");
                } else {
                    firstPar = false;
                }
                queryString = queryString.concat("station", j.toString(), "=", idSelectedStation.toString());
                j++;
        }
    }

    var selectWeatherDimension = document.getElementById("selectWeatherDimension");
    var weatherDimension = selectWeatherDimension.options[selectWeatherDimension.selectedIndex].text
    if (weatherDimension !== selectWeatherDimension.options[0].text) {
        if (!firstPar) {
            queryString = queryString.concat("&");
        } else {
            firstPar = false;
        }
        queryString = queryString.concat("weatherDimension=",weatherDimension.toString());
    }

    var startDate = document.getElementById("timeFrameStartingDate").value;
    var endDate = document.getElementById("timeFrameEndingDate").value;

    if (startDate !== "" && endDate !== "") {
        if (!firstPar) {
            queryString = queryString.concat("&");
        } else {
            firstPar = false;
        }
        queryString = queryString.concat("startDate=", startDate.toString(), "&endDate=", endDate.toString());
    }

    var showAverage = document.getElementById("showAvgCheckbox").checked;
    if (showAverage) {
        if (!firstPar) {
            queryString = queryString.concat("&");
        } else {
            firstPar = false;
        }
        queryString = queryString.concat("showAvg=", showAverage.toString());
    }

    var showVar = document.getElementById("showVarCheckbox").checked;
    if (showVar) {
        if (!firstPar) {
            queryString = queryString.concat("&");
        }
        queryString = queryString.concat("showVar=", showVar.toString());
    }

    xhr.open('GET', getContextPath() + "/QueryDataGraph".concat(queryString));
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            // code for building the graph

            //sample code to test the animation
            var plotDiv = document.getElementById("plot");
            plotDiv.style.backgroundColor = "red";
            plotDiv.style.height = "300px";

            $('#plot').slideDown("slow");

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


function homepageSetup () {
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
        var maxDate = new Date(selected.date.valueOf());
        $('#divStartTimeFrame').data("DateTimePicker").maxDate(maxDate);
    });

    $('#plot').hide();

    appendSelectToContainer();

}


var stationNumber = 0;
function appendSelectToContainer () {

    $('#buttonAdd' + (stationNumber-1).toString()).hide("fast");
    if (stationNumber-1 !== 0)
        $('#buttonRemove' + (stationNumber-1).toString()).hide("fast");

    var divSelect = document.createElement("div");
    divSelect.setAttribute("id", "divSelect" + stationNumber.toString());
    divSelect.setAttribute("class", "row justify-content-start mb-2");

    document.getElementById("containerSelects").appendChild(divSelect);

    var divColSelect = document.createElement("div");
    divColSelect.setAttribute("class", "col-md-auto");

    divSelect.appendChild(divColSelect);

    var selectStation = document.createElement("select");
    selectStation.setAttribute("id", "selectForStation" + stationNumber.toString());
    selectStation.setAttribute("class", "browser-default custom-select");
    selectStation.setAttribute("onchange", "handleSelectBehaviour(event)");

    divColSelect.appendChild(selectStation);

    var defaultOption = document.createElement("option");
    defaultOption.setAttribute("value", "none")
    defaultOption.selected = true;
    defaultOption.innerText = "Select the station you want to display";

    selectStation.appendChild(defaultOption);


    for (var i = 0; i < stationListJSON.length; i++) {
        var option = document.createElement("option");
        option.setAttribute("value", stationListJSON[i].idStation);
        option.setAttribute("data-station-type", stationListJSON[i].type);
        option.innerText = stationListJSON[i].name;
        selectStation.appendChild(option);
    }

    $(document).ready(function(){
        var selectForStation = $('#selectForStation' + stationNumber.toString());
        selectForStation.data('pre', selectForStation.val());

        selectForStation.find('option').each(function (index, element) {
            if (selectedStationsId.includes(element.value)) element.disabled = true;
            else element.disabled = false;
        })

        /*for (var i = 0; i < selectForStation.options.length; i++) {
            if (selectedStationsId.includes(selectForStation.options[j].value)) selectForStation.options[j].disabled = true;
            else selectForStation.options[j].disabled = false;
        }*/
    });

    var divColButtons = document.createElement("div");
    divColButtons.setAttribute("class", "col-md-auto");

    divSelect.appendChild(divColButtons);

    var buttonAdd = document.createElement("button");
    buttonAdd.setAttribute("type", "button");
    buttonAdd.setAttribute("id", "buttonAdd" + stationNumber.toString());
    buttonAdd.setAttribute("class", "btn btn-primary btn-sm px-3");
    buttonAdd.setAttribute("onclick", "appendSelectToContainer()");

    divColButtons.appendChild(buttonAdd);

    var iconAdd = document.createElement("i");
    iconAdd.setAttribute("class", "fas fa-plus");

    buttonAdd.appendChild(iconAdd);

    if (stationNumber !== 0) {

        var buttonRemove = document.createElement("button");
        buttonRemove.setAttribute("type", "button");
        buttonRemove.setAttribute("id", "buttonRemove" + stationNumber.toString());
        buttonRemove.setAttribute("class", "btn btn-primary btn-sm px-3");
        buttonRemove.setAttribute("onclick", "removeSelectFromContainer()");

        divColButtons.appendChild(buttonRemove);

        var iconRemove = document.createElement("i");
        iconRemove.setAttribute("class", "fas fa-times");

        buttonRemove.appendChild(iconRemove);
    }

    $(divSelect).hide();
    $(divSelect).slideDown("slow");
    stationNumber++;
}

function removeSelectFromContainer() {

    $('#buttonAdd' + (stationNumber-2).toString()).show("fast");
    if (stationNumber-2 !== 0)
        $('#buttonRemove' + (stationNumber-2).toString()).show("fast");

    var selectForStation = $('#selectForStation' + (stationNumber-1).toString());
    var previousSelection = selectForStation.data('pre');
    removeElementFromArray(selectedStationsId, previousSelection);

    var selects = document.querySelectorAll('[id^="selectForStation"]');
    for (var i = 0; i < selects.length; i++) {
        for (var j = 0; j < selects[i].options.length; j++) {
            if (selectedStationsId.includes(selects[i].options[j].value)) selects[i].options[j].disabled = true;
            else selects[i].options[j].disabled = false;
        }
    }

    var containerSelects = document.getElementById("containerSelects");
    $(containerSelects.lastChild).slideUp("slow", function() {
        $(this).remove();
        // it is better to decrease the counter once the div was ACTUALLY removed
        stationNumber--;
    });



}