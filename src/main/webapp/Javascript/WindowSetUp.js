window.addEventListener("load", setupFunction, true);
function setupFunction(){
    createStationModalSetup();
    uploadDataModalSetup();
};

$(document).ready(function(){
    $('.toast').toast('show'); //if any
});
