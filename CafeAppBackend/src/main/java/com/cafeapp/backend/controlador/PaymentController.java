package com.cafeapp.backend.controlador;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.cafeapp.backend.repositorio.ProductoRepository;
import com.cafeapp.backend.modelo.Producto;

import java.util.HashMap;
import java.util.*;

@RestController
@RequestMapping("/api/pagos")
public class PaymentController {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    // Inyectamos el repositorio para consultar los precios reales
    private final ProductoRepository productoRepository;

    public PaymentController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @PostMapping("/crear-intento")
    public Map<String, String> crearIntentoPago(@RequestBody Map<String, Object> datos) throws Exception {
        Stripe.apiKey = stripeApiKey;

        // 1. Extraemos los items que envía el frontend
        List<Map<String, Object>> items = (List<Map<String, Object>>) datos.get("items");
        
        // 2. RECALCULAMOS EL TOTAL DESDE LA BASE DE DATOS
        long totalEnCentimos = 0;

        for (Map<String, Object> item : items) {
            Long id = Long.parseLong(item.get("id").toString());
            int cantidad = Integer.parseInt(item.get("cantidad").toString());

            // Buscamos el producto en la BD de Supabase
            Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));

            // Sumamos al total (Precio en BD * Cantidad * 100 para céntimos)
            totalEnCentimos += (long) (producto.getPrecio() * 100 * cantidad);
        }

        // 3. Aplicamos impuestos si fuera necesario (ej. 7% IGIC)
        long impuesto = (long) (totalEnCentimos * 0.07);
        long totalFinal = totalEnCentimos + impuesto;

        // 4. Creamos la intención en Stripe con el precio REAL verificado
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
