/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 BeiJing Baidu Netcom Science Technology Co., Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.baidubce.kafka.samples;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;

class Consumer {
    private static final int TIME_OUT_MS = 5000;

    static void run(String topic, int numOfRecords) throws IOException {
        Properties properties = new Properties();
        properties.load(Consumer.class.getClassLoader().getResourceAsStream("client.properties"));
        properties.setProperty(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, "kafka.bj.baidubce.com:9092");
        properties.setProperty(CommonClientConfigs.CLIENT_ID_CONFIG, "kafka-samples-java-consumer-1");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "kafka-samples-java-group");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());

        KafkaConsumer<byte[], byte[]> consumer = new KafkaConsumer<byte[], byte[]>(properties);
        consumer.subscribe(Collections.singletonList(topic));

        while (numOfRecords > 0) {
            ConsumerRecords<byte[], byte[]> records = consumer.poll(TIME_OUT_MS);
            for (ConsumerRecord<byte[], byte[]> record : records) {
                String position = record.partition() + "-" + record.offset();
                String key = new String(record.key(), "UTF-8");
                String value = new String(record.value(), "UTF-8");
                System.out.println(position + ": " + key + " " + value);
            }
            numOfRecords -= records.count();
        }

        consumer.close();
    }
}
