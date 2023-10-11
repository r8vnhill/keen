<#
    Copyright 2015 the original author or authors.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
#>

$DEBUG = $env:DEBUG
if (-not $DEBUG) {
    $Host.UI.RawUI.WindowTitle = "Gradle startup script for Windows"
}

$DIRNAME = Split-Path -Parent $MyInvocation.MyCommand.Definition
if (-not $DIRNAME) { $DIRNAME = "." }
$APP_BASE_NAME = [System.IO.Path]::GetFileNameWithoutExtension($MyInvocation.MyCommand.Name)
$APP_HOME = $DIRNAME

# Resolve any "." and ".." in APP_HOME to make it shorter.
$APP_HOME = Resolve-Path $APP_HOME

# Default JVM options
$DEFAULT_JVM_OPTS = "-Xmx64m", "-Xms64m"

# Find java.exe
if ($env:JAVA_HOME) {
    $JAVA_EXE = Join-Path $env:JAVA_HOME "bin\java.exe"
    if (-not (Test-Path $JAVA_EXE)) {
        Write-Host "`nERROR: JAVA_HOME is set to an invalid directory: $env:JAVA_HOME`n"
        Write-Host "Please set the JAVA_HOME variable in your environment to match the"
        Write-Host "location of your Java installation."
        exit 1
    }
} else {
    $JAVA_EXE = Get-Command java.exe -ErrorAction SilentlyContinue
    if (-not $JAVA_EXE) {
        Write-Host "`nERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.`n"
        Write-Host "Please set the JAVA_HOME variable in your environment to match the"
        Write-Host "location of your Java installation."
        exit 1
    } else {
        $JAVA_EXE = $JAVA_EXE.Source
    }
}

# Setup the command line
$CLASSPATH = Join-Path $APP_HOME "gradle\wrapper\gradle-wrapper.jar"

# Execute Gradle
& "$JAVA_EXE" $DEFAULT_JVM_OPTS $env:JAVA_OPTS $env:GRADLE_OPTS "-Dorg.gradle.appname=$APP_BASE_NAME" -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain @args

if ($LASTEXITCODE -ne 0) {
    if ($env:GRADLE_EXIT_CONSOLE) { exit 1 }
    Write-Error "Gradle execution failed."
}
