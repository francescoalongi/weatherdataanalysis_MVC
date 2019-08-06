
function retrieveStations() {

    //First of all create and append a spinner, it will be deleted when the AJAX response will get to the client
    var spinner = document.createElement("div");
    spinner.setAttribute("class", "spinner-border");
    spinner.setAttribute("role", "status");

    var rowSpinner = document.createElement("div");
    rowSpinner.setAttribute("class", "row");
    rowSpinner.appendChild(spinner);

    var divText = document.createElement("div");
    var strongText = document.createElement("strong");
    strongText.innerText = "Please wait, stations are being loaded...";
    divText.appendChild(strongText);

    var rowDivText = document.createElement("div");
    rowDivText.setAttribute("class", "row");
    rowDivText.appendChild(divText);

    var spinnerContainer = document.createElement("div");
    spinnerContainer.setAttribute("class", "d-flex justify-content-center flex-column align-items-center");
    spinnerContainer.appendChild(rowSpinner);
    spinnerContainer.appendChild(rowDivText);



    var modalBody = document.getElementById("modalBody");
    modalBody.appendChild(spinnerContainer);

    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    xhr.open('GET', getContextPath() + "/LoadStations");
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            var JSONArray = JSON.parse(xhr.responseText);
            modalBody.innerText = ""; // as soon as data are ready, delete the spinner

            if (JSONArray.length === 0) { // if no stations exist in the db

                var par = document.createElement("p");
                var a = document.createElement("a");
                a.setAttribute("data-toggle", "modal");
                a.setAttribute("href", "#createStationModal");
                a.innerText = "here";
                par.appendChild(document.createTextNode("No station has been created yet. Click "));
                par.appendChild(a);
                par.appendChild(document.createTextNode(" to create a new one."));
                modalBody.appendChild(par)

            } else {

                if (modalBody.getElementsByTagName("P").length === 0) {
                    var p = document.createElement("p");
                    p.innerText = "Choose the station to which your data belongs and then choose the dataset you want to upload";
                    //modalBody.insertBefore(p, modalBody.firstChild);
                    modalBody.appendChild(p);
                }

                var stationList = document.createElement("div");
                stationList.setAttribute("id", "stationList");
                modalBody.appendChild(stationList);

                var form = document.createElement("form");
                form.setAttribute("action", getContextPath() + "/UploadData");
                form.setAttribute("method", "POST");
                form.setAttribute("enctype", "multipart/form-data");
                form.setAttribute("id", "uploadDataForm");

                stationList.appendChild(form);

                for (var i = 0; i < JSONArray.length; i++) {
                    var div = document.createElement("div");
                    div.setAttribute("class", "custom-control custom-radio")
                    var input = document.createElement("input");
                    input.setAttribute("type", "radio");
                    input.setAttribute("class", "custom-control-input");
                    input.setAttribute("name", "radios");
                    input.setAttribute("value", JSONArray[i].idStation)
                    input.setAttribute("id", "defaultGroup" + i);

                    var label = document.createElement("label");
                    label.setAttribute("class", "custom-control-label");
                    label.setAttribute("for", "defaultGroup" + i);

                    label.innerText = JSONArray[i].name;

                    div.appendChild(input);
                    div.appendChild(label);

                    form.appendChild(div);
                }


                var divInputGroup = document.createElement("div");
                divInputGroup.setAttribute("class", "input-group");

                var divCustomFile = document.createElement("div");
                divCustomFile.setAttribute("class", "custom-file");

                var fileInput = document.createElement("input");
                fileInput.setAttribute("type", "file");
                fileInput.setAttribute("class", "custom-file-input");
                fileInput.setAttribute("name", "newData")
                fileInput.setAttribute("id", "inputFile");
                fileInput.setAttribute("aria-describedby", "inputAddon");

                divCustomFile.appendChild(fileInput);

                var labelFile = document.createElement("label");
                labelFile.setAttribute("class", "custom-file-label");
                labelFile.setAttribute("for", "inputFile");
                labelFile.innerText = "Choose file";

                fileInput.addEventListener("change", function () {
                    labelFile.innerText = this.value.replace('C:\\fakepath\\', "");
                });

                divCustomFile.appendChild(labelFile);

                divInputGroup.appendChild(divCustomFile);

                form.appendChild(divInputGroup);


                var modalContent = document.getElementById("uploadDataModalContent");
                if (modalContent.getElementsByClassName("modal-footer").length === 0) {
                    var modalFooter = document.createElement("div");
                    modalFooter.setAttribute("class", "modal-footer d-flex justify-content-center");
                    var sendButton = document.createElement("button");
                    sendButton.setAttribute("class", "btn btn-indigo");
                    sendButton.setAttribute("onclick", "submitForm()");
                    sendButton.innerText = "Send";

                    modalFooter.appendChild(sendButton);
                    modalContent.appendChild(modalFooter);
                }
            }
        }
    };
    xhr.send();
}

function submitForm() {
    var uploadDataForm = document.getElementById("uploadDataForm");
    if (isOneRadioChecked() && document.getElementById("inputFile").value)
        uploadDataForm.submit();
    else if (document.getElementById("divAlert") === null) {
        var divStationList = document.getElementById("stationList");
        var divAlert = document.createElement("div");
        divAlert.setAttribute("class", "alert alert-danger text-center");
        divAlert.setAttribute("role", "alert");
        divAlert.setAttribute("id", "divAlert");
        divAlert.innerText = "It is necessary to select one station and one dataset!";

        divStationList.insertBefore(divAlert, divStationList.firstChild);
    }
}

// used to empty a modal whenever it gets hidden
function modalSetup() {
    $('.modal').on('hidden.bs.modal', function (e) {
        var body = document.getElementsByClassName("modal-body");
        body[0].innerHTML = "";

        //if any, delete footer
        var modalContents = document.getElementsByClassName("modal-content");
        for (var i = 0; i < modalContents.length; i++) {
            var footers = modalContents[i].getElementsByClassName("modal-footer");
            if (footers.length > 0){
                footers[0].parentNode.removeChild(footers[0]);
            }
        }
    });

}

// Helper function
function isOneRadioChecked() {
    var radios = document.getElementsByName("radios");
    for (var i = 0; i < radios.length; i++) {
        if (radios[i].checked) return true;
    }
    return false;

}

// Helper function
function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}

