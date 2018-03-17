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

import com.google.firebasedatabase.FIRDataSnapshot;
import com.google.firebasedatabase.FIRDatabaseQuery;
import com.google.firebasedatabase.enums.FIRDataEventType;

import java.io.FileNotFoundException;

import apple.foundation.NSError;
import mk.gdx.firebase.GdxFIRLogger;
import mk.gdx.firebase.callbacks.DataCallback;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.ReadValueValidator;
import mk.gdx.firebase.ios.database.DataProcessor;
import mk.gdx.firebase.ios.database.Database;
import mk.gdx.firebase.ios.database.IosDatabaseQuery;

/**
 * Provides call to {@link FIRDatabaseQuery#observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock(long, FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_1, FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_2)}.
 */
public class ReadValueQuery extends IosDatabaseQuery<Void>
{
    public ReadValueQuery(Database databaseDistribution)
    {
        super(databaseDistribution);
    }

    @Override
    protected void prepare()
    {
        super.prepare();
    }

    @Override
    protected ArgumentsValidator createArgumentsValidator()
    {
        return new ReadValueValidator();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Void run()
    {
        filtersProvider.applyFiltering().observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock(FIRDataEventType.Value, new FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_1()
        {
            @Override
            public void call_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_1(FIRDataSnapshot arg0, String arg1)
            {
                if (arg0.value() == null) {
                    if (arguments.get(1) != null)
                        ((DataCallback) arguments.get(1)).onError(new FileNotFoundException());
                } else {
                    Object data = null;
                    try {
                        data = DataProcessor.iosDataToJava(arg0.value(), (Class) arguments.get(0));
                    } catch (Exception e) {
                        if (arguments.get(1) != null) {
                            ((DataCallback) arguments.get(1)).onError(e);
                        } else {
                            GdxFIRLogger.error(e.getLocalizedMessage(), e);
                        }
                        return;
                    }
                    ((DataCallback) arguments.get(1)).onData(data);
                }
            }
        }, new FIRDatabaseQuery.Block_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_2()
        {
            @Override
            public void call_observeSingleEventOfTypeAndPreviousSiblingKeyWithBlockWithCancelBlock_2(NSError arg0)
            {
                ((DataCallback) arguments.get(1)).onError(new Exception(arg0.localizedDescription()));
            }
        });
        return null;
    }
}
