import React from "react";

export default function ResultCard({ blob }) {
  const downloadUrl = URL.createObjectURL(blob);

  return (
    <div className="text-center">
      <p className="text-green-600 font-medium mb-4">✅ Файл успешно конвертирован!</p>
      <a
        href={downloadUrl}
        download="converted.pdf"
        className="bg-green-500 text-white px-6 py-2 rounded-lg font-semibold hover:bg-green-600 transition"
      >
        Скачать PDF
      </a>
    </div>
  );
}
