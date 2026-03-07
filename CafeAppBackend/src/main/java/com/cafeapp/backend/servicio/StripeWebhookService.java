package com.cafeapp.backend.servicio;

import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.repositorio.PedidoRepository;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Servicio encargado de procesar los eventos enviados por Stripe
 * a través del webhook configurado.
 *
 * Actualmente maneja:
 * - payment_intent.succeeded → cambia el pedido a PREPARANDO
 */
@Service
public class StripeWebhookService {

    private final PedidoRepository pedidoRepository;
    private final PedidoStateService pedidoStateService;
    private final String webhookSecret;

    /**
     * Constructor con inyección de dependencias.
     */
    public StripeWebhookService(
            PedidoRepository pedidoRepository,
            PedidoStateService pedidoStateService,
            @Value("${stripe.webhook.secret}") String webhookSecret
    ) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoStateService = pedidoStateService;
        this.webhookSecret = webhookSecret;
    }

    /**
     * Procesa un evento enviado por Stripe.
     *
     * @param payload cuerpo del webhook
     * @param sigHeader cabecera de firma enviada por Stripe
     */
    public void procesarEvento(String payload, String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException("Firma de webhook inválida");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {

            PaymentIntent intent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (intent != null) {
                String pedidoIdStr = intent.getMetadata().get("pedidoId");

                if (pedidoIdStr != null) {
                    Long pedidoId = Long.valueOf(pedidoIdStr);

                    Pedido pedido = pedidoRepository.findById(pedidoId)
                            .orElseThrow(() -> new RuntimeException("Pedido no encontrado para webhook"));

                    // Acción por defecto: pasar pedido a PREPARANDO
                    pedidoStateService.cambiarEstado(pedidoId, "PREPARANDO");
                }
            }
        }
    }
}
