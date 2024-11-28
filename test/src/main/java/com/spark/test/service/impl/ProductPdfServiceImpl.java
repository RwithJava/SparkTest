package com.spark.test.service.impl;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.spark.test.entity.Product;
import com.spark.test.exception.TestApiException;
import com.spark.test.repository.ProductRepository;
import com.spark.test.service.ProductPdfService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class ProductPdfServiceImpl implements ProductPdfService {

    private final ProductRepository productRepository;

    public ProductPdfServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private static Table getTable(List<Product> products) {

        log.info("Creating table for PDF with {} products.", products.size());

        Table table = new Table(6);
        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Description");
        table.addCell("Price");
        table.addCell("Quantity");
        table.addCell("Revenue");

        for (Product product : products) {
            table.addCell(String.valueOf(product.getId()));
            table.addCell(product.getName());
            table.addCell(product.getDescription());
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell(String.valueOf(product.getQuantity()));
            table.addCell(String.valueOf(product.getRevenue()));
        }

        log.info("Table creation completed.");
        return table;
    }

    @Override
    public byte[] generateProductPdf(HttpServletResponse response) throws IOException {
        log.info("Starting PDF generation.");

        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        log.info("Fetching product data from the repository.");
        List<Product> products = productRepository.findAll();
        log.info("Fetched {} products from the database.", products.size());

        try {
            PdfWriter writer = new PdfWriter(pdfOutputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            log.info("Setting up PDF font.");
            PdfFont font = PdfFontFactory.createFont(StandardFonts.COURIER_BOLD);

            log.info("Adding title to the PDF.");
            document.add(new Paragraph(new Text("Product List").setFont(font).setFontSize(16)));

            log.info("Adding product table to the PDF.");
            Table table = getTable(products);
            document.add(table);
            log.info("Finalizing the PDF document.");
            document.close();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=product_list.pdf");
            log.info("PDF generation completed successfully.");
        } catch (IOException e) {
            log.error("Error occurred during PDF generation: {}", e.getMessage());
            throw new TestApiException(HttpStatus.EXPECTATION_FAILED, "PDF generation failed.");
        }

        return pdfOutputStream.toByteArray();
    }
}
