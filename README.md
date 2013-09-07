# Pony Note

[![Build Status](https://api.travis-ci.org/Moredread/PonyNote.png)](https://travis-ci.org/Moredread/PonyNote)

A simple android note taking app. Soon(tm) with secure syncing.

## Hints

Needs Java 7, because of generics bug http://bugs.sun.com/view_bug.do?bug_id=6302954

## License

Pony Note is available under the GPL v3. See the LICENSE file for details.

# TODO

* use https://github.com/square/fest-android for testing

## Problems with android + scala + robolectric + ORMlite

### robolectric deletes db after each test
* should be fixable by using file db, but haven't found docs yet (FIXED, looked at changeset https://github.com/robolectric/robolectric/commit/7b75327afc139b85299f4c0fe95512cd276a494b
* still the setup is not good, need to delete notes manually, should be done by ORMLite
* Also ORMLite should request file db but, actually requests a memory db, or the robolectric code does not handle it correctly (TODO: Bug report)
