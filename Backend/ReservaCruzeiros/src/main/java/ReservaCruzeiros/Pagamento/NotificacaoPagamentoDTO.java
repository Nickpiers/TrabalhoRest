package ReservaCruzeiros.Pagamento;

class NotificacaoPagamentoDTO {
    private Long idReserva;
    private String status;

    @Override
    public String toString() {
        return "idReserva=" + idReserva + ", status=" + status;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

