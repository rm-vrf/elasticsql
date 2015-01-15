#!/bin/sh

# env
JAVA_OPTS=
PORT=5050
LOG_LEVEL=INFO
FILE_ENCODING=UTF-8

# resolve links - $0 may be a softlink
PRG="$0"
while [ -h "$PRG" ] ; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
PRGDIR=`dirname "$PRG"`

# set customer vars
if [ -r "$PRGDIR/setenv.sh" ]; then
  . "$PRGDIR/setenv.sh"
fi

LIB_DIR=$PRGDIR/lib
JAR_FILE=$PRGDIR/elasticsql.jar

java $JAVA_OPTS -Djava.ext.dirs=$LIB_DIR \
-Dfile.encoding=$FILE_ENCODING \
-jar $JAR_FILE \
--port=$PORT \
--log.level=$LOG_LEVEL
