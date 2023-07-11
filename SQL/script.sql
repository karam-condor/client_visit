drop table visclifoto;
drop table visclivisita;
drop table viscliusur;



create table viscliusur
(
    codusur number(4,0),
    dtinclusao date,
    token varchar2(32),
    primary key(codusur)
);

-----------------------------------------------

create table visclivisita
(
    id number(6,0),
    codusur number(4,0),
    dtinclusao date,
    dttermino date,
    cnpj varchar2(18),
    codcli number(6,0),
    email varchar2(100),
    telefone varchar2(13),
    representante varchar2(100),
    obs VARCHAR2(200),
    latitude number(22,9),
    longitude number(22,9),
    primary key(id),
    FOREIGN key(codusur) REFERENCES viscliusur(codusur)    
);


-----------------------------------------------

create table visclifoto
(
    id number(8,0),
    id_visita number(6,0),
    url varchar2(300),
    seq number(2,0),
    descricao VARCHAR2(100),
    primary key(id),
    foreign key(ID_VISITA) references visclivisita(id)
);


-----------------------------------------------


CREATE SEQUENCE seq_visclivisita
    START WITH 1
    INCREMENT BY 1;    

-----------------------------------------------


CREATE SEQUENCE seq_visclifoto
    START WITH 1
    INCREMENT BY 1;

