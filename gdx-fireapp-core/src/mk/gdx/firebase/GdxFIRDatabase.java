/*
 * Copyright 2017 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mk.gdx.firebase;

import java.util.Map;

import mk.gdx.firebase.callbacks.CompleteCallback;
import mk.gdx.firebase.callbacks.DataCallback;
import mk.gdx.firebase.callbacks.TransactionCallback;
import mk.gdx.firebase.database.FilterType;
import mk.gdx.firebase.database.OrderByMode;
import mk.gdx.firebase.deserialization.DataCallbackMitmConverter;
import mk.gdx.firebase.deserialization.DataChangeListenerMitmConverter;
import mk.gdx.firebase.deserialization.FirebaseMapConverter;
import mk.gdx.firebase.deserialization.MapConverter;
import mk.gdx.firebase.deserialization.TransactionMitmConverter;
import mk.gdx.firebase.distributions.DatabaseDistribution;
import mk.gdx.firebase.listeners.ConnectedListener;
import mk.gdx.firebase.listeners.DataChangeListener;

/**
 * Gets access to Firebase Database API in multi-modules.
 *
 * @see DatabaseDistribution
 * @see PlatformDistributor
 */
public class GdxFIRDatabase extends PlatformDistributor<DatabaseDistribution> implements DatabaseDistribution {

    private static volatile GdxFIRDatabase instance;
    private FirebaseMapConverter mapConverter;

    /**
     * GdxFIRDatabase protected constructor.
     * <p>
     * Instance of this class should be getting by {@link #instance()}
     * <p>
     * {@link PlatformDistributor#PlatformDistributor()}
     */
    private GdxFIRDatabase() {
        mapConverter = new MapConverter();
    }

    /**
     * @return Thread-safe singleton instance of this class.
     */
    public static GdxFIRDatabase instance() {
        GdxFIRDatabase result = instance;
        if (result == null) {
            synchronized (GdxFIRDatabase.class) {
                result = instance;
                if (result == null) {
                    instance = result = new GdxFIRDatabase();
                }
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConnect(ConnectedListener connectedListener) {
        platformObject.onConnect(connectedListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseDistribution inReference(String databasePath) {
        platformObject.inReference(databasePath);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Object value) {
        platformObject.setValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Object value, CompleteCallback completeCallback) {
        platformObject.setValue(value, completeCallback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, E extends T> void readValue(Class<T> dataType, DataCallback<E> callback) {
        DataCallbackMitmConverter<T, E> mitmConverter = new DataCallbackMitmConverter<T, E>(dataType, callback, mapConverter);
        if (mitmConverter.isPojo(dataType)) {
            platformObject.readValue(Map.class, mitmConverter.getPojoDataCallback());
        } else {
            platformObject.readValue(dataType, mitmConverter.getGenericDataCallback());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, E extends T> void onDataChange(Class<T> dataType, DataChangeListener<E> listener) {
        DataChangeListenerMitmConverter<T, E> mitmConverter = new DataChangeListenerMitmConverter<T, E>(dataType, listener, mapConverter);
        if (mitmConverter.isPojo(dataType)) {
            platformObject.onDataChange(Map.class, mitmConverter.getPojoListener());
        } else {
            platformObject.onDataChange(dataType, mitmConverter.getGenericListener());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <V> DatabaseDistribution filter(FilterType filterType, V... filterArguments) {
        platformObject.filter(filterType, filterArguments);
        return this;
    }

    @Override
    public DatabaseDistribution orderBy(OrderByMode orderByMode, String argument) {
        platformObject.orderBy(orderByMode, argument);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatabaseDistribution push() {
        return platformObject.push();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeValue() {
        platformObject.removeValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeValue(CompleteCallback completeCallback) {
        platformObject.removeValue(completeCallback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChildren(Map<String, Object> data) {
        platformObject.updateChildren(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateChildren(Map<String, Object> data, CompleteCallback completeCallback) {
        platformObject.updateChildren(data, completeCallback);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, R extends T> void transaction(Class<T> dataType, TransactionCallback<R> transactionCallback, CompleteCallback completeCallback) {
        TransactionMitmConverter<T, R> mitmConverter = new TransactionMitmConverter<T, R>(dataType, transactionCallback, mapConverter);
        if (mitmConverter.isPojo(dataType)) {
            platformObject.transaction(Map.class, mitmConverter.getPojoCallback(), completeCallback);
        } else {
            platformObject.transaction(dataType, mitmConverter.getGenericCallback(), completeCallback);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPersistenceEnabled(boolean enabled) {
        platformObject.setPersistenceEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void keepSynced(boolean synced) {
        platformObject.keepSynced(synced);
    }

    /**
     * Sets map converter which is use to do conversion between database maps into instance objects.
     *
     * @param mapConverter Map convert instance, not null
     */
    public void setMapConverter(FirebaseMapConverter mapConverter) {
        this.mapConverter = mapConverter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getIOSClassName() {
        return "mk.gdx.firebase.ios.database.Database";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getAndroidClassName() {
        return "mk.gdx.firebase.android.database.Database";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWebGLClassName() {
        return "mk.gdx.firebase.html.database.Database";
    }
}
