
function retrieveStations() {
    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    xhr.open('GET', getContextPath() + "/LoadStations");
    xhr.onreadystatechange = function() {
        if (xhr.readyState>3 && xhr.status==200) {
            var JSONArray = JSON.parse(xhr.responseText);
            var stationList = document.getElementById("stationList");
            stationList.innerHTML = ""; // in order to delete the spinner

            var form = document.createElement("form");
            form.setAttribute("action", getContextPath() + "/UploadData");
            form.setAttribute("method", "POST");
            form.setAttribute("enctype", "multipart/form-data");
            form.setAttribute("id","uploadDataForm");

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

                label.innerText = JSONArray[i].name ;

                div.appendChild(input);
                div.appendChild(label);

                form.appendChild(div);
            }


            var divInputGroup = document.createElement("div");
            divInputGroup.setAttribute("class", "input-group");

            var divCustomFile = document.createElement("div");
            divCustomFile.setAttribute("class", "custom-file");

            var fileInput = document.createElement("input");
            fileInput.setAttribute("type","file");
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

        }
    };
    xhr.send();
}

function submitForm() {
    var uploadDataForm = document.getElementById("uploadDataForm");
    if (isOneRadioChecked() && document.getElementById("inputFile").value)
        uploadDataForm.submit();
    else {
        var divStationList = document.getElementById("stationList");
        var firstDiv = divStationList.firstChild;
        if (firstDiv.getAttribute("id") !== "divAlert") {
            var divAlert = document.createElement("div");
            divAlert.setAttribute("class", "alert alert-danger text-center");
            divAlert.setAttribute("role", "alert");
            divAlert.setAttribute("id", "divAlert");
            divAlert.innerText = "It is necessary to select one station and one dataset!";

            divStationList.insertBefore(divAlert, divStationList.firstChild);
        }
    }
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

