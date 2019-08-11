package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.ActivityDao;
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

    private static final long tripId = 2;
    private static final long ID = 4;
    private static final String NAME ="Scuba diving";
    private static final String CATEGORY = "Sports";


    @Autowired
    private ActivityDao ad;


    @Test
    public void testfindById() {
        Optional<Activity> activity = ad.findById(ID);
        Assert.assertTrue(activity.isPresent());
        Assert.assertEquals(NAME, activity.get().getName());
        Assert.assertEquals(CATEGORY, activity.get().getCategory());

    }

    @Test
    public void testfindByName() {
        Optional<Activity> activity = ad.findByName(NAME);
        Assert.assertTrue(activity.isPresent());
        Assert.assertEquals(NAME, activity.get().getName());
    }

    @Test
    public void testfindByCategory(){
        Optional<Activity> activity = ad.findByCategory(CATEGORY);
        Assert.assertTrue(activity.isPresent());
        Assert.assertEquals(CATEGORY, activity.get().getCategory());
    }

    @Test
    public void testGetTripActivities() {
        List<Activity> activities = ad.getTripActivities(tripId);
        Assert.assertNotNull(activities);
        Assert.assertTrue(!activities.isEmpty());
        Assert.assertEquals(ID, activities.get(0).getId());
    }


}
