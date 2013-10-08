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

import org.scaloid.common.{STextView, SVerticalLayout, SActivity}
import android.content.{Intent, Context}
import android.text.util.Linkify

/**
 * Shows an about page, information about the app and the license
 *
 * TODO: add libs
 */
class AboutDialog extends SActivity {
  val text = " - a note taking application for Android\n\nCopyright (C) 2013\nAndre-Patrick Bubel <code@andre-bubel.de>\n\nThis program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.\n\nThis program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more details.\n\nYou should have received a copy of the GNU General Public License along with this program.  If not, see\nhttp://www.gnu.org/licenses"
  lazy val textView = new STextView()

  onCreate {
    contentView(new SVerticalLayout += textView)

    val appname = getResources().getString(R.string.app_name)
    textView.setText(appname + text)
    Linkify.addLinks(textView, Linkify.ALL)
  }
}


object AboutDialog {
  /**
   * Helper method to start this activity with a prepared intent.
   *
   * @param c       the calling context
   */
  def callMe(c: Context) {
    c.startActivity(new Intent(c, classOf[AboutDialog]))
  }

}