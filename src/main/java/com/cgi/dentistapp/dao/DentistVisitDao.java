package com.cgi.dentistapp.dao;

import com.cgi.dentistapp.dao.entity.DentistVisitEntity;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DentistVisitDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Creates a new visit entry to the database
     *
     * @param visit The visit entity
     */
    public void create(DentistVisitEntity visit) {
        entityManager.persist(visit);
    }

    /**
     * Return a list of all visits
     *
     * @return A list of all visits
     */
    public List<DentistVisitEntity> getAllVisits() {
        return entityManager.createQuery("SELECT d FROM DentistVisitEntity d", DentistVisitEntity.class).getResultList();
    }

    /**
     * Returns a visit by its id
     *
     * @param id Id of the visit
     * @return The visit with the given id
     */
    public DentistVisitEntity getVisitById(Long id) {
        return entityManager.find(DentistVisitEntity.class, id);
    }

    /**
     * Deletes a visit from the database
     *
     * @param id Id of the visit
     */
    public void deleteVisit(Long id) {
        entityManager.remove(getVisitById(id));
    }

    /**
     * Updates a visit
     *
     * @param visit Visit entity with new data
     */
    public void updateVisit(DentistVisitEntity visit) {
        entityManager.merge(visit);
    }

    /**
     * Returns all visits of a single dentist
     *
     * @param name Name of the dentist
     * @return A list of visits of the given dentist
     */
    public List<DentistVisitEntity> getAllDentistVisits(String name) {
        TypedQuery<DentistVisitEntity> query = entityManager.createQuery("SELECT d FROM DentistVisitEntity d WHERE d.dentistName=:name", DentistVisitEntity.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    /**
     * Returns all visits that have the given SSN
     *
     * @param ssn SSN of the patient
     * @return A list of visits that have the given SSN
     */
    public List<DentistVisitEntity> getVisitsBySsn(String ssn) {
        TypedQuery<DentistVisitEntity> query = entityManager.createQuery("SELECT d FROM DentistVisitEntity d WHERE d.ssn=:ssn", DentistVisitEntity.class);
        query.setParameter("ssn", ssn);
        return query.getResultList();
    }

    /**
     * Returns all visits that have the given SSN and the given dentist
     *
     * @param ssn SSN of the patient
     * @param name Name of the dentist
     * @return A list of visits that have the given SSN and the given dentist
     */
    public List<DentistVisitEntity> getVisitsBySsnAndDentist(String ssn, String name) {
        TypedQuery<DentistVisitEntity> query = entityManager.createQuery("SELECT d FROM DentistVisitEntity d WHERE d.ssn=:ssn AND d.dentistName=:name", DentistVisitEntity.class);
        query.setParameter("ssn", ssn);
        query.setParameter("name", name);
        return query.getResultList();
    }
}
