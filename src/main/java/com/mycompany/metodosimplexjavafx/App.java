package com.mycompany.metodosimplexjavafx;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Locale;

public class App extends Application {

    // Datos del problema
    TextField z1;
    TextField z2;
    TextField cantidad;

    VBox cajaRestricciones;

    TextField[] x1;
    TextField[] x2;
    TextField[] r;

    int numeroRestricciones = 3;

    // Cosas que vamos a mostrar
    TextArea tablaInicial;
    TextArea tablaFinal;
    TextArea resultados;

    Canvas grafica;

    // Matriz del Simplex
    double[][] matriz;

    String[] base;

    @Override
    public void start(Stage stage) {

        BorderPane ventana = new BorderPane();

        ventana.setPadding(new Insets(15));

        Label titulo =
                new Label("MÉTODO SIMPLEX");

        titulo.setFont(Font.font(25));
        titulo.setStyle("-fx-font-weight: bold;");

        /*
         * GRAFICA
         */
        grafica = new Canvas(850, 400);

        Label nombreGrafica =
                new Label("REGIÓN FACTIBLE");

        nombreGrafica.setFont(Font.font(18));
        nombreGrafica.setStyle("-fx-font-weight: bold;");

        VBox parteGrafica =
                new VBox(8, nombreGrafica, grafica);

        /*
         * TABLAS
         */
        tablaInicial = new TextArea();
        tablaFinal = new TextArea();

        tablaInicial.setEditable(false);
        tablaFinal.setEditable(false);

        tablaInicial.setFont(
                Font.font("Monospaced", 12));

        tablaFinal.setFont(
                Font.font("Monospaced", 12));

        Label inicio =
                new Label("TABLA INICIAL");

        Label fin =
                new Label("TABLA FINAL");

        inicio.setStyle("-fx-font-weight: bold;");
        fin.setStyle("-fx-font-weight: bold;");

        VBox tablas =
                new VBox(
                        5,
                        inicio,
                        tablaInicial,
                        fin,
                        tablaFinal);

        ScrollPane scrollTablas =
                new ScrollPane(tablas);

        scrollTablas.setFitToWidth(true);

        /*
         * DATOS
         */
        VBox datos = crearDatos();

        ScrollPane scrollDatos =
                new ScrollPane(datos);

        scrollDatos.setFitToWidth(true);

        /*
         * PARTE DE ABAJO
         */
        HBox abajo =
                new HBox(
                        15,
                        scrollTablas,
                        scrollDatos);

        scrollTablas.setPrefWidth(650);
        scrollDatos.setPrefWidth(400);

        VBox centro =
                new VBox(
                        15,
                        parteGrafica,
                        abajo);

        ScrollPane scrollPrincipal =
                new ScrollPane(centro);

        scrollPrincipal.setFitToWidth(true);

        ventana.setTop(titulo);
        ventana.setCenter(scrollPrincipal);

        Scene escena =
                new Scene(
                        ventana,
                        1200,
                        850);

        stage.setTitle("Método Simplex");
        stage.setScene(escena);
        stage.show();
    }

    // Datos que mete el usuario
    public VBox crearDatos() {

        VBox datos =
                new VBox(8);

        Label titulo =
                new Label("DATOS DEL PROBLEMA");

        titulo.setFont(Font.font(18));
        titulo.setStyle("-fx-font-weight: bold;");

        // Funcion Z
        HBox funcion =
                new HBox(5);

        z1 = new TextField("3");
        z2 = new TextField("2");

        z1.setPrefWidth(60);
        z2.setPrefWidth(60);

        funcion.getChildren().addAll(
                new Label("Max Z ="),
                z1,
                new Label("X1 +"),
                z2,
                new Label("X2"));

        // Cantidad de restricciones
        HBox cantidadBox =
                new HBox(5);

        cantidad =
                new TextField("3");

        cantidad.setPrefWidth(60);

        Button generar =
                new Button("Generar");

        generar.setOnAction(e ->
                generarRestricciones());

        cantidadBox.getChildren().addAll(
                new Label("Restricciones:"),
                cantidad,
                generar);

        cajaRestricciones =
                new VBox(5);

        generarRestricciones();

        Button resolver =
                new Button("GRAFICAR Y RESOLVER");

        resolver.setStyle(
                "-fx-font-weight: bold;");

        resolver.setOnAction(e ->
                resolver());

        resultados =
                new TextArea();

        resultados.setEditable(false);
        resultados.setPrefHeight(180);

        Label tituloResultados =
                new Label("RESULTADOS");

        tituloResultados.setStyle(
                "-fx-font-weight: bold;");

        datos.getChildren().addAll(
                titulo,
                funcion,
                cantidadBox,
                cajaRestricciones,
                resolver,
                tituloResultados,
                resultados);

        return datos;
    }

    // Generamos las filas
    public void generarRestricciones() {

        try {

            numeroRestricciones =
                    Integer.parseInt(
                            cantidad.getText());

        } catch (Exception e) {

            numeroRestricciones = 3;
        }

        if (numeroRestricciones < 1) {
            numeroRestricciones = 1;
        }

        if (numeroRestricciones > 8) {
            numeroRestricciones = 8;
        }

        cajaRestricciones
                .getChildren()
                .clear();

        x1 =
                new TextField[
                        numeroRestricciones];

        x2 =
                new TextField[
                        numeroRestricciones];

        r =
                new TextField[
                        numeroRestricciones];

        for (int i = 0;
                i < numeroRestricciones;
                i++) {

            x1[i] = new TextField();
            x2[i] = new TextField();
            r[i] = new TextField();

            x1[i].setPrefWidth(55);
            x2[i].setPrefWidth(55);
            r[i].setPrefWidth(55);

            // Ejemplo
            if (i == 0) {

                x1[i].setText("2");
                x2[i].setText("5");
                r[i].setText("35");
            }

            if (i == 1) {

                x1[i].setText("3");
                x2[i].setText("-2");
                r[i].setText("18");
            }

            if (i == 2) {

                x1[i].setText("2");
                x2[i].setText("4");
                r[i].setText("26");
            }

            HBox fila =
                    new HBox(5);

            fila.setAlignment(
                    Pos.CENTER_LEFT);

            fila.getChildren().addAll(
                    new Label(
                            "R" + (i + 1) + ":"),
                    x1[i],
                    new Label("X1 +"),
                    x2[i],
                    new Label("X2 <="),
                    r[i]);

            cajaRestricciones
                    .getChildren()
                    .add(fila);
        }
    }

    // Aqui hacemos todo
    public void resolver() {

        try {

            double[] funcionZ = {

                Double.parseDouble(
                        z1.getText()),

                Double.parseDouble(
                        z2.getText())
            };

            double[][] restricciones =
                    new double[
                            numeroRestricciones][2];

            double[] resultados =
                    new double[
                            numeroRestricciones];

            for (int i = 0;
                    i < numeroRestricciones;
                    i++) {

                restricciones[i][0] =
                        Double.parseDouble(
                                x1[i].getText());

                restricciones[i][1] =
                        Double.parseDouble(
                                x2[i].getText());

                resultados[i] =
                        Double.parseDouble(
                                r[i].getText());
            }

            crearMatriz(
                    funcionZ,
                    restricciones,
                    resultados);

            tablaInicial.setText(
                    hacerTabla());

            calcularSimplex();

            tablaFinal.setText(
                    hacerTabla());

            mostrarResultados();

            dibujarGrafica(
                    restricciones,
                    resultados);

        } catch (Exception e) {

            resultados.setText(
                    "Hay un error en los datos.\n"
                    + "Revisa que todos sean numeros.");
        }
    }

    // Armamos la tabla inicial
    public void crearMatriz(
            double[] funcionZ,
            double[][] restricciones,
            double[] resultados) {

        matriz =
                new double[
                        numeroRestricciones + 1]
                        [2 + numeroRestricciones + 2];

        base =
                new String[
                        numeroRestricciones + 1];

        int columnas =
                matriz[0].length;

        matriz[0][0] = 1;

        base[0] = "Z";

        // Z
        for (int i = 0;
                i < 2;
                i++) {

            matriz[0][i + 1] =
                    -funcionZ[i];
        }

        // Restricciones
        for (int fila = 1;
                fila <= numeroRestricciones;
                fila++) {

            matriz[fila][0] = 0;

            matriz[fila][1] =
                    restricciones[fila - 1][0];

            matriz[fila][2] =
                    restricciones[fila - 1][1];

            // Holgura
            matriz[fila][2 + fila] = 1;

            // R
            matriz[fila][columnas - 1] =
                    resultados[fila - 1];

            base[fila] =
                    "S" + fila;
        }
    }

    // Metodo simplex
    public void calcularSimplex() {

        int columnas =
                matriz[0].length;

        int filas =
                matriz.length;

        while (buscarNegativos()) {

            double menor = 0;

            int columnaPivote = 0;

            // Buscamos el mas negativo
            for (int i = 1;
                    i < columnas - 1;
                    i++) {

                if (matriz[0][i] < menor) {

                    menor =
                            matriz[0][i];

                    columnaPivote = i;
                }
            }

            double menorRazon =
                    Double.MAX_VALUE;

            int filaPivote = -1;

            // Buscamos la razon
            for (int i = 1;
                    i < filas;
                    i++) {

                if (matriz[i][columnaPivote] > 0) {

                    double razon =
                            matriz[i][columnas - 1]
                            / matriz[i][columnaPivote];

                    if (razon < menorRazon
                            && razon >= 0) {

                        menorRazon = razon;

                        filaPivote = i;
                    }
                }
            }

            // No se puede seguir
            if (filaPivote == -1) {
                break;
            }

            double pivote =
                    matriz[
                            filaPivote]
                            [columnaPivote];

            // Hacemos el pivote 1
            for (int j = 0;
                    j < columnas;
                    j++) {

                matriz[filaPivote][j]
                        /= pivote;
            }

            // Hacemos los demas 0
            for (int i = 0;
                    i < filas;
                    i++) {

                if (i != filaPivote) {

                    double factor =
                            matriz[i]
                            [columnaPivote];

                    for (int j = 0;
                            j < columnas;
                            j++) {

                        matriz[i][j] =
                                matriz[i][j]
                                - factor
                                * matriz[filaPivote][j];
                    }
                }
            }

            // Cambiamos la base
            if (columnaPivote >= 1
                    && columnaPivote <= 2) {

                base[filaPivote] =
                        "X" + columnaPivote;

            } else {

                base[filaPivote] =
                        "S"
                        + (columnaPivote - 2);
            }
        }
    }

    public boolean buscarNegativos() {

        // No revisamos R
        for (int i = 1;
                i < matriz[0].length - 1;
                i++) {

            if (matriz[0][i] < -0.000001) {
                return true;
            }
        }

        return false;
    }

    // Convertimos la matriz en una tabla
    public String hacerTabla() {

        String tabla = "";

        tabla += "Base\t";
        tabla += "X1\t";
        tabla += "X2\t";

        for (int i = 1;
                i <= numeroRestricciones;
                i++) {

            tabla += "S" + i + "\t";
        }

        tabla += "R\n";

        for (int i = 0;
                i < matriz.length;
                i++) {

            tabla += base[i] + "\t";

            for (int j = 1;
                    j < matriz[0].length;
                    j++) {

                double numero =
                        matriz[i][j];

                if (Math.abs(numero)
                        < 0.000001) {

                    numero = 0;
                }

                tabla +=
                        String.format(
                                Locale.US,
                                "%.4f",
                                numero);

                tabla += "\t";
            }

            tabla += "\n";
        }

        return tabla;
    }

    // Mostramos los resultados
    public void mostrarResultados() {

        double resultadoX1 =
                obtenerResultado("X1");

        double resultadoX2 =
                obtenerResultado("X2");

        double resultadoZ =
                matriz[0]
                [matriz[0].length - 1];

        resultados.setText(
                "X1 = "
                + formatearResultado(
                        resultadoX1)

                + "\n\nX2 = "
                + formatearResultado(
                        resultadoX2)

                + "\n\nZ = "
                + formatearResultado(
                        resultadoZ));
    }

    // Buscamos una variable en la base
    public double obtenerResultado(
            String variable) {

        for (int i = 1;
                i < matriz.length;
                i++) {

            if (base[i].equals(variable)) {

                return matriz[i]
                        [matriz[0].length - 1];
            }
        }

        return 0;
    }

    // Decimal y fraccion
    public String formatearResultado(
            double numero) {

        if (Math.abs(
                numero - Math.round(numero))
                < 0.000001) {

            numero =
                    Math.round(numero);
        }

        String decimal =
                String.format(
                        Locale.US,
                        "%.6f",
                        numero);

        // Quitamos ceros de mas
        while (decimal.contains(".")
                && decimal.endsWith("0")) {

            decimal =
                    decimal.substring(
                            0,
                            decimal.length() - 1);
        }

        if (decimal.endsWith(".")) {
            decimal += "0";
        }

        String fraccion =
                convertirFraccion(numero);

        return decimal
                + " = "
                + fraccion;
    }

    // Buscamos la fraccion
    public String convertirFraccion(
            double numero) {

        for (int denominador = 1;
                denominador <= 1000;
                denominador++) {

            double numerador =
                    numero * denominador;

            if (Math.abs(
                    numerador
                    - Math.round(numerador))
                    < 0.000001) {

                long num =
                        Math.round(numerador);

                long den =
                        denominador;

                long a =
                        Math.abs(num);

                long b =
                        den;

                while (b != 0) {

                    long aux = b;

                    b = a % b;

                    a = aux;
                }

                if (a != 0) {

                    num /= a;
                    den /= a;
                }

                return num + "/" + den;
            }
        }

        return "No encontrada";
    }

    // Dibujamos la grafica
    public void dibujarGrafica(
            double[][] restricciones,
            double[] valoresR) {

        GraphicsContext gc =
                grafica.getGraphicsContext2D();

        double ancho =
                grafica.getWidth();

        double alto =
                grafica.getHeight();

        gc.setFill(Color.WHITE);

        gc.fillRect(
                0,
                0,
                ancho,
                alto);

        double maxX = 10;
        double maxY = 10;

        // Buscamos un tamaño
        for (int i = 0;
                i < restricciones.length;
                i++) {

            double a =
                    restricciones[i][0];

            double b =
                    restricciones[i][1];

            double valor =
                    valoresR[i];

            if (a > 0) {

                maxX =
                        Math.max(
                                maxX,
                                valor / a);
            }

            if (b > 0) {

                maxY =
                        Math.max(
                                maxY,
                                valor / b);
            }
        }

        maxX *= 1.2;
        maxY *= 1.2;

        double izquierda = 55;
        double arriba = 20;
        double derecha = ancho - 25;
        double abajo = alto - 45;

        double escalaX =
                (derecha - izquierda)
                / maxX;

        double escalaY =
                (abajo - arriba)
                / maxY;

        // Cuadricula
        gc.setStroke(
                Color.LIGHTGRAY);

        gc.setLineWidth(1);

        for (int i = 0;
                i <= (int) maxX;
                i++) {

            double px =
                    izquierda
                    + i * escalaX;

            gc.strokeLine(
                    px,
                    arriba,
                    px,
                    abajo);

            gc.setFill(Color.BLACK);

            gc.fillText(
                    "" + i,
                    px - 3,
                    abajo + 15);
        }

        for (int i = 0;
                i <= (int) maxY;
                i++) {

            double py =
                    abajo
                    - i * escalaY;

            gc.strokeLine(
                    izquierda,
                    py,
                    derecha,
                    py);

            gc.setFill(Color.BLACK);

            gc.fillText(
                    "" + i,
                    izquierda - 18,
                    py + 4);
        }

        // Pintamos la region factible
        gc.setFill(
                Color.rgb(
                        60,
                        150,
                        220,
                        0.25));

        for (int px =
                (int) izquierda;
                px < derecha;
                px += 3) {

            for (int py =
                    (int) arriba;
                    py < abajo;
                    py += 3) {

                double X =
                        (px - izquierda)
                        / escalaX;

                double Y =
                        (abajo - py)
                        / escalaY;

                boolean factible =
                        X >= 0 && Y >= 0;

                for (int i = 0;
                        i < restricciones.length;
                        i++) {

                    double valor =
                            restricciones[i][0] * X
                            + restricciones[i][1] * Y;

                    if (valor >
                            valoresR[i]
                            + 0.000001) {

                        factible = false;
                        break;
                    }
                }

                if (factible) {

                    gc.fillRect(
                            px,
                            py,
                            3,
                            3);
                }
            }
        }

        // Colores de restricciones
        Color[] colores = {

            Color.RED,
            Color.ORANGE,
            Color.GREEN,
            Color.PURPLE,
            Color.BROWN
        };

        // Dibujamos las lineas
        for (int i = 0;
                i < restricciones.length;
                i++) {

            double a =
                    restricciones[i][0];

            double b =
                    restricciones[i][1];

            double valor =
                    valoresR[i];

            gc.setStroke(
                    colores[
                            i % colores.length]);

            gc.setLineWidth(2);

            if (Math.abs(b)
                    < 0.000001) {

                if (Math.abs(a)
                        > 0.000001) {

                    double X =
                            valor / a;

                    double px =
                            izquierda
                            + X * escalaX;

                    gc.strokeLine(
                            px,
                            arriba,
                            px,
                            abajo);
                }

            } else {

                double Y1 =
                        valor / b;

                double Y2 =
                        (valor
                        - a * maxX) / b;

                double px1 =
                        izquierda;

                double py1 =
                        abajo
                        - Y1 * escalaY;

                double px2 =
                        derecha;

                double py2 =
                        abajo
                        - Y2 * escalaY;

                gc.strokeLine(
                        px1,
                        py1,
                        px2,
                        py2);
            }

            gc.setFill(
                    colores[
                            i % colores.length]);

            gc.fillText(
                    "R" + (i + 1),
                    izquierda + 10,
                    arriba + 20 + i * 18);
        }

        // Ejes
        gc.setStroke(Color.BLACK);

        gc.setLineWidth(2);

        gc.strokeLine(
                izquierda,
                abajo,
                derecha,
                abajo);

        gc.strokeLine(
                izquierda,
                arriba,
                izquierda,
                abajo);

        gc.setFill(Color.BLACK);

        gc.fillText(
                "X1",
                derecha - 15,
                abajo + 30);

        gc.fillText(
                "X2",
                izquierda - 30,
                arriba + 10);

        // Punto optimo
        double Xopt =
                obtenerResultado("X1");

        double Yopt =
                obtenerResultado("X2");

        double pxOpt =
                izquierda
                + Xopt * escalaX;

        double pyOpt =
                abajo
                - Yopt * escalaY;

        gc.setFill(Color.RED);

        gc.fillOval(
                pxOpt - 6,
                pyOpt - 6,
                12,
                12);

        gc.setFill(Color.BLACK);

        gc.fillText(
                "Óptimo ("
                + decimalGrafica(Xopt)
                + ", "
                + decimalGrafica(Yopt)
                + ")",
                pxOpt + 10,
                pyOpt - 10);
    }

    public String decimalGrafica(
            double numero) {

        if (Math.abs(
                numero - Math.round(numero))
                < 0.000001) {

            return "" +
                    Math.round(numero);
        }

        return String.format(
                Locale.US,
                "%.2f",
                numero);
    }

    public static void main(
            String[] args) {

        launch(args);
    }
}