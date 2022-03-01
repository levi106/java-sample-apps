package com.example.kvtest.controller;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("secret")
public class SecretController {
    @Value("${secret1}")
    private String mySecretProperty;

    @RequestMapping(method=RequestMethod.GET)
    public String get() {
        // SecretClient secretClient = new SecretClientBuilder()
        //     .vaultUrl("https://kvtest220225.vault.azure.net/")
        //     .credential(new DefaultAzureCredentialBuilder().build())
        //     .buildClient();
        // KeyVaultSecret secret = secretClient.getSecret("secret1");
        // return secret.getValue();
        return mySecretProperty;
    }
}
