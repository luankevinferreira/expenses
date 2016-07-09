package luankevinferreira.expenses.domain;

import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TypeTest {

    @Test
    public void test_the_class_properties() {
        // Prepare
        int id = 2;
        String name = "Luan";

        Type type = new Type();

        // Action
        type.setId(id);
        type.setName(name);

        // Verify
        assertEquals(id, type.getId());
        assertEquals(name, type.getName());
    }
}
