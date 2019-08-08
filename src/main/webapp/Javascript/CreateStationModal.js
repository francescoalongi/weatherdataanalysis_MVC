function createStation() {
    var params = {
        name: document.getElementById('createStationName').value,
        latitude: document.getElementById('createStationLatitude').value,
        longitude: document.getElementById('createStationLongitude').value,
        altitude: document.getElementById('createStationAltitude').value,
        type: getRadioTypeChecked(),
        UOMTemperature: document.getElementById('createStationTemperature').value,
        UOMPressure: document.getElementById('createStationPressure').value,
        UOMHumidity: document.getElementById('createStationHumidity').value,
        UOMRain: document.getElementById('createStationRain').value,
        UOMWindModule: document.getElementById('createStationWindModule').value,
        UOMWindDirection: document.getElementById('createStationWindDirection').value,
        UOMAdditionalField: document.getElementById('createStationAdditionalField').value
    };

    var xhr = new XMLHttpRequest();
    xhr.open('POST', getContextPath() + "/CreateStation");
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            alert("Temporarry alert");
        }
    };
    xhr.send(JSON.stringify(params));

}

function sendAJAX() {
    if (everyInputFilled()) {
      createStation();
    } else if (document.getElementById("divAlert") === null) {
        var divCreateStationModalBody = document.getElementById("createStationModalBody");
        var divAlert = document.createElement("div");
        divAlert.setAttribute("class", "alert alert-danger text-center");
        divAlert.setAttribute("role", "alert");
        divAlert.setAttribute("id", "divAlert");
        divAlert.innerText = "It is necessary to fill in every input form!";

        divCreateStationModalBody.insertBefore(divAlert, divCreateStationModalBody.firstChild);
        $('#modalCreateStation').animate({ scrollTop: 0 }, 'slow');

    }
}

function everyInputFilled() {
    var inputList = document.querySelector('[id^="createStation"]').getElementsByTagName("input");
    for (var i = 0; i < inputList.length; i++) {
        if (inputList[i].value === "") return false;
    }
    return true;
}

function changeAdditionalFieldPlaceholder(id) {

    var radio = document.getElementById("createStationAdditionalField");
    switch (id) {
        case "radioCity":
            radio.setAttribute("placeholder", "Pollution level");
            break;
        case "radioCountry":
            radio.setAttribute("placeholder", "Dew point");
            break;
        case "radioMountain":
            radio.setAttribute("placeholder", "Snow level");
            break;
        case "radioSea":
            radio.setAttribute("placeholder", "Uv radiation level")
            break;
    }

}

function getRadioTypeChecked() {
    var radios = document.getElementsByName("groupStationType");
    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked) {
            return radios[i].id.replace("radio","");
        }
    }
}

function createStationModalSetup() {
    $('#modalCreateStation').on('hidden.bs.modal', function () {
        //if any, delete the divAlert
        var divAlert = document.getElementById("divAlert");
        if (divAlert !== null)
            divAlert.parentNode.removeChild(divAlert);
    });
}