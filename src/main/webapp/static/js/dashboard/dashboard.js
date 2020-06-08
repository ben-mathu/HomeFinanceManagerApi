var timeInterval = setInterval(function() {myTimer()}, 1000);

function myTimer() {
    var date = new Date();
    var hours = "" + date.getHours();
    var min = "" + date.getMinutes();
    var seconds = "" + date.getSeconds();

    if (getLength(hours) == 1) {
        hours = "0" + hours;
    }

    if (getLength(min) == 1) {
        min = "0" + min;
    }

    if (getLength(seconds) == 1) {
        seconds = "0" + seconds;
    }

    var time = hours + ":" + min + ":" + seconds;
    document.getElementById("timeNow").innerHTML = time;
}

function getLength(s) {
    return [...s].length;
}