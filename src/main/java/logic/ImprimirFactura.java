package logic;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;

public class ImprimirFactura implements Printable {
    private List<String> lineas;
    private List<List<String>> Product;
    private String nOrder;
    private String days;
    private String month;
    private String year;
    private String nOrderTemp;
    private BufferedImage facturaTemplate;

    public ImprimirFactura(List<String> lineas, List<List<String>> Product, String nOrder, String days, String month, String year, String nOrderTemp) {
        this.lineas = lineas;
        this.Product = Product;
        this.days = days;
        this.month = month;
        this.year = year;
        this.nOrderTemp = nOrderTemp;
        this.nOrder = nOrder;

        // Cargar imagen del diseño de factura desde resources
        try {
            InputStream is = getClass().getResourceAsStream("/factura_template_i2.png");
            if (is == null) throw new RuntimeException("No se encontró la imagen de la factura en resources");
            facturaTemplate = ImageIO.read(is);
        } catch (Exception ex) {
            ex.printStackTrace();
            facturaTemplate = null;
        }
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
        if (pageIndex > 0)
            return NO_SUCH_PAGE;

        Graphics2D g2d = (Graphics2D)graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

        double ancho = pageFormat.getImageableWidth();
        double alto_total = pageFormat.getImageableHeight();
        double alto = alto_total / 2; // Usamos solo media página

        // Dibuja la imagen (sin rotar, en la mitad superior)
        if (facturaTemplate != null) {
            g2d.drawImage(facturaTemplate, 0, 0, (int)ancho, (int)alto, null);
        }

        // Guarda el estado original antes de rotar
        AffineTransform old = g2d.getTransform();

        // Rota y traslada SOLO el área de la factura
        g2d.rotate(Math.toRadians(-90));
        g2d.translate(-alto, 0);

        // Ahora imprime todo el texto alineado con la factura horizontal:
        // Cliente
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString(lineas.get(0), 26, 75);
        // Dirección
        g2d.setFont(new Font("Arial", Font.PLAIN, 7));
        g2d.drawString(lineas.get(1), 24, 97);
        // Fecha
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString(this.days, 289, 100);
        g2d.drawString(this.month, 317, 100);
        g2d.drawString(this.year, 344, 100);
        // Número de orden temporal y orden real
        g2d.drawString(this.nOrderTemp, 25, 51);
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString(this.nOrder, 317, 51);

        // Tabla de productos
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        double yProductos = 174;
        for (int p = 0; p < Product.size(); p++) {
            List<String> prod = Product.get(p);
            String cantidad = prod.get(0);
            String descripcion = prod.get(1);
            String precioU = prod.get(2);
            String precioT = prod.get(3);
            g2d.drawString(cantidad, 35, (int)yProductos);
            g2d.drawString(descripcion, 58, (int)yProductos);
            g2d.drawString(precioU, 259, (int)yProductos);
            g2d.drawString(precioT, 312, (int)yProductos);
            yProductos += 19.7;
        }

        // Total (la última línea de lineas)
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString(lineas.get(lineas.size() - 1), 292, 535);

        // Restaura el contexto
        g2d.setTransform(old);

        return PAGE_EXISTS;
    }
}
