package com.spark.test.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ProductPdfService {

    public byte[] generateProductPdf(HttpServletResponse response) throws IOException;

}
