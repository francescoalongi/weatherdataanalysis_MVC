window.addEventListener("load", setupFunction, true);
function setupFunction(){
    createStationModalSetup();
    uploadDataModalSetup();
    downloadDataModalSetup()
};


$(document).ready(function(){
    $('#uploadDataToast').toast('show'); //if any
});

