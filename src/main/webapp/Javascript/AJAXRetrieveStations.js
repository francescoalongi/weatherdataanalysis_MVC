
function retrieveStations() {
    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP');
    xhr.open('GET', getContextPath() + "/LoadStations");
    xhr.onreadystatechange = function() {
        if (xhr.readyState>3 && xhr.status==200) {
            var JSONArray = JSON.parse(xhr.responseText);
            var stationList = document.getElementById("stationList");
            stationList.innerHTML = "";

            for (var i = 0; i < JSONArray.length; i++) {
                var div = document.createElement("div");
                div.setAttribute("class", "custom-control custom-radio")
                var input = document.createElement("input");
                input.setAttribute("type", "radio");
                input.setAttribute("class", "custom-control-input");
                input.setAttribute("name", "groupOfDefaultRadios");
                input.setAttribute("id", "defaultGroup" + i);

                var label = document.createElement("label");
                label.setAttribute("class", "custom-control-label");
                label.setAttribute("for", "defaultGroup" + i);

                label.innerText = JSONArray[i].name ;

                div.appendChild(input);
                div.appendChild(label);

                stationList.appendChild(div);
            }

        }
    };
    xhr.send();
}

function getContextPath() {
    return window.location.pathname.substring(0, window.location.pathname.indexOf("/",2));
}
