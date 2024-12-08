package com.example.controller;

import com.example.models.Client;
import com.example.repository.ClientRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
}
