package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.model.Activity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestActivityHibernateDao {

    @Autowired
    private ActivityHibernateDao activityDao;


    @Test
    public void testFindById() {
        Optional<Activity> activityOptional = activityDao.findById(1);
        Assert.assertTrue(activityOptional.isPresent());
        Assert.assertEquals("Trip Activity", activityOptional.get().getName());
    }

    @Test
    public void testFindByCategory() {
        Optional<Activity> activityOptional = activityDao.findByCategory("Adventure");
        Assert.assertTrue(activityOptional.isPresent());
        Assert.assertEquals("Adventure", activityOptional.get().getCategory());
    }

    @Test
    public void testGetTripActivities() {
        List<Activity> activities = activityDao.getTripActivities(1);
        Assert.assertNotNull(activities);
        Assert.assertFalse(activities.isEmpty());
        Assert.assertEquals("Adventure", activities.get(0).getCategory());
    }

}
