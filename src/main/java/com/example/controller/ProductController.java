package com.example.controller;

import com.example.models.Product;
import com.example.repository.ProductRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    /* La url es por GET : http://localhost:8080/products  */
    @GetMapping
    public String findAll(Model model) {
        List<Product> products = this.repository.findAll();
        model.addAttribute("products", products);
        return "productos/product-list";
    }
    /*
    GET http://localhost:8080/products/new
     */
    @GetMapping("/new")
    public String getForm(Model model){
        model.addAttribute("product", new Product());
        return "productos/product-form";
    }

    /*
    POST http://localhost:8080/products
     */
    @PostMapping("/new")
    public String save(@ModelAttribute("product") Product product){
        this.repository.save(product);
        return "redirect:/products";
    }
    @GetMapping("/{id}/view")
    public String verProducto(@PathVariable String id, Model model) {
        Product product = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "productos/product-view";
    }
    @GetMapping("/{id}/edit")
    public String editarProducto(@PathVariable String id, Model model) {
        Product product = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("product", product);
        return "productos/product-edit";
    }
    @PostMapping("/{id}/edit")
    public String saveFromEdit(@ModelAttribute("product") Product product){
        this.repository.save(product);
        return "redirect:/products";
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        this.repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllProducts() {
        this.repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
    //Métodos para generar pdf de productos
    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePDF() throws IOException, DocumentException {
        List<Product> products = repository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, baos);
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        document.open();

        // Configurar colores para modo oscuro
        BaseColor backgroundColor = new BaseColor(44, 48, 52); // #2c3034
        BaseColor textColor = new BaseColor(224, 224, 224); // #e0e0e0
        BaseColor headerColor = new BaseColor(0, 123, 255); // #007bff

        // Establecer color de fondo
        PdfContentByte canvas = writer.getDirectContentUnder();
        canvas.setColorFill(backgroundColor);
        canvas.rectangle(0, 0, PageSize.A4.getWidth(), PageSize.A4.getHeight());
        canvas.fill();

        // Título
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, textColor);
        Paragraph title = new Paragraph("Lista de Productos", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        // Tabla
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(20);

        addTableHeader(table, headerColor, textColor);
        addRows(table, products, textColor);

        document.add(table);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "productos.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private void addTableHeader(PdfPTable table, BaseColor headerColor, BaseColor textColor) {
        Stream.of("Título", "Cantidad", "Precio", "ID")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(headerColor);
                    header.setBorderWidth(2);
                    header.setPadding(8);
                    Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, textColor);
                    header.setPhrase(new Phrase(columnTitle, headerFont));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<Product> products, BaseColor textColor) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, textColor);
        for (Product product : products) {
            addCell(table, product.getTitle(), cellFont);
            addCell(table, String.valueOf(product.getQuantity()), cellFont);
            addCell(table, product.getPrice() + " €", cellFont);
            addCell(table, product.getId(), cellFont);
        }
    }

    private void addCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorderColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
