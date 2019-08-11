function fillDownloadModal() {
    var spinner = generateSpinner("Please wait, information are being loaded...");
    var modalBody = document.getElementById("downloadDataModalBody");
    modalBody.appendChild(spinner);

    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    xhr.open('GET', getContextPath() + "/LoadStations");
    xhr.onreadystatechange = function() {
        if (xhr.readyState > 3 && xhr.status === 200) {
            var JSONArray = JSON.parse(xhr.responseText);
            modalBody.innerText = ""; // as soon as data are ready, delete the spinner

            if (JSONArray.length === 0) { // if no stations exist in the db

                var par1 = document.createElement("p");
                var a = document.createElement("a");
                a.setAttribute("data-toggle", "modal");
                a.setAttribute("href", "#modalCreateStation");
                a.setAttribute("onclick", "closeCurrentModal()");
                a.innerText = "here";
                par1.appendChild(document.createTextNode("No station has been created yet. Click "));
                par1.appendChild(a);
                par1.appendChild(document.createTextNode(" to create a new one."));
                modalBody.appendChild(par)

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

                var divStartingDateMdForm = document.createElement("div");
                divStartingDateMdForm.setAttribute("class", "md-form");

                divColStartDate.appendChild(divStartingDateMdForm);

                var inputStartingDatePicker = document.createElement("input");
                inputStartingDatePicker.setAttribute("placeholder", "Select the starting date");
                inputStartingDatePicker.setAttribute("type", "text");
                inputStartingDatePicker.setAttribute("id", "startingDate");
                inputStartingDatePicker.setAttribute("class", "form-control datepicker");

                divStartingDateMdForm.appendChild(inputStartingDatePicker);

                var divColEndDate = document.createElement("div");
                divColEndDate.setAttribute("class", "col-md-6 mb-4");

                divRow.appendChild(divColEndDate);

                var divEndingDateMdForm = document.createElement("div");
                divEndingDateMdForm.setAttribute("class", "md-form");

                divColEndDate.appendChild(divEndingDateMdForm);

                var inputEndingDatePicker = document.createElement("input");
                inputEndingDatePicker.setAttribute("placeholder", "Select the ending date");
                inputEndingDatePicker.setAttribute("type", "text");
                inputEndingDatePicker.setAttribute("id", "endingDate");
                inputEndingDatePicker.setAttribute("class", "form-control datepicker");

                divEndingDateMdForm.appendChild(inputEndingDatePicker);

                modalBody.appendChild(divContainerDatePicker);


                /*var from_input = $('#startingDate').pickadate(),
                    from_picker = from_input.pickadate('picker');
                var to_input = $('#endingDate').pickadate(),
                    to_picker = to_input.pickadate('picker');

                if ( from_picker.get('value') ) {
                    to_picker.set('min', from_picker.get('select'));
                }
                if ( to_picker.get('value') ) {
                    from_picker.set('max', to_picker.get('select'));
                }

                from_picker.on('set', function(event) {
                    if ( event.select ) {
                        to_picker.set('min', from_picker.get('select'));
                    }
                    else if ( 'clear' in event ) {
                        to_picker.set('min', false);
                    }
                });
                to_picker.on('set', function(event) {
                    if ( event.select ) {
                        from_picker.set('max', to_picker.get('select'));
                    }
                    else if ( 'clear' in event ) {
                        from_picker.set('max', false);
                    }
                });*/

                var modalContent = document.getElementById("downloadDataModalContent");
                if (modalContent.getElementsByClassName("modal-footer").length === 0) {
                    var modalFooter = document.createElement("div");
                    modalFooter.setAttribute("class", "modal-footer d-flex justify-content-center");
                    var downloadButton = document.createElement("button");
                    downloadButton.setAttribute("class", "btn btn-indigo");
                    downloadButton.setAttribute("onclick", "submitForm()");
                    downloadButton.innerText = "Download data";

                    modalFooter.appendChild(downloadButton);
                    modalContent.appendChild(modalFooter);
                }
            }
        }
    };
    xhr.send();



}