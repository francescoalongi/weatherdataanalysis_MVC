
function fillModalUploadData(modalBody, JSONArray) {
    var p = document.createElement("p");
    p.innerText = "Choose the station to which your data belongs and then choose the dataset you want to upload";
    modalBody.appendChild(p);

    var form = document.createElement("form");
    form.setAttribute("action", getContextPath() + "/UploadData");
    form.setAttribute("method", "POST");
    form.setAttribute("enctype", "multipart/form-data");
    form.setAttribute("id", "uploadDataForm");

    modalBody.appendChild(form);

    createStationListRadio(form, JSONArray);

    var divInputGroup = document.createElement("div");
    divInputGroup.setAttribute("class", "input-group");

    var divCustomFile = document.createElement("div");
    divCustomFile.setAttribute("class", "custom-file");

    var fileInput = document.createElement("input");
    fileInput.setAttribute("type", "file");
    fileInput.setAttribute("class", "custom-file-input");
    fileInput.setAttribute("name", "newData");
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

    var modalFooter = document.createElement("div");
    modalFooter.setAttribute("class", "modal-footer modal-footer-upload d-flex justify-content-center");
    modalFooter.setAttribute("id", "modalUploadFooter");
    var sendButton = document.createElement("button");
    sendButton.setAttribute("class", "btn btn-indigo");
    sendButton.setAttribute("onclick", "submitForm()");
    sendButton.innerText = "Upload data";
    modalFooter.appendChild(sendButton);
    modalBody.parentNode.appendChild(modalFooter);

}


function submitForm() {
    var uploadDataForm = document.getElementById("uploadDataForm");
    if (isOneRadioChecked("idStation") && document.getElementById("inputFile").value) {
        uploadDataForm.submit();
    } else {
        var modalUploadBody = document.getElementById("modalUploadBody");
        insertAlert(modalUploadBody, "It is necessary to select one station and one dataset!");
        $('#modalUpload').animate({ scrollTop: 0 }, 'slow');

    }
}





