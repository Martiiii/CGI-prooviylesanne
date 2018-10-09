const dateInput = document.getElementById('visitTime');
flatpickr.localize(flatpickr.l10ns.et);
const currentTime = new Date();

const endInput = document.getElementById('visitEnd');
const endPicker = flatpickr(endInput, {
    enableTime: true,
    noCalendar: true,
    dateFormat: "d.m.Y H:i",
    minDate: 'today',
    minTime: "08:00",
    maxTime: "18:00",
    time_24hr: true,
});

const startInput = document.getElementById('visitStart');
const startPicker = flatpickr(startInput, {
    enableTime: true,
    noCalendar: false,
    dateFormat: "d.m.Y H:i",
    minDate: 'today',
    minTime: "08:00",
    maxTime: "17:45",
    defaultHour: currentTime.getHours(),
    defaultMinute: currentTime.getMinutes(),
    time_24hr: true,
    onChange: function(selectedDates, dateStr, instance) {
        endPicker.setDate(dateStr);
        endPicker.set("minTime", selectedDates[0].getHours() + ":" + selectedDates[0].getMinutes());
    },
    disable: [
            function(date) {
                return (date.getDay() === 6 || date.getDay() === 0);
            }
        ],
});
