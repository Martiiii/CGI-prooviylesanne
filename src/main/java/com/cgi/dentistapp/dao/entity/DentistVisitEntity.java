package com.cgi.dentistapp.dao.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dentist_visit")
public class DentistVisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "dentist_name")
    private String dentistName;

    @Column(name = "ssn")
    private String ssn;

    @Column(name = "visit_start")
    private Date visitStart;

    @Column(name = "visit_end")
    private Date visitEnd;

    public DentistVisitEntity() {
    }

    public DentistVisitEntity(String dentistName, String ssn, Date visitStart, Date visitEnd) {
        this.dentistName = dentistName;
        this.ssn = ssn;
        this.visitStart = visitStart;
        this.visitEnd = visitEnd;
    }

    public Long getId() {
        return this.id;
    }

    public String getDentistName() {
        return this.dentistName;
    }

    public String getSsn() {
        return this.ssn;
    }

    public Date getVisitStart() { return this.visitStart; }

    public Date getVisitEnd() {
        return this.visitEnd;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public void setVisitStart(Date visitStart) {
        this.visitStart = visitStart;
    }

    public void setVisitEnd(Date visitEnd) {
        this.visitEnd = visitEnd;
    }

    @Override
    public String toString() {
        return "DentistVisitEntity{" +
                "id=" + id +
                ", dentistName='" + dentistName + '\'' +
                ", ssn='" + ssn + '\'' +
                ", visitStart=" + visitStart +
                ", visitEnd=" + visitEnd +
                '}';
    }
}
