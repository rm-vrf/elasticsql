@echo off

java -Djava.ext.dirs=.\lib ^
-Dfile.encoding=UTF-8 ^
-jar elasticsql.jar ^
--port=5050 ^
--log.level=DEBUG 
