package com.cgi.dentistapp.controller;

import com.cgi.dentistapp.dao.entity.DentistVisitEntity;
import com.cgi.dentistapp.dto.DentistVisitDTO;
import com.cgi.dentistapp.misc.Dentist;
import com.cgi.dentistapp.service.DentistVisitService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@EnableAutoConfiguration
public class DentistAppController extends WebMvcConfigurerAdapter {
    private static Logger logger = LoggerFactory.getLogger(DentistAppController.class);
    @Autowired
    private DentistVisitService dentistVisitService;

    /**
     * Premade example dentists
     */
    private List<Dentist> dentists = Arrays.asList(
            new Dentist("Mari", "Süstal"),
            new Dentist("Tõnu", "Kanüül"),
            new Dentist("Priit", "Plaaster")
    );

    /**
     * Function that cheks if two time periods overlap
     *
     * @param start1 Start time of one period
     * @param end1 End time of one period
     * @param start2 Start time of the other period
     * @param end2 End time of the other period
     * @return True if time overlaps, False otherwise
     */
    public static boolean timeOverlaps(Date start1, Date end1, Date start2, Date end2) {
        return start2 != null && end2!= null && !(!end1.after(start2) || !start1.before(end2));
        // Is public for Unit Tests
    }

    /**
     * Function that checks if the given visit overlaps with another visit of the same dentist
     *
     * @param dentistVisitDTO The visit that is checked
     * @return True if the given visit overlaps with another of the same dentist, False otherwise
     */
    private boolean timeIsInvalid(DentistVisitDTO dentistVisitDTO) {
        if (dentistVisitDTO != null) {
            List<DentistVisitEntity> allVisits = dentistVisitService.getAllVisits();
            logger.info("Are there any visits={}", !allVisits.isEmpty());
            if (!allVisits.isEmpty()) {
                List<DentistVisitEntity> doctorVisits = allVisits.stream()
                        .filter(visit -> visit.getDentistName().equals(dentistVisitDTO.getDentist()))
                        .collect(Collectors.toList());
                logger.info("All visits={}", allVisits);
                if (!doctorVisits.isEmpty()) {
                    Predicate<DentistVisitEntity> overlapPredicate = visit ->
                            timeOverlaps(visit.getVisitStart(), visit.getVisitEnd(), dentistVisitDTO.getVisitStart(), dentistVisitDTO.getVisitEnd());
                    return doctorVisits.stream().anyMatch(overlapPredicate);
                }
            }
        }
        return false;
    }

    /**
     * Function that checks if the given visit overlaps with another visit of the same dentist.
     * Ignores visits with the given id
     *
     * @param dentistVisitDTO The visit that is checked
     * @param id The id to be ignored
     * @return True if the given visit overlaps with another of the same dentist, False otherwise
     */
    private boolean timeIsInvalid(DentistVisitDTO dentistVisitDTO, Long id) {
        if (dentistVisitDTO != null) {
            List<DentistVisitEntity> allVisits = dentistVisitService.getAllVisits();
            logger.info("Are there any visits={}", !allVisits.isEmpty());
            if (!allVisits.isEmpty()) {
                List<DentistVisitEntity> doctorVisits = allVisits.stream()
                        .filter(visit -> visit.getDentistName().equals(dentistVisitDTO.getDentist()) && !visit.getId().equals(id))
                        .collect(Collectors.toList());
                logger.info("All visits={}", allVisits.toString());
                if (!doctorVisits.isEmpty()) {
                    Predicate<DentistVisitEntity> overlapPredicate = visit ->
                            timeOverlaps(visit.getVisitStart(), visit.getVisitEnd(), dentistVisitDTO.getVisitStart(), dentistVisitDTO.getVisitEnd());
                    return doctorVisits.stream().anyMatch(overlapPredicate);
                }
            }
        }
        return false;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
        registry.addViewController("/registrations").setViewName("registrations");
    }

    @GetMapping("/")
    public String showRegisterForm(DentistVisitDTO dentistVisitDTO, Model model) {
        model.addAttribute("dentists", dentists);
        return "form";
    }

    @PostMapping("/")
    public String postRegisterForm(@Valid DentistVisitDTO dentistVisitDTO, BindingResult bindingResult, Model model, HttpServletRequest request) {
        // Check if valid dentist name was given
        Predicate<Dentist> doctorPredicate = dentist -> dentist.getFullname().equals(dentistVisitDTO.getDentist());
        boolean doctorIsValid = dentists.stream().anyMatch(doctorPredicate);

        if (!doctorIsValid) {
            bindingResult.rejectValue("dentist", "dentist.error", "Doctor not found");
        }

        // Check if valid start and end time was given
        boolean dateOverlaps = timeIsInvalid(dentistVisitDTO);
        if (dateOverlaps) {
            bindingResult.rejectValue("visitEnd", "visitEnd.overlap", "Visit overlaps with another visit");
        }

        if (bindingResult.hasErrors()) {
            // If some fields were invalid or time overlapped
            model.addAttribute("dentists", dentists);
            logger.info("bindingResults is={}", bindingResult.getAllErrors().toString());
            return "form";
        } else if (request.getParameter("buttonCheck") != null) {
            // If user checked time overlap and time is valid
            model.addAttribute("timeCheck", "visitEnd.noOverlap");
            model.addAttribute("dentists", dentists);
            return "form";
        }
        // If no errors were present, add visit to the database
        dentistVisitService.addVisit(dentistVisitDTO.getDentist(), dentistVisitDTO.getVisitStart(), dentistVisitDTO.getVisitEnd());
        return "redirect:/results";
    }

    @GetMapping("registrations")
    public String showRegistrations(Model model, @RequestParam(value="dentist", required=false, defaultValue = "") String doctorParam) {
        // Check if a specific doctor visits are to be displayed
        if (doctorParam == null || doctorParam.equals("")) {
            // Display all visits
            model.addAttribute("visits", dentistVisitService.getAllVisits());
        } else {
            // Display a specific doctor's visits
            // First check if doctor's name is in the "database"
            Predicate<Dentist> doctorPredicate = doctor -> doctor.getFullname().equals(doctorParam);
            boolean doctorIsValid = dentists.stream().anyMatch(doctorPredicate);

            if (doctorIsValid) {
                // If name was valild, show only selected doctor's visits
                model.addAttribute("visits", dentistVisitService.getAllDentistVisits(doctorParam));
            } else {
                // If name was invalid, show error message and show all visits
                model.addAttribute("dentistSearch", "Pick a valid dentist");
                model.addAttribute("visits", dentistVisitService.getAllVisits());
            }
        }
        model.addAttribute("dentists", dentists);
        return "registrations";
    }

    @GetMapping("registrations/{id}")
    public String showRegistration(@PathVariable String id, @RequestParam(value="action", required=false, defaultValue = "") String action, DentistVisitDTO dentistVisitDTO, Model model) {
        // Get the latest visit info from the database
        DentistVisitEntity visit = dentistVisitService.getVisitById(Long.parseLong(id));
        dentistVisitDTO.setDentist(visit.getDentistName());
        dentistVisitDTO.setVisitStart(visit.getVisitStart());
        dentistVisitDTO.setVisitEnd(visit.getVisitEnd());

        // Get all but the current visit's dentist
        List<Dentist> doctorsRest = dentists.stream()
                .filter(dentist -> !dentist.getFullname().equals(visit.getDentistName()))
                .collect(Collectors.toList());

        model.addAttribute("visitVar", visit);
        model.addAttribute("dentists", doctorsRest);

        // Check if user is deleting message
        if (action.equals("delete")) {
            // Delete selected message and go to registrations page
            dentistVisitService.deleteVisit(Long.parseLong(id));
            return "redirect:/registrations";
        }
        return "registration";
    }

    @PostMapping("registrations/{id}")
    public String postRegistration(@PathVariable String id, @Valid DentistVisitDTO dentistVisitDTO, BindingResult bindingResult, Model model) {
        // Get the latest visit info from the database
        DentistVisitEntity visitEntity = dentistVisitService.getVisitById(Long.parseLong(id));

        // Check if new entered dentist name is valid
        Predicate<Dentist> doctorPredicate = dentist -> dentist.getFullname().equals(dentistVisitDTO.getDentist());
        boolean doctorIsValid = dentists.stream().anyMatch(doctorPredicate);

        if (!doctorIsValid) {
            bindingResult.rejectValue("dentist", "dentist.error", "Doctor not found");
        }

        // Check if entered dates overlap
        boolean dateOverlaps = timeIsInvalid(dentistVisitDTO, visitEntity.getId());
        if (dateOverlaps) {
            bindingResult.rejectValue("visitEnd", "visitEnd.overlap", "Visit overlaps with another visit");
        }

        if (bindingResult.hasErrors()) {
            // If something was invalid, restore previous state
            dentistVisitDTO.setDentist(visitEntity.getDentistName());
            dentistVisitDTO.setVisitStart(visitEntity.getVisitStart());
            dentistVisitDTO.setVisitEnd(visitEntity.getVisitEnd());

            List<Dentist> doctorsRest = dentists.stream()
                    .filter(dentist -> !dentist.getFullname().equals(visitEntity.getDentistName()))
                    .collect(Collectors.toList());

            model.addAttribute("visitVar", visitEntity);
            model.addAttribute("dentists", doctorsRest);
            logger.info("bindingResults is={}", bindingResult.getAllErrors().toString());
            return "registration";
        }

        // If no errors found, change current visit's data
        visitEntity.setDentistName(dentistVisitDTO.getDentist());
        visitEntity.setVisitStart(dentistVisitDTO.getVisitStart());
        visitEntity.setVisitEnd(dentistVisitDTO.getVisitEnd());
        dentistVisitService.updateVisit(visitEntity);
        return "redirect:/registrations/"+id;
    }

}
