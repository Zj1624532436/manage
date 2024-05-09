@echo off  
start /min cmd /k npm run serve
start /min cmd /k java -Dfile.encoding=utf-8 -jar D:\project\manage\target\manage-0.0.1-SNAPSHOT.jar