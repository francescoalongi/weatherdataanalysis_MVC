window.addEventListener("load", setupFunction, true);
function setupFunction(){
    createStationModalSetup();
    uploadDataModalSetup();
};


$(document).ready(function(){
    $('#uploadDataToast').toast('show'); //if any
});
