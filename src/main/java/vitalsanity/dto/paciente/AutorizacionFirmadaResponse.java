package vitalsanity.dto.paciente;

public class AutorizacionFirmadaResponse {
    private String pdfBase64;
    private Long idSolicitud;

    public AutorizacionFirmadaResponse(String pdfBase64, Long idSolicitud) {
        this.pdfBase64 = pdfBase64;
        this.idSolicitud = idSolicitud;
    }

    public String getPdfBase64() {
        return pdfBase64;
    }

    public Long getIdSolicitud() {
        return idSolicitud;
    }
}

