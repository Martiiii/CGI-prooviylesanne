package com.cgi.dentistapp.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SearchDTO {

    /**
     * Name of the dentist. Can't be null, minimum length 1 and maximum 50
     */
    @Size(max = 50)
    private String dentist;

    /**
     * SSN of the patient.
     */
    @Size(max = 11)
    @Pattern(regexp="^(0|[1-9][0-9]*)$")
    private String ssn;

    public SearchDTO() {

    }

    public SearchDTO(String dentist, String ssn) {
        this.dentist = dentist;
        this.ssn = ssn;
    }

    public String getDentist() {
        return this.dentist;
    }

    public void setDentist(String dentist) {
        this.dentist = dentist;
    }

    public String getSsn() {
        return this.ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }
}
