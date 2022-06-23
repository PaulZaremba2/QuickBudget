/**
 * Simple class to represent a retailer and which category of the budget it falls under
 */

package com.zaremba.quickbudget;

public class Retailer {
    private String name;
    private Category category;

    public Retailer(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    /**
     * Improve this to determine if a portion of the string is the same.
     * Issues with retailers only change numbers and small parts even though same retailer
     * Be nice to be able to check that for equality
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Retailer) {
            Retailer other = (Retailer) obj;
            if (this.name.equals(other.name)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }
}
