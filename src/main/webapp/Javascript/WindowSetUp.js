window.addEventListener("load", setupFunction, true);

function setupFunction(){
    createStationModalSetup();
    uploadDataModalSetup();
    downloadDataModalSetup();
    homepageSetup();

}


$(document).ready(function(){
    $('#uploadDataToast').toast('show'); //if any
});

