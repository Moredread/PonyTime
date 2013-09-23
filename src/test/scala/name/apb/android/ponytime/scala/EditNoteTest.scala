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

package name.apb.android.ponytime.scala

import android.content.Intent
import android.widget.Button
import android.widget.ListView
import com.j256.ormlite.dao.Dao
import name.apb.android.ponytime.java.db.DatabaseHelper
import name.apb.android.ponytime.java.db.Note
import org.junit._
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows._
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import java.lang.String
import org.robolectric.util.{ActivityController, DatabaseConfig}
import java.util.Properties

@DatabaseConfig.UsingDatabaseMap(classOf[SQLMap])
@RunWith(classOf[RobolectricTestRunner]) class EditNoteTest {
  private var activityController: ActivityController[EditNote] = null
  private var activity: EditNote = null
  private var helper: DatabaseHelper = null
  private var dao: Dao[Note, Integer] = null

  val props: Properties = System.getProperties
  props.setProperty("robolectric.logging", "stdout")

  @Before def setUp() {
    activityController = Robolectric.buildActivity(classOf[EditNote]).create
    activity = activityController.get
    helper = activity.getHelper()
    dao = helper.getNoteDao

    helper.getWritableDatabase

    // reset database
    // TODO: recreate via ORMLite, but no idea how to do that

    val allNotes = dao.queryForAll()
    dao.delete(allNotes)
  }

  @Test def newEntryIsEmpty() {
    activity.onCreate()

    assertThat(activity.noteEditText.text.length, equalTo(0))
  }

  @Test def editingNoteHasPreviousTextSet() {
    val note = new Note()
    val text = "Hello World"

    note.setNote(text)
    dao.create(note)

    val intent = new Intent(Robolectric.getShadowApplication.getApplicationContext, classOf[EditNote])
    intent.putExtra(EditNote.NOTE_ID, note.getId)
    activity = Robolectric.buildActivity(classOf[EditNote]).withIntent(intent).create.get

    assertThat(activity.noteEditText.text.toString, equalTo(text))
  }
}

