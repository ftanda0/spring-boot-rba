package rba.it.CardApp.dto;

public class ErrorResponseDTO {
    private String code;
    private String id;
    private String description;

    // Konstruktor, getteri i setteri
    public ErrorResponseDTO(String code, String id, String description) {
        this.code = code;
        this.id = id;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}