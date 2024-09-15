package net.product.dual.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.icoderman.woocommerce.oauth.OAuthConfig;

@Configuration
public class WoocommerceConfig {

    @Bean
    public OAuthConfig oAuthConfig() {
        String url = System.getenv("WOOCOMMERCE_URL");
        String consumerKey = System.getenv("WOOCOMMERCE_CONSUMER_KEY");
        String consumerSecret = System.getenv("WOOCOMMERCE_CONSUMER_SECRET");

        if (url == null || consumerKey == null || consumerSecret == null) {
            throw new IllegalStateException("WooCommerce configuration environment variables are not set");
        }

        return new OAuthConfig(url, consumerKey, consumerSecret);
    }
}