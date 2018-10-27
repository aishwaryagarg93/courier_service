package com.swift.courier_service;

import com.networknt.client.Http2Client;
import io.undertow.UndertowOptions;
import io.undertow.client.ClientConnection;
import io.undertow.client.ClientRequest;
import io.undertow.client.ClientResponse;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import org.xnio.IoUtils;
import org.xnio.OptionMap;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class CourierServiceClient {

    static Http2Client client = Http2Client.getInstance();
    static ClientConnection connection;

    public static void main(String[] args) throws Exception {

        //reusedConnection = client.connect(new URI(apiHost), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
        CourierServiceClient e = new CourierServiceClient();
        e.testHttp2Post();
        System.exit(0);
    }

    /**
     * This is an example for post request. Please note that you need to set header TRANSFER_ENCODING
     * and pass the request body into the callback function.
     *
     * @throws Exception Exception
     */
    public void testHttp2Post() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);
        final ClientConnection connection = client.connect(new URI("http://restapi.demoqa.com/customer/register"), Http2Client.WORKER, Http2Client.SSL, Http2Client.BUFFER_POOL, OptionMap.create(UndertowOptions.ENABLE_HTTP2, true)).get();
        final AtomicReference<ClientResponse> reference = new AtomicReference<ClientResponse>();
        try {
            final ClientRequest request = new ClientRequest().setMethod(Methods.POST).setPath("/post");
            request.getRequestHeaders().put(Headers.TRANSFER_ENCODING, "chunked");
            connection.sendRequest(request, client.createClientCallback(reference, latch, "{\n" +
                    "\t\"FirstName\" : \"aish\",\n" +
                    "\t\"LastName\" : \"jain\",\n" +
                    "\t\"UserName\" : \"aishais\",\n" +
                    "\t\"Password\" : \"aishwaihs\",\n" +
                    "\t\"Email\" : \"auish@hs.com\"\n" +
                    "}"));
            latch.await(100, TimeUnit.MILLISECONDS);
        } finally {
            IoUtils.safeClose(connection);
        }
        String g = "t";
        //System.out.println("testHttp2Post: statusCode = " + reference.get().getResponseCode() + " body = " + reference.get().getAttachment(Http2Client.RESPONSE_BODY));
    }
}
