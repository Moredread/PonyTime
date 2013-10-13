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
import name.apb.android.ponytime.java.db.{Category, DatabaseHelper}
import org.junit._
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.hamcrest.CoreMatchers._
import org.junit.Assert._
import org.robolectric.util.DatabaseConfig
import java.util.{Date, Properties}
import name.apb.android.ponytime.scala._
import java.lang.IllegalArgumentException

@DatabaseConfig.UsingDatabaseMap(classOf[SQLMap])
@RunWith(classOf[RobolectricTestRunner]) class CategoryTest {
  private var activity: PonyTime = null
  private var helper: DatabaseHelper = null
  private var dao: Dao[Category, Integer] = null

  val props: Properties = System.getProperties
  props.setProperty("robolectric.logging", "stdout")

  @Before def setUp() {
    activity = Robolectric.buildActivity(classOf[PonyTime]).create.get
    helper = activity.getHelper
    dao = helper.getCategoryDao

    helper.getWritableDatabase

    // reset database
    // TODO: recreate via ORMLite, but no idea how to do that

    val allNotes = dao.queryForAll()
    dao.delete(allNotes)
  }

  @Test def createEmptyCategory() {
    val category = new Category

    assertNull(category.getId)
    assertNull(category.getLastChangeDate)
    assertNull(category.getName)
  }

  @Test def createCategory() {
    val name = "Hello World"
    val category = new Category(name)

    assertThat(category.getName, equalTo(name))
  }

  @Test def createCategoryWithNameAsNull() {
    try {
      val category = new Category(null)
      fail("activity constructor should throw exception when called with null name")
    } catch {
      case e: IllegalArgumentException => {}
    }
  }

  @Test def lastChangeDateOnCreate() {
    val category = new Category("Hello World")

    Thread.sleep(100)

    assertTrue(category.getLastChangeDate.before(new Date()))
  }

  @Test def lastChangeDateUpdate() {
    val category = new Category("Hello World")
    val createDate = category.getLastChangeDate

    Thread.sleep(100)

    category.setName("Hi")

    assertTrue(createDate.before(category.getLastChangeDate))
  }

  @Test def addCategoryToDB() {
    val category = new Category("Hello World")

    dao.create(category)

    assertNotNull(category.getId)

    val dbCategory = dao.queryForId(category.getId)

    assertThat(dbCategory.getName, equalTo(category.getName))
    assertThat(dbCategory.getId, equalTo(category.getId))
    assertThat(dbCategory.getLastChangeDate, equalTo(category.getLastChangeDate))
  }

  @Test def deleteCategoryFromDB() {
    val category = new Category("Hello World")

    dao.create(category)

    assertTrue(dao.idExists(category.getId))

    dao.delete(category)

    assertFalse(dao.idExists(category.getId))
  }
}

