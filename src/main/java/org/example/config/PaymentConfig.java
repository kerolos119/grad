package org.example.config;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

@Configuration
public class PaymentConfig {
    
    @Value("${payment.stripe.api-key}")
    private String stripeApiKey;
    
    @Value("${payment.stripe.webhook-secret}")
    private String stripeWebhookSecret;
    
    @Value("${payment.stripe.currency:USD}")
    private String stripeCurrency;
    
    /**
     * Initialize Stripe API with the configured API key
     */
    @PostConstruct
    public void initStripe() {
        Stripe.apiKey = stripeApiKey;
    }
    
    /**
     * Get the configured webhook secret for signature verification
     */
    @Bean
    public String stripeWebhookSecret() {
        return stripeWebhookSecret;
    }
    
    /**
     * Get the configured currency for payments
     */
    @Bean
    public String stripeCurrency() {
        return stripeCurrency;
    }
} 