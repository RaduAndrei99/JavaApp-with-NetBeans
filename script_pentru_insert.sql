INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Ignat Mihaela', 1991129226198, '0721364171', 'eusuntignat@yahoo.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Stan Ionut Mirel', 1992348367432, '0754562171', 'eusutnstan@gmail.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Mircea Radu', 1991114626198, '0754124171', NULL);
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Mircea Marius',1992232332187, '0719355221','eusuntmirceamarius@gmail.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Popescu Stefanel', 1991112356198, '0754894171', 'eusuntpopescu@yahoo.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Ionescu Aurel',    1991234562347, '0754982561', NULL);
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Prodan Maria Ioana', 1991234962347,'0750964171',  'eusuntprdanmaria@gmail.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Mircea Radu', 1937534562347, '0754367821',  'eusuntmircea@gmail.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Ionescu Vlad', 1937543542354, '0754098171',  'eusuntvlad@gmail.com');
INSERT INTO clienti (nume,cnp,telefon,email) VALUES ('Pandelescu Stefania-Cezara', 1993452678453, '0753982674',  'eusuntstefaniacezara@yahoo.com');


INSERT INTO tip_serviciu (nume_serviciu,pret_per_kilowatt) VALUES ('CASNIC_A',0.48);
INSERT INTO tip_serviciu (nume_serviciu,pret_per_kilowatt) VALUES ('CASNIC_B',0.45);
INSERT INTO tip_serviciu (nume_serviciu,pret_per_kilowatt) VALUES ('IND_A',0.33);
INSERT INTO tip_serviciu (nume_serviciu,pret_per_kilowatt) VALUES ('IND_B',0.3);
INSERT INTO tip_serviciu (nume_serviciu,pret_per_kilowatt) VALUES ('CAS_ECO',0.39);


INSERT INTO Conturi_bancare (nume_cont,IBAN,suma_curenta) VALUES ('BRD Targu Mures','RO53BRDE270SV23904012700',10);
INSERT INTO Conturi_bancare (nume_cont,IBAN,suma_curenta) VALUES ('ING Bank Tirgu Mures','RO27INGB0014000028188911',67890.30);
INSERT INTO Conturi_bancare (nume_cont,IBAN,suma_curenta) VALUES ('UniCredit Bank Bucuresti','RO58BACX0000003701625003',54987.24);
INSERT INTO Conturi_bancare (nume_cont,IBAN,suma_curenta) VALUES ('CreditCoop Cluj-Napoca','RO88CRCOX130013000088260',3456.12);
INSERT INTO Conturi_bancare (nume_cont,IBAN,suma_curenta) VALUES ('Trezorerie Sibiu','RO12TREZ5765069XXX007055',900);


INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Str. Cuza Voda 29, Iasi', 700037, TO_DATE('25.05.2020', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CASNIC_B'),1);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Strada Principala, nr. 82, Sat Buhalnita, Iasi', 707071, TO_DATE('01.10.2020', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CASNIC_A'),2);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Strada Principala, nr. 84, Sat Buhalnita, Iasi', 707071, TO_DATE('01.05.2020', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CASNIC_A'),2);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Strada Principala, nr. 86, Sat Buhalnita, Iasi', 707071, TO_DATE('01.06.2020', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CASNIC_B'),2);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Str. Stefan cel Mare si Sfint nr. 69, Iasi', 700075, TO_DATE('01.10.2018', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CASNIC_A'),4);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Bd. Carol I Nr.27, Dealul Copou', 700507, TO_DATE('01.05.2017', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='IND_B'),4);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Str. Alexandru Lapusneanu nr. 7-9', 700507, TO_DATE('1.01.2020', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CAS_ECO'),3);

INSERT INTO contracte (adresa_contract,cod_postal,data_start,id_serviciu,cod_client) 
VALUES ('Str. Stefan cel Mare nr. 4, Iasi', 700507, TO_DATE('1.08.2020', 'DD.MM.YYYY'),
(SELECT id_serviciu FROM tip_serviciu WHERE nume_serviciu='CAS_ECO'),7);


INSERT INTO Angajati (nume_angajat) VALUES ('Mihai Popovici');
INSERT INTO Angajati (nume_angajat) VALUES ('Stanescu Ion');
INSERT INTO Angajati (nume_angajat) VALUES ('Grogoras Mircea');
INSERT INTO Angajati (nume_angajat) VALUES ('Ionescu Dan');
INSERT INTO Angajati (nume_angajat) VALUES ('Zamfirescu Gheorghe');


INSERT INTO Contoare VALUES (NULL,250600,250500,TO_DATE('01.06.2019', 'DD.MM.YYYY'),TO_DATE('25.07.2020', 'DD.MM.YYYY'),0,1);
INSERT INTO Contoare VALUES (NULL,50200,50150,TO_DATE('21.04.2019', 'DD.MM.YYYY'),TO_DATE('25.08.2019', 'DD.MM.YYYY'),1,2);
INSERT INTO Contoare VALUES (NULL,450120,450300,TO_DATE('02.05.2019', 'DD.MM.YYYY'),TO_DATE('25.08.2019', 'DD.MM.YYYY'),1,3);
INSERT INTO Contoare VALUES (NULL,350280,350300,TO_DATE('02.06.2019', 'DD.MM.YYYY'),TO_DATE('27.09.2019', 'DD.MM.YYYY'),0,4);
INSERT INTO Contoare VALUES (NULL,450120,450270,TO_DATE('23.10.2019', 'DD.MM.YYYY'),TO_DATE('22.12.2019', 'DD.MM.YYYY'),1,5);
INSERT INTO Contoare VALUES (NULL,450120,450080,TO_DATE('12.06.2019', 'DD.MM.YYYY'),TO_DATE('27.09.2019', 'DD.MM.YYYY'),0,6);


INSERT INTO contoare_angajati (Contoare_id_contor, Angajati_id_angajat, data_citirii) VALUES (1,2,TO_DATE('01.06.2020', 'DD.MM.YYYY'));
INSERT INTO contoare_angajati (Contoare_id_contor, Angajati_id_angajat, data_citirii) VALUES (2,2,TO_DATE('01.12.2020', 'DD.MM.YYYY'));
INSERT INTO contoare_angajati (Contoare_id_contor, Angajati_id_angajat, data_citirii) VALUES (3,4,TO_DATE('01.07.2020', 'DD.MM.YYYY'));
INSERT INTO contoare_angajati (Contoare_id_contor, Angajati_id_angajat, data_citirii) VALUES (4,2,TO_DATE('02.06.2020', 'DD.MM.YYYY'));
INSERT INTO contoare_angajati (Contoare_id_contor, Angajati_id_angajat, data_citirii) VALUES (5,2,TO_DATE('02.06.2020', 'DD.MM.YYYY'));
INSERT INTO contoare_angajati (Contoare_id_contor, Angajati_id_angajat, data_citirii) VALUES (6,2,TO_DATE('03.08.2020', 'DD.MM.YYYY'));


INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (250500,250600,TO_DATE('07.10.2020', 'DD.MM.YYYY'),1,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (49800,49900,TO_DATE('07.10.2020', 'DD.MM.YYYY'),2,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450000,450120,TO_DATE('07.10.2020', 'DD.MM.YYYY'),3,'CITIRE_CLIENT',45);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (350000,350120,TO_DATE('07.10.2020', 'DD.MM.YYYY'),4,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450020,450120,TO_DATE('07.10.2020', 'DD.MM.YYYY'),5,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450010,450120,TO_DATE('07.10.2020', 'DD.MM.YYYY'),6,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450010,450120,TO_DATE('07.10.2020', 'DD.MM.YYYY'),7,'CITIRE_CLIENT',56);

INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (250600,250780,TO_DATE('07.11.2020', 'DD.MM.YYYY'),1,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (49900,50100,TO_DATE('07.11.2020', 'DD.MM.YYYY'),2,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450120,450245,TO_DATE('07.11.2020', 'DD.MM.YYYY'),3,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (350120,350232,TO_DATE('07.11.2020', 'DD.MM.YYYY'),4,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450120,450190,TO_DATE('07.11.2020', 'DD.MM.YYYY'),5,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450120,450333,TO_DATE('07.11.2020', 'DD.MM.YYYY'),6,'CITIRE_CLIENT',0);
INSERT INTO Facturi (index_vechi,index_nou,data_factura,cod_contract,tip_factura,suma_in_avans) VALUES (450120,455675,TO_DATE('07.11.2020', 'DD.MM.YYYY'),7,'CITIRE_CLIENT',0);


INSERT INTO Plata_factura (suma_plata,data_plata,numar_factura,id_cont) VALUES (25,TO_DATE('01.05.2017', 'DD.MM.YYYY'),3,3);
INSERT INTO Plata_factura (suma_plata,data_plata,numar_factura,id_cont) VALUES (120,TO_DATE('01.08.2017', 'DD.MM.YYYY'),2,1);
INSERT INTO Plata_factura (suma_plata,data_plata,numar_factura,id_cont) VALUES (205,TO_DATE('01.08.2017', 'DD.MM.YYYY'),5,4);
INSERT INTO Plata_factura (suma_plata,data_plata,numar_factura,id_cont) VALUES (120,TO_DATE('01.08.2017', 'DD.MM.YYYY'),9,1);

--parola_admin
INSERT INTO Conturi_admin VALUES(12,'7a6b2279766b696b6e777378');

COMMIT;
