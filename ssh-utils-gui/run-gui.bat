rem ------------------------------------------------
@echo off
cd /d %~dp0
start "run" "%JAVA_HOME%\bin\javaw" -Xmx128m -cp "target/ssh-utils-gui.jar" ru.ezhov.ssh.utils.gui.Application