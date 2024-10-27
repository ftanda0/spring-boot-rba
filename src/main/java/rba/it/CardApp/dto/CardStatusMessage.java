package rba.it.CardApp.dto;

public class CardStatusMessage {
    private String oib;
    private String status;

    public CardStatusMessage() {
    }

    public CardStatusMessage(String oib, String status) {
        this.oib = oib;
        this.status = status;
    }

    // Getteri i setteri

    public String getOib() {
        return oib;
    }

    public void setOib(String oib) {
        this.oib = oib;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
