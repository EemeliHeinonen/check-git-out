package com.eemeliheinonen.gitcheck;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by eemeliheinonen on 10/04/2017.
 */


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class RepositoriesFragmentTest {


    @Mock
    final GitHubApiInterface apiService = RestClient.getClient();

    private RepositoriesFragment mRepositoriesFragment;

    @Before
    public void setupRepositoriesFragment() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        //Get a reference to the class under test
        mRepositoriesFragment = RepositoriesFragment.newInstance();
    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        startFragment( mRepositoriesFragment );
        assertNotNull( mRepositoriesFragment );
    }

    /*

    @Test
    public void noSearchWithoutUsername() throws Exception {
        when(mRepositoriesFragment.etSearch.getText().toString()).thenReturn("");

        mRepositoriesFragment.findViewById(R.id.login).performClick();

        try{
            mRepositoriesFragment.getList(apiService);
            fail("Exception not thrown");
        }catch(Exception e){
            System.out.println("catch print");
        }

    }
    */

}