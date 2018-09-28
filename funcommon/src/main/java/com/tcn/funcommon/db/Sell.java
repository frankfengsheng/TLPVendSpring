package com.tcn.funcommon.db;

public class Sell {
    private int id;
    private int paytype;
    private int result;
    private int up_flag;
    private String Partner;
    private String Termina;
    private String DATA;
    private String Time;
    private String CardNumber;
    private String SerialNumber;
    private String ReferNo;

    public Sell() {
        super();
    }



    public Sell(int id, int paytype, int result, int up_flag, String partner,
                String termina, String dATA, String time, String cardNumber,
                String serialNumber, String referNo) {
        super();
        this.id = id;
        this.paytype = paytype;
        this.result = result;
        this.up_flag = up_flag;
        Partner = partner;
        Termina = termina;
        DATA = dATA;
        Time = time;
        CardNumber = cardNumber;
        SerialNumber = serialNumber;
        ReferNo = referNo;
    }



    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getPaytype() {
        return paytype;
    }
    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }
    public int getResult() {
        return result;
    }
    public void setResult(int result) {
        this.result = result;
    }
    public int getUp_flag() {
        return up_flag;
    }
    public void setUp_flag(int up_flag) {
        this.up_flag = up_flag;
    }


    public String getPartner() {
        return Partner;
    }


    public void setPartner(String partner) {
        Partner = partner;
    }


    public String getTermina() {
        return Termina;
    }


    public void setTermina(String termina) {
        Termina = termina;
    }


    public String getDATA() {
        return DATA;
    }


    public void setDATA(String dATA) {
        DATA = dATA;
    }


    public String getTime() {
        return Time;
    }


    public void setTime(String time) {
        Time = time;
    }


    public String getCardNumber() {
        return CardNumber;
    }


    public void setCardNumber(String cardNumber) {
        CardNumber = cardNumber;
    }


    public String getSerialNumber() {
        return SerialNumber;
    }


    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }


    public String getReferNo() {
        return ReferNo;
    }


    public void setReferNo(String referNo) {
        ReferNo = referNo;
    }


    @Override
    public String toString() {
        return "Sell [id=" + id + ", paytype=" + paytype + ", result=" + result
                + ", up_flag=" + up_flag + ", Partner=" + Partner
                + ", Termina=" + Termina + ", DATA=" + DATA + ", Time=" + Time
                + ", CardNumber=" + CardNumber + ", SerialNumber="
                + SerialNumber + ", ReferNo=" + ReferNo + "]";
    }
}
