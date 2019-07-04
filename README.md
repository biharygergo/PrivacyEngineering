## Purpose-Aware MQTT Broker

### Motivation

Article 5 of the General Data Protection Regulation (GDPR) states that personal data should be “...collected for specified, explicit and legitimate purposes and not further processed in a manner that is incompatible with those purposes;...”. 
### Overview
 
Extended from the open-source broker [Moquette](https:github.com/moquette-io/moquette) and with the use of [YaPPL](https://www.ise.tu-berlin.de/fileadmin/fg308/publications/2018/Ulbricht_Pallas-2018-YaPPL.pdf) which is a privacy-preference language that can be used to codify purposes.  The message broker will process messages published under a certain topic, match them with the specified consents and then re-publish the messages in a purpose topology, ensuring that only subscribers with purposes included in the consent can receive the messages.  

### Documentation
#### Javadocs
 https://biharygergo.github.io/PrivacyEngineering/


#### Broker
The broker package contains an extension of the Moquette broker. The broker is responsible for transmitting messages and includes the extension of the purpose-aware republishing functionality. In the following, we will introduce the basic structure of the code, as well as its main classes and methods. 

#### YaPPL
As we have discussed before, YaPPL is a privacy preference language which can be used to store and access user consents programmatically. The language has been developed in Python and since our project was created in a different language, we needed to port the implementation to Java.

##### Functionality
An example implementation and usage of YaPPL can be found in the PolicyHandler class, which acts as the policy repository for the purpose aware broker. 

##### Clients
This package contains classes introduced for the sake of testing. SubscriberSimulator subscribes to the purpose-topology with an id of one of the available Utilizers. On the other hand, ClientSimulator floods the topic-topology with a specified number of messages to available topics.

### Payload Format
Please follow this JSON format format under `./src/main/resources/example_message.json`

```json
{
  "measurement": 8.46347296143437,
  "timestamp": 1561473679,
  "subscriber_id": "HhfRDKWjIklhjpTMaKLqDWs",
  "measurement_type": "temperature"
}
```

### Purpose Configuration 

Purpose configuration under `./src/main/resources/purpose_config.json`

```json
[
  {
    "id": "admin/analysis",
    "removed_properties": [],
    "added_properties": [
      "address"
    ],
    "aggregate_frequency": "0"
  },
  ...
]
```

### Utilizer Configuration 

Utilizer configuration under `./src/main/resources/utilizer_config.json`

example:
```json
[
  {
    "id": "Uwr1j3ifoe2onmwofi23nor",
    "name": "Vatenfall",
    "address": "Some street 1, 11111 Berlin",
    "phone_number": "123123123"
  },
  ...
 ]
```
``