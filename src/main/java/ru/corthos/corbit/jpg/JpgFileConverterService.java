package ru.corthos.corbit.jpg;

public class JpgFileConverterService {
    /*


    public Path convertJpgToPdf(Path jpgFilePath) throws IOException, DocumentException {
        if (!Files.exists(jpgFilePath)) {
            throw new IllegalArgumentException("Файл не найден: " + jpgFilePath);
        }

        String pdfFileName = jpgFilePath.getFileName().toString().replaceFirst("\\.jpg$", "") + ".pdf";
        Path pdfFilePath = jpgFilePath.getParent().resolve(pdfFileName);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath.toFile()));
        document.open();

        Image image = Image.getInstance(jpgFilePath.toString());

        // Подгоняем картинку под размер страницы (иначе может "вылезать")
        image.scaleToFit(document.getPageSize().getWidth() - 50,
                         document.getPageSize().getHeight() - 50);
        image.setAlignment(Image.MIDDLE);

        document.add(image);
        document.close();

        return pdfFilePath;
    }




     */
}
