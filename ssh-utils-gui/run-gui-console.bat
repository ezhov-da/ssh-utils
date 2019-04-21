rem ------------------------------------------------
@echo off
cd /d %~dp0
"%JAVA_HOME%\bin\java" -Xmx128m -cp "target/ssh-utils-gui.jar" ru.ezhov.ssh.utils.gui.Application