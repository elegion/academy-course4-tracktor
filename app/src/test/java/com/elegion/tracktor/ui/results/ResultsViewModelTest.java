package com.elegion.tracktor.ui.results;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.Observer;

import com.elegion.tracktor.data.RealmRepository;
import com.elegion.tracktor.data.model.Track;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Azret Magometov
 */
@RunWith(MockitoJUnitRunner.class)
public class ResultsViewModelTest {

    private ResultsViewModel mResultsViewModel;

    @Mock
    private RealmRepository mRealmRepository;

    @Mock
    private Observer<List<Track>> mObserver;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    private List<Track> mTracks;

    @Before
    public void setUp() throws Exception {

        mTracks = new ArrayList<>();
        mTracks.add(new Track());
        when(mRealmRepository.getAll()).thenReturn(mTracks);

        mResultsViewModel = new ResultsViewModel(mRealmRepository);

        mResultsViewModel.getTracks().observeForever(mObserver);
    }

    @Test
    public void checkObserverTriggeredOnChengedTest() {
        mResultsViewModel.loadTracks();

        verify(mObserver, times(1)).onChanged(mTracks);

    }


}