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
    } else {
        var existentDivAlert = document.getElementById("divAlert");
        existentDivAlert.innerText = text;
    }
}

function closeCurrentModal(modalId) {
    $("#"+ modalId).modal('hide');
    $('.modal').css('overflow-y', 'auto');
}