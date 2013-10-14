/*
 * Pony Time
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
import com.j256.ormlite.dao.Dao
import name.apb.android.ponytime.java.db.DatabaseHelper
import name.apb.android.ponytime.java.db.Activity
import org.junit._
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.robolectric.util.{ActivityController, DatabaseConfig}
import java.util.Properties

@DatabaseConfig.UsingDatabaseMap(classOf[SQLMap])
@RunWith(classOf[RobolectricTestRunner]) class EditActivityTest {
  private var activityController: ActivityController[EditActivity] = null
  private var activity: EditActivity = null
  private var helper: DatabaseHelper = null
  private var dao: Dao[Activity, Integer] = null

  val props: Properties = System.getProperties
  props.setProperty("robolectric.logging", "stdout")

  @Before def setUp() {
    activityController = Robolectric.buildActivity(classOf[EditActivity]).create
    activity = activityController.get
    helper = activity.getHelper()
    dao = helper.getActivityDao

    helper.getWritableDatabase

    // reset database
    // TODO: recreate via ORMLite, but no idea how to do that

    val allNotes = dao.queryForAll()
    dao.delete(allNotes)
  }

  @Test def newEntryIsEmpty() {
    activity.onCreate()

    assertThat(activity.nameEditText.text.length, equalTo(0))
  }

  @Test def editingNoteHasPreviousTextSet() {
    val text = "Hello World"
    val activity1 = new Activity(text)

    dao.create(activity1)

    val intent = new Intent(Robolectric.getShadowApplication.getApplicationContext, classOf[EditActivity])
    intent.putExtra(EditActivity.ACTIVITY_ID, activity1.getId)
    activity = Robolectric.buildActivity(classOf[EditActivity]).withIntent(intent).create.get

    assertThat(activity.nameEditText.text.toString, equalTo(text))
  }
}

