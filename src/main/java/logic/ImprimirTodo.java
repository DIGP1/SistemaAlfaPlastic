import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class ImprimirTodo implements Printable {

    private List<List<Object>> data;

    public ImprimirTodo(List<List<Object>> data) {
        this.data = data;
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
        if (pageIndex >= data.size()) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        // Configurar la fuente y otros atributos de formato
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        int lineHeight = g2d.getFontMetrics().getHeight();

        // Configurar interlineado
        int interlineado = 5;

        // Definir la posición inicial en el eje Y (3.6 cm convertido a puntos)
        double yPos = 90.37007874015748; // 3.6 cm en puntos

        // Obtener los datos para la página actual
        List<Object> currentPageData = data.get(pageIndex);

        // Imprimir los datos en la posición adecuada
        for (int i = 0; i < currentPageData.size(); i++) {
            Object item = currentPageData.get(i);
            if (item instanceof String) {
                String texto = (String) item;
                // Imprimir texto en la posición adecuada
                g2d.drawString(texto, 100, (int) yPos);
            } else if (item instanceof List<?>) {
                List<String> productos = (List<String>) item;
                // Imprimir productos en la posición adecuada
                for (int j = 0; j < productos.size(); j++) {
                    String producto = productos.get(j);
                    g2d.drawString(producto, 100, (int) yPos + (j * lineHeight));
                }
            } else {
                // Otros tipos de datos
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
