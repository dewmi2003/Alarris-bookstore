package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.Order;
import com.bookstore.entity.OrderItem;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.util.stream.Stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PdfService {

    public ByteArrayInputStream generateOrderPdf(Order order) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Paragraph para = new Paragraph("Order Confirmation", fontHeader);
            para.setAlignment(Element.ALIGN_CENTER);
            document.add(para);
            document.add(Chunk.NEWLINE);

            Font fontPara = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.add(new Paragraph("Order ID: #" + order.getId(), fontPara));
            document.add(new Paragraph("Date: " + order.getOrderDate(), fontPara));
            document.add(new Paragraph("Status: " + order.getStatus(), fontPara));
            document.add(new Paragraph("Total: LKR " + order.getTotalAmount(), fontPara));

            if (order.getUser() != null) {
                document.add(new Paragraph("Customer: " + order.getUser().getFullName(), fontPara));
                document.add(new Paragraph("Email: " + order.getUser().getEmail(), fontPara));
                if (order.getUser().getAddress() != null) {
                    document.add(new Paragraph("Shipping Address: " + order.getUser().getAddress(), fontPara));
                }
            }

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new int[] { 4, 2, 2, 2 });

            Stream.of("Book Title", "Quantity", "Price", "Subtotal")
                    .forEach(headerTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
                        header.setBorderWidth(1);
                        header.setPhrase(new Phrase(headerTitle));
                        table.addCell(header);
                    });

            for (OrderItem item : order.getItems()) {
                table.addCell(item.getBook().getTitle());
                table.addCell(String.valueOf(item.getQuantity()));
                table.addCell("LKR " + item.getPrice());
                table.addCell("LKR " + (item.getPrice() * item.getQuantity()));
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
