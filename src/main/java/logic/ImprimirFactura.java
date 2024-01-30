/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logic;

import java.awt.*;
import java.awt.print.*;
import java.util.List;

/**
 *
 * @author Diego
 */
public class ImprimirFactura implements Printable {

    private List<String> lineas;
    private List<List<String>>Product;
    private String nOrder ;
    private String days;
    private String month;
    private String year;
    private String nOrderTemp;

    public ImprimirFactura(List<String> lineas, List<List<String>>Product, String nOrder, String days, String month, String year,String nOrderTemp) {
        this.lineas = lineas;
        this.Product = Product;
        this.days = days;
        this.month = month;
        this.year = year;
        this.nOrderTemp = nOrderTemp;
        this.nOrder = nOrder;
    }

    public void imprimir() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(this);

        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Establecer el tamaño de la hoja
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        int lineHeight = g2d.getFontMetrics().getHeight();

        // Ajustar el interlineado
        int interlineado = 5;

        // Definir la posición inicial en el eje Y (3.6 cm convertido a puntos)
        double yPos = 90.37007874015748; // 3.6 cm en puntos

        // Calcular cuántas líneas caben en la página
        int linesPerPage = (int) ((pageFormat.getImageableHeight() - interlineado - yPos) / (lineHeight + interlineado));

        // Imprimir las líneas en la posición adecuada
        int startIndex = linesPerPage * pageIndex;
        int endIndex = Math.min(startIndex + linesPerPage, lineas.size());

        for (int i = startIndex; i < endIndex; i++) {
            String lineaActual = lineas.get(i);
            if(i>4 && i<22){
                int j = i-4;
                String can = Product.get(j).get(0);
                String pn = Product.get(j).get(1);
                String pu = Product.get(j).get(2);
                String pt = Product.get(j).get(3);
                
                g2d.drawString(can, 116, (int) yPos);
                g2d.drawString(pn, 144, (int) yPos);
                g2d.drawString(pu, 350, (int) yPos);
                g2d.drawString(pt, 414, (int) yPos);
                
            }else{
                switch (i) {
                    case 1 -> {
                        g2d.setFont(new Font("Arial", Font.PLAIN, 7));
                        g2d.drawString(lineaActual, 100, (int) yPos);
                        System.out.println(yPos);
                    }
                    case 2 -> {
                        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                            g2d.drawString(days, 363, 113);
                            g2d.drawString(month, 401, 113);
                            g2d.drawString(year, 438, 113);
                    }
                    default -> {
                        if(i == 23){
                            g2d.setFont(new Font("Arial", Font.BOLD, 13));
                            g2d.drawString(lineaActual, 410, (int) yPos);
                        }else{
                            if(i == 0){
                                System.out.println(nOrder);
                                g2d.drawString(nOrderTemp, 100, 73);
                                g2d.drawString(nOrder, 414, 73);
                                g2d.drawString(lineaActual, 100, (int) yPos);
                            }
                            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                            g2d.drawString(lineaActual, 100, (int) yPos);
                        }
                    }
                }
            }
            yPos += (lineHeight + interlineado);
        }

        return PAGE_EXISTS;
    }

    // Método para configurar el tamaño de la hoja
    private PageFormat getPageFormat() {
        PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
        PageFormat customPageFormat = new PageFormat();

        // Configurar el tamaño de la página directamente al área imprimible
        // Puedes ajustar width y height según tus necesidades
        customPageFormat.setPaper(new CustomPaper(138, 210));

        return customPageFormat;
    }

    // Clase para personalizar el tamaño de la página directamente al área imprimible
    private static class CustomPaper extends Paper {
        private double width;
        private double height;

        public CustomPaper(double width, double height) {
            this.width = width;
            this.height = height;
        }

        @Override
        public double getWidth() {
            return width;
        }

        @Override
        public double getHeight() {
            return height;
        }

        @Override
        public double getImageableX() {
            return 0;
        }

        @Override
        public double getImageableY() {
            return 0;
        }

        @Override
        public double getImageableWidth() {
            return width;
        }

        @Override
        public double getImageableHeight() {
            return height;
        }
    }
}
