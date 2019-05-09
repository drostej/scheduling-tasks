### Starten der spring boot Anwendung mit mvn
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

## Tests

#### 1.) In einem Interval alle 5 Sekunden wird das Datum mit date now ausgegeben und das Parallel das Datum des Containers verändert


#### 2.) Der Cron Expression liegt in der Zukunft und die Conainterzeit kurz davor gelegt 

Vorbedingungen:
@Scheduled(cron = "0 15 15 ? * *") // 15:15
Container Zeit auf 15:14 vorverlegt

Ergebnis:
Java Cron Scheduler wird nicht gestartet

### 3.) der Cron Expression ist ein Inverval (alle 5 min und die Anwendung ) die 

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


