package com.cafeapp.backend.dto.ticket;

public record TicketResponseDTO(
        Long id,
        Long pedidoId,
        String numeroTicket,
        String nombreCentroCopia,
        String nombreClienteCopia,
        String cursoClienteCopia,
        String tipoTicket,
        String fechaEmision,
        Double totalPagado
) {}
