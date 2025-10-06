import React, { useState } from "react";
import FileUpload from "./components/FileUpload";
import ResultCard from "./components/ResultCard";
import Loader from "./components/Loader";

export default function App() {
  const [isLoading, setIsLoading] = useState(false);
  const [pdfBlob, setPdfBlob] = useState(null);
  const [error, setError] = useState(null);

  const handleUpload = async (file) => {
    if (!file) return;

    setIsLoading(true);
    setError(null);
    setPdfBlob(null);

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("/upload", {
        method: "POST",
        body: formData,
      });

      if (!response.ok) {
        throw new Error(`Ошибка: ${response.status}`);
      }

      const blob = await response.blob();
      setPdfBlob(blob);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-100 via-indigo-100 to-purple-100 flex flex-col items-center justify-center p-6">
      <div className="bg-white shadow-2xl rounded-2xl p-8 w-full max-w-md">
        <h1 className="text-2xl font-bold text-gray-800 text-center mb-6">
          📄 File → PDF Converter
        </h1>

        {isLoading && <Loader />}
        {!isLoading && !pdfBlob && <FileUpload onUpload={handleUpload} />}
        {!isLoading && pdfBlob && <ResultCard blob={pdfBlob} />}
        {error && <p className="text-red-600 text-center mt-4">{error}</p>}
      </div>
      <p className="text-gray-500 mt-6 text-sm">© 2025 Corbit Converter</p>
    </div>
  );
}
