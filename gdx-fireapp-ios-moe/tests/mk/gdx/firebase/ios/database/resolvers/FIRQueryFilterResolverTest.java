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

package mk.gdx.firebase.ios.database.resolvers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.moe.natj.general.NatJ;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import bindings.google.firebasedatabase.FIRDatabaseQuery;
import mk.gdx.firebase.database.FilterType;
import mk.gdx.firebase.ios.GdxIOSAppTest;

@PrepareForTest({NatJ.class, FIRDatabaseQuery.class})
public class FIRQueryFilterResolverTest extends GdxIOSAppTest {

    private FIRDatabaseQuery firDatabaseQuery;

    @Override
    public void setup() {
        super.setup();
        PowerMockito.mockStatic(FIRDatabaseQuery.class);
        firDatabaseQuery = PowerMockito.mock(FIRDatabaseQuery.class);
    }

    @Test
    public void resolve_limitFirst() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.LIMIT_FIRST, firDatabaseQuery, 2);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryLimitedToFirst(Mockito.anyLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolve_limitFirstWrongArgument() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.LIMIT_FIRST, firDatabaseQuery, "test");

        // Then
        Assert.fail();
    }

    @Test
    public void resolve_endAt() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.END_AT, firDatabaseQuery, "test");

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryEndingAtValue(Mockito.eq("test"));
    }

    @Test
    public void resolve_endAt2() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.END_AT, firDatabaseQuery, 2.0);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryEndingAtValue(Mockito.any());
    }

    @Test
    public void resolve_endAt3() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.END_AT, firDatabaseQuery, true);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryEndingAtValue(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolve_endAtWrongArgument() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.END_AT, firDatabaseQuery, 2);

        // Then
        Assert.fail();
    }

    @Test
    public void resolve_equalTo() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.EQUAL_TO, firDatabaseQuery, "test");

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryEqualToValue(Mockito.eq("test"));
    }

    @Test
    public void resolve_equalTo2() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.EQUAL_TO, firDatabaseQuery, 10.0);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryEqualToValue(Mockito.any());
    }

    @Test
    public void resolve_equalTo3() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.EQUAL_TO, firDatabaseQuery, true);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryEqualToValue(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolve_equalToWrongArgument() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.EQUAL_TO, firDatabaseQuery, 10f);

        // Then
        Assert.fail();
    }

    @Test
    public void resolve_limitLast() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.LIMIT_LAST, firDatabaseQuery, 2);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryLimitedToLast(Mockito.anyLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolve_limitLastWrongArgument() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.LIMIT_LAST, firDatabaseQuery, "test");

        // Then
        Assert.fail();
    }

    @Test
    public void resolve_startAt() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.START_AT, firDatabaseQuery, "test");

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryStartingAtValue(Mockito.eq("test"));
    }

    @Test
    public void resolve_startAt2() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.START_AT, firDatabaseQuery, 10.0);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryStartingAtValue(Mockito.any());
    }

    @Test
    public void resolve_startAt3() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.START_AT, firDatabaseQuery, true);

        // Then
        Mockito.verify(firDatabaseQuery, VerificationModeFactory.times(1)).queryStartingAtValue(Mockito.any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void resolve_startAtWrongArgument() {
        // Given
        FIRQueryFilterResolver firQueryFilterResolver = new FIRQueryFilterResolver();

        // When
        firQueryFilterResolver.resolve(FilterType.START_AT, firDatabaseQuery, 10f);

        // Then
        Assert.fail();
    }

}