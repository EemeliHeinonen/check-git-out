package com.eemeliheinonen.gitcheck;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;
import static org.robolectric.Shadows.shadowOf;


/**
 * Created by Eemeli on 14/04/2017.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class MainActivityTest {

    @Test
    public void activityNotNull() throws Exception {
        assertTrue(Robolectric.setupActivity(MainActivity.class) != null);

    }


}