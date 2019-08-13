function fillDownloadModal() {
    var spinner = generateSpinner("Please wait, information are being loaded...");
    var modalBody = document.getElementById("downloadDataModalBody");
    modalBody.appendChild(spinner);

    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    xhr.open('GET', getContextPath() + "/LoadStations");
    xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            var JSONArray = JSON.parse(xhr.responseText);
            modalBody.innerText = ""; // as soon as data are ready, delete the spinner

            if (JSONArray.length === 0) { // if no stations exist in the db

                var par1 = document.createElement("p");
                var a = document.createElement("a");
                a.setAttribute("data-toggle", "modal");
                a.setAttribute("href", "#modalCreateStation");
                a.setAttribute("onclick", "closeCurrentModal(\"modalDownload\")");
                a.innerText = "here";
                par1.appendChild(document.createTextNode("No station has been created yet. Click "));
                par1.appendChild(a);
                par1.appendChild(document.createTextNode(" to create a new one."));
                modalBody.appendChild(par1)

            } else {

                if (modalBody.getElementsByTagName("P").length === 0) {
                    var p = document.createElement("p");
                    p.innerText = "Choose the station from which you want to download data";
                    modalBody.appendChild(p);
                }

                var stationList = document.createElement("div");
                stationList.setAttribute("id", "stationList");
                stationList.setAttribute("class", "mb-3");
                modalBody.appendChild(stationList);

                for (var i = 0; i < JSONArray.length; i++) {
                    var divRadio = document.createElement("div");
                    divRadio.setAttribute("class", "custom-control custom-radio");
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

                    divRadio.appendChild(input);
                    divRadio.appendChild(label);

                    stationList.appendChild(divRadio);
                }


                var par2 = document.createElement("p");
                par2.innerText = "Choose the time frame of the data you want to download";
                modalBody.appendChild(par2);

                var divContainerDatePicker = document.createElement("div");
                divContainerDatePicker.setAttribute("class", "container");

                var divRow = document.createElement("div");
                divRow.setAttribute("class", "row");

                divContainerDatePicker.appendChild(divRow);

                var divColStartDate = document.createElement("div");
                divColStartDate.setAttribute("class", "col-md-6 mb-4");

                divRow.appendChild(divColStartDate);

                var divStartingDate = document.createElement("div");
                divStartingDate.setAttribute("class", "input-group date startdate");
                divStartingDate.setAttribute("id", "divStartDate");

                divColStartDate.appendChild(divStartingDate);

                var inputStartingDatePicker = document.createElement("input");
                inputStartingDatePicker.setAttribute("placeholder", "Starting date");
                inputStartingDatePicker.setAttribute("type", "text");
                inputStartingDatePicker.setAttribute("id", "downloadDataStartingDate");
                inputStartingDatePicker.setAttribute("class", "form-control");

                divStartingDate.appendChild(inputStartingDatePicker);

                var divColEndDate = document.createElement("div");
                divColEndDate.setAttribute("class", "col-md-6 mb-4");

                divRow.appendChild(divColEndDate);

                var divEndingDate = document.createElement("div");
                divEndingDate.setAttribute("class", "input-group date enddate");
                divEndingDate.setAttribute("id", "divEndDate");

                divColEndDate.appendChild(divEndingDate);

                var inputEndingDatePicker = document.createElement("input");
                inputEndingDatePicker.setAttribute("placeholder", "Ending date");
                inputEndingDatePicker.setAttribute("type", "text");
                inputEndingDatePicker.setAttribute("id", "downloadDataEndingDate");
                inputEndingDatePicker.setAttribute("class", "form-control");

                divEndingDate.appendChild(inputEndingDatePicker);

                modalBody.appendChild(divContainerDatePicker);

                $('#divStartDate').datetimepicker({
                    format: 'DD/MM/YYYY',
                    useCurrent: false,
                    keepOpen: true,
                    allowInputToggle: true,
                    ignoreReadonly: true,
                    showTodayButton: true,
                    viewMode: 'days'
                }).on('dp.change', function (selected) {
                    var minDate = new Date(selected.date.valueOf());
                    $('#divEndDate').data("DateTimePicker").minDate(minDate);
                });


                $('#divEndDate').datetimepicker({
                    format: 'DD/MM/YYYY',
                    useCurrent: false,
                    keepOpen: true,
                    allowInputToggle: true,
                    ignoreReadonly: true,
                    showTodayButton: true,
                    viewMode: 'days'
                }).on('dp.change', function (selected) {
                    var maxDate = new Date(selected.date.valueOf())
                    $('#divStartDate').data("DateTimePicker").maxDate(maxDate);
                });

                var modalContent = document.getElementById("downloadDataModalContent");
                if (modalContent.getElementsByClassName("modal-footer").length === 0) {
                    var modalFooter = document.createElement("div");
                    modalFooter.setAttribute("class", "modal-footer d-flex justify-content-center");
                    modalFooter.setAttribute("id", "downloadDataModalFooter");
                    var downloadButton = document.createElement("button");
                    downloadButton.setAttribute("class", "btn btn-indigo");
                    downloadButton.setAttribute("onclick", "downloadData()");
                    downloadButton.innerText = "Download data";
                    modalFooter.appendChild(downloadButton);
                    modalContent.appendChild(modalFooter);
                }
            }
        }
    };
    xhr.send();

}

function downloadData() {

    if (isOneRadioChecked("radios") && document.getElementById("downloadDataStartingDate").value && document.getElementById("downloadDataEndingDate")) {

        var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
        var stationId;
        var radios = document.getElementsByName("radios");
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) stationId = radios[i].value;
        }
        var beginDate = document.getElementById("downloadDataStartingDate").value;
        var endDate = document.getElementById("downloadDataEndingDate").value;

    window.location = getContextPath() + "/DownloadData?station_id=" + stationId + "&begin_date=" + beginDate + "&end_date=" + endDate;
    closeCurrentModal("modalDownload");
    } else {
        var divDownloadDataModalBody = document.getElementById("downloadDataModalBody");
        insertAlert(divDownloadDataModalBody, "It is necessary to select one station, a starting date and an end date!");
        $('#modalDownload').animate({ scrollTop: 0 }, 'slow');
    }
}

function downloadDataModalSetup() {
    $('#modalDownload').on('hidden.bs.modal', function (e) {
        var downloadDataBody = document.getElementById("downloadDataModalBody");
        downloadDataBody.innerHTML = "";

        //if any, delete footer
        var footer = document.getElementById("downloadModalFooter");
        if (footer)
            footer.parentNode.removeChild(footer);
    });
}