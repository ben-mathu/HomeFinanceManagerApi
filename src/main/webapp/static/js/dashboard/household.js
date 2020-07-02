function configureHousehold() {
    
}

/**
 * opens the household modal for user to update the household details
 * 
 * @param {Callback} modalDetails callback
 */
function openHouseholdModal(modalDetails) {

}

function updateHousehold(incompleteKey) {
    var request = getXmlHttpRequest();

    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                delete incomplete[callback.key];

                let len = Object.keys(incomplete).length;

                if (len > 1) {
                    callback.onNext(callback);
                } else if(len == 1) {
                    callback.onDone(callback);
                } else {
                    callback.onComplete();
                }
            }
        }
    }
}