package luankevinferreira.expenses.enumeration;

import java.util.Arrays;
import java.util.List;

public enum LabelsType {

    JANUARY(0, "jan"),
    FEBRUARY(1, "fev"),
    MARCH(2, "mar"),
    APRIL(3, "abr"),
    MAY(4, "mai"),
    JUNE(5, "jun"),
    JULY(6, "jul"),
    AUGUST(7, "ago"),
    SEPTEMBER(8, "set"),
    OCTOBER(9, "out"),
    NOVEMBER(10, "nov"),
    DECEMBER(11, "dez");

    private final int code;
    private final String month;

    LabelsType(int code, String month) {
        this.code = code;
        this.month = month;
    }

    public int getCode() {
        return this.code;
    }

    public String getMonth() {
        return month;
    }

    public static String getLabel(int code) {
        List<LabelsType> labelsTypes = Arrays.asList(LabelsType.values());

        for (LabelsType type : labelsTypes) {
            if (code == type.getCode()) {
                return type.getMonth();
            }
        }
        return "";
    }
}
