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

import name.apb.android.ponytime.java.db.{Note, DatabaseHelper}
import android.os.Bundle
import org.scaloid.common._
import android.content.{Intent, Context}
import android.text.method.ScrollingMovementMethod

class EditNote extends SActivity with db.ORMLiteDatabaseHelperTrait[DatabaseHelper] {

  lazy val noteEditText = new SEditText()
  lazy val saveButton = new SButton("Save", {
    saveToDb(); finish()
  })

  var noteId: Option[Integer] = None

  onCreate {
    contentView(new SScrollView += new SVerticalLayout { this +=
      noteEditText +=
      saveButton
    })

    if (getNoteIdFromIntent == -1) {
      info("creating new note")
      this.setTitle("Create Note")
    } else {
      info("editing note " + getNoteIdFromIntent)
      loadFromDb(getNoteIdFromIntent)
    }
  }

  /**
   * Returns the note ID from the given intent
   */
  def getNoteIdFromIntent: Integer = {
    getIntent.getIntExtra(EditNote.NOTE_ID, -1)
  }

  def saveToDb() {
    val newNote: Note = new Note()

    noteId match {
      case None => {}
      case Some(i: Integer) => newNote.setId(i)
    }

    newNote.setNote(noteEditText.text.toString)

    val dao = getHelper.getNoteDao

    dao.createOrUpdate(newNote)

    assert(newNote.getId != null)

    // update the node ID, as a new node might have been created
    noteId = Some(newNote.getId)
  }

  def loadFromDb(loadNoteId: Integer) {
    val dao = getHelper.getNoteDao
    val note = dao.queryForId(loadNoteId)

    noteEditText.setText(note.getNote)
    noteId = Some(loadNoteId)
  }
}

object EditNote {
  val NOTE_ID = "noteId"

  /**
   * Helper method to start this activity with a prepared intent. A new note is created.
   *
   * @param c       the calling context
   */
  def callMe(c: Context) {
    c.startActivity(new Intent(c, classOf[EditNote]))
  }

  /**
   * Helper method to start this activity with a prepared intent
   *
   * @param c       the calling context
   * @param noteId  the note id that should be edited
   */
  def callMeWithNoteId(c: Context, noteId: Integer) {
    val intent = new Intent(c, classOf[EditNote])
    intent.putExtra(NOTE_ID, noteId)
    c.startActivity(intent)
  }
}
