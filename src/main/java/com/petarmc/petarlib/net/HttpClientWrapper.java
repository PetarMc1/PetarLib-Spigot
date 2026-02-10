package com.petarmc.petarlib.net;

import com.petarmc.petarlib.Config;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import static com.petarmc.petarlib.PetarLib.getPlugin;
// Warrning:
// This class was copied exactly like it was from PetarLib Farbic and the only chnages made were to the logging!
/**
 * A simple wrapper around Java's HttpClient with support for retries and asycn execution.
 */
public class HttpClientWrapper {
    private final HttpClient client;
    private final int maxRetries;
    private final ExecutorService executor;

    /**
     * Creates a new HttpClientWrapper with a default maximum number of retries from the plugin config.
     * Callers can use the no-arg constructor to pick up the default, or the (int) constructor to override it.
     */
    public HttpClientWrapper() {
        this(Config.defaultMaxRetries);
    }

    /**
     * Creates a new HttpClientWrapper with a specified maximum number of retries.
     *
     * @param maxRetries maximum number of attempts for failed reqs, minimum 1.
     * A default value can be set in the plugin's config.yml
     */
    public HttpClientWrapper(int maxRetries) {
        this.client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.executor = Executors.newCachedThreadPool();
        this.maxRetries = Math.max(1, maxRetries);
    }

    /**
     * Sends an async GET request to the specified URL.
     *
     * @param url the target URL
     * @return a CompletableFuture containing the response body
     */
    public CompletableFuture<String> get(String url) {
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .timeout(Duration.ofSeconds(20))
                .build();
        return sendWithRetry(req, 1);
    }

    /**
     * Sends an async POST request using the provided HttpRequest.
     *
     * @param request the HttpRequest to send
     * @return a CompletableFuture containing the response body
     */
    public CompletableFuture<String> post(HttpRequest request) {
        return sendWithRetry(request, 1);
    }

    private CompletableFuture<String> sendWithRetry(HttpRequest req, int attempt) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());
                return resp.body();
            } catch (Exception e) {
                if (attempt >= maxRetries) {
                    getPlugin().getLogger().severe("HttpClient failed after retries: " + e.getMessage());
                }
                getPlugin().getLogger().severe("HttpClient attempt " + attempt + " failed: " + e.getMessage());
                return sendWithRetry(req, attempt + 1).join();
            }
        }, executor);
    }

    /**
     * Shuts down the executor used for async HTTP requests.
     */
    public void shutdown() {
        if (Config.debugMode) { getPlugin().getLogger().info("Shutting down HttpClient executor"); }
        executor.shutdownNow();
    }
}
