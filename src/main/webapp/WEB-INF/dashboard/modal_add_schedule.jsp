<div id="scheduleModal" class="modal">
    <div class="modal-content" >
        <h4 class="modal-title">Add a schedule</h4>
        <p id="schedule-modal-error" class="error-text"></p>
        <div>
            <input id="scheduleDescription" class="input-style" type="text" placeholder="Schedule Description" />
        </div>
        <div class="number-input">
            Time: <input id="scheduledHour" class=" date-time-input" type="time" placeholder="00" max="23" />
        </div>
        <div class="number-input">
            Date: <input id="scheduledDate" class="date-time-input" type="date" name="date" placeholder="00" />
        </div>
        <div id="scheduleTaskSection">
            <input id="scheduledTask" class="input-style schedule-tasks" type="text" name="task" placeholder="Tasks" />

        </div>
        <div>
            <input id="addTasks" class="btn2" type="button" name="add" value="+ Add Task">
        </div>
        <div>
            <input id="addSchedule" class="btn2" type="button" value="Submit" />
            <input id="cancelScheduleModal" class="btn4-caution" type="button" value="Cancel">
        </div>
    </div>
</div>