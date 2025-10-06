import React, { useState } from "react";

export default function FileUpload({ onUpload }) {
  const [file, setFile] = useState(null);

  const handleFileChange = (e) => setFile(e.target.files[0]);
  const handleSubmit = (e) => {
    e.preventDefault();
    if (file) onUpload(file);
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="flex flex-col items-center justify-center border-2 border-dashed border-indigo-400 rounded-xl p-6 hover:bg-indigo-50 transition"
    >
      <input
        type="file"
        id="fileInput"
        onChange={handleFileChange}
        className="hidden"
        accept=".txt,.doc,.docx,.odt,.xls,.xlsx,.ppt,.pptx,.rtf"
      />
      <label
        htmlFor="fileInput"
        className="cursor-pointer flex flex-col items-center"
      >
        <span className="text-gray-600 mb-2">Перетащите файл сюда</span>
        <span className="text-indigo-600 font-semibold">
          или выберите файл
        </span>
      </label>

      {file && (
        <p className="text-gray-700 mt-3 text-sm">
          Вы выбрали: <span className="font-medium">{file.name}</span>
        </p>
      )}

      <button
        type="submit"
        disabled={!file}
        className={`mt-5 px-6 py-2 rounded-lg font-semibold transition ${
          file
            ? "bg-indigo-600 text-white hover:bg-indigo-700"
            : "bg-gray-300 text-gray-600 cursor-not-allowed"
        }`}
      >
        Конвертировать
      </button>
    </form>
  );
}
