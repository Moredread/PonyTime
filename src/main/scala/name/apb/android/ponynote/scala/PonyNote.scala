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

package name.apb.android.ponynote.scala

import android.os.Bundle
import android.content.Context

import name.apb.android.ponynote.java.db.DatabaseHelper
import name.apb.android.ponynote.java.db.Note
import android.widget.{TextView, ArrayAdapter, ListView, Button}

import android.view.{LayoutInflater, ViewGroup, View}

import org.scaloid.common._

class PonyNote extends SActivity with db.ORMLiteDatabaseHelperTrait[DatabaseHelper] {
  var noteListView: ListView = null

  override def onCreate(savedInstanceState: Bundle): Unit = {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main)

    find[Button](R.id.new_button).onClick {
      EditNote.callMe(this)
    }

    find[Button](R.id.about_button).onClick {
      AboutDialog.callMe(this)
    }

    noteListView = this.findViewById(R.id.noteListView).asInstanceOf[ListView]
  }

  override def onResume(): Unit = {
    super.onResume()

    fillListView()
    debug("List has " + noteListView.getCount + " entries")
  }

  def fillListView(): Unit = {
    info("Restore note list view")

    val dao = getHelper.getNoteDao
    val builder = dao.queryBuilder
    builder.orderBy(Note.LAST_CHANGED_DATE_COLUMN_NAME, false)
    val list = dao.query(builder.prepare)

    debug("We have " + list.size + " notes")

    val noteAdapter: ArrayAdapter[Note] = new ArrayAdapter[Note](this, R.layout.note_row, list) {
      override def getView(position: Int, convertView: View, parent: ViewGroup): View = {
        val v: View = Option(convertView) getOrElse getSystemService(Context.LAYOUT_INFLATER_SERVICE).asInstanceOf[LayoutInflater].inflate(R.layout.note_row, null)
        val note: Note = getItem(position)

        v.findViewById(R.id.note_text).asInstanceOf[TextView].setText(note.getNote)
        v.onClick(EditNote.callMeWithNoteId(this.getContext, note.getId))

        debug("Adding note " + position)

        return v
      }
    }

    noteListView.setAdapter(noteAdapter)
  }

}