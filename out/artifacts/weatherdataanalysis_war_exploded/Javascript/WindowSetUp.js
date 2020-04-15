window.addEventListener("load", setupFunction, true);

function setupFunction(){
    createStationModalSetup();
    homepageSetup();
    modalSetup("modalRegisterStation");
    modalSetup("modalUpload");
    modalSetup("modalDownload")

}


$(document).ready(function(){
    $('#uploadDataToast').toast('show'); //if any
});

