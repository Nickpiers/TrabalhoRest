package ReservaCruzeiros.Pagamento;

class NotificacaoPagamentoDTO {
    private Long idReserva;
    private String status;
    private PagamentoDTO pagamento;

    @Override
    public String toString() {
        return "idReserva=" + idReserva + ", status=" + status;
    }

    public NotificacaoPagamentoDTO(Long idReserva, String status, PagamentoDTO pagamento) {
        this.idReserva = idReserva;
        this.status = status;
        this.pagamento = pagamento;
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

    public PagamentoDTO getPagamento() {
        return pagamento;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

