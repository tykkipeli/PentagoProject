# Viikkoraportti 3

### Mitä olen tehnyt tällä viikolla?
Tein ohjelmasta alustavan version. Lisäsin siihen ruman GUI:n, jonka koodi on pelkkää purkkakoodia ja kaikkien hyvien käytänteiden vastaista. 
Kuitenkin GUI:n kautta ohjelmaa on paljon kivempi testailla. Tein GUI:n java Swingillä, vaikken sitä osaakkaan ja kiersin pari ongelmaa purkkaratkaisuilla. 
Varsinaiseen työhön liittyn toteutin perus alpha-beta etsinnän, joka käyttää transpositiotaulua, todella yksinkertaista heuristiikaa ja iteratiivista syvenemistä.  
Sain jacocon ja checkstylen toimimaan ja lisäsin tekoälylle joitain testejä.

### Miten ohjelma on edistynyt?
Tekoäly näyttäisi toimivan ihan kohtalaisesti ja bottia on ihmisen jo todella vaikea voittaa jos sen antaa aloittaa. 
Jos itse aloittaa, niin taitava Pentagon pelaaja pystyy sen kuitenkin päihittämään, vaikka botti laskeekin jo noin viiden puolisiirron päähän. 
Olisi kuitenkin kiva, jos botti osaisi pelata niin hyvin, etten sitä itse pystyisi enää voittamaan vaikka saisinkin tehdä ensimmäisen siiron.

### Mitä opin tällä viikolla?
Algoritmeihin liittyen en varsinaisesti paljoa, mutta joitan java Swingin perusasioita opin, vaikka toteutinkin GUI:n varmaan jotenkin väärin.

### Mikä oli ongelmallista
Olen yrittänyt ymmärtää voisinko hyödyntää Geoffrey Irvingin artikkelissa https://arxiv.org/pdf/1404.0743.pdf kuvattuja superpositioita jotenkin omassa työssäni.

### Mitä teen seuraavaksi?
Jossain vaiheessa arraylist ja hashmap tulee varmaan korvata omilla toteutuksilla. Koodia on hyvä myös refaktoroida. Kuitenkin eniten tekisi mieli vain miettiä
tapoja, miten tekoälyä voisi parantaa.

### Ajan käyttö
Käytin 20 tuntia projektiin, mutta suurin osa ajasta kului eri ideoiden miettimiseen, miten tekoälyä voisi parantaa, joista mitään ei ole vielä varsinaisesti 
mietitty loppuun enkä oikein osaa päättää mihin suuntaa tekoälyä kannattaisi viedä. 
