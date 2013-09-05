package name.apb.android.ponynote.scala

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

import android.content.Intent
import android.widget.Button
import android.widget.ListView
import com.j256.ormlite.dao.Dao
import name.apb.android.ponynote.java.db.DatabaseHelper
import name.apb.android.ponynote.java.db.Note
import org.junit._
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows._
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import java.lang.String

@RunWith(classOf[RobolectricTestRunner]) class PonyNoteTest {
  private var activity: PonyNote = null

  @Before def setUp() {
    activity = Robolectric.buildActivity(classOf[PonyNote]).create.get
  }

  @Test def checkAppName() {
    val appName: String = activity.getResources.getString(R.string.app_name)

    assertThat(appName, equalTo("Pony Note"))
  }

  @Test def checkListEmptyAtStart() {
    val list: ListView = activity.noteListView

    assertThat(list.getChildCount, equalTo(0))
  }

  @Test def clickAboutButton() {
    val aboutButton: Button = activity.findViewById(R.id.about_button).asInstanceOf[Button]
    Robolectric.clickOn(aboutButton)

    val shadowActivity: ShadowActivity = Robolectric.shadowOf(activity)
    val startedIntent: Intent = shadowActivity.getNextStartedActivity
    val shadowIntent: ShadowIntent = Robolectric.shadowOf(startedIntent)

    assertThat(shadowIntent.getComponent.getClassName, equalTo(classOf[AboutDialog].getName))
  }

  @Test def checkDaoGetting() {
    val helper: DatabaseHelper = activity.getHelper()

    val dao: Dao[Note, Integer] = helper.getNoteDao

    assertNotNull(dao)
  }

  @Test def databaseShouldBeEmptyByDefault() {
    val helper: DatabaseHelper = activity.getHelper()

    val dao: Dao[Note, Integer] = helper.getNoteDao

    assertThat(dao.countOf, equalTo(0L))
  }

  @Test def correctNumberOfEntries() {
    val note1: Note = new Note
    val note2: Note = new Note

    note1.setNote("Hello")
    note2.setNote("World")

    val dao = activity.getHelper().getNoteDao

    dao.create(note1)
    dao.create(note2)

    assertThat(dao.countOf, equalTo(2L))
  }
}

