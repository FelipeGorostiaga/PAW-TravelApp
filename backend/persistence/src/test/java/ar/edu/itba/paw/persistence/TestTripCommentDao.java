package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.TripCommentsDao;
import ar.edu.itba.paw.model.TripComment;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@Sql("classpath:schema.sql")
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TestTripCommentDao {

    @Autowired
    private TripCommentsDao tcd;

    private static final String COMMENT = "test comment";
    private static final long ID = 1;

    @Test
    public void testFindById() {
        Optional<TripComment> tripComment = tcd.findById(ID);
        Assert.assertTrue(tripComment.isPresent());
        Assert.assertEquals(COMMENT, tripComment.get().getComment());
    }

}
