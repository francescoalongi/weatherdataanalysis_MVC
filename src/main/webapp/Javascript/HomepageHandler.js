var selectedStations = new Map();

function handleCheckBehaviour(checkboxId, selectId) {

    var select = document.getElementById(selectId);
    var checkbox = document.getElementById(checkboxId);
    if (checkbox.checked) {
        select.disabled = false;
        var dummyEvent = {};
        dummyEvent.currentTarget = select;
        handleSelectBehaviour(dummyEvent);
    } else {
        select.disabled = true;
        selectedStations.delete(select.id);
        if (checkEveryEntryEqual(selectedStations)) {
            var additionalFieldOption = getAdditionalFieldOption();
            switch (selectedStations.values().next().value) {
                case "City":
                    additionalFieldOption.innerText = "Pollution level";
                    break;
                case "Country":
                    additionalFieldOption.innerText = "Dew point";
                    break;
                case "Mountain":
                    additionalFieldOption.innerText = "Snow level";
                    break;
                case "Sea":
                    additionalFieldOption.innerText = "Uv radiation";
                    break;
            }
        } else {
            var additionalFieldOpt = document.getElementById("additionalFieldOption");
            if (typeof additionalFieldOpt !== "undefined")
                additionalFieldOpt.parentNode.removeChild(additionalFieldOpt);
        }
    }

    var selectWeatherDimension = document.getElementById("selectWeatherDimension");
    if (selectedStations.size)
        selectWeatherDimension.disabled = false;
    else selectWeatherDimension.disabled = true;
}


function handleSelectBehaviour(event) {
     var selectId = event.currentTarget.id;
     var stationType = event.currentTarget.selectedOptions[0].getAttribute("data-station-type");

     selectedStations.set(selectId, stationType);
     if (checkEveryEntryEqual(selectedStations)) {
         var additionalFieldOption = getAdditionalFieldOption();
         switch (stationType) {
             case "City":
                 additionalFieldOption.innerText = "Pollution level";
                 break;
             case "Country":
                 additionalFieldOption.innerText = "Dew point";
                 break;
             case "Mountain":
                 additionalFieldOption.innerText = "Snow level";
                 break;
             case "Sea":
                 additionalFieldOption.innerText = "Uv radiation";
                 break;
         }
     } else {
         var additionalFieldOpt = document.getElementById("additionalFieldOption");
         if (typeof additionalFieldOpt !== "undefined")
            additionalFieldOpt.parentNode.removeChild(additionalFieldOpt);
     }

}

function checkEveryEntryEqual(map) {

    if (map.size > 0) {
        for (let [k, v] of map) {
            if (v !== map.values().next().value) return false
        }
        return true;
    } else return false;
}

function getAdditionalFieldOption() {
    var additionalFieldOption = document.getElementById("additionalFieldOption");
    if (additionalFieldOption === null) {
        additionalFieldOption = document.createElement("option");
        additionalFieldOption.setAttribute("id", "additionalFieldOption");
        document.getElementById("selectWeatherDimension").appendChild(additionalFieldOption);
    }
    return additionalFieldOption;
}

