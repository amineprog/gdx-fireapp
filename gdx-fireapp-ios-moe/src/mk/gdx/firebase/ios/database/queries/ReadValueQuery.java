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

import apple.foundation.NSError;
import bindings.google.firebasedatabase.FIRDataSnapshot;
import bindings.google.firebasedatabase.FIRDatabaseQuery;
import bindings.google.firebasedatabase.enums.FIRDataEventType;
import mk.gdx.firebase.callbacks.DataCallback;
import mk.gdx.firebase.database.pojos.OrderByClause;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.ReadValueValidator;
import mk.gdx.firebase.ios.database.DataProcessor;
import mk.gdx.firebase.ios.database.Database;
import mk.gdx.firebase.ios.database.IosDatabaseQuery;
import mk.gdx.firebase.ios.database.resolvers.FIRDataSnapshotOrderByResolver;

/**
 * Provides call to {@link FIRDatabaseQuery#observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock(long, FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_1, FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_2)}.
 */
public class ReadValueQuery extends IosDatabaseQuery<Void> {
    public ReadValueQuery(Database databaseDistribution) {
        super(databaseDistribution);
    }

    @Override
    protected ArgumentsValidator createArgumentsValidator() {
        return new ReadValueValidator();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Void run() {
        filtersProvider.applyFiltering().observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock(FIRDataEventType.Value,
                new ReadValueBlock((Class) arguments.get(0), orderByClause, (DataCallback) arguments.get(1)),
                new ReadValueCancelBlock((DataCallback) arguments.get(1)));
        return null;
    }

    /**
     * Observer read value block. Wraps {@code DataCallback}
     */
    private class ReadValueBlock implements FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_1 {

        private Class type;
        private DataCallback dataCallback;
        private OrderByClause orderByClause;

        private ReadValueBlock(Class type, OrderByClause orderByClause, DataCallback dataCallback) {
            this.type = type;
            this.orderByClause = orderByClause;
            this.dataCallback = dataCallback;
        }

        @Override
        public void call_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_1(FIRDataSnapshot arg0, String arg1) {
            if (arg0.value() == null) {
                dataCallback.onError(new Exception(GIVEN_DATABASE_PATH_RETURNED_NULL_VALUE));
            } else {
                Object data = null;
                try {
                    if (!FIRDataSnapshotOrderByResolver.shouldResolveOrderBy(orderByClause, type, arg0)) {
                        data = DataProcessor.iosDataToJava(arg0.value(), type);
                    } else {
                        data = FIRDataSnapshotOrderByResolver.resolve(arg0);
                    }
                } catch (Exception e) {
                    dataCallback.onError(e);
                    return;
                }
                dataCallback.onData(data);
            }
        }
    }

    private class ReadValueCancelBlock implements FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_2 {

        private DataCallback dataCallback;

        private ReadValueCancelBlock(DataCallback dataCallback) {
            this.dataCallback = dataCallback;
        }

        @Override
        public void call_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_2(NSError arg0) {
            dataCallback.onError(new Exception(arg0.localizedDescription()));
        }
    }
}
