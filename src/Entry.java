import java.util.ArrayList;

public class Entry {
    public String date;
    public String name;
    public String credit;
    public String debit;
    public String total;
    public boolean exists;
    public int catNum;

    public Entry(String date, String name, String credit, String debit, String total, ArrayList<Category> c){
        this.date = date;
        this.name = name;
        this.credit = credit;
        this.debit = debit;
        this.total = total;
        for (Category cat : c) {
            for (String s : cat.retailers) {
                if (s.equals(name)) {
                    exists = true;
                    catNum = c.indexOf(cat);
                }
            }
        }
    }
}
