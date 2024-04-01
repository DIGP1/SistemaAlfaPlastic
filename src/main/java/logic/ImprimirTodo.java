package logic;
import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class ImprimirTodo implements Printable {

    private List<List<String>> data;
    private List<List<List<String>>> products;
    private List<String> NOrders;
    private String days;
    private String month;
    private String year;
    private int itn = 0;

    public ImprimirTodo(List<List<String>> data, List<List<List<String>>> products, List<String> NOrders, String days, String month, String year) {
        this.data = data;
        this.products = products;
        this.NOrders = NOrders;
        this.days = days;
        this.month = month;
        this.year = year;
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
    Graphics2D g2d = (Graphics2D) graphics;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    // Establecer el tamaño de la hoja
    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
    int lineHeight = g2d.getFontMetrics().getHeight();

    // Ajustar el interlineado
    int interlineado = 5;

    // Definir la posición inicial en el eje Y (3.6 cm convertido a puntos)
    double yPos = 82.37007874015748; // 3.6 cm en puntos

    // Calcular cuántas líneas caben en la página
    int linesPerPage = (int) ((pageFormat.getImageableHeight() - interlineado - yPos) / (lineHeight + interlineado));

    // Verificar si la página solicitada está dentro del rango total de páginas
    if (pageIndex < data.size()) {
        // Obtener las líneas de datos para esta página
        List<String> currentLines = data.get(pageIndex);

        // Imprimir las líneas en la posición adecuada
        for (int i = 0; i < currentLines.size(); i++) {
            String lineaActual = currentLines.get(i);
            if (i >= 5 && i < 23) {
                int j = i - 5;
                String can = products.get(pageIndex).get(j).get(0);
                String pn = products.get(pageIndex).get(j).get(1);
                String pu = products.get(pageIndex).get(j).get(2);
                String pt = products.get(pageIndex).get(j).get(3);
                g2d.drawString(can, 123, (int) yPos);
                g2d.drawString(pn, 150, (int) yPos);
                g2d.drawString(pu, 355, (int) yPos);
                g2d.drawString(pt, 419, (int) yPos);
            } else {
                switch (i) {
                    case 0 -> {
                        g2d.drawString(String.valueOf((pageIndex + 1)), 100, 70);
                        g2d.drawString(NOrders.get(pageIndex), 414, 70);
                        g2d.drawString(lineaActual, 110, (int) yPos);
                    }
                    case 1 -> {
                        g2d.setFont(new Font("Arial", Font.PLAIN, 7));
                        g2d.drawString(lineaActual, 110, (int) yPos);
                    }
                    case 2 -> {
                        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                        g2d.drawString(days, 370, 110);
                        g2d.drawString(month, 408, 110);
                        g2d.drawString(year, 446, 110);
                    }
                    case 23 -> {
                        g2d.setFont(new Font("Arial", Font.BOLD, 13));
                        g2d.drawString(lineaActual, 410, (int) yPos);
                    }
                    default -> {
                    }
                }
            }
            yPos += (lineHeight + interlineado);
        }

        return PAGE_EXISTS;
    } else {
        return NO_SUCH_PAGE;
    }
}

    // Método para configurar el tamaño de la hoja
    private PageFormat getPageFormat() {
        PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
        PageFormat customPageFormat = new PageFormat();

        // Configurar el tamaño de la página directamente al área imprimible
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
