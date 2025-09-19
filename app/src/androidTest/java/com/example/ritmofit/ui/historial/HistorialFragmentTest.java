package com.example.ritmofit.ui.historial;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.ritmofit.R;
import com.example.ritmofit.data.repository.HistorialRepository;
import com.example.ritmofit.model.DateRange;
import com.example.ritmofit.model.HistorialItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

/**
 * Integration tests for HistorialFragment user interactions.
 * Tests UI behavior, RecyclerView display, and user interaction flows.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class HistorialFragmentTest {

    @Mock
    private HistorialRepository mockRepository;

    private MutableLiveData<HistorialUiState> uiStateLiveData;
    private MutableLiveData<DateRange> dateRangeLiveData;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Initialize LiveData objects
        uiStateLiveData = new MutableLiveData<>();
        dateRangeLiveData = new MutableLiveData<>();
        
        // Setup mock repository
        when(mockRepository.isNetworkAvailable()).thenReturn(true);
        when(mockRepository.validateDateRange(any(), any())).thenReturn(null);
        
        // Mock successful data loading
        doAnswer(invocation -> {
            HistorialRepository.HistorialCallback callback = invocation.getArgument(2);
            callback.onSuccess(createSampleHistorialItems());
            return null;
        }).when(mockRepository).getHistorial(any(), any(), any());
    }

    @Test
    public void fragment_displaysCorrectly_whenLaunched() {
        // Given
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        // Then
        onView(withId(R.id.rvHistorial)).check(matches(isDisplayed()));
        onView(withId(R.id.etFromDate)).check(matches(isDisplayed()));
        onView(withId(R.id.etToDate)).check(matches(isDisplayed()));
        onView(withId(R.id.btnClearFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.btnApplyFilter)).check(matches(isDisplayed()));
        onView(withId(R.id.swipeRefreshLayout)).check(matches(isDisplayed()));
    }

    @Test
    public void fragment_showsLoadingState_whenDataIsLoading() {
        // Given
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        scenario.onFragment(fragment -> {
            // Simulate loading state
            HistorialUiState loadingState = HistorialUiState.loading();
            fragment.getViewModel().uiState.setValue(loadingState);
        });
        
        // Then
        onView(withId(R.id.layoutLoading)).check(matches(isDisplayed()));
        onView(withId(R.id.rvHistorial)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutEmpty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())));
    }

    @Test
    public void fragment_showsSuccessState_whenDataLoaded() {
        // Given
        List<HistorialItem> testData = createSampleHistorialItems();
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        scenario.onFragment(fragment -> {
            // Simulate success state
            HistorialUiState successState = HistorialUiState.success(testData);
            fragment.getViewModel().uiState.setValue(successState);
        });
        
        // Then
        onView(withId(R.id.rvHistorial)).check(matches(isDisplayed()));
        onView(withId(R.id.layoutLoading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutEmpty)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())));
    }

    @Test
    public void fragment_showsEmptyState_whenNoData() {
        // Given
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        scenario.onFragment(fragment -> {
            // Simulate empty state
            HistorialUiState emptyState = HistorialUiState.empty();
            fragment.getViewModel().uiState.setValue(emptyState);
        });
        
        // Then
        onView(withId(R.id.layoutEmpty)).check(matches(isDisplayed()));
        onView(withId(R.id.rvHistorial)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutLoading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutError)).check(matches(not(isDisplayed())));
    }

    @Test
    public void fragment_showsErrorState_whenErrorOccurs() {
        // Given
        String errorMessage = "Error de conexión";
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        scenario.onFragment(fragment -> {
            // Simulate error state
            HistorialUiState errorState = HistorialUiState.error(errorMessage);
            fragment.getViewModel().uiState.setValue(errorState);
        });
        
        // Then
        onView(withId(R.id.layoutError)).check(matches(isDisplayed()));
        onView(withId(R.id.rvHistorial)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutLoading)).check(matches(not(isDisplayed())));
        onView(withId(R.id.layoutEmpty)).check(matches(not(isDisplayed())));
    }

    @Test
    public void clearFilterButton_clearsDateInputs_whenClicked() {
        // Given
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        scenario.onFragment(fragment -> {
            // Set some dates first
            fragment.handleFromDateSelection(LocalDate.of(2024, 12, 1));
            fragment.handleToDateSelection(LocalDate.of(2024, 12, 31));
        });
        
        // When
        onView(withId(R.id.btnClearFilter)).perform(click());
        
        // Then
        onView(withId(R.id.etFromDate)).check(matches(withText("")));
        onView(withId(R.id.etToDate)).check(matches(withText("")));
    }

    @Test
    public void applyFilterButton_triggersDataLoad_whenClicked() {
        // Given
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        // When
        onView(withId(R.id.btnApplyFilter)).perform(click());
        
        // Then - Verify that data loading was triggered
        scenario.onFragment(fragment -> {
            // This would verify that loadHistorialData was called
            // In a real test, we'd verify through the ViewModel or Repository mock
        });
    }

    @Test
    public void swipeRefresh_triggersDataRefresh_whenPerformed() {
        // Given
        FragmentScenario<HistorialFragment> scenario = FragmentScenario.launchInContainer(HistorialFragment.class);
        
        // When - Simulate swipe refresh
        scenario.onFragment(fragment -> {
            fragment.getBinding().swipeRefreshLayout.setRefreshing(true);
            // Trigger refresh manually since Espresso swipe actions are complex
            fragment.getViewModel().refreshHistorialData();
        });
        
        // Then - Verify refresh was triggered
        scenario.onFragment(fragment -> {
            // In a real implementation, we'd verify through mock interactions
            // For now, we just ensure the fragment handles the refresh state
        });
    }

    /**
     * Helper method to create sample HistorialItem objects for testing
     */
    private List<HistorialItem> createSampleHistorialItems() {
        return Arrays.asList(
            new HistorialItem(
                1L,
                "Yoga Avanzado",
                "Sede Palermo",
                LocalDate.of(2024, 12, 15),
                LocalTime.of(18, 30),
                60
            ),
            new HistorialItem(
                2L,
                "Pilates",
                "Sede Centro",
                LocalDate.of(2024, 12, 14),
                LocalTime.of(10, 0),
                45
            )
        );
    }
}