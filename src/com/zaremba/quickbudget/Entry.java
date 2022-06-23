/**
 * Entry into a months budget.  Contains a categroy, a retailer and the amount spent
 */

package com.zaremba.quickbudget;

public class Entry {
    private Category category;
    private Retailer retailer;
    private String date;
    private double spent;
    private boolean isCredit;

    public Entry(Category category, Retailer retailer, String date, double spent, boolean isCredit) {
        this.category = category;
        this.retailer = retailer;
        this.date = date;
        this.spent = spent;
        this.isCredit = isCredit;
    }

    public Category getCategory() {
        return category;
    }

    public boolean getIsCredit(){
        return isCredit;
    }

    public Retailer getRetailer() {
        return retailer;
    }

    public String getDate() {
        return date;
    }

    public double getSpent() {
        return spent;
    }

    public void printEntry(){
        if (isCredit) {
            System.out.printf("%-36s %-36s %-36s\n","Category: " + category.getName(),"Retailer: " + retailer.getName(), "Credit: " + spent);
        }else{
            System.out.printf("%-36s %-36s %-36s\n","Category: " + category.getName(),"Retailer: " + retailer.getName(), "Debit: " + spent);
        }
    }
}
