function createStation() {
    var params = {
        name: document.getElementById('createStationName').value,
        latitude: document.getElementById('createStationLatitude').value,
        longitude: document.getElementById('createStationLongitude').value,
        altitude: document.getElementById('createStationAltitude').value,
        type: getRadioTypeChecked(),
        unitOfMeasure: {
            temperature: document.getElementById('createStationTemperature').value,
            pressure: document.getElementById('createStationPressure').value,
            humidity: document.getElementById('createStationHumidity').value,
            rain: document.getElementById('createStationRain').value,
            windModule: document.getElementById('createStationWindModule').value,
            windDirection: document.getElementById('createStationWindDirection').value,
        }
    };

    params = addAdditionalField(params);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', getContextPath() + "/CreateStation");
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            var JSONResponse = JSON.parse(xhr.responseText);
            //TODO: handle this check at the client side also
            if (JSONResponse.success === "true") {
                $('#modalCreateStation').modal('hide');
                document.getElementById("toastCreateStationBody").innerText = JSONResponse.text;
                $('#toastCreateStation').toast('show');
            } else {
                var createStationModalBody = document.getElementById("createStationModalBody")
                insertAlert(createStationModalBody, JSONResponse.text);
                $('#modalCreateStation').animate({ scrollTop: 0 }, 'slow')
                document.getElementById("createStationLatitude").classList.add("is-invalid");
                document.getElementById("createStationLongitude").classList.add("is-invalid");
                document.getElementById("createStationAltitude").classList.add("is-invalid");
            }
        }
    };
    xhr.send(JSON.stringify(params));
}

function addAdditionalField(JSONObject) {

    var additionalField = document.getElementById('createStationAdditionalField').value;

    switch (getRadioTypeChecked()) {
        case "City":
            JSONObject.unitOfMeasure.pollutionLevel = additionalField;
            break;
        case "Country":
            JSONObject.unitOfMeasure.dewPt = additionalField;
            break;
        case "Mountain":
            JSONObject.unitOfMeasure.snowLevel = additionalField;
            break;
        case "Sea":
            JSONObject.unitOfMeasure.uvRadiation = additionalField;
            break;
    }

    return JSONObject;
}

function sendAJAX() {
    var divCreateStationModalBody = document.getElementById("createStationModalBody");
    if (checkEveryInputFilled()) {
        if (checkLatLongAltFloat()) {
            createStation();
        } else {
            insertAlert(divCreateStationModalBody, "Latitude, Longitude and Altitude must be numbers!");
            $('#modalCreateStation').animate({ scrollTop: 0 }, 'slow');
            document.getElementById("createStationLatitude").classList.add("is-invalid");
            document.getElementById("createStationLongitude").classList.add("is-invalid");
            document.getElementById("createStationAltitude").classList.add("is-invalid");
        }
    } else{
        insertAlert(divCreateStationModalBody, "It is necessary to fill in every input form!");
        $('#modalCreateStation').animate({ scrollTop: 0 }, 'slow');
    }
}

function checkLatLongAltFloat() {

}
function checkEveryInputFilled() {
    var inputList = document.querySelector('[id^="createStation"]').getElementsByTagName("input");
    for (var i = 0; i < inputList.length; i++) {
        if (inputList[i].type === "text" && inputList[i].value === "") return false;
    }
    return true;
}

function emptyInputForms() {
    var inputList = document.querySelector('[id^="createStation"]').getElementsByTagName("input");
    for (var i = 0; i < inputList.length; i++) {
        inputList[i].value = ""
    }
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

        emptyInputForms();

        document.getElementById("createStationLatitude").classList.remove("is-invalid");
        document.getElementById("createStationLongitude").classList.remove("is-invalid");
        document.getElementById("createStationAltitude").classList.remove("is-invalid");

        //if any, delete the divAlert
        var divAlert = document.getElementById("divAlert");
        if (divAlert !== null)
            divAlert.parentNode.removeChild(divAlert);
    });
}