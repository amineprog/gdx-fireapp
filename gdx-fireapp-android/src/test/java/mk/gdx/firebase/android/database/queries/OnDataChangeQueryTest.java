/*
 * Copyright 2018 mk
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

import com.badlogic.gdx.utils.GdxNativesLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.reflect.Whitebox;

import java.util.Map;

import mk.gdx.firebase.android.AndroidContextTest;
import mk.gdx.firebase.android.database.AndroidDatabaseQuery;
import mk.gdx.firebase.android.database.Database;
import mk.gdx.firebase.android.database.providers.QueryFilteringProvider;
import mk.gdx.firebase.database.DataListenersManager;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.database.validators.OnDataValidator;
import mk.gdx.firebase.listeners.DataChangeListener;

@PrepareForTest({
        GdxNativesLoader.class, FirebaseDatabase.class,
        QueryFilteringProvider.class
//        OnDataChangeQuery.class,
})
public class OnDataChangeQueryTest extends AndroidContextTest {

    private FirebaseDatabase firebaseDatabase;

    @Override
    public void setup() throws Exception {
        super.setup();
        PowerMockito.mockStatic(FirebaseDatabase.class);
        firebaseDatabase = PowerMockito.mock(FirebaseDatabase.class);
        Mockito.when(FirebaseDatabase.getInstance()).thenReturn(firebaseDatabase);
    }

    @Test
    public void createArgumentsValidator() {
        // Given
        Database databaseDistribution = Mockito.mock(Database.class);
        OnDataChangeQuery onDataChangeQuery = new OnDataChangeQuery(databaseDistribution);

        // When
        ArgumentsValidator argumentsValidator = onDataChangeQuery.createArgumentsValidator();

        // Then
        Assert.assertNotNull(argumentsValidator);
        Assert.assertTrue(argumentsValidator instanceof OnDataValidator);
    }

    @Test
    public void run() throws Exception {
        // Given
        Database databaseDistribution = Mockito.spy(Database.class);
        DatabaseReference databaseReference = Mockito.mock(DatabaseReference.class);
        DataChangeListener dataChangeListener = Mockito.mock(DataChangeListener.class);
        Mockito.when(firebaseDatabase.getReference(Mockito.anyString())).thenReturn(databaseReference);
        Mockito.when(databaseDistribution.inReference(Mockito.anyString())).thenCallRealMethod();
        OnDataChangeQuery onDataChangeQuery = new OnDataChangeQuery(databaseDistribution);

        // When
        databaseDistribution.inReference("/test");
        onDataChangeQuery.withArgs(Map.class, dataChangeListener).execute();

        // Then
        Mockito.verify(databaseReference, VerificationModeFactory.times(1)).addValueEventListener(Mockito.any(ValueEventListener.class));
    }

    @Test
    public void run_detach() throws Exception {
        // Given
        Database databaseDistribution = Mockito.spy(Database.class);
        DatabaseReference databaseReference = Mockito.mock(DatabaseReference.class);
        Mockito.when(firebaseDatabase.getReference(Mockito.anyString())).thenReturn(databaseReference);
        Mockito.when(databaseDistribution.inReference(Mockito.anyString())).thenCallRealMethod();
        OnDataChangeQuery onDataChangeQuery = new OnDataChangeQuery(databaseDistribution);

        // When
        databaseDistribution.inReference("/test");
        onDataChangeQuery.withArgs(Map.class, null).execute();

        // Then
        Mockito.verify(databaseReference, VerificationModeFactory.times(0)).addValueEventListener(Mockito.any(ValueEventListener.class));
    }
}