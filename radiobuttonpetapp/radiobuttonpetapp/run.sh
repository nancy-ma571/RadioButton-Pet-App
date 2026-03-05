#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

# Launch Java outside Snap-injected runtime paths to avoid GLIBC symbol issues.
exec env -u SNAP -u SNAP_NAME -u SNAP_REVISION -u SNAP_ARCH -u SNAP_DATA -u SNAP_COMMON \
  -u SNAP_USER_DATA -u SNAP_USER_COMMON -u SNAP_LIBRARY_PATH -u SNAP_CONTEXT -u SNAP_COOKIE \
  -u LOCPATH -u GTK_PATH -u GTK_EXE_PREFIX -u GIO_MODULE_DIR -u GSETTINGS_SCHEMA_DIR \
  -u GDK_PIXBUF_MODULEDIR -u GDK_PIXBUF_MODULE_FILE -u GTK_IM_MODULE_FILE \
  -u XDG_DATA_DIRS -u XDG_DATA_HOME \
  PATH="/usr/bin:/bin" JAVA_HOME="/usr/lib/jvm/default-java" \
  /usr/bin/java -cp src RadioButtonPetApp
