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

import com.j256.ormlite.android.apptools.{OrmLiteSqliteOpenHelper, OpenHelperManager}
import org.scaloid.common._

trait ORMLiteDatabaseHelperTrait[V <: OrmLiteSqliteOpenHelper] extends SActivity {
  var databaseHelper: Option[V] = None

  onDestroy {
    databaseHelper match {
      case Some(_) => {
        OpenHelperManager.releaseHelper()
        databaseHelper = None
        debug("destroyed database helper")
      }
      case None => null
    }
  }

  /**
   * See http://stackoverflow.com/questions/8208179/scala-obtaining-a-class-object-from-a-generic-type
   */
  def getHelper()(implicit m: Manifest[V]): V = {
    databaseHelper match {
      case Some(_) => null
      case None => databaseHelper = Option(OpenHelperManager.getHelper(this, m.runtimeClass.asInstanceOf[Class[V]]))
    }
    databaseHelper.get
  }
}
