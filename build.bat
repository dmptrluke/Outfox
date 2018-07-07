call ren src\main\resources\mcmod.info mcmod.dev.info
call ren src\main\resources\mcmod.gradle.info mcmod.info

call gradlew build

call ren src\main\resources\mcmod.info mcmod.gradle.info
call ren src\main\resources\mcmod.dev.info mcmod.info

pause