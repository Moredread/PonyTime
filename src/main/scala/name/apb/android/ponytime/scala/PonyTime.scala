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

import android.os.Bundle
import android.content.Context

import name.apb.android.ponytime.java.db.DatabaseHelper
import name.apb.android.ponytime.java.db.Activity
import android.widget.{TextView, ArrayAdapter, ListView, Button}

import com.actionbarsherlock.app.ActionBar
import com.actionbarsherlock.view.Menu
import com.actionbarsherlock.view.MenuItem
import com.actionbarsherlock.view.MenuInflater
import com.actionbarsherlock.app.SherlockActivity

import android.view.{LayoutInflater, ViewGroup, View}

import org.scaloid.common._

class PonyTime extends SherlockActivity with SActivity with db.ORMLiteDatabaseHelperTrait[DatabaseHelper] {
  var activityListView: ListView = null

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    find[Button](R.id.new_button).onClick {
      EditActivity.callMe(this)
    }

    find[Button](R.id.about_button).onClick {
      AboutDialog.callMe(this)
    }

    activityListView = this.findViewById(R.id.activityListView).asInstanceOf[ListView]
  }

  override def onResume(): Unit = {
    super.onResume()

    fillListView()
    debug("List has " + activityListView.getCount + " entries")
  }

  def fillListView(): Unit = {
    info("Restore activity list view")

    val dao = getHelper.getActivityDao
    val builder = dao.queryBuilder
    builder.orderBy(Activity.LAST_CHANGED_DATE_COLUMN_NAME, false)
    val list = dao.query(builder.prepare)

    debug("We have " + list.size + " activities")

    val activityAdapter: ArrayAdapter[Activity] = new ArrayAdapter[Activity](this, R.layout.activity_row, list) {
      override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
        val v: View = Option(convertView) getOrElse getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater].inflate(R.layout.activity_row, null)
        val activity: Activity = getItem(position)

        v.findViewById(R.id.activity_text).asInstanceOf[TextView].setText(activity.getName)
        v.onClick({
          info("Starting editing dialog of activity " + position)
          EditActivity.callMeWithActivityId(this.getContext, activity.getId)
        })

        debug("Adding activity " + position)

        return v
      }
    }

    activityListView.setAdapter(activityAdapter)
  }

}