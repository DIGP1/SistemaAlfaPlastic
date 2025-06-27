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
public class ImprimirFacturaBackup implements Printable {
  private List<String> lineas;
  
  private List<List<String>> Product;
  
  private String nOrder;
  
  private String days;
  
  private String month;
  
  private String year;
  
  private String nOrderTemp;
  
  public ImprimirFacturaBackup(List<String> lineas, List<List<String>> Product, String nOrder, String days, String month, String year, String nOrderTemp) {
    this.lineas = lineas;
    this.Product = Product;
    this.days = days;
    this.month = month;
    this.year = year;
    this.nOrderTemp = nOrderTemp;
    this.nOrder = nOrder;
  }
  
  private static String quitarCerosNoSignificativos(float numero) {
    String cadena = String.valueOf(numero);
    cadena = cadena.replaceAll("\\.?0*$", "");
    return cadena;
  }
  
  public void imprimir() {
    PrinterJob job = PrinterJob.getPrinterJob();
    job.setPrintable(this);
    if (job.printDialog())
      try {
        job.print();
      } catch (PrinterException e) {
        e.printStackTrace();
      }  
  }
  
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
    if (pageIndex > 0)
      return 1; 
    Graphics2D g2d = (Graphics2D)graphics;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    g2d.setFont(new Font("Arial", 0, 10));
    int lineHeight = g2d.getFontMetrics().getHeight();
    int interlineado = 5;
    double yPos = 97.37007874015748D;
    int linesPerPage = (int)((pageFormat.getImageableHeight() - interlineado - yPos) / (lineHeight + interlineado));
    int startIndex = linesPerPage * pageIndex;
    int endIndex = Math.min(startIndex + linesPerPage, this.lineas.size());
    yPos = 187.37007874015748D;
    for (int i = startIndex; i < endIndex; i++) {
      String lineaActual = this.lineas.get(i);
      if (i >= 5 && i < 23) {
        int j = i - 5;
        System.out.println(yPos);
        String can = ((List<String>)this.Product.get(j)).get(0);
        String pn = ((List<String>)this.Product.get(j)).get(1);
        String pu = ((List<String>)this.Product.get(j)).get(2);
        String pt = ((List<String>)this.Product.get(j)).get(3);
        g2d.drawString(can, 130, (int)yPos);
        g2d.drawString(pn, 153, (int)yPos);
        g2d.drawString(pu, 363, (int)yPos);
        g2d.drawString(pt, 424, (int)yPos);
        yPos += 18.98D;
      } else {
        switch (i) {
          case 0:
            g2d.drawString(this.nOrderTemp, 110, 70);
            g2d.setFont(new Font("Arial", 1, 13));
            g2d.drawString(this.nOrder, 427, 70);
            g2d.setFont(new Font("Arial", 0, 10));
            g2d.drawString(lineaActual, 120, 91);
            break;
          case 1:
            g2d.setFont(new Font("Arial", 0, 7));
            g2d.drawString(lineaActual, 124, 111);
            break;
          case 2:
            g2d.setFont(new Font("Arial", 0, 10));
            g2d.drawString(this.days, 395, 116);
            g2d.drawString(this.month, 425, 116);
            g2d.drawString(this.year, 453, 116);
            break;
          case 23:
            g2d.setFont(new Font("Arial", 1, 13));
            g2d.drawString(lineaActual, 388, 538);
            break;
        } 
      } 
    } 
    return 0;
  }
  
  private PageFormat getPageFormat() {
    PageFormat pageFormat = PrinterJob.getPrinterJob().defaultPage();
    PageFormat customPageFormat = new PageFormat();
    customPageFormat.setPaper(new CustomPaper(138.0D, 210.0D));
    return customPageFormat;
  }
  
  private static class CustomPaper extends Paper {
    private double width;
    
    private double height;
    
    public CustomPaper(double width, double height) {
      this.width = width;
      this.height = height;
    }
    
    public double getWidth() {
      return this.width;
    }
    
    public double getHeight() {
      return this.height;
    }
    
    public double getImageableX() {
      return 0.0D;
    }
    
    public double getImageableY() {
      return 0.0D;
    }
    
    public double getImageableWidth() {
      return this.width;
    }
    
    public double getImageableHeight() {
      return this.height;
    }
  }
}
