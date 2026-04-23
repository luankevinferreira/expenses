package luankevinferreira.expenses.util;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;

import luankevinferreira.expenses.domain.Expense;

import static org.junit.Assert.assertEquals;

public class ExpenseCsvUtilsTest {

    @Test
    public void testShouldBuildHeaderOnlyWhenExpensesListIsEmpty() {
        assertEquals("id,date,description,type,value\n",
                ExpenseCsvUtils.buildCsv(Collections.<Expense>emptyList()));
    }

    @Test
    public void testShouldBuildCsvEscapingTextAndKeepingStableFormats() {
        Expense coffee = new Expense();
        coffee.setId(1);
        coffee.setDate(new GregorianCalendar(2026, Calendar.APRIL, 22, 10, 15, 30).getTime());
        coffee.setDescription("Coffee, \"large\"");
        coffee.setType("Food");
        coffee.setValue(12.5d);

        Expense ticket = new Expense();
        ticket.setId(2);
        ticket.setDate(new GregorianCalendar(2026, Calendar.JANUARY, 5, 8, 0, 0).getTime());
        ticket.setDescription("Bus\nTicket");
        ticket.setType("Transport");
        ticket.setValue(7d);

        String expected = "id,date,description,type,value\n"
                + "1,2026-04-22 10:15:30,\"Coffee, \"\"large\"\"\",Food,12.50\n"
                + "2,2026-01-05 08:00:00,\"Bus\nTicket\",Transport,7.00\n";

        assertEquals(expected, ExpenseCsvUtils.buildCsv(Arrays.asList(coffee, ticket)));
    }

    @Test
    public void testShouldWriteCsvUsingUtf8() throws Exception {
        Expense expense = new Expense();
        expense.setId(3);
        expense.setDate(new GregorianCalendar(2026, Calendar.MARCH, 1, 9, 45, 0).getTime());
        expense.setDescription("Pao de queijo");
        expense.setType("Alimentacao");
        expense.setValue(5.25d);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        ExpenseCsvUtils.write(Collections.singletonList(expense), outputStream);

        assertEquals(ExpenseCsvUtils.buildCsv(Collections.singletonList(expense)),
                outputStream.toString(StandardCharsets.UTF_8.name()));
    }
}
