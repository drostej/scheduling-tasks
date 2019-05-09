# Anforderung - Regressionstests an einer Anwendung über einen simuliert längern Zeitraum

```
Ziel dieser Tests ist es ....
Im Idealfall soll die Software nicht angepasst werden müssen, sondern die Zeit der Testumgenung
```



## 1. Anpassung der Systemzeit eines docker containers. 

Mit der Veränderung der Systemzeit sollen die wiederkehenden Aktionen, die von der Anwendung erwatet werden, getestet werden.
Für diese Tests müssen privileded Containers eingesetzt werden, da nur diese eine eigene Systemzeit haben. Das Feature wurde von docker
implementiert um docker Instanzen innerhalb anderer Docker Instanzen laufen zu lassen. 



### 1.1. Schritte mit dieser BeispielApp
./mvnw spring-boot:run

### Build des Images mit maven
./mvnw install dockerfile:build
And you can push the image to dockerhub with ./mvnw dockerfile:push.

### Starten des docker images

docker images

docker run 4baa - der Container startet

### Shell in dem Container starten
öffnen einer busybox shell
docker exec -it 119a4823ba5a /bin/sh


### Versuch in der busybox von spring die Zeit anzupassen
öffnen einer busybox shell
docker exec -it 119a4823ba5a /bin/sh

date 100616442009
sets it to 16:44 6th October 2009


date -s "2008-11-18 08:24:00"

## So stellt man die Betriebssystem Zeit im docker um
docker run --privileged 4baac00d2c72 date -s "2008-11-18 08:24:00"

In dem Container kann die Systemzeit dann mit date immer neu gesetzt werden.

### 1.2 Tests

1.2.1.) In einem Interval alle 5 Sekunden wird das Datum mit date now ausgegeben und das Parallel das Datum des Containers verändert


1.2.2.) Der Cron Expression liegt in der Zukunft und die Conainterzeit kurz davor gelegt 

Vorbedingungen:
@Scheduled(cron = "0 15 15 ? * *") // 15:15
Container Zeit auf 15:14 vorverlegt

Ergebnis:
Java Cron Scheduler wird nicht gestartet

1.1.33.) der Cron Expression ist ein Inverval (alle 5 min und die Anwendung ) die 

@Scheduled(cron = "0 0/5 * * * ?")

Der Versuch das Feuern der Jobs vorzuziehen:
```
Wed Jan 17 16:18:23 UTC 2018
/ # date -s "2018-01-17 16:24:00"
Wed Jan 17 16:24:00 UTC 2018
/ # date -s "2018-01-17 16:39:00"
Wed Jan 17 16:39:00 UTC 2018
/ # date -s "2018-01-17 16:44:00"
Wed Jan 17 16:44:00 UTC 2018
```
Ausgabe
```
2018-01-17 16:10:00.003  INFO 1 --- [pool-1-thread-1] hello.ScheduledTasks                     : The time is now 16:10:00
2018-01-17 16:15:00.006  INFO 1 --- [pool-1-thread-1] hello.ScheduledTasks                     : The time is now 16:15:00
2018-01-17 16:20:44.486  INFO 1 --- [pool-1-thread-1] hello.ScheduledTasks                     : The time is now 16:20:44
2018-01-17 16:27:47.147  INFO 1 --- [pool-1-thread-1] hello.ScheduledTasks                     : The time is now 16:27:47
2018-01-17 16:30:00.001  INFO 1 --- [pool-1-thread-1] hello.ScheduledTasks                     : The time is now 16:30:00
```

## Fazit
Das Ändern der Betriebssystemzeit ist für Java/Spring Jobs nicht relevant. Das spring boot scheduling organisiert sich selbt
Instanziert man ein Datumsopbjekt neu (newDate()), wird jedoch die im DockerContainer gesetzte Betriebssystemzeit ausgegeben.

Im dritten Versuch wird im logfile zwar "brav" die geänderte Betriebssystemzeit angezeigt. Eine Beobachtung mit der Stopuhr ergibt,
daß die logeinträge im 5 Minuten Interval erzeugt wurden.

Dies lies sich im logfile navhvollziehen.

## Quellen

https://spring.io/guides/gs/scheduling-tasks/
https://spring.io/guides/gs/spring-boot-docker/


## 2 Starten des Services mit Faketime
Versuch mit folgender Konfiguration den Zeitraum zur ersten Ausfühung zu verkürzen

Mit faketime die JVM Methode um Millisekunden vorstellen

java -agentpath:/path/to/libfaketime.jnilib \
    -XX:+UnlockDiagnosticVMOptions \
    -XX:DisableIntrinsic=_currentTimeMillis \
    -XX:CompileCommand=exclude,java/lang/System.currentTimeMillis \
    org.test.Main

Vorbedingung:
@Scheduled(fixedRate = 10000) // 10 Sekunden Interval



***Ergebnis:***
Mit einem Logging im scheduled taks zeigt, daß der Aufruf unbefrührt bleibt.
Der Effekt ist der gleiche wie im docker Container. 

Aufruf der Anwendung mit faketime    
***java -agentpath:./libfaketime.jnilib -XX:+UnlockDiagnosticVMOptions -XX:DisableIntrinsic=_currentTimeMillis -jar target/gs-scheduling-tasks-0.2.0.jar*** 
    
    
ohne Faketime
***java -jar target/gs-scheduling-tasks-0.2.0.jar***


***Quelle:***
https://github.com/arvindsv/faketime

#### 3 ClockService im data-order-servie

```
Hierbei wird der Versuch gestartet mit zusätzlicher in dem Sourcecode für die Busenesslogik nicht eingreifen zu mnüssen
und der JVM ein sogenanntes clock Ojekt mit einem Zeit Offset zu übertragen.
```

#### 4. Weitere Versuche, die man unternehmen kann...

Jeder Aktion einen Endpunkt zur Verfügung stellen und diesen zu Testzwecken von außen triggern.

Den scheduler in der Vergangenheit starten und datasnack buchen. Zu relevanten Aktionszeitpunkten startet man den Container neu. Die DB muß während der Neustarts konsitent sein.

Implementierung eines überwachenden Threads, der bei Abweichungen zwischen Systemzeit und Applikationszeit einen Reset veranlasst

 
   