/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */
package org.apache.plc4x.java.ads.api.commands.types;

import io.netty.buffer.ByteBuf;
import org.apache.plc4x.java.ads.api.util.ByteReadable;
import org.apache.plc4x.java.ads.api.util.LengthSupplier;

import java.util.Objects;

import static org.apache.plc4x.java.ads.api.util.ByteReadableUtils.buildByteBuff;

public class AdsNotificationSample implements ByteReadable {

    /**
     * 4 Bytes	Handle of notification.
     */
    private final NotificationHandle notificationHandle;
    /**
     * 4 Bytes	Size of data range in bytes.
     */
    private final SampleSize sampleSize;
    /**
     * n Bytes	Data
     */
    private final Data data;

    ////
    // Used when fields should be calculated. TODO: check if we better work with a subclass.
    private final LengthSupplier lengthSupplier;
    private final boolean calculated;
    //
    ///

    protected AdsNotificationSample(NotificationHandle notificationHandle, Data data) {
        this.notificationHandle = Objects.requireNonNull(notificationHandle);
        this.sampleSize = null;
        this.data = Objects.requireNonNull(data);
        calculated = true;
        lengthSupplier = () -> data.getCalculatedLength();
    }

    protected AdsNotificationSample(NotificationHandle notificationHandle, SampleSize sampleSize, Data data) {
        this.notificationHandle = Objects.requireNonNull(notificationHandle);
        this.sampleSize = Objects.requireNonNull(sampleSize);
        this.data = Objects.requireNonNull(data);
        calculated = true;
        lengthSupplier = null;
    }

    public static AdsNotificationSample of(NotificationHandle notificationHandle, Data data) {
        return new AdsNotificationSample(notificationHandle, data);
    }

    public static AdsNotificationSample of(NotificationHandle notificationHandle, SampleSize sampleSize, Data data) {
        return new AdsNotificationSample(notificationHandle, sampleSize, data);
    }

    @Override
    public ByteBuf getByteBuf() {
        return buildByteBuff(notificationHandle, (calculated ? SampleSize.of(lengthSupplier.getCalculatedLength()) : sampleSize), data);
    }

    @Override
    public String toString() {
        return "AdsNotificationSample{" +
            "notificationHandle=" + notificationHandle +
            ", sampleSize=" + (calculated ? SampleSize.of(lengthSupplier.getCalculatedLength()) : sampleSize) +
            ", data=" + data +
            '}';
    }
}