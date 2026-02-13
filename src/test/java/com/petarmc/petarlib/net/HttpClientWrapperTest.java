package com.petarmc.petarlib.net;

import com.petarmc.petarlib.PetarLib;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HttpClientWrapperTest {

    @BeforeEach
    void setUpPluginLogger() throws Exception {
        PetarLib plugin = mock(PetarLib.class);
        when(plugin.getLogger()).thenReturn(Logger.getLogger("test"));
        setPlugin(plugin);
    }

    @AfterEach
    void tearDownPluginLogger() throws Exception {
        setPlugin(null);
    }

    @Test
    void get_retriesThenSucceeds() throws Exception {
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResp = mock(HttpResponse.class);
        when(mockResp.body()).thenReturn("ok");
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenThrow(new IOException("fail once"))
                .thenReturn(mockResp);

        HttpClientWrapper wrapper = new HttpClientWrapper(3);
        setField(wrapper, "client", mockClient);

        String body = wrapper.get("https://example.com").get(1, TimeUnit.SECONDS);

        assertEquals("ok", body);
        verify(mockClient, times(2)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        wrapper.shutdown();
    }

    @Test
    void post_usesProvidedRequestAndReturnsBody() throws Exception {
        HttpClient mockClient = mock(HttpClient.class);
        HttpResponse<String> mockResp = mock(HttpResponse.class);
        when(mockResp.body()).thenReturn("posted");
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResp);

        HttpClientWrapper wrapper = new HttpClientWrapper(1);
        setField(wrapper, "client", mockClient);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://example.com/submit"))
                .timeout(Duration.ofSeconds(5))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        String body = wrapper.post(request).get(1, TimeUnit.SECONDS);

        assertEquals("posted", body);
        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
        wrapper.shutdown();
    }

    @Test
    void shutdown_isIdempotent() throws Exception {
        HttpClientWrapper wrapper = new HttpClientWrapper(1);
        assertDoesNotThrow(wrapper::shutdown);
        assertDoesNotThrow(wrapper::shutdown);
    }

    private void setField(HttpClientWrapper target, String fieldName, Object value) throws Exception {
        Field f = HttpClientWrapper.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    private void setPlugin(PetarLib plugin) throws Exception {
        Field f = PetarLib.class.getDeclaredField("plugin");
        f.setAccessible(true);
        f.set(null, plugin);
    }
}

