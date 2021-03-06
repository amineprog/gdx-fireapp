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

package mk.gdx.firebase.html.database.queries;

import java.util.Map;

import mk.gdx.firebase.callbacks.CompleteCallback;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.UpdateChildrenValidator;
import mk.gdx.firebase.html.database.Database;
import mk.gdx.firebase.html.database.GwtDatabaseQuery;
import mk.gdx.firebase.html.database.MapTransformer;

/**
 * Provides update javascript execution.
 */
public class UpdateChildrenQuery extends GwtDatabaseQuery {
    public UpdateChildrenQuery(Database databaseDistribution) {
        super(databaseDistribution);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void runJS() {
        if (arguments.size == 1) {
            update(databaseReferencePath, MapTransformer.mapToJSON((Map<String, Object>) arguments.get(0)));
        } else if (arguments.size == 2) {
            updateWithCallback(databaseReferencePath, MapTransformer.mapToJSON((Map<String, Object>) arguments.get(0)), (CompleteCallback) arguments.get(1));
        } else {
            throw new IllegalStateException();
        }
    }

    @Override
    protected ArgumentsValidator createArgumentsValidator() {
        return new UpdateChildrenValidator();
    }

    /**
     * Writes multiple values to database at once.
     * <p>
     * JSON.parse is using here. It get object or primitive (for primitives types it is primitive wrapped by string ex. "1", "true") and returns object representation.
     * Is one exception here - when stringValue is actually a string like "abc", JSON.parse will be throw error.
     * <p>
     * You can read more here: <a href="https://firebase.google.com/docs/reference/js/firebase.database.Reference#update">https://firebase.google.com/docs/reference/js/firebase.database.Reference#update</a>
     *
     * @param reference   Reference path, not null
     * @param stringValue String value representation
     */
    public static native void update(String reference, String stringValue) /*-{
        var val;
        try{
            val = JSON.parse(stringValue);
        }catch(err){
            val = stringValue;
        }
        $wnd.firebase.database().ref(reference).update(val);
    }-*/;

    /**
     * Update multiple values in database with callback.
     * <p>
     * For more explanation look at {@link #update(String, String)}
     *
     * @param reference   Reference path, not null
     * @param stringValue String value representation
     * @param callback    Callback
     */
    public static native void updateWithCallback(String reference, String stringValue, CompleteCallback callback) /*-{
        var val;
        try{
            val = JSON.parse(stringValue);
        }catch(err){
            val = stringValue;
        }
        $wnd.firebase.database().ref(reference).update(val).then(function(){
            callback.@mk.gdx.firebase.callbacks.CompleteCallback::onSuccess()();
        })['catch'](function(error){
            callback.@mk.gdx.firebase.callbacks.CompleteCallback::onError(Ljava/lang/Exception;)(@java.lang.Exception::new(Ljava/lang/String;)(error.message));
        });
    }-*/;
}
