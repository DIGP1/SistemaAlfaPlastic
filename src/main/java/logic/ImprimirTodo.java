package logic;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;

public class ImprimirTodo implements Printable {
    private List<List<String>> data;
    private List<List<List<String>>> products;
    private List<String> NOrders;
    private String days, month, year;
    private BufferedImage facturaTemplate;

    public ImprimirTodo(List<List<String>> data, List<List<List<String>>> products, List<String> NOrders, String days, String month, String year) {
        this.data = data;
        this.products = products;
        this.NOrders = NOrders;
        this.days = days;
        this.month = month;
        this.year = year;

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
    int facturasPorPagina = 2;
    int totalFacturas = data.size();
    int facturasEnEstaPagina = Math.min(facturasPorPagina, totalFacturas - pageIndex * facturasPorPagina);

    if (facturasEnEstaPagina <= 0) {
        return NO_SUCH_PAGE;
    }

    Graphics2D g2d = (Graphics2D) graphics;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());

    double ancho = pageFormat.getImageableWidth();
    double alto = pageFormat.getImageableHeight() / facturasPorPagina;

    for (int i = 0; i < facturasEnEstaPagina; i++) {
        int facturaIndex = pageIndex * facturasPorPagina + i;
        double yOffset = i * alto;

        // Dibuja la imagen (sin rotar)
        if (facturaTemplate != null) {
            g2d.drawImage(facturaTemplate, 0, (int)yOffset, (int)ancho, (int)alto, null);
        }

        // Guarda el estado original antes de rotar
        AffineTransform old = g2d.getTransform();

        // Rota y traslada SOLO el área de la factura
        g2d.rotate(Math.toRadians(-90));
        g2d.translate(-(int)(yOffset + alto), 0);

        List<String> datos = data.get(facturaIndex);
        List<List<String>> productosFactura = products.get(facturaIndex);

        // Ahora las coordenadas X/Y vuelven a ser las mismas que en vertical original,
        // pero saldrán en la orientación correcta.

        // Cliente
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString(datos.get(0), 26, 75);
        // Dirección
        g2d.setFont(new Font("Arial", Font.PLAIN, 7));
        g2d.drawString(datos.get(1), 24, 97);
        // Fecha
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString(days, 289, 100);
        g2d.drawString(month, 317, 100);
        g2d.drawString(year, 344, 100);
        // Número de orden
        g2d.drawString(String.valueOf(facturaIndex + 1), 25, 51);
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString(NOrders.get(facturaIndex), 317, 51);
        

        // Tabla de productos
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        double yProductos = 174;
        for (int p = 0; p < productosFactura.size(); p++) {
            List<String> prod = productosFactura.get(p);
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

        // Total
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        g2d.drawString(datos.get(datos.size() - 1), 292, 535);

        // Restaura el contexto para la siguiente factura
        g2d.setTransform(old);
    }

    return PAGE_EXISTS;
    }
}