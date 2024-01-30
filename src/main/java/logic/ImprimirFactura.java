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

    public ImprimirFactura(List<String> lineas) {
        this.lineas = lineas;
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

    // Establecer la fuente y otros atributos de formato
    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
    int lineHeight = g2d.getFontMetrics().getHeight();
    
    // Calcular cuántas líneas caben en la página
    int linesPerPage = (int) (pageFormat.getImageableHeight() / lineHeight);

    // Imprimir las líneas en la posición adecuada
    int startIndex = linesPerPage * pageIndex;
    int endIndex = Math.min(startIndex + linesPerPage, lineas.size());

    for (int i = startIndex; i < endIndex; i++) {
        String lineaActual = lineas.get(i);
        g2d.drawString(lineaActual, 100, 100 + (i - startIndex) * lineHeight);
    }

    return PAGE_EXISTS;
}
}
