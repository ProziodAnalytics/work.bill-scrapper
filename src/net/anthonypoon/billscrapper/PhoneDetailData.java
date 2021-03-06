/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.anthonypoon.billscrapper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author anthony.poon
 */
public class PhoneDetailData{
    private String phoneNumber;
    private Integer daypassCount = 0;
    private Float regexSum;
    private Float iddRegexSum;
    private Map<String, Float> chargedItem = new HashMap();
    private Map<String, Float> miscCharge = new HashMap();
    private List<IddDetailData> iddDetail = new ArrayList();
    private List<RoamingDetailData> roamingDetail = new ArrayList();
    public PhoneDetailData(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public Float getFixedMonthly() {
        return chargedItem.get("fixed_monthly_fee") != null ? chargedItem.get("fixed_monthly_fee") : 0.0f;
    }
    
    void setFixedMonthlyFee(String str) {
        chargedItem.put("fixed_monthly_fee", Float.parseFloat(str));
    }

    void setIddFee(String str) {
        chargedItem.put("idd_fee", Float.parseFloat(str));
    }
    
    public Float getIddFee() {
        return chargedItem.get("idd_fee") != null ? chargedItem.get("idd_fee") : 0.0f;
    }    

    void setRoamingVoiceFee(String str) {
        chargedItem.put("roaming_voice_fee", Float.parseFloat(str));
    }

    public Float getRoamingVoiceFee() {
        return chargedItem.get("roaming_voice_fee") != null ? chargedItem.get("roaming_voice_fee") : 0.0f;
    }
    
    void setRoamingDataFee(String str) {
        chargedItem.put("roaming_data_fee", Float.parseFloat(str));
    }

    public Float getRoamingDataFee() {
        return chargedItem.get("roaming_data_fee") != null ? chargedItem.get("roaming_data_fee") : 0.0f;
    }


    void setDayPass(String daypassCount, String daypassFee) {
        this.daypassCount = Integer.parseInt(daypassCount);
        chargedItem.put("daypass_fee", Float.parseFloat(daypassFee));
    }
    
    public Integer getDayPassCount() {
        return this.daypassCount;
    }
    
    public Float getDayPassFee() {
        return chargedItem.get("daypass_fee") != null ? chargedItem.get("daypass_fee") : 0.0f;
    }

    void setIddRoamingDiscount(String str) {
        chargedItem.put("idd_roaming_discount", -Float.parseFloat(str));
    }

    public Float getIddRoamingDiscount() {
        return chargedItem.get("idd_roaming_discount") != null ? chargedItem.get("idd_roaming_discount") : 0.0f;
    }
    
    void setRegexSum(String str) {
        regexSum = Float.parseFloat(str);
    }
    
    void setIddRegexSum(String str) {
        iddRegexSum = Float.parseFloat(str);
    }
    
    public Float getRegexSum() {
        return regexSum;
    }
    
    public Float getIddRegexSum() {
        return iddRegexSum != null ? iddRegexSum : 0.0f;
    }
  
    void addMiscCharge(String itemName, String amount) {
        miscCharge.put(itemName, Float.parseFloat(amount));
    }
    
    public void addIddDetail(IddDetailData dataObj) {
        iddDetail.add(dataObj);
    }
    
    public void addRoamingDetail(RoamingDetailData dataObj) {
        roamingDetail.add(dataObj);
    }
    
    public List getIddDetail() {
        return iddDetail;
    }
    
    public List getRoamingDetail() {
        return roamingDetail;
    }
    
    public void dump() {
        System.out.println("----------------------------------");
        System.out.println("Phone no.: " + phoneNumber);
        System.out.println("dayPass: " + daypassCount);
        for (Map.Entry<String, Float> item : chargedItem.entrySet()) {
            System.out.println(item.getKey() + " -> " + item.getValue());
        }
        for (Map.Entry<String, Float> item : miscCharge.entrySet()) {
            System.out.println(item.getKey() + " -> " + item.getValue());
        }        
        for (RoamingDetailData item : roamingDetail) {
            System.out.println(item.getDateTimeString() + " " + item.getLocation() + " " + item.getMinute() + " " + item.getAmount() + " " + item.getRemark());
        }        
        for (RoamingDetailData item : roamingDetail) {
            System.out.println(item.getDateTimeString() + " " + item.getLocation() + " " + item.getMinute() + " " + item.getAmount() + " " + item.getRemark());
        }
        if (!this.checkSumCharge()) {
            System.out.println("Check sum mismatched");
            /**
            System.out.println("Items:");
            for (Map.Entry<String, Float> item : chargedItem.entrySet()) {
                System.out.println(item.getKey() + " -> " + item.getValue());
            }
            for (Map.Entry<String, Float> item : miscCharge.entrySet()) {
                System.out.println(item.getKey() + " -> " + item.getValue());
            }
            **/
        } else {
            System.out.println("Check sum matched");
        }
        if (!this.checkSumIdd()) {
            System.out.println("IDD Check sum mismatched");
            /**
            System.out.println("Items:");
            for (IddDetailData item : iddDetail) {
                item.dump();
            }
            **/
        } else {
            System.out.println("IDD Check sum matched");
        }
        if (!this.checkSumRoamingDetail()) {
            System.out.println("Roaming Check sum mismatched");
            /**
            System.out.println("Items:");
            for (RoamingDetailData item : roamingDetail) {
                item.dump();
            }
            **/
        } else {
            System.out.println("Roaming Check sum matched");
        }
    }
    
    public boolean checkSum() {
        return checkSumCharge() && checkSumIdd() && checkSumRoamingDetail();
    }
    
    public boolean checkSumCharge() {
        Float sumOfItem = 0.00f;
        
        //Float sumOfItem = 0.00f;
        for (Map.Entry<String, Float> item : chargedItem.entrySet()) {
            sumOfItem += item.getValue();
        }
        for (Map.Entry<String, Float> item : miscCharge.entrySet()) {
            sumOfItem += item.getValue();
        }
        return Math.abs(regexSum - sumOfItem) < 0.01;
    }
    
    public boolean checkSumIdd() {
        if (!iddDetail.isEmpty()) {
            Float sumOfIdd = 0.00f;
            for (IddDetailData item : iddDetail) {
                sumOfIdd += item.getAmount();
            }        
            return Math.abs(iddRegexSum - sumOfIdd) < 0.01;
        } else {
            return true;
        }
    }
    
    public boolean checkSumRoamingDetail(){
        Float sum = 0.0f;
        for (RoamingDetailData dataObj : roamingDetail) {
            sum += dataObj.getAmount();
        }
        return Math.abs(getRoamingVoiceFee() + getRoamingDataFee() - sum) < 0.01;
    }
}
