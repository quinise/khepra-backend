package com.khepraptah.khepra_site_backend.security;
public class KeyGeneratorApp {
    public static void main(String[] args) {
        try {
            String secretKey = SecurityUtils.generateSecretKey();

            System.out.println("🔐 Generated Secret Key: " + secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}