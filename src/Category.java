import java.util.ArrayList;

public class Category {
    public String name;
    public ArrayList<String> retailers;
    public double goal;
    double actual;

    Category(String name,double goal) {
        retailers = new ArrayList<>();
        this.name = name;
        this.goal = goal;
        actual = 0;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public void addRetailer(String retailer){
        retailers.add(retailer);
    }

    public boolean ifRetailerExists(String retailer) {
        return retailers.contains(retailer);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Category) {
            Category other = (Category) obj;
            if (other.name.equals(this.name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
