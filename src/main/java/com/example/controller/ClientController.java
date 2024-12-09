package com.example.controller;

import com.example.models.Client;
import com.example.models.Product;
import com.example.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepository repository;

    public ClientController(ClientRepository repository) {
        this.repository = repository;
    }
    /* La url es por GET : http://localhost:8080/clients  */
    @GetMapping
    public String findAll(Model model) {
        List<Client> clientes = this.repository.findAll();
        model.addAttribute("clientes", clientes);
        return "clientes/client-list";
    }
    @GetMapping("/new")
    public String getForm(Model model){
        model.addAttribute("cliente", new Client());
        return "clientes/client-form";
    }
    @PostMapping("/new")
    public String save(@ModelAttribute("cliente") Client cliente){
        this.repository.save(cliente);
        return "redirect:/clients";
    }
    @GetMapping("/{id}/view")
    public String verCliente(@PathVariable String id, Model model) {
        Client cliente = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/client-view";
    }
    @GetMapping("/{id}/edit")
    public String editarCliente(@PathVariable String id, Model model) {
        Client cliente = this.repository.findById(id).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        model.addAttribute("cliente", cliente);
        return "clientes/client-edit";
    }
    @PostMapping("/{id}/edit")
    public String saveFromEdit(@ModelAttribute("cliente") Client cliente){
        this.repository.save(cliente);
        return "redirect:/clients";
    }
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCliente(@PathVariable String id) {
        this.repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<Void> deleteAllClientes() {
        this.repository.deleteAll();
        return ResponseEntity.noContent().build();
    }
    //Métodos para generar el pdf
    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePDF() throws IOException, DocumentException {
        List<Client> clients = repository.findAll();

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
        addRows(table, clients, textColor);

        document.add(table);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "productos.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private void addTableHeader(PdfPTable table, BaseColor headerColor, BaseColor textColor) {
        Stream.of("Nombre", "DNI", "Fecha de Nacimiento", "Sexo")
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

    private void addRows(PdfPTable table, List<Client> clients, BaseColor textColor) {
        Font cellFont = FontFactory.getFont(FontFactory.HELVETICA, 11, textColor);
        for (Client client : clients) {
            addCell(table, client.getNombre(), cellFont);
            addCell(table, client.getDni(), cellFont);
            addCell(table, client.getFechaNacimiento().toString(), cellFont);
            addCell(table, client.getSexo(), cellFont);
        }
    }

    private void addCell(PdfPTable table, String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setBorderColor(BaseColor.DARK_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }
}
