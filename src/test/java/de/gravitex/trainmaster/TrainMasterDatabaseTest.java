package de.gravitex.trainmaster;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.gravitex.trainmaster.repo.test.TrainRunRepository;

@DataJpaTest
public class TrainMasterDatabaseTest {

    @Autowired
    private TrainRunRepository trainRunRepository;

    @Test
    public void testOK() throws Exception {
    	try {
    		trainRunRepository.findAll();			
		} catch (Exception e) {
			throw new Exception(e);
		}
    }
}