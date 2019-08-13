window.addEventListener("load", setupFunction, true);

function setupFunction(){
    createStationModalSetup();
    uploadDataModalSetup();
    downloadDataModalSetup();
    homepageDatePickerSetup();

}


$(document).ready(function(){
    $('#uploadDataToast').toast('show'); //if any
});

