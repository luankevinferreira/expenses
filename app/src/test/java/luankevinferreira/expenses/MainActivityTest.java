package luankevinferreira.expenses;

import android.view.MenuItem;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class MainActivityTest {

    @Test
    public void when_item_menu_true_selected_will_be_change_selected_to_false() {
        // Prepare
        MainActivity mainActivity = new MainActivity();
        MenuItem menuItem = Mockito.mock(MenuItem.class);
        menuItem.setChecked(true);

        // Action
        mainActivity.onOptionsItemSelected(menuItem);

        // Verify
        Assert.assertFalse(menuItem.isChecked());
    }
}
