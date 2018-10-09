package com.cgi.dentistapp;

import com.cgi.dentistapp.controller.DentistAppController;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DentistAppApplicationTests {

	@Test
	public void timeOverlapsTest1() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        Date start1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 14);
        Date end1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 13);
        Date start2 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 16);
        Date end2 = cal.getTime();

        assertTrue(DentistAppController.timeOverlaps(start1, end1, start2, end2));
	}

    @Test
    public void timeOverlapsTest2() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        Date start1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 13);
        Date end1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 13);
        Date start2 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 16);
        Date end2 = cal.getTime();

        assertFalse(DentistAppController.timeOverlaps(start1, end1, start2, end2));
    }

    @Test
    public void timeOverlapsTest3() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        cal.set(Calendar.HOUR_OF_DAY, 12);
        cal.set(Calendar.MINUTE, 0);
        Date start1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 14);
        Date end1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 13);
        Date start2 = cal.getTime();

        cal.set(Calendar.MINUTE, 30);
        Date end2 = cal.getTime();

        assertTrue(DentistAppController.timeOverlaps(start1, end1, start2, end2));
    }

    @Test
    public void timeOverlapsTest4() {
        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, 2018);
        cal.set(Calendar.MONTH, Calendar.OCTOBER);
        cal.set(Calendar.DAY_OF_MONTH, 10);
        cal.set(Calendar.HOUR_OF_DAY, 14);
        cal.set(Calendar.MINUTE, 0);
        Date start1 = null;
        Date end1 = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 13);
        Date start2 = cal.getTime();
        Date end2 = null;

        assertFalse(DentistAppController.timeOverlaps(start1, end1, start2, end2));
    }
}
