package com.example.tiac_tac;

public class Ip {
    public static final String BASE_URL = "http://192.160.161.237:80/Tic_tac/"; // Canvia per la teva IP

    // Si necessites afegir endpoints específics
    public static final String LOGIN_URL = BASE_URL + "usuaris/login.php";

    public static final String PERFIL_URL = BASE_URL + "usuaris/perfil.php";

    public static final String FITXAR_URL = BASE_URL + "usuaris/fitxar.php";

    public static final String INCIDENCIA_AFEGIR_URL = BASE_URL + "usuaris/incidencia_afegir.php";

    public static final String INCIDENCIA_MOSTRAR_URL = BASE_URL + "usuaris/incidencia_mostrar.php";

    public static final String HORARI_URL = BASE_URL + "usuaris/horari.php";
}