package com.cafeapp.backend.controlador;

import com.cafeapp.backend.servicio.StripeWebhookService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stripe")
public class StripeWebhookController {

    private final StripeWebhookService stripeWebhookService;

    public StripeWebhookController(StripeWebhookService stripeWebhookService) {
        this.stripeWebhookService = stripeWebhookService;
    }

    @Operation(summary = "Recibir eventos de Stripe (webhook)")
    @PostMapping("/webhook")
    public ResponseEntity<String> recibirWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader
    ) {
        stripeWebhookService.procesarEvento(payload, sigHeader);
        return ResponseEntity.ok("ok");
    }
}
