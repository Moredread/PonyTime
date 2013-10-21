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

import name.apb.android.ponytime.java.db.{Activity, DatabaseHelper}
import org.scaloid.common._
import android.content.{Intent, Context}

import com.actionbarsherlock.app.ActionBar
import com.actionbarsherlock.view.Menu
import com.actionbarsherlock.view.MenuItem
import com.actionbarsherlock.view.MenuInflater
import com.actionbarsherlock.app.SherlockActivity

class EditActivity extends SherlockActivity with SActivity with db.ORMLiteDatabaseHelperTrait[DatabaseHelper] {

  lazy val nameEditText = new SEditText()
  lazy val saveButton = new SButton("Save", {
    saveToDb();
    finish()
  })

  var activityId: Option[Integer] = None

  onCreate {
    contentView(new SScrollView += new SVerticalLayout {
      this +=
        nameEditText +=
        saveButton
    })

    if (getActivityIdFromIntent == -1) {
      info("creating new activity")
      this.setTitle("Create Activity")
    } else {
      info("editing activity " + getActivityIdFromIntent)
      loadFromDb(getActivityIdFromIntent)
    }
  }

  /**
   * Returns the note ID from the given intent
   */
  def getActivityIdFromIntent: Integer = {
    getIntent.getIntExtra(EditActivity.ACTIVITY_ID, -1)
  }

  def saveToDb() {
    val newActivity: Activity = new Activity()

    activityId match {
      case None => {}
      case Some(i: Integer) => newActivity.setId(i)
    }

    newActivity.setName(nameEditText.text.toString)

    val dao = getHelper.getActivityDao

    dao.createOrUpdate(newActivity)

    assert(newActivity.getId != null)

    // update the node ID, as a new node might have been created
    activityId = Some(newActivity.getId)
  }

  def loadFromDb(loadActivityId: Integer) {
    val dao = getHelper.getActivityDao
    val activity = dao.queryForId(loadActivityId)

    nameEditText.setText(activity.getName)
    activityId = Some(loadActivityId)
  }
}

object EditActivity {
  val ACTIVITY_ID = "activityId"

  /**
   * Helper method to start this activity with a prepared intent. A new note is created.
   *
   * @param c       the calling context
   */
  def callMe(c: Context) {
    c.startActivity(new Intent(c, classOf[EditActivity]))
  }

  /**
   * Helper method to start this activity with a prepared intent
   *
   * @param c       the calling context
   * @param activityId  the id of the activity that should be edited
   */
  def callMeWithActivityId(c: Context, activityId: Integer) {
    val intent = new Intent(c, classOf[EditActivity])
    intent.putExtra(ACTIVITY_ID, activityId)
    c.startActivity(intent)
  }
}
