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

package mk.gdx.firebase.ios.database.queries;

import com.badlogic.gdx.utils.Array;
import com.google.firebasedatabase.FIRDataSnapshot;
import com.google.firebasedatabase.FIRDatabaseQuery;
import com.google.firebasedatabase.enums.FIRDataEventType;

import java.io.FileNotFoundException;

import apple.foundation.NSError;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.OnDataValidator;
import mk.gdx.firebase.ios.database.DataProcessor;
import mk.gdx.firebase.ios.database.Database;
import mk.gdx.firebase.ios.database.IosDatabaseQuery;
import mk.gdx.firebase.ios.database.observers.DataObserversManager;
import mk.gdx.firebase.listeners.DataChangeListener;

/**
 * Provides call to {@link FIRDatabaseQuery#observeEventTypeWithBlockWithCancelBlock(long, FIRDatabaseQuery.Block_observeEventTypeWithBlockWithCancelBlock_1, FIRDatabaseQuery.Block_observeEventTypeWithBlockWithCancelBlock_2)}.
 */
public class OnDataChangeQuery extends IosDatabaseQuery<Void>
{
    private static final DataObserversManager observersManager = new DataObserversManager();

    public OnDataChangeQuery(Database databaseDistribution)
    {
        super(databaseDistribution);
    }

    @Override
    protected ArgumentsValidator createArgumentsValidator()
    {
        return new OnDataValidator();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Void run()
    {
        // TODO - ordering FIRDataSnapsho
        if (arguments.get(1) != null) {
            long handle = filtersProvider.applyFiltering().observeEventTypeWithBlockWithCancelBlock(FIRDataEventType.Value,
                    new DataChangeBlock((Class) arguments.get(0), (DataChangeListener) arguments.get(1)),
                    new DataChangeCancelBlock((DataChangeListener) arguments.get(1)));
            observersManager.addNewListener(databasePath, handle);
        } else {
            if (observersManager.hasListeners(databasePath)) {
                Array<Long> handles = observersManager.getListeners(databasePath);
                for (Long handle : handles)
                    query.removeObserverWithHandle(handle);
                observersManager.removeListenersForPath(databasePath);
            }
        }
        return null;
    }

    /**
     * Observer for data change. Wraps {@code DataChangeListener}
     */
    private class DataChangeBlock implements FIRDatabaseQuery.Block_observeEventTypeWithBlockWithCancelBlock_1
    {

        private Class type;
        private DataChangeListener dataChangeListener;

        private DataChangeBlock(Class type, DataChangeListener dataChangeListener)
        {
            this.type = type;
            this.dataChangeListener = dataChangeListener;
        }


        @Override
        public void call_observeEventTypeWithBlockWithCancelBlock_1(FIRDataSnapshot arg0)
        {
            if (arg0.value() == null) {
                dataChangeListener.onCanceled(new FileNotFoundException());
            } else {
                Object data = null;
                try {
                    data = DataProcessor.iosDataToJava(arg0.value(), type);
                } catch (Exception e) {
                    dataChangeListener.onCanceled(e);
                    return;
                }
                dataChangeListener.onChange(data);
            }
        }
    }

    /**
     * Observer for data change cancel. Wraps {@code DataChangeListener}
     */
    private class DataChangeCancelBlock implements FIRDatabaseQuery.Block_observeEventTypeWithBlockWithCancelBlock_2
    {

        private DataChangeListener dataChangeListener;

        private DataChangeCancelBlock(DataChangeListener dataChangeListener)
        {
            this.dataChangeListener = dataChangeListener;
        }

        @Override
        public void call_observeEventTypeWithBlockWithCancelBlock_2(NSError arg0)
        {
            dataChangeListener.onCanceled(new Exception(arg0.localizedDescription()));
        }
    }
}
