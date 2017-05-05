# Android-Exam
## IMessage for Android

### Gruppen: Mikkel Ziemer, Daniel Winkel, Hazem Saeid og Phillip Brink.

### Formål
 - At lave vores eksamens projekt i Android Studio.
 - At lave en App der virker ligesom Imessage, blot til Android
 - Lave en App til Android telefoner.

### Appen <br>
Appen skal fungere ligesom Imessage og det er meningen at man skal kunne sende beskeder, audiobesked, billeder og videoer. Derudover så er det meningen at appen skal gemme op til 30 dags historik fra top 5 man skriver med og derudover skal den kunne hente de andre beskeder mens den åbnes.

 - Imessage bare til Android (AMessage) <br>
 - Den skal kunne sende beskeder. <br>
 - Det skal være muligt at sende sin lokation. <br>
 - Det skal være muligt at tage billeder og sende. <br>
 - Besked historik, på ens top 5 skal gemmes i SQLlite databasen på telefoner. <br>
 - Brugernavn og Password skal gemmes på telefonen så den hurtig kan kontrolleres. <br>
 - Token til at lave Get kald skal gemmes i appen, sammen med brugernavn og password. <br>
 - Liste over personer du skrive <br>
 - Knap til at skrive med nye, kan vælge mellem e-mail eller telefonnummer. <br>
 - Audiobesked (Talebesked) <br>
 - Lave en database til hvor alt historikken gemmes. <br>

### Plantegning over hvordan appen skal se ud.
 ![alt text](https://github.com/philliphb/Android-Exam/blob/master/Appen.png) <br>
#### - 1: Login
  - "Logo eller App-navn" i toppen.
  - "Username" and "Password" bruges til login knappen.
  Looks in the database, local on the phone (SQLLite) if it exsist collects token form phone to get chat from server. If the user do not exsist connects to main server to see if user exsist in main database and collects userinformation and token.
  - "Log in" knappen kigger i den locale database (SQLLite) på telefonen for at se om brugere har være logget ind. Hvis brugere har været logget in så gider den token til at lave sikkert get klad for at hente besked historik fra hoved databasen. Hvis bruger ikke har været på telefonen før så laver den et kald til hoved serveren for at se om bruger ligger i hoved database hvorefter den gemme brugernavn, password og token på telefonen. Hvis det er en gyldig bruger leder den bruger videre til billede 2.
  <br>
#### - 2: Overview
  - "Logo eller App-navn" udskiftes med Username.
  - Knap "+" brugers til at komme til 3 ny besked.
  - ved tryk på navn eller rundt om kommer man til 4 chat med den specifikke person.
  - Kan "-" er til at slette samtaler med den specifikke person.
#### 3: New Message
  - "Username" udskiftes med et field hvor man kan skrive nummer eller email.
  - "Besked felt" er tomt.
  - "Knap" brugers til at sende billeder, videoer, talebeskeder eller sin lokation.
  - "Besked du vil sende" Her skriver man sin bedsked.
#### - 4: Chat
  - "Username" udskiftes med navnet med den personer man skriver med.
  - "Besked felt" her er historikken for den man har skrevet med.
  - "Knap" brugers til at sende billeder, videoer, talebeskeder eller sin lokation.
  - "Besked du vil sende" Her skriver man sin bedsked.
<br>
#### Features <br>
##### Must have <br>
 - Send besked / oprette besked
 - Logge ind
 - Liste over folk du skriver med
 - Navigere rundt i appen
 - Matche brugernavn og password i SQLLite (På telefonen)
##### Nice <br>
 - Sende Billede
 - Sende Lokation
 - Sende Audio Besked
