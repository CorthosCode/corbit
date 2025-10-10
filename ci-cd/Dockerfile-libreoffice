FROM debian:bookworm-slim

# Устанавливаем только LibreOffice Writer + шрифты
RUN apt-get update && apt-get install -y --no-install-recommends \
      libreoffice-writer \
      fonts-dejavu \
      fonts-liberation \
      ghostscript \
      unzip \
      && apt-get clean \
      && rm -rf /var/lib/apt/lists/*

# Рабочая директория (куда будем монтировать файлы)
WORKDIR /data

# LibreOffice headless для конвертации DOCX → PDF
ENTRYPOINT ["libreoffice", "--headless", "--convert-to", "pdf", "--outdir", "/data"]
