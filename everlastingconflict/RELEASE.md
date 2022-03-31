# Empaquetado en JAR
Se deben seguir los siguientes pasos : 
1. Compilar el proyecto en un jar usando "Build / Build Artifacts... / RTS:jar / Build"
2. Copiar archivo generado en "out/artifacts/RTS_jar/RTS.jar" y moverlo al escritorio
3. Ejecutar Jarsplice.jar
4. En el punto 1 de "Add Jars" seleccionamos el "RTS.jar" de los puntos anteriores y todos los .jar de la carpeta "libs"
5. En el "Add natives" añadimos todos los ".dll" que se encuentran en la carpeta "libs" del proyecto
6. En "Main class" escribimos "everlastingconflict.RTS"
7. En "Create FAT JAR" le damos al boton y escribimos el nombre de nuestro nuevo jar y la localizacion
  
