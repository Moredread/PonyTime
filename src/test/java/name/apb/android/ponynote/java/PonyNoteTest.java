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

import name.apb.android.ponynote.scala.PonyNote;
import name.apb.android.ponynote.scala.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class PonyNoteTest {

    @Test
    public void checkAppName() throws Exception {
        PonyNote activity = Robolectric.buildActivity(PonyNote.class).create().get();
        String appName = activity.getResources().getString(R.string.app_name);
        assertThat(appName, equalTo("Pony Note"));
    }
}
