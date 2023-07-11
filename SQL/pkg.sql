CREATE OR REPLACE PACKAGE PKG_VISCLI IS
    PROCEDURE LOGIN(
        PUSUARIO IN VARCHAR2,
        PSENHA IN VARCHAR2,
        PTOKEN IN VARCHAR2,
        PCODUSUR OUT VARCHAR2
    );
END PKG_VISCLI;
 -----------------------------------------------------
 CREATE     OR REPLACE PACKAGE BODY PKG_VISCLI IS
    PROCEDURE LOGIN(
        PUSUARIO IN VARCHAR2,
        PSENHA IN VARCHAR2,
        PTOKEN IN VARCHAR2,
        PCODUSUR OUT VARCHAR2
    )IS
        VCODUSUR   NUMBER(4.0);
VUSUARIO   VARCHAR2(15);
VSENHA     VARCHAR2(60);
VMATRICULA NUMBER(8, 0);
ISAUTH     NUMBER(1, 0);
    BEGIN
        PCODUSUR := '0';
        BEGIN
            SELECT
                PCEMPR.CODUSUR,
                DECRYPT(PCEMPR.SENHABD,
                PCEMPR.USUARIOBD),
                PCEMPR.USUARIOBD,
                PCEMPR.MATRICULA INTO VCODUSUR,
                VSENHA,
                VUSUARIO,
                VMATRICULA
            FROM
                PCEMPR@WINT PCEMPR
            WHERE
                PCEMPR.USUARIOBD = PUSUARIO;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                VMATRICULA := 0;
        END;
        IF VMATRICULA > 0 THEN
            IF UPPER(PUSUARIO) = UPPER(VUSUARIO) AND UPPER(PSENHA) = UPPER(VSENHA) THEN
                SELECT
                    COUNT(*) INTO ISAUTH
                FROM
                    KAPERMISSIONUSUR
                WHERE
                    KAPERMISSIONUSUR.CODAPP = 12
                    AND KAPERMISSIONUSUR.CODPERMISSAO = 1
                    and KAPERMISSIONUSUR.MATRICULA = VMATRICULA
                    AND ROWNUM = 1;
                IF ISAUTH > 0 THEN
                    BEGIN
                        MERGE INTO VISCLIUSUR USING DUAL ON(VISCLIUSUR.CODUSUR = VCODUSUR) WHEN NOT MATCHED THEN INSERT (VISCLIUSUR.CODUSUR, VISCLIUSUR.DTINCLUSAO, VISCLIUSUR.TOKEN) VALUES (VCODUSUR, TRUNC(SYSDATE), PTOKEN) WHEN MATCHED THEN UPDATE SET VISCLIUSUR.TOKEN = PTOKEN;
                        PCODUSUR := TO_CHAR(VCODUSUR);                        
                    EXCEPTION
                        WHEN OTHERS THEN
                            NULL;
                    END;
                END IF;
            END IF;
        END IF;
    END;
END PKG_VISCLI;