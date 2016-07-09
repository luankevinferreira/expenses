package luankevinferreira.expenses.domain;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {

    private static final long serialVersionUID = -2649626479524569980L;

    private long id;
    private String description;
    private double value;
    private Date date;
    private String type;

    public Expense() {
        date = new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", value=" + value +
                ", date=" + date +
                ", type='" + type + '\'' +
                '}';
    }
}
