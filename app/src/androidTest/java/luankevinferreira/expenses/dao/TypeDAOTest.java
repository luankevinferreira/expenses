package luankevinferreira.expenses.dao;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import luankevinferreira.expenses.domain.Type;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Luan Kevin Ferreira.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class TypeDAOTest {

    private TypeDAO typeDAO;

    @Before
    public void setUp() {
        typeDAO = new TypeDAO(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void finish() throws IOException {
        typeDAO.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(typeDAO);
    }

    @Test
    public void testFindAllDescriptions() throws Exception {
        // Prepare
        String expectedName = "Food";

        // Action
        List<String> descriptions = typeDAO.findAllDescriptions();

        // Verify
        assertNotNull(descriptions);
        assertFalse(descriptions.isEmpty());
        assertEquals(expectedName, descriptions.get(0));
    }
}
