package com.example.blobcert;

import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.azure.core.http.HttpClient;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.util.internal.EmptyArrays;

@SpringBootApplication
public class BlobCertApplication implements CommandLineRunner {
	private static Logger LOG = LoggerFactory.getLogger(BlobCertApplication.class);
	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(BlobCertApplication.class, args);
	}

	@Override
	public void run(String... args) throws NoSuchAlgorithmException {
		String endpoint = env.getProperty("STORAGE_ENDPOINT");
		String sasToken = env.getProperty("STORAGE_SASTOKEN");
		String containerName = env.getProperty("STORAGE_CONTAINER_NAME");
		//SSLContext sc = SSLContext.getInstance("TLS");
		reactor.netty.http.client.HttpClient baseHttpClient = reactor.netty.http.client.HttpClient.create().resolver(io.netty.resolver.DefaultAddressResolverGroup.INSTANCE);
		baseHttpClient = baseHttpClient.secure(sslContextSpec -> {
			try {
				// TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
				TrustManager tm = new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] chain, String s) {
						for (X509Certificate cert : chain) {
							LOG.info("Accepting a server certificate: " + cert.getSubjectDN());
							LOG.info("IssuerDN: " + cert.getIssuerDN());
						}
					}
					@Override
					public void checkServerTrusted(X509Certificate[] chain, String s) {
						for (X509Certificate cert : chain) {
							LOG.info("Accepting a server certificate: " + cert.getSubjectDN());
							LOG.info("IssuerDN: " + cert.getIssuerDN());
						}
					}
					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return EmptyArrays.EMPTY_X509_CERTIFICATES;
					}
				};
				SslContextBuilder sslContextBuilder = SslContextBuilder.forClient();
				// sslContextBuilder.trustManager(tmf);
				sslContextBuilder.trustManager(tm);
				sslContextSpec.sslContext(sslContextBuilder.build());
			} catch (SSLException ex) {
				LOG.error("Error in building the SSL context", ex);
			// } catch (NoSuchAlgorithmException ex) {
			// 	LOG.error("Error in building the SSL context", ex);
			}
		});
		HttpClient httpClient = new NettyAsyncHttpClientBuilder(baseHttpClient)
			.build();
		BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
			.httpClient(httpClient)
			.endpoint(endpoint)
			.sasToken(sasToken)
			.buildClient();
		BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
		for (BlobItem blobItem : blobContainerClient.listBlobs()) {
			LOG.info("{}", blobItem.getName());
		}
	}

}
