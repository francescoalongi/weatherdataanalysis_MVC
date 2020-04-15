function fillModalDownloadData (modalBody, JSONArray) {
    var p = document.createElement("p");
    p.innerText = "Choose the station from which you want to download data";
    modalBody.appendChild(p);

    createStationListRadio(modalBody, JSONArray);

    var timeFrameDescription = document.createElement("p");
    timeFrameDescription.innerText = "Choose the time frame of the data you want to download";
    modalBody.appendChild(timeFrameDescription);

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

    var modalFooter = document.createElement("div");
    modalFooter.setAttribute("class", "modal-footer d-flex justify-content-center");
    modalFooter.setAttribute("id", "modalDownloadFooter");
    var downloadButton = document.createElement("button");
    downloadButton.setAttribute("class", "btn btn-indigo");
    downloadButton.setAttribute("onclick", "downloadData()");
    downloadButton.innerText = "Download data";
    modalFooter.appendChild(downloadButton);
    modalBody.parentNode.appendChild(modalFooter);

}

function downloadData() {

    if (isOneRadioChecked("idStation") && document.getElementById("downloadDataStartingDate").value && document.getElementById("downloadDataEndingDate")) {

        var stationId;
        var radios = document.getElementsByName("idStation");
        for (var i = 0; i < radios.length; i++) {
            if (radios[i].checked) stationId = radios[i].value;
        }
        var beginDate = document.getElementById("downloadDataStartingDate").value;
        var endDate = document.getElementById("downloadDataEndingDate").value;

        window.location = getContextPath() + "/DownloadData?station_id=" + stationId + "&begin_date=" + beginDate + "&end_date=" + endDate;
        closeCurrentModal("modalDownload");
    } else {
        var modalDownloadBody = document.getElementById("modalDownloadBody");
        insertAlert(modalDownloadBody, "It is necessary to select one station, a starting date and an end date!");
        $('#modalDownload').animate({ scrollTop: 0 }, 'slow');
    }
}
