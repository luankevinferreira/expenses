package luankevinferreira.expenses.enumeration;

public enum ExtraType {

    EXPENSE("expense");

    private final String extra;

    ExtraType(String extra) {
        this.extra = extra;
    }

    public String getExtraAttribute() {
        return this.extra;
    }
}
