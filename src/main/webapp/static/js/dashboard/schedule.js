// modal element declaration
let scheduleHour;
let scheduledDate;
let scheduledTasksArr;
let btnAddTask;
let scheduleDescription;
let taskCount;
let errorSection;

function configureSchedule() {
    // initialize to represent first task
    taskCount = 1;
    errorSection = document.getElementById("schedule-modal-error");
    
    scheduleDescription = document.getElementById("scheduleDescription");
    scheduleHour = document.getElementById("scheduledHour");
    scheduledDate = document.getElementById("scheduledDate");
    scheduledTasksArr = document.getElementsByClassName("schedule-tasks");

    btnAddTask = document.getElementById("addTasks");
    btnAddTask.onclick = function() {
        var task = document.getElementById("scheduledTask") == null ? "" : document.getElementById("scheduledTask").value;
        if (task == "") {
            errorSection.innerHTML = "Fill in one task before adding another.";
        } else {
            addTaskFields();
        }
    }

    var btnOpenScheduleModal = document.getElementById("btnOpenScheduleModal");
    btnOpenScheduleModal.onclick = function() {
        scheduleModal.style.display = "block";
    }
    
    var btnAddSchedule = document.getElementById("addSchedule");
    btnAddSchedule.onclick = function() {
        scheduleTask();
    }

    var btnCancelScheduleModal = document.getElementById("cancelScheduleModal");
    btnCancelScheduleModal.onclick = function() {
        scheduleModal.style.display = "none";
    }
}

function addTaskFields() {
    var tasksSection = document.getElementById("scheduleTaskSection");
    var inputTask = document.createElement("input");
    inputTask.setAttribute("type", "text");
    inputTask.setAttribute("placeholder", "Task")
    inputTask.classList.add("input-style");
    inputTask.classList.add("schedule-tasks");
    tasksSection.appendChild(inputTask);
}

function scheduleTask() {
    var request = getXmlHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == 4) {
            if (request.status == 200) {
                
            }
        }
    }

    var description = "schedule-description=" + escape(scheduleDescription.value);
    var time = "schedule-time=" + escape(scheduleHour.value);
    var date = "schedule-date=" + escape(scheduledDate.value);
    
    var tasks = [];
    var count = 0;
    for (let i = 0; i < scheduledTasksArr.length; i++) {
        var task = scheduledTasksArr[i] == null ? "" : scheduledTasksArr[i].value;
        if (task != null && task != "") {
            tasks[count] = task;
            count++;
        }
    }

    var jsonTasks = "schedule_tasks=" + escape(JSON.stringify(tasks));

    var data = description + "&" + time + "&" + date + "&" + jsonTasks;
    var path = document.getElementById("contextPath").value;

    // send request
    request.open("PUT", path + "/dashboard/schedule-controller?" + data, true);
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}