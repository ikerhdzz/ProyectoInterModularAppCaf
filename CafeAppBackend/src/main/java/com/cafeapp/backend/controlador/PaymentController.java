package com.cafeapp.backend.controlador;

import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.repositorio.ProductoRepository;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/pagos")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private final ProductoRepository productoRepository;

    public PaymentController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Operation(summary = "Crear intento de pago con Stripe")
    @PostMapping("/crear-intento")
    public Map<String, String> crearIntentoPago(@Valid @RequestBody Map<String, Object> datos) throws Exception {

        Stripe.apiKey = stripeApiKey;

        List<Map<String, Object>> items = (List<Map<String, Object>>) datos.get("items");
        if (items == null || items.isEmpty()) {
            throw new RuntimeException("No se enviaron productos para el pago");
        }

        long totalEnCentimos = 0;

        for (Map<String, Object> item : items) {

            Long id = Long.parseLong(item.get("id").toString());
            int cantidad = Integer.parseInt(item.get("cantidad").toString());

            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));

            totalEnCentimos += (long) (producto.getPrecioBase() * 100 * cantidad);
        }

        long impuesto = (long) (totalEnCentimos * 0.07);
        long totalFinal = totalEnCentimos + impuesto;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalFinal)
                .setCurrency("eur")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Map<String, String> response = new HashMap<>();
        response.put("clientSecret", paymentIntent.getClientSecret());
        return response;
    }
}
