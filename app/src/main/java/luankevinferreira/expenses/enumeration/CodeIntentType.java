package luankevinferreira.expenses.enumeration;

public enum CodeIntentType {

    STATUS_OK(200),
    STATUS_ERROR(500),
    REQUEST_NEW_EXPENSE(1000),
    REQUEST_EDIT_EXPENSE(1001),
    REQUEST_DETAIL_EXPENSES(1002),
    REQUEST_NEW_EXPENSE_TYPE(1003),
    REQUEST_EXPORT_EXPENSES(1004);

    private final int code;

    CodeIntentType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
