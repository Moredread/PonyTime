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

package name.apb.android.ponytime.scala.db

import com.j256.ormlite.dao.Dao
import name.apb.android.ponytime.java.db.DatabaseHelper
import name.apb.android.ponytime.java.db.Activity
import org.junit._
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import java.lang.IllegalArgumentException
import org.robolectric.util.DatabaseConfig
import java.util.{Date, Properties}
import name.apb.android.ponytime.scala._

@DatabaseConfig.UsingDatabaseMap(classOf[SQLMap])
@RunWith(classOf[RobolectricTestRunner]) class ActivityTest {
  private var activity: PonyTime = null
  private var helper: DatabaseHelper = null
  private var dao: Dao[Activity, Integer] = null

  val props: Properties = System.getProperties
  props.setProperty("robolectric.logging", "stdout")

  @Before def setUp() {
    activity = Robolectric.buildActivity(classOf[PonyTime]).create.get
    helper = activity.getHelper
    dao = helper.getActivityDao

    helper.getWritableDatabase

    // reset database
    // TODO: recreate via ORMLite, but no idea how to do that

    val allNotes = dao.queryForAll()
    dao.delete(allNotes)
  }

  @Test def createEmptyActivity() {
    val activity = new Activity

    assertNull(activity.getId)
    assertNull(activity.getLastChangeDate)
    assertNull(activity.getName)
  }

  @Test def createActivity() {
    val name = "Hello World"
    val activity = new Activity(name)

    assertThat(activity.getName, equalTo(name))
  }

  @Test def createActivityWithNameAsNull() {
    try {
      val activity = new Activity(null)
      fail("activity constructor should throw exception when called with null name")
    } catch {
      case e: IllegalArgumentException => {}
    }
  }

  @Test def lastChangeDateOnCreate() {
    val activity = new Activity("Hello World")

    Thread.sleep(100)

    assertTrue(activity.getLastChangeDate.before(new Date()))
  }

  @Test def lastChangeDateUpdate() {
    val activity = new Activity("Hello World")
    val createDate = activity.getLastChangeDate

    Thread.sleep(100)

    activity.setName("Hi")

    assertTrue(createDate.before(activity.getLastChangeDate))
  }

  @Test def addActivityToDB() {
    val activity = new Activity("Hello World")

    dao.create(activity)

    assertNotNull(activity.getId)

    val dbActivity = dao.queryForId(activity.getId)

    assertThat(dbActivity.getName, equalTo(activity.getName))
    assertThat(dbActivity.getId, equalTo(activity.getId))
    assertThat(dbActivity.getLastChangeDate, equalTo(activity.getLastChangeDate))
  }

  @Test def deleteActivityFromDB() {
    val activity = new Activity("Hello World")

    dao.create(activity)

    assertTrue(dao.idExists(activity.getId))

    dao.delete(activity)

    assertFalse(dao.idExists(activity.getId))
  }
}

