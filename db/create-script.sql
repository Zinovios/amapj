CREATE TABLE ROLETRESORIER (ID BIGINT NOT NULL, UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE UTILISATEUR (ID BIGINT NOT NULL, CODEPOSTAL VARCHAR(255), COTISATION VARCHAR(255), EMAIL VARCHAR(255), ETATUTILISATEUR VARCHAR(255), LIBADR1 VARCHAR(255), NOM VARCHAR(255), NUMTEL1 VARCHAR(255), NUMTEL2 VARCHAR(255), PASSWORD VARCHAR(255), PRENOM VARCHAR(255), RESETPASSWORDDATE TIMESTAMP, RESETPASSWORDSALT VARCHAR(255), SALT VARCHAR(255), VILLE VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE DATEPERMANENCEUTILISATEUR (ID BIGINT NOT NULL, NUMSESSION INTEGER, DATEPERMANENCE_ID BIGINT, UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE PRODUCTEURUTILISATEUR (ID BIGINT NOT NULL, NOTIFICATION VARCHAR(255), PRODUCTEUR_ID BIGINT, UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE PRODUCTEURREFERENT (ID BIGINT NOT NULL, PRODUCTEUR_ID BIGINT, REFERENT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE MODELECONTRATPRODUIT (ID BIGINT NOT NULL, INDX INTEGER, PRIX INTEGER, MODELECONTRAT_ID BIGINT, PRODUIT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE MODELECONTRATDATEPAIEMENT (ID BIGINT NOT NULL, DATEPAIEMENT DATE, MODELECONTRAT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE MODELECONTRATEXCLUDE (ID BIGINT NOT NULL, DATE_ID BIGINT, MODELECONTRAT_ID BIGINT, PRODUIT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE PAIEMENT (ID BIGINT NOT NULL, ETAT VARCHAR(255), MONTANT INTEGER, CONTRAT_ID BIGINT, MODELECONTRATDATEPAIEMENT_ID BIGINT, REMISE_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE ROLEADMIN (ID BIGINT NOT NULL, UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE DATEPERMANENCE (ID BIGINT NOT NULL, DATEPERMANENCE DATE, PRIMARY KEY (ID))
CREATE TABLE PARAMETRES (ID BIGINT NOT NULL, BACKUPRECEIVER VARCHAR(255), ETATPLANNINGDISTRIBUTION VARCHAR(255), NOMAMAP VARCHAR(255), SENDINGMAILPASSWORD VARCHAR(255), SENDINGMAILUSERNAME VARCHAR(255), URL VARCHAR(255), VILLEAMAP VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE REMISEPRODUCTEUR (ID BIGINT NOT NULL, DATECREATION TIMESTAMP, DATEREMISE TIMESTAMP, MONTANT INTEGER, DATEPAIEMENT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE LOGACCESS (ID BIGINT NOT NULL, BROWSER VARCHAR(255), DATEIN TIMESTAMP, DATEOUT TIMESTAMP, IP VARCHAR(255), UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE PRODUIT (ID BIGINT NOT NULL, CONDITIONNEMENT VARCHAR(255), NOM VARCHAR(255), TYPFACTURATION VARCHAR(255), PRODUCTEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE MODELECONTRAT (ID BIGINT NOT NULL, DATEFININSCRIPTION DATE, DATEREMISECHEQUE DATE, DESCRIPTION VARCHAR(255), ETAT VARCHAR(255), GESTIONPAIEMENT VARCHAR(255), LIBCHEQUE VARCHAR(255), NOM VARCHAR(255), TEXTPAIEMENT VARCHAR(255), PRODUCTEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE NOTIFICATIONDONE (ID BIGINT NOT NULL, DATEENVOI TIMESTAMP, TYPNOTIFICATIONDONE VARCHAR(255), MODELECONTRATDATE_ID BIGINT, UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE PRODUCTEUR (ID BIGINT NOT NULL, DELAIMODIFCONTRAT INTEGER, NOM VARCHAR(255), PRIMARY KEY (ID))
CREATE TABLE MODELECONTRATDATE (ID BIGINT NOT NULL, DATELIV DATE, MODELECONTRAT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE CONTRATCELL (ID BIGINT NOT NULL, QTE INTEGER, CONTRAT_ID BIGINT, MODELECONTRATDATE_ID BIGINT, MODELECONTRATPRODUIT_ID BIGINT, PRIMARY KEY (ID))
CREATE TABLE CONTRAT (ID BIGINT NOT NULL, DATECREATION TIMESTAMP, DATEMODIFICATION TIMESTAMP, MONTANTAVOIR INTEGER, MODELECONTRAT_ID BIGINT, UTILISATEUR_ID BIGINT, PRIMARY KEY (ID))
ALTER TABLE UTILISATEUR ADD CONSTRAINT UNQ_UTILISATEUR_0 UNIQUE (nom, prenom)
ALTER TABLE UTILISATEUR ADD CONSTRAINT UNQ_UTILISATEUR_1 UNIQUE (email)
ALTER TABLE REMISEPRODUCTEUR ADD CONSTRAINT UNQ_REMISEPRODUCTEUR_0 UNIQUE (datePaiement_id)
ALTER TABLE MODELECONTRAT ADD CONSTRAINT UNQ_MODELECONTRAT_0 UNIQUE (nom)
ALTER TABLE PRODUCTEUR ADD CONSTRAINT UNQ_PRODUCTEUR_0 UNIQUE (nom)
ALTER TABLE CONTRAT ADD CONSTRAINT UNQ_CONTRAT_0 UNIQUE (modeleContrat_id, utilisateur_id)
ALTER TABLE ROLETRESORIER ADD CONSTRAINT FK_ROLETRESORIER_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE DATEPERMANENCEUTILISATEUR ADD CONSTRAINT FK_DATEPERMANENCEUTILISATEUR_DATEPERMANENCE_ID FOREIGN KEY (DATEPERMANENCE_ID) REFERENCES DATEPERMANENCE (ID)
ALTER TABLE DATEPERMANENCEUTILISATEUR ADD CONSTRAINT FK_DATEPERMANENCEUTILISATEUR_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE PRODUCTEURUTILISATEUR ADD CONSTRAINT FK_PRODUCTEURUTILISATEUR_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE PRODUCTEURUTILISATEUR ADD CONSTRAINT FK_PRODUCTEURUTILISATEUR_PRODUCTEUR_ID FOREIGN KEY (PRODUCTEUR_ID) REFERENCES PRODUCTEUR (ID)
ALTER TABLE PRODUCTEURREFERENT ADD CONSTRAINT FK_PRODUCTEURREFERENT_REFERENT_ID FOREIGN KEY (REFERENT_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE PRODUCTEURREFERENT ADD CONSTRAINT FK_PRODUCTEURREFERENT_PRODUCTEUR_ID FOREIGN KEY (PRODUCTEUR_ID) REFERENCES PRODUCTEUR (ID)
ALTER TABLE MODELECONTRATPRODUIT ADD CONSTRAINT FK_MODELECONTRATPRODUIT_MODELECONTRAT_ID FOREIGN KEY (MODELECONTRAT_ID) REFERENCES MODELECONTRAT (ID)
ALTER TABLE MODELECONTRATPRODUIT ADD CONSTRAINT FK_MODELECONTRATPRODUIT_PRODUIT_ID FOREIGN KEY (PRODUIT_ID) REFERENCES PRODUIT (ID)
ALTER TABLE MODELECONTRATDATEPAIEMENT ADD CONSTRAINT FK_MODELECONTRATDATEPAIEMENT_MODELECONTRAT_ID FOREIGN KEY (MODELECONTRAT_ID) REFERENCES MODELECONTRAT (ID)
ALTER TABLE MODELECONTRATEXCLUDE ADD CONSTRAINT FK_MODELECONTRATEXCLUDE_DATE_ID FOREIGN KEY (DATE_ID) REFERENCES MODELECONTRATDATE (ID)
ALTER TABLE MODELECONTRATEXCLUDE ADD CONSTRAINT FK_MODELECONTRATEXCLUDE_MODELECONTRAT_ID FOREIGN KEY (MODELECONTRAT_ID) REFERENCES MODELECONTRAT (ID)
ALTER TABLE MODELECONTRATEXCLUDE ADD CONSTRAINT FK_MODELECONTRATEXCLUDE_PRODUIT_ID FOREIGN KEY (PRODUIT_ID) REFERENCES MODELECONTRATPRODUIT (ID)
ALTER TABLE PAIEMENT ADD CONSTRAINT FK_PAIEMENT_MODELECONTRATDATEPAIEMENT_ID FOREIGN KEY (MODELECONTRATDATEPAIEMENT_ID) REFERENCES MODELECONTRATDATEPAIEMENT (ID)
ALTER TABLE PAIEMENT ADD CONSTRAINT FK_PAIEMENT_CONTRAT_ID FOREIGN KEY (CONTRAT_ID) REFERENCES CONTRAT (ID)
ALTER TABLE PAIEMENT ADD CONSTRAINT FK_PAIEMENT_REMISE_ID FOREIGN KEY (REMISE_ID) REFERENCES REMISEPRODUCTEUR (ID)
ALTER TABLE ROLEADMIN ADD CONSTRAINT FK_ROLEADMIN_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE REMISEPRODUCTEUR ADD CONSTRAINT FK_REMISEPRODUCTEUR_DATEPAIEMENT_ID FOREIGN KEY (DATEPAIEMENT_ID) REFERENCES MODELECONTRATDATEPAIEMENT (ID)
ALTER TABLE LOGACCESS ADD CONSTRAINT FK_LOGACCESS_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE PRODUIT ADD CONSTRAINT FK_PRODUIT_PRODUCTEUR_ID FOREIGN KEY (PRODUCTEUR_ID) REFERENCES PRODUCTEUR (ID)
ALTER TABLE MODELECONTRAT ADD CONSTRAINT FK_MODELECONTRAT_PRODUCTEUR_ID FOREIGN KEY (PRODUCTEUR_ID) REFERENCES PRODUCTEUR (ID)
ALTER TABLE NOTIFICATIONDONE ADD CONSTRAINT FK_NOTIFICATIONDONE_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE NOTIFICATIONDONE ADD CONSTRAINT FK_NOTIFICATIONDONE_MODELECONTRATDATE_ID FOREIGN KEY (MODELECONTRATDATE_ID) REFERENCES MODELECONTRATDATE (ID)
ALTER TABLE MODELECONTRATDATE ADD CONSTRAINT FK_MODELECONTRATDATE_MODELECONTRAT_ID FOREIGN KEY (MODELECONTRAT_ID) REFERENCES MODELECONTRAT (ID)
ALTER TABLE CONTRATCELL ADD CONSTRAINT FK_CONTRATCELL_MODELECONTRATDATE_ID FOREIGN KEY (MODELECONTRATDATE_ID) REFERENCES MODELECONTRATDATE (ID)
ALTER TABLE CONTRATCELL ADD CONSTRAINT FK_CONTRATCELL_CONTRAT_ID FOREIGN KEY (CONTRAT_ID) REFERENCES CONTRAT (ID)
ALTER TABLE CONTRATCELL ADD CONSTRAINT FK_CONTRATCELL_MODELECONTRATPRODUIT_ID FOREIGN KEY (MODELECONTRATPRODUIT_ID) REFERENCES MODELECONTRATPRODUIT (ID)
ALTER TABLE CONTRAT ADD CONSTRAINT FK_CONTRAT_UTILISATEUR_ID FOREIGN KEY (UTILISATEUR_ID) REFERENCES UTILISATEUR (ID)
ALTER TABLE CONTRAT ADD CONSTRAINT FK_CONTRAT_MODELECONTRAT_ID FOREIGN KEY (MODELECONTRAT_ID) REFERENCES MODELECONTRAT (ID)
CREATE TABLE SEQUENCE (SEQ_NAME VARCHAR(50) NOT NULL, SEQ_COUNT NUMERIC(38), PRIMARY KEY (SEQ_NAME))
INSERT INTO SEQUENCE(SEQ_NAME, SEQ_COUNT) values ('SEQ_GEN', 0)
