package io.areguig.jpmtest1.to;

import java.time.LocalDate;

/**
 * The object representing an Instruction in the system.
 */

public class Instruction {

    private String entity;
    private InstructionType type;
    private double agreedFx;
    private String currency;
    private LocalDate instructionDate;
    private LocalDate settlementDate;
    private double units;
    private double price;

    @Override
    public String toString(){
        return "{ entity : "+entity+ ", type : "+ type.toString()+
                ", agreedFx : "+agreedFx+", currency : "+currency+
                ", instructionDate : "+instructionDate+", settlementDate : "+settlementDate+
                ", units : "+units+", price : "+price+" }";
    }

    /**
     * Getters and setters
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public void setAgreedFx(double agreedFx) {
        this.agreedFx = agreedFx;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setInstructionDate(LocalDate instructionDate) {
        this.instructionDate = instructionDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public void setUnits(double units) {
        this.units = units;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public InstructionType getType() {
        return type;
    }

    public double getAgreedFx() {
        return agreedFx;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDate getInstructionDate() {
        return instructionDate;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public double getUnits() {
        return units;
    }

    public double getPrice() {
        return price;
    }

    public String getEntity() {
        return entity;
    }
}
