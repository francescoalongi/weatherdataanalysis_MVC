function generateSpinner(text) {
    var spinner = document.createElement("div");
    spinner.setAttribute("class", "spinner-border mb-3");
    spinner.setAttribute("role", "status");

    var rowSpinner = document.createElement("div");
    rowSpinner.setAttribute("class", "row");
    rowSpinner.appendChild(spinner);

    var divText = document.createElement("div");
    var strongText = document.createElement("strong");
    strongText.innerText = text;
    divText.appendChild(strongText);

    var rowDivText = document.createElement("div");
    rowDivText.setAttribute("class", "row");
    rowDivText.appendChild(divText);

    var spinnerContainer = document.createElement("div");
    spinnerContainer.setAttribute("class", "d-flex justify-content-center flex-column align-items-center");
    spinnerContainer.appendChild(rowSpinner);
    spinnerContainer.appendChild(rowDivText);

    return spinnerContainer;
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

function isOneRadioChecked(groupName) {
    var radios = document.getElementsByName(groupName);
    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked) return true;
    }
    return false;
}

function insertAlert(divToWhichAppendAlert, text){
    if (document.getElementById("divAlert") === null) {
        var divAlert = document.createElement("div");
        divAlert.setAttribute("class", "alert alert-danger text-center");
        divAlert.setAttribute("role", "alert");
        divAlert.setAttribute("id", "divAlert");
        divAlert.innerText = text;

        divToWhichAppendAlert.insertBefore(divAlert, divToWhichAppendAlert.firstChild);
        $('#divAlert').hide();
        $('#divAlert').slideDown("slow");
    } else {
        var existentDivAlert = document.getElementById("divAlert");
        existentDivAlert.innerText = text;
    }
}

function closeCurrentModal(modalId) {
    $("#"+ modalId).modal('hide');
    $('.modal').css('overflow-y', 'auto');
}


function createStationListRadio(appendTo, JSONArray) {

    var stationList = document.createElement("div");
    stationList.setAttribute("id", "stationList");
    stationList.setAttribute("class", "mb-3");
    appendTo.appendChild(stationList);

    for (var i = 0; i < JSONArray.length; i++) {
        var divRadio = document.createElement("div");
        divRadio.setAttribute("class", "custom-control custom-radio");
        var input = document.createElement("input");
        input.setAttribute("type", "radio");
        input.setAttribute("class", "custom-control-input");
        input.setAttribute("name", "idStation");
        input.setAttribute("value", JSONArray[i].idStation);
        input.setAttribute("id", "defaultGroup" + i);

        var label = document.createElement("label");
        label.setAttribute("class", "custom-control-label");
        label.setAttribute("for", "defaultGroup" + i);

        label.innerText = JSONArray[i].name;

        divRadio.appendChild(input);
        divRadio.appendChild(label);

        stationList.appendChild(divRadio);
    }

}

function fillModal(clickedObject) {

    var modalName = clickedObject.getAttribute("data-target").substring(1);
    var spinner = generateSpinner("Please wait, stations are being loaded...");
    var modalBody = document.getElementById(modalName.concat("Body"));
    modalBody.appendChild(spinner);

    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    xhr.open('GET', getContextPath() + "/LoadStations");
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            var JSONArray = JSON.parse(xhr.responseText);
            modalBody.innerText = ""; // as soon as data are ready, delete the spinner

            if (JSONArray.length === 0) { // if no stations exist in the db

                var par = document.createElement("p");
                var a = document.createElement("a");
                a.setAttribute("data-toggle", "modal");
                a.setAttribute("href", "#modalCreateStation");
                a.setAttribute("onclick", "closeCurrentModal(" + modalName + ")");
                a.innerText = "here";
                par.appendChild(document.createTextNode("No station has been created yet. Click "));
                par.appendChild(a);
                par.appendChild(document.createTextNode(" to create a new one."));
                modalBody.appendChild(par)

            } else {

                switch(modalName) {
                    case "modalRegisterStation":
                        fillModalRegisterStation(modalBody, JSONArray);
                        break;
                    case "modalDownload":
                        fillModalDownloadData(modalBody, JSONArray);
                        break;
                    case "modalUpload":
                        fillModalUploadData(modalBody, JSONArray);
                        break;
                }
            }
        }
    };
    xhr.send();
}


function modalSetup(modalName) {
    $('#'.concat(modalName)).on('hidden.bs.modal', function (e) {
        this.querySelector('#'.concat(modalName).concat("Body")).innerHTML="";
        var modalFooter = this.querySelector('#'.concat(modalName).concat("Footer"));
        if (modalFooter)
            this.querySelector('#'.concat(modalName).concat("Content")).removeChild(modalFooter);
    });
}


function getRandomColor() {
    var letters = '0123456789ABCDEF'.split('');
    var color = '#';
    for (var i = 0; i < 6; i++ ) {
        color += letters[Math.round(Math.random() * 15)];
    }
    return color;
}