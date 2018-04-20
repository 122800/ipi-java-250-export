package com.example.demo.controller;

import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.FactureDTO;
import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;
import com.example.demo.service.export.ExportCSVService;
import com.example.demo.service.export.ExportPDFTextService;
import com.example.demo.service.export.ExportXLSXService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controlleur pour réaliser les exports
 */
@Controller
@RequestMapping(value="/")
public class ExportController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ExportCSVService exportCSVService;

    @Autowired
    private FactureService factureService;

    @Autowired
    private ExportXLSXService exportXLSXService;

    @Autowired
    private ExportPDFTextService exportPDFTextService;

    @GetMapping(value="/clients/csv", produces="text/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        List<ClientDTO> clients = clientService.findAllClients();
        exportCSVService.export(response.getWriter(), clients);
    }

    @GetMapping(value="/clients/xlsx", produces="application/vnd.ms-excel")
    public void clientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");
        List<ClientDTO> clients = clientService.findAllClients();
        exportXLSXService.export(response.getOutputStream(), clients);
    }

    @GetMapping(value="/clients/{id}/factures/xlsx", produces="application/vnd.ms-excel")
    public void facturesDUnClient(@PathVariable("id") Long clientId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures client " + clientId + ".xlsx\"");
        ClientDTO client = clientService.findById(clientId);
        List<FactureDTO> hello = factureService.findByClient(clientId);
        exportXLSXService.exportfacturesDUnClient(response.getOutputStream(), client);
    }


    @GetMapping(value="/factures/{id}/pdf", produces="application/pdf")
    public void facturePDF(@PathVariable("id") Long factureId, HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException {
        //response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"facture " + factureId + ".pdf\"");
        FactureDTO facture = factureService.findById(factureId);
        exportPDFTextService.export(response.getOutputStream(), facture);
    }

}
