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

package mk.gdx.firebase.ios.database.providers;

import bindings.google.firebasedatabase.FIRDatabaseQuery;
import mk.gdx.firebase.database.SortingFilteringProvider;
import mk.gdx.firebase.ios.database.resolvers.FIRQueryFilterResolver;
import mk.gdx.firebase.ios.database.resolvers.FIRQueryOrderByResolver;

/**
 * Provides decision between call {@code FIRDatabaseReference} or {@code FIRDatabaseQuery} based at current query context.
 */
public class FIRDatabaseQueryFilteringProvider extends SortingFilteringProvider<FIRDatabaseQuery, FIRQueryFilterResolver, FIRQueryOrderByResolver> {

    @Override
    public FIRQueryFilterResolver createFilterResolver() {
        return new FIRQueryFilterResolver();
    }

    @Override
    public FIRQueryOrderByResolver createOrderByResolver() {
        return new FIRQueryOrderByResolver();
    }
}
