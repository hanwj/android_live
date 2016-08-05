package com.zvidia.pomelo.websocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.zvidia.pomelo.exception.PomeloException;
import com.zvidia.pomelo.protocol.PomeloMessage;

/**
 * Created with IntelliJ IDEA.
 * User: jiangzm
 * Date: 13-8-8
 * Time: 下午10:14
 * To change this template use File | Settings | File Templates.
 */
public class PomeloClientTest {
    public static void assertConcurrent(final String message, final List<? extends Runnable> runnables, final int maxTimeoutSeconds) throws InterruptedException {
        final int numThreads = runnables.size();
        final List<Throwable> exceptions = Collections.synchronizedList(new ArrayList<Throwable>());
        final ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        try {
            final CountDownLatch allExecutorThreadsReady = new CountDownLatch(numThreads);
            final CountDownLatch afterInitBlocker = new CountDownLatch(1);
            final CountDownLatch allDone = new CountDownLatch(numThreads);
            for (final Runnable submittedTestRunnable : runnables) {
                threadPool.submit(new Runnable() {
                    public void run() {
                        allExecutorThreadsReady.countDown();
                        try {
                            afterInitBlocker.await();
                            submittedTestRunnable.run();
                        } catch (final Throwable e) {
                            exceptions.add(e);
                        } finally {
                            allDone.countDown();
                        }
                    }
                });
            }
            // wait until all threads are ready
            //assertTrue("Timeout initializing threads! Perform long lasting initializations before passing runnables to assertConcurrent", allExecutorThreadsReady.await(runnables.size() * 10, TimeUnit.MILLISECONDS));
            // start all test runners
            afterInitBlocker.countDown();
            //assertTrue(message + " timeout! More than" + maxTimeoutSeconds + "seconds", allDone.await(maxTimeoutSeconds, TimeUnit.SECONDS));
        } finally {
            threadPool.shutdownNow();
        }
        //assertTrue(message + "failed with exception(s)" + exceptions, exceptions.isEmpty());
    }

    boolean flag = false;

    OnHandshakeSuccessHandler onConnectorHandshakeSuccessHandler = new OnHandshakeSuccessHandler() {
        @Override
        public void onSuccess(PomeloClient client, JSONObject jsonObject) {
            try {
                JSONObject connectorJson = new JSONObject();
                String username = "222";
                String rid = "a";
                connectorJson.put("username", username);
                connectorJson.put("rid", rid);
                client.request("connector.entryHandler.enter", connectorJson.toString(), new OnDataHandler() {

                    @Override
                    public void onData(PomeloMessage.Message message) {
                        System.out.println(message.toString());
                        flag = true;
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
                flag = true;
            } catch (PomeloException e) {
                e.printStackTrace();
                flag = true;
            }
        }
    };
    OnErrorHandler onErrorHandler = new OnErrorHandler() {
        @Override
        public void onError(Exception e) {
            //To change body of implemented methods use File | Settings | File Templates.
            e.printStackTrace();
            flag = true;
        }
    };

    public void testConnect() throws InterruptedException {
        try {
            final PomeloClient client = new PomeloClient(new URI("ws://localhost:3014"));
//            List<Runnable> runs = new ArrayList<Runnable>();
//            runs.add(client);
//            PomeloClientTest.assertConcurrent("test websocket client", runs, 200);
            OnHandshakeSuccessHandler onHandshakeSuccessHandler = new OnHandshakeSuccessHandler() {
                @Override
                public void onSuccess(PomeloClient _client, JSONObject resp) {
                    try {
                        JSONObject json = new JSONObject();
                        json.put("uid", 1);
                        client.request("gate.gateHandler.queryEntry", json.toString(), new OnDataHandler() {
                            @Override
                            public void onData(PomeloMessage.Message message) {
                                try {
                                    JSONObject bodyJson = message.getBodyJson();
                                    String host = bodyJson.getString(PomeloClient.HANDSHAKE_RES_HOST_KEY);
                                    String port = bodyJson.getString(PomeloClient.HANDSHAKE_RES_PORT_KEY);
                                    client.close();
                                    PomeloClient connector = new PomeloClient(new URI("ws://" + host + ":" + port));
                                    connector.setOnHandshakeSuccessHandler(onConnectorHandshakeSuccessHandler);
                                    connector.setOnErrorHandler(onErrorHandler);
                                    connector.connect();
                                } catch (JSONException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                    flag = true;
                                } catch (URISyntaxException e) {
                                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                    flag = true;
                                }
                            }
                        });
                    } catch (PomeloException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        flag = true;
                    } catch (JSONException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        flag = true;
                    }
                }
            };
            client.setOnHandshakeSuccessHandler(onHandshakeSuccessHandler);
            client.setOnErrorHandler(onErrorHandler);
            client.connect();
            while (!flag) {
                Thread.sleep(100);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

}
