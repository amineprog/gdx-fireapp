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

package mk.gdx.firebase.android.database.queries;

import com.google.firebase.database.DatabaseReference;

import mk.gdx.firebase.android.database.AndroidDatabaseQuery;
import mk.gdx.firebase.android.database.Database;
import mk.gdx.firebase.android.database.listeners.QueryCompletionListener;
import mk.gdx.firebase.callbacks.CompleteCallback;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.SetValueValidator;

/**
 * Provides setValue execution with firebase database reference.
 */
public class SetValueQuery extends AndroidDatabaseQuery<Void> {
    public SetValueQuery(Database databaseDistribution) {
        super(databaseDistribution);
    }

    @Override
    protected void prepare() {
        super.prepare();
        if (!(query instanceof DatabaseReference))
            throw new IllegalStateException(SHOULD_BE_RUN_WITH_DATABASE_REFERENCE);
    }

    @Override
    protected ArgumentsValidator createArgumentsValidator() {
        return new SetValueValidator();
    }

    @Override
    protected Void run() {
        if (arguments.size == 1) {
            ((DatabaseReference) query).setValue(arguments.get(0));
        } else if (arguments.size == 2) {
            ((DatabaseReference) query).setValue(arguments.get(0), new QueryCompletionListener((CompleteCallback) arguments.get(1)));
        } else {
            throw new IllegalStateException();
        }
        return null;
    }
}
