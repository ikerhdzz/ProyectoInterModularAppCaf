package com.cafeapp.backend.servicio;

import com.cafeapp.backend.dto.pedido.PedidoTotales;
import com.cafeapp.backend.modelo.Pedido;
import com.cafeapp.backend.repositorio.PedidoRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio encargado de gestionar los pagos mediante Stripe.
 *
 * Funcionalidades:
 * - Crear PaymentIntent para un pedido
 * - Calcular totales antes del pago
 */
@Service
public class PagoService {

    private final PedidoRepository pedidoRepository;
    private final PedidoTotalsService pedidoTotalsService;

    /**
     * Constructor que inicializa Stripe con la API Key.
     */
    public PagoService(
            PedidoRepository pedidoRepository,
            PedidoTotalsService pedidoTotalsService,
            @Value("${stripe.api.key}") String stripeApiKey
    ) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoTotalsService = pedidoTotalsService;
        Stripe.apiKey = stripeApiKey;
    }

    /**
     * Crea un PaymentIntent en Stripe para un pedido.
     *
     * @param pedidoId ID del pedido
     * @return mapa con clientSecret, pedidoId y amount
     */
    public Map<String, Object> crearPaymentIntent(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        PedidoTotales totales = pedidoTotalsService.calcularTotales(pedidoId);

        long amountInCents = Math.round(totales.totalFinal() * 100);

        try {
            PaymentIntentCreateParams params =
                    PaymentIntentCreateParams.builder()
                            .setAmount(amountInCents)
                            .setCurrency("eur")
                            .putMetadata("pedidoId", pedido.getId().toString())
                            .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("clientSecret", paymentIntent.getClientSecret());
            respuesta.put("pedidoId", pedido.getId());
            respuesta.put("amount", amountInCents);
            return respuesta;

        } catch (Exception e) {
            throw new RuntimeException("Error creando PaymentIntent: " + e.getMessage(), e);
        }
    }
}
