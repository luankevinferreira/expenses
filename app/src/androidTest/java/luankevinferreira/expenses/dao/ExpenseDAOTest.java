package luankevinferreira.expenses.dao;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import luankevinferreira.expenses.dao.ExpenseDAO;
import luankevinferreira.expenses.domain.Expense;
import luankevinferreira.expenses.domain.Type;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Luan Kevin Ferreira on 12/25/2016.
 *
 * @see ExpenseDAO
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ExpenseDAOTest {

    private ExpenseDAO expenseDAO;

    @Before
    public void setUp() {
        expenseDAO = new ExpenseDAO(InstrumentationRegistry.getTargetContext());
        expenseDAO.deleteAll();
    }

    @After
    public void finish() {
        expenseDAO.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(expenseDAO);
    }

    @Test
    public void testShouldAddExpenseType() throws Exception {
        // Prepare
        double value = 100.0;
        String type = "Type";
        long id = 123456;
        String description = "Description";
        Date date = new Date();

        Expense expense = new Expense();
        expense.setValue(value);
        expense.setType(type);
        expense.setId(id);
        expense.setDescription(description);
        expense.setDate(date);

        // Action
        expenseDAO.insert(expense);
        List<Type> rate = expenseDAO.selectTypesExpenses();

        // Verify
        assertThat(rate.size(), is(1));
        assertEquals(type, rate.get(0).getName());
        expenseDAO.delete(expense);
    }

    @Test
    public void testSelectOnlyTypesOfActualMonth() throws Exception {
        // Prepare
        Calendar dateActual = Calendar.getInstance();
        Calendar dateAnotherMonth = Calendar.getInstance();
        dateAnotherMonth.set(Calendar.MONTH, (Calendar.MONTH - 3));

        String typeLastMonth = "TypeLastMonth";
        String typeActualMonth = "TypeActualMonth";

        String description = "Description";
        double value = 123.45;
        long id = 12345;

        Expense expenseLastMonth = new Expense();
        expenseLastMonth.setDate(dateAnotherMonth.getTime());
        expenseLastMonth.setDescription(description);
        expenseLastMonth.setValue(value);
        expenseLastMonth.setId(id);
        expenseLastMonth.setType(typeLastMonth);

        Expense expenseActualMonth = new Expense();
        expenseActualMonth.setDate(dateActual.getTime());
        expenseActualMonth.setDescription(description);
        expenseActualMonth.setId(id);
        expenseActualMonth.setValue(value);
        expenseActualMonth.setType(typeActualMonth);

        expenseDAO.insert(expenseLastMonth);
        expenseDAO.insert(expenseActualMonth);

        // Action
        List<Type> rate = expenseDAO.selectTypesExpenses();

        // Verify
        assertThat(rate.size(), is(1));
        assertEquals(typeActualMonth, rate.get(0).getName());
        expenseDAO.delete(expenseActualMonth);
        expenseDAO.delete(expenseLastMonth);
    }

    @Test
    public void testSelectTotalExpensesOfActualYear() throws Exception {
        // Prepare
        Calendar dateActualYear = Calendar.getInstance();
        Calendar dateLastYear = Calendar.getInstance();
        dateLastYear.set(Calendar.YEAR, (Calendar.YEAR - 1));

        String description = "Description";
        String type = "Type";
        double value = 12345;
        int id1 = 1, id2 = 2;

        Expense expenseActualYear = new Expense();
        expenseActualYear.setDate(dateActualYear.getTime());
        expenseActualYear.setType(type);
        expenseActualYear.setValue(value);
        expenseActualYear.setId(id1);
        expenseActualYear.setDescription(description);

        Expense expenseLastYear = new Expense();
        expenseLastYear.setDate(dateLastYear.getTime());
        expenseLastYear.setDescription(description);
        expenseLastYear.setValue(value);
        expenseLastYear.setType(type);
        expenseLastYear.setId(id2);

        expenseDAO.insert(expenseLastYear);
        expenseDAO.insert(expenseActualYear);

        // Action
        double totalSelected = expenseDAO.selectTotalMonth(new Date(), null);

        // Verify
        assertEquals(value, totalSelected);
        expenseDAO.delete(expenseActualYear);
        expenseDAO.delete(expenseLastYear);
    }
}
