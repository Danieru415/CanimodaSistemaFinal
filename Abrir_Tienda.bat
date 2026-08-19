@echo off
title Sistema Canimoda
color 0A
echo ===================================================
echo     PRENDIENDO EL SISTEMA CANIMODA...
echo     Dame unos 15 segundos para conectar la Base de Datos.
echo ===================================================

:: 1. Entra a la carpeta demo y prende el motor
cd "%~dp0demo"
start "Motor Java" /min cmd /c ".\mvnw spring-boot:run"

:: 2. Espera 15 segundos para que cargue la nube Neon
timeout /t 15 /nobreak >nul

:: 3. Abre tu diseño con tu navegador por defecto (Brave, Edge, etc.)
cd "%~dp0"
start CanimodaOficial.html

exit