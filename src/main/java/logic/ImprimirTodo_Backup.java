package logic;
import java.awt.*;
import java.awt.print.*;
import java.util.List;

public class ImprimirTodo_Backup implements Printable {
  private List<List<String>> data;
  
  private List<List<List<String>>> products;
  
  private List<String> NOrders;
  
  private String days;
  
  private String month;
  
  private String year;
  
  private int itn = 0;
  
  public ImprimirTodo_Backup(List<List<String>> data, List<List<List<String>>> products, List<String> NOrders, String days, String month, String year) {
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
    if (job.printDialog())
      try {
        job.print();
      } catch (PrinterException e) {
        e.printStackTrace();
      }  
  }
  
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
    Graphics2D g2d = (Graphics2D)graphics;
    g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
    g2d.setFont(new Font("Arial", 0, 10));
    int lineHeight = g2d.getFontMetrics().getHeight();
    int interlineado = 5;
    double yPos = 84.37007874015748D;
    int linesPerPage = (int)((pageFormat.getImageableHeight() - interlineado - yPos) / (lineHeight + interlineado));
    if (pageIndex < this.data.size()) {
      List<String> currentLines = this.data.get(pageIndex);
      yPos = 174.37007874015748D;
      for (int i = 0; i < currentLines.size(); i++) {
        String lineaActual = currentLines.get(i);
        if (i >= 5 && i < 23) {
          int j = i - 5;
          System.out.println(yPos);
          String can = ((List<String>)((List<List<String>>)this.products.get(pageIndex)).get(j)).get(0);
          String pn = ((List<String>)((List<List<String>>)this.products.get(pageIndex)).get(j)).get(1);
          String pu = ((List<String>)((List<List<String>>)this.products.get(pageIndex)).get(j)).get(2);
          String pt = ((List<String>)((List<List<String>>)this.products.get(pageIndex)).get(j)).get(3);
          g2d.drawString(can, 130, (int)yPos);
          g2d.drawString(pn, 153, (int)yPos);
          g2d.drawString(pu, 363, (int)yPos);
          g2d.drawString(pt, 424, (int)yPos);
          yPos += 18.98D;
        } else {
          switch (i) {
            case 0:
              g2d.drawString(String.valueOf(pageIndex + 1), 110, 56);
              g2d.setFont(new Font("Arial", 1, 13));
              g2d.drawString(this.NOrders.get(pageIndex), 427, 56);
              g2d.setFont(new Font("Arial", 0, 10));
              g2d.drawString(lineaActual, 120, 77);
              break;
            case 1:
              g2d.setFont(new Font("Arial", 0, 7));
              g2d.drawString(lineaActual, 124, 97);
              break;
            case 2:
              g2d.setFont(new Font("Arial", 0, 10));
              g2d.drawString(this.days, 395, 100);
              g2d.drawString(this.month, 425, 100);
              g2d.drawString(this.year, 453, 100);
              break;
            case 23:
              g2d.setFont(new Font("Arial", 1, 13));
              g2d.drawString(lineaActual, 395, 525);
              break;
          } 
        } 
      } 
      return 0;
    } 
    return 1;
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