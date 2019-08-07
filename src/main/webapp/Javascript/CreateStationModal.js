function createStation(event) {

}

function changeAdditionalFieldPlaceholder(id) {

    var radio = document.getElementById("createStationAdditionalField");
    switch (id) {
        case "radioCity":
            radio.setAttribute("placeholder", "Pollution level");
            break;
        case "radioCountry":
            radio.setAttribute("placeholder", "Dew point");
            break;
        case "radioMountain":
            radio.setAttribute("placeholder", "Snow level");
            break;
        case "radioSea":
            radio.setAttribute("placeholder", "Uv radiation level")
            break;
    }

}