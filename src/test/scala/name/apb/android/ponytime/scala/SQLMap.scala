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

import java.sql.ResultSet
import java.io.File
import org.robolectric.util.DatabaseConfig

// Use file db for in memory db
// BUG: No idea why this is necessary, ORMLite should open file db anyhow, file bug report
class SQLMap extends DatabaseConfig.DatabaseMap {
  def getDriverClassName: String = {
    return "org.sqlite.JDBC"
  }

  def getConnectionString(file: File): String = {
    return "jdbc:sqlite:" + file.getAbsolutePath
  }

  def getMemoryConnectionString: String = {
    //return "jdbc:sqlite::memory:"
    return "jdbc:sqlite:test.db"
  }

  def getSelectLastInsertIdentity: String = {
    return "SELECT last_insert_rowid() AS id"
  }

  def getResultSetType: Int = {
    return ResultSet.TYPE_FORWARD_ONLY
  }
}
