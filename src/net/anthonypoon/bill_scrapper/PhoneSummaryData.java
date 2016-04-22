/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anthonypoon.bill_scrapper;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author anthony.poon
 */
public class PhoneSummaryData {
    private Map<String, Float> db = new HashMap();
    public void addEntry(String phoneNumber, String fee) {
        db.put(phoneNumber, Float.parseFloat(fee));
    }
    
    public void dump() {
        for (Map.Entry<String, Float> entry : db.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}