package com.cgi.dentistapp.dto;

import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.constraints.FutureOrPresent;
import java.util.Calendar;
import java.util.Date;

public class DentistVisitDTO {

    /**
     * Name of the dentist. Can't be null, minimum length 1 and maximum 50
     */
    @NotNull
    @Size(min = 1, max = 50)
    private String dentist;

    /**
     * Start time of the visit. Can't be null, must be in the future or present, has to be in format "dd.MM.yyyy HH:mm"
     */
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date visitStart;

    /**
     * End time of the visit. Can't be null, must be in the future or present, has to be in format "dd.MM.yyyy HH:mm"
     */
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private Date visitEnd;

    /**
     * Function that checks if the visit start time if before its end time, must be true.
     *
     * @return True when start time is before end time
     */
    @AssertTrue
    public boolean isStartEndValid() {
        return visitStart != null && visitEnd != null && visitStart.before(visitEnd);
    }

    /**
     * Function that checks if the visit starts at, or after 08:00 and is not a weekend day, must be true.
     *
     * @return True when start time is before end time
     */
    @AssertTrue
    public boolean isStartValid() {
        if (visitStart != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(visitStart);
            boolean isAfterEight = calendar.get(Calendar.HOUR_OF_DAY) >= 8;
            boolean isNotWeekend = calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;

            return isAfterEight && isNotWeekend;
        }
        return false;
    }

    /**
     * Function that checks if the visit ends at, or before 18:00 and is not a weekend day, must be true.
     *
     * @return True when start time is before end time
     */
    @AssertTrue
    public boolean isEndValid() {
        if (visitEnd != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(visitEnd);
            boolean isBeforeSix = calendar.get(Calendar.HOUR_OF_DAY) <= 18;
            boolean isNotWeekend = calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;

            return isBeforeSix && isNotWeekend;
        }
        return false;
    }

    public DentistVisitDTO() {
    }

    public DentistVisitDTO(String dentist, Date visitStart, Date visitEnd) {
        this.dentist = dentist;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
    }

    public String getDentist() {
        return dentist;
    }

    public void setDentist(String dentist) {
        this.dentist = dentist;
    }

    public Date getVisitStart() {
        return visitStart;
    }

    public Date getVisitEnd() {
        return visitEnd;
    }

    public void setVisitStart(Date visitStart) {
        this.visitStart = visitStart;
    }

    public void setVisitEnd(Date visitEnd) {
        this.visitEnd = visitEnd;
    }
}
