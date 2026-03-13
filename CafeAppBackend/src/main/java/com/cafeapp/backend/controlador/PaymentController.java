package com.cafeapp.backend.controlador;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cafeapp.backend.dto.pago.PaymentIntentRequest;
import com.cafeapp.backend.modelo.Producto;
import com.cafeapp.backend.repositorio.ProductoRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.net.RequestOptions;
import com.stripe.param.PaymentIntentCreateParams;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pagos")
public class PaymentController {
    
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    private final ProductoRepository productoRepository;

    public PaymentController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Operation(summary = "Crear intento de pago con Stripe")
    @PostMapping("/crear-intento")
    public Map<String, String> crearIntentoPago(@Valid @RequestBody PaymentIntentRequest request) throws StripeException {
        
        // ✅ Obtener usuario autenticado
        String emailActual = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        
        if (request.items() == null || request.items().isEmpty()) {
            throw new RuntimeException("No se enviaron productos para el pago");
        }

        long totalEnCentimos = 0;
        StringBuilder detalles = new StringBuilder();

        for (var item : request.items()) {
            Producto producto = productoRepository.findById(item.productoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + item.productoId()));

            // ✅ Nota: Validación de stock se debe hacer en StockCentroService/Controller
            // ya que el stock es por centro, no global

            long precioEnCentimos = (long) (producto.getPrecioBase() * 100);
            totalEnCentimos += precioEnCentimos * item.cantidad();
            detalles.append(producto.getNombre()).append("x").append(item.cantidad()).append(", ");
        }

        long impuesto = (long) (totalEnCentimos * 0.07);
        long totalFinal = totalEnCentimos + impuesto;

        try {
            PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                    .setAmount(totalFinal)
                    .setCurrency("eur")
                    .setAutomaticPaymentMethods(
                            PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                    .setEnabled(true)
                                    .build()
                    )
                    .putMetadata("usuarioEmail", emailActual)
                    .putMetadata("detalles", detalles.toString())
                    .build();

            // ✅ Usar RequestOptions con la Secret Key (nunca exponer Stripe.apiKey en variables globales)
            PaymentIntent paymentIntent = PaymentIntent.create(
                params,
                RequestOptions.builder().setApiKey(stripeSecretKey).build()
            );

            logger.info("PaymentIntent creado exitosamente: {} para usuario: {}",
                paymentIntent.getId(), emailActual);

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());
            return response;
            
        } catch (StripeException e) {
            logger.error("Error creando PaymentIntent para usuario: {}", emailActual, e);
            throw new RuntimeException("Error procesando pago. Intenta nuevamente.");
        }
    }
}
