function fillModalRegisterStation(modalBody, JSONArray) {
    var p = document.createElement("p");
    p.innerText = "Choose the station you want to register";
    modalBody.appendChild(p);

    createStationListRadio(modalBody, JSONArray);

    var urlDescription = document.createElement("p");
    urlDescription.innerText = "Type the url from where your data must be downloaded";
    modalBody.appendChild(urlDescription);

    var urlInput = document.createElement("input");
    urlInput.setAttribute("type", "text");
    urlInput.setAttribute("id", "registerStationUrl");
    urlInput.setAttribute("class", "form-control mb-3");
    urlInput.setAttribute("placeholder", "URL");
    modalBody.appendChild(urlInput);

    var timeIntervalDescription = document.createElement("p");
    timeIntervalDescription.innerText = "Type the time interval (in minutes) between subsequent update of the file located in the above URL";
    modalBody.appendChild(timeIntervalDescription);

    var timeIntervalInput = document.createElement("input");
    timeIntervalInput.setAttribute("type", "text");
    timeIntervalInput.setAttribute("id", "registerStationTimeInterval");
    timeIntervalInput.setAttribute("class", "form-control mb-3");
    timeIntervalInput.setAttribute("placeholder", "Time interval");
    modalBody.appendChild(timeIntervalInput);


    var modalFooter = document.createElement("div");
    modalFooter.setAttribute("class", "modal-footer d-flex justify-content-center");
    modalFooter.setAttribute("id", "modalRegisterStationFooter");
    var downloadButton = document.createElement("button");
    downloadButton.setAttribute("class", "btn btn-indigo");
    downloadButton.setAttribute("onclick", "registerStation()");
    downloadButton.innerText = "Register station";
    modalFooter.appendChild(downloadButton);
    modalBody.parentNode.appendChild(modalFooter);
}


function registerStation() {

    if (isOneRadioChecked("idStation") && document.getElementById("registerStationUrl").value && document.getElementById("registerStationTimeInterval").value) {

        var stationId;
        var radios = document.getElementsByName("idStation");
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) stationId = radios[i].value;
        }

        var params = {
            idStation: stationId,
            URL: document.getElementById('registerStationUrl').value,
            timeInterval: document.getElementById('registerStationTimeInterval').value,
        };

        var xhr = new XMLHttpRequest();
        xhr.open('POST', getContextPath() + "/RegisterStation");
        xhr.setRequestHeader('Content-type', 'application/json');
        xhr.onreadystatechange = function () {
            if (xhr.readyState > 3 && xhr.status === 200) {
                var JSONResponse = JSON.parse(xhr.responseText);
                if (JSONResponse.success === "true") {
                    $('#modalRegisterStation').modal('hide');
                    document.getElementById("toastRegisterStationBody").innerHTML = JSONResponse.text;
                    $('#toastRegisterStation').toast('show');
                } else {
                    var modalRegisterStationBody = document.getElementById("modalRegisterStationBody");
                    insertAlert(modalRegisterStationBody, JSONResponse.text);
                    $('#modalRegisterStation').animate({scrollTop: 0}, 'slow');
                }
            }
        };
        xhr.send(JSON.stringify(params));
    } else {
        var modalRegisterStationBody = document.getElementById("modalRegisterStationBody");
        insertAlert(modalRegisterStationBody, "Every field must be filled in!");
        $('#modalRegisterStation').animate({scrollTop: 0}, 'slow');
    }

}