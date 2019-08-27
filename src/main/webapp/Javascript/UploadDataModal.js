
function fillUploadModal() {

    var spinner = generateSpinner("Please wait, stations are being loaded...");
    var modalBody = document.getElementById("uploadDataModalBody");
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
                a.setAttribute("onclick", "closeCurrentModal(\"modalUpload\")");
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



                var form = document.createElement("form");
                form.setAttribute("action", getContextPath() + "/UploadData");
                form.setAttribute("method", "POST");
                form.setAttribute("enctype", "multipart/form-data");
                form.setAttribute("id", "uploadDataForm");

                modalBody.appendChild(form);

                var stationList = document.createElement("div");
                stationList.setAttribute("id", "stationList");
                stationList.setAttribute("class", "mb-3");

                form.appendChild(stationList);

                for (var i = 0; i < JSONArray.length; i++) {
                    var div = document.createElement("div");
                    div.setAttribute("class", "custom-control custom-radio");
                    var input = document.createElement("input");
                    input.setAttribute("type", "radio");
                    input.setAttribute("class", "custom-control-input");
                    input.setAttribute("name", "radios");
                    input.setAttribute("value", JSONArray[i].idStation);
                    input.setAttribute("id", "defaultGroup" + i);

                    var label = document.createElement("label");
                    label.setAttribute("class", "custom-control-label");
                    label.setAttribute("for", "defaultGroup" + i);

                    label.innerText = JSONArray[i].name;

                    div.appendChild(input);
                    div.appendChild(label);

                    stationList.appendChild(div);
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
                    modalFooter.setAttribute("class", "modal-footer modal-footer-upload d-flex justify-content-center");
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
    if (isOneRadioChecked("radios") && document.getElementById("inputFile").value) {
        uploadDataForm.submit();
    } else {
        var divStationList = document.getElementById("stationList");
        insertAlert(divStationList, "It is necessary to select one station and one dataset!");
    }
}

function uploadDataModalSetup() {
    $('#modalUpload').on('hidden.bs.modal', function (e) {
        var body = document.getElementsByClassName("modal-body");
        body[0].innerHTML = "";

        //if any, delete footer
        var modalContents = document.getElementsByClassName("modal-content");
        for (var i = 0; i < modalContents.length; i++) {
            var footers = modalContents[i].getElementsByClassName("modal-footer-upload");
            if (footers.length > 0){
                footers[0].parentNode.removeChild(footers[0]);
            }
        }
    });
}




