package com.cgi.dentistapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

import com.cgi.dentistapp.dao.DentistVisitDao;
import com.cgi.dentistapp.dao.entity.DentistVisitEntity;

@Service
@Transactional
public class DentistVisitService {

    @Autowired
    private DentistVisitDao dentistVisitDao;

    /**
     * Adds a single visit to the database
     *
     * @param dentistName Name of the dentist
     * @param visitStart Start time of the visit
     * @param visitEnd End time of the visit
     */
    public void addVisit(String dentistName, String ssn, Date visitStart, Date visitEnd) {
        DentistVisitEntity dentistVisitEntity = new DentistVisitEntity(dentistName, ssn, visitStart, visitEnd);
        dentistVisitDao.create(dentistVisitEntity);
    }

    /**
     * Returns every visit in the database
     *
     * @return A list of visits
     */
    public List<DentistVisitEntity> getAllVisits() {
        return dentistVisitDao.getAllVisits();
    }

    /**
     * Returns a visit by its id
     *
     * @param id Id of the visit
     * @return A single visit with the given id
     */
    public DentistVisitEntity getVisitById(Long id) {
        return dentistVisitDao.getVisitById(id);
    }

    /**
     * Deletes a visit by its id
     *
     * @param id Id of the visit
     */
    public void deleteVisit(Long id) {
        dentistVisitDao.deleteVisit(id);
    }

    /**
     * Updates a visit
     *
     * @param visit The visit with the new data
     */
    public void updateVisit(DentistVisitEntity visit) {
        dentistVisitDao.updateVisit(visit);
    }

    /**
     * Returns all visits of a single dentist
     *
     * @param name Name of the dentist
     * @return A list of visits of the given dentist
     */
    public List<DentistVisitEntity> getAllDentistVisits(String name) {
        return dentistVisitDao.getAllDentistVisits(name);
    }

    /**
     * Returns all visits that have the given SSN
     *
     * @param ssn SSN of the patient
     * @return A list of visits with the given SSN
     */
    public List<DentistVisitEntity> getVisitsBySsn(String ssn) {
        return dentistVisitDao.getVisitsBySsn(ssn);
    }

    /**
     * Returns all visits that have the given SSN and dentist
     *
     * @param ssn SSN of the patient
     * @param name Name of the dentist
     * @return A list of visits with the given SSN and dentist
     */
    public List<DentistVisitEntity> getVisitsBySsnAndDentist(String ssn, String name) {
        return dentistVisitDao.getVisitsBySsnAndDentist(ssn, name);
    }
}
