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
import clients.SubscriberSimulator;

import java.io.IOException;

/**
 * Simple example of how to embed the broker in another project
 */
public final class BrokerLauncher {


    public static void main(String[] args) throws InterruptedException, IOException {
        PurposeBroker purposeBroker = new PurposeBroker();
        purposeBroker.startBroker();

        SubscriberSimulator subscriberSimulator = new SubscriberSimulator();
        subscriberSimulator.start();

    }

}
