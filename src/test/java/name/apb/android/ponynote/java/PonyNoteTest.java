package name.apb.android.ponynote.java;

/*
 * Pony Note
 * Copyright (C) 2013  Andre-Patrick Bubel <code@andre-bubel.de>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import name.apb.android.ponynote.scala.AboutDialog;
import name.apb.android.ponynote.scala.PonyNote;
import name.apb.android.ponynote.scala.R;
import org.junit.*;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PonyNoteTest {
    private PonyNote activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(PonyNote.class).create().get();
    }


    @Test
    public void checkAppName() throws Exception {
        String appName = activity.getResources().getString(R.string.app_name);

        assertThat(appName, equalTo("Pony Note"));
    }

    @Test
    public void checkListEmptyAtStart() throws Exception {
        ListView list = activity.noteListView();

        assertThat(list.getChildCount(), equalTo(0));
    }

    @Test
    public void clickAboutButton() throws Exception {
        Button aboutButton = (Button) activity.findViewById(R.id.about_button);

        Robolectric.clickOn(aboutButton);

        ShadowActivity shadowActivity = Robolectric.shadowOf(activity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();
        ShadowIntent shadowIntent = Robolectric.shadowOf(startedIntent);
        assertThat(shadowIntent.getComponent().getClassName(), equalTo(AboutDialog.class.getName()));
    }
}
