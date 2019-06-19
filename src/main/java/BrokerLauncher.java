/*
 * Copyright (c) 2012-2018 The original author or authors
 * ------------------------------------------------------
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * The Apache License v2.0 is available at
 * http://www.opensource.org/licenses/apache2.0.php
 *
 * You may elect to redistribute this code under either of these licenses.
 */


import broker.PurposeBroker;
import clients.ClientSimulator;
import clients.PublisherClient;
import clients.PublisherSyncClient;
import io.moquette.interception.InterceptHandler;
import io.moquette.server.Server;
import io.moquette.server.config.ClasspathResourceLoader;
import io.moquette.server.config.IConfig;
import io.moquette.server.config.IResourceLoader;
import io.moquette.server.config.ResourceLoaderConfig;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Simple example of how to embed the broker in another project
 */
public final class BrokerLauncher {


    public static void main(String[] args) throws InterruptedException, IOException {
        IResourceLoader classpathLoader = new ClasspathResourceLoader();
        final IConfig classPathConfig = new ResourceLoaderConfig(classpathLoader);

        final Server mqttBroker = new Server();
        PurposeBroker interceptor = new PurposeBroker();
        List<? extends InterceptHandler> userHandlers = Collections.singletonList(interceptor);

        // Start the broker
        mqttBroker.startServer(classPathConfig, userHandlers);
        System.out.println("Broker started. Press [CTRL+C] to stop.");

        //Bind a shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Stopping broker");
            mqttBroker.stopServer();
            System.out.println("Broker stopped");
        }));

        Thread.sleep(5000);

        interceptor.setClient(new PublisherSyncClient("localhost", 1883));
        ClientSimulator clientSimulator = new ClientSimulator();
        clientSimulator.start();

    }

    private BrokerLauncher() {
    }
}
