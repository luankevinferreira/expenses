package luankevinferreira.expenses.domain;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ExpenseTest {

    @Test
    public void testTheClassProperties() {
        // Prepare
        String description = "Description";
        Date date = new Date();
        long id = 1234567890;
        String type = "Type";
        double value = 123456.123;

        Expense expense = new Expense();

        // Action
        expense.setDescription(description);
        expense.setDate(date);
        expense.setId(id);
        expense.setType(type);
        expense.setValue(value);

        // Verify
        assertEquals(description, expense.getDescription());
        assertEquals(date.getTime(), expense.getDate().getTime());
        assertEquals(id, expense.getId());
        assertEquals(type, expense.getType());
        assertEquals(value, expense.getValue(), 0);
    }

    @Test
    public void whenCallConstructorMustReturnDateAttributeAndToStringNotNull() {
        // Prepare
        Expense expense;

        // Action
        expense = new Expense();

        // Verify
        assertNotNull(expense.getDate());
        assertNotNull(expense.toString());
    }
}
